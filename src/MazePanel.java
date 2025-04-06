package src;


import javax.swing.JPanel;
import javax.swing.Timer; // Dies ist optional, wenn du den vollqualifizierten Namen nutzt
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

public class MazePanel extends JPanel {
    private final int rows, cols, cellSize;
    private MazeCell[][] grid;
    private MazeCell startCell, endCell;

    // Zustände für den Lösungsalgorithmus
    private boolean solving = false;
    // Verwende hier den vollqualifizierten Namen, um Konflikte mit java.awt.List zu vermeiden
    private java.util.List<MazeCell> solutionPath = new ArrayList<>();
    private java.util.Set<MazeCell> visitedCells = new HashSet<>();
    // Auch hier: Wir wollen die Swing-Version des Timers
    private javax.swing.Timer solverTimer;
    private Deque<MazeCell> stack = new ArrayDeque<>();

    public MazePanel(int cols, int rows, int cellSize) {
        this.cols = cols;
        this.rows = rows;
        this.cellSize = cellSize;
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        setBackground(Color.WHITE);

        // Gitter initialisieren und Maze generieren
        initGrid();
        generateMaze();

        // Start- und Endpunkt festlegen
        startCell = grid[0][0];
        endCell = grid[rows - 1][cols - 1];
    }

    // Initialisiert das Zellen-Gitter
    private void initGrid() {
        grid = new MazeCell[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new MazeCell(r, c);
            }
        }
    }

    // Generiert das Labyrinth mittels rekursivem Backtracking
    private void generateMaze() {
        Stack<MazeCell> cellStack = new Stack<>();
        MazeCell current = grid[0][0];
        current.visited = true;
        int totalCells = rows * cols;
        int visitedCount = 1;

        while (visitedCount < totalCells) {
            MazeCell next = getUnvisitedNeighbor(current);
            if (next != null) {
                next.visited = true;
                visitedCount++;
                removeWalls(current, next);
                cellStack.push(current);
                current = next;
            } else if (!cellStack.isEmpty()) {
                current = cellStack.pop();
            }
        }
        // Reset der visited-Flags für den Lösungsalgorithmus
        for (MazeCell[] row : grid) {
            for (MazeCell cell : row) {
                cell.visited = false;
            }
        }
    }

    // Gibt einen zufälligen, unbesuchten Nachbarn der aktuellen Zelle zurück
    private MazeCell getUnvisitedNeighbor(MazeCell cell) {
        java.util.List<MazeCell> neighbors = new ArrayList<>();
        int r = cell.row;
        int c = cell.col;
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
        Collections.shuffle(neighbors);
        return neighbors.get(0);
    }

    // Entfernt die Wände zwischen zwei benachbarten Zellen
    private void removeWalls(MazeCell current, MazeCell next) {
        int dx = current.col - next.col;
        int dy = current.row - next.row;

        if (dx == 1) { // next liegt links von current
            current.walls[3] = false;
            next.walls[1] = false;
        } else if (dx == -1) { // next liegt rechts von current
            current.walls[1] = false;
            next.walls[3] = false;
        }
        if (dy == 1) { // next liegt über current
            current.walls[0] = false;
            next.walls[2] = false;
        } else if (dy == -1) { // next liegt unter current
            current.walls[2] = false;
            next.walls[0] = false;
        }
    }

    // Startet den animierten DFS-Lösungsalgorithmus
    public void startSolving() {
        if (solving) return; // Mehrfachstart verhindern

        // Reset der Zustände
        solutionPath.clear();
        visitedCells.clear();
        stack.clear();
        for (MazeCell[] row : grid) {
            for (MazeCell cell : row) {
                cell.visited = false;
            }
        }
        stack.push(startCell);
        startCell.visited = true;
        visitedCells.add(startCell);
        solving = true;

        solverTimer = new javax.swing.Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveStep();
            }
        });
        solverTimer.start();
    }

    // Ein einzelner Schritt des DFS-Lösungsalgorithmus
    private void solveStep() {
        if (stack.isEmpty()) {
            solverTimer.stop();
            solving = false;
            return;
        }
        MazeCell current = stack.peek();
        if (!solutionPath.contains(current)) {
            solutionPath.add(current);
        }
        if (current.equals(endCell)) {
            solverTimer.stop();
            solving = false;
            repaint();
            return;
        }
        MazeCell next = getUnvisitedAccessibleNeighbor(current);
        if (next != null) {
            next.visited = true;
            visitedCells.add(next);
            stack.push(next);
        } else {
            stack.pop();
            solutionPath.remove(current);
        }
        repaint();
    }

    // Gibt einen unbesuchten, erreichbaren Nachbarn zurück (keine Mauer im Weg)
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

    // Zeichnet das Labyrinth, die besuchten Zellen, den Lösungsweg sowie Start und Ziel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c].draw(g, cellSize);
            }
        }
        // Besuchte Zellen während der Suche hervorheben
        for (MazeCell cell : visitedCells) {
            int x = cell.col * cellSize;
            int y = cell.row * cellSize;
            g.setColor(new Color(200, 200, 255));
            g.fillRect(x + 2, y + 2, cellSize - 4, cellSize - 4);
        }
        // Lösungsweg als Linie darstellen
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
        // Start (grün) und Ziel (rot) markieren
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
