package maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import maze.Config.*;

public class Point {
    public static class ScorePoint {
        public int x, y;
    
        public ScorePoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
    } 
    
    public static List<ScorePoint> scorePoints =new ArrayList<>();
    public static List<int[]> path =new ArrayList<>();

    public static List<ScorePoint> createPoints(int num) {
        scorePoints.clear();
        Random rand = new Random();
        int minDistance = 4;
    
        while (scorePoints.size() < num) {
            int x = rand.nextInt(Conf.row);
            int y = rand.nextInt(Conf.column);
    
            if (State.map[x][y] == 1 && distance(1, 1, x, y) > 4 && distance(Conf.row, Conf.column, x, y) > 4) {
                boolean valid = true;
    
                for (ScorePoint p : scorePoints) {
                    int dist = distance(p.x, p.y, x, y);
                    if (dist < minDistance) {
                        valid = false;
                        break;
                    }
                }
    
                if (valid) {
                    scorePoints.add(new ScorePoint(x, y));
                }
            }
        }
        return scorePoints;
    }

    static int distance(int x1, int y1, int x2, int y2) {
        int dist = Math.abs(x1 - x2) + Math.abs(y1 - y2);
        return dist;
    }

    public static List<int[]> getPointPath(int px, int py) {
        ScorePoint nearest = null;
        int minLen = Integer.MAX_VALUE;
        List<int[]> bestPath = null;
    
        for (ScorePoint p : scorePoints) {
            path=Astar.buildPath(px, py, p.x, p.y);
    
            if (path != null && path.size() < minLen) {
                minLen = path.size();
                nearest = p;
                bestPath = path;
            }
        }
        if (nearest != null && bestPath != null) {
            path = bestPath;
        }
        return path;
    }

    void print() {
        for (ScorePoint p : scorePoints) {
            System.out.println("ScorePoint at (" + p.x + ", " + p.y + ")");
        }
    }

}
