package lab8;
import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Crawler {
    static LinkedList<URLDepthPair> findLink = new LinkedList<>();
    static LinkedList<URLDepthPair> viewedLink = new LinkedList<>();

    public static void showResult(LinkedList<URLDepthPair> viewedLink) {
        for (URLDepthPair c : viewedLink) {
            System.out.println(c.getDepth() + "\t" + c.getURL());
        }
    }

    public static void request(PrintWriter out, URLDepthPair pair) throws MalformedURLException {
        out.println("GET " + pair.getPath() + " HTTP/1.1");
        out.println("Host: " + pair.getHost());
        out.println("Connection: close");
        out.println();
        out.flush();
    }

    public static void Process(String pair, int maxDepth) throws IOException {
        findLink.add(new URLDepthPair(pair, 0));
        while (!findLink.isEmpty()) {
            URLDepthPair currentPair = findLink.removeFirst();
            if (currentPair.getDepth() < maxDepth) {
                Socket my_socket = new Socket(currentPair.getHost(), 80);
                my_socket.setSoTimeout(1000);
                try (BufferedReader in = new BufferedReader(new InputStreamReader(my_socket.getInputStream()));
                     PrintWriter out = new PrintWriter(my_socket.getOutputStream(), true)) {
                    request(out, currentPair);
                    String line;

                    while ((line = in.readLine()) != null) {
                        if (line.indexOf(currentPair.URL_PREFIX) != -1 && line.indexOf('"') != -1) {
                            StringBuilder currentLink = new StringBuilder();
                            int i = line.indexOf(currentPair.URL_PREFIX);
                            while (i < line.length() && line.charAt(i) != '"' && line.charAt(i) != ' ') {
                                if (line.charAt(i) == '<') {
                                    currentLink.deleteCharAt(currentLink.length() - 1);
                                    break;
                                } else {
                                    currentLink.append(line.charAt(i));
                                    i++;
                                }
                            }
                            URLDepthPair newPair = new URLDepthPair(currentLink.toString(), currentPair.getDepth() + 1);
                            if (currentPair.check(findLink, newPair) && currentPair.check(viewedLink, newPair) && !currentPair.getURL().equals(newPair.getURL())) {
                                findLink.add(newPair);
                            }
                        }
                    }
                    my_socket.close();
                } catch (SocketTimeoutException e) {
                    my_socket.close();
                }
            }
            viewedLink.add(currentPair);
        }
        showResult(viewedLink);
    }


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("URL: ");
        String url = in.next();
        System.out.print("Input a depth of search: ");
        String depth = in.next();
        try {
            Process(url, Integer.parseInt(depth));
        } catch (NumberFormatException | IOException e) {
            System.out.println("usage: java Crawler " + url + " " + depth);
        }
    }
}