import javax.swing.*;

/**
 * Created by Dzmitry on 04.07.2017.
 */
public class MVCImageProc {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ViewImageProc theView = new ViewImageProc("Image Processing");
                ModelImageProc theModel = new ModelImageProc();
                Histogram theHistogram = new Histogram();
                new ControllerImageProc(theView, theModel, theHistogram);
            }
        });
    }
}
