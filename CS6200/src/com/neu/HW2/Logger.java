package com.neu.HW2;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
        public static void log(String message,String outPath) throws IOException {
                PrintWriter out = new PrintWriter(new FileWriter(outPath), true);
                out.write(message);
                out.close();
        }
    }
