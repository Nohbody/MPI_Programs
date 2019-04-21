import edu.princeton.cs.algs4.*;

import java.util.Iterator;

/**
 * Created by zora on 3/5/16.
 */
public class KdTree {
    private KdTreeSet pointSet;

    private class KdTreeSet {
        private int size;
        private Node root;

        private class Node {
            public Point2D point;
            public int layer;
            public Node ld;
            public Node ru;
        }

        private class MinPair {
            public Point2D point;
            public double dist;
        }

        public KdTreeSet() {
            size = 0;
            root = null;
        }

        public void add(Point2D p) {
            if (root == null) {
                root = new Node();
                root.point = p;
                root.layer = 0;
                root.ld = null;
                root.ru = null;
                size++;
            }
            else {
                search(root, p, true);
            }
        }

        public boolean contains(Point2D p) {
            if (root == null) {
                return false;
            } else {
                return search(root, p, false);
            }
        }

        public int size() { return size; }

        public Iterable<Point2D> range(RectHV rect) {
            Queue<Point2D> inRect = new Queue<Point2D>();
            searchRect(inRect, rect, root);
            return inRect;
        }

        public Point2D nearest(Point2D p) {
            if (contains(p)) return p;
            MinPair minPair = new MinPair();
            minPair.point = root.point;
            minPair.dist = p.distanceTo(root.point);
            double minDist = p.distanceTo(root.point);
            return searchNearest(root, minPair, p).point;
        }

        public void draw() {
            drawKdDivision(root, 0, 1, 0, 1);
        }

        private boolean search(Node node, Point2D p, boolean boolInsert) {
            if (node.point.equals(p)) return true;

            if ((node.layer % 2 == 0 && Point2D.X_ORDER.compare(p, node.point) <= 0) ||
                    (node.layer % 2 == 1 && Point2D.Y_ORDER.compare(p, node.point) <= 0)) {
                if (node.ld != null) { search(node.ld, p, boolInsert); }
                else if (boolInsert) {
                    node.ld = new Node();
                    node.ld.point = p;
                    node.ld.layer = node.layer + 1;
                    node.ld.ld = null;
                    node.ld.ru = null;
                    size++;
                    return true;
                }
                else { return false; }
            }
            else {
                if (node.ru != null) { search(node.ru, p, boolInsert); }
                else if (boolInsert) {
                    node.ru = new Node();
                    node.ru.point = p;
                    node.ru.layer = node.layer + 1;
                    node.ru.ld = null;
                    node.ru.ru = null;
                    size++;
                    return true;
                }
                else { return false; }
            }
            return false;
        }

        private void searchRect(Queue<Point2D> inRect, RectHV rect, Node node) {
            if (node == null) return;
            if (rect.contains(node.point)) {
                inRect.enqueue(node.point);
                searchRect(inRect, rect, node.ld);
                searchRect(inRect, rect, node.ru);

            }
            else if (node.layer % 2 == 0) {
                if (node.point.x() < rect.xmin()) searchRect(inRect, rect, node.ru);
                else if (node.point.x() >= rect.xmax()) searchRect(inRect, rect, node.ld);
                else {
                    searchRect(inRect, rect, node.ld);
                    searchRect(inRect, rect, node.ru);
                }
            }
            else {
                if (node.point.y() < rect.ymin()) searchRect(inRect, rect, node.ru);
                else if (node.point.x() >= rect.xmax()) searchRect(inRect, rect, node.ld);
                else {
                    searchRect(inRect, rect, node.ld);
                    searchRect(inRect, rect, node.ru);
                }
            }
            return;
        }

        private MinPair searchNearest(Node node, MinPair minPair, Point2D p) {
            if (node == null) return minPair;
            if (minPair.dist > p.distanceTo(node.point)) {
                minPair.point = node.point;
                minPair.dist = p.distanceTo(node.point);
            }
            if ((node.layer % 2 == 0 && p.x() - node.point.x() <= 0) ||
                    (node.layer % 2 == 1 && p.y() - node.point.y() <= 0)) {
                minPair = searchNearest(node.ld, minPair, p);
                if ((node.layer % 2 == 0 && minPair.dist >= node.point.x() - p.x()) ||
                        (node.layer % 2 == 1 && minPair.dist >= node.point.y() - p.y())) {
                    searchNearest(node.ru, minPair, p);
                }
            }
            else {
                minPair = searchNearest(node.ru, minPair, p);
                if ((node.layer % 2 == 0 && minPair.dist >= p.x() - node.point.x()) ||
                        (node.layer % 2 == 1 && minPair.dist >= p.y() - node.point.y())) {
                    searchNearest(node.ld, minPair, p);
                }
            }
            return minPair;
        }

        private void drawKdDivision(Node node, double xMin, double xMax, double yMin, double yMax) {
            if (node == null) return;
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            StdDraw.point(node.point.x(), node.point.y());
            if (node.layer %2 == 0) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius(.005);
                StdDraw.line(node.point.x(), yMin, node.point.x(), yMax);
                drawKdDivision(node.ld, xMin, node.point.x(), yMin, yMax);
                drawKdDivision(node.ru, node.point.x(), xMax, yMin, yMax);
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius(.005);
                StdDraw.line(xMin, node.point.y(), xMax, node.point.y());
                drawKdDivision(node.ld, xMin, xMax, yMin, node.point.y());
                drawKdDivision(node.ru, xMin, xMax, node.point.y(), yMax);
            }

        }
    }

    public KdTree() {
        pointSet = new KdTreeSet();
    }                              // construct an empty set of points

    public boolean isEmpty() {
        return (pointSet.size() == 0);
    }                     // is the set empty?

    public int size() {
        return pointSet.size();
    }                        // number of points in the set

    public void insert(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException("Value is Null");
        if (!contains(p)) { pointSet.add(p); }
    }             // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException("Value is Null");
        return pointSet.contains(p);
    }           // does the set contain point p?

    public void draw() {
        pointSet.draw();
    }                        // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.NullPointerException("Value is Null");
        return pointSet.range(rect);
    }            // all points that are inside the rectangle

    public Point2D nearest(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException("Value is Null");
        return (pointSet.nearest(p));
    }            // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {
        RectHV rect = new RectHV(0, 0, 0.6, 0.6);
        KdTree pointSet = new KdTree();
        Point2D[] points = new Point2D[5];
        points[0] = new Point2D(0.5, 0.3);
        points[1] = new Point2D(0.5, 0.3);
        points[2] = new Point2D(0.5, 0.3);
        points[3] = new Point2D(0.7, 0.8);
        points[4] = new Point2D(0.9, 0.8);
        Point2D testPoint = new Point2D(0.2, 0.5);
        for (int i = 0; i < 5; i++) {
            pointSet.insert(points[i]);
        }
        StdOut.println(pointSet.size());
    }
}
