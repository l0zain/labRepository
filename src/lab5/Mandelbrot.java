package lab5;
import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {
    //константа с максимальным количеством итераций
    public static final int MAX_ITERATIONS = 2000;

    //метод для определения области для конкретного фрактала
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    //метод реализации функции для фрактала Мандельброта
    public int numIterations(double x, double y) {
        double zr = 0;
        double zi = 0;
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            double zrNew = zr * zr - zi * zi + x;
            double ziNew = 2 * zr * zi + y;
            if ((zr * zr + zi * zi) > 4)
                return i;
            zr = zrNew;
            zi = ziNew;
        }
        return -1;
    }
}
