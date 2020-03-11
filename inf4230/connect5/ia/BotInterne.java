package inf4230.connect5.ia;
/*
 * Point de départ pour un Bot écrit en Java.
 *
 * Vous pouvez ajouter d'autres classes sous le package inf4230.connect5.ia.
 *
 * Prénom Nom    (CODE00000001)
 * Prénom Nom    (CODE00000002)
 */

import inf4230.connect5.Grille;
import inf4230.connect5.Joueur;
import inf4230.connect5.Position;
import java.util.ArrayList;
import java.util.Random;

public class BotInterne implements Joueur {

    private final Random random = new Random();

    /**
     * Voici la fonction à modifier.
     * Évidemment, vous pouvez ajouter d'autres fonctions dans BotInterne.
     * Vous pouvez aussi ajouter d'autres classes, mais elles doivent être
     * ajoutées dans le package inf4230.connect5.ia.
     * Vous ne pouvez pas modifier les autres fichiers dans les autres packages,
     * car ils seront écrasés.
     * 
     * @param grille Grille reçue (état courrant).
     * @param delais Délais de rélexion maximum (en secondes).
     * @return Retourne la meilleur meilleure actions.
     */
    // @Override
    // public Position getAction(Grille grille, int delais) {
    //     ArrayList<Integer> casesvides = new ArrayList<Integer>();
    //     int nbcol = grille.getData()[0].length;
    //     for(int l=0;l<grille.getData().length;l++)
    //         for(int c=0;c<nbcol;c++)
    //             if(grille.getData()[l][c]==0)
    //                 casesvides.add(l*nbcol+c);
    //     int choix = random.nextInt(casesvides.size());
    //     choix = casesvides.get(choix);
    //     return new Position(choix / nbcol, choix % nbcol);
    // }

    @Override
    public Position getAction(Grille grille, int delais) {
        ArrayList<PositionWithEval> listOfPosEvals = new ArrayList<>();

        int nbCol = grille.getData()[0].length;
        for(int i = 0; i < grille.getData().length; i++){
            for(int j = 0; j < nbCol; j++){
                Position nextPos = new Position(i, j);

                if(grille.getData()[i][j] == 0){
                    PositionWithEval posEval = evaluateNextGrid(grille, nextPos);
                    listOfPosEvals.add(posEval);
                }
            }
        }
        listOfPosEvals.sort((PositionWithEval posEval1, PositionWithEval posEval2) -> posEval1.getEval() - posEval2.getEval());

        return listOfPosEvals.get(listOfPosEvals.size() - 1).getPos();
    }

    private PositionWithEval evaluateNextGrid(Grille grille, Position nextPos) {
        // return new PositionWithEval(new Position(1, 2), 5);
        return null;
	}

	@Override
    public String getAuteurs() {
        return "Philip D'Costa (DCOP17069401)  et  Prénom2 Nom2 (CODE00000002)";
    }

}
