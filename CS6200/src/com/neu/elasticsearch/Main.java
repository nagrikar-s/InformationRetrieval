package com.neu.elasticsearch;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        try (ParseDocument parser = new ParseDocument()) {
            parser.parseFiles(new File("C:/Users/snehanay/Google Drive/CS6200/AP89_DATA/AP_DATA/ap89_collection"), "ap");
        }
    }
}
