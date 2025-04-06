package src;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

/**
 * Die MazeSolver-Klasse ist der Einstiegspunkt der Anwendung.
 * Sie erstellt das Hauptfenster (JFrame), fügt das MazePanel zur Darstellung
 * des Labyrinths hinzu und platziert einen Button, mit dem der animierte
 * Lösungsprozess gestartet werden kann.
 */
public class MazeSolver extends JFrame {

    /**
     * Konstruktor, der das Hauptfenster initialisiert, das MazePanel einbindet
     * und den Button zur Steuerung der Lösungsanimation hinzufügt.
     */
    public MazeSolver() {
        setTitle("Labyrinth Generator und LÖSER");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Erstelle das MazePanel mit den gewünschten Parametern:
        // 20 Spalten, 20 Zeilen, und 25 Pixel pro Zelle.
        MazePanel mazePanel = new MazePanel(20, 20, 25);
        add(mazePanel, BorderLayout.CENTER);

        // Erstelle einen Button, der beim Klicken den Lösungsalgorithmus startet.
        JButton solveButton = new JButton("Maze lösen");
        // Anonymer ActionListener, der die Methode startSolving im MazePanel aufruft.
        solveButton.addActionListener(e -> mazePanel.startSolving());
        // Packe den Button in ein eigenes Panel (für bessere Layout-Kontrolle).
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(solveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Packe alle Komponenten, zentriere das Fenster und zeige es an.
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Die main-Methode startet die Anwendung im Event Dispatch Thread,
     * um thread-sicher mit Swing zu arbeiten.
     *
     * @param args Kommandozeilenargumente (werden hier nicht verwendet)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MazeSolver::new);
    }
}
