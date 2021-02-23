
package com.williamfiset.algorithms.datastructures.utils;
import static com.google.common.truth.Truth.assertThat;
import org.junit.*;


public class TreePrinterTest {
    private class TestTreeNode implements TreePrinter.PrintableNode {
        String value;
        private TestTreeNode left;
        private TestTreeNode right;
    
        public TestTreeNode(String value, TestTreeNode left, TestTreeNode right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }
    
        // Get left child
        public TreePrinter.PrintableNode getLeft() {
            return this.left;
        }
    
        // Get right child
        public TreePrinter.PrintableNode getRight() {
            return this.right;
        }
    
        // Get text to be printed
        public String getText() {
            return value;
        }
    }

  @Test
  public void treePrinter_displayTree_checkNodeValuesExist() {
    TestTreeNode treeToTest = new TestTreeNode(
        "A", 
        new TestTreeNode(
            "B", 
            null, 
            null
        ), 
        new TestTreeNode(
            "C", 
            null, 
            new TestTreeNode(
                "D",
                null,
                null
            )
        )
    );
    String treeDisplayOutput = TreePrinter.getTreeDisplay(treeToTest);
    assertThat(treeDisplayOutput).contains("A");
    assertThat(treeDisplayOutput).contains("B");
    assertThat(treeDisplayOutput).contains("C");
    assertThat(treeDisplayOutput).contains("D");
  }

  @Test
  public void treePrinter_displayTree_nodeValuesOnCorrectLines() {
    TestTreeNode treeToTest = new TestTreeNode(
        "ROOT", 
        new TestTreeNode(
            "ROW1_LEFT", 
            new TestTreeNode(
                "LEFT_ROW2_LEFT",
                null,
                null
            ), 
            new TestTreeNode(
                "LEFT_ROW2_RIGHT",
                null,
                null
            )
        ), 
        new TestTreeNode(
            "ROW1_RIGHT", 
            new TestTreeNode(
                "RIGHT_ROW2_LEFT",
                null,
                null
            ), 
            new TestTreeNode(
                "RIGHT_ROW2_RIGHT",
                null,
                null
            )
        )
    );
    String treeDisplayOutput = TreePrinter.getTreeDisplay(treeToTest);
    String[] treeDisplayOutputLines = treeDisplayOutput.split("\\n", -1);
    assertThat(treeDisplayOutputLines[0]).contains("ROOT");
    assertThat(treeDisplayOutputLines[2]).contains("ROW1_LEFT");
    assertThat(treeDisplayOutputLines[2]).contains("ROW1_RIGHT");
    assertThat(treeDisplayOutputLines[4]).contains("LEFT_ROW2_LEFT");
    assertThat(treeDisplayOutputLines[4]).contains("LEFT_ROW2_RIGHT");
    assertThat(treeDisplayOutputLines[4]).contains("RIGHT_ROW2_LEFT");
    assertThat(treeDisplayOutputLines[4]).contains("RIGHT_ROW2_RIGHT");
  }

  @Test
  public void treePrinter_displayTree_treeRootAsNull() {
    String treeDisplayOutput = TreePrinter.getTreeDisplay(null);
    assertThat(treeDisplayOutput).isEqualTo(null);
  }

  @Test
  public void treePrinter_displayTree_nodeValueAsNull() {
    TestTreeNode treeToTest = new TestTreeNode(
        null, 
        new TestTreeNode(
            null, null, null
        ), 
        new TestTreeNode(
            null, null, null
        )
    );
    String treeDisplayOutput = TreePrinter.getTreeDisplay(treeToTest);
    assertThat(treeDisplayOutput).contains("NULL");
  }

  @Test
  public void treePrinter_displayTree_oneSidedTree() {
    TestTreeNode treeToTest = new TestTreeNode(
        "ROOT", 
        null, 
        new TestTreeNode(
            "ROW1", 
            null, 
            new TestTreeNode(
                "ROW2", 
                null, 
                new TestTreeNode(
                    "ROW3", 
                    null, 
                    new TestTreeNode(
                        "ROW4", 
                        null, 
                        null
                    )
                )
            )
        )
    );
    String treeDisplayOutput = TreePrinter.getTreeDisplay(treeToTest);
    String[] treeDisplayOutputLines = treeDisplayOutput.split("\\n", -1);
    assertThat(treeDisplayOutputLines[0]).contains("ROOT");
    assertThat(treeDisplayOutputLines[2]).contains("ROW1");
    assertThat(treeDisplayOutputLines[4]).contains("ROW2");
    assertThat(treeDisplayOutputLines[6]).contains("ROW3");
    assertThat(treeDisplayOutputLines[8]).contains("ROW4");
  }
}
