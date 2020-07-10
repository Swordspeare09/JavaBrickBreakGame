import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;


public class GameLogic extends JPanel implements KeyListener, ActionListener{

    private boolean isPlaying = false;
    private int score = 0;
    private int totalBricks = 21;

    private Timer time;
    private int delay = 8;

    private int playerX = 310;

    private int ballPositionX = 120;
    private int ballPositionY = 350;
    private int ballXDirection = -1;
    private int ballYDirection = -2;

    private MapGenerator map;

    public GameLogic() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        time = new Timer(delay, this);
        time.start();
    }

    
    public void paint(Graphics g) {

        // *** Background Color ***
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        g.setColor(Color.RED);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString(""+ score, 590, 30);

        g.setColor(Color.GREEN);
        g.fillRect(playerX, 550, 100, 8);

        g.setColor(Color.GRAY);
        g.fillOval(ballPositionX, ballPositionY, 20, 20);

        if(totalBricks <= 0) {
            isPlaying = false;
            ballXDirection = 0;
            ballYDirection = 0;
            g.setColor(Color.BLUE);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Success!!", 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        map.draw((Graphics2D) g);

        if(ballPositionY > 570) {
            isPlaying = false;
            ballXDirection = 0;
            ballYDirection = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over...", 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }
        g.dispose();
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        time.start();

        if(isPlaying) {

            if(new Rectangle(ballPositionX, ballPositionY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYDirection = -ballYDirection;
            }

            A: for(int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++){
                    
                    if(map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHieght = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHieght);
                        Rectangle ballRect = new Rectangle(ballPositionX, ballPositionY, 20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score+=10;

                            if(ballPositionX + 19 <= brickRect.x || ballPositionX + 1 >= brickRect.x + brickRect.width) {
                                ballXDirection = -ballXDirection;
                            } else {
                                ballYDirection = -ballYDirection;
                            }
                            break A;
                        }
                    }
                }
            }

            ballPositionX += ballXDirection;
            ballPositionY += ballYDirection;

            if(ballPositionX < 0) {
                ballXDirection = -ballXDirection;
            }
            if (ballPositionY < 0) {
                ballYDirection = -ballYDirection;
            }
            if (ballPositionX > 670) {
                ballXDirection = -ballXDirection;
            }
        }
        repaint();
    }

    // *** Not used for this project ***
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode()== KeyEvent.VK_RIGHT) {
                if(playerX >= 600)
                {
                    playerX = 600;
                }
                else {
                    moveRight();
                }

        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (playerX < 10) {
                playerX = 10;
                } else {
                    moveLeft();
                }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(!isPlaying) {
                isPlaying = true;
                ballPositionX = 120;
                ballPositionY = 350;
                ballXDirection = -1;
                ballYDirection = -2;

                playerX = 310;
                totalBricks = 21;
                map = new MapGenerator(3 , 7);

                repaint();
            }
        }
    }

    public void moveRight() {

        isPlaying = true;
        playerX+=20;

    }

    public void moveLeft() {

        isPlaying = true;
        playerX-=20;

    }
}