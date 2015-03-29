package com.neu.HW3;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Canonicalizer {
    public URL getCanonizedText(URL url) throws IOException, URISyntaxException{
        String protocol = url.getProtocol();
        String host = url.getHost();
        String path = url.getPath();
        String query = url.getQuery();
        /**
         * Convert the scheme and host to lower case:
         */
        host = host.toLowerCase();

        /**
         * Remove port 80 from http URLs, and port 443 from HTTPS
         */
        path = new URI(path).normalize().toString();
        
        //System.out.println(path);
        /**
         * Make relative URLs absolute:
         */
        while (path.startsWith("/../")) {
            path = path.substring(3);
        }
        /**
         * Remove the fragment, which begins with #
         */
        if (path.contains(".")) {
            // System.out.println("path tobe modified: " + path);
            /*
             * int dotIndex = path.indexOf("."); path = path.substring(0,
             * dotIndex)+path.substring(dotIndex+1);
             */
            path = path.replaceAll("\\.", "");
            // System.out.println("modified path: " + path);
        }
        
        /**
         * Remove duplicate slashes
         */
            int idx = path.indexOf("//");
            while (idx >= 0) {
                path = path.replace("//", "/");
                idx = path.indexOf("//");
            }
            
        if (query == null) {
            url = new URL(protocol + "://" + host + path);
        } else {
            url = new URL(protocol + "://" + host + path + "?" + query);
        }
        return url;
    }

    public static URL getCanonicalURL(String href, String context) throws URISyntaxException {
        try {
            URL canonicalURL;
            if (context == null) {
                canonicalURL = new URL(href);
            } else {
                canonicalURL = new URL(new URL(context), href);
            }
            String path = canonicalURL.getPath();
            return canonicalURL;
        } catch (MalformedURLException ex) {
            return null;
        }
    }
}
