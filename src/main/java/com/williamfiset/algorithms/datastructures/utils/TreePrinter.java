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

  /**
   * Repeats a character a given amount of times.
   * 
   * @param amount    The amount to repeat the given character
   * @param character The character to repeat
   * @return The result after repeating the given character
   */
  private static String repeatedChar(int amount, String character) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < amount; i++) {
      builder.append(character);
    }
    return builder.toString();
  }

  /**
   * Create a connection string line from line data
   * 
   * @param line     The line data
   * @param perpiece The size of a node value section
   * @return A string representing the connection between node values at a given
   *         line.
   */
  private static String createNodeConnectionLine(List<String> line, int perpiece) {
    StringBuilder connectionLineBuilder = new StringBuilder();
    int hpw = (int) Math.floor(perpiece / 2f) - 1;
    String lineChar = "#";
    String emptySpaceChar = " ";

    for (int j = 0; j < line.size(); j++) {
      // split node
      String c = emptySpaceChar;
      if (j % 2 == 1) {
        if (line.get(j - 1) != null) {
          c = lineChar;
        } else {
          if (j < line.size() && line.get(j) != null)
            c = lineChar;
        }
      }
      connectionLineBuilder.append(c);

      // lines and spaces
      if (line.get(j) == null) {
        connectionLineBuilder.append(repeatedChar(perpiece - 1, emptySpaceChar));
      } else {
        connectionLineBuilder.append(repeatedChar(hpw, j % 2 == 0 ? emptySpaceChar : lineChar));
        connectionLineBuilder.append(lineChar);
        connectionLineBuilder.append(repeatedChar(hpw, j % 2 == 0 ? lineChar : emptySpaceChar));
      }
    }
    return connectionLineBuilder.toString();
  }

  /**
   * Create a string line from line data
   * 
   * @param line     The line data
   * @param perpiece The size of a node value section
   * @return A string representing the node values at a given line.
   */
  private static String createNodeValueLine(List<String> line, int perpiece) {
    StringBuilder nodeValueBuilder = new StringBuilder();
    for (int j = 0; j < line.size(); j++) {
      String f = line.get(j);
      if (f == null)
        f = "";
      int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
      int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);
      nodeValueBuilder.append(repeatedChar(gap1, " "));
      nodeValueBuilder.append(f);
      nodeValueBuilder.append(repeatedChar(gap2, " "));
    }
    return nodeValueBuilder.toString();
  }

  /**
   * Get the tree as lines
   * 
   * @param root The root of the binary tree
   * @return The tree represented as lines
   */
  private static List<List<String>> getEntireTreeAsLines(PrintableNode root) {
    List<List<String>> lines = new ArrayList<>();
    List<PrintableNode> level = new ArrayList<>();
    List<PrintableNode> next = new ArrayList<>();
    int nn = 1;
    level.add(root);

    while (nn != 0) {
      nn = 0;
      List<String> line = new ArrayList<String>();
      for (PrintableNode node : level) {
        if (node == null) {
          line.add(null);
          next.add(null);
          next.add(null);
          continue;
        }

        String aa = node.getText();
        if (aa == null)
          aa = "NULL";
        line.add(aa);

        next.add(node.getLeft());
        next.add(node.getRight());

        if (node.getLeft() != null)
          nn++;
        if (node.getRight() != null)
          nn++;

      }

      lines.add(line);

      List<PrintableNode> tmp = level;
      level = next;
      next = tmp;
      next.clear();
    }
    return lines;
  }

  /**
   * Getting the widest list elements, that is, the widest node value as string
   * element in the entire tree.
   * 
   * @param lines All the line data
   * @return The widest list element
   */
  private static int getWidestListElement(List<List<String>> lines) {
    int widest = 0;
    for (List<String> line : lines) {
      for (String lineElement : line) {
        if (lineElement == null)
          continue;
        if (lineElement.length() > widest)
          widest = lineElement.length();
      }
      if (widest % 2 == 1)
        widest++;
    }
    return widest;
  }

  /**
   * Prints a binary tree
   * 
   * @param root The root of the tree to print
   * @return A string of the binary tree
   */
  // Print a binary tree.
  public static String getTreeDisplay(PrintableNode root) {

    if (root == null)
      return null;

    StringBuilder treeAsStringBuilder = new StringBuilder();
    List<List<String>> treeAsLines = getEntireTreeAsLines(root);
    int widest = getWidestListElement(treeAsLines);
    int perpiece = treeAsLines.get(treeAsLines.size() - 1).size() * (widest + 4);

    for (int i = 0; i < treeAsLines.size(); i++) {
      List<String> lineData = treeAsLines.get(i);
      boolean isFirstLine = i == 0;
      if (!isFirstLine)
        treeAsStringBuilder.append(createNodeConnectionLine(lineData, perpiece) + "\n");
      treeAsStringBuilder.append(createNodeValueLine(lineData, perpiece) + "\n");
      perpiece /= 2;
    }
    return treeAsStringBuilder.toString();
  }
}
