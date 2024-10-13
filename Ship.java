import java.io.Serializable;
import java.util.ArrayList;

public class Ship implements Serializable {
    private int x, y;
    private int length;
    private int tp;
    public ArrayList<int[]> fig;
    public int[] cells;

    public int count(int x) {
        int c = 0;
        for (int i = 0; i < length; ++i) {
            if (cells[i] == x) {
                ++c;
            }
        }
        return c;
    }

    public Ship(int length, int tp, int x, int y) {
        this.length = length;
        this.fig = new ArrayList<>();
        this.cells = new int[length];

        for (int i = 0; i < length; i++) {
            cells[i] = 1;
        }

        setStartCoords(x, y, tp, 10);

    }

    public int getLength() {
        return length;
    }

    public int getTp() {
        return tp;
    }

    public ArrayList<int[]> getFig() {
        return fig;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public int[] getCells() {
        return cells;
    }

    public boolean setStartCoords(int x, int y, int tp, int size) {
        if (x < 0 || x >= size || y < 0 || y >= size || (tp == 1 && x + length > size) || (tp == 2 && y + length > size)) {
            return false;
        }
        this.x = x;
        this.y = y;
        this.tp = tp;
        fig.clear();
        fig.add(new int[]{x, y});

        for (int i = 0; i < length - 1; i++) {
            int j = (tp == 1) ? 0 : 1;
            int k = (tp == 2) ? 0 : 1;
            fig.add(new int[]{fig.get(fig.size() - 1)[0] + k, fig.get(fig.size() - 1)[1] + j});
        }
    
        return true;
    }

    public boolean isCollide(Ship ship) {
        ArrayList<int[]> zonePlusShip = new ArrayList<>(getShipZone());
        zonePlusShip.addAll(fig);

        for (int[] coord : zonePlusShip) {
            for (int[] shipCoord : ship.getFig()) {
                if (coord[0] == shipCoord[0] && coord[1] == shipCoord[1]) {
                    return true;
                }
            }
        }

        return false;
    }

    public ArrayList<int[]> getShipZone() {
        ArrayList<int[]> zone = new ArrayList<>(fig);

        if (tp == 1) {
            zone.add(new int[]{x - 1, y});
            zone.add(new int[]{x + length, y});

            for (int[] coord : new ArrayList<>(zone)) {
                zone.add(new int[]{coord[0], coord[1] - 1});
                zone.add(new int[]{coord[0], coord[1] + 1});
            }

        } else if (tp == 2) {
            zone.add(new int[]{x, y - 1});
            zone.add(new int[]{x, y + length});

            for (int[] coord : new ArrayList<>(zone)) {
                zone.add(new int[]{coord[0] - 1, coord[1]});
                zone.add(new int[]{coord[0] + 1, coord[1]});
            }
        }

        ArrayList<int[]> filteredZone = new ArrayList<>();
        for (int[] coord : zone) {
            if (coord[0] >= 0 && coord[0] < 10 && coord[1] >= 0 && coord[1] < 10 && !fig.contains(coord)) {
                filteredZone.add(coord);
            }
        }

        return filteredZone;
    }
}
