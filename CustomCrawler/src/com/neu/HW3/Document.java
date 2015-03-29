package com.neu.HW3;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Document {
    String urlId;
    List<URL> inLinks = new ArrayList<>();
    List<URL> outLinks = new ArrayList<>();
    String text;
}
