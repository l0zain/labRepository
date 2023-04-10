package lab6;
import javax.swing.*;
import java.awt.image.*;
import java.awt.*;

//класс для отображения фракталов
class JImageDisplay extends JComponent {
    //управление изображением, содержимое которого можно записать
    private BufferedImage displayImage;

    //конструктор для инициализации объекта BufferedImage новым изображением
    public JImageDisplay(int width, int height) {
        displayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //метод для отображения на экране изображения
        Dimension imageDimension = new Dimension(width, height);
        super.setPreferredSize(imageDimension);
    }

    //вызываем метод суперкласса paintComponent(g), чтобы объекты отображались правильно
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(displayImage, 0, 0, displayImage.getWidth(), displayImage.getHeight(), null);
    }

    //метод для установки всех пикселей черного цвета
    public void clearImage() {
        int[] blankArray = new int[getWidth() * getHeight()];
        displayImage.setRGB(0, 0, getWidth(), getHeight(), blankArray, 0, 1);
    }

    //метод для установки пикселя в определенный цвет
    public void drawPixel(int x, int y, int rgbColor) {

        displayImage.setRGB(x, y, rgbColor);
    }
    //метод для получения изображения из другого класса
    public BufferedImage getDisplayImage() {
        return displayImage;
    }

}

