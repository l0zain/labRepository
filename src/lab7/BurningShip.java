package lab7;
import java.awt.geom.Rectangle2D;

public class BurningShip extends FractalGenerator
{
    //константа с максимальным количеством итераций
    public static final int MAX_ITERATIONS = 2000;

    //метод для определения области для конкретного фрактала
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    //метод реализации функции для фрактала Burning Ship
    public int numIterations(double x, double y) {
        int i = 0;
        double zr = 0;
        double zi = 0;
        while (i < MAX_ITERATIONS && zr*zr + zi*zi < 4) {
            double zrNew = zr*zr - zi*zi + x;
            double ziNew= 2 * Math.abs(zr) * Math.abs(zi) + y;
            zr = zrNew;
            zi = ziNew;
            i += 1;
        }
        if (i == MAX_ITERATIONS) {
            return -1;
        }
        return i;
    }

    //метод, который возвращает имя фрактала
    public String toString() {
        return "Burning Ship";
    }
}