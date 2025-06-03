package maze;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;

import maze.Point.ScorePoint;

public class Config {
    public static class Conf {
        public static int row;
        public static int column;
        public static int viewSize;
        public static int mode;
        public static int cellSize = 40;
        public static int width = 1500;
        public static int height = 1000;
    }

    public static class State {
        public static int[][] map;
        public static List<int[]> path;
        public static List<ScorePoint> scorePoints;
        public static Player player;
        public static List<Enemy> enemies;
        public static Timer timer;

        public static int secondsPassed;
        public static int timeleft;
        public static int exitEnabled;
        public static int isPaused;
        public static int showTip;
        public static int isMove;
        public static int god = 0;

        public State(){
            showTip = 0;
            isMove = 0;
            isPaused = 0;
            map = Maze.createMap();
            if (Conf.mode == 0) {
                secondsPassed = 0;
                exitEnabled = 1;
                scorePoints = Point.createPoints(10);
            } else {
                exitEnabled = 0;
                timeleft = Conf.row * Conf.column / 5;
                scorePoints = Point.createPoints(10);
                enemies = new ArrayList<>();
                enemies.add(new Enemy(1, Conf.column - 2));
                enemies.add(new Enemy(Conf.row - 2, 1)); 
            }
            if(god== 1 ) exitEnabled =1;
            player = new Player(1, 1);
            path = new ArrayList<int[]>();

        }
    }

    public static class Player {
        public int x, y;
        private final int startX, startY;
        public int hp;
        public int score;
    
        public Player(int startX, int startY) {
            this.startX = startX;
            this.startY = startY;
            this.hp = 5; 
            this.score = 0; 
            reset();
        }

        public void reset() {
            this.x = startX;
            this.y = startY;
        }

        public void addScore(int delta) {
            this.score += delta;
        }
    
        public boolean moveTo(int nx, int ny) {
            if (!isValidMove(nx, ny)) return false;
            this.x = nx;
            this.y = ny;
    
            checkScorePoints();
            updatePath();
            return true;
        }
    
        private boolean isValidMove(int nx, int ny) {
            return nx >= 0 && nx < Conf.row && ny >= 0 && ny < Conf.column && State.map[nx][ny] != 0;
        }
    
        private void checkScorePoints() {
            Iterator<ScorePoint> iterator = State.scorePoints.iterator();
            while (iterator.hasNext()) {
                ScorePoint p = iterator.next();
                if (this.x == p.x && this.y == p.y) {
                    iterator.remove();
                    addScore(10);
                    if (State.player.score >= 100) {
                        State.exitEnabled = 1;
                    }
                    break;
                }
            }
        }

        public boolean checkTouchesEnemy(int nx, int ny, Enemy enemy) {
            return nx == enemy.x && ny == enemy.y;
        }
    
        public void updatePath() {
            if (State.exitEnabled == 0) {
                State.path = Point.getPointPath(this.x, this.y);
            } else {
                State.path = Astar.buildPath(this.x, this.y, Conf.row - 2, Conf.column - 2);
            }
        }
    }

    public static class Enemy {
        public int x, y;
        private final int startX, startY;

        public Enemy(int startX, int startY) {
            this.startX = startX;
            this.startY = startY;
            reset();
        }

        public void reset() {
            this.x = startX;
            this.y = startY;
        }

        public void moveTowards() {
            List<int[]> path = Astar.buildPath(x, y, State.player.x,  State.player.y);
            if (path != null && path.size() > 1) {
                int[] next = path.get(1); 
                this.x = next[0];
                this.y = next[1];
            }
        }

        public boolean checkTouchesPlayer() {
            return this.x == State.player.x && this.y == State.player.y;
        }
    }
}
