package lab9;
import java.util.LinkedList;

public class URLPool {
    LinkedList<URLDepthPair> findLink;
    LinkedList<URLDepthPair> viewedLink;
    int maxDepth;
    //число потоков, ждущих ссылку для обработки
    int cWait;

    //конструктор, принимающий значение максимальной глубины
    public URLPool(int maxDepth) {
        this.maxDepth = maxDepth;
        findLink = new LinkedList<URLDepthPair>();
        viewedLink = new LinkedList<URLDepthPair>();
        cWait = 0; // количество потоков, ожидающих ссылку для обработки.
    }

    //метод для получения пары для обработки из потока
    public synchronized URLDepthPair getPair() {
        while (findLink.size() == 0)
        {
            cWait++;
            try {
                wait();
            }
            catch (InterruptedException e) {
                System.out.println("Ignoring InterruptedException");
            }
            cWait--;
        }
        URLDepthPair nextPair = findLink.removeFirst();
        return nextPair;
    }

    //метод для добавления пары из потока в список просмотренных или найденных ссылок
    public synchronized void addPair(URLDepthPair pair) {
        if (URLDepthPair.check(viewedLink, pair)) {
            viewedLink.add(pair);
            if (pair.getDepth() < maxDepth) {
                findLink.add(pair);
                notify();
            }
        }
    }

    public synchronized int getWait() {
        return cWait;
    }

    public LinkedList<URLDepthPair> getResult() {
        return viewedLink;
    }
}