package src;


import javax.swing.*;
import java.awt.BorderLayout;

public class MazeSolver extends JFrame {
    public MazeSolver() {
        setTitle("Labyrinth Generator und LÖSER");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // MazePanel mit Spalten, Zeilen und Zellgröße
        MazePanel mazePanel = new MazePanel(20, 20, 25);
        add(mazePanel, BorderLayout.CENTER);

        // Button zum Starten der Lösungsanimation
        JButton solveButton = new JButton("Maze lösen");
        solveButton.addActionListener(e -> mazePanel.startSolving());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(solveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MazeSolver::new);
    }
}
