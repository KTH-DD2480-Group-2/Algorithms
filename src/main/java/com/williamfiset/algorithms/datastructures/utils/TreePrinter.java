// Taken from @MightyPork at:
// https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
package com.williamfiset.algorithms.datastructures.utils;

import java.util.*;

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

    if (root == null) return null;

    StringBuilder sb = new StringBuilder();
    List<List<String>> lines = new ArrayList<List<String>>();
    List<PrintableNode> level = new ArrayList<PrintableNode>();
    List<PrintableNode> next = new ArrayList<PrintableNode>();

    level.add(root);
    int nn = 1;
    int widest = 0;

    // Refactor plan: Move to other function. This while loop prepares and creates the lines that 
    // later is going to be used to create the tree. It also finds the widest node value
    // in the tree that is used later when creating the tree. These two actions could be refactored
    // to two different methods in this class.
    while (nn != 0) {
      nn = 0;
      List<String> line = new ArrayList<String>();
      for (PrintableNode n : level) {
        if (n == null) {
          line.add(null);
          next.add(null);
          next.add(null);
        } else {
          String aa = n.getText();
          if (aa == null) aa = "NULL";
          line.add(aa);
          if (aa.length() > widest) widest = aa.length();

          next.add(n.getLeft());
          next.add(n.getRight());

          if (n.getLeft() != null) nn++;
          if (n.getRight() != null) nn++;
        }
      }

      if (widest % 2 == 1) widest++;

      lines.add(line);

      List<PrintableNode> tmp = level;
      level = next;
      next = tmp;
      next.clear();
    }

    int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);

    // Refactor plan: Move to other function. This for loop generates the two lines, the node connection line and the node values themselves for the tree to display. 
    // This could be refactored to a separate function that return the finished line.
    for (int i = 0; i < lines.size(); i++) {
      List<String> line = lines.get(i);
      // Refactor plan: Give other name to the variable for better understanding
      int hpw = (int) Math.floor(perpiece / 2f) - 1;

      // Refactor plan: Give if statement a variable as a bool to make it more clear what the if-statement
      // checks
      if (i > 0) {

        // Refactor plan: Move to other function. This function creates the node connection lines which can
        // be moved to another function.
        for (int j = 0; j < line.size(); j++) {

          // split node
          char c = ' ';
          if (j % 2 == 1) {
            if (line.get(j - 1) != null) {
              // Refactor plan: Remove as it has no purpose
              c = (line.get(j) != null) ? '#' : '#';
            } else {
              if (j < line.size() && line.get(j) != null) c = '#';
            }
          }
          sb.append(c);

          // lines and spaces
          if (line.get(j) == null) {
            // Refactor plan: Create helper function to create a repeated string
            for (int k = 0; k < perpiece - 1; k++) {
              sb.append(' ');
            }
          } else {
            // Refactor plan: Create helper function to create a repeated string
            for (int k = 0; k < hpw; k++) {
              sb.append(j % 2 == 0 ? " " : "#");
            }
            // Refactor plan: Remove as it has no purpose
            sb.append(j % 2 == 0 ? "#" : "#");
            // Refactor plan: Create helper function to create a repeated string
            for (int k = 0; k < hpw; k++) {
              sb.append(j % 2 == 0 ? "#" : " ");
            }
          }
        }
        sb.append('\n');
      }

      // Refactor plan: Move to other function. This functions create the lines with the node values. 
      // Can be refactored to other 
      for (int j = 0; j < line.size(); j++) {
        String f = line.get(j);
        if (f == null) f = "";
        int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
        int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

        // Refactor plan: Create helper function to create a repeated string
        for (int k = 0; k < gap1; k++) {
          sb.append(' ');
        }
        sb.append(f);
        // Refactor plan: Create helper function to create a repeated string
        for (int k = 0; k < gap2; k++) {
          sb.append(' ');
        }
      }
      sb.append('\n');

      perpiece /= 2;
    }
    return sb.toString();
  }
}
