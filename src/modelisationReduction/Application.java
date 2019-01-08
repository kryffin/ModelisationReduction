package modelisationReduction;

public class Application {

    public static void main (String[] args) {
        if (args.length != 2) {
            System.out.println("Le programme nécessite 2 arguments : java Application [fichierSource.pgm] [fichierCible.pgm]");
            return;
        }

        /*
        On convertit l'image fournie en tableau d'intérêt, on le convertit ensuite en graphe sur lequel on effectuera Bellman
        pour retrouver le chemin ayant le moins d'intérêt entre un pixel du haut et un du bas (à voir qui choisir).
        D'une manière ou d'une autre on garde ce parcours en mémoire pour supprimer les pixels parcourus sur l'image.
         */

    }

}
