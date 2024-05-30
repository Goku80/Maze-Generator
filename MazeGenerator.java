import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import java.util.Vector;

// Klasse zum Erzeugen von Labyrinthen
public class MazeGenerator extends JFrame
{
    private static final int CELLSIZE = 50; // Größe einer Zelle im Labyrinth
    private Vector<Line> walls = new Vector<>(); // Vektor zum Speichern der Labyrinthwände

    // Konstruktor für das Labyrinthgenerator-Fenster
    public MazeGenerator()
    {
        setTitle("Maze Generator"); // Fenstertitel festlegen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Beim Schließen des Fensters das Programm beenden

        // Panel für die Darstellung des Labyrinths erstellen
        var panel = new JPanel()
        {
            // Methode zum Zeichnen des Labyrinths
            @Override
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g); // Vorhandene Zeichenfläche löschen
                subdivideChamber(new Chamber(0, 0, getWidth() / CELLSIZE, getHeight() / CELLSIZE)); // Labyrinth erzeugen

                // Alle Wände des Labyrinths zeichnen
                for (var line : walls)
                {
                    line.draw(g);
                }
            }
        };

        panel.setPreferredSize(new Dimension(500, 500)); // Größe des Panels festlegen
        setLocationRelativeTo(null); // Fenster zentrieren
        add(panel); // Panel zum Fenster hinzufügen
        pack(); // Fenstergröße anpassen
        setVisible(true); // Fenster sichtbar machen
    }

    // Methode zum Unterteilen einer Kammer in zwei Teile (vertikal oder horizontal)
    private void subdivideChamber(Chamber chamber)
    {
        // Überprüfen, ob die Kammer breiter oder höher ist
        if (chamber.getWidth() > chamber.getHeight())
        {
            subdivideVertically(chamber); // Kammer vertikal unterteilen
        }
        else if (chamber.getHeight() > chamber.getWidth())
        {
            subdivideHorizontally(chamber); // Kammer horizontal unterteilen
        }
        else
        {
            // Falls Kammer gleich breit und hoch ist, zufällig vertikal oder horizontal unterteilen
            var random = new Random();
            if (random.nextBoolean())
            {
                subdivideVertically(chamber); // Vertikal unterteilen
            }
            else
            {
                subdivideHorizontally(chamber); // Horizontal unterteilen
            }
        }
    }

    // Methode zum vertikalen Unterteilen einer Kammer
    private void subdivideVertically(Chamber chamber)
    {
        // Überprüfen, ob die Kammer eine Breite von mindestens 1 Zelle hat
        if (chamber.getWidth() <= 1)
        {
            return; // Falls nicht, abbrechen
        }

        // Zufällig eine Lücke in der Wand wählen
        int x = (int) (Math.random() * (chamber.getWidth() - 1)) + chamber.x1 + 1;
        int gap = (int) (Math.random() * chamber.getHeight());

        // Neue vertikale Wand hinzufügen, um die Kammer zu unterteilen
        if (gap > 0)
        {
            walls.add(new VerticalLine(x * CELLSIZE, chamber.y1 * CELLSIZE, gap * CELLSIZE));
        }

        // Neue vertikale Wand hinzufügen, um den verbleibenden Teil der Kammer zu unterteilen
        if ((chamber.getHeight() - gap - 1) > 0)
        {
            walls.add(new VerticalLine(x * CELLSIZE, (chamber.y1 + gap + 1) * CELLSIZE, (chamber.getHeight() - gap - 1) * CELLSIZE));
        }

        // Rekursiver Aufruf zum Unterteilen der beiden entstandenen Kammern
        subdivideChamber(new Chamber(chamber.x1, chamber.y1, x, chamber.y2));
        subdivideChamber(new Chamber(x, chamber.y1, chamber.x2, chamber.y2));
    }

    // Methode zum horizontalen Unterteilen einer Kammer
    private void subdivideHorizontally(Chamber chamber)
    {
        // Überprüfen, ob die Kammer eine Höhe von mindestens 1 Zelle hat
        if (chamber.getHeight() <= 1)
        {
            return; // Falls nicht, abbrechen
        }

        // Zufällig eine Lücke in der Wand wählen
        int y = (int) (Math.random() * (chamber.getHeight() - 1)) + chamber.y1 + 1;
        int gap = (int) (Math.random() * chamber.getWidth());

        // Neue horizontale Wand hinzufügen, um die Kammer zu unterteilen
        if (gap > 0)
        {
            walls.add(new HorizontalLine(chamber.x1 * CELLSIZE, y * CELLSIZE, gap * CELLSIZE));
        }

        // Neue horizontale Wand hinzufügen, um den verbleibenden Teil der Kammer zu unterteilen
        if ((chamber.getWidth() - gap - 1) > 0)
        {
            walls.add(new HorizontalLine((chamber.x1 + gap + 1) * CELLSIZE, y * CELLSIZE, (chamber.getWidth() - gap - 1) * CELLSIZE));
        }

        // Rekursiver Aufruf zum Unterteilen der beiden entstandenen Kammern
        subdivideChamber(new Chamber(chamber.x1, chamber.y1, chamber.x2, y));
        subdivideChamber(new Chamber(chamber.x1, y, chamber.x2, chamber.y2));
    }

    // Hauptmethode zum Starten des Labyrinthgenerators
    public static void main(String[] args)
    {
        new MazeGenerator(); // Neue Instanz des Labyrinthgenerators erstellen
    }
}

