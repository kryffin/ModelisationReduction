package modelisationReduction.pixels;

/**
 * @author KLEINHENTZ 'Kryffin' Nicolas
 */
public abstract class Pixel {

    /**
     * canal rouge (ou niveau de gris dans le cas d'un GrayPixel)
     */
    private int R;

    /**
     * canal vert
     */
    private int G;

    /**
     * canal bleu
     */
    private int B;

    /**
     * Constructeur vide
     */
    public Pixel () {
        this(0, 0, 0);
    }

    /**
     * Constructeur par renseignement des canaux
     * @param R canal rouge (ou niveau de gris pour un GrayPixel)
     * @param G canal vert
     * @param B canal bleu
     */
    public Pixel (int R, int G, int B) {
        this.R = R;
        this.G = G;
        this.B = B;
    }

    /**
     * Fonction calculant un pixel représentant la différence absolue de chaque canaux des pixels donnés
     * @param p pixel
     * @return pixel représentant la différence absolue
     */
    public abstract Pixel absoluteDifference (Pixel p);

    /**
     * Fonction retournant la moyenne des 3 canaux par luminance
     * @param p pixel à moyenner
     * @return entier représentant la 'moyenne' du pixel
     */
    public static int average (Pixel p) {
        //(0,2126 * R) + (0,7152 * G) + (0,0722 * B)
        return (int) ((0.2126 * p.R) + (0.7152 * p.G) + (0.0722 * p.B));
    }

    /**
     * @return canal rouge
     */
    public int getR () {
        return R;
    }

    /**
     * Setteur du canal rouge
     * @param r valeur du canal
     */
    public void setR (int r) {
        R = r;
    }

    /**
     * @return canal vert
     */
    public int getG () {
        return G;
    }

    /**
     * Setteur du canal vert
     * @param g valeur du canal
     */
    public void setG (int g) {
        G = g;
    }

    /**
     * @return canal bleu
     */
    public int getB () {
        return B;
    }

    /**
     * Setteur du canal bleu
     * @param b valeur du canal
     */
    public void setB (int b) {
        B = b;
    }

    public abstract String toString ();

}
