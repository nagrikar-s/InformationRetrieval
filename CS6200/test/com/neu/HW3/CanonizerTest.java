package com.neu.HW3;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class CanonizerTest {
    Canonizer obj = new Canonizer();

    @Test
    public void testPort() throws IOException {
        URL url = obj.getCanonizedText(new URL("https://nuonline.neu.edu:443/webapps/portal/execute/tabs/tabAction?tab_tab_group_id=_29_1"));
        System.out.println("port removed: "+url);
    }

    @Test
    public void testSlash() throws IOException {
        URL url = obj.getCanonizedText(new URL("https://nuonline.neu.edu//webapps///portal/execute/tabs/tabAction?tab_tab_group_id=_29_1"));
        System.out.println("slashes removed: "+url);
    }
    
    @Test
    public void testDot() throws IOException {
        URL url = obj.getCanonizedText(new URL("https://nuonline.neu.edu/webapps/portal../execute/tabs/tabAction?tab_tab_group_id=_29_1"));
        System.out.println("dots removed: "+url);
    }
    @Test
    public void testCase() throws IOException {
        URL url = obj.getCanonizedText(new URL("https://Nuonline.nEu.eDu/webApps/portal/../execute/tabs/tabAction?tab_tab_group_id=_29_1"));
        System.out.println("to lower case: "+url);
    }
}
