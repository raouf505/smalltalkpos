package shtrihfrapplet;
/**
 *
 * @author assargadon
 */

import java.awt.*;
import java.applet.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.AccessController;
import java.security.PrivilegedAction;
import netscape.javascript.JSObject;


public class ShtrihFRApplet extends Applet
    implements FocusListener, MouseListener{
    int[] message, answer;
    String callback;
    ShtrihFRCommunicator communicator = new ShtrihFRCommunicator();
    int err=0; //if <0 - no error, if >0 - error, if ==0 - "neutral", waiting to do something to determine if there are any error or not

    private Image waitImage;
    private Image errorImage;
    private Image okImage;
    private String appletName;

    @Override
    public void init(){
        String tmp;

        tmp=getParameter("appletName");
        if (tmp!=null) appletName = tmp; else appletName="ShtrihFRApplet";

        tmp=getParameter("portName");
        if (tmp!=null) communicator.portName = tmp; else communicator.portName="COM1";
        
        tmp=getParameter("baudRate");
        if (tmp!=null) communicator.baudRate = Integer.parseInt(tmp); else communicator.baudRate=9600;

        tmp=getParameter("databits");
        if (tmp!=null) communicator.databits = Integer.parseInt(tmp);

        tmp=getParameter("stopbits");
        if (tmp!=null) communicator.stopbits = Integer.parseInt(tmp);

        tmp=getParameter("parity");
        if (tmp!=null) communicator.parity = Integer.parseInt(tmp);

        waitImage = getImage(ShtrihFRApplet.class.getResource("res/yellow-led.png"));
        errorImage = getImage(ShtrihFRApplet.class.getResource("res/red-led.png"));
        okImage = getImage(ShtrihFRApplet.class.getResource("res/green-led.png"));

        addFocusListener(this);
        addMouseListener(this);
    }

  

    @Override
    public void paint(Graphics g) {
        if(err>0) {g.drawImage(errorImage, 0, 0, this); return;}
        if(err<0) {g.drawImage(okImage, 0, 0, this); return;}
        g.drawImage(waitImage, 0, 0, this);
    }

    public int[] sendToPort(int[] msg){
       message=msg;

       //We need this, because if we call this method from javascript on page, 
       //more restrictive permissions will be used, effectively blocking as from
       //writing to port. So we need to perform privileged action.
       AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                answer=communicator.sendAndWaitForAnswer(message);
                return null; // nothing to return
            }
        });

        if(answer==null) err=1; else err=-1;
        repaint();
        return answer;
    }

    //This focus trics are needed, because in Windows environment, scrolling will be blocked
    //it's some strange problem, when focus are locked on the applet and never leaves it anymore...or something like this
    public void focusGained(FocusEvent fe)
    {
//        System.out.println("Focus gained...");
//        System.out.println("Try to BLUR...");
        JSObject.getWindow(this).eval((new StringBuilder()).append("document.getElementById(\"").append(getParameter("appletId")).append("\").blur()").toString());
    }

    public void focusLost(FocusEvent fe)
    {
//        System.out.println("Focus lost...");
    }
    //----------------------------------------------------------



// This is used instead a tooltip (which is not accessible for applets in HTML)
// when you drags the mouse over the applet, it's name will be displayed in browser's status bar
    public void mouseEntered(MouseEvent arg0)
    {
//        System.out.println("Mouse entered...");
        showStatus(appletName);
    }

    public void mouseExited(MouseEvent arg0)
    {
//        System.out.println("Mouse exited...");
        showStatus("");
    }
    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }
    //----------------------------------------------------------

}