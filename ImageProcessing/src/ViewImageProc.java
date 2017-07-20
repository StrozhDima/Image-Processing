import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicMenuBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Dzmitry on 30.06.2017.
 */
public class ViewImageProc extends JFrame {
    private JMenuBar menuBar;
    private JLabel labelImage1;
    private JLabel labelImage2;
    private ImagePreviewPanel previewPanel = new ImagePreviewPanel();

    public ViewImageProc(String tittle) {
        super(tittle);
        ImageIcon icon = new ImageIcon("img/icon.png");
        setIconImage(icon.getImage());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel(new BorderLayout());

        this.menuBar = new JMenuBar();
        menuBar.setUI ( new BasicMenuBarUI(){
            public void paint ( Graphics g, JComponent c ){
                g.setColor (new Color(70, 180, 255));
                g.fillRect ( 0, 0, c.getWidth (), c.getHeight () );
            }
        } );

        this.labelImage1 = new JLabel();
        this.labelImage2 = new JLabel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //при нажатии крестика программа завершит свою работу
        setLayout(new BorderLayout());
        setUndecorated(false); //отобразить или нет верхнюю строку окна
        setPreferredSize(new Dimension(800, 600));
        setExtendedState(JFrame.MAXIMIZED_BOTH); //размер окна

        panel.add(this.labelImage1, BorderLayout.LINE_START);
        panel.add(this.labelImage2, BorderLayout.LINE_END);

        panel.setBackground(new Color(64, 164, 214));

        add(panel);
        initFileMenuBar(this.menuBar);
        initEditMenuBar(this.menuBar);
        initHistogramMenuBar(this.menuBar);

        setJMenuBar(this.menuBar);

        pack();
        setVisible(true); //отобразить форму
    }

    private void initFileMenuBar(JMenuBar jMenuBar) {
        Font font = new Font("MV Boli", Font.PLAIN, 16);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(font);

        JMenuItem openItem = new JMenuItem("Open");
        openItem.setFont(font);
        fileMenu.add(openItem);

        JMenuItem saveItem = new JMenuItem("Save processed file");
        saveItem.setFont(font);
        fileMenu.add(saveItem);

        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.setFont(font);
        fileMenu.add(closeItem);

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setFont(font);
        fileMenu.add(exitItem);

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        jMenuBar.add(fileMenu);
    }

    private void initEditMenuBar(JMenuBar jMenuBar) {
        Font font = new Font("MV Boli", Font.PLAIN, 16);

        JMenu editMenu = new JMenu("Edit");
        editMenu.setFont(font);

        JMenuItem linearContrastItem = new JMenuItem("Linear contrast");
        linearContrastItem.setFont(font);
        editMenu.add(linearContrastItem);

        JMenuItem robertsOperatorItem = new JMenuItem("Roberts operator");
        robertsOperatorItem.setFont(font);
        editMenu.add(robertsOperatorItem);

        editMenu.addSeparator();

        JMenuItem brightnessItem = new JMenuItem("Sepia effect");
        brightnessItem.setFont(font);
        editMenu.add(brightnessItem);

        jMenuBar.add(editMenu);
    }

    private void initHistogramMenuBar(JMenuBar jMenuBar) {
        Font font = new Font("MV Boli", Font.PLAIN, 16);

        JMenu editMenu = new JMenu("Histograms");
        editMenu.setFont(font);

        JMenuItem linearContrastItem = new JMenuItem("Opened image");
        linearContrastItem.setFont(font);
        editMenu.add(linearContrastItem);

        JMenuItem robertsOperatorItem = new JMenuItem("Processed image");
        robertsOperatorItem.setFont(font);
        editMenu.add(robertsOperatorItem);

        jMenuBar.add(editMenu);
    }

    public void openItemListener(ActionListener listenForOpenItem) {
        this.menuBar.getMenu(0).getItem(0).addActionListener(listenForOpenItem);
    }

    public void saveItemListener(ActionListener listenForSaveItem) {
        this.menuBar.getMenu(0).getItem(1).addActionListener(listenForSaveItem);
    }

    public void closeItemListener(ActionListener listenForCloseItem) {
        this.menuBar.getMenu(0).getItem(2).addActionListener(listenForCloseItem);
    }

    public void linearContrastItemListener(ActionListener listenForLinearContrastItem) {
        this.menuBar.getMenu(1).getItem(0).addActionListener(listenForLinearContrastItem);
    }

    public void robertsOperatorItemListener(ActionListener listenForRobertsOperatorItem) {
        this.menuBar.getMenu(1).getItem(1).addActionListener(listenForRobertsOperatorItem);
    }

    public void sepiaItemListener(ActionListener listenForSepiaItem) {
        this.menuBar.getMenu(1).getItem(3).addActionListener(listenForSepiaItem);
    }

    public void histogramFirstItemListener(ActionListener listenForHistogramFirstItem) {
        this.menuBar.getMenu(2).getItem(0).addActionListener(listenForHistogramFirstItem);
    }

    public void histogramSecondItemListener(ActionListener listenForHistogramSecondItem) {
        this.menuBar.getMenu(2).getItem(1).addActionListener(listenForHistogramSecondItem);
    }

    public File saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAccessory(previewPanel);
        fileChooser.addPropertyChangeListener(previewPanel);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));
        fileChooser.setAcceptAllFileFilterUsed(true);

        File file = new File("");
        if (fileChooser.showSaveDialog(getContentPane()) == 0)
            file = fileChooser.getSelectedFile();
        return file;
    }

    public File openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAccessory(previewPanel);
        fileChooser.addPropertyChangeListener(previewPanel);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));
        fileChooser.setAcceptAllFileFilterUsed(true);

        File file = new File("");
        if (fileChooser.showOpenDialog(getContentPane()) == 0)
            file = fileChooser.getSelectedFile();
        return file;
    }

    public void setLabel(BufferedImage image, JLabel label) {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();
        int boundWidth = getContentPane().getWidth() / 2;
        int boundHeight = getContentPane().getHeight();
        int newWidth = originalWidth;
        int newHeight = originalHeight;

        if (originalWidth > boundWidth) {
            newWidth = boundWidth;
            newHeight = (newWidth * originalHeight) / originalWidth;
        }

        if (newHeight > boundHeight) {
            newHeight = boundHeight;
            newWidth = (newHeight * originalWidth) / originalHeight;
        }

        label.setIcon(new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)));
        getContentPane().repaint();
    }

    public JLabel getLabelImage1() {
        return labelImage1;
    }

    public JLabel getLabelImage2() {
        return labelImage2;
    }

}

