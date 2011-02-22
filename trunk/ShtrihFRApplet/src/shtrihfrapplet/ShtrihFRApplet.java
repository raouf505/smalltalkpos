package shtrihfrapplet;
/**
 *
 * @author assargadon
 */

import java.awt.*;
import java.applet.*;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.security.AccessController;
import java.security.PrivilegedAction;


public class ShtrihFRApplet extends Applet {
    int[] message, answer;
    String callback;
    ShtrihFRCommunicator communicator = new ShtrihFRCommunicator();
    int err=-1;


    @Override
    public void paint(Graphics g) {
        g.drawString("Applet initiated!", 20, 20);
        g.drawString(Integer.toString(err), 20, 40);
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

        repaint();
        return answer;
    }
}