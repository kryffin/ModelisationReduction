package modelisationReduction.pixels;

/**
 * @author KLEINHENTZ 'Kryffin' Nicolas
 */
public class GrayPixel extends Pixel {

    /**
     * Constructeur vide construisant un pixel avec un canal rouge à 0, le canal rouge est utilisé pour le niveau de gris
     */
    public GrayPixel () {
        super();
    }

    /**
     * Constructeur par renseignement du niveau de gris (le canal rouge d'un pixel représente le niveau de gris)
     * @param gray niveau de gris
     */
    public GrayPixel (int gray) {
        super(gray, 0, 0);
    }

    /**
     * Constructeur d'un pixel moyen entre 2 pixels donnés (le canal est la moyenne de ce canal sur les 2 autres pixels)
     * @param p1 premier pixel
     * @param p2 second pixel
     */
    public GrayPixel (Pixel p1, Pixel p2) {
        this((p1.getR() + p2.getR()) / 2);
    }

    /**
     * Différence absolue entre deux pixels gris
     * @param p pixel à différencier
     * @return un pixel représentant la différence absolue des niveaux de gris des deux pixels
     */
    @Override
    public Pixel absoluteDifference(Pixel p) {
        return new GrayPixel(Math.abs(this.getR() - p.getR()));
    }

    @Override
    public String toString() {
        return "{" + getR() + "}";
    }

}
