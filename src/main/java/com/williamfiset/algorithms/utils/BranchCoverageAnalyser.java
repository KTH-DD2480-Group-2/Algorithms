package com.williamfiset.algorithms.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class implements the functions that will be injected in the source code for analyzing. These
 * functions will output the unique ID of each visited branch into a file
 * (build/reports/diy-branch-coverage-results.txt).
 */
public class BranchCoverageAnalyser {

  /** Is used to prevent a branch to be marked as covered more than once */
  private static ArrayList<String> branchesCoveredCache = new ArrayList<>();

  private BranchCoverageAnalyser() {}

  /**
   * Creates a new file diy-branch-coverage-results.txt
   *
   * @return The file handler.
   */
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

  /**
   * Creates a file writer for diy-branch-coverage-results.txt
   *
   * @return a file writer.
   */
  private static FileWriter getResultFileWriter() {
    try {
      return new FileWriter(getResultFile(), true);
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Appends the branch ID to diy-branch-coverage-results.txt.
   *
   * @param branchId The branch ID
   */
  public static void markCovered(String branchId) {

    if (branchId == null || branchId.equals("")) return;
    if (alreadyMarkedBranch(branchId)) return;

    FileWriter fileWriter = getResultFileWriter();
    if (fileWriter == null) return;

    try {
      fileWriter.append(branchId + "\n");
      fileWriter.close();
      branchesCoveredCache.add(branchId);
    } catch (IOException e) {
      System.out.print("Could not write to file!");
    }
  }

  /**
   * Appends the branch ID to diy-branch-coverage-results.txt and returns a value. This function is
   * tailored to be used on ternary statements.
   *
   * @param branchId The branch ID to be appended.
   * @param value The value to be returned to the ternary statement.
   * @param <T> The type of the returned value.
   * @return the value of type T.
   */
  public static <T> T markCoveredValue(String branchId, T value) {
    markCovered(branchId);
    return value;
  }

  /**
   * checks id the branch has already been appended.
   *
   * @param branchId The branch ID to check.
   * @return True if already appended, otherwise false.
   */
  private static boolean alreadyMarkedBranch(String branchId) {
    return branchesCoveredCache.contains(branchId);
  }
}
