package com.williamfiset.algorithms.utils;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

/**
 * This class will generate a coverage report for all the functions that have been analysed with the
 * BranchCoverageAnalyser. More specifically, it will compare
 * build/reports/diy-branch-coverage-results.txt with diy-branch-coverage-control.txt (which
 * contains all possible branch IDs) and then generate the file
 * build/reports/DIY-Coverage-Report.txt
 */
public class CoverageStatistics {
  public static void main(String[] args) throws IOException {
    getStatistics(
        "diy-branch-coverage-control.txt", "build/reports/diy-branch-coverage-results.txt");
  }

  /**
   * This function will compare the visited branches with all possible branches and and output the
   * results into a file build/reports/DIY-Coverage-Report.txt.
   *
   * @param allBranchFile The file that contains a list of the IDs of all possible branches.
   * @param visitedBranchFile Teh file that contains a list of the IDs of visited branches.
   * @throws IOException In case any of the input files does not exist.
   */
  public static void getStatistics(String allBranchFile, String visitedBranchFile)
      throws IOException {
    HashMap<String, HashSet<String>> allBranchSet = getBranchSet(allBranchFile);
    HashMap<String, HashSet<String>> visitedBranchSet = getBranchSet(visitedBranchFile);

    BufferedWriter writer =
        new BufferedWriter(new FileWriter("./build/reports/DIY-Coverage-Report.txt"));

    for (Map.Entry<String, HashSet<String>> entry : allBranchSet.entrySet()) {
      int total = entry.getValue().size();
      int visited;
      if (visitedBranchSet.containsKey(entry.getKey()))
        visited = visitedBranchSet.get(entry.getKey()).size();
      else visited = 0;
      float coverage = (float) visited * 100 / total;
      writer.write(
          entry.getKey()
              + ": Total="
              + total
              + ", Visited="
              + visited
              + ", Coverage="
              + coverage
              + "%\n");
    }
    writer.close();
  }

  /**
   * This function will parse the text input file into a hashmap
   *
   * @param branchFile The text input file that contains a list fo branch IDs.
   * @return A hashmap representing the branches listed in the file.
   */
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
