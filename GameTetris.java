import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.*;

public final class GameTetris extends JFrame {
    private Panel mainPanel = new Panel();
    private Figure figure = new Figure(0);
    private Figure nextFigure = new Figure(340);
    private JButton buttonExit = new JButton("Exit");
    private JButton buttonNewGame = new JButton("New Game");
    private JButton buttonTop = new JButton("Top");
    boolean gameOver = false;
    private final String TITLE_OF_GAME = "Tetris";
    private final int WINDOW_WIDTH = 600;
    private final int WINDOW_HEIGHT = 650;
    private final int WINDOW_INDENT = 300;
    final int BLOCK_SIZE = 25;
    final int GAME_FIELD_WIDTH = 250;
    final int GAME_FIELD_HEIGHT = 500;
    final int FIELD_INDENT = 50;
    final int ARC_RADIUS = 5;
    final int BUTTON_X = 400;
    final int BUTTON_Y = 400;
    final int BUTTON_WIDTH = 100;
    final int BUTTON_HEIGHT = 30;
    final int UP = 38;
    final int LEFT = 37;
    final int RIGHT = 39;
    final int DOWN = 32;
    final int STEP_DOWN = 40;
    final int ESC = 27;
    final int F1 = 112;
    final int F2 = 113;
    final static int MINUTE = 60_000;
    static int score = 0;
    int delay = 400;
    int allTime = 0;
    int lvl = 1;
    int mirrorY = 0;
    boolean pause = false;
    boolean tapDown = false;
    volatile boolean firstMoveCall = false;
    volatile boolean pressedMoveLeft = false;
    volatile boolean pressedMoveRight = false;
    static ArrayList<Integer[][]> scoreARR;
    static ArrayList<Integer[][]> lvlARR;
    static ArrayList<Integer[][]> top10;
    static ArrayList<ArrayList<Integer[][]>> topNumARR = new ArrayList<>();
    static LinkedList<Integer> topNum;
    Timer delayTimer;
    static final int[][][] FIGURES = {
            {{0, 1, 0, 0}, {0, 1, 0, 0}, {0, 1, 0, 0}, {0, 1, 0, 0}},
            {{0, 1, 0, 0}, {0, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}},
            {{0, 0, 0, 0}, {0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}},
            {{0, 0, 0, 0}, {0, 1, 1, 0}, {1, 1, 0, 0}, {0, 0, 0, 0}},
            {{0, 0, 0, 0}, {0, 1, 1, 0}, {0, 0, 1, 1}, {0, 0, 0, 0}},
            {{0, 0, 1, 0}, {0, 0, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}},
            {{0, 0, 0, 0}, {0, 0, 1, 0}, {0, 1, 1, 1}, {0, 0, 0, 0}}
    };
    static final int[][] NEXT_FIGURE = {
            {1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0},
            {1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
            {1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0}
    };
    static final int[][] LVL = {
            {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
            {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1},
            {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1},
            {1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0}
    };
    static final int[][] TOP = {
            {1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0},
            {0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1},
            {0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0},
            {0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1},
            {0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0}
    };
    final int[][] GAME_OVER_MSG = {
            {0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0},
            {1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1},
            {1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0},
            {0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0},
            {1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0},
            {1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0},
            {1, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0},
            {0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0}};
    final int[][] SCORE_MSG = {
            {0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0},
            {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1},
            {0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0},
            {0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1},
            {1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0}};
    static final Color[] COLORS = {
            Color.GREEN, Color.BLUE, Color.ORANGE,
            Color.RED, Color.MAGENTA, Color.PINK, Color.CYAN
    };
    static final Color AQUA = new Color(0, 200, 255);
    int[][] cells = new int
            [GAME_FIELD_HEIGHT / BLOCK_SIZE + 3]
            [GAME_FIELD_WIDTH / BLOCK_SIZE];

    GameTetris() {
        createFrameAndPanel();
        createButtons();
        mainPanel.add(buttonNewGame);
        mainPanel.add(buttonTop);
        mainPanel.add(buttonExit);
        setVisible(true);
        delayTimer = new Timer(delay, e -> {
            if (!gameOver) {
                figure.mirror();
                repaint(0, 0,
                        GAME_FIELD_WIDTH + 60, GAME_FIELD_HEIGHT + 90);
                if (figure.isFalls) {
                    figure.stepDown();
                } else {
                    if (!tapDown) {
                        figure.stepDown();
                        figure.isMoving = false;
                        figure.stepDown();
                        while (!figure.thatsAll) {
                            figure.stepDown();
                        }
                    }
                    checkWall();
                    figure = nextFigure;
                    nextFigure.paintShift = 0;
                    nextFigure = new Figure(340);
                    figure.mirror();
                    tapDown = false;
                    repaint(nextFigure.paintShift + 10, 0,
                            230, 230);
                }
            } else {
                delayTimer.stop();
            }
            allTime += delay;
            if (allTime >= MINUTE) {
                delay = (int) (delay * 0.75);
                delayTimer.setDelay(delay);
                allTime = 0;
                lvl++;
                lvlARR = BitNumber.convert(lvl);
            }
        });
        delayTimer.start();
        Thread checkMoves = new Thread(() -> {
            while (true) {
                try {
                    if (firstMoveCall) {
                        Thread.sleep(150);
                        firstMoveCall = false;
                    } else {
                        Thread.sleep(60);
                    }
                } catch (InterruptedException e) {
                    continue;
                }
                if (!tapDown) {
                    if (pressedMoveRight) {
                        keyMove(RIGHT);
                    }
                    if (pressedMoveLeft) {
                        keyMove(LEFT);
                    }
                }
            }
        });
        checkMoves.setDaemon(true);
        checkMoves.start();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                if (!pause) {
                    if (e.getKeyCode() == LEFT || e.getKeyCode() == RIGHT) {
                        pressedMoveLeft = false;
                        pressedMoveRight = false;
                    }
                }
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                if (!pause) {
                    if (e.getKeyCode() == UP) {
                        keyUp();
                    }
                    if (e.getKeyCode() == LEFT || e.getKeyCode() == RIGHT) {
                        firstMoveCall = true;
                        if (e.getKeyCode() == LEFT) {
                            pressedMoveLeft = true;
                        } else {
                            pressedMoveRight = true;
                        }
                    }
                    if (e.getKeyCode() == DOWN) {
                        do {
                            figure.stepDown();
                        } while (figure.isFalls);
                        tapDown = true;
                        figure.isMoving = false;
                        figure.stepDown();
                    }
                    if (e.getKeyCode() == STEP_DOWN) {
                        figure.stepDown();
                    }
                }
                if (e.getKeyCode() == ESC) {
                    exit();
                }
                if (e.getKeyCode() == F1) {
                    saveResult();
                    newGame();
                }
                if (e.getKeyCode() == F2) {
                    showTop();
                }
            }
        });

        convertAndFill();
    }

    private void convertAndFill() {
        Arrays.fill(cells[GAME_FIELD_HEIGHT / BLOCK_SIZE + 2], 1);
        scoreARR = BitNumber.convert(score);
        lvlARR = BitNumber.convert(lvl);
        top10 = BitNumber.convert(123456789);
        top10.add(BitNumber.TEN);
        topNum = WorkWithIO.read();
        if (topNum != null) {
            for (Integer num : topNum) {
                topNumARR.add(BitNumber.convert(num));
            }
        }
    }

    private void createFrameAndPanel() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        setTitle(TITLE_OF_GAME);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(final WindowEvent e) {

            }

            @Override
            public void windowClosing(final WindowEvent e) {
                exit();
            }

            @Override
            public void windowClosed(final WindowEvent e) {

            }

            @Override
            public void windowIconified(final WindowEvent e) {

            }

            @Override
            public void windowDeiconified(final WindowEvent e) {

            }

            @Override
            public void windowActivated(final WindowEvent e) {

            }

            @Override
            public void windowDeactivated(final WindowEvent e) {

            }
        });
        setBounds(WINDOW_INDENT, WINDOW_INDENT, WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        add(mainPanel);
        setContentPane(mainPanel);
        mainPanel.setLayout(null);
    }

    private void createButtons() {
        buttonNewGame.setBounds(BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        buttonNewGame.addActionListener(e -> newGame());
        buttonNewGame.setForeground(Color.BLACK);
        buttonNewGame.setBackground(AQUA);
        buttonNewGame.setFocusable(false);

        buttonTop.setBounds(BUTTON_X, BUTTON_Y + 50, BUTTON_WIDTH, BUTTON_HEIGHT);
        buttonTop.addActionListener(e -> showTop());
        buttonTop.setForeground(Color.BLACK);
        buttonTop.setBackground(AQUA);
        buttonTop.setFocusable(false);

        buttonExit.setBounds(BUTTON_X, BUTTON_Y + 100, BUTTON_WIDTH, BUTTON_HEIGHT);
        buttonExit.addActionListener(e -> exit());
        buttonExit.setForeground(Color.BLACK);
        buttonExit.setBackground(AQUA);
        buttonExit.setFocusable(false);
    }

    private void showTop() {
        if (pause) {
            delayTimer.start();
            pause = false;
        } else {
            delayTimer.stop();
            pause = true;
        }
        repaint();
    }

    private void saveResult() {
        if (topNum == null) {
            topNum = new LinkedList<>(Collections.nCopies(10, 0));
        }
        if (topNum.size() != 0 && score > topNum.getLast()) {
            if (topNum.size() >= 10) {
                topNum.set(topNum.size() - 1, score);
            } else {
                topNum.add(score);
            }
            Collections.sort(topNum);
            Collections.reverse(topNum);
            topNumARR = new ArrayList<>();
            for (Integer num : topNum) {
                topNumARR.add(BitNumber.convert(num));
            }
        } else if (score > topNum.getLast()) {
            if (topNum.size() >= 10) {
                topNum.set(topNum.size() - 1, score);
            } else {
                topNum.add(score);
            }
            Collections.sort(topNum);
            Collections.reverse(topNum);
            topNumARR = new ArrayList<>();
            for (Integer num : topNum) {
                topNumARR.add(BitNumber.convert(num));
            }
        }
    }

    private void exit() {
        saveResult();
        if (topNum != null) {
            WorkWithIO.write(topNum);
        }
        System.exit(0);
    }

    private void keyMove(final int dir) {
        figure.movingTimes++;
        if (figure.isFalls) {
            figure.movingTimes = 0;
            figure.move(dir);
        } else if (figure.movingTimes < 20) {
            delayTimer.stop();
            figure.move(dir);
            delayTimer.start();
        }
        figure.mirror();
    }

    private void keyUp() {
        figure.movingTimes++;
        if (figure.isFalls) {
            figure.movingTimes = 0;
            figure.rotate();
        } else if (figure.movingTimes < 20) {
            delayTimer.stop();
            figure.rotate();
            delayTimer.start();
        }
        figure.isFalls = true;
        figure.mirror();
    }

    void newGame() {
        gameOver = true;
        delayTimer.stop();
        remove(mainPanel);
        mainPanel = new Panel();
        add(mainPanel);
        lvl = 1;
        score = 0;
        allTime = 0;
        figure = new Figure(0);
        nextFigure = new Figure(340);
        delay = 400;
        delayTimer.setDelay(delay);
        cells = new int
                [GAME_FIELD_HEIGHT / BLOCK_SIZE + 3]
                [GAME_FIELD_WIDTH / BLOCK_SIZE];
        Arrays.fill(cells[GAME_FIELD_HEIGHT / BLOCK_SIZE + 2], 1);
        scoreARR = BitNumber.convert(score);
        lvlARR = BitNumber.convert(lvl);
        revalidate();
        repaint();
        gameOver = false;
        delayTimer.start();
    }

    void checkWall() {
        int k = 0;
        int lines = 0;
        for (int y = 0; y < 22; y++) {
            for (int x = 0; x < 10; x++) {
                if (cells[y][x] > 0) {
                    k++;
                }
            }
            if (k == 10) {
                lines++;
                for (int x = 0; x < 10; x++) {
                    cells[y][x] = 0;
                }
                for (int i = y; i > -1; i--) {
                    for (int x = 0; x < 10; x++) {
                        if (cells[i][x] > 0) {
                            int tmp = cells[i][x];
                            cells[i][x] = cells[i - 1][x];
                            cells[i + 1][x] = tmp;
                        }
                    }
                }
            }
            k = 0;
        }
        if (lines > 0) {
            score += lines * 100 * lines * lvl;
            scoreARR = BitNumber.convert(score);
            repaint();
        }
    }


    class Panel extends JPanel {
        @Override
        public void paint(final Graphics g) {
            super.paint(g);
            if (!pause) {
                g.drawRect(FIELD_INDENT, FIELD_INDENT,
                        GAME_FIELD_WIDTH, GAME_FIELD_HEIGHT);

                drawFigures(g);
                figure.paint(g);
                nextFigure.paint(g);
                drawScore(g);
                drawLvlTitle(g);
                drawScoreTitle(g);
                drawNextFigureTitle(g);
                drawLvl(g);

            } else {
                drawTopTitle(g);
                drawTop10(g);
                drawTop10Num(g);
            }
            if (gameOver && !pause) {
                drawGameOver(g);
            }
        }

        private void drawGameOver(final Graphics g) {
            for (int y = 0; y < GAME_OVER_MSG.length; y++) {
                for (int x = 0; x < GAME_OVER_MSG[y].length; x++) {
                    if (GAME_OVER_MSG[y][x] == 1) {
                        g.setColor(Color.BLACK);
                        g.drawRect(x * 11 + 70, y * 11 + 160, 10, 10);
                        g.setColor(new Color(11, 152, 222));
                        g.fill3DRect(x * 11 + 70, y * 11 + 160, 10, 10, true);
                    }
                }
            }
        }

        private void drawTop10Num(final Graphics g) {
            for (int x = 0; x < topNumARR.size(); x++) {
                for (int c = 0; c < topNumARR.get(x).size(); c++) {
                    for (int y = 0; y < topNumARR.get(x).get(c).length; y++) {
                        for (int z = 0; z < topNumARR.get(x).get(c)[y].length; z++) {
                            if (topNumARR.get(x).get(c)[y][z].equals(1)) {
                                g.setColor(Color.BLACK);
                                g.drawRect(z * 8 + 90 + c * 50, y * 8 + 50 * x + 100, 7, 7);
                                g.setColor(new Color(245, 132, 132));
                                g.fill3DRect(z * 8 + 90 + c * 50, y * 8 + 50 * x + 100, 7, 7, true);
                            }
                        }
                    }
                }
            }
        }

        private void drawTop10(final Graphics g) {
            for (int x = 0; x < top10.size(); x++) {
                for (int y = 0; y < top10.get(x).length; y++) {
                    for (int z = 0; z < top10.get(x)[y].length; z++) {
                        if (top10.get(x)[y][z].equals(1)) {
                            g.setColor(Color.BLACK);
                            if (x != 9) {
                                g.drawRect(z * 8 + 40, y * 8 + 50 * x + 100, 7, 7);
                                g.setColor(new Color(11, 152, 222));
                                g.fill3DRect(z * 8 + 40, y * 8 + 50 * x + 100, 7, 7, true);
                            } else {
                                g.drawRect(z * 8 + 8, y * 8 + 50 * x + 100, 7, 7);
                                g.setColor(new Color(11, 152, 222));
                                g.fill3DRect(z * 8 + 8, y * 8 + 50 * x + 100, 7, 7, true);
                            }
                        }
                    }
                }
            }
        }

        private void drawTopTitle(final Graphics g) {
            for (int y = 0; y < TOP.length; y++) {
                for (int x = 0; x < TOP[y].length; x++) {
                    if (TOP[y][x] == 1) {
                        g.setColor(Color.BLACK);
                        g.drawRect(x * 11 + 50, y * 11 + 30, 10, 10);
                        g.setColor(new Color(11, 152, 222));
                        g.fill3DRect(x * 11 + 50, y * 11 + 30, 10, 10, true);
                    }
                }
            }
        }

        private void drawLvl(final Graphics g) {
            for (int y = 0; y < LVL.length; y++) {
                for (int x = 0; x < LVL[y].length; x++) {
                    if (LVL[y][x] == 1) {
                        g.setColor(Color.BLACK);
                        g.drawRect(x * 6 + 320, y * 6 + 100, 5, 5);
                        g.setColor(new Color(11, 152, 222));
                        g.fill3DRect(x * 6 + 320, y * 6 + 100, 5, 5, true);
                    }
                }
            }
        }

        private void drawNextFigureTitle(final Graphics g) {
            for (int y = 0; y < NEXT_FIGURE.length; y++) {
                for (int x = 0; x < NEXT_FIGURE[y].length; x++) {
                    if (NEXT_FIGURE[y][x] == 1) {
                        g.setColor(Color.BLACK);
                        g.drawRect(x * 6 + 320, y * 6 + 25, 5, 5);
                        g.setColor(new Color(11, 152, 222));
                        g.fill3DRect(x * 6 + 320, y * 6 + 25, 5, 5, true);
                    }
                }
            }
        }

        private void drawScoreTitle(final Graphics g) {
            for (int y = 0; y < SCORE_MSG.length; y++) {
                for (int x = 0; x < SCORE_MSG[y].length; x++) {
                    if (SCORE_MSG[y][x] == 1) {
                        g.setColor(Color.BLACK);
                        g.drawRect(x * 11 + 320, y * 11 + 170, 10, 10);
                        g.setColor(new Color(11, 152, 222));
                        g.fill3DRect(x * 11 + 320, y * 11 + 170, 10, 10, true);
                    }
                }
            }
        }

        private void drawLvlTitle(final Graphics g) {
            for (int x = 0; x < lvlARR.size(); x++) {
                for (int y = 0; y < lvlARR.get(x).length; y++) {
                    for (int z = 0; z < lvlARR.get(x)[y].length; z++) {
                        if (lvlARR.get(x)[y][z].equals(1)) {
                            g.setColor(Color.BLACK);
                            g.drawRect(z * 6 + x * 50 + 420, y * 6 + 100, 5, 5);
                            g.setColor(new Color(11, 152, 222));
                            g.fill3DRect(z * 6 + x * 50 + 420, y * 6 + 100, 5, 5, true);
                        }
                    }
                }
            }
        }

        private void drawScore(final Graphics g) {
            for (int x = 0; x < scoreARR.size(); x++) {
                for (int y = 0; y < scoreARR.get(x).length; y++) {
                    for (int z = 0; z < scoreARR.get(x)[y].length; z++) {
                        if (scoreARR.get(x)[y][z].equals(1)) {
                            g.setColor(Color.BLACK);
                            g.drawRect(z * 11 + x * 50 + 320, y * 11 + 260, 10, 10);
                            g.setColor(new Color(11, 152, 222));
                            g.fill3DRect(z * 11 + x * 50 + 320, y * 11 + 260, 10, 10, true);
                        }
                    }
                }
            }
        }

        private void drawFigures(final Graphics g) {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 22; y++) {
                    if (cells[y][x] > 0) {
                        g.setColor(chooseColor(cells[y][x]));
                        g.fill3DRect(x * BLOCK_SIZE + 1 + FIELD_INDENT,
                                y * BLOCK_SIZE + 1,
                                BLOCK_SIZE - 1,
                                BLOCK_SIZE - 1, true);
                    }
                    if (cells[y][x] == -1) {
                        g.setColor(Color.BLACK);
                        g.drawRect(x * BLOCK_SIZE + 1 + FIELD_INDENT,
                                y * BLOCK_SIZE + 1,
                                BLOCK_SIZE,
                                BLOCK_SIZE);
                    }
                }
            }
        }

        private Color chooseColor(final int colorNum) {
            switch (colorNum) {
                case 1:
                    return COLORS[0];
                case 2:
                    return COLORS[1];
                case 3:
                    return COLORS[2];
                case 4:
                    return COLORS[3];
                case 5:
                    return COLORS[4];
                case 6:
                    return COLORS[5];
                case 7:
                    return COLORS[6];
                default:
                    throw new RuntimeException();
            }
        }
    }

    class Figure {
        ArrayList<Block> blocksFigure;
        Color color;
        int type;
        int[][] localCoord;
        int rotateType = 0;
        int x;
        int y;
        int movingTimes = 0;
        int paintShift;
        boolean isFalls = true;
        boolean isMoving = true;
        boolean thatsAll = false;

        Figure(final int paintShift) {
            this.paintShift = paintShift;
            createFigure();
        }

        void createFigure() {
            type = (int) (Math.random() * FIGURES.length);
            if (type == 0) {
                y = 1;
            } else {
                y = 0;
            }
            x = 3;
            localCoord = FIGURES[type].clone();
            color = COLORS[(int) (Math.random() * COLORS.length)];
            blocksFigure = new ArrayList<>(new Block(localCoord)
                    .createBlocks(color, x, y));
        }

        void stepDown() {
            for (Block block : blocksFigure) {
                if (block.getY() + 1 == 22
                        || cells[block.getY() + 1][block.getX()] > 0) {
                    repaint(0, 0,
                            GAME_FIELD_WIDTH + 60, GAME_FIELD_HEIGHT + 90);
                    isFalls = false;
                    if (!isMoving) {
                        for (Block block2 : blocksFigure) {
                            cells[block2.getY()][block2.getX()]
                                    = block2.colorNum;
                        }
                        checkGameOver();
                        thatsAll = true;
                        return;
                    }
                    return;
                }
            }
            for (Block block : blocksFigure) {
                block.setY(block.getY() + 1);
            }
            y++;
            repaint(0, 0,
                    GAME_FIELD_WIDTH + 60, GAME_FIELD_HEIGHT + 90);
        }

        void checkGameOver() {
            for (Block block : blocksFigure) {
                if (block.getY() < 2) {
                    gameOver = true;
                    break;
                }
            }
        }

        void mirror() {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 22; y++) {
                    if (cells[y][x] == -1) {
                        cells[y][x] = 0;
                    }
                }
            }
            for (Block bl : blocksFigure) {
                mirrorY = 0;
                boolean success = false;
                while (mirrorY < 23 - bl.getY()) {
                    if (cells[mirrorY + bl.getY()][bl.getX()] > 0) {
                        mirrorY--;
                        success = true;
                        for (Block block : blocksFigure) {
                            if (mirrorY + bl.getY() > 0) {
                                if (cells[mirrorY + block.getY()][block.getX()] == 0) {
                                    continue;
                                } else {
                                    success = false;
                                    mirrorY++;
                                    break;
                                }
                            }
                        }
                        if (success) {
                            for (Block block2 : blocksFigure) {
                                cells[mirrorY + block2.getY()][block2.getX()] = -1;
                            }
                            break;
                        }
                    }
                    mirrorY++;
                }
                if (success) {
                    break;
                }
            }
        }

        void rotate() {
            switch (type) {
                case 0:
                    rotateForLine();
                    break;
                case 1:
                    rotateForG1();
                    break;
                case 2:
                    rotateForCube();
                    break;
                case 3:
                    rotateForZ1();
                    break;
                case 4:
                    rotateForZ2();
                    break;
                case 5:
                    rotateForG2();
                    break;
                case 6:
                    rotateForW();
                    break;
                default:
                    throw new RuntimeException();
            }
        }

        void rotateForLine() {
            int[][] tmp = localCoord.clone();
            if (rotateType == 0) {
                localCoord = new int[][]{{0, 0, 0, 0}, {1, 1, 1, 1},
                        {0, 0, 0, 0}, {0, 0, 0, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType++;
            } else {
                localCoord = new int[][]{{0, 0, 1, 0}, {0, 0, 1, 0},
                        {0, 0, 1, 0}, {0, 0, 1, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType--;
            }
            int k = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (localCoord[i][j] != 0) {
                        blocksFigure.get(k).setX(x + j);
                        blocksFigure.get(k).setY(y + i);
                        k++;
                    }
                }
            }
            repaint(0, 0,
                    GAME_FIELD_WIDTH + 60, GAME_FIELD_HEIGHT + 90);
        }

        void rotateForCube() {
            //Nothing to do!
        }

        void rotateForZ1() {
            int[][] tmp = localCoord.clone();
            if (rotateType == 0) {
                localCoord = new int[][]{{0, 1, 0, 0}, {0, 1, 1, 0},
                        {0, 0, 1, 0}, {0, 0, 0, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType++;
            } else {
                localCoord = new int[][]{{0, 0, 0, 0}, {0, 1, 1, 0},
                        {1, 1, 0, 0}, {0, 0, 0, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType--;
            }
            int k = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (localCoord[i][j] != 0) {
                        blocksFigure.get(k).setX(x + j);
                        blocksFigure.get(k).setY(y + i);
                        k++;
                    }
                }
            }
            repaint(0, 0,
                    GAME_FIELD_WIDTH + 60, GAME_FIELD_HEIGHT + 90);
        }

        void rotateForZ2() {
            int[][] tmp = localCoord.clone();
            if (rotateType == 0) {
                localCoord = new int[][]{{0, 0, 0, 0}, {0, 0, 1, 0},
                        {0, 1, 1, 0}, {0, 1, 0, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType++;
            } else {
                localCoord = new int[][]{{0, 0, 0, 0}, {0, 1, 1, 0},
                        {0, 0, 1, 1}, {0, 0, 0, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType--;
            }
            int k = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (localCoord[i][j] != 0) {
                        blocksFigure.get(k).setX(x + j);
                        blocksFigure.get(k).setY(y + i);
                        k++;
                    }
                }
            }
            repaint(0, 0,
                    GAME_FIELD_WIDTH + 60, GAME_FIELD_HEIGHT + 90);
        }

        void rotateForG1() {
            int[][] tmp = localCoord.clone();
            if (rotateType == 0) {
                localCoord = new int[][]{{0, 0, 0, 0}, {1, 1, 1, 0},
                        {1, 0, 0, 0}, {0, 0, 0, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType++;
            } else if (rotateType == 1) {
                localCoord = new int[][]{{0, 0, 0, 0}, {0, 1, 1, 0},
                        {0, 0, 1, 0}, {0, 0, 1, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType++;
            } else if (rotateType == 2) {
                localCoord = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 1},
                        {0, 1, 1, 1}, {0, 0, 0, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType++;
            } else if (rotateType == 3) {
                localCoord = new int[][]{{0, 1, 0, 0}, {0, 1, 0, 0},
                        {0, 1, 1, 0}, {0, 0, 0, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType = 0;
            }
            int k = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (localCoord[i][j] != 0) {
                        blocksFigure.get(k).setX(x + j);
                        blocksFigure.get(k).setY(y + i);
                        k++;
                    }
                }
            }
            repaint(0, 0,
                    GAME_FIELD_WIDTH + 60, GAME_FIELD_HEIGHT + 90);

        }

        void rotateForG2() {
            int[][] tmp = localCoord.clone();
            if (rotateType == 0) {
                localCoord = new int[][]{{0, 0, 0, 0}, {0, 1, 0, 0},
                        {0, 1, 1, 1}, {0, 0, 0, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType++;
            } else if (rotateType == 1) {
                localCoord = new int[][]{{0, 0, 0, 0}, {0, 1, 1, 0},
                        {0, 1, 0, 0}, {0, 1, 0, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType++;
            } else if (rotateType == 2) {
                localCoord = new int[][]{{0, 0, 0, 0}, {1, 1, 1, 0},
                        {0, 0, 1, 0}, {0, 0, 0, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType++;
            } else if (rotateType == 3) {
                localCoord = new int[][]{{0, 0, 1, 0}, {0, 0, 1, 0},
                        {0, 1, 1, 0}, {0, 0, 0, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType = 0;
            }
            int k = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (localCoord[i][j] != 0) {
                        blocksFigure.get(k).setX(x + j);
                        blocksFigure.get(k).setY(y + i);
                        k++;
                    }
                }
            }
            repaint(0, 0,
                    GAME_FIELD_WIDTH + 60, GAME_FIELD_HEIGHT + 90);
        }

        void rotateForW() {
            int[][] tmp = localCoord.clone();
            if (rotateType == 0) {
                localCoord = new int[][]{{0, 0, 0, 0}, {0, 0, 1, 0},
                        {0, 0, 1, 1}, {0, 0, 1, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType++;
            } else if (rotateType == 1) {
                localCoord = new int[][]{{0, 0, 0, 0}, {0, 1, 1, 1},
                        {0, 0, 1, 0}, {0, 0, 0, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType++;
            } else if (rotateType == 2) {
                localCoord = new int[][]{{0, 0, 0, 0}, {0, 0, 1, 0},
                        {0, 1, 1, 0}, {0, 0, 1, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType++;
            } else if (rotateType == 3) {
                localCoord = new int[][]{{0, 0, 0, 0}, {0, 0, 1, 0},
                        {0, 1, 1, 1}, {0, 0, 0, 0}};
                if (tryRotateLeft() && tryRotateRight()) {
                    if (!isFalls) {
                        if (tryRotateUp()) {
                            localCoord = tmp.clone();
                            return;
                        }
                        isFalls = true;
                    } else {
                        localCoord = tmp.clone();
                        return;
                    }
                }
                rotateType = 0;
            }

            int k = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (localCoord[i][j] != 0) {
                        blocksFigure.get(k).setX(x + j);
                        blocksFigure.get(k).setY(y + i);
                        k++;
                    }
                }
            }
            repaint(0, 0,
                    GAME_FIELD_WIDTH + 60, GAME_FIELD_HEIGHT + 90);
        }

        boolean tryRotateUp() {
            if (!checkNeighbors()) {
                for (Block block : blocksFigure) {
                    if (cells[block.getY() - 1][block.getX()] > 0) {
                        return true;
                    }
                }
                for (Block block : blocksFigure) {
                    block.setY(block.getY() - 1);
                }
                y--;
                if (!checkNeighbors()) {
                    for (Block block : blocksFigure) {
                        block.setY(block.getY() + 1);
                    }
                    y++;
                    return true;
                }
                return false;
            }
            return false;
        }

        boolean tryRotateLeft() {
            if (!checkNeighbors()) {
                int tmp = x;
                move(LEFT);
                if (tmp == x) {
                    return true;
                } else if (!checkNeighbors()) {
                    if (this.type == 0) {
                        move(LEFT);
                        if (checkNeighbors()) {
                            return false;
                        }
                        move(RIGHT);
                    }
                    move(RIGHT);
                    return true;
                }
            }
            return false;
        }

        boolean tryRotateRight() {
            if (!checkNeighbors()) {
                int tmp = x;
                move(RIGHT);
                if (tmp == x) {
                    return true;
                } else if (!checkNeighbors()) {
                    if (this.type == 0) {
                        move(RIGHT);
                        if (checkNeighbors()) {
                            return false;
                        }
                        move(LEFT);
                    }
                    move(LEFT);
                    return true;
                }
            }
            return false;
        }

        boolean checkNeighbors() {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (localCoord[i][j] != 0) {
                        if ((x + j < 0 || x + j > 9)
                                || cells[y + i][x + j] > 0) {
                            return false;
                        }

                    }
                }
            }
            return true;
        }

        void paint(final Graphics g) {
            for (Block block : blocksFigure) {
                block.paint(g, paintShift);
            }
        }

        boolean isTouchWall(final int direction) {
            for (Block block : blocksFigure) {
                if (direction == LEFT && ((block.getX() <= 0)
                        || (block.getX() - 1 > 0
                        && cells[block.getY()][block.getX() - 1] > 0))) {
                    return false;
                }
                if (direction == RIGHT
                        && ((block.getX() >= 9)
                        || (block.getX() + 1 < 10
                        && cells[block.getY()][block.getX() + 1] > 0))) {
                    return false;
                }
            }
            return true;
        }

        void move(final int keyCode) {
            if (isTouchWall(keyCode)) {
                int dx = keyCode - UP; // UP = 38 => for LEFT = -1 for RIGHT = 1
                for (Block block : blocksFigure) {
                    block.setX(block.getX() + dx);
                }
                x += dx;
            }
            repaint(0, 0,
                    GAME_FIELD_WIDTH + 60, GAME_FIELD_HEIGHT + 90);

        }
    }

    class Block {
        int[][] figureCoordLocal;
        int x;
        int y;
        Color color;
        int colorNum;

        Block(final int[][] type) {
            figureCoordLocal = type.clone();
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setX(final int x) {
            if (x < 0) {
                figure.move(RIGHT);
                this.x = x + 1;
            } else {
                this.x = x;
            }
        }

        public void setY(final int y) {
            this.y = y;
        }

        Block(final int x, final int y, final Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
            for (int i = 0; i < COLORS.length; i++) {
                if (this.color == COLORS[i]) {
                    colorNum = i + 1;
                    break;
                }
            }
        }

        ArrayList<Block> createBlocks(final Color color,
                                      final int x, final int y) {
            ArrayList<Block> result = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (figureCoordLocal[i][j] != 0) {
                        result.add(new Block(j + x, i + y, color));
                    }
                }
            }
            return result;
        }

        void paint(final Graphics g, final int paintShift) {
            g.setColor(color);
            g.fillRoundRect(x * BLOCK_SIZE + FIELD_INDENT + paintShift,
                    y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE,
                    ARC_RADIUS, ARC_RADIUS);
            g.setColor(Color.BLACK);
            g.drawRoundRect(x * BLOCK_SIZE + FIELD_INDENT + paintShift,
                    y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE,
                    ARC_RADIUS, ARC_RADIUS);
        }
    }
}
