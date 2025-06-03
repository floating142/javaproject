package Maze_Game;

import java.util.*;

import Maze_Game.Config.Conf;

public class Maze {
    private static int row;// 行数
    private static int column;// 列数
    public static int[][] map;// 地图
    private static int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
    private static Random rand = new Random();

    // 初始化地图
    private static void init() {
        row = Conf.row;
        column = Conf.column;
        map= new int[row][column];
        for (int i = 0; i < row; i++) {
            Arrays.fill(map[i], 0);
        }
    }

    // 生成迷宫
    public static int[][] createMap(){
        init();

        List<int[]> wallList = new ArrayList<>();

        int startX = 2 * rand.nextInt((column - 1) / 2) + 1;
        int startY = 2 * rand.nextInt((row - 1) / 2) + 1;

        map[startY][startX] = 1;
        addWalls(startX, startY, wallList);

        while (!wallList.isEmpty()) {
            int[] wall = wallList.remove(rand.nextInt(wallList.size()));
            int x = wall[0];
            int y = wall[1];

            for (int[] d : dirs) {
                int x1 = x + d[0], y1 = y + d[1];
                int x2 = x - d[0], y2 = y - d[1];

                if (inBounds(x1, y1) && inBounds(x2, y2)) {
                    if (map[y1][x1] == 1 && map[y2][x2] == 0) {
                        map[y][x] = 1;
                        map[y2][x2] = 1;
                        addWalls(x2, y2, wallList);
                        break;
                    } else if (map[y2][x2] == 1 && map[y1][x1] == 0) {
                        map[y][x] = 1;
                        map[y1][x1] = 1;
                        addWalls(x1, y1, wallList);
                        break;
                    }
                }
            }
        }

        if(Conf.mode==1)addExtraPaths(0.06); // 添加额外路径
        
        return map;
    }

    // 添加墙体到列表
    private static void addWalls(int x, int y, List<int[]> wallList) {
        for (int[] dir : dirs) {
            int nx = x + dir[0], ny = y + dir[1];
            if (inBounds(nx, ny) && map[ny][nx] == 0) {
                wallList.add(new int[] { nx, ny });
            }
        }
    }

    // 判断坐标是否在边界内
    private static boolean inBounds(int x, int y) {
        return x > 0 && x < column - 1 && y > 0 && y < row - 1;
    }

    // 添加额外路径
    private static void addExtraPaths(double ratio) {
        int total = (row - 2) * (column - 2);
        int count = (int)(total * ratio);

        for (int i = 0; i < count; i++) {
            int x, y;
            do {
                x = rand.nextInt(column - 2) + 1;
                y = rand.nextInt(row - 2) + 1;
            } while (map[y][x] == 1 || !isWallBetweenPaths(x, y));
            map[y][x] = 1;
        }
    }

    // 判断墙是否在两条路径之间
    private static boolean isWallBetweenPaths(int x, int y) {
        if (map[y][x] != 0) return false;

        int paths = 0;
        for (int[] d : dirs) {
            int nx = x + d[0], ny = y + d[1];
            if (inBounds(nx, ny) && map[ny][nx] == 1) {
                paths++;
                if (paths > 2) return false;
            }
        }
        return paths == 2;
    }
    
    // 打印迷宫
    void print() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
}
