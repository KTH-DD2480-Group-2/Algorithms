package com.williamfiset.algorithms.datastructures.quadtree;

import static com.google.common.truth.Truth.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class QuadTreeTest {

  static final int LOOPS = 50;
  static final int TEST_SZ = 1000;
  static final int MAX_RAND_NUM = +2000;

  @Before
  public void setup() {}

  @Test
  public void testRectIntersection() {

    QuadTree.Rect r1 = new QuadTree.Rect(0, 0, 5, 5);

    QuadTree.Rect r1Center = new QuadTree.Rect(1, 1, 4, 4);
    QuadTree.Rect r1NWCorner = new QuadTree.Rect(-1, 5, 0, 6);
    QuadTree.Rect r1SWCorner = new QuadTree.Rect(-1, -1, 0, 0);
    QuadTree.Rect r1SECorner = new QuadTree.Rect(5, -1, 6, 0);
    QuadTree.Rect r1NECorner = new QuadTree.Rect(5, 5, 6, 6);
    QuadTree.Rect r1Above = new QuadTree.Rect(2, 6, 3, 8);
    QuadTree.Rect r1Below = new QuadTree.Rect(2, -5, 5, -1);
    QuadTree.Rect r1Left = new QuadTree.Rect(-5, -4, -1, 8);
    QuadTree.Rect r1Right = new QuadTree.Rect(6, -3, 7, 8);

    assertThat(r1.intersects(r1)).isTrue();

    assertThat(r1.intersects(r1Center)).isTrue();
    assertThat(r1Center.intersects(r1)).isTrue();

    assertThat(r1.intersects(r1NWCorner)).isTrue();
    assertThat(r1NWCorner.intersects(r1)).isTrue();

    assertThat(r1.intersects(r1NECorner)).isTrue();
    assertThat(r1NECorner.intersects(r1)).isTrue();

    assertThat(r1.intersects(r1SECorner)).isTrue();
    assertThat(r1SECorner.intersects(r1)).isTrue();

    assertThat(r1.intersects(r1SWCorner)).isTrue();
    assertThat(r1SWCorner.intersects(r1)).isTrue();

    assertThat(r1.intersects(r1Above)).isFalse();
    assertThat(r1Above.intersects(r1)).isFalse();

    assertThat(r1.intersects(r1Below)).isFalse();
    assertThat(r1Below.intersects(r1)).isFalse();

    assertThat(r1.intersects(r1Left)).isFalse();
    assertThat(r1Left.intersects(r1)).isFalse();

    assertThat(r1.intersects(r1Right)).isFalse();
    assertThat(r1Right.intersects(r1)).isFalse();
  }

  @Test
  public void testRectContainment() {

    QuadTree.Rect r1 = new QuadTree.Rect(0, 0, 5, 5);

    QuadTree.Rect r1Center = new QuadTree.Rect(1, 1, 4, 4);
    QuadTree.Rect r1NWCorner = new QuadTree.Rect(-1, 5, 0, 6);
    QuadTree.Rect r1SWCorner = new QuadTree.Rect(-1, -1, 0, 0);
    QuadTree.Rect r1SECorner = new QuadTree.Rect(5, -1, 6, 0);
    QuadTree.Rect r1NECorner = new QuadTree.Rect(5, 5, 6, 6);
    QuadTree.Rect r1Above = new QuadTree.Rect(2, 6, 3, 8);
    QuadTree.Rect r1Below = new QuadTree.Rect(2, -5, 5, -1);
    QuadTree.Rect r1Left = new QuadTree.Rect(-5, -4, -1, 8);
    QuadTree.Rect r1Right = new QuadTree.Rect(6, -3, 7, 8);

    assertThat(r1.contains(r1)).isTrue();

    assertThat(r1.contains(r1Center)).isTrue();
    assertThat(r1Center.contains(r1)).isFalse();

    assertThat(r1.contains(r1NWCorner)).isFalse();
    assertThat(r1NWCorner.contains(r1)).isFalse();

    assertThat(r1.contains(r1NECorner)).isFalse();
    assertThat(r1NECorner.contains(r1)).isFalse();

    assertThat(r1.contains(r1SECorner)).isFalse();
    assertThat(r1SECorner.contains(r1)).isFalse();

    assertThat(r1.contains(r1SWCorner)).isFalse();
    assertThat(r1SWCorner.contains(r1)).isFalse();

    assertThat(r1.contains(r1Above)).isFalse();
    assertThat(r1Above.contains(r1)).isFalse();

    assertThat(r1.contains(r1Below)).isFalse();
    assertThat(r1Below.contains(r1)).isFalse();

    assertThat(r1.contains(r1Left)).isFalse();
    assertThat(r1Left.contains(r1)).isFalse();

    assertThat(r1.contains(r1Right)).isFalse();
    assertThat(r1Right.contains(r1)).isFalse();
  }

  @Test
  public void testPointContainment() {

    QuadTree.Rect r1 = new QuadTree.Rect(0, 0, 5, 5);

    // Corner check
    assertThat(r1.contains(0, 0)).isTrue();
    assertThat(r1.contains(0, 5)).isTrue();
    assertThat(r1.contains(5, 0)).isTrue();
    assertThat(r1.contains(5, 5)).isTrue();

    // Side check
    assertThat(r1.contains(0, 1)).isTrue();
    assertThat(r1.contains(0, 2)).isTrue();
    assertThat(r1.contains(0, 3)).isTrue();
    assertThat(r1.contains(0, 4)).isTrue();

    // Side check
    assertThat(r1.contains(1, 0)).isTrue();
    assertThat(r1.contains(2, 0)).isTrue();
    assertThat(r1.contains(3, 0)).isTrue();
    assertThat(r1.contains(4, 0)).isTrue();

    // Side check
    assertThat(r1.contains(1, 5)).isTrue();
    assertThat(r1.contains(2, 5)).isTrue();
    assertThat(r1.contains(3, 5)).isTrue();
    assertThat(r1.contains(4, 5)).isTrue();

    // Side check
    assertThat(r1.contains(5, 1)).isTrue();
    assertThat(r1.contains(5, 2)).isTrue();
    assertThat(r1.contains(5, 3)).isTrue();
    assertThat(r1.contains(5, 4)).isTrue();

    // Inside check
    assertThat(r1.contains(2, 3)).isTrue();
    assertThat(r1.contains(1, 1)).isTrue();
    assertThat(r1.contains(4, 3)).isTrue();
    assertThat(r1.contains(3, 1)).isTrue();

    // Outside check
    assertThat(r1.contains(-1, 3)).isFalse();
    assertThat(r1.contains(-2, -2)).isFalse();
    assertThat(r1.contains(6, 3)).isFalse();
    assertThat(r1.contains(3, 6)).isFalse();
    assertThat(r1.contains(3, -6)).isFalse();
    assertThat(r1.contains(-3, 6)).isFalse();
  }

  @Test
  public void testCountingPoints() {

    final int SZ = 100;
    QuadTree.Rect region = new QuadTree.Rect(0, 0, SZ, SZ);
    QuadTree quadTree = new QuadTree(region);

    // Add points on a diagonal
    for (int i = 0; i <= SZ; i++) quadTree.add(i, i);

    // Query entire region there should be 101 points
    assertThat(quadTree.count(region)).isEqualTo(101);
  }

  public int bruteForceCount(int[][] grid, int x1, int y1, int x2, int y2) {
    int sum = 0;
    for (int i = y1; i <= y2; i++) for (int j = x1; j <= x2; j++) sum += grid[i][j];
    return sum;
  }

  @Test
  public void randomizedQueryTests() {

    for (int test = 0; test < LOOPS; test++) {

      int W = 1 + (int) (Math.random() * MAX_RAND_NUM);
      int H = 1 + (int) (Math.random() * MAX_RAND_NUM);

      QuadTree quadTree = new QuadTree(new QuadTree.Rect(0, 0, W, H));
      int[][] grid = new int[H + 1][W + 1];

      for (int i = 0; i < TEST_SZ; i++) {
        int x = (int) (Math.random() * (W + 1));
        int y = (int) (Math.random() * (H + 1));
        assertThat(quadTree.add(x, y)).isTrue();
        grid[y][x]++;
        // System.out.printf("(%d, %d)\n",x,y);
      }

      // for (int i = H; i >= 0; i--) System.out.println(Arrays.toString(grid[i]));

      for (int i = 0; i < TEST_SZ; ) {

        int x1 = (int) (Math.random() * (W));
        int y1 = (int) (Math.random() * (H));
        int x2 = x1 + (int) (Math.random() * (W - x1));
        int y2 = y1 + (int) (Math.random() * (H - y1));

        // Make sure region is valid
        if (x1 <= x2 && y1 <= y2) {

          // System.out.printf("(%d, %d) (%d %d)\n", x1,y1,x2,y2);

          QuadTree.Rect region = new QuadTree.Rect(x1, y1, x2, y2);
          int expectedPts = bruteForceCount(grid, x1, y1, x2, y2);
          int quadTreeCount = quadTree.count(region);
          // System.out.printf("EXPECTED: %d, GOT: %d\n", expectedPts, quadTreeCount);
          assertThat(quadTreeCount).isEqualTo(expectedPts);

          // Increment because we have a valid region
          i++;
        }
      }
    }
  }

  boolean comparePT(QuadTree.Pt pt1, long x, long y) {
    return (pt1.x == x && pt1.y == y);
  }

  /** This test was already there but was commented out. Covers 77/192 40% */
  @Test
  public void testKNN1() {

    int W = 99, H = 99, NUM_NODES = 2;
    QuadTree quadTree = new QuadTree(new QuadTree.Rect(0, 0, W, H), NUM_NODES);

    int x = 46, y = 92, k = 7;

    // Cluster surrounding point
    quadTree.add(x, y - 1); // Below
    quadTree.add(x, y + 1); // Above
    quadTree.add(x - 1, y); // Left
    quadTree.add(x + 1, y); // Right

    // Noise points far away left from point in NW quadrant.
    quadTree.add(0, 77);
    quadTree.add(4, 56);
    quadTree.add(2, 80);
    quadTree.add(6, 60);
    quadTree.add(8, 90);

    // Noise points in quadrants
    quadTree.add(25, 25);
    quadTree.add(75, 25);
    quadTree.add(25, 75); // Target point in NW quadrant.
    quadTree.add(75, 75);

    // NE quadrant target points
    quadTree.add(52, y);
    quadTree.add(52, y + 1);
    quadTree.add(52, y - 1);

    List<QuadTree.Pt> points = quadTree.kNearestNeighbors(k, x, y);
    System.out.println(points);

    List<QuadTree.SortedPt> sPoints = new ArrayList<>();
    for (QuadTree.Pt p : quadTree.getPoints()) {
      sPoints.add(new QuadTree.SortedPt(Math.hypot(p.x - x, p.y - y), p));
    }
    Collections.sort(sPoints);
    for (QuadTree.SortedPt p : sPoints) {
      System.out.println(p);
    }
  }

  /** Checks the edge case of an empty tree. Total 79/192 41% (when executed with precious tests) */
  @Test
  public void testKNN2_EmptyTree() {

    int W = 200, H = 200, NUM_NODES = 6;
    QuadTree quadTree = new QuadTree(new QuadTree.Rect(0, 0, W, H), NUM_NODES);

    int x = 50, y = 50, k = 5;

    List<QuadTree.Pt> points = quadTree.kNearestNeighbors(k, x, y);
    assertThat(points.size() == 0);
  }

  /**
   * Tessts the case when all the points are located in NE. Total 99/192 51% (when executed with
   * precious tests)
   */
  @Test
  public void testKNN3_Points_In_NE() {

    int W = 199, H = 199, NUM_NODES = 2;
    QuadTree quadTree = new QuadTree(new QuadTree.Rect(0, 0, W, H), NUM_NODES);

    int x = 50, y = 50, k = 4;

    quadTree.add(110, 110);
    quadTree.add(120, 120);
    quadTree.add(130, 130);
    quadTree.add(140, 140);
    quadTree.add(150, 150);
    quadTree.add(160, 160);
    quadTree.add(170, 170);

    List<QuadTree.Pt> points = quadTree.kNearestNeighbors(k, x, y);
    System.out.println(points);

    List<QuadTree.SortedPt> sPoints = new ArrayList<>();
    for (QuadTree.Pt p : quadTree.getPoints()) {
      sPoints.add(new QuadTree.SortedPt(Math.hypot(p.x - x, p.y - y), p));
    }
    Collections.sort(sPoints);
    for (QuadTree.SortedPt p : sPoints) {
      System.out.println(p);
    }

    boolean[] found = new boolean[] {false, false, false, false};
    for (QuadTree.Pt pt : points) {
      if (comparePT(pt, 110, 110)) found[0] = true;
      else if (comparePT(pt, 120, 120)) found[1] = true;
      else if (comparePT(pt, 130, 130)) found[2] = true;
      else if (comparePT(pt, 140, 140)) found[3] = true;
    }
    assertThat(found[0] && found[1] && found[2] && found[3]);
  }

  /**
   * Tessts the case when all the points are located in SW. Total 107/192 55% (when executed with
   * precious tests)
   */
  @Test
  public void testKNN4_SW() {

    int W = 199, H = 199, NUM_NODES = 2;
    QuadTree quadTree = new QuadTree(new QuadTree.Rect(0, 0, W, H), NUM_NODES);

    int x = 150, y = 150, k = 4;

    quadTree.add(10, 10);
    quadTree.add(20, 20);
    quadTree.add(30, 30);
    quadTree.add(40, 40);
    quadTree.add(50, 50);
    quadTree.add(60, 60);
    quadTree.add(70, 70);

    List<QuadTree.Pt> points = quadTree.kNearestNeighbors(k, x, y);
    System.out.println(points);

    List<QuadTree.SortedPt> sPoints = new ArrayList<>();
    for (QuadTree.Pt p : quadTree.getPoints()) {
      sPoints.add(new QuadTree.SortedPt(Math.hypot(p.x - x, p.y - y), p));
    }
    Collections.sort(sPoints);
    for (QuadTree.SortedPt p : sPoints) {
      System.out.println(p);
    }

    boolean[] found = new boolean[] {false, false, false, false};
    for (QuadTree.Pt pt : points) {
      if (comparePT(pt, 70, 70)) found[0] = true;
      else if (comparePT(pt, 60, 60)) found[1] = true;
      else if (comparePT(pt, 50, 50)) found[2] = true;
      else if (comparePT(pt, 40, 40)) found[3] = true;
    }
    assertThat(found[0] && found[1] && found[2] && found[3]);
  }

  /**
   * Tessts the case when all the points are located in NW. Total 108/192 56% (when executed with
   * precious tests)
   */
  @Test
  public void testKNN5_NW() {

    int W = 199, H = 199, NUM_NODES = 2;
    QuadTree quadTree = new QuadTree(new QuadTree.Rect(0, 0, W, H), NUM_NODES);

    int x = 150, y = 50, k = 4;

    quadTree.add(90, 110);
    quadTree.add(80, 120);
    quadTree.add(70, 130);
    quadTree.add(60, 140);
    quadTree.add(50, 150);
    quadTree.add(40, 160);
    quadTree.add(30, 170);

    List<QuadTree.Pt> points = quadTree.kNearestNeighbors(k, x, y);
    System.out.println(points);

    List<QuadTree.SortedPt> sPoints = new ArrayList<>();
    for (QuadTree.Pt p : quadTree.getPoints()) {
      sPoints.add(new QuadTree.SortedPt(Math.hypot(p.x - x, p.y - y), p));
    }
    Collections.sort(sPoints);
    for (QuadTree.SortedPt p : sPoints) {
      System.out.println(p);
    }

    boolean[] found = new boolean[] {false, false, false, false};
    for (QuadTree.Pt pt : points) {
      if (comparePT(pt, 90, 110)) found[0] = true;
      else if (comparePT(pt, 80, 120)) found[1] = true;
      else if (comparePT(pt, 70, 130)) found[2] = true;
      else if (comparePT(pt, 60, 140)) found[3] = true;
    }
    assertThat(found[0] && found[1] && found[2] && found[3]);
  }

  /**
   * Tests the case when all points have the same distance. Total 108/192 56% (when executed with
   * precious tests)
   */
  @Test
  public void testKNN6_Same_Dist() {

    int W = 199, H = 199, NUM_NODES = 2;
    QuadTree quadTree = new QuadTree(new QuadTree.Rect(0, 0, W, H), NUM_NODES);

    int x = 100, y = 100, k = 4;

    quadTree.add(50, 50);
    quadTree.add(50, 150);
    quadTree.add(150, 150);
    quadTree.add(150, 50);

    List<QuadTree.Pt> points = quadTree.kNearestNeighbors(k, x, y);
    System.out.println(points);

    List<QuadTree.SortedPt> sPoints = new ArrayList<>();
    for (QuadTree.Pt p : quadTree.getPoints()) {
      sPoints.add(new QuadTree.SortedPt(Math.hypot(p.x - x, p.y - y), p));
    }
    Collections.sort(sPoints);
    for (QuadTree.SortedPt p : sPoints) {
      System.out.println(p);
    }

    boolean[] found = new boolean[] {false, false, false, false};
    for (QuadTree.Pt pt : points) {
      if (comparePT(pt, 50, 150)) found[0] = true;
      else if (comparePT(pt, 150, 150)) found[1] = true;
      else if (comparePT(pt, 150, 50)) found[2] = true;
      else if (comparePT(pt, 50, 50)) found[3] = true;
    }
    assertThat(found[0] && found[1] && found[2] && found[3]);
  }

  /**
   * Tests the case when the reference point is outside the region. Total 109/192 57% (when executed
   * with precious tests)
   */
  @Test
  public void testKNN7_Outside() {

    int W = 199, H = 199, NUM_NODES = 2;
    QuadTree quadTree = new QuadTree(new QuadTree.Rect(0, 0, W, H), NUM_NODES);

    int x = 300, y = 300, k = 4;

    quadTree.add(10, 10);
    quadTree.add(20, 20);
    quadTree.add(30, 30);
    quadTree.add(40, 40);
    quadTree.add(50, 50);
    quadTree.add(60, 60);
    quadTree.add(70, 70);

    List<QuadTree.Pt> points = quadTree.kNearestNeighbors(k, x, y);
    System.out.println(points);

    List<QuadTree.SortedPt> sPoints = new ArrayList<>();
    for (QuadTree.Pt p : quadTree.getPoints()) {
      sPoints.add(new QuadTree.SortedPt(Math.hypot(p.x - x, p.y - y), p));
    }
    Collections.sort(sPoints);
    for (QuadTree.SortedPt p : sPoints) {
      System.out.println(p);
    }

    boolean[] found = new boolean[] {false, false, false, false};
    for (QuadTree.Pt pt : points) {
      if (comparePT(pt, 70, 70)) found[0] = true;
      else if (comparePT(pt, 60, 60)) found[1] = true;
      else if (comparePT(pt, 50, 50)) found[2] = true;
      else if (comparePT(pt, 40, 40)) found[3] = true;
    }
    assertThat(found[0] && found[1] && found[2] && found[3]);
  }
}
