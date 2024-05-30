import java.awt.*;

// Klasse zur Darstellung einer Linie
public class Line
{
    private int xStart, yStart, xEnd, yEnd; // Start- und Endkoordinaten der Linie

    // Konstruktor für eine Linie mit den gegebenen Koordinaten
    public Line(int xStart, int yStart, int xEnd, int yEnd)
    {
        // Start- und Endkoordinaten setzen
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
    }

    // Methode zum Zeichnen der Linie auf einer Grafikkomponente
    public void draw(Graphics g)
    {
        g.drawLine(xStart, yStart, xEnd, yEnd); // Linie zeichnen
    }
}

