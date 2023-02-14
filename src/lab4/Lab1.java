package lab4;

public class Lab1 {
    public static void main(String[] args) {
        Point3D point3D1 = new Point3D();
        Point3D[] point3D = {new Point3D(1, -1, 1), new Point3D(0, -3, 1), new Point3D(7, 8, 1)};
        if (point3D[0].equals(point3D[1]) || point3D[1].equals(point3D[2]) || point3D[0].equals(point3D[2])) {
            System.out.println("Введите другие координаты");
            return;
        }
        System.out.println(Math.round(point3D1.computeArea(point3D1.distanceTo(point3D)) * 10.0) / 10.0);

    }

}
