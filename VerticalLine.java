// Klasse für eine vertikale Linie, die von der Klasse Line erbt
public class VerticalLine extends Line
{
    // Konstruktor für eine vertikale Linie mit den gegebenen Koordinaten und Höhe
    public VerticalLine(int x, int y, int height)
    {
        // Aufruf des Konstruktors der Oberklasse Line mit den Start- und Endkoordinaten der Linie
        super(x, y, x, y + height);
    }
}

