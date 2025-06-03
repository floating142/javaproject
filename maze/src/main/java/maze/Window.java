package maze;

import javax.swing.*;

import maze.Config.*;

import java.awt.*;

public class Window extends JFrame{
    GamePanel gamePanel;
    StartPanel startPanel;

    public Window(String s,int x,int y,int w,int h){
        init();
        setTitle(s);
        setLocation(x,y);
        setSize(w,h);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void init(){
        Tools.setView(this);

        startPanel = new StartPanel();
        add(startPanel);
    }
}

class StartPanel extends JPanel {
    JLabel titleLabel;

    JButton startButton;
    JButton exitButton;

    JRadioButton nomalmode;
    JRadioButton timemode;
    ButtonGroup modeGroup;

    JRadioButton smallsize;
    JRadioButton mediumsize;
    JRadioButton bigsize;
    JRadioButton customsize;
    ButtonGroup sizeGroup;
    
    JLabel customLabel;
    JSpinner customRowSpinner;
    JSpinner customColumnSpinner;

    JRadioButton openview;
    JRadioButton smallview;
    JRadioButton mediumview;
    JRadioButton bigview;
    ButtonGroup viewGroup;

    JLabel modeLabel;
    JLabel sizeLabel;
    JLabel viewLabel;

    StartActionListener actionlistener;
    CheatCodeListener cheatCodeListener;

    void init(){
        Tools.setView(this);
        setLayout(null);
        setVisible(true); 
        setFocusable(true);  
    }

    public StartPanel() {
        init();

        actionlistener = new StartActionListener();
        actionlistener.setview(this);
        cheatCodeListener = new CheatCodeListener();
        addKeyListener(cheatCodeListener);

        titleLabel = Create.createLabel(this,"Maze Game",450, 50, 800);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 100));

        modeLabel = Create.createLabel(this,"游戏模式选择:",150, 300, 300);
        viewLabel = Create.createLabel(this,"视野范围选择:",150, 400, 300);
        sizeLabel = Create.createLabel(this,"地图大小选择:",150, 500, 300);

        startButton = Create.createButton(this,"开始游戏", 250, 700, 400, actionlistener);
        exitButton = Create.createButton(this,"退出游戏", 850, 700, 400, actionlistener);

        modeGroup = new ButtonGroup();
        nomalmode =Create.createRadioButton(this,"普通模式",450, 300, 200, modeGroup, actionlistener);
        timemode = Create.createRadioButton(this,"限时模式",650, 300, 200, modeGroup, actionlistener);
        nomalmode.setSelected(true);

        sizeGroup = new ButtonGroup();
        smallsize = Create.createRadioButton(this,"21*21",450, 500, 200, sizeGroup, actionlistener);
        mediumsize = Create.createRadioButton(this,"31*31",650, 500, 200, sizeGroup, actionlistener);
        bigsize = Create.createRadioButton(this,"41*41",850, 500, 200, sizeGroup, actionlistener);
        customsize = Create.createRadioButton(this,"自定义",1050, 500, 150, sizeGroup, actionlistener);
        customsize.addActionListener(actionlistener);
        smallsize.setSelected(true);

        customLabel = Create.createLabel(this,"*",1280, 500, 50);
        customRowSpinner = Create.createSpinner(this, 1200, 520, 75, 21, 41);
        customColumnSpinner = Create.createSpinner(this, 1300, 520, 75, 21 ,61);

        
        customLabel.setVisible(false);
        customRowSpinner.setVisible(false);
        customColumnSpinner.setVisible(false);

        viewGroup = new ButtonGroup();
        openview = Create.createRadioButton(this,"all",450, 400, 100,viewGroup);
        smallview = Create.createRadioButton(this,"7",550, 400, 100,viewGroup);
        mediumview = Create.createRadioButton(this,"5",650, 400, 100,viewGroup);
        bigview = Create.createRadioButton(this,"3",750, 400, 100,viewGroup);
        openview.setSelected(true);

        
    }

}

class GamePanel extends JPanel{
    MazePanel mazePanel;

    JButton restart;
    JButton tip;
    JButton notip;
    JButton pause;
    JButton nopause;
    JButton return1;

    GameActionListener actionlistener;

    JLabel timeLabel;
    JLabel scoreLabel;
    JLabel hpLabel;

    int Size;

    void init(){
        Tools.setView(this);
        setLayout(null);

        mazePanel=new MazePanel();
        mazePanel.setBounds(0, 0, Conf.column * Conf.cellSize, Conf.row * Conf.cellSize);
        add(mazePanel);

        Size = Conf.cellSize * Conf.column + 50;
    }

    GamePanel(){
        init();
        actionlistener = new GameActionListener();
        actionlistener.setview(this);

        timeLabel = Create.createLabel(this, "", Size,  0, 400);
        scoreLabel = Create.createLabel(this, "得分 : "+State.player.score, Size,  60, 400);
        if(Conf.mode==1){ 
            hpLabel = Create.createLabel(this, "生命值 : "+State.player.hp, Size, 120, 400);
            if(State.god == 1) hpLabel.setText("生命值: INF");
        }

        restart = Create.createButton(this,"重新开始", Size, 500, 400, actionlistener);
        pause = Create.createButton(this,"暂停", Size, 350, 400, actionlistener);
        nopause = Create.createButton(this,"继续", Size, 350, 400, actionlistener);
        nopause.setVisible(false);
        tip = Create.createButton(this,"提示", Size, 200,400, actionlistener);
        notip = Create.createButton(this,"取消提示", Size, 200,400, actionlistener);
        notip.setVisible(false);
        return1 = Create.createButton(this,"返回", Size, 650,400, actionlistener);
    }
    
}

class  MazePanel extends JPanel{

    MazeKeyListener keylistener;
    MazeMouseListener mouselistener;
    MazeMouseMoveListener mousemovelistener;
    
    void init(){
        Tools.setView(this);
        setPreferredSize(new Dimension(Conf.column * Conf.cellSize, Conf.row * Conf.cellSize));
        setBackground(Color.white);
        setVisible(true);
        setFocusable(true);
        requestFocusInWindow();

        new State();

    }

    MazePanel(){
        setLayout(null);
        init();
        JFrame topLevelFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topLevelFrame != null) {
            topLevelFrame.setSize(Conf.column * Conf.cellSize + 200, Conf.row * Conf.cellSize + 100);
        }

        keylistener =new MazeKeyListener();
        mouselistener=new MazeMouseListener();
        mousemovelistener=new MazeMouseMoveListener();

        addKeyListener(keylistener);
        addMouseListener(mouselistener);
        addMouseMotionListener(mousemovelistener);

    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < Conf.row; i++) {
            for (int j = 0; j < Conf.column; j++) {
                if (Draw.checkView(i,j)) {
                    Draw.drawCell(g, i, j, State.map[i][j]); 
                } else {
                    Draw.drawFog(g, i, j); 
                }
            }
        }
        Draw.drawView(g);
    }
}
