package modelisationReduction;

/**
 * @author KLEINHENTZ 'Kryffin' Nicolas
 */
public class Pixel {

    /**
     * canal rouge
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
     * @param R canal rouge
     * @param G canal vert
     * @param B canal bleu
     */
    public Pixel (int R, int G, int B) {
        this.R = R;
        this.G = G;
        this.B = B;
    }

    /**
     * Constructeur retournant un pixel moyen entre 2 pixels donnés (chaque canal est la moyenne de ce canal sur les 2 autres pixels)
     * @param p1 premier pixel
     * @param p2 second pixel
     */
    public Pixel (Pixel p1, Pixel p2) {
        this((p1.getR() + p2.getR()) / 2, (p1.getG() + p2.getG()) / 2, (p1.getB() + p2.getB()) / 2);
    }

    /**
     * Fonction calculant un pixel représentant la différence absolue de chaque canaux des pixels donnés
     * @param p1 premier pixel
     * @param p2 second pixel
     * @return pixel représentant la différence absolue
     */
    public static Pixel absoluteDifference (Pixel p1, Pixel p2) {
        return new Pixel(Math.abs(p1.getR() - p2.getR()), Math.abs(p1.getG() - p2.getG()), Math.abs(p1.getB() - p2.getB()));
    }

    /**
     * Fonction retournant un la moyenne des 3 canaux d'un pixel
     * @param p pixel à moyenner
     * @return entier représentant la moyenne du pixel
     */
    public static int average (Pixel p) {
        return (p.getR() + p.getG() + p.getB()) / 3;
    }

    /**
     * @return canal rouge
     */
    public int getR() {
        return R;
    }

    /**
     * Setteur du canal rouge
     * @param r valeur du canal
     */
    public void setR(int r) {
        R = r;
    }

    /**
     * @return canal vert
     */
    public int getG() {
        return G;
    }

    /**
     * Setteur du canal vert
     * @param g valeur du canal
     */
    public void setG(int g) {
        G = g;
    }

    /**
     * @return canal bleu
     */
    public int getB() {
        return B;
    }

    /**
     * Setteur du canal bleu
     * @param b valeur du canal
     */
    public void setB(int b) {
        B = b;
    }
}
