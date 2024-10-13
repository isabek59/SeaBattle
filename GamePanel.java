import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.Serializable;


public class GamePanel extends JPanel implements Serializable {
    private Server server;
    private Client client;
    private int xMouse, yMouse;
    private GamePole gamePole1;

    private GamePole gamePole2;
    private boolean player1turn = true;

    public boolean whoami = true;


    public void shootInClient(int x, int y) {
        player1turn = gamePole2.shoot(x, y);
        repaint();
        if (gamePole1.getKilled() == 10 || gamePole2.getKilled() == 10) {
            System.exit(0);
            try {
                client.getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void shootInServer(int x, int y) {
        player1turn = !gamePole1.shoot(x, y);
        repaint();
        if (gamePole1.getKilled() == 10 || gamePole2.getKilled() == 10) {
            System.exit(0);
            try {
                server.getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void setPlayerMode(boolean b) {
        this.player1turn = b;
        repaint();
    }
    public boolean endGame() {
        if (gamePole2.getKilled() == 10 || gamePole1.getKilled() == 10) {
            return true;
        }
        return false;
    }

    public void set(Server sserver) {
        this.server = sserver;
    }

    public boolean getTurn() {
        return player1turn;
    }

    public void sset(Client client) {
        this.client = client;
    }

    public class MouseListenerImpl implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1 && e.getClickCount() == 1) {
                    xMouse = e.getX();
                    yMouse = e.getY();
                    if (xMouse >= 100 && yMouse >= 100 && xMouse < 400 && yMouse < 400 && !player1turn) {
                        int y = (yMouse - 100) / 30;
                        int x = (xMouse - 100) / 30;
                        client.sendToServer(x, y);
                        player1turn = !gamePole1.shoot(x, y);
                        repaint();
                        if (gamePole1.getKilled() == 10 || gamePole2.getKilled() == 10) {
                            System.exit(0);
                            try {
                                server.getSocket().close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    if (xMouse >= 500 && yMouse >= 100 && xMouse < 800 && yMouse < 400 && player1turn) {
                        int y = (yMouse - 100) / 30;
                        int x = (xMouse - 500) / 30;
                        server.sendToClient(x, y);
                        player1turn = gamePole2.shoot(x, y);
                        System.out.println(player1turn);
                        repaint();
                        if (gamePole2.getKilled() == 10 || gamePole1.getKilled() == 10) {
                            System.exit(0);
                            try {
                                client.getSocket().close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public class MouseMotionListenerImpl implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            xMouse = e.getX();
            yMouse = e.getY();

            if (xMouse >= 100 && yMouse >= 100 && xMouse < 400 && yMouse < 400 && !player1turn) {
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            } else {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            if (xMouse >= 500 && xMouse < 800 && yMouse >= 100 && yMouse < 400 && player1turn) {
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            } else {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

        }
    }

    public GamePanel(GamePole gamePole1, GamePole gamePole2) {
        addMouseListener(new MouseListenerImpl());
        addMouseMotionListener(new MouseMotionListenerImpl());

        this.gamePole1 = gamePole1;

        this.gamePole2 = gamePole2;

        JButton newGameBtn = new JButton();
        newGameBtn.setText("New game");
        newGameBtn.setForeground(Color.BLUE);
        newGameBtn.setFont(new Font("serif", 0, 20));
        newGameBtn.setBounds(130, 450, 80, 200);
//        newGameBtn.addActionListener(new ActionListenerImpl());
        add(newGameBtn);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (int i = 0; i <= 10; ++i) {
            g.drawLine(100 + i * 30, 100, 100 + i * 30, 400);
            g.drawLine(100, 100 + i * 30, 400, 100 + i * 30);
        }

        for (int i = 0; i <= 10; ++i) {
            g.drawLine(500 + i * 30, 100, 500 + i * 30, 400);
            g.drawLine(500, 100 + i * 30, 800, 100 + i * 30);
        }

        if (whoami) {
            for (int i = 0; i < 10; ++i) {
                for (int j = 0; j < 10; ++j) {
                    if (gamePole1.getField()[i][j] == 2) {
                        g.setColor(Color.RED);
                        g.fillRect(100 + j * 30, 100 + i * 30, 30, 30);
                    } else if (gamePole1.getField()[i][j] == 1){
                        g.setColor(Color.BLACK);
                        g.fillRect(100 + j * 30, 100 + i * 30, 30, 30);
                    } else if (gamePole1.getField()[i][j] == 7){
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(100 + j * 30, 100 + i * 30, 30, 30);
                    }
                }
            }
            for (int i = 0; i < 10; ++i) {
                for (int j = 0; j < 10; ++j) {
                    if (gamePole2.getField()[i][j] == 2) {
                        g.setColor(Color.RED);
                        g.fillRect(500 + j * 30, 100 + i * 30, 30, 30);
                    } else if (gamePole2.getField()[i][j] == 7){
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(500 + j * 30, 100 + i * 30, 30, 30);
                    }
                }
            }
        }

        if (!whoami) {
             for (int i = 0; i < 10; ++i) {
                for (int j = 0; j < 10; ++j) {
                    if (gamePole2.getField()[i][j] == 2) {
                        g.setColor(Color.RED);
                        g.fillRect(500 + j * 30, 100 + i * 30, 30, 30);
                    } else if (gamePole2.getField()[i][j] == 1){
                        g.setColor(Color.BLACK);
                        g.fillRect(500 + j * 30, 100 + i * 30, 30, 30);
                    } else if (gamePole2.getField()[i][j] == 7){
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(500 + j * 30, 100 + i * 30, 30, 30);
                    }
                }
            }
            for (int i = 0; i < 10; ++i) {
                for (int j = 0; j < 10; ++j) {
                    if (gamePole1.getField()[i][j] == 2) {
                        g.setColor(Color.RED);
                        g.fillRect(100 + j * 30, 100 + i * 30, 30, 30);
                    } else if (gamePole1.getField()[i][j] == 7){
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(100 + j * 30, 100 + i * 30, 30, 30);
                    }
                }
            }
        }

    }
}