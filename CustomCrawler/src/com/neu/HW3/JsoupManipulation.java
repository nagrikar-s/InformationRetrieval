package com.neu.HW3;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

/*
 * "https://nuonline.neu.edu:443/webapps/portal/execute/tabs/tabAction?tab_tab_group_id=_29_1"
 * http://en.wikipedia.org/wiki/Philosophy
 */
public class JsoupManipulation {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("http://en.wikipedia.org/wiki/Philosophy").get();
        System.out.println("title: " + doc.title());
        String newDoc = Jsoup.clean("https://nuonline.neu.edu/webapps/blackboard/execute/announcement", Whitelist.none());
        /*
         * get text on the page
         */
        System.out.println("doc body: "+doc.body().text());
       // doc.body().
        System.out.println("cleaned: "+newDoc);
        Elements links = doc.select("a");
        /*for (Element link : links) {
            System.out.println("html: " + link.attr("abs:href"));
        }*/
    }
}
