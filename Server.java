import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Serializable {
    private Socket s;

    public Socket getSocket() {
        return s;
    }
    private GamePanel gamePanel;
    public void start() {
        try {
            GamePole gamePole1 = new GamePole(10);
            GamePole gamePole2 = new GamePole(10);
            gamePole1.init();
            gamePole1.getPole();
            gamePole2.init();
            gamePole2.getPole();
            gamePanel = new GamePanel(gamePole1, gamePole2);
            gamePanel.set(this);
            ServerSocket serverSocket = new ServerSocket(3456);
            System.out.println("Сервер запущен...");
            JFrame player1Frame = new JFrame("Player 1");
            player1Frame.setSize(900, 600);
            player1Frame.add(gamePanel);
            player1Frame.setVisible(true);



            s = serverSocket.accept();
            System.out.println("Игрок 1 присоединился.");
            ObjectOutputStream outt = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream inn = new ObjectInputStream(s.getInputStream());
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            outt.writeObject(gamePole1);
            outt.writeObject(gamePole2);
            while (true) {
                int x = s.getInputStream().read();
                int y = s.getInputStream().read();
                gamePanel.shootInServer(x, y);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToClient(int x, int y) {
        System.out.println(x);
        System.out.println(y);
        try {
            s.getOutputStream().write(x);
            s.getOutputStream().write(y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
