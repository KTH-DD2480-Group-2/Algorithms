package com.williamfiset.algorithms;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class CoverageStatistics {
//    public static void creat() throws IOException {
//        String str = "Hello";
//        BufferedWriter writer = new BufferedWriter(new FileWriter("test.txt"));
//        writer.write(str);
//
//        writer.close();
//    }

    public static void getStatistics(String allBranchFile, String visitedBranchFile) {
        HashMap<String, HashSet<String>> allBranchSet = getBranchSet(allBranchFile);
        HashMap<String, HashSet<String>> visitedBranchSet = getBranchSet(visitedBranchFile);

        for (Map.Entry<String, HashSet<String>> entry : allBranchSet.entrySet()) {
            int total = entry.getValue().size();
            int visited;
            if (visitedBranchSet.containsKey(entry.getKey()))
                visited = visitedBranchSet.get(entry.getKey()).size();
            else
                visited = 0;
            float coverage = (float) visited / total;
            System.out.println(entry.getKey() + ": Total=" + total + ", Visited=" + visited + ", Coverage=" + coverage + "%");
        }
    }

    private static HashMap<String, HashSet<String>> getBranchSet(String branchFile) {
        HashMap<String, HashSet<String>> result = new HashMap<String, HashSet<String>>();
        String function = "";
        String branch = "";
        try {
            File myObj = new File(branchFile);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data.trim().length() > 0) {
                    String[] dataSplit = data.split("\\.");
                    for (int i = 0; i < dataSplit.length - 1; i++) {
                        function += dataSplit[i];
                        function += '.';
                    }
                    function = function.substring(0, function.length() - 1);
                    branch = dataSplit[dataSplit.length - 1];
                    if (result.containsKey(function)) {
                        result.get(function).add(branch);
                    } else {
                        result.put(function, new HashSet<String>());
                        result.get(function).add(branch);
                    }
                    function = "";
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return result;
    }
}
