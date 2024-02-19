import guilines.IJeuDesBilles ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random ;

public class MonJeu implements IJeuDesBilles {
    public static final int VIDE = -1 ; /* VARIABLE DEFINISSANT LE VIDE  */
    private int score=0 ; /* INITALISATION DU SCORE A 0 */
    private int nbBillesAjoutees=3 ; /* NB BILLES AJOUTEES DEFINI A 3 */
    private int[] nouvellesCouleurs = new int[3] ; /* TABLEAU DES NOUVELLES VOULEURS A FAORE TOMBER  */
    private List<Point> used = new ArrayList<Point>();/* LES BILLES DEJA DEPLACEES AVEC LES NOUVELLES COORDONNEES APRES DEPLACEMENT */
     /****** DIMENSIONS ******** */
    private int nbColonnes = 5 ;
    private int nblignes = 5 ;
    /**************************** */
    private int nbCouleurs = 3 ;
    private int[][] Terrain = new int[this.nblignes][this.nbColonnes];/* GRILLES DU JEU */


    public MonJeu(){/* CONSTRUCTEUR  */
       this.reinit();/* REINITNIT A CHAQUE INSTANCIATION */
    }
                                             /*********** GETTERS********* */
    public int getScore(){
        return this.score ;
    }

    public int getNbBillesAjoutees(){
        return this.nbBillesAjoutees;
    }

    public int getNbColonnes(){
        return this.nbColonnes;
    }

    public int getNbLignes() {
        return nblignes;
    }

    public int getNbCouleurs() {
        return nbCouleurs;
    }

    public int getCouleur ( int var1 , int var2){
        return Terrain[var1][var2];
    }

    public int[] getNouvellesCouleurs(){
        for( int i = 0 ; i<3 ; i++){
            this.nouvellesCouleurs[i] = this.randomColor();
        }
        return this.nouvellesCouleurs;
    }
                                             /******************************** */
    public boolean partieFinie(){/* VERIFIER SI TOUTES LES CASES SONT PLEINES POUR TERMINER LE JEU */
        boolean res = true ;
        for( int[]  a : this.Terrain){
            for ( int b : a){
                res = res && ( b != VIDE);
            }
        }
        return res ;
    }

    public int randomColor(){ /* CHOISIT DES CLRS AU HASARD DANS UN PANEL DE 0 A 2 */
        Random random = new Random();
        return random.nextInt(3);
    }

    public Point randomEmplacement (){/* CHOISIT DE MANIERE ALEATOIRE UN EMPLACEMENT VIDE  CETTE METHODE FAIT APPEL A PARTIE FINIE*/
        Random random = new Random();
        Point point = new Point();
        point.setLocation( random.nextInt(this.nblignes) , random.nextInt(this.nbColonnes));/* SELECTION ALEATOIRE DU POINT */
        while(Terrain[(int)point.getX()][(int)point.getY()] != VIDE && !this.partieFinie() ){/* AVANT DE JOUER VERIFIER SI LA PARTIE EST FINIE DEJA ET QUE LE POINT TROUVE PAR RANDOM EST DISPONIBLE  */
            point.setLocation( random.nextInt(this.nblignes) , random.nextInt(this.nbColonnes));
        }
        return point;
    }

    
    public List<Point> deplace( int var1 , int var2 , int var3 , int var4){
   /* METHODE PRINCIPALE */
        if ( this.Terrain[var1][var2] == VIDE){/* DEPART DUNE CASE PLEINE */
            return used ;
        }
        if(this.Terrain[var3][var4] != VIDE){ /* ARRIVEE DANS UNE CASE VIDE  */
            return used;
        }
        List<Point> list = new ArrayList<Point>();/* ARRAYTLIST POUR ENREGISTRER LES CASE PARCOURU A PARTIR DE LA CASE DE DEPART JUSQUA CELLE DARRIVEE */
        if( this.cheminPossible(var1, var2, var3, var4 , list)){ /* SI IL YA CHEMIN POSSIBLE ENTRE DEPART ET ARRIVEE ( HORIZONTAL OU VERTICAL) */
        this.Terrain[var3][var4] = this.Terrain[var1][var2];/* PERMUTATION */
        this.Terrain[var1][var2] = VIDE;
        this.ajouterScore(this.Terrain[var3][var4] , var3 , var4);/* VERIFIER SI IL YA ALIGENEMENT  */
        Point d = new Point( var1 , var2);/* AJOUT AU ARRAY LIST DES POINTS DEPLACES */
        used.add(d);
        d.setLocation(var3, var4);
        used.add(d);
        jouerBilles();/* JOUER LES BILLES ALEATOIREMENT */
        }
        return used ;
    } 


    public void jouerBilles(){ /* JOUE LES BILLES ALEATOIREMENT ET LES AJOUTE A ARRAYLIST USED( VOIR ATTRIBUTS ) */
        for ( int i=0 ; i<this.nouvellesCouleurs.length ; i++){
            Point point = this.randomEmplacement();/* CASE VIDE TROUVEE !! */
            this.Terrain[(int)point.getX()][(int)point.getY()]=this.nouvellesCouleurs[i];/* SELECTION DE COULEURS */
            this.ajouterScore(this.Terrain[(int)point.getX()][(int)point.getY()] , (int)point.getX() , (int)point.getY());/* INCREMENTER LE SCORE SI YA ALIGNEMENT PAR EMPLACEMENT ALEATOIRE DE LA MACHINE */
            this.used.add(point);  
        }
    }
    public void reinit(){
        this.score = 0 ;
        for( int i =  0 ; i<this.nblignes ; i ++){ /* TOUTES LES CASE SONT VIDE CEST IMPORTANT !! */
            for ( int j = 0 ; j<this.nbColonnes ; j++){
                this.Terrain[i][j]=-1 ;
            }
        }
        for ( int i = 0 ; i<3 ; i++){ /* PLACER 3 BILLES ALEATOIREMENT POUR COMMANCER LA PARTIE */
            Point point = this.randomEmplacement();
            int color = this.randomColor();
            this.Terrain[(int)point.getX()][(int)point.getY()]= color ;
        }
    }

  
    public int aligne(int couleur ,int x , int y , boolean sense){ /* VERIFIE SI IL YA ALIGNEMENT */
        int cpt = 1 ;
        if ( sense){/* alignement horizontal */
            for( int i = x ; i< this.nblignes-1 ; i+= 1){
                if( this.Terrain[x][y] == this.Terrain[i+1][y]){
                    cpt++ ;
                }
            }
            for(  int i = x ; i>0 ; i+= -1){
                if( this.Terrain[x][y] == this.Terrain[i-1][y]){
                    cpt++ ;
                }
            
        }   
    }
    else{/* alignement vertical */
        for( int i = y ; i< this.nbColonnes-1 ; i+= 1){
            if( this.Terrain[x][y] == this.Terrain[x][i+1]){
                cpt++ ;
            }
        }
        for(  int i = y ; i>0 ; i+= -1){
            if( this.Terrain[x][y] == this.Terrain[x][i-1]){
                cpt++ ;
            }
        }
    }
        return cpt ;
    }


    public void ajouterScore( int couleur , int x , int y){/* UPDATE SCORE */
        if ( aligne( couleur , x , y , true )  == 5){/* Alignement horizontal */
            this.score++ ;
        }
        if( aligne( couleur , x , y , false )  == 5){
            this.score++ ;/* aligement vertical */
        }
    }

    public List<Point> deplassable(int x , int y){ /* VERIFIE SI UNE BILLE EST DEPLACABLE */
        List<Point> array = new ArrayList<Point>() ;
        if( x != 0 && this.Terrain[x-1][y]== VIDE){
            array.add( new Point(x-1 , y));
        }
        if( y != this.nbColonnes-1 && this.Terrain[x][y+1]== VIDE){
            array.add( new Point(x , y+1));
        }
        if( x != this.nblignes-1 && this.Terrain[x+1][y]== VIDE){
            array.add( new Point(x+1 , y));
        }
        if( y != 0 && this.Terrain[x][y-1]== VIDE){
            array.add( new Point(x , y-1));
        } 
        return array ;
    }

    public boolean cheminPossible(int x, int y, int x1, int y1 , List<Point> liste) { /* VERIFIE RECURSIVEMENT SI YA AUMOINS UN CHEMIN POSSIBLE
        LA LIST OF POINT EST UTILISEE POUR SAUVEGRADER 
        LES CASES DEJA VISITEE */
        liste.add(new Point(x,y));
        if (x == x1 && y == y1) {
            return true;
        }
        List<Point> points = this.deplassable(x, y);/* LES POINT VOISINANTS DEPLASSABLES */
        if (!points.isEmpty()) {
            for (Point point : points) {
                if( !liste.contains(point)){
                if (cheminPossible(point.x, point.y, x1, y1 , liste)) {
                    return true;
                }
            }
            }
        }
        return false;
    }
    
}

   