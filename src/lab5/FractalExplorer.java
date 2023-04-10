package lab5;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;

/**
 * Класс, необходимый для отображеия графического интерфейса
 * и обработки действий пользователя
 */
public class FractalExplorer {
    /** Ширина и высота отображения - целочисленная, поэтому int **/
    private int displaySize;
    /** JImageDisplay для обновления отображения изображения **/
    private JImageDisplay display;
    /** Объект для разных типов фракталов **/
    private FractalGenerator fractal;
    /**
     * Объект, определяющий размер текущего диапозона просмотра
     * То, что показывается в данный момент
     **/
    private Rectangle2D.Double range;
    /**
     * Конструктор, принимающий размер дисплея и сохраняющий его,
     * после чего инициализирующий объекты диспозона и генератора фрактала
     **/
    //конструктор
    public FractalExplorer(int size) {
        displaySize = size;
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);
    }

    /**
     * Метод инициализирует графический интерфейс Swing и кнопку для сброса
     **/
    //метод для инициализации графического интерфейса Swing
    public void createAndShowGUI() {
        display.setLayout(new BorderLayout());
        JFrame myFrame = new JFrame("Fractal Explorer");
        // Добавление и центровки объекта изображения
        myFrame.add(display, BorderLayout.CENTER);
        // Создание объекта кнопки сброса
        JButton resetButton = new JButton("Reset Display");
        ResetHandler handler = new ResetHandler();
        // Обработка события нажатия на кнопку
        resetButton.addActionListener(handler);
        myFrame.add(resetButton, BorderLayout.SOUTH);
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setTitle("That's the fractal");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        myFrame.setBounds(dimension.width/2 - 300,dimension.height/2 - 300, 600, 600);
        myFrame.pack();
        myFrame.setVisible(true);
        myFrame.setResizable(false);
    }
    /**
     * Вспомогательный метод для отображения фрактала.
     * Он проходит по пикселям на дисплее и вычисляет количество итераций для координат в фрактале
     * Если кол-во итераций = -1, он устанавливает чёрный цвет пикселя
     * иначе же выбирает значение в зависимости от количества итераций.
     * Когда работа выполнена - обновляет дисплей
     * **/
    //метод для вывода фрактала на экран
    private void drawFractal() {
        for (int x = 0; x < displaySize; x++) {
            for (int y = 0; y < displaySize; y++) {
                double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);
                double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);
                int iteration = fractal.numIterations(xCoord, yCoord);
                if (iteration == -1) {
                    display.drawPixel(x, y, 0);
                }
                else {
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    display.drawPixel(x, y, rgbColor);
                }
            }
        }
        display.repaint();
    }

    //внутренний класс для обработки событий ActionListener от кнопки сброса
    private class ResetHandler implements ActionListener
    {
        //метод для сброса до начального диапазона
        public void actionPerformed(ActionEvent e)
        {
            fractal.getInitialRange(range);
            drawFractal();
        }
    }

    //внутренний класс для обработки событий MouseListener с дисплея
    private class MouseHandler extends MouseAdapter {
        /**
         * Когда происходит нажатие мышкой, перемещается на указанные щелчком координаты.
         * Приближение вполовину от нынешнего
         * **/
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x,
                    range.x + range.width, displaySize, x);
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y,
                    range.y + range.height, displaySize, y);

            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }


    //метод для запуска FractalExplorer
    public static void main(String[] args) {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}