import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Dzmitry on 04.07.2017.
 */
public class ControllerImageProc {
    private ViewImageProc theView;
    private ModelImageProc theModel;
    private Histogram theHistogram;

    public ControllerImageProc(ViewImageProc theView, ModelImageProc theModel, Histogram theHistogram) {
        this.theView = theView;
        this.theModel = theModel;
        this.theHistogram = theHistogram;
        this.theView.getJMenuBar().getMenu(0).getItem(1).setEnabled(false); //save file
        this.theView.getJMenuBar().getMenu(0).getItem(2).setEnabled(false); //close file
        this.theView.getJMenuBar().getMenu(1).getItem(0).setEnabled(false); // contrast
        this.theView.getJMenuBar().getMenu(1).getItem(1).setEnabled(false); // roberts
        this.theView.getJMenuBar().getMenu(1).getItem(3).setEnabled(false); // sepia
        this.theView.getJMenuBar().getMenu(2).getItem(0).setEnabled(false); //first histogram
        this.theView.getJMenuBar().getMenu(2).getItem(1).setEnabled(false); //second histogram

        this.theView.openItemListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (theModel.getForSavingImage() != null) {
                    theView.getJMenuBar().getMenu(0).getItem(1).setEnabled(false); //save file
                    theView.getJMenuBar().getMenu(1).getItem(0).setEnabled(false); // contrast
                    theView.getJMenuBar().getMenu(1).getItem(1).setEnabled(false); // roberts
                    theView.getJMenuBar().getMenu(1).getItem(3).setEnabled(false); // sepia
                    theView.getJMenuBar().getMenu(2).getItem(1).setEnabled(false); //second histogram
                }
                theModel.setOpenedFile(theView.openFile());
                theModel.setOpenedImage(theModel.fileToImageBuffered(theModel.getOpenedFile()));
                if (theModel.getOpenedFile() != null) {
                    theView.setLabel(theModel.getOpenedImage(), theView.getLabelImage1());
                    theView.setTitle(theView.getTitle() + ": " + theModel.getOpenedFile().getName().toString());
                    theView.getLabelImage2().setIcon(null);
                    theView.getJMenuBar().getMenu(0).getItem(2).setEnabled(true); //close file
                    theView.getJMenuBar().getMenu(1).getItem(0).setEnabled(true); // contrast
                    theView.getJMenuBar().getMenu(1).getItem(1).setEnabled(true); // roberts
                    theView.getJMenuBar().getMenu(1).getItem(3).setEnabled(true); // sepia
                    theView.getJMenuBar().getMenu(2).getItem(0).setEnabled(true); //first histogram
                }
            }
        });

        this.theView.saveItemListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theModel.saveImageBufferedToFile(theView.saveFile(), theModel.getForSavingImage());
            }
        });

        this.theView.closeItemListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theView.setTitle("Image Processing");
                theView.getLabelImage1().setIcon(null);
                theView.getLabelImage2().setIcon(null);
                theModel.setOpenedImage(null);
                theModel.setOpenedFile(null);
                theModel.setForSavingImage(null);
                theModel.setForSavingFile(null);
                theView.getJMenuBar().getMenu(0).getItem(1).setEnabled(false); //save file
                theView.getJMenuBar().getMenu(0).getItem(2).setEnabled(false); //close file
                theView.getJMenuBar().getMenu(1).getItem(0).setEnabled(false); // contrast
                theView.getJMenuBar().getMenu(1).getItem(1).setEnabled(false); // roberts
                theView.getJMenuBar().getMenu(1).getItem(3).setEnabled(false); // sepia
                theView.getJMenuBar().getMenu(2).getItem(0).setEnabled(false); //first histogram
                theView.getJMenuBar().getMenu(2).getItem(1).setEnabled(false); //second histogram
            }
        });

        this.theView.linearContrastItemListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int gMin = Integer.parseInt(JOptionPane.showInputDialog(theView, "Enter 'Gmin' value (0 - 255)"));
                            int gMax = Integer.parseInt(JOptionPane.showInputDialog(theView, "Enter 'Gmax' value (0 - 255)"));
                            theModel.setForSavingImage(theModel.linearContrast(theModel.getOpenedImage(), gMin, gMax));
                            theView.setLabel(theModel.getForSavingImage(), theView.getLabelImage2());
                            theView.getJMenuBar().getMenu(0).getItem(1).setEnabled(true); //save file
                            theView.getJMenuBar().getMenu(2).getItem(1).setEnabled(true); //second histogram
                        } catch (NumberFormatException e) {
                            System.err.println(e);
                            JOptionPane.showConfirmDialog(theView, "Uncorrect value! " + e.getLocalizedMessage(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }).start();
            }
        });

        this.theView.robertsOperatorItemListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            theModel.setForSavingImage(theModel.operatorRoberts(theModel.getOpenedImage()));
                            theView.setLabel(theModel.getForSavingImage(), theView.getLabelImage2());
                            theView.getJMenuBar().getMenu(0).getItem(1).setEnabled(true); //save file
                            theView.getJMenuBar().getMenu(2).getItem(1).setEnabled(true); //second histogram
                        } catch (NumberFormatException e) {
                            System.err.println(e);
                        }
                    }
                }).start();

            }
        });

        this.theView.sepiaItemListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int sepiaDept = Integer.parseInt(JOptionPane.showInputDialog(theView, "Enter value of dept (0 - 50). Recommended: 20."));
                            theModel.setForSavingImage(theModel.sepia(theModel.getOpenedImage(), sepiaDept));
                            theView.setLabel(theModel.getForSavingImage(), theView.getLabelImage2());
                            theView.getJMenuBar().getMenu(0).getItem(1).setEnabled(true); //save file
                            theView.getJMenuBar().getMenu(2).getItem(1).setEnabled(true); //second histogram
                        } catch (NumberFormatException e) {
                            System.err.println(e);
                            JOptionPane.showConfirmDialog(theView, "Uncorrect value! " + e.getLocalizedMessage(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }).start();
            }
        });

        this.theView.histogramFirstItemListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        theHistogram.showHistogram(theModel.getOpenedImage(), "Original " + theModel.getOpenedFile().getName());
                    }
                });
            }
        });

        this.theView.histogramSecondItemListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        theHistogram.showHistogram(theModel.getForSavingImage(), "Processed " + theModel.getOpenedFile().getName());
                    }
                });
            }
        });
    }

}
