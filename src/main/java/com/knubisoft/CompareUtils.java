package com.knubisoft;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class CompareUtils {
    public BufferedImage compareImages(BufferedImage img1, BufferedImage img2) {
        List<Point> points = new ArrayList<>();
        findDifferentPoints(img1, img2, points);
        List<List<Point>> groups = new ArrayList<>();
        formGroups(points, groups);
        buildRectangles(img2, groups);
        return img2;
    }

    private void findDifferentPoints(BufferedImage img1, BufferedImage img2, List<Point> points){
        int w1 = img1.getWidth();
        int h1 = img1.getHeight();
        for (int y = 0; y < h1; y++){
            for (int x = 0; x < w1; x++){
                int pixel1 = img1.getRGB(x, y);
                int pixel2 = img2.getRGB(x, y);
                if (pixel1 != pixel2){
                    points.add(new Point(x, y));
                }
            }
        }
    }

    private void formGroups(List<Point> points, List<List<Point>> groups) {
        Comparator<Point> pointComparator = Comparator.comparingInt(Point::getX);
        points.sort(pointComparator);
        while (!points.isEmpty()){
            List<Point> group = new ArrayList<>();
            Point firstPoint = points.get(0);
            for (Point p : points){
                if (calculateDistance(firstPoint.getX(), firstPoint.getY(), p.getX(), p.getY()) < 10){
                    group.add(p);
                }
            }
            points.removeAll(group);
            mergeOrAddNewGroup(groups, group);
        }
    }

    private double calculateDistance(int x, int y, int x1, int y1) {
        return Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2));
    }

    private void mergeOrAddNewGroup(List<List<Point>> groups, List<Point> group) {
        boolean flag = false;
        for (List<Point> list : groups){
            for (Point fmp : group){
                for (Point p : list){
                    if (calculateDistance(fmp.getX(), fmp.getY(), p.getX(), p.getY()) < 30){
                        flag = true;
                        list.addAll(group);
                        break;
                    }
                }
                if (flag){
                    break;
                }
            }
            if (flag){
                break;
            }
        }
        if (!flag){
            groups.add(group);
        }
    }

    private void buildRectangles(BufferedImage img2, List<List<Point>> groups) {
        for (List<Point> list : groups){
            int mostLeft = Integer.MAX_VALUE;
            int mostTop = Integer.MAX_VALUE;
            int mostBottom = 0;
            int mostRight = 0;
            for (Point p : list){
                mostLeft = Math.min(p.getX(), mostLeft);
                mostTop = Math.min(p.getY(), mostTop);
                mostRight = Math.max(p.getX(), mostRight);
                mostBottom = Math.max(p.getY(), mostBottom);
            }
            highlight(img2, mostLeft, mostTop, mostBottom, mostRight);
        }
    }

    private void highlight(BufferedImage img2, int mostLeftPoint, int mostTop, int mostBottom, int mostRightPoint) {
        int width = mostRightPoint - mostLeftPoint;
        int height = mostBottom - mostTop;
        Graphics2D g2d = img2.createGraphics();
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.RED);
        g2d.drawRect(mostLeftPoint - 5, mostTop - 5, width + 10, height + 10);
        g2d.dispose();
    }
}