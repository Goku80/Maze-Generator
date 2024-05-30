// Klasse für eine horizontale Linie, die von der Klasse Line erbt
public class HorizontalLine extends Line
{
    // Konstruktor für eine horizontale Linie mit den gegebenen Koordinaten und Länge
    public HorizontalLine(int x, int y, int length)
    {
        // Aufruf des Konstruktors der Oberklasse Line mit den Start- und Endkoordinaten der Linie
        super(x, y, x + length, y);
    }
}
