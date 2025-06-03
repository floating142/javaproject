package Maze_Game;

import javax.swing.JOptionPane;
import java.awt.event.*;
import Maze_Game.Config.*;

class MazeKeyListener implements KeyListener {

    public void keyPressed(KeyEvent e) {
        int nx = State.player.x;
        int ny = State.player.y;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> nx--;
            case KeyEvent.VK_DOWN -> nx++;
            case KeyEvent.VK_LEFT -> ny--;
            case KeyEvent.VK_RIGHT -> ny++;
            default -> {
                return;
            }
        }
        Tools.checkMove(nx, ny);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}

class GameActionListener implements ActionListener {
    GamePanel view;

    public void setview(GamePanel view) {
        this.view = view;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.restart) {
            Tools.resetGame();
        } else if (e.getSource() == view.pause) {
            Tools.enablePause();
        } else if (e.getSource() == view.nopause) {
            Tools.disablePause();
        }else if (e.getSource() == view.tip) {
            Tools.enableTip();
        } else if (e.getSource() == view.notip) {
            Tools.disableTip();
        } else if (e.getSource() == view.return1) {
            int option = JOptionPane.showConfirmDialog(view, "是否返回？", "返回", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                Tools.back();
            }
        }
    }

}

class StartActionListener implements ActionListener {
    StartPanel view;

    public void setview(StartPanel view) {
        this.view = view;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.customsize) {
            view.customLabel.setVisible(true);
            view.customRowSpinner.setVisible(true);
            view.customColumnSpinner.setVisible(true);
        } else if (e.getSource() == view.smallsize || e.getSource() == view.mediumsize
                || e.getSource() == view.bigsize) {
            view.customLabel.setVisible(false);
            view.customRowSpinner.setVisible(false);
            view.customColumnSpinner.setVisible(false);
        }

        if (e.getSource() == view.exitButton) {
            int option = JOptionPane.showConfirmDialog(view, "是否退出？", "退出", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }

        if (e.getSource() == view.startButton) {
            int mode = 0;
            if (view.nomalmode.isSelected()) {
                mode = 0;
            } else if (view.timemode.isSelected()) {
                mode = 1;
            }
            Conf.mode = mode;

            int row = 21, col = 21;
            if (view.smallsize.isSelected()) {
                row = col = 21;
            } else if (view.mediumsize.isSelected()) {
                row = col = 31;
            } else if (view.bigsize.isSelected()) {
                row = col = 41;
            } else if (view.customsize.isSelected()) {
                row = (Integer) view.customRowSpinner.getValue();
                col = (Integer) view.customColumnSpinner.getValue();
            }
            Conf.row = row;
            Conf.column = col;

            int viewSize = 0;
            if (view.openview.isSelected()) {
                viewSize = 0;
            } else if (view.smallview.isSelected()) {
                viewSize = 7;
            } else if (view.mediumview.isSelected()) {
                viewSize = 5;
            } else if (view.bigview.isSelected()) {
                viewSize = 3;
            }
            Conf.viewSize = viewSize;

            Tools.startGame();
        }
    }

}

class MazeMouseListener implements MouseListener {

    public void mousePressed(MouseEvent e) {
        int y = e.getX() / Conf.cellSize;
        int x = e.getY() / Conf.cellSize;
        int px = State.player.x;
        int py = State.player.y;
        if (x == px && y == py) {
            State.isMove = 1;
            return;
        }
    }

    public void mouseReleased(MouseEvent e) {
        State.isMove = 0;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

}

class MazeMouseMoveListener implements MouseMotionListener {

    public void mouseDragged(MouseEvent e) {
        if (State.isMove == 0)
            return;
        int ny = e.getX() / Conf.cellSize;
        int nx = e.getY() / Conf.cellSize;
        int px = State.player.x;
        int py = State.player.y;
        // 检查是否是相邻格子
        if (Math.abs(nx - px) + Math.abs(ny - py) == 1) {
            Tools.checkMove(nx, ny);
        }
    }

    public void mouseMoved(MouseEvent e) {
    }

}

class CheatCodeListener extends KeyAdapter {
    private final StringBuilder buffer = new StringBuilder();

    public void keyTyped(KeyEvent e) {
        buffer.append(e.getKeyChar());

        // 限制缓冲区大小
        if (buffer.length() > 20) {
            buffer.delete(0, buffer.length() - 20);
        }

        // 检查彩蛋码
        if (buffer.toString().toLowerCase().contains("ciallo")) {
            State.god=1;
            System.out.println("God mode activated!");
            buffer.setLength(0); // 重置缓冲
        }
    }
}