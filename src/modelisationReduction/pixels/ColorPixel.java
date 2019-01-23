package modelisationReduction.pixels;

/**
 * @author KLEINHENTZ 'Kryffin' Nicolas
 */
public class ColorPixel extends Pixel {

    public ColorPixel () {
        super();
    }

    /**
     * Constructeur par renseignement des canaux
     * @param R canal rouge
     * @param G canal vert
     * @param B canal bleu
     */
    public ColorPixel (int R, int G, int B) {
        super(R, G, B);
    }

    /**
     * Constructeur d'un pixel moyen entre 2 pixels donnés (chaque canal est la moyenne de ce canal sur les 2 autres pixels)
     * @param p1 premier pixel
     * @param p2 second pixel
     */
    public ColorPixel (Pixel p1, Pixel p2) {
        this((p1.getR() + p2.getR()) / 2, (p1.getG() + p2.getG()) / 2, (p1.getB() + p2.getB()) / 2);
    }

    /**
     * Différence absolue entre deux pixels
     * @param p pixel à différencier
     * @return un pixel représentant la différence absolue des canaux des deux pixels
     */
    @Override
    public Pixel absoluteDifference (Pixel p) {
        return new ColorPixel(Math.abs(this.getR() - p.getR()), Math.abs(this.getG() - p.getG()), Math.abs(this.getB() - p.getB()));
    }

    @Override
    public String toString() {
        return "{" + getR() + ", " + getG() + ", " + getB() + "}";
    }
}
