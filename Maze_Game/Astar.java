package Maze_Game;

import java.util.*;

import Maze_Game.Config.*;

public class Astar {
    private static int[][] map;
    private static int STEP = 10;// 每一步的代价
    private static int row;
    private static int column;
    private static Node startNode;
    private static Node endNode;

    private static PriorityQueue<Node> openList = new PriorityQueue<>((a, b) -> {
        if (a.F != b.F) return Integer.compare(a.F, b.F);
        if (a.H != b.H) return Integer.compare(a.H, b.H); 
        if (a.x != b.x) return Integer.compare(a.x, b.x);
        return Integer.compare(a.y, b.y);
    });
    private static HashSet<Node> closeList = new HashSet<>();
    private static HashMap<String, Node> openMap = new HashMap<>();
    private static int[][] dir = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    public static List<int[]> path;

    private static class Node {
        int x, y;
        int G;
        int H;
        int F;
        Node parent;

        public void getF() {
            this.F = this.G + this.H;
        }

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node node = (Node) obj;
            return x == node.x && y == node.y;
        }

        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static List<int[]> buildPath(int sx, int sy, int ex, int ey) {
        init(sx, sy, ex, ey);
        Node end = findPath(startNode, endNode);
        if (end == null) return null;

        Node curr = end;
        while (curr != null) {
            path.add(0, new int[]{curr.x, curr.y});
            curr = curr.parent;
        }
        return path;
    }
    // 初始化地图和起点终点
    private static void init(int nx, int ny, int ex, int ey) {
        map = State.map;
        row = Conf.row;
        column = Conf.column;

        openList.clear();
        closeList.clear();
        openMap.clear();

        startNode = new Node(nx, ny);
        endNode = new Node(ex, ey);
        path = new ArrayList<>();
    }

    // 寻找路径
    private static Node findPath(Node startNode, Node endNode) {
        openList.add(startNode);
        openMap.put(getKey(startNode), startNode);

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            openMap.remove(getKey(currentNode));
            closeList.add(currentNode);

            if (currentNode.x == endNode.x && currentNode.y == endNode.y) {
                return currentNode;
            }

            ArrayList<Node> neighborNodes = findNeighborNodes(currentNode);
            for (Node neighborNode : neighborNodes) {
                if (closeList.contains(neighborNode)) {
                    continue;
                }
                if (!openMap.containsKey(getKey(neighborNode))) {
                    neighborNode.G = getG(startNode, neighborNode);
                    neighborNode.H = getH(endNode, neighborNode);
                    neighborNode.getF();
                    openList.add(neighborNode);
                } else {
                    Node node = openMap.get(getKey(neighborNode));
                    int G = getG(startNode, neighborNode);
                    if (G < node.G) {
                        node.parent = currentNode;
                        node.G = G;
                        node.getF();

                        openList.remove(node);
                        openList.add(node);
                    }
                }
            }
        }
        return null;
    }

    // 获取节点的唯一键值
    private static String getKey(Node node) {
        return node.x + "," + node.y;
    }

    // 获取当前节点的邻居节点
    private static ArrayList<Node> findNeighborNodes(Node currentNode) {
        ArrayList<Node> neighborNodes = new ArrayList<>();
        for (int[] d : dir) {
            int x = currentNode.x + d[0];
            int y = currentNode.y + d[1];
            if (isValidNode(x, y)) {
                Node node = new Node(x, y);
                node.parent = currentNode;
                neighborNodes.add(node);
            }
        }
        return neighborNodes;
    }

    // 检查节点是否有效
    private static boolean isValidNode(int x, int y) {
        return x >= 0 && x < row && y >= 0 && y < column && map[x][y] == 1;
    }

    // 计算 G 值（从起点到当前节点的代价）
    private static int getG(Node start, Node node) {
        int G = STEP;
        int parentG = node.parent != null ? node.parent.G : 0;
        return G + parentG;
    }

    // 计算 H 值（从当前节点到终点的估算代价）
    private static int getH(Node end, Node node) {
        int step = Math.abs(node.x - end.x) + Math.abs(node.y - end.y);
        return step * STEP;
    }

    // 打印地图
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