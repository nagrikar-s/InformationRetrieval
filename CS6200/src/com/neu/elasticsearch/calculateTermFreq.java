package com.neu.elasticsearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class calculateTermFreq {
    public final static int D = 84678; // total number of documents in the
                                       // corpus
    public int numOftopDocs = 100; // total number top documents returned
    public final int V = 178050;
    private static DocumentLength parser = new DocumentLength();
    private static final Map<String, Integer> docLength = parser.parseFiles(
            new File("C:/Users/snehanay/Google Drive/CS6200/AP89_DATA/AP_DATA/ap89_collection"), "ap");
    private static StringBuilder okapiBuilder = new StringBuilder();
    private static StringBuilder tf_idfBuilder = new StringBuilder();
    private static StringBuilder BM25Builder = new StringBuilder();
    private static StringBuilder lm_laplaceBuilder = new StringBuilder();
    private static StringBuilder JM_tfBuilder = new StringBuilder();

    public static List<Float> docStatistics() {
        int sum = 0;
        float avgLength;
        List<Float> stats = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : docLength.entrySet()) {
            sum = sum + entry.getValue();
        }
        avgLength = sum / D;
        stats.add((float) sum);
        stats.add(avgLength);
        return stats;
    }

    public void getTF(QueryResult hitsResult, float avgLength, Float sum) throws IOException {
        for (int query = 0; query < hitsResult.qResult.size(); query++) {
            List<String> docId = hitsResult.qResult.get(query).docId;
            List<Float> tf = hitsResult.qResult.get(query).tf;
            List<Integer> frequency = hitsResult.qResult.get(query).frequency;
            List<Integer> termNo = hitsResult.qResult.get(query).termNumber;
            int queryNo = hitsResult.qResult.get(query).queryNumber;
            Map<String, Float> okapi_tf = new HashMap<>();
            Map<String, Float> tf_idf = new HashMap<>();
            Map<String, Float> BM25 = new HashMap<>();
            Map<String, Float> lm_laplace = new HashMap<>();
            Map<String, Float> JM_tf = new HashMap<>();
            Float[] tmp = new Float[docId.size()];
            Float[] tmpBM25 = new Float[docId.size()];
            Float[] tf_idfTemp = new Float[tmp.length];
            Float[] p_laplace = new Float[docId.size()];
            Float[] p_jm = new Float[docId.size()];
            double k1 = 1.2;
            double k2 = 100;
            double b = 0.75;
            int queryTermFreq = 1;
            
            int j = 0;
            Float[] tfSum = new Float[termNo.size()];
            for (int term = 0; term < termNo.size(); term++) {
                tfSum[term] = (float) 0;
                while (term == termNo.indexOf(j)) {
                    tfSum[term] = tfSum[term] + tf.get(j);
                    j++;
                }
            }
            for (int i = 0; i < docId.size(); i++) {
                tmp[i] = (float) (tf.get(i) / (tf.get(i) + 0.5 + (1.5 * docLength.get(docId.get(i)) / avgLength)));//
                okapi_tf = calculateOkapiTF(okapi_tf, tmp[i], docId.get(i));

                tf_idfTemp[i] = (float) (tmp[i] * Math.log(D / frequency.get(termNo.get(i))));
                tf_idf = calculateTf_idf(tf_idf, tf_idfTemp[i], docId.get(i));

                double block1 = (Math.log((D + 0.5) / frequency.get(termNo.get(i))));
                double block2 = (tf.get(i) + (k1 * tf.get(i) / tf.get(i) + k1 * ((1 - b) + b * (docLength.get(docId.get(i)) / avgLength))));
                double block3 = (queryTermFreq + (k2 * queryTermFreq)) / (queryTermFreq + k2);
                tmpBM25[i] = (float) (block1 * block2 * block3);
                BM25 = calculateOkapiBM25(BM25, docId.get(i), tmpBM25[i]);

                p_laplace[i] = (tf.get(i) + 1) / (docLength.get(docId.get(i)) + V);
                lm_laplace = calculateLaplaceSmoothing(lm_laplace, docId.get(i), p_laplace[i]);
                double lambda = docLength.get(docId.get(i))/(docLength.get(docId.get(i))+V);
                p_jm[i] = (float) (lambda * (tf.get(i) / docLength.get(docId.get(i))) + (1 - lambda)
                        * ((tfSum[termNo.get(i)] - tf.get(i)) / (sum - docLength.get(docId.get(i)))));
                JM_tf = calculateJMSmothing(JM_tf, docId.get(i), p_jm[i]);
            }
            getTopDocs(okapi_tf, queryNo, 1);
            getTopDocs(tf_idf, queryNo, 2);
            getTopDocs(BM25, queryNo, 3);
            getTopDocs(lm_laplace, queryNo, 4);
            getTopDocs(JM_tf, queryNo, 5);
        }
    }

    public Map<String, Float> calculateOkapiTF(Map<String, Float> okapi_tf, Float tmp, String docId) {
        if (okapi_tf.containsKey(docId)) {
            okapi_tf.put(docId, (okapi_tf.get(docId) + tmp));
        } else {
            okapi_tf.put(docId, tmp);
        }
        return okapi_tf;
    }

    /**
     * 
     * @param frequency
     *            is number of documents with contains term w
     * @param tmp
     */

    public Map<String, Float> calculateTf_idf(Map<String, Float> tf_idf, Float tf_idfTemp, String docId) {
        if (tf_idf.containsKey(docId)) {
            tf_idf.put(docId, (tf_idf.get(docId) + tf_idfTemp));
        } else {
            tf_idf.put(docId, tf_idfTemp);
        }
        return tf_idf;
    }

    public Map<String, Float> calculateOkapiBM25(Map<String, Float> BM25, String docId, float tmp) {
        if (BM25.containsKey(docId)) {
            BM25.put(docId, (BM25.get(docId) + tmp));
        } else {
            BM25.put(docId, tmp);
        }
        return BM25;
    }

    public Map<String, Float> calculateLaplaceSmoothing(Map<String, Float> lm_laplace, String docId, Float p_laplace) {
        if (lm_laplace.containsKey(docId)) {
            lm_laplace.put(docId, (lm_laplace.get(docId) + p_laplace));
        } else {
            lm_laplace.put(docId, p_laplace);
        }
        return lm_laplace;
    }

    public Map<String, Float> calculateJMSmothing(Map<String, Float> JM_tf, String docId, Float p_jm) {
        if (JM_tf.containsKey(docId)) {
            JM_tf.put(docId, (float) (JM_tf.get(docId) + Math.log(p_jm)));// ****
        } else {
            JM_tf.put(docId, (float) Math.log(p_jm));
        }
        return JM_tf;
    }

    public String[] getTopDocs(Map<String, Float> overall_tf, int queryNo, int model) throws IOException {
        System.out.println("getting top documents");
        String[] topDocs = new String[numOftopDocs];
        String topId = null;
        for (int doc = 0; doc < numOftopDocs; doc++) {
            float max = Integer.MIN_VALUE;
            for (Entry<String, Float> entry : overall_tf.entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    topId = entry.getKey();
                }
            }
            topDocs[doc] = topId;
            overall_tf.remove(topId);
            int rank = doc + 1;
            if (model == 1) {
                okapiBuilder.append(queryNo + " Q0 " + topDocs[doc] + " " + rank + " " + max + " Exp\n");
            }
            if (model == 2) {
                tf_idfBuilder.append(queryNo + " Q0 " + topDocs[doc] + " " + rank + " " + max + " Exp\n");
            }
            if (model == 3) {
                BM25Builder.append(queryNo + " Q0 " + topDocs[doc] + " " + rank + " " + max + " Exp\n");
            }
            if (model == 4) {
                lm_laplaceBuilder.append(queryNo + " Q0 " + topDocs[doc] + " " + rank + " " + max + " Exp\n");
            }
            if (model == 5) {
                JM_tfBuilder.append(queryNo + " Q0 " + topDocs[doc] + " " + rank + " " + max + " Exp\n");
            }
            System.out.println(queryNo + " Q0 " + topDocs[doc] + " " + rank + " " + max + " Exp\n");
        }
        return topDocs;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        List<Float> stats = docStatistics();
        Float sum = stats.get(0);
        Float avgLength = stats.get(1);
        try (SearchQueries qp = new SearchQueries(new File("C:/Users/snehanay/Google Drive/CS6200/AP89_DATA/AP_DATA/newStopList.txt"))) {
        //QueryTextParser qp = new QueryTextParser(new File("C:/Users/snehanay/Google Drive/CS6200/AP89_DATA/AP_DATA/newStopList.txt"));    
        File file = new File("C:/Users/snehanay/Google Drive/CS6200/AP89_DATA/AP_DATA/query_desc.51-100.short.txt");//dummyQuery.txt");// 
            QueryResult hitsResult = qp.parseFile(file);
            System.out.println("entering the tf method");
            calculateTermFreq newtf = new calculateTermFreq();
            newtf.getTF(hitsResult, avgLength, sum);
            Logger.log(okapiBuilder.toString(), 1);
            Logger.log(tf_idfBuilder.toString(), 2);
            Logger.log(BM25Builder.toString(), 3);
            Logger.log(lm_laplaceBuilder.toString(), 4);
            Logger.log(JM_tfBuilder.toString(), 5);
        }
    }
}