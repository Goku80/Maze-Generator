package src;


import java.awt.*;
import java.util.Objects;

public class MazeCell {
    public int row, col;
    // Wände: Index 0 = oben, 1 = rechts, 2 = unten, 3 = links
    public boolean[] walls = { true, true, true, true };
    public boolean visited = false;

    public MazeCell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // Zeichnet die Zelle inkl. ihrer Wände
    public void draw(Graphics g, int size) {
        int x = col * size;
        int y = row * size;
        g.setColor(Color.BLACK);
        if (walls[0])
            g.drawLine(x, y, x + size, y);
        if (walls[1])
            g.drawLine(x + size, y, x + size, y + size);
        if (walls[2])
            g.drawLine(x, y + size, x + size, y + size);
        if (walls[3])
            g.drawLine(x, y, x, y + size);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof MazeCell))
            return false;
        MazeCell other = (MazeCell) obj;
        return this.row == other.row && this.col == other.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
