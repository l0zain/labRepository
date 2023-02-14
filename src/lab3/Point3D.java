package lab3;

import java.util.Objects;

public class Point3D extends Point2D{
    private double z;

    public Point3D(double x, double y, double z) {
        super(x, y);
        this.z = z;
    }
    public Point3D(){
        this(0, 0, 0);
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getZ() {
        return z;
    }
    public  double[] distanceTo(Point3D[] point){
        double[] len = new double[3];
        int k = 0; // index
        for (int i = 0; i < 3 - 1; i ++){ // last value
            for(int j = i + 1; j < 3; j ++, k++) { // next value
                len[k] = Math.sqrt(Math.pow(point[j].getX() - point[i].getX(), 2) +
                        Math.pow(point[j].getY() - point[i].getY(), 2) +
                        Math.pow(point[j].getZ() - point[i].getZ(), 2));
            }
        }
        return len;
    }
    public double computeArea(double[] array){
        double summ = 0;
        for (double arr : array) {
            summ += arr;
        }
        double p = summ / 2;
        double square = Math.sqrt(p * (p - array[0]) * (p - array[1]) * (p - array[2]));
        return square;
    }

    @Override // переопределяем для сравнения объектов
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point3D point3D = (Point3D) o;
        return Double.compare(point3D.getX(), getX()) == 0 && Double.compare(point3D.getY(), getY()) == 0 && Double.compare(point3D.getZ(), getZ()) == 0;
    }


    @Override
    public int hashCode() {
        return Objects.hash(getX()) + Objects.hash(getY()) + Objects.hash(getZ());
    }
}

