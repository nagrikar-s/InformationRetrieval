package com.neu.HW2;

import java.io.IOException;
import java.util.HashSet;

public class GetIndexedTermsTest {
    public static void main(String[] args) throws IOException {
        TokenizerTest tt = new TokenizerTest("C:\\Users\\snehanay\\Google Drive\\CS6200\\HW2\\stopwords.txt");
        HashSet<String> indexedTerms = tt.getIndexedTerms();
        System.out.println(indexedTerms.size());
    }
}