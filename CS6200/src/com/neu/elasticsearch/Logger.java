package com.neu.elasticsearch;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
        public static void log(String message, int model) throws IOException {
            if(model == 1){
                PrintWriter out = new PrintWriter(new FileWriter("1output.txt"), true);
                out.write(message);
                out.close();
            }
            if(model == 2){
                PrintWriter out = new PrintWriter(new FileWriter("2output.txt"), true);
                out.write(message);
                out.close();
            }
            if(model == 3){
                PrintWriter out = new PrintWriter(new FileWriter("3output.txt"), true);
                out.write(message);
                out.close();
            }
            if(model == 4){
                PrintWriter out = new PrintWriter(new FileWriter("4output.txt"), true);
                out.write(message);
                out.close();
            }
            if(model == 5){
                PrintWriter out = new PrintWriter(new FileWriter("5output.txt"), true);
                out.write(message);
                out.close();
            }
        }
    }
