// Taken from @MightyPork at:
// https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
package com.williamfiset.algorithms.datastructures.utils;

import java.util.*;

import com.williamfiset.algorithms.utils.BranchCoverageAnalyser;

public class TreePrinter {

  /** Node that can be printed */
  public interface PrintableNode {

    // Get left child
    public PrintableNode getLeft();

    // Get right child
    public PrintableNode getRight();

    // Get text to be printed
    public String getText();
  }

  // Print a binary tree.
  public static String getTreeDisplay(PrintableNode root) {

    StringBuilder sb = new StringBuilder();
    List<List<String>> lines = new ArrayList<List<String>>();
    List<PrintableNode> level = new ArrayList<PrintableNode>();
    List<PrintableNode> next = new ArrayList<PrintableNode>();

    level.add(root);
    int nn = 1;
    int widest = 0;

    BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_1");
    while (nn != 0) {
      BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_2");
      nn = 0;
      List<String> line = new ArrayList<String>();
      for (PrintableNode n : level) {
        BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_3");
        if (n == null) {
          BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_4");
          line.add(null);
          next.add(null);
          next.add(null);
        } else {
          BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_5");
          String aa = n.getText();
          line.add(aa);
          if (aa.length() > widest) {
            BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_6");
            widest = aa.length();
          }

          next.add(n.getLeft());
          next.add(n.getRight());

          if (n.getLeft() != null) {
            BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_7");
            nn++;
          }
          if (n.getRight() != null) {
            BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_8");
            nn++;
          }
        }
      }

      if (widest % 2 == 1) {
        BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_9");
        widest++;
      }

      lines.add(line);

      List<PrintableNode> tmp = level;
      level = next;
      next = tmp;
      next.clear();
    }

    int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
    for (int i = 0; i < lines.size(); i++) {
      BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_10");
      List<String> line = lines.get(i);
      int hpw = (int) Math.floor(perpiece / 2f) - 1;
      if (i > 0) {
        BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_11");
        for (int j = 0; j < line.size(); j++) {
          BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_12");
          // split node
          char c = ' ';
          if (j % 2 == 1) {
            BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_13");
            if (line.get(j - 1) != null) {
              BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_14");
              c = (line.get(j) != null) 
                ? BranchCoverageAnalyser.markCoveredValue("TreePrinter.getTreeDisplay.ID_14", '#')
                : BranchCoverageAnalyser.markCoveredValue("TreePrinter.getTreeDisplay.ID_15", '#');
            } else {
              BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_16");
              if (j < line.size() && line.get(j) != null) {
                BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_17");
                c = '#';
              }
            }
          }
          sb.append(c);

          // lines and spaces
          if (line.get(j) == null) {
            BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_18");
            for (int k = 0; k < perpiece - 1; k++) {
              BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_19");
              sb.append(' ');
            }
          } else {
            BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_20");
            for (int k = 0; k < hpw; k++) {
              BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_21");
              sb.append(
                j % 2 == 0 
                  ? BranchCoverageAnalyser.markCoveredValue("TreePrinter.getTreeDisplay.ID_22", " ")
                  : BranchCoverageAnalyser.markCoveredValue("TreePrinter.getTreeDisplay.ID_23", "#")
              );
            }
            sb.append(
              j % 2 == 0 
                ? BranchCoverageAnalyser.markCoveredValue("TreePrinter.getTreeDisplay.ID_24", "#")
                : BranchCoverageAnalyser.markCoveredValue("TreePrinter.getTreeDisplay.ID_25", "#")
            );
            for (int k = 0; k < hpw; k++) {
              sb.append(
                j % 2 == 0 
                  ? BranchCoverageAnalyser.markCoveredValue("TreePrinter.getTreeDisplay.ID_26", "#")
                  : BranchCoverageAnalyser.markCoveredValue("TreePrinter.getTreeDisplay.ID_27", " ")
              );
            }
          }
        }
        sb.append('\n');
      }
      for (int j = 0; j < line.size(); j++) {
        BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_28");
        String f = line.get(j);
        if (f == null) {
          BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_29");
          f = "";
        }
        int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
        int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

        for (int k = 0; k < gap1; k++) {
          BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_30");
          sb.append(' ');
        }
        sb.append(f);
        for (int k = 0; k < gap2; k++) {
          BranchCoverageAnalyser.markCovered("TreePrinter.getTreeDisplay.ID_31");
          sb.append(' ');
        }
      }
      sb.append('\n');

      perpiece /= 2;
    }
    return sb.toString();
  }
}
