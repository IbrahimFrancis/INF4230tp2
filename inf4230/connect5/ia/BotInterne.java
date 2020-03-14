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
        if(isMaxTurn(grille))
            System.out.println("max");
        else
            System.out.println("min");
        listOfPosEvals.sort((PositionWithEval posEval1, PositionWithEval posEval2) -> posEval1.getEval() - posEval2.getEval());

        return listOfPosEvals.get(listOfPosEvals.size() - 1).getPos();
    }

    private PositionWithEval evaluateNextGrid(Grille grille, PositionWithEval nextPos) {
        // return new PositionWithEval(new Position(1, 2), 5);
        int groupe1PierreBlanche = 0;
        int groupe2PierreBlanche = 0;
        int groupe3PierreBlanche = 0;
        int groupe4PierreBlanche = 0;
        int groupe5PierreBlanche = 0;
        int groupe1PierreNoire = 0;
        int groupe2PierreNoire = 0;
        int groupe3PierreNoire = 0;
        int groupe4PierreNoire = 0;
        int groupe5PierreNoire = 0;

        int alignNOSE = 1;
        int alignSONE = 1;
        int alignOE = 1;
        int alignNS = 1;

        // additionne les pions successifs du nord-ouest et sud-est
        alignNOSE = alignNOSE + positionDiagonaleNO(grille, nextPos.getPos()) + positionDiagonaleSE(grille, position); 

        //additionne les pions successifs du sud-ouest et du nord-est 
        alignSONE = alignSONE + positionDiagonaleSO(grille, nextPos.getPos()) + positionDiagonaleNE(grille, position); 

        //additionne les pions de l'ouest et de l'est
        alignOE = alignOE + positionOuest(grille, nextPos.getPos()) + positionEst(grille, position);
        
        //additionne les pions du nord et du sud
        alignNS= alignNS + positionNord(grille, nextPos.getPos()) + positionSud(grille, position);

        //Si le pion n'est à côté d'aucun autre pion, groupe5pierre = 1; 
        //Le jeu sera terminé.
        if((alignNOSE == 5) || (alignSONE == 5) || (alignOE == 5) || (alignNS == 5)){
            groupe5PierreNoire = groupe5PierreNoire + 1;
        }
        //Si le pion n'est à côté d'aucun autre pion, groupe1pierre = 1; 
        if((alignNOSE == 1) || (alignSONE == 1) || (alignOE == 1) || (alignNS == 1)){
            groupe1PierreNoire = groupe1PierreNoire + 1;
        }

        int nbCol = grille.getData()[0].length;
        // on vérifie dans quel grouupe sont les autres pions sur le plateau
        for(int i = 0; i < grille.getData().length; i++){
            for(int j = 0; j < nbCol; j++){

                Position posTemp = new Position(i, j);
                // à faire : un if qui permettra de savoir pour quelle couleur on fait le compte
                //if(){}
                alignNOSE = 1;
                alignSONE = 1;
                alignOE = 1;
                alignNS = 1;

                // additionne les pions successifs du nord-ouest et sud-est
                alignNOSE = alignNOSE + positionDiagonaleNO(grille, posTemp) + positionDiagonaleSE(grille, posTemp); 

                //additionne les pions successifs du sud-ouest et du nord-est 
                alignSONE = alignSONE + positionDiagonaleSO(grille, posTemp) + positionDiagonaleNE(grille, posTemp); 

                //additionne les pions de l'ouest et de l'est
                alignOE = alignOE + positionOuest(grille, posTemp) + positionEst(grille, posTemp);
                
                //additionne les pions du nord et du sud
                alignNS= alignNS + positionNord(grille, posTemp) + positionSud(grille, posTemp);

                if((alignNOSE == 4) || (alignSONE == 4) || (alignOE == 4) || (alignNS == 4)){
                    groupe4PierreNoire = groupe4PierreNoire + 1;
                }

                if((alignNOSE == 3) || (alignSONE == 3) || (alignOE == 3) || (alignNS == 3)){
                    groupe3PierreNoire = groupe3PierreNoire + 1;
                }

                if((alignNOSE == 2) || (alignSONE == 2) || (alignOE == 2) || (alignNS == 2)){
                    groupe2PierreNoire = groupe2PierreNoire + 1;
                }

                if((alignNOSE == 1) || (alignSONE == 2) || (alignOE == 2) || (alignNS == 2)){
                    groupe1PierreNoire = groupe1PierreNoire + 1;
                }

                if((alignNOSE == 5) || (alignSONE == 5) || (alignOE == 5) || (alignNS == 5)){
                    groupe5PierreNoire = groupe5PierreNoire + 1;
                }

            }
        }

        return null;
    }


    //ici, on fait la diagonale l+1 et c+ 1 (SudEst)
    //Ici le nbPierreAlignDiag sert à compter les pions alignés
    // ex utilisation : positionDiagonaleEval(grille, nextPos.getPos())
    private int positionDiagonaleSE(Grille grille, Position position) {

        int nbPierreAlignDiagonale = 0;

        int ligneTemp = (position.ligne) + 1;
        int colonneTemp = (position.colonne) + 1;
        Position posTemp = new Position();

        //vérifie si on est dans les limites de la grille
        if((ligneTemp < grille.getData().length )&& (colonneTemp < grille.getData()[0].length )){
            posTemp.ligne = ligneTemp;
            posTemp.colonne = colonneTemp;

            //Si le prochain pion est de la même couleur que le précédent, on augmente nbpierreAlignDiagSE
            if(grille.get(posTemp) == grille.get(posTemp)){
                positionDiagonaleEval(grille, posTemp);
                nbPierreAlignDiagonale = nbPierreAlignDiagonale + 1;
            }
        }
        return nbPierreAlignDiagonale;
    }

    private int positionDiagonaleSO(Grille grille, Position position) {

        int nbPierreAlignDiagonale = 0;

        int ligneTemp = (position.ligne) + 1;
        int colonneTemp = (position.colonne) - 1;
        Position posTemp = new Position();

        //vérifie si on est dans les limites de la grille
        if((ligneTemp < grille.getData().length )&& (colonneTemp >= 0 )){
            posTemp.ligne = ligneTemp;
            posTemp.colonne = colonneTemp;

            //Si le prochain pion est de la même couleur que le précédent, on augmente nbpierreAlignDiagSO
            if(grille.get(posTemp) == grille.get(posTemp)){
                positionDiagonaleSO(grille, posTemp);
                nbPierreAlignDiagonale = nbPierreAlignDiagonale + 1;
            }
        }
        return nbPierreAlignDiagonale;
    }

    private int positionDiagonaleNE(Grille grille, Position position) {

        int nbPierreAlignDiagonale = 0;

        int ligneTemp = (position.ligne) - 1;
        int colonneTemp = (position.colonne) + 1;
        Position posTemp = new Position();

        //vérifie si on est dans les limites de la grille
        if((ligneTemp >=0 )&& (colonneTemp < grille.getData()[0].length)){
            posTemp.ligne = ligneTemp;
            posTemp.colonne = colonneTemp;

            //Si le prochain pion est de la même couleur que le précédent, on augmente nbpierreAlignNE 
            if(grille.get(posTemp) == grille.get(posTemp)){
                positionDiagonaleNE(grille, posTemp);
                nbPierreAlignDiagonale = nbPierreAlignDiagonale + 1;
            }
        }
        return nbPierreAlignDiagonale;
    }

    private int positionDiagonaleNO(Grille grille, Position position) {

        int nbPierreAlignDiagonale = 0;

        int ligneTemp = (position.ligne) - 1;
        int colonneTemp = (position.colonne) - 1;
        Position posTemp = new Position();

        //vérifie si on est dans les limites de la grille
        if((ligneTemp >=0 )&& (colonneTemp >= 0)){
            posTemp.ligne = ligneTemp;
            posTemp.colonne = colonneTemp;

            //Si le prochain pion est de la même couleur que le précédent, on augmente nbpierreAlignDiagNO 
            if(grille.get(posTemp) == grille.get(posTemp)){
                positionDiagonaleNO(grille, posTemp);
                nbPierreAlignDiagonale = nbPierreAlignDiagonale + 1;
            }
        }
        return nbPierreAlignDiagonale;
    }

    private int positionNord(Grille grille, Position position) {

        int nbPierreAlignNord = 0;

        int ligneTemp = (position.ligne) - 1;
        int colonneTemp = position.colonne;
        Position posTemp = new Position();

        //vérifie si on est dans les limites de la grille
        if(ligneTemp >=0 ){
            posTemp.ligne = ligneTemp;
            posTemp.colonne = colonneTemp;

            //Si le prochain pion est de la même couleur que le précédent, on augmente nbpierreAlignNord 
            if(grille.get(posTemp) == grille.get(posTemp)){
                positionNord(grille, posTemp);
                nbPierreAlignNord = nbPierreAlignNord + 1;
            }
        }
        return nbPierreAlignNord;
    }

    private int positionSud(Grille grille, Position position) {

        int nbPierreAlignSud = 0;

        int ligneTemp = (position.ligne) + 1;
        int colonneTemp = position.colonne;
        Position posTemp = new Position();

        //vérifie si on est dans les limites de la grille
        if(ligneTemp < grille.getData().length){
            posTemp.ligne = ligneTemp;
            posTemp.colonne = colonneTemp;

            //Si le prochain pion est de la même couleur que le précédent, on augmente nbpierreAlignSud 
            if(grille.get(posTemp) == grille.get(posTemp)){
                positionSud(grille, posTemp);
                nbPierreAlignSud = nbPierreAlignSud + 1;
            }
        }
        return nbPierreAlignSud;
    }

    private int positionEst(Grille grille, Position position) {

        int nbPierreAlignEst = 0;

        int ligneTemp = position.ligne;
        int colonneTemp = (position.colonne) + 1;
        Position posTemp = new Position();

        //vérifie si on est dans les limites de la grille
        if(colonneTemp >= grille.getData()[0].length){
            posTemp.ligne = ligneTemp;
            posTemp.colonne = colonneTemp;

            //Si le prochain pion est de la même couleur que le précédent, on augmente nbPierreAlignEst 
            if(grille.get(posTemp) == grille.get(posTemp)){
                positionEst(grille, posTemp);
                nbPierreAlignEst = nbPierreAlignEst + 1;
            }
        }
        return nbPierreAlignEst;
    }

    private int positionOuest(Grille grille, Position position) {

        int nbPierreAlignOuest = 0;

        int ligneTemp = position.ligne;
        int colonneTemp = (position.colonne) - 1;
        Position posTemp = new Position();

        //vérifie si on est dans les limites de la grille
        if(colonneTemp >= 0){
            posTemp.ligne = ligneTemp;
            posTemp.colonne = colonneTemp;

            //Si le prochain pion est de la même couleur que le précédent, on augmente nbpierreAlignOuest
            if(grille.get(posTemp) == grille.get(posTemp)){
                positionOuest(grille, posTemp);
                nbPierreAlignOuest = nbPierreAlignOuest + 1;
            }
        }
        return nbPierreAlignOuest;
    }

    private boolean isMaxTurn(Grille grille){
        boolean result = false;
        int turnNumber = grille.getNbCases() - grille.getNbCasesLibres();
        if(isNumEven(turnNumber))
            result = true;

        return result;
    }

    private boolean isNumEven(int number){
        boolean result = false;

        if(number % 2 == 0)
            result = true;
        
        return result;
    }

	@Override
    public String getAuteurs() {
        return "Philip D'Costa (DCOP17069401)  et  Ibrahim Francis Coulibaly (COUI03069706)";
    }

}
