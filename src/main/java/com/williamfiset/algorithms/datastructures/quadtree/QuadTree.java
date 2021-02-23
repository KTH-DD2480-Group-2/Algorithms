/**
 * A QuadTree implementation with integer coordinates.
 *
 * <p>NOTE: THIS FILE IS STILL UNDER DEVELOPMENT!
 *
 * @author William Fiset, william.alexandre.fiset@gmail.com
 */
package com.williamfiset.algorithms.datastructures.quadtree;

import com.williamfiset.algorithms.utils.BranchCoverageAnalyser;

import static java.lang.Double.POSITIVE_INFINITY;

import java.util.*;

public class QuadTree {

    private static int NORTH_EAST = 1;
    private static int NORTH_WEST = 2;
    private static int SOUTH_EAST = 3;
    private static int SOUTH_WEST = 4;

    private static boolean isNorth(int dir) {
        return dir == NORTH_EAST || dir == NORTH_WEST;
    }

    class Pt {
        long x, y;

        public Pt(long xx, long yy) {
            y = yy;
            x = xx;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    static class SortedPt implements Comparable<SortedPt> {
        Pt pt;
        double dist;

        public SortedPt(double dist, Pt pt) {
            this.dist = dist;
            this.pt = pt;
        }

        @Override
        public int compareTo(SortedPt other) {
            return Double.compare(dist, other.dist);
        }

        @Override
        public String toString() {
            return dist + " - " + pt;
        }
    }

    // Node that represents a regions with points inside this region.
    class Node {

        // Keeps track of how many points are currently
        // contained within this quad tree node.
        private int ptCount = 0;

        // Tracks the (x,y) coordinates of points within this quad tree node.
        private long[] X, Y;

        // Define four Quad Tree nodes to subdivide the region we're
        // considering into four parts: north west (nw), north east (ne),
        // south west(sw) and south east(se).
        private Node nw, ne, sw, se;

        // The region this node encompasses
        private Rect region;

        // Construct a quad tree for a particular region.
        public Node(Rect region) {
            if (region == null) throw new IllegalArgumentException("Illegal argument");
            this.region = region;
            X = new long[NUM_POINTS];
            Y = new long[NUM_POINTS];
        }

        // Try adding a point to the current region and if the
        // region is already full subdivide and recurse until
        // you are able to place the point inside a smaller region
        private boolean add(long x, long y) {

            // Point is not within this region.
            if (!region.contains(x, y)) return false;

            // The point is within this region and there is room for it.
            if (ptCount < NUM_POINTS) {

                X[ptCount] = x;
                Y[ptCount] = y;
                ptCount++;

                return true;

                // This region is full, so subdivide the region into four
                // quadrants and try adding the point to these new regions
            } else {

                // Find the center of this region at (cx, cy)
                long cx = (region.x1 + region.x2) / 2;
                long cy = (region.y1 + region.y2) / 2;

                // Lazily subdivide each of the regions into four parts
                // one by one as needed to save memory.

                if (sw == null) sw = new Node(new Rect(region.x1, region.y1, cx, cy));
                if (sw.add(x, y)) return true;

                if (nw == null) nw = new Node(new Rect(region.x1, cy, cx, region.y2));
                if (nw.add(x, y)) return true;

                if (ne == null) ne = new Node(new Rect(cx, cy, region.x2, region.y2));
                if (ne.add(x, y)) return true;

                if (se == null) se = new Node(new Rect(cx, region.y1, region.x2, cy));
                return se.add(x, y);
            }
        }

        // Count how many points are found within a certain rectangular region
        private int count(Rect area) {

            if (area == null || !region.intersects(area)) return 0;

            int count = 0;

            // The area we're considering fully contains
            // the region of this node, so simply add the
            // number of points within this region to the count
            if (area.contains(region)) {
                count = ptCount;

                // Our regions overlap, so some points in this
                // region may intersect with the area we're considering
            } else {
                for (int i = 0; i < ptCount; i++) if (area.contains(X[i], Y[i])) count++;
            }

            // Dig into each of the quadrants and count all points
            // which overlap with the area and sum their count
            if (nw != null) count += nw.count(area);
            if (ne != null) count += ne.count(area);
            if (sw != null) count += sw.count(area);
            if (se != null) count += se.count(area);

            return count;
        }

        private List<Pt> kNearestNeighbors(int k, long x, long y) {
            PriorityQueue<SortedPt> heap = new PriorityQueue<>(k, Collections.reverseOrder());
            knn(k, x, y, heap);

            List<Pt> neighbors = new ArrayList<>();
            for (SortedPt n : heap) neighbors.add(n.pt);
            return neighbors;
        }

        // Find the k-nearest neighbors.
        private void knn(int k, long x, long y, PriorityQueue<SortedPt> heap) {

            for (int i = 0; i < ptCount; i++) {
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_1");
                long xx = X[i], yy = Y[i];

                // Get largest radius.
                double radius = heap.isEmpty()
                        ? BranchCoverageAnalyser.markCoveredValue("QuadTree.knn.id_3", POSITIVE_INFINITY)
                        : BranchCoverageAnalyser.markCoveredValue("QuadTree.knn.id_4", heap.peek().dist);

                // Get distance from point to this point.
                double distance = Math.sqrt((xx - x) * (xx - x) + (yy - y) * (yy - y));

                // Add node to heap.
                if (heap.size() < k) {
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_5");
                    heap.add(new SortedPt(distance, new Pt(xx, yy)));
                } else if (distance < radius) {
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_6");
                    heap.poll();
                    // System.out.println("POLLED: " + heap.poll());
                    heap.add(new SortedPt(distance, new Pt(xx, yy)));
                } else {
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_7");
                }
            }
            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_2");

            int pointQuadrant = 0;

            // Dig to find the quadrant (x, y) belongs to.
            if (nw != null && region.contains(x, y)) {
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_8");
                nw.knn(k, x, y, heap);
                pointQuadrant = NORTH_WEST;
            } else if (ne != null && region.contains(x, y)) {
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_9");
                ne.knn(k, x, y, heap);
                pointQuadrant = NORTH_EAST;
            } else if (sw != null && region.contains(x, y)) {
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_10");
                sw.knn(k, x, y, heap);
                pointQuadrant = SOUTH_WEST;
            } else if (se != null && region.contains(x, y)) { // Use else clause?
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_11");
                se.knn(k, x, y, heap);
                pointQuadrant = SOUTH_EAST;
            } else {
                if (nw == null && region.contains(x, y))
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_12");
                else if (nw == null && !region.contains(x, y))
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_13");
                else if (nw != null && !region.contains(x, y))
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_14");
                else if (ne == null && region.contains(x, y))
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_15");
                else if (ne == null && !region.contains(x, y))
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_16");
                else if (ne != null && !region.contains(x, y))
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_17");
                else if (sw == null && region.contains(x, y))
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_18");
                else if (sw == null && !region.contains(x, y))
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_19");
                else if (sw != null && !region.contains(x, y))
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_20");
                else if (se == null && region.contains(x, y))
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_21");
                else if (se == null && !region.contains(x, y))
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_22");
                else if (se != null && !region.contains(x, y))
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_23");
            }

            if (pointQuadrant == 0) {
                // System.out.println("UNDEFINED QUADRANT?");
                // return;
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_24");
            } else {
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_25");
            }

            // Get largest radius.
            double radius = heap.isEmpty()
                    ? BranchCoverageAnalyser.markCoveredValue("QuadTree.knn.id_180",POSITIVE_INFINITY)
                    : BranchCoverageAnalyser.markCoveredValue("QuadTree.knn.id_181", heap.peek().dist);

            // Find the center of this region at (cx, cy)
            long cx = (region.x1 + region.x2) / 2;
            long cy = (region.y1 + region.y2) / 2;

            // Compute the horizontal (dx) and vertical (dy) distance from the
            // point (x, y) to the nearest cell.
            long dx = Math.abs(x - cx);
            long dy = Math.abs(y - cy);

            boolean checkHorizontalCell = radius >= dx;
            if (radius >= dx)
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_182");
            else
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_183");
            boolean checkVerticalCell = radius >= dy;
            if (radius >= dy)
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_184");
            else
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_185");
            boolean checkDiagonalCell = checkHorizontalCell && checkVerticalCell;
            if (checkHorizontalCell && checkVerticalCell) {
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_90");
            } else if (checkHorizontalCell && !checkVerticalCell) {
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_91");
            } else if (!checkHorizontalCell && checkVerticalCell) {
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_92");
            } else {
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_93");
            }

            // TODO(williamfiset): Refactor.
            if (heap.size() == k) {
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_26");

                if (isNorth(pointQuadrant)) {
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_28");
                    if (pointQuadrant == NORTH_WEST) {
                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_30");
                        if (checkHorizontalCell) {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_32");
                            if (ne != null) {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_34");
                                ne.knn(k, x, y, heap);
                            } else
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_35");
                        } else {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_33");
                        }
                        if (checkVerticalCell) {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_36");
                            if (sw != null) {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_38");
                                sw.knn(k, x, y, heap);
                            } else {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_39");
                            }
                        } else {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_37");
                        }
                        if (checkDiagonalCell) {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_40");
                            if (se != null) {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_42");
                                se.knn(k, x, y, heap);
                            } else {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_43");
                            }
                        } else {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_41");
                        }
                    } else {
                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_31");
                        if (checkHorizontalCell) {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_44");
                            if (nw != null) {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_46");
                                nw.knn(k, x, y, heap);
                            } else {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_47");
                            }
                        } else {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_45");
                        }
                        if (checkVerticalCell) {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_48");
                            if (se != null) {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_50");
                                se.knn(k, x, y, heap);
                            } else {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_51");
                            }
                        } else {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_49");
                        }
                        if (checkDiagonalCell) {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_192");
                            if (nw != null) {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_53");
                                nw.knn(k, x, y, heap);
                            } else {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_54");
                            }
                        } else {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_52");
                        }
                    }
                } else {
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_29");
                    if (pointQuadrant == SOUTH_WEST) {
                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_55");
                        if (checkHorizontalCell) {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_57");
                            if (se != null) {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_59");
                                se.knn(k, x, y, heap);
                            } else {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_60");
                            }
                        } else {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_58");
                        }
                        if (checkVerticalCell) {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_61");
                            if (nw != null) {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_63");
                                nw.knn(k, x, y, heap);
                            } else {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_64");
                            }
                        } else {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_62");
                        }
                        if (checkDiagonalCell) {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_65");
                            if (ne != null) {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_67");
                                ne.knn(k, x, y, heap);
                            } else {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_68");
                            }
                        } else {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_66");
                        }
                    } else {
                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_56");
                        if (checkHorizontalCell) {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_69");
                            if (sw != null) {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_71");
                                sw.knn(k, x, y, heap);
                            } else {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_72");
                            }
                        } else {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_70");
                        }
                        if (checkVerticalCell) {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_73");
                            if (ne != null) {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_75");
                                ne.knn(k, x, y, heap);
                            } else {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_76");
                            }
                        } else {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_74");
                        }
                        if (checkDiagonalCell) {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_77");
                            if (nw != null) {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_79");
                                nw.knn(k, x, y, heap);
                            } else {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_80");
                            }
                        } else {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_78");
                        }
                    }
                }

                // Still need to find k - heap.size() nodes!
            } else {
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_27");

                // explore all quadrants ?
                // Do it lazy? Inspect return val after each call?

                for (int quadrant = 1; quadrant <= 4; quadrant++) {
                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_81");

                    if (quadrant == pointQuadrant) {
                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_130");
                        continue;
                    } else {
                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_83");
                    }
                    radius = heap.isEmpty()
                            ? BranchCoverageAnalyser.markCoveredValue("QuadTree.knn.id_84", POSITIVE_INFINITY)
                            : BranchCoverageAnalyser.markCoveredValue("QuadTree.knn.id_85", heap.peek().dist);

                    checkHorizontalCell = radius >= dx;
                    if (radius >= dx)
                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_186");
                    else
                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_187");
                    checkVerticalCell = radius >= dy;
                    if (radius >= dy)
                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_188");
                    else
                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_189");

                    checkDiagonalCell = checkHorizontalCell && checkVerticalCell;
                    if (checkHorizontalCell && checkVerticalCell) {
                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_86");
                    } else if (checkHorizontalCell && !checkVerticalCell) {
                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_87");
                    } else if (!checkHorizontalCell && checkVerticalCell) {
                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_88");
                    } else {
                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_89");
                    }


                    // No validation
                    if (heap.size() != k) {
                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_94");
                        if (isNorth(pointQuadrant)) {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_96");
                            if (pointQuadrant == NORTH_WEST) {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_98");
                                if (ne != null) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_100");
                                    ne.knn(k, x, y, heap);
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_101");
                                }
                                if (sw != null) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_102");
                                    sw.knn(k, x, y, heap);
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_103");
                                }
                                if (se != null) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_104");
                                    se.knn(k, x, y, heap);
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_105");
                                }
                            } else {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_99");
                                if (nw != null) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_106");
                                    nw.knn(k, x, y, heap);
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_107");
                                }
                                if (se != null) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_108");
                                    se.knn(k, x, y, heap);
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_109");
                                }
                                if (nw != null) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_110");
                                    nw.knn(k, x, y, heap);
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_111");
                                }
                            }
                        } else {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_97");
                            if (pointQuadrant == SOUTH_WEST) {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_112");
                                if (se != null) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_114");
                                    se.knn(k, x, y, heap);
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_115");
                                }
                                if (nw != null) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_116");
                                    nw.knn(k, x, y, heap);
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_117");
                                }
                                if (ne != null) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_118");
                                    ne.knn(k, x, y, heap);
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_119");
                                }
                            } else {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_113");
                                if (sw != null) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_120");
                                    sw.knn(k, x, y, heap);
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_121");
                                }
                                if (ne != null) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_122");
                                    ne.knn(k, x, y, heap);
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_123");
                                }
                                if (nw != null) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_124");
                                    nw.knn(k, x, y, heap);
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_125");
                                }
                            }
                        }

                        // must intersect
                    } else {
                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_95");
                        if (isNorth(pointQuadrant)) {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_126");
                            if (pointQuadrant == NORTH_WEST) {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_127");
                                if (checkHorizontalCell) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_128");
                                    if (ne != null) {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_129");
                                        ne.knn(k, x, y, heap);
                                    } else {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_191");
                                    }
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_131");
                                }
                                if (checkVerticalCell) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_132");
                                    if (sw != null) {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_133");
                                        sw.knn(k, x, y, heap);
                                    } else {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_134");
                                    }
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_135");
                                }
                                if (checkDiagonalCell) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_136");
                                    if (se != null) {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_137");
                                        se.knn(k, x, y, heap);
                                    } else {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_138");
                                    }
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_139");
                                }
                            } else {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_140");
                                if (checkHorizontalCell) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_141");
                                    if (nw != null) {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_142");
                                        nw.knn(k, x, y, heap);
                                    } else {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_143");
                                    }
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_144");
                                }
                                if (checkVerticalCell) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_145");
                                    if (se != null) {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_146");
                                        se.knn(k, x, y, heap);
                                    } else {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_147");
                                    }
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_148");
                                }
                                if (checkDiagonalCell) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_149");
                                    if (nw != null) {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_150");
                                        nw.knn(k, x, y, heap);
                                    } else {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_151");
                                    }
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_152");
                                }
                            }
                        } else {
                            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_153");
                            if (pointQuadrant == SOUTH_WEST) {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_154");
                                if (checkHorizontalCell) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_155");
                                    if (se != null) {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_156");
                                        se.knn(k, x, y, heap);
                                    } else {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_157");
                                    }
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_158");
                                }
                                if (checkVerticalCell) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_159");
                                    if (nw != null) {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_160");
                                        nw.knn(k, x, y, heap);
                                    } else {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_161");
                                    }
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_162");
                                }
                                if (checkDiagonalCell) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_163");
                                    if (ne != null) {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_164");
                                        ne.knn(k, x, y, heap);
                                    } else {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_165");
                                    }
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_166");
                                }
                            } else {
                                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_167");
                                if (checkHorizontalCell) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_168");
                                    if (sw != null) {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_169");
                                        sw.knn(k, x, y, heap);
                                    } else {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_170");
                                    }
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_171");
                                }
                                if (checkVerticalCell) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_172");
                                    if (ne != null) {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_173");
                                        ne.knn(k, x, y, heap);
                                    } else {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_174");
                                    }
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_175");
                                }
                                if (checkDiagonalCell) {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_176");
                                    if (nw != null) {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_177");
                                        nw.knn(k, x, y, heap);
                                    } else {
                                        BranchCoverageAnalyser.markCovered("QuadTree.knn.id_178");
                                    }
                                } else {
                                    BranchCoverageAnalyser.markCovered("QuadTree.knn.id_179");
                                }
                            }
                        }
                    }
                } // for
                BranchCoverageAnalyser.markCovered("QuadTree.knn.id_82");
            } // if
            BranchCoverageAnalyser.markCovered("QuadTree.knn.id_190");
        } // method
    } // node

    public static class Rect {

        long x1, y1, x2, y2;

        // Define a rectangle as a pair of points (x1, y1) in the bottom left corner
        // and (x2, y2) in the top right corner of the rectangle.
        public Rect(long x1, long y1, long x2, long y2) {
            if (x1 > x2 || y1 > y2) throw new IllegalArgumentException("Illegal rectangle coordinates");
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        // Check for an intersection between two rectangles. The easiest way to do this is to
        // check if the two rectangles do not intersect and negate the logic afterwards.
        public boolean intersects(Rect r) {
            return r != null && !(r.x2 < x1 || r.x1 > x2 || r.y1 > y2 || r.y2 < y1);
        }

        // Check if a point (x, y) is within this rectangle, this
        // includes the boundary of the rectangle.
        public boolean contains(long x, long y) {
            return (x1 <= x && x <= x2) && (y1 <= y && y <= y2);
        }

        // Check if another rectangle is strictly contained within this rectangle.
        public boolean contains(Rect r) {
            return r != null && contains(r.x1, r.y1) && contains(r.x2, r.y2);
        }
    }

    // This is the maximum number of points each quad tree node can
    // sustain before it has to subdivide into four more regions.
    // This variable can have a significant impact on performance.
    final int NUM_POINTS;

    public static final int DEFAULT_NUM_POINTS = 16;

    // Root node of the quad tree. Public for testing.
    public Node root;

    public QuadTree(Rect region) {
        this.NUM_POINTS = DEFAULT_NUM_POINTS;
        root = new Node(region);
    }

    public QuadTree(Rect region, int pointsPerNode) {
        this.NUM_POINTS = pointsPerNode;
        root = new Node(region);
    }

    public boolean add(long x, long y) {
        return root.add(x, y);
    }

    public int count(Rect region) {
        return root.count(region);
    }

    public List<Pt> kNearestNeighbors(int k, long x, long y) {
        return root.kNearestNeighbors(k, x, y);
    }

    public List<Pt> getPoints() {
        List<Pt> points = new ArrayList<>();
        getPoints(root, points);
        return points;
    }

    private void getPoints(Node node, List<Pt> points) {
        if (node == null) return;
        for (int i = 0; i < node.ptCount; i++) points.add(new Pt(node.X[i], node.Y[i]));
        getPoints(node.nw, points);
        getPoints(node.ne, points);
        getPoints(node.sw, points);
        getPoints(node.se, points);
    }
}
