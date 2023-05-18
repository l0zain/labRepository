package lab9;
import java.net.MalformedURLException;
import java.util.LinkedList;

public class Crawler {
    public static void showResult(LinkedList<URLDepthPair> resultLink) {
        for (URLDepthPair c : resultLink) {
            System.out.println(c.getDepth() + "\t" + c.getURL());
        }
    }

    //являеется ли введённый символ числом
    public static boolean checkDigit(String line) {
        boolean isDigit = true;
        for (int i = 0; i < line.length() && isDigit; i++) {
            isDigit = Character.isDigit(line.charAt(i));
        }
        return isDigit;
    }

    public static void main(String[] args) throws MalformedURLException {
        //ввод ссылки и проверка данных
        args = new String[]{"http://bogoslovie.pro/", "4", "100"};
        if (args.length == 3 && checkDigit(args[1]) && checkDigit(args[2])) {
            String lineUrl = args[0];
            int numThreads = Integer.parseInt(args[2]);
            //инициализация пула адресов
            URLPool pool = new URLPool(Integer.parseInt(args[1]));
            pool.addPair(new URLDepthPair(lineUrl, 0));
            //запуск потоков
            for (int i = 0; i < numThreads; i++) {
                //передача пула адресов в каждый созданный поток
                CrawlerTask c = new CrawlerTask(pool);
                Thread t = new Thread(c);
                t.start();
            }
            //ожидание завершения работы всех потоков
            while (pool.getWait() != numThreads) {
                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                    System.out.println("Ignoring InterruptedException");
                }
            }
            try {
                showResult(pool.getResult());;
            }
            catch (NullPointerException e) {
                System.out.println("Not a link");
            }
            System.exit(0);
        }
        else {
            System.out.println("usage: java Crawler <URL> <maximum_depth> <num_threads> or second/third not digit");
        }
    }
}