package lab8;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.net.MalformedURLException;
import java.net.URL;

public class URLDepthPair {
    public static final String URL_PREFIX =  "http://";
    public String URL;
    public int depth;

    public static boolean isUrlValid(String url) {
        if (url == null)
            return false;
        Pattern urlValidationPattern = Pattern.compile(URL_PREFIX);
        return urlValidationPattern.matcher(url).find();
    }

    public URLDepthPair (String URL, int depth) throws MalformedURLException {
        if (!isUrlValid(URL)) {
            throw new MalformedURLException();
        }
        this.URL = URL;
        this.depth = depth;
    }

    public String getHost() throws MalformedURLException {
        URL host = new URL(URL);
        return host.getHost();
    }

    public String getPath() throws MalformedURLException {
        URL path = new URL(URL);
        return path.getPath();
    }

    public int getDepth() {
        return depth;
    }

    public String getURL() {
        return URL;
    }

    public static boolean check(LinkedList<URLDepthPair> resultLink, URLDepthPair pair) {
        boolean isAlready = true;
        for (URLDepthPair c : resultLink) {
            if (c.getURL().equals(pair.getURL())) {
                isAlready = false;
            }
        }
        return isAlready;
    }
}
