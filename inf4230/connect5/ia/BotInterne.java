package inf4230.connect5.ia;
/*
 * Point de départ pour un Bot écrit en Java.
 *
 * Vous pouvez ajouter d'autres classes sous le package inf4230.connect5.ia.
 *
 * Philip D'Costa (DCOP17069401) 
 * Ibrahim Francis Coulibaly (COUI03069706)
 */

import inf4230.connect5.Grille;
import inf4230.connect5.Joueur;
import inf4230.connect5.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
    //     // if(isMaxTurn(grille))
    //     //     System.out.println("max");
    //     // else
    //     //     System.out.println("min");
    //     return new Position(choix / nbcol, choix % nbcol);
    // }

    @Override
    public Position getAction(Grille grille, int delais) {
        ArrayList<PositionWithEval> listOfPosEvals = new ArrayList<>();
        boolean isMaxTurn = isMaxTurn(grille);
        Position result = null;
        
        int nbCol = grille.getData()[0].length;
        for(int i = 0; i < grille.getData().length; i++){
            for(int j = 0; j < nbCol; j++){
                PositionWithEval nextPos = new PositionWithEval(new Position(i, j), 0);

                if(grille.getData()[i][j] == 0){
                    PositionWithEval posEval = evaluateNextGrid(grille, nextPos, isMaxTurn);
                    listOfPosEvals.add(posEval);
                }
            }
        }
        
        listOfPosEvals.sort((PositionWithEval posEval1, PositionWithEval posEval2) -> posEval1.getEval() - posEval2.getEval());

        int bestEval;
        if(isMaxTurn)
            bestEval = listOfPosEvals.get(listOfPosEvals.size() - 1).getEval();
        else
            bestEval = listOfPosEvals.get(0).getEval();
        List<PositionWithEval> listOptimalResults = listOfPosEvals.stream().filter(x -> x.getEval() == bestEval).collect(Collectors.toList());
        int choice = random.nextInt(listOptimalResults.size());
        result = listOptimalResults.get(choice).getPos();

        // System.out.println("Turn Max: " + isMaxTurn);
        // listOfPosEvals.forEach(x -> System.out.print(x.getEval() + " -- " + x.getPos() + " | "));
        // System.out.println("Position: " + result.toString());

        return result;
    }

    private PositionWithEval evaluateNextGrid(Grille grille, PositionWithEval nextPos, boolean actualPlayerisMax) {
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

        int evalGr1pierre = 0;
        int evalGr2pierre = 0;
        int evalGr3pierre = 0;
        int evalGr4pierre = 0;
        int evalGr5pierre = 0;// on le garde pour l'instant mais pas vraiment besoin car jeux sera terminé s'il y a ça
        int evalTotal = 0;

        int joueurActuel = 1; //Par défaut, on est sur max.

        if (actualPlayerisMax == false){
            joueurActuel = 2;
        }
        
        int alignNOSE = 0;
        int alignSONE = 0;
        int alignOE = 0;
        int alignNS = 0;

        // additionne les pions successifs du nord-ouest et sud-est
        alignNOSE = alignNOSE + positionDiagonaleNO(grille, nextPos.getPos(), joueurActuel) + positionDiagonaleSE(grille, nextPos.getPos(), joueurActuel); 

        //additionne les pions successifs du sud-ouest et du nord-est 
        alignSONE = alignSONE + positionDiagonaleSO(grille, nextPos.getPos(), joueurActuel) + positionDiagonaleNE(grille, nextPos.getPos(), joueurActuel); 

        //additionne les pions de l'ouest et de l'est
        alignOE = alignOE + positionOuest(grille, nextPos.getPos(), joueurActuel) + positionEst(grille, nextPos.getPos(), joueurActuel);
        
        //additionne les pions du nord et du sud
        alignNS= alignNS + positionNord(grille, nextPos.getPos(), joueurActuel) + positionSud(grille, nextPos.getPos(), joueurActuel);

        //Si le pion n'est à côté d'aucun autre pion, groupe5pierre = 1; 
        //Le jeu sera terminé.
        
        if (actualPlayerisMax == false){
            if((alignNOSE == 5) || (alignSONE == 5) || (alignOE == 5) || (alignNS == 5)){
                groupe5PierreBlanche = groupe5PierreBlanche + 1;
            }
            //Si le pion n'est à côté d'aucun autre pion, groupe1pierre = 1; 
            if(alignNOSE == 5){
                groupe5PierreBlanche = groupe5PierreBlanche + 1;
            }
            if(alignSONE == 5){
                groupe5PierreBlanche = groupe5PierreBlanche + 1;
            }
            if(alignOE == 5){
                groupe5PierreBlanche = groupe5PierreBlanche + 1;
            }
            if(alignNS == 5){
                groupe5PierreBlanche = groupe5PierreBlanche + 1;
            }
            if(alignNOSE == 4){
                groupe4PierreBlanche = groupe4PierreBlanche + 1;
            }
            if(alignSONE == 4){
                groupe4PierreBlanche = groupe4PierreBlanche + 1;
            }
            if(alignOE == 4){
                groupe4PierreBlanche = groupe4PierreBlanche + 1;
            }
            if(alignNS == 4){
                groupe4PierreBlanche = groupe4PierreBlanche + 1;
            }
    
            if(alignNOSE == 3){
                groupe3PierreBlanche = groupe3PierreBlanche + 1;
            }
            if(alignSONE == 3){
                groupe3PierreBlanche = groupe3PierreBlanche + 1;
            }
            if(alignOE == 3){
                groupe3PierreBlanche = groupe3PierreBlanche + 1;
            }
            if(alignNS == 3){
                groupe3PierreBlanche = groupe3PierreBlanche + 1;
            }
    
            if(alignNOSE == 2){
                groupe2PierreBlanche = groupe2PierreBlanche + 1;
            }
            if(alignSONE == 2){
                groupe2PierreBlanche = groupe2PierreBlanche + 1;
            }
            if(alignOE == 2){
                groupe2PierreBlanche = groupe2PierreBlanche + 1;
            }
            if(alignNS == 2){
                groupe2PierreBlanche = groupe2PierreBlanche + 1;
            }
    
            if(alignNOSE == 1){
                groupe1PierreBlanche = groupe1PierreBlanche + 1;
            }
            if(alignSONE == 1){
                groupe1PierreBlanche = groupe1PierreBlanche + 1;
            }
            if(alignOE == 1){
                groupe1PierreBlanche = groupe1PierreBlanche + 1;
            }
            if(alignNS == 1){
                groupe1PierreBlanche = groupe1PierreBlanche + 1;
            }
        } else {
            //Si le pion n'est à côté d'aucun autre pion, groupe1pierre = 1;
            if(alignNOSE == 5){
                groupe5PierreNoire = groupe5PierreNoire + 1;
            }
            if(alignSONE == 5){
                groupe5PierreNoire = groupe5PierreNoire + 1;
            }
            if(alignOE == 5){
                groupe5PierreNoire = groupe5PierreNoire + 1;
            }
            if(alignNS == 5){
                groupe5PierreNoire = groupe5PierreNoire + 1;
            }
            if(alignNOSE == 4){
                groupe4PierreNoire = groupe4PierreNoire + 1;
            }
            if(alignSONE == 4){
                groupe4PierreNoire = groupe4PierreNoire + 1;
            }
            if(alignOE == 4){
                groupe4PierreNoire = groupe4PierreNoire + 1;
            }
            if(alignNS == 4){
                groupe4PierreNoire = groupe4PierreNoire + 1;
            }

            if(alignNOSE == 3){
                groupe3PierreNoire = groupe3PierreNoire + 1;
            }
            if(alignSONE == 3){
                groupe3PierreNoire = groupe3PierreNoire + 1;
            }
            if(alignOE == 3){
                groupe3PierreNoire = groupe3PierreNoire + 1;
            }
            if(alignNS == 3){
                groupe3PierreNoire = groupe3PierreNoire + 1;
            }

            if(alignNOSE == 2){
                groupe2PierreNoire = groupe2PierreNoire + 1;
            }
            if(alignSONE == 2){
                groupe2PierreNoire = groupe2PierreNoire + 1;
            }
            if(alignOE == 2){
                groupe2PierreNoire = groupe2PierreNoire + 1;
            }
            if(alignNS == 2){
                groupe2PierreNoire = groupe2PierreNoire + 1;
            }

            if(alignNOSE == 1){
                groupe1PierreNoire = groupe1PierreNoire + 1;
            }
            if(alignSONE == 1){
                groupe1PierreNoire = groupe1PierreNoire + 1;
            }
            if(alignOE == 1){
                groupe1PierreNoire = groupe1PierreNoire + 1;
            }
            if(alignNS == 1){
                groupe1PierreNoire = groupe1PierreNoire + 1;
            }
        }

        int nbCol = grille.getData()[0].length;
        // on vérifie dans quel grouupe sont les autres pions sur le plateau
        for(int i = 0; i < grille.getData().length; i++){
            for(int j = 0; j < nbCol; j++){

                if(grille.getData()[i][j] == 1){

                    Position posTemp = new Position(i, j);
                    
                    alignNOSE = 0;
                    alignSONE = 0;
                    alignOE = 0;
                    alignNS = 0;

                    // additionne les pions successifs du nord-ouest et sud-est
                    alignNOSE = alignNOSE + positionDiagonaleNO(grille, posTemp, 1) + positionDiagonaleSE(grille, posTemp, 1); 

                    //additionne les pions successifs du sud-ouest et du nord-est 
                    alignSONE = alignSONE + positionDiagonaleSO(grille, posTemp, 1) + positionDiagonaleNE(grille, posTemp, 1); 

                    //additionne les pions de l'ouest et de l'est
                    alignOE = alignOE + positionOuest(grille, posTemp, 1) + positionEst(grille, posTemp, 1);
                    
                    //additionne les pions du nord et du sud
                    alignNS= alignNS + positionNord(grille, posTemp, 1) + positionSud(grille, posTemp, 1);

                    if(alignNOSE == 5){
                        groupe5PierreNoire = groupe5PierreNoire + 1;
                    }
                    if(alignSONE == 5){
                        groupe5PierreNoire = groupe5PierreNoire + 1;
                    }
                    if(alignOE == 5){
                        groupe5PierreNoire = groupe5PierreNoire + 1;
                    }
                    if(alignNS == 5){
                        groupe5PierreNoire = groupe5PierreNoire + 1;
                    }
                    if(alignNOSE == 4){
                        groupe4PierreNoire = groupe4PierreNoire + 1;
                    }
                    if(alignSONE == 4){
                        groupe4PierreNoire = groupe4PierreNoire + 1;
                    }
                    if(alignOE == 4){
                        groupe4PierreNoire = groupe4PierreNoire + 1;
                    }
                    if(alignNS == 4){
                        groupe4PierreNoire = groupe4PierreNoire + 1;
                    }

                    if(alignNOSE == 3){
                        groupe3PierreNoire = groupe3PierreNoire + 1;
                    }
                    if(alignSONE == 3){
                        groupe3PierreNoire = groupe3PierreNoire + 1;
                    }
                    if(alignOE == 3){
                        groupe3PierreNoire = groupe3PierreNoire + 1;
                    }
                    if(alignNS == 3){
                        groupe3PierreNoire = groupe3PierreNoire + 1;
                    }

                    if(alignNOSE == 2){
                        groupe2PierreNoire = groupe2PierreNoire + 1;
                    }
                    if(alignSONE == 2){
                        groupe2PierreNoire = groupe2PierreNoire + 1;
                    }
                    if(alignOE == 2){
                        groupe2PierreNoire = groupe2PierreNoire + 1;
                    }
                    if(alignNS == 2){
                        groupe2PierreNoire = groupe2PierreNoire + 1;
                    }

                    if(alignNOSE == 1){
                        groupe1PierreNoire = groupe1PierreNoire + 1;
                    }
                    if(alignSONE == 1){
                        groupe1PierreNoire = groupe1PierreNoire + 1;
                    }
                    if(alignOE == 1){
                        groupe1PierreNoire = groupe1PierreNoire + 1;
                    }
                    if(alignNS == 1){
                        groupe1PierreNoire = groupe1PierreNoire + 1;
                    }

                } else if(grille.getData()[i][j] == 2){

                    alignNOSE = 0;
                    alignSONE = 0;
                    alignOE = 0;
                    alignNS = 0;

                    Position posTemp = new Position(i, j);

                    alignNOSE = alignNOSE + positionDiagonaleNO(grille, posTemp, 1) + positionDiagonaleSE(grille, posTemp, 1); 

                    //additionne les pions successifs du sud-ouest et du nord-est 
                    alignSONE = alignSONE + positionDiagonaleSO(grille, posTemp, 1) + positionDiagonaleNE(grille, posTemp, 1); 

                    //additionne les pions de l'ouest et de l'est
                    alignOE = alignOE + positionOuest(grille, posTemp, 1) + positionEst(grille, posTemp, 1);
                    
                    //additionne les pions du nord et du sud
                    alignNS= alignNS + positionNord(grille, posTemp, 1) + positionSud(grille, posTemp, 1);

                    if(alignNOSE == 5){
                        groupe5PierreBlanche = groupe5PierreBlanche + 1;
                    }
                    if(alignSONE == 5){
                        groupe5PierreBlanche = groupe5PierreBlanche + 1;
                    }
                    if(alignOE == 5){
                        groupe5PierreBlanche = groupe5PierreBlanche + 1;
                    }
                    if(alignNS == 5){
                        groupe5PierreBlanche = groupe5PierreBlanche + 1;
                    }
                    if(alignNOSE == 4){
                        groupe4PierreBlanche = groupe4PierreBlanche + 1;
                    }
                    if(alignSONE == 4){
                        groupe4PierreBlanche = groupe4PierreBlanche + 1;
                    }
                    if(alignOE == 4){
                        groupe4PierreBlanche = groupe4PierreBlanche + 1;
                    }
                    if(alignNS == 4){
                        groupe4PierreBlanche = groupe4PierreBlanche + 1;
                    }
            
                    if(alignNOSE == 3){
                        groupe3PierreBlanche = groupe3PierreBlanche + 1;
                    }
                    if(alignSONE == 3){
                        groupe3PierreBlanche = groupe3PierreBlanche + 1;
                    }
                    if(alignOE == 3){
                        groupe3PierreBlanche = groupe3PierreBlanche + 1;
                    }
                    if(alignNS == 3){
                        groupe3PierreBlanche = groupe3PierreBlanche + 1;
                    }
            
                    if(alignNOSE == 2){
                        groupe2PierreBlanche = groupe2PierreBlanche + 1;
                    }
                    if(alignSONE == 2){
                        groupe2PierreBlanche = groupe2PierreBlanche + 1;
                    }
                    if(alignOE == 2){
                        groupe2PierreBlanche = groupe2PierreBlanche + 1;
                    }
                    if(alignNS == 2){
                        groupe2PierreBlanche = groupe2PierreBlanche + 1;
                    }
            
                    if(alignNOSE == 1){
                        groupe1PierreBlanche = groupe1PierreBlanche + 1;
                    }
                    if(alignSONE == 1){
                        groupe1PierreBlanche = groupe1PierreBlanche + 1;
                    }
                    if(alignOE == 1){
                        groupe1PierreBlanche = groupe1PierreBlanche + 1;
                    }
                    if(alignNS == 1){
                        groupe1PierreBlanche = groupe1PierreBlanche + 1;
                    }
                }

            }
        }

        if (actualPlayerisMax == true){
            evalGr1pierre = groupe1PierreNoire - groupe1PierreBlanche;
            evalGr2pierre = groupe2PierreNoire - groupe2PierreBlanche;
            evalGr3pierre = groupe3PierreNoire - groupe3PierreBlanche;
            evalGr4pierre = groupe4PierreNoire - groupe4PierreBlanche;
            evalGr5pierre = groupe5PierreNoire - groupe5PierreBlanche;
        } else {
            evalGr1pierre = groupe1PierreBlanche - groupe1PierreNoire;
            evalGr2pierre = groupe2PierreBlanche - groupe2PierreNoire;
            evalGr3pierre = groupe3PierreBlanche - groupe3PierreNoire;
            evalGr4pierre = groupe4PierreBlanche - groupe4PierreNoire;
            evalGr5pierre = groupe5PierreBlanche - groupe5PierreNoire;
        }

        evalTotal = evalGr1pierre + (evalGr2pierre*10) + (evalGr3pierre*100) + (evalGr4pierre*1000) + (evalGr5pierre*10000);

        nextPos.setEval(evalTotal);

        return nextPos;
    }


    //ici, on fait la diagonale l+1 et c+ 1 (SudEst)
    //Ici le nbPierreAlignDiag sert à compter les pions alignés
    // ex utilisation : positionDiagonaleEval(grille, nextPos.getPos())
    
    private int positionDiagonaleSE(Grille grille, Position position, int joueur) {

        int nbPierreAlignDiagonale = 0;

        int ligneTemp = (position.ligne) + 1;
        int colonneTemp = (position.colonne) + 1;
        Position posTemp = new Position();

        //vérifie si on est dans les limites de la grille
        if((ligneTemp < grille.getData().length )&& (colonneTemp < grille.getData()[0].length )){
            posTemp.ligne = ligneTemp;
            posTemp.colonne = colonneTemp;

            //Si le prochain pion est de la même couleur que le précédent, on augmente nbpierreAlignDiagSE
            if(grille.get(posTemp) == joueur){
                positionDiagonaleSE(grille, posTemp, joueur);
                nbPierreAlignDiagonale = nbPierreAlignDiagonale + 1;
            }
        }
        return nbPierreAlignDiagonale;
    }

    private int positionDiagonaleSO(Grille grille, Position position, int joueur) {

        int nbPierreAlignDiagonale = 0;

        int ligneTemp = (position.ligne) + 1;
        int colonneTemp = (position.colonne) - 1;
        Position posTemp = new Position();

        //vérifie si on est dans les limites de la grille
        if((ligneTemp < grille.getData().length )&& (colonneTemp >= 0 )){
            posTemp.ligne = ligneTemp;
            posTemp.colonne = colonneTemp;

            //Si le prochain pion est de la même couleur que le précédent, on augmente nbpierreAlignDiagSO
            if(grille.get(posTemp) == joueur){
                positionDiagonaleSO(grille, posTemp, joueur);
                nbPierreAlignDiagonale = nbPierreAlignDiagonale + 1;
            }
        }
        return nbPierreAlignDiagonale;
    }

    private int positionDiagonaleNE(Grille grille, Position position, int joueur) {

        int nbPierreAlignDiagonale = 0;

        int ligneTemp = (position.ligne) - 1;
        int colonneTemp = (position.colonne) + 1;
        Position posTemp = new Position();

        //vérifie si on est dans les limites de la grille
        if((ligneTemp >=0 )&& (colonneTemp < grille.getData()[0].length)){
            posTemp.ligne = ligneTemp;
            posTemp.colonne = colonneTemp;

            //Si le prochain pion est de la même couleur que le précédent, on augmente nbpierreAlignNE 
            if(grille.get(posTemp) == joueur){
                positionDiagonaleNE(grille, posTemp, joueur);
                nbPierreAlignDiagonale = nbPierreAlignDiagonale + 1;
            }
        }
        return nbPierreAlignDiagonale;
    }

    private int positionDiagonaleNO(Grille grille, Position position, int joueur) {

        int nbPierreAlignDiagonale = 0;

        int ligneTemp = (position.ligne) - 1;
        int colonneTemp = (position.colonne) - 1;
        Position posTemp = new Position();

        //vérifie si on est dans les limites de la grille
        if((ligneTemp >=0 )&& (colonneTemp >= 0)){
            posTemp.ligne = ligneTemp;
            posTemp.colonne = colonneTemp;

            //Si le prochain pion est de la même couleur que le précédent, on augmente nbpierreAlignDiagNO 
            if(grille.get(posTemp) == joueur){
                positionDiagonaleNO(grille, posTemp, joueur);
                nbPierreAlignDiagonale = nbPierreAlignDiagonale + 1;
            }
        }
        return nbPierreAlignDiagonale;
    }

    private int positionNord(Grille grille, Position position, int joueur) {

        int nbPierreAlignNord = 0;

        int ligneTemp = (position.ligne) - 1;
        int colonneTemp = position.colonne;
        Position posTemp = new Position();

        //vérifie si on est dans les limites de la grille
        if(ligneTemp >=0 ){
            posTemp.ligne = ligneTemp;
            posTemp.colonne = colonneTemp;

            //Si le prochain pion est de la même couleur que le précédent, on augmente nbpierreAlignNord 
            if(grille.get(posTemp) == joueur){
                positionNord(grille, posTemp, joueur);
                nbPierreAlignNord = nbPierreAlignNord + 1;
            }
        }
        return nbPierreAlignNord;
    }

    private int positionSud(Grille grille, Position position, int joueur) {

        int nbPierreAlignSud = 0;

        int ligneTemp = (position.ligne) + 1;
        int colonneTemp = position.colonne;
        Position posTemp = new Position();

        //vérifie si on est dans les limites de la grille
        if(ligneTemp < grille.getData().length){
            posTemp.ligne = ligneTemp;
            posTemp.colonne = colonneTemp;

            //Si le prochain pion est de la même couleur que le précédent, on augmente nbpierreAlignSud 
            if(grille.get(posTemp) == joueur){
                positionSud(grille, posTemp, joueur);
                nbPierreAlignSud = nbPierreAlignSud + 1;
            }
        }
        return nbPierreAlignSud;
    }

    private int positionEst(Grille grille, Position position, int joueur) {

        int nbPierreAlignEst = 0;

        int ligneTemp = position.ligne;
        int colonneTemp = (position.colonne) + 1;
        Position posTemp = new Position();

        //vérifie si on est dans les limites de la grille
        if(colonneTemp < grille.getData()[0].length){
            posTemp.ligne = ligneTemp;
            posTemp.colonne = colonneTemp;

            //Si le prochain pion est de la même couleur que le précédent, on augmente nbPierreAlignEst 
            if(grille.get(posTemp) == joueur){
                positionEst(grille, posTemp, joueur);
                nbPierreAlignEst = nbPierreAlignEst + 1;
            }
        }
        return nbPierreAlignEst;
    }

    private int positionOuest(Grille grille, Position position, int joueur) {

        int nbPierreAlignOuest = 0;

        int ligneTemp = position.ligne;
        int colonneTemp = (position.colonne) - 1;
        Position posTemp = new Position();

        //vérifie si on est dans les limites de la grille
        if(colonneTemp >= 0){
            posTemp.ligne = ligneTemp;
            posTemp.colonne = colonneTemp;

            //Si le prochain pion est de la même couleur que le précédent, on augmente nbpierreAlignOuest
            if(grille.get(posTemp) == joueur){
                positionOuest(grille, posTemp, joueur);
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
