package com.neu.elasticsearch.test;

import static org.junit.Assert.assertArrayEquals;

import java.io.BufferedReader;
import java.io.FileReader;

import org.junit.Test;

import java.io.File;
public class stringTest {

    @Test
    public void test1() {
        String num = "36.".replace(".", " ").trim();
        assertArrayEquals(new int[36], new int[java.lang.Integer.valueOf(num)]);
        }
   
  
    }