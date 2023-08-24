import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;

public class SnakeGame extends JFrame {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 100;

    private Snake snake;
    private Apple apple;

    private int applesEaten;
    private String highScore = "0";

    private boolean running = false;
    private boolean notStarted = true;

    private JLabel scoreLabel;
    private JLabel highScoreLabel;
    private JButton startButton;
    private Timer timer;

    public SnakeGame () {
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH + 16, HEIGHT + 68);
        setResizable(false);
        setLocationRelativeTo(null);
        addKeyListener(new KeyAdapter() {
            public void keyPressed (KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (snake.direction != 'R') snake.setDirection('L');
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (snake.direction != 'L') snake.setDirection('R');
                        break;   
                    case KeyEvent.VK_UP:
                        if (snake.direction != 'D') snake.setDirection('U');
                        break;  
                    case KeyEvent.VK_DOWN:
                        if (snake.direction != 'U') snake.setDirection('D');
                        break;                   
                }
            }
        });

        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                draw(g);
            }
        };
        gamePanel.setBackground(Color.BLACK);
        gamePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        add(gamePanel, BorderLayout.CENTER);

        startButton = new JButton("Start Game");
        startButton.setFocusable(false);
        startButton.addActionListener(e -> {
            startGame();
            startButton.setEnabled(false);
        });

        scoreLabel = new JLabel("Score: " + applesEaten);
        scoreLabel.setForeground(Color.WHITE);

        highScore = getHighScore();
        System.out.println(highScore);
        highScoreLabel = new JLabel("High Score: " + highScore);
        highScoreLabel.setForeground(Color.WHITE);

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER, WIDTH / 7, 0));
        scorePanel.setBackground(Color.BLACK);
        scorePanel.add(startButton);
        scorePanel.add(scoreLabel);
        scorePanel.add(highScoreLabel);
        add(scorePanel, BorderLayout.SOUTH);

        snake = new Snake(GAME_UNITS);
        apple = new Apple(snake.x, snake.y, snake.bodyParts);

        gamePanel.requestFocus();
        setVisible(true);

    }

    private void startGame () {
        snake = new Snake(GAME_UNITS);
        apple = new Apple(snake.x, snake.y, snake.bodyParts);
        applesEaten = 0;
        running = true;
        notStarted = false;
        timer = new Timer(DELAY, new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                if (running) {
                    snake.move();
                    checkApple();
                    checkCollisions();
                }
                repaint();
            }
        });

        timer.start();
    }

    private void checkApple() {
        if ((snake.x[0] == apple.x) && (snake.y[0] == apple.y)) {
            snake.setBodyParts(snake.bodyParts + 1);
            applesEaten++;
            scoreLabel.setText("Score: " + applesEaten);
            apple = new Apple(snake.x, snake.y, snake.bodyParts);
        }
    }

    private void checkCollisions() {
        if (!snake.checkCollisions()) {
            running = false;
            startButton.setEnabled(true);
        }

        if (!running) {
            if (applesEaten > Integer.parseInt(highScore)) {
                highScore = String.valueOf(applesEaten);
                highScoreLabel.setText("High Score: " + highScore);

                File scoreFile = new File("highscore.dat");
                if (!scoreFile.exists()) {
                    try {
                        scoreFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                }

                FileWriter writeFile = null;
                BufferedWriter writer = null;
                try {
                    writeFile = new FileWriter(scoreFile);
                    writer = new BufferedWriter(writeFile);
                    writer.write(highScore);
                } catch (Exception e) {
                    //errors
                } finally {
                    try {
                        if (writer != null) {
                            writer.close();
                        }
                    } catch (Exception e) {}

                }
            }
            timer.stop();
        }
    }

    public void draw (Graphics g) {

        //Draw Background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //Draw Grid Lines
        /* g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < WIDTH / UNIT_SIZE + 1; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, HEIGHT);
        }
        for (int i = 0; i < HEIGHT / UNIT_SIZE + 1; i++) {
            g.drawLine(0, i * UNIT_SIZE, WIDTH, i * UNIT_SIZE);
        } */

        //Draw Apple
        g.setColor(Color.RED);
        g.fillOval(apple.x, apple.y, UNIT_SIZE, UNIT_SIZE);

        //Draw Snake
        g.setColor(Color.GREEN);
        g.fillRect(snake.x[0], snake.y[0], UNIT_SIZE, UNIT_SIZE);
        for (int i = 1; i < snake.bodyParts; i++) {
            g.fillOval(snake.x[i], snake.y[i], UNIT_SIZE, UNIT_SIZE);
        }

        //Draw Score and High Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 3 - 60, g.getFont().getSize() + HEIGHT);
        g.drawString("High Score: " + highScore, (WIDTH - metrics.stringWidth("High Score: " + highScore)) / 3 + WIDTH / 3, g.getFont().getSize() + HEIGHT);

        //Draw Game Over
        if (!running && !notStarted) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            metrics = getFontMetrics(g.getFont());
            g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);
        }
    }

    public String getHighScore () {
        FileReader readFile = null;
        BufferedReader reader = null;
        try {
            readFile = new FileReader("highscore.dat");
            reader = new BufferedReader(readFile);
            //System.out.println("high score is " + reader.readLine());
            return reader.readLine();
        } catch (Exception e) {
            return "0";
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }   
        }
    }

    public static void main (String[] args) {
        new SnakeGame();
    }
}