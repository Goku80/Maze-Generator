package src;

import javax.swing.JPanel;
import javax.swing.Timer; // Swing-Timer für animierte Abläufe
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Stack;

/**
 * Das MazePanel ist die zentrale Komponente, die das Labyrinth zeichnet,
 * die Maze-Generierung durchführt und den animierten Lösungsprozess mittels DFS (Depth-First Search)
 * visualisiert.
 */
public class MazePanel extends JPanel {
    // Dimensionen des Labyrinths: Anzahl Zeilen, Spalten und die Größe einer Zelle in Pixeln.
    private final int rows, cols, cellSize;
    // 2D-Array, das alle Zellen (MazeCell) enthält.
    private MazeCell[][] grid;
    // Referenzen auf Start- und Endzelle im Labyrinth.
    private MazeCell startCell, endCell;

    // Zustände für den Lösungsalgorithmus:
    // Flag, ob der Lösungsprozess bereits läuft.
    private boolean solving = false;
    // List, in der der aktuelle Pfad der Lösung (in der Reihenfolge der Zellen) gespeichert wird.
    // Wir verwenden den vollqualifizierten Namen, um Konflikte mit java.awt.List zu vermeiden.
    private java.util.List<MazeCell> solutionPath = new ArrayList<>();
    // Set, das alle bereits besuchten Zellen während der Lösung speichert.
    private java.util.Set<MazeCell> visitedCells = new HashSet<>();
    // Swing-Timer, der periodisch einen Lösungs-Schritt ausführt (Animation).
    private javax.swing.Timer solverTimer;
    // Stack, der als DFS-Datenstruktur dient: Speichert Zellen, die noch weiter untersucht werden sollen.
    private Deque<MazeCell> stack = new ArrayDeque<>();

    /**
     * Konstruktor des MazePanel.
     * Hier werden Parameter übernommen, der Zeichenbereich festgelegt,
     * das interne Zellen-Gitter initialisiert und das Labyrinth generiert.
     *
     * @param cols Anzahl der Spalten
     * @param rows Anzahl der Zeilen
     * @param cellSize Größe einer Zelle in Pixeln
     */
    public MazePanel(int cols, int rows, int cellSize) {
        this.cols = cols;
        this.rows = rows;
        this.cellSize = cellSize;
        // Festlegung der bevorzugten Panel-Größe basierend auf Spalten, Zeilen und Zellgröße.
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        // Hintergrundfarbe auf Weiß setzen.
        setBackground(Color.WHITE);

        // Initialisiere das Gitter der Zellen und generiere das Labyrinth.
        initGrid();
        generateMaze();

        // Definiere Start- und Endzelle: Hier wird die linke obere Zelle als Start
        // und die rechte untere Zelle als Ziel gewählt.
        startCell = grid[0][0];
        endCell = grid[rows - 1][cols - 1];
    }

    /**
     * Initialisiert das 2D-Array 'grid' mit MazeCell-Objekten,
     * sodass jede Zelle an ihrer Position (Zeile, Spalte) erstellt wird.
     */
    private void initGrid() {
        grid = new MazeCell[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new MazeCell(r, c);
            }
        }
    }

    /**
     * Generiert das Labyrinth mithilfe eines rekursiven Backtracking-Algorithmus.
     * Es wird ein Stack verwendet, um den aktuellen Pfad zu speichern, und durch
     * das zufällige Auswählen unbesuchter Nachbarn werden die "Wände" zwischen den Zellen
     * entfernt, um einen zusammenhängenden Pfad zu erzeugen.
     */
    private void generateMaze() {
        Stack<MazeCell> cellStack = new Stack<>();
        MazeCell current = grid[0][0];
        current.visited = true;
        int totalCells = rows * cols;
        int visitedCount = 1;

        // Solange nicht alle Zellen besucht wurden:
        while (visitedCount < totalCells) {
            MazeCell next = getUnvisitedNeighbor(current);
            if (next != null) {
                // Markiere den nächsten unbesuchten Nachbarn als besucht.
                next.visited = true;
                visitedCount++;
                // Entferne die Wand zwischen der aktuellen Zelle und dem Nachbarn.
                removeWalls(current, next);
                // Speichere die aktuelle Zelle im Stack und setze den Nachbarn als aktuelle Zelle.
                cellStack.push(current);
                current = next;
            } else if (!cellStack.isEmpty()) {
                // Falls es keine unbesuchten Nachbarn mehr gibt, gehe im Stack zurück (Backtracking).
                current = cellStack.pop();
            }
        }
        // Nachdem das Labyrinth generiert wurde, werden die visited-Flags zurückgesetzt,
        // um den Lösungsalgorithmus nicht zu beeinträchtigen.
        for (MazeCell[] row : grid) {
            for (MazeCell cell : row) {
                cell.visited = false;
            }
        }
    }

    /**
     * Sucht einen zufälligen unbesuchten Nachbarn für eine gegebene Zelle.
     * Dabei werden die vier möglichen Richtungen (oben, rechts, unten, links) geprüft.
     *
     * @param cell Aktuelle Zelle
     * @return Ein zufällig ausgewählter unbesuchter Nachbar oder null, falls keiner vorhanden ist.
     */
    private MazeCell getUnvisitedNeighbor(MazeCell cell) {
        java.util.List<MazeCell> neighbors = new ArrayList<>();
        int r = cell.row;
        int c = cell.col;
        // Prüfe jede Richtung und füge den Nachbarn hinzu, falls er unbesucht ist.
        if (r > 0 && !grid[r - 1][c].visited)
            neighbors.add(grid[r - 1][c]);
        if (c < cols - 1 && !grid[r][c + 1].visited)
            neighbors.add(grid[r][c + 1]);
        if (r < rows - 1 && !grid[r + 1][c].visited)
            neighbors.add(grid[r + 1][c]);
        if (c > 0 && !grid[r][c - 1].visited)
            neighbors.add(grid[r][c - 1]);
        if (neighbors.isEmpty())
            return null;
        // Mische die Liste, um zufällige Auswahl zu ermöglichen, und gib den ersten zurück.
        Collections.shuffle(neighbors);
        return neighbors.get(0);
    }

    /**
     * Entfernt die Wand zwischen zwei benachbarten Zellen.
     * Die Logik basiert darauf, die relative Position (dx, dy) der Zellen zu bestimmen.
     *
     * @param current Die aktuelle Zelle
     * @param next Der Nachbar, zu dem die Wand entfernt wird
     */
    private void removeWalls(MazeCell current, MazeCell next) {
        int dx = current.col - next.col;
        int dy = current.row - next.row;

        // Je nach relativer Position werden die entsprechenden Wand-Flags in den Zellen auf false gesetzt.
        if (dx == 1) { // Der Nachbar liegt links von current
            current.walls[3] = false;
            next.walls[1] = false;
        } else if (dx == -1) { // Der Nachbar liegt rechts von current
            current.walls[1] = false;
            next.walls[3] = false;
        }
        if (dy == 1) { // Der Nachbar liegt über current
            current.walls[0] = false;
            next.walls[2] = false;
        } else if (dy == -1) { // Der Nachbar liegt unter current
            current.walls[2] = false;
            next.walls[0] = false;
        }
    }

    /**
     * Startet den animierten DFS-Lösungsalgorithmus, der den Pfad vom Start zum Ziel sucht.
     * Dabei wird ein Timer verwendet, der in regelmäßigen Abständen jeweils einen Schritt des Algorithmus ausführt.
     */
    public void startSolving() {
        if (solving)
            return; // Verhindere mehrfaches Starten des Lösungsprozesses

        // Setze alle Zustände zurück: Lösche den bisherigen Lösungsweg, die Besuchs-Informationen und den Stack.
        solutionPath.clear();
        visitedCells.clear();
        stack.clear();
        // Setze für jede Zelle das visited-Flag auf false.
        for (MazeCell[] row : grid) {
            for (MazeCell cell : row) {
                cell.visited = false;
            }
        }
        // Beginne den DFS von der Startzelle.
        stack.push(startCell);
        startCell.visited = true;
        visitedCells.add(startCell);
        solving = true;

        // Initialisiere einen Swing-Timer, der alle 50ms einen Schritt (solveStep) ausführt.
        solverTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveStep();
            }
        });
        solverTimer.start();
    }

    /**
     * Führt einen einzelnen Schritt des DFS-Lösungsalgorithmus aus.
     * Dabei wird versucht, einen unbesuchten, erreichbaren Nachbarn zu finden.
     * Falls keiner vorhanden ist, wird zurückgegangen (Backtracking).
     */
    private void solveStep() {
        // Falls der Stack leer ist, gibt es keinen Weg zum Ziel → Stoppe die Animation.
        if (stack.isEmpty()) {
            solverTimer.stop();
            solving = false;
            return;
        }
        // Schaue in den obersten Eintrag des Stacks (aktueller Knoten).
        MazeCell current = stack.peek();
        // Falls die aktuelle Zelle noch nicht im Lösungsweg enthalten ist, füge sie hinzu.
        if (!solutionPath.contains(current)) {
            solutionPath.add(current);
        }
        // Falls das Ziel erreicht wurde, stoppe die Animation.
        if (current.equals(endCell)) {
            solverTimer.stop();
            solving = false;
            repaint();
            return;
        }
        // Suche einen unbesuchten, erreichbaren Nachbarn (d.h. es darf keine Wand im Weg sein).
        MazeCell next = getUnvisitedAccessibleNeighbor(current);
        if (next != null) {
            // Wenn ein Nachbar gefunden wurde, markiere ihn als besucht und füge ihn dem Stack hinzu.
            next.visited = true;
            visitedCells.add(next);
            stack.push(next);
        } else {
            // Kein Nachbar gefunden → Backtracking: Entferne die aktuelle Zelle aus dem Stack
            // und entferne sie auch aus dem Lösungsweg, da es sich um einen toten Pfad handelt.
            stack.pop();
            solutionPath.remove(current);
        }
        // Aktualisiere die Anzeige, damit der Fortschritt sichtbar wird.
        repaint();
    }

    /**
     * Sucht einen unbesuchten Nachbarn, der auch erreichbar ist (keine Wand im Weg).
     * Prüft in allen vier Richtungen.
     *
     * @param cell Die Zelle, deren Nachbarn untersucht werden sollen.
     * @return Ein unbesuchter, erreichbarer Nachbar oder null, wenn keiner gefunden wurde.
     */
    private MazeCell getUnvisitedAccessibleNeighbor(MazeCell cell) {
        java.util.List<MazeCell> neighbors = new ArrayList<>();
        int r = cell.row;
        int c = cell.col;
        if (!cell.walls[0] && r > 0 && !grid[r - 1][c].visited)
            neighbors.add(grid[r - 1][c]);
        if (!cell.walls[1] && c < cols - 1 && !grid[r][c + 1].visited)
            neighbors.add(grid[r][c + 1]);
        if (!cell.walls[2] && r < rows - 1 && !grid[r + 1][c].visited)
            neighbors.add(grid[r + 1][c]);
        if (!cell.walls[3] && c > 0 && !grid[r][c - 1].visited)
            neighbors.add(grid[r][c - 1]);
        if (neighbors.isEmpty())
            return null;
        Collections.shuffle(neighbors);
        return neighbors.get(0);
    }

    /**
     * Überschriebene Methode, die vom JPanel aufgerufen wird, um die Komponente zu zeichnen.
     * Hier werden alle Zellen des Labyrinths, die bereits besuchten Zellen während der Suche,
     * der aktuelle Lösungsweg sowie der Start- und Endpunkt gezeichnet.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Zeichne jede Zelle des Gitters.
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c].draw(g, cellSize);
            }
        }
        // Hebe alle während der Lösungsfindung besuchten Zellen farblich hervor.
        for (MazeCell cell : visitedCells) {
            int x = cell.col * cellSize;
            int y = cell.row * cellSize;
            g.setColor(new Color(200, 200, 255));
            g.fillRect(x + 2, y + 2, cellSize - 4, cellSize - 4);
        }
        // Zeichne den aktuellen Lösungsweg als durchgehende Linie.
        if (!solutionPath.isEmpty()) {
            g.setColor(Color.RED);
            for (int i = 0; i < solutionPath.size() - 1; i++) {
                MazeCell a = solutionPath.get(i);
                MazeCell b = solutionPath.get(i + 1);
                int x1 = a.col * cellSize + cellSize / 2;
                int y1 = a.row * cellSize + cellSize / 2;
                int x2 = b.col * cellSize + cellSize / 2;
                int y2 = b.row * cellSize + cellSize / 2;
                g.drawLine(x1, y1, x2, y2);
            }
        }
        // Markiere den Startpunkt (grün) und den Zielpunkt (rot).
        g.setColor(Color.GREEN);
        g.fillOval(startCell.col * cellSize + cellSize / 4,
                   startCell.row * cellSize + cellSize / 4,
                   cellSize / 2, cellSize / 2);
        g.setColor(Color.RED);
        g.fillOval(endCell.col * cellSize + cellSize / 4,
                   endCell.row * cellSize + cellSize / 4,
                   cellSize / 2, cellSize / 2);
    }
}
