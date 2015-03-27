package com.neu.elasticsearch.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.neu.HW2.Porter;

public class TestSet {
    @Test
    public void test() {
        String text = "my name is anthony gonsalvis,\nmay be my name is sneha translate presidential Democratic";
        HashSet<String> hashset = new HashSet<>();
        //System.out.println("text: " + text);
        Pattern pattern = Pattern.compile("\\w+(\\.?\\w+)*");
        Matcher matcher = pattern.matcher(text.toLowerCase());
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                Porter obj = new Porter();
                String term = obj.stripAffixes(matcher.group(i));
                if (hashset.add(matcher.group(i))) {
                  //  System.out.println(i + ": hashword: " + term);
                }
               // System.out.println(i + ": word: " + term);
            }
        }
        String[] arr = (String[]) hashset.toArray();
        System.out.println("hashset: " + hashset.size() + "arr: "+arr.length);
        for(int i=0; i<arr.length; i++){
            System.out.println("arr: " + arr[i]);
        }
    }
}