package src;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Objects;

/**
 * Die MazeCell repräsentiert eine einzelne Zelle im Labyrinth.
 * Jede Zelle besitzt vier Wände (oben, rechts, unten, links), die
 * durch ein Boolean-Array dargestellt werden. Zusätzlich wird ein
 * Flag verwendet, um anzuzeigen, ob die Zelle bereits besucht wurde.
 */
public class MazeCell {
    public int row, col;
    // Wände: Index 0 = oben, 1 = rechts, 2 = unten, 3 = links.
    public boolean[] walls = { true, true, true, true };
    // Flag, das angibt, ob die Zelle (für die Maze-Generierung bzw. Lösungsfindung) bereits besucht wurde.
    public boolean visited = false;

    /**
     * Konstruktor, der die Zelle an ihrer Position initialisiert.
     *
     * @param row Die Zeile der Zelle
     * @param col Die Spalte der Zelle
     */
    public MazeCell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Zeichnet die Zelle und deren Wände in das Graphics-Objekt.
     * Dabei wird anhand der Zellposition und der vorgegebenen Zellgröße
     * der exakte Zeichenbereich berechnet.
     *
     * @param g Das Graphics-Objekt, in das gezeichnet wird
     * @param size Die Größe der Zelle in Pixeln
     */
    public void draw(Graphics g, int size) {
        int x = col * size;
        int y = row * size;
        g.setColor(Color.BLACK);
        // Zeichne die obere Wand
        if (walls[0])
            g.drawLine(x, y, x + size, y);
        // Zeichne die rechte Wand
        if (walls[1])
            g.drawLine(x + size, y, x + size, y + size);
        // Zeichne die untere Wand
        if (walls[2])
            g.drawLine(x, y + size, x + size, y + size);
        // Zeichne die linke Wand
        if (walls[3])
            g.drawLine(x, y, x, y + size);
    }

    /**
     * Überschreibt equals, um zwei MazeCell-Objekte als gleich zu betrachten,
     * wenn sie in derselben Zeile und Spalte liegen.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof MazeCell))
            return false;
        MazeCell other = (MazeCell) obj;
        return this.row == other.row && this.col == other.col;
    }

    /**
     * Liefert den Hashcode basierend auf Zeile und Spalte.
     * Wichtig für die Verwendung in Hash-basierten Collections.
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
