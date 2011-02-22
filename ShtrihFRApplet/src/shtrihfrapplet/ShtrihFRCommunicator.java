package shtrihfrapplet;

/**
 *
 * @author assargadon
 */

//import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

//import java.io.FileDescriptor;
//import java.io.IOException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.logging.Level;
//import java.util.logging.Logger;

public class ShtrihFRCommunicator {

    public static final byte STX   = 2;
    public static final byte ENQ   = 5;
    public static final byte ACK   = 6;
    public static final byte NAK   = 21;

    private static final int OPEN_PORT_TIMEOUT = 100;  //0,1 sec

    private static final int MAX_POSSIBLE_MESSAGE = 260; //message are limited by 256 due to length byte. And then 3 more bytes for STX, length byte and LRC (checksum). And one mo for reserve and to reach "round" value.

    SerialPort port;
    InputStream in;
    OutputStream out;

    public int calcCheckSumCRC(int[] data) {
        int isum = 0;
        for (int i = 0; i < data.length; i++) {
            isum = (isum ^ data[i]);
        }
        return 255 & isum; //mask to select only one byte
    }

    public int[] encode(int[] msg){
        int[] res=new int[msg.length+3];
        res[1]=msg.length;
        System.arraycopy(msg, 0, res, 2, msg.length);
        res[msg.length+2]=calcCheckSumCRC(res); //checksum should include lenght byte, and zeros do not affect it
        res[0]=STX;
        return res;
    }

    private void printByteDebug(int data){
        switch (data){
            case STX: System.out.println("STX"); break;
            case ENQ: System.out.println("ENQ"); break;
            case ACK: System.out.println("ACK"); break;
            case NAK: System.out.println("NAK"); break;
            case  -1: System.out.println("TIMEOUT"); break;
            default: System.out.println(Integer.toHexString(data)); break;
        }
    }
    private void writeByte(int data) throws IOException{
        out.write(data);
        System.out.print("FR<===");
        printByteDebug(data);
    }
    private synchronized int readByteWithTimeout(int timeout_ms){ // 'synchronized' needed to correctly call wait()
    //Trying to read one byte from this.in, and return this byte, or, if timeout was reached, return -1.
    int time_granularity=50;
    int timeout=timeout_ms;
        try
        {
            while(in.available()<=0 && timeout>0)
            {
                wait(time_granularity); //I beleive there is better way to perform timeout reading, but I have no time for searching, unfortunately
                timeout-=time_granularity;
            }

            if(in.available()>0){
                int data=in.read();
                System.out.print("FR===>");
                printByteDebug(data);
                return data;
            }else{
                System.out.print("FR===>");
                printByteDebug(-1);
                return -1;
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return -1;
        }
    }

    private int[] receiveAnswer() throws IOException{

        int[] buffer = new int[MAX_POSSIBLE_MESSAGE];
        int[] res = null;
        int cursor = 0;
        int len;

        if(readByteWithTimeout(1000)!=STX){
            System.out.println("No STX in FR answer");
            return null;
        }

        if((len=readByteWithTimeout(1000))==-1){
            System.out.println("Length byte timeout in FR answer");
            return null;
        }

        int data = -1;
        buffer[0]=len;
        for(cursor=0; cursor<len; cursor++){
            data = readByteWithTimeout(1000);
            if(data==-1){
                System.out.println("Message byte timeout in FR answer");
                return null;
            }
            buffer[cursor+1]=data;
        }

        int checksum = readByteWithTimeout(1000);
        if(checksum==-1){
            System.out.println("Checksum timeout in FR answer");
            return null;
        }
        if(checksum!=calcCheckSumCRC(buffer)){
            System.out.println("Checksum WRONG in FR answer, have "+Integer.toHexString(checksum)+", need "+Integer.toHexString(calcCheckSumCRC(buffer)));
            writeByte(NAK);
            return null;
        }
        buffer[len+1]=checksum;
        writeByte(ACK);

        res = new int[len+2];
        System.arraycopy(buffer, 0, res, 0, len+2);
        return res;
    }


    private void sendMessage(int[] msg){
        try {
            for(int i=0; i<msg.length; i++)
                writeByte((byte) msg[i]);
            out.flush();

        } catch (IOException ex) {
            Logger.getLogger(ShtrihFRCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public int[] sendAndWaitForAnswer(int[] msg){
        int[] res = null;
        boolean answer_flag=false;

        try {

        if (connect("/dev/ttyUSB0") == 0){
             System.out.println("Failed to open port");
             return null; //error while opening
        }
        main: while (true)
        {

            writeByte(ENQ);

            switch (readByteWithTimeout(1000)){

                case -1:
                    System.out.println("No connect or device in process of transmitting answer on previous message");
                    return null;

                case NAK:
                    int i;
                    for(i=0;i<10;i++)
                    {
                        sendMessage(encode(msg));
                        int confirm=readByteWithTimeout(1000);
                        if (confirm==ACK) break;
                        if (confirm==-1){
                            System.out.println("Timeout on receiving confirmation from FR");
                            return null;
                        }
                        
                    }
                    if(i>=10){
                        System.out.println("No receiving confirmation from FR");
                        return null;
                    }else{answer_flag=true;}
                    //MIND: no break, continue execution of next case

                case ACK:
                    for(i=0;i<10;i++)
                    {
                        if (i>0){ //We have failed to read answer on first attempt, so we need to send ENQ again
                            writeByte(ENQ);
                            if (readByteWithTimeout(1000)!=ACK) continue; //if we receive NOT ASC, then try to ENQ one time more, decreasing attempts counter wia FOR statement
                        }
                        res = receiveAnswer();
                        if (res!=null)
                            if (answer_flag)
                                break main; //We have answer to OUR message
                            else
                                continue main; //We have answer, but it's answer to some other, not our message, for example by abandoned previous attempt
                    }
                    if(i>=10){
                        System.out.println("Can't receive answer from FR");
                        return null;
                    }

            }
            wait(1000);
        }

        disconnect();

        } catch (Exception ex) {
            disconnect();
            return null;
        }
        return res;
        //return null;
    }

    int connect ( String portName ) throws Exception
    {
        CommPortIdentifier commPortIdentifier;
        commPortIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (commPortIdentifier.isCurrentlyOwned()) return 0;//Port is currently in use

        port = (SerialPort) commPortIdentifier.open("ShtrihFR", OPEN_PORT_TIMEOUT);
        //Timeout should be managed this or that way

        port.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

        in = port.getInputStream();
        out = port.getOutputStream();

        return 1; //Everything OK
    }

    void disconnect()
    {
        try {
            in.close();
            out.close();
            port.close();
        } catch (IOException ex) {
            Logger.getLogger(ShtrihFRCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}


/*try
{
    // Using Thread.sleep() we can add delay in our application in
    // a millisecond time. For the example below the program will
    // take a deep breath for one second before continue to print
    // the next value of the loop.
    Thread.sleep(1000);
    // The Thread.sleep() need to be executed inside a try-catch
    // block and we need to catch the InterruptedException.
} catch (InterruptedException ie)
{
    ie.printStackTrace();
}*/