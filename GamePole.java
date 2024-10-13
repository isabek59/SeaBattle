import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class GamePole implements Serializable {
    private int size;
    private int[][] field;
    private ArrayList<Ship> ships;
    private int killed;
    private ArrayList<int[]> visited;
    private ArrayList<int[]> aroundShip;

    public int getKilled() {
        return killed;
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    public GamePole(int size) {
        this.size = size;
        this.ships = new ArrayList<>();
        this.killed = 0;
        this.visited = new ArrayList<>();
        this.aroundShip = new ArrayList<>();
        this.field = new int[size][size];
    }

    public void init() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = 0;
            }
        }
        ships.clear();
        ships.add(new Ship(4, 1, -1, -1));
        ships.add(new Ship(3, 1, -1, -1));
        ships.add(new Ship(3, 1, -1, -1));
        ships.add(new Ship(2, 1, -1, -1));
        ships.add(new Ship(2, 1, -1, -1));
        ships.add(new Ship(2, 1,-1, -1));
        ships.add(new Ship(1, 1,-1, -1));
        ships.add(new Ship(1, 1, -1, -1));
        ships.add(new Ship(1, 1,-1, -1));
        ships.add(new Ship(1, 1,-1, -1));

        Random random = new Random();

        for (Ship ship : ships) {
            boolean fl = false;

            while (!fl) {
                int tp = random.nextInt(2) + 1;
                int x = random.nextInt(size);
                int y = random.nextInt(size);

                if (!ship.setStartCoords(x, y, tp, size)) {
                    continue;
                }

                fl = true;

                for (Ship otherShip : ships) {
                    if (ship.equals(otherShip)) {
                        fl = true;
                        break;
                    }

                    if (ship.isCollide(otherShip)) {
                        fl = false;
                        break;
                    }
                }
            }
        }
    }

    public int[][] getField() {
        return field;
    }

    public int[][] getPole() {
        for (int[] coord : visited) {
            field[coord[0]][coord[1]] = -1;
        }

        for (int[] coord : aroundShip) {
            field[coord[0]][coord[1]] = -1;
        }

        ArrayList<int[]> shipGood = new ArrayList<>();
        ArrayList<int[]> shipBad = new ArrayList<>();

        for (Ship ship : ships) {
            for (int i = 0; i < ship.getLength(); i++) {
                int[] coords = ship.getFig().get(i);
                if (ship.getCells()[i] == 2) {
                    shipBad.add(coords);
                } else {
                    shipGood.add(coords);
                }
            }
        }

        for (int[] i : shipGood) {
            field[i[1]][i[0]] = 1;
        }

        for (int[] i : shipBad) {
            field[i[1]][i[0]] = 2;
        }
        return field;
    }

    public boolean shoot(int x, int y) {
         int shoot = field[y][x];
         if (shoot == 0) {
             field[y][x] = 7;
             return false;
         } else if (shoot == 1) {
             for (Ship ship : ships) {
                 for (int i = 0; i < ship.getLength(); ++i) {
                     if (x == ship.fig.get(i)[0] && y == ship.fig.get(i)[1]) {
                         ship.cells[i] = 2;
                         field[y][x] = 2;
                         if (ship.count(2) == ship.getLength()) {
                                killed++;

                                ArrayList<int[]> around = ship.getShipZone();
                                for (int[] k : around) {
                                    field[k[1]][k[0]] = 7;
                                }
                         }
                     }
                 }
             }
         }
         return true;
    }
}
