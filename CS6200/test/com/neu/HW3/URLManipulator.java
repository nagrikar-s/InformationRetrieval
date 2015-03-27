package com.neu.HW3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class URLManipulator {
    public void getDetails(URL url){
        try
        {
           URL dummyurl = new URL("http://www.amrood.com");
           // URL dummyurl = new URL("http:www.mynamesome");
           URLConnection urlConnection = url.openConnection();
           HttpURLConnection connection = null;
           if(urlConnection instanceof HttpURLConnection)
           {
              connection = (HttpURLConnection) urlConnection;
           }
           else
           {
              System.out.println("Please enter an HTTP URL.");
              return;
           }
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
                String urlString = "";
                String current;
                while((current = in.readLine()) != null)
                {
                   urlString += current;
                   System.out.println(current);
                }
        }catch(IOException e)
        {
           e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException{
        URLManipulator cz = new URLManipulator();
       // URL exURL = new URL("https://nuonline.neu.edu:443//webapps/portal/execute/tabs/tabAction?tab_tab_group_id=_29_1");
        URL exURL = new URL("http://example.com/index.html");
        cz.getDetails(exURL);
        String prot = exURL.getProtocol();
        String host = exURL.getHost();
        int port = exURL.getPort();
        String file = exURL.getFile();
        String path = exURL.getPath();
        String auth = exURL.getAuthority();
        String query = exURL.getQuery();
        String ref = exURL.getRef();
       Object content = exURL.getContent();
       System.out.println("Content: " + content);
       System.out.println("protocol: " + prot + " host: " + host + " port: " + port + " path: " + path + " query: " + query + " ref: " + ref + " file: " + file + " authority: " + auth);;
    
    }
}
