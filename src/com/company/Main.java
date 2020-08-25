package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static BufferedReader getFile(String path) throws FileNotFoundException {
        FileReader f = null;
        return new BufferedReader(new FileReader(path));
    }

    private static List fileSplit(BufferedReader br, int nums) throws IOException {
        int numRows = 0;
        String countLine = null;
        BufferedReader init = br;
        while ((countLine = init.readLine()) != null) {
            numRows++;
        }
        List<String> fileList = new ArrayList<>();
        int rowFlag = 0;
        int singleFileRows = numRows / nums;
        int singleFileRowsFlag = singleFileRows;
        int lastRow = numRows % nums;
        String line =null;
        while(nums-->0){
            StringBuilder singleFile = new StringBuilder();
            while (singleFileRows-->0) {
                line = br.readLine();
                System.out.println(line);
                singleFile.append(line);
            }
            String sb2String = singleFile.toString();
            fileList.add(sb2String);
            singleFileRows = singleFileRowsFlag;
        }
        List<List<String>> wordList = new ArrayList<>();
        for(String str:fileList){
            String[] strList = str.split(" ");
            List<String> lineStringList = new ArrayList<>();
            for (String s: strList) {
                String s1 = s.trim().replaceAll("^[,!?.;':\\-\\[\\]|&]{1,3}", "")
                        .replaceAll("[,!?.;':\\-\\[\\]|&]{1,3}$", "").toLowerCase();
                lineStringList.add(s);
            }
            wordList.add(lineStringList);
        }
        return wordList;
    }

//    private static List fileMap(String str, nums) {
//
//        return fileList;
//    }
//
//    private static List fileReduce(BufferedReader br, nums) {
//
//        return fileList;
//    }


    public static void main(String[] args) throws IOException {
        BufferedReader br = getFile("hamlet.txt");
        List wordList = fileSplit(br, 5);
        System.out.println(wordList);

    }


//    public static void main(String[] args) {
//	// write your code here
//        FileReader f = null;
//
//        try {
//            f = new FileReader("hamlet.txt");
//            BufferedReader br = new BufferedReader(f);
//            String str = null;
//            List<String> wordList = new ArrayList<String>();
//            Map<String, Integer> wordDict = new HashMap<>();
//
//            while((str = br.readLine()) != null){
//                String[] strList = str.split(" ");
//                for (String s: strList) {
//                    String s1 = s.trim();
//                    String s2 = s1.replaceAll("^[,!?.;':\\-\\[\\]|&]{1,3}", "");
//                    String s3 = s2.replaceAll("[,!?.;':\\-\\[\\]|&]{1,3}$", "");
//                    String s4 = s3.toLowerCase();
//                    wordList.add(s4);
//
//                }
//            }
//            for (String s:wordList) {
//                if(wordDict.containsKey(s)){
//                    wordDict.put(s, wordDict.get(s)+1);
//                }else{
//                    wordDict.put(s, 1);
//                }
//            }
//
//            wordDict.forEach((K,V) ->System.out.println(K+"  "+V));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
