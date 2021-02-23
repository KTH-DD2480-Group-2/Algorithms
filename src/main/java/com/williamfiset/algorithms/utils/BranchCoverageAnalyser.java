package com.williamfiset.algorithms.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BranchCoverageAnalyser {

  /** Is used to prevent a branch to be marked as covered more than once */
  private static ArrayList<String> branchesCoveredCache = new ArrayList<>();

  private BranchCoverageAnalyser() {}

  private static File getResultFile() {
    String resultFilePath = "./build/reports/diy-branch-coverage-results.txt";
    try {
      File resultFile = new File(resultFilePath);
      resultFile.createNewFile();
      return resultFile;
    } catch (IOException e) {
      return null;
    }
  }

  private static FileWriter getResultFileWriter() {
    try {
      return new FileWriter(getResultFile(), true);
    } catch (IOException e) {
      return null;
    }
  }

  public static void markCovered(String branchId) {
    FileWriter fileWriter = getResultFileWriter();

    if (branchId == null || branchId.equals("")) return;
    if (fileWriter == null) return;
    if (alreadyMarkedBranch(branchId)) return;

    try {
      fileWriter.append(branchId + "\n");
      fileWriter.close();
      branchesCoveredCache.add(branchId);
    } catch (IOException e) {
      System.out.print("Could not write to file!");
    }
  }

  public static <T> T markCoveredValue(String branchId, T value) {
    markCovered(branchId);
    return value;
  }

  private static boolean alreadyMarkedBranch(String branchId) {
    return branchesCoveredCache.contains(branchId);
  }

}