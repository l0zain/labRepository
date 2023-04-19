package lab7;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.filechooser.*;
import javax.swing.JOptionPane;

public class FractalExplorer {
    private int rowsRemaining; // завершена ли перерисовка
    private int displaySize;
    private JImageDisplay display;
    private FractalGenerator fractal;
    private Rectangle2D.Double range;
    private JButton saveButton;
    private JButton resetButton;
    private JComboBox myComboBox;

    //конструктор
    public FractalExplorer(int size) {
        displaySize = size;
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);
    }

    //метод для инициализации графического интерфейса Swing
    public void createAndShowGUI() {
        display.setLayout(new BorderLayout());
        JFrame myFrame = new JFrame("Fractal Explorer");
        myFrame.add(display, BorderLayout.CENTER);
        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ButtonHandler());
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myComboBox = new JComboBox();
        FractalGenerator MandelbrotFractal = new Mandelbrot();
        myComboBox.addItem(MandelbrotFractal);
        FractalGenerator TricornFractal = new Tricorn();
        myComboBox.addItem(TricornFractal);
        FractalGenerator BurningShipFractal = new BurningShip();
        myComboBox.addItem(BurningShipFractal);
        ButtonHandler fractalChooser = new ButtonHandler();
        myComboBox.addActionListener(fractalChooser);
        JPanel myPanel = new JPanel();
        JLabel myLabel = new JLabel("Fractal:");
        myPanel.add(myLabel);
        myPanel.add(myComboBox);
        myFrame.add(myPanel, BorderLayout.NORTH);
        saveButton = new JButton("Save");
        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(saveButton);
        myBottomPanel.add(resetButton);
        myFrame.add(myBottomPanel, BorderLayout.SOUTH);
        ActionListener saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);

        myFrame.setTitle("That's the fractal");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        myFrame.setBounds(dimension.width/2 - 300,dimension.height/2 - 300, 600, 600);
        myFrame.pack();
        myFrame.setVisible(true);
        myFrame.setResizable(false);
    }

    //внутренний класс для обработки событий ActionListener
    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();
            if (e.getSource() instanceof JComboBox) {
                JComboBox mySource = (JComboBox) e.getSource();
                fractal = (FractalGenerator) mySource.getSelectedItem();
                fractal.getInitialRange(range);
                drawFractal();
            }
            else if (action.equals("Reset")) {
                fractal.getInitialRange(range);
                drawFractal();
            }
            else if (action.equals("Save")) {
                JFileChooser myFileChooser = new JFileChooser();
                FileFilter extensionFilter = new FileNameExtensionFilter("PNG Images", "png");
                myFileChooser.setFileFilter(extensionFilter);
                myFileChooser.setAcceptAllFileFilterUsed(false);

                int userSelection = myFileChooser.showSaveDialog(display);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    java.io.File file = myFileChooser.getSelectedFile();
                    if (!file.getAbsolutePath().endsWith(".png")){
                        file = new java.io.File(myFileChooser.getSelectedFile()+".png");
                    }
                    try {
                        BufferedImage displayImage = display.getDisplayImage();
                        javax.imageio.ImageIO.write(displayImage, "png", file);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(display, exception.getMessage() + exception.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else return;
            }
        }
    }

    //метод для отображения фрактала
    private void drawFractal() {
        //отключаем все элементы пользовательского интерфейса во время рисования
        enableUI(false);
        //устанавливаем значение равное общему количеству строк, которые нужно нарисовать
        rowsRemaining = displaySize;
        //на каждой строке дисплея вычисляем значения цвета
        for (int x = 0; x < displaySize; x++) {
            FractalWorker drawRow = new FractalWorker(x);
            drawRow.execute();
        }
    }

    //метод для включения/отключения кнопки интерфейса и поля со списком в зависимости от заданного значения
    private void enableUI(boolean value) {
        myComboBox.setEnabled(value);
        resetButton.setEnabled(value);
        saveButton.setEnabled(value);
    }

    //внутренний класс для обработки событий MouseListener с дисплея
    private class MouseHandler extends MouseAdapter {
        //класс для отображения пиксельных кооринат щелчка в области фрактала
        @Override
        public void mouseClicked(MouseEvent e) {
            if (rowsRemaining != 0) {
                return;
            }
            //получаем координаты x и у области отображения щелчка мыши
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x,range.x + range.width, displaySize, x);
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y,range.y + range.height, displaySize, y);
            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }

    //класс для вычисления значений цвета одной строки фрактала
    private class FractalWorker extends SwingWorker<Object, Object> {
        int yCoordinate;
        int[] arrayRGB; //массив чисел типа int для хранения вычисленных значений RGB для каждого пикселя в этой строке

        //конструктор принимает и соохраняет координату y
        private FractalWorker(int row) {
            yCoordinate = row;
        }

        //вызывается в фоновом потоке для выполнения длительной задачи
        protected Object doInBackground() {
            arrayRGB = new int[displaySize];
            //цикл для сохранения каждого значения RGB в соответствующем элементе массива
            for (int i = 0; i < arrayRGB.length; i++) {
                double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, i);
                double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, yCoordinate);
                int iteration = fractal.numIterations(xCoord, yCoord);
                if (iteration == -1) {
                    arrayRGB[i] = 0;
                }
                else {
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    arrayRGB[i] = rgbColor;
                }
            }
            return null;
        }

        //метод для отрисовки пикселей текущей строки и обновления отображения
        //вызывается после завершения фоновой задачи
        protected void done() {
            for (int i = 0; i < arrayRGB.length; i++) {
                display.drawPixel(i, yCoordinate, arrayRGB[i]);
            }
            display.repaint(0, 0, yCoordinate, displaySize, 1);
            rowsRemaining--;
            if (rowsRemaining == 0) {
                enableUI(true);
            }
        }
    }

    //метод для запуска FractalExplorer
    public static void main(String[] args) {
        FractalExplorer displayExplorer = new FractalExplorer(500);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}