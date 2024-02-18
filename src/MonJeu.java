import guilines.IJeuDesBilles ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random ;

public class MonJeu implements IJeuDesBilles {
    public static final int VIDE = -1 ;
    private int score=0 ;
    private int nbBillesAjoutees=3 ;
    private int[] nouvellesCouleurs = new int[3] ;
    private List<Point> used = new ArrayList<Point>();
    private int nbColonnes = 5 ;
    private int nblignes = 5 ;
    private int nbCouleurs = 3 ;
    private int[][] Terrain = new int[this.nblignes][this.nbColonnes];


    public MonJeu(){
       this.reinit();
    }

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

    public boolean partieFinie(){
        boolean res = true ;
        for( int[]  a : this.Terrain){
            for ( int b : a){
                res = res && ( b != VIDE);
            }
        }
        return res ;
    }

    public int randomColor(){
        Random random = new Random();
        return random.nextInt(3);
    }

    public Point randomEmplacement (){
        Random random = new Random();
        Point point = new Point();
        point.setLocation( random.nextInt(this.nblignes) , random.nextInt(this.nbColonnes));
        while(Terrain[(int)point.getX()][(int)point.getY()] != VIDE && !this.partieFinie() ){
            point.setLocation( random.nextInt(this.nblignes) , random.nextInt(this.nbColonnes));
        }
        return point;
    }

    public int[] getNouvellesCouleurs(){
        for( int i = 0 ; i<3 ; i++){
            this.nouvellesCouleurs[i] = this.randomColor();
        }
        return this.nouvellesCouleurs;
    }
    public List<Point> deplace( int var1 , int var2 , int var3 , int var4){
   
        if ( this.Terrain[var1][var2] == VIDE){
            return used ;
        }
        if(this.Terrain[var3][var4] != VIDE){
            return used;
        }
        this.Terrain[var3][var4] = this.Terrain[var1][var2];
        this.Terrain[var1][var2] = VIDE;

        this.ajouterScore(this.Terrain[var3][var4] , var3 , var4);
        Point d = new Point( var1 , var2);
        used.add(d);
        d.setLocation(var3, var4);
        used.add(d);
        jouerBilles();
        return used ;
    } 


    public void jouerBilles(){
        for ( int i=0 ; i<this.nouvellesCouleurs.length ; i++){
            Point point = this.randomEmplacement();
            this.Terrain[(int)point.getX()][(int)point.getY()]=this.nouvellesCouleurs[i];
            this.ajouterScore(this.Terrain[(int)point.getX()][(int)point.getY()] , (int)point.getX() , (int)point.getY());
            this.used.add(point);        }
    }
    public void reinit(){
        this.score = 0 ;
        for( int i =  0 ; i<this.nblignes ; i ++){
            for ( int j = 0 ; j<this.nbColonnes ; j++){
                this.Terrain[i][j]=-1 ;
            }
        }
        for ( int i = 0 ; i<3 ; i++){
            Point point = this.randomEmplacement();
            int color = this.randomColor();
            this.Terrain[(int)point.getX()][(int)point.getY()]= color ;
        }
    }

  
    public int aligne(int couleur ,int x , int y , boolean sense){
        int cpt = 1 ;
        if ( sense){/* alignement horizontal */
            for( int i = x ; i< this.nblignes-1 ; i+= 1){
                if( this.Terrain[i][y] == this.Terrain[i+1][y]){
                    cpt++ ;
                }
            }
            for(  int i = x ; i>0 ; i+= -1){
                if( this.Terrain[i][y] == this.Terrain[i-1][y]){
                    cpt++ ;
                }
            
        }   
    }
    else{/* alignement vertical */
        for( int i = x ; i< this.nbColonnes-1 ; i+= 1){
            if( this.Terrain[x][i] == this.Terrain[x][i+1]){
                cpt++ ;
            }
        }
        for(  int i = x ; i>0 ; i+= -1){
            if( this.Terrain[x][i] == this.Terrain[x][i-1]){
                cpt++ ;
            }
        }
    }
        return cpt-1 ;
    }


    public void ajouterScore( int couleur , int x , int y){
        if ( aligne( couleur , x , y , true ) == 5){/* Alignement horizontal */
            this.score++ ;
        }
        if( aligne( couleur , x , y , false ) == 5){
            this.score++ ;/* aligement vertical */
        }
    }
    }
    

    

   