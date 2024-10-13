import javax.swing.*;
import java.io.*;
import java.net.*;

public class Client {
    private Socket s;
    public Socket getSocket() {
        return s;
    }
    private GamePanel gamePanel;

    public void start() {
        try {
            s = new Socket("localhost", 3456);
            System.out.println("Connected to server.");

            ObjectOutputStream outt = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream inn = new ObjectInputStream(s.getInputStream());

            GamePole gamePole1 = (GamePole) inn.readObject();
            GamePole gamePole2 = (GamePole) inn.readObject();
            gamePanel = new GamePanel(gamePole1, gamePole2);
            gamePanel.sset(this);
            gamePanel.whoami = false;
            JFrame player1Frame = new JFrame("Player 2");
            player1Frame.setSize(900, 600);
            player1Frame.add(gamePanel);
            player1Frame.setVisible(true);
            while (true) {
                int x = s.getInputStream().read();
                int y = s.getInputStream().read();
                gamePanel.shootInClient(x, y);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendToServer(int x, int y) {
        System.out.println(x);
        System.out.println(y);
        try {
            s.getOutputStream().write(x);
            s.getOutputStream().write(y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Client c = new Client();
        c.start();
    }

}