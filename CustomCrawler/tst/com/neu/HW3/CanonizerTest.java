package com.neu.HW3;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.junit.Test;

public class CanonizerTest {
    Canonicalizer obj = new Canonicalizer();
    URLCanonicalizer newObj = new URLCanonicalizer();

    @Test
    public void testPort() throws IOException, URISyntaxException {
        URL url = obj.getCanonizedText(new URL("https://nuonline.neu.edu:443/webapps/portal/execute/tabs/tabAction?tab_tab_group_id=_29_1"));
       URL newurl = newObj.getCanonicalURL("https://nuonline.neu.edu:443/webapps/portal/execute/tabs/tabAction?tab_tab_group_id=_29_1", null);
       System.out.println("port removed: "+newurl);
        //System.out.println("port removed: "+url);
    }

    @Test
    public void testSlash() throws IOException, URISyntaxException {
        URL url = obj.getCanonizedText(new URL("https://nuonline.neu.edu//webapps///portal/execute/tabs/tabAction?tab_tab_group_id=_29_1"));
        System.out.println("slashes removed: "+url);
    }
    
    @Test
    public void testDot() throws IOException, URISyntaxException {
        URL url = obj.getCanonizedText(new URL("https://nuonline.neu.edu/webapps/portal../execute/tabs/tabAction?tab_tab_group_id=_29_1"));
        System.out.println("dots removed: "+url);
    }
    
    @Test
    public void testCase() throws IOException, URISyntaxException {
        URL url = obj.getCanonizedText(new URL("https://Nuonline.nEu.eDu/webApps/portal/../execute/tabs/tabAction?tab_tab_group_id=_29_1"));
        System.out.println("to lower case: "+url);
    }
    
    @Test
    public void testDir() throws IOException, URISyntaxException {
        URL url = obj.getCanonizedText(new URL("http://example.com/some/../folder"));
        System.out.println("changed dir: "+url);
    }
    
    @Test
    public void testNormalize() throws URISyntaxException, Exception, IOException{
        String url = "http://www.example.com/../a/b/../c/./d.html";
        URL newurl = obj.getCanonizedText(new URL(url));
        System.out.println("normalized url: " + newurl);
    }
    
    @Test
    public void testEncoding() throws URISyntaxException, Exception, IOException{
        String url = "http://www.example.com/../a/b/../c/./d.html";
        String path = "http://www.example.com/ %7Ed.html";
        //URL newurl = obj.getCanonizedText(new URL(url));
       path = path.replace("%7E", "~").replace(" ", "%20");
        //System.out.println("normalized url: " + newurl);
        System.out.println("decoded path: " + path);
    }
    
    @Test
    public void testBuitIndecoder() throws URISyntaxException, Exception, IOException{
        String url = "http://www.example.com/ %7Ed.html";
        String eurl = URLDecoder.decode(url, "UTF-8");
        System.out.println("new url: " + eurl);
       // String durl = URLEncoder.encode(eurl, "UTF-8");
        //System.out.println("new url: " + durl);
    }
}
