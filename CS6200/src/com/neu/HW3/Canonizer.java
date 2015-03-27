package com.neu.HW3;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Canonizer {
    public URL getCanonizedText(URL url) throws IOException{
        String protocol = url.getProtocol();
        String host = url.getHost();
        String path = url.getPath();
        String query = url.getQuery();
        /**
         * check if url contains default port number
         */
        host = host.toLowerCase();
        if(path.contains(".")){
            //System.out.println("path tobe modified: " + path);
            /*int dotIndex = path.indexOf(".");
            path = path.substring(0, dotIndex)+path.substring(dotIndex+1);*/
            path = path.replaceAll("\\.", "");
            //System.out.println("modified path: " + path);
        }
        if(path.contains("//")){
            String[] tokens = path.split("/");
            path = "";
            for(String token : tokens){
                if(!token.isEmpty()){
                    path = path +"/"+ token.replaceAll("/", "");
                    }
                }
        }
        url = new URL(protocol+"://"+host+path);
        return url;
    }
    public InputStream getInputStream(URL x) throws IOException{
        InputStream st = x.openStream();
        return st;
    }
    
    public static void main(String[] args) throws IOException{
        Canonizer cz = new Canonizer();
        URL expURL = new URL("https://nuonline.neu.edu:443/webapps/portal/execute/tabs/tabAction?tab_tab_group_id=_29_1");
        URL canonicalizedURL = cz.getCanonizedText(expURL);
        System.out.println("canonicalizedURL: " + canonicalizedURL);
        }
}
