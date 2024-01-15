package utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class Helper {
    public URL getPageUrl(String pageName) throws MalformedURLException {
        String pageUrlString = ResourceBundle.getBundle("config.url-config").getString(pageName);
        return new URL(pageUrlString);
    }
}
