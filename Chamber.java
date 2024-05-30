// Klasse zur Darstellung einer Kammer im Labyrinth
public class Chamber
{
    int x1, y1, x2, y2; // Koordinaten der oberen linken und unteren rechten Ecke der Kammer

    // Konstruktor für eine Kammer mit den gegebenen Koordinaten
    public Chamber(int xUpperLeft, int yUpperLeft, int xLowerRight, int yLowerRight)
    {
        // Die Koordinaten der Ecken setzen
        this.x1 = xUpperLeft;
        this.y1 = yUpperLeft;
        this.x2 = xLowerRight;
        this.y2 = yLowerRight;
    }

    // Methode zur Berechnung der Breite der Kammer
    public int getWidth()
    {
        return x2 - x1; // Differenz der x-Koordinaten der Ecken
    }

    // Methode zur Berechnung der Höhe der Kammer
    public int getHeight()
    {
        return y2 - y1; // Differenz der y-Koordinaten der Ecken
    }

    // Überschriebene toString-Methode zur Darstellung der Kammerinformationen
    @Override
    public String toString()
    {
        return "Chamber: top left(" + x1 + "," + y1 +
                "), bottom right(" + x2 + "," + y2 +
                "), Width: " + getWidth() + ", Height: " + getHeight() + ")";
    }
}

