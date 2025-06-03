package Maze_Game;

import javax.swing.*;

import Maze_Game.Config.*;
import Maze_Game.Point.ScorePoint;

import java.awt.*;
import java.awt.event.*;

public class Tools {
    public static MazePanel mazePanel;
    public static GamePanel gamePanel;
    public static Window window;
    public static StartPanel startPanel;


    public static void setView(MazePanel view) {
        Tools.mazePanel = view;
    }
    public static void setView(GamePanel view) {
        Tools.gamePanel = view;
    }
    public static void setView(Window view) {
        Tools.window = view;
    }
    public static void setView(StartPanel view) {
        Tools.startPanel = view;
    }

    // 开始游戏
    public static void startGame(){
        startPanel.setVisible(false);
        window.gamePanel = new GamePanel();
        gamePanel.setVisible(true);
        window.add(gamePanel);
        mazePanel.init();
        window.setSize(Conf.column * Conf.cellSize + 500, Conf.row * Conf.cellSize + 200);
        startTimer();
    }
    
    // 重置游戏
    public static void resetGame() {
        mazePanel.init();
        gamePanel.scoreLabel.setText("得分 : " + State.player.score);
        if(Conf.mode == 1) {
            gamePanel.hpLabel.setText("生命值: " + State.player.hp);
            if(State.god == 1) gamePanel.hpLabel.setText("生命值: INF");
        }

        gamePanel.tip.setVisible(true);
        gamePanel.notip.setVisible(false);
        gamePanel.pause.setVisible(true);
        gamePanel.nopause.setVisible(false);
        mazePanel.repaint();
        mazePanel.requestFocusInWindow();
        startTimer();
    }

    // 返回开始界面
    public static void back(){
        State.timer.stop();
        gamePanel.setVisible(false);
        window.setSize(Conf.width, Conf.height);
        startPanel.setVisible(true);
    }

    // 启用暂停
    public static void enablePause() {
        State.isPaused = 1;
        gamePanel.pause.setVisible(false);
        gamePanel.nopause.setVisible(true);
        refreshView();
    }

    // 禁用暂停
    public static void disablePause() {
        State.isPaused = 0;
        gamePanel.pause.setVisible(true);
        gamePanel.nopause.setVisible(false);
        refreshView();
    }

    // 启用提示
    public static void enableTip() {
        State.showTip = 1;
        State.player.updatePath();
        gamePanel.tip.setVisible(false);
        gamePanel.notip.setVisible(true);
        refreshView();
    }

    // 禁用提示
    public static void disableTip() {
        State.showTip = 0;
        gamePanel.tip.setVisible(true);
        gamePanel.notip.setVisible(false);
        refreshView();
    }

    // 刷新视图
    public static void refreshView() {
        mazePanel.repaint();
        mazePanel.requestFocusInWindow();
    }

    // 启动计时器
    public static void startTimer() {
        stopTimer();
        
        if (Conf.mode == 0) {
            gamePanel.timeLabel.setText("用时: " + State.secondsPassed + " 秒");
            State.timer = new Timer(1000, e -> {
                if(State.isPaused == 1) return;
                State.secondsPassed++;
                gamePanel.timeLabel.setText("用时: " + State.secondsPassed + " 秒");
            });
        } else if (Conf.mode == 1) {
            if(State.god == 1)gamePanel.timeLabel.setText("剩余时间: INF 秒");
            else gamePanel.timeLabel.setText("剩余时间: " + State.timeleft + " 秒");
            State.timer = new Timer(1000, e -> {
                if(State.isPaused == 1) return;
                if(State.god == 0){
                    State.timeleft--;
                    gamePanel.timeLabel.setText("剩余时间: " + State.timeleft + " 秒");
                }
                
                if (State.timeleft == 0) {
                    stopTimer();
                    endGame(false);
                }

                for (Enemy enemy : State.enemies) {
                    enemy.moveTowards();
                    if(enemy.checkTouchesPlayer()) isTouch(enemy);;
                }

                mazePanel.repaint();
            });
        }
        State.timer.start();
    }
    
    // 关闭计时器
    public static void stopTimer() {
        if (State.timer != null) State.timer.stop();
    }

    // 处理游戏结束逻辑
    public static void endGame(boolean success) {
        stopTimer();
        int option = 0;
        if (Conf.mode == 0){
            option = JOptionPane.showConfirmDialog(mazePanel, "恭喜你到达终点！是否再来一局\n"+
            "用时: " + State.secondsPassed + " 秒\n"+"得分: " + State.player.score + " 分", "游戏结束", JOptionPane.YES_NO_OPTION);
        }
        
        if(Conf.mode ==1){
            if(success){
                option = JOptionPane.showConfirmDialog(mazePanel, "恭喜你到达终点！是否再来一局\n"+
            "得分: " + State.player.score + " 分", "游戏结束", JOptionPane.YES_NO_OPTION);
            }else{
                if(State.player.hp == 0){
                    option = JOptionPane.showConfirmDialog(mazePanel, "生命值耗尽，未到达终点！是否再来一局？"
                    , "游戏失败", JOptionPane.YES_NO_OPTION);
                }else
                    option = JOptionPane.showConfirmDialog(mazePanel, "时间结束，未到达终点！是否再来一局？"
                    , "游戏失败", JOptionPane.YES_NO_OPTION);
            }
        }
        if (option == JOptionPane.YES_OPTION) {
            resetGame();
        }else{
            back();
        }
    }    
    
    public static void checkMove(int nx, int ny) {
        if (State.isPaused == 1) return;
        if (State.player.moveTo(nx, ny)) {
            if(Conf.mode == 1)
            for (Enemy enemy : State.enemies) {
                if(State.player.checkTouchesEnemy(nx, ny, enemy)) isTouch(enemy);
            }
            gamePanel.scoreLabel.setText("得分: " + State.player.score);
            mazePanel.repaint();

            if (State.exitEnabled == 1 && nx == Conf.row - 2 && ny == Conf.column - 2) {
                Tools.endGame(true);
            }
        }
    }

    public static void isTouch(Enemy enemy) {
        if(State.god == 1) return;
        State.player.hp--;
        gamePanel.hpLabel.setText("生命值: " + State.player.hp );
        State.player.reset();
        enemy.reset();

        if (State.player.hp == 0) {
            Tools.endGame(false);
        }

    }

}

class Draw {
    public static void drawCell(Graphics g, int i, int j, int value) {
        int x = j * Conf.cellSize;
        int y = i * Conf.cellSize;
    
        switch (value) {
            case 0 -> g.setColor(Color.BLACK);   // 墙
            case 1 -> g.setColor(Color.WHITE);   // 路
        }
    
        g.fillRect(x, y, Conf.cellSize, Conf.cellSize);
        g.setColor(Color.GRAY);                   // 画格子线
        g.drawRect(x, y, Conf.cellSize, Conf.cellSize);
    }
    
    public static void drawFog(Graphics g, int i, int j) {
        int x = j * Conf.cellSize;
        int y = i * Conf.cellSize;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, Conf.cellSize, Conf.cellSize);
    }

    public static void drawView(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (State.showTip == 1 && State.path != null && State.path.size() > 1) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(8)); // 可调整线宽
            for (int i = 0; i < State.path.size() - 1; i++) {
                int[] p1 = State.path.get(i);
                int[] p2 = State.path.get(i + 1);
                if (checkView(p1[0], p1[1]) && checkView(p2[0], p2[1])) {
                    int x1 = p1[1] * Conf.cellSize + Conf.cellSize / 2;
                    int y1 = p1[0] * Conf.cellSize + Conf.cellSize / 2;
                    int x2 = p2[1] * Conf.cellSize + Conf.cellSize / 2;
                    int y2 = p2[0] * Conf.cellSize + Conf.cellSize / 2;
                    g2d.drawLine(x1, y1, x2, y2);
                }
            }
        }

        for (ScorePoint p : State.scorePoints) {
            int px = p.y * Conf.cellSize;
            int py = p.x * Conf.cellSize;
            if (checkView(p.x,p.y)){
                g.setColor(Color.MAGENTA); 
                g.fillOval(px + Conf.cellSize/4, py + Conf.cellSize/4, Conf.cellSize/2, Conf.cellSize/2);
            }
            
        }        
        
        g.setColor(Color.ORANGE);
        g.fillRect(State.player.y * Conf.cellSize, State.player.x * Conf.cellSize, Conf.cellSize, Conf.cellSize);

        if(State.exitEnabled==1) {
            g.setColor(Color.GREEN);
            g.fillRect((Conf.column-2) * Conf.cellSize, (Conf.row-2) * Conf.cellSize, Conf.cellSize, Conf.cellSize);
        }

        if (Conf.mode == 1) {
            g.setColor(Color.BLUE);
            for (Enemy enemy : State.enemies) {
                if(checkView(enemy.x,enemy.y))
                    g.fillRect(enemy.y * Conf.cellSize, enemy.x * Conf.cellSize, Conf.cellSize, Conf.cellSize);
            }
        }

    }

    public static boolean checkView(int x, int y){
        return (Conf.viewSize==0)||(Math.abs(x - State.player.x) <= Conf.viewSize && Math.abs(y - State.player.y) <= Conf.viewSize);
    }
}


class Create extends JPanel{
    public static JSpinner createSpinner(Container container, int x, int y, int w, int min, int max) {
        // 创建一个范围为 min 到 max，步长为 2 的 spinner
        SpinnerNumberModel model = new SpinnerNumberModel(min, min, max, 2);
        JSpinner spinner = new JSpinner(model);
    
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "0");
        spinner.setEditor(editor);
        ((JSpinner.NumberEditor) spinner.getEditor()).getTextField().setEditable(false);

        spinner.setBounds(x, y, w, 60);
        container.add(spinner);
    
        return spinner;
    }
    

    public static JButton createButton(Container container,String text, int x, int y, int w, ActionListener listener) {
        JButton button = new JButton(text);
        button.setBounds(x, y, w, 100);
        button.addActionListener(listener);
        container.add(button);
        return button;
    }
    
    public static JLabel createLabel(Container container,String text, int x, int y, int w) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, w, 100);
        container.add(label);
        return label;
    }

    public static JRadioButton CreateRadioButton(Container container,String text, int x, int y, int w,ButtonGroup group) {
        JRadioButton button = new JRadioButton(text);
        button.setBounds(x, y, w, 100);
        group.add(button);
        container.add(button);
        return button;
    }
    public static JRadioButton createRadioButton(Container container,String text, int x, int y, int w,ButtonGroup group) {
        return CreateRadioButton(container,text,x,y,w,group);
    }
    public static JRadioButton createRadioButton(Container container,String text, int x, int y, int w,ButtonGroup group,ActionListener listener) {
        JRadioButton button=CreateRadioButton(container,text,x,y,w,group);
        button.addActionListener(listener);
        return button;
    }
}
