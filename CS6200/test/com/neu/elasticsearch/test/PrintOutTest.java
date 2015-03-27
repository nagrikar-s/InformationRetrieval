package com.neu.elasticsearch.test;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.junit.Test;

public class PrintOutTest {

    @Test
    public void test() throws FileNotFoundException {
        for (int i = 0; i < 1; i++) {
            System.setOut(new PrintStream("F:\\Sneha\\CS6240\\HW2\\index"));
            System.out.println("Not yet implemented");
        }
    }

}
