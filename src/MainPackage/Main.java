package MainPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Main {
	
	/*** variables utilisees pour ESSAIS SUCCESSIFS **************************************************/
	//en euros
	static final double monnaieARendreES=1.87;
	//static final double monnaieARendreES=6.33;
	//static final double monnaieARendreES=15.15;
	
	static List<Double> piecesUtilisables = new ArrayList<Double>();//vecteur des valeurs de pieces utilisables
	static int n;//taille du vecteur
	
	static List<Integer> X = new ArrayList<Integer>();//vecteur du nb de pieces utilise pour chaque valeur de piece
	static int nbPieces=0;//somme du vecteur X
	static double somcour=0.0;//somme courantes des valeurs des pieces de X
	
	static List<Integer> bestX = new ArrayList<Integer>();//le meilleur vecteur X (utilisant le moins de pieces)
	static int bestNbPieces=(int) (monnaieARendreES*100)+1;//on initialise au "pire cas" correspondant a (monnaieARendre*100) pieces de 0.01 
	
	static int nbAppelsEssaisSuccessifs=0;
	/*********************************************************************************************************/
	
	/*** variables utilisées pour PROGRAMMATION DYNAMIQUE **************************************************/
	//en centimes
	static int monnaieARendrePD = 187;
	//static int monnaieARendrePD = 633;
	//static int monnaieARendrePD = 1515;
	
	private final static List<Integer> piecesDynamiques= new ArrayList<Integer>();
	/*********************************************************************************************************/
	
	/*** variables utilisees pour GLOUTON **************************************************/
	//en euros
	static final double monnaieARendreG=1.87;
	//static final double monnaieARendreG=6.33;
	//static final double monnaieARendreG=15.15;
	
	private final static int OBJECTIF_DYNAMIQUE = 6000; // Nombre de centimes en entier
	/*********************************************************************************************************/
	
	
	public static void main(String[] args) {
		
		Chrono chrono = new Chrono();
		double duree;
		
		/*** ESSAIS SUCCESSIFS ***/
		System.out.println("ESSAIS SUCCESSIFS");
		//initialiser les pieces utilisables
		piecesUtilisables.add(1.0);
		piecesUtilisables.add(0.2);
		piecesUtilisables.add(0.1);
		piecesUtilisables.add(0.01);
		piecesUtilisables.add(0.05);
		piecesUtilisables.add(0.02);
		piecesUtilisables.add(2.0);
		piecesUtilisables.add(0.5);
		n=piecesUtilisables.size();

		//initialisation des vecteurs X et bestX
		X=initVecteur(X,n);
		bestX=initVecteur(bestX,n);
		
		//appel a la fonction essais successifs
		chrono.start(); 
		solutionEssaisSuccessifs(0);
		chrono.stop(); // arrêt
		duree=chrono.getDureeMs(); // affichage du résultat en millisecondes
		
		//affichage de la solution
		//afficherSolutionEssaisSuccessifs(bestX);
		System.out.println("Meilleure solution essais successifs (en "+nbAppelsEssaisSuccessifs+" appels) (en "+duree+"ms) pour rendre "+monnaieARendreES+"€ :");
		afficherSES(bestX);
		System.out.println();
		System.out.println();
		/*** FIN ESSAIS SUCCESSIFS ***/
		
		/*** PROGRAMMATION DYNAMIQUE ***/
		System.out.println("PROGRAMMATION DYNAMIQUE");
		
		piecesDynamiques.add(200);
		piecesDynamiques.add(100);
		piecesDynamiques.add(50);
		piecesDynamiques.add(20);
		piecesDynamiques.add(10);
		piecesDynamiques.add(5);
		piecesDynamiques.add(2);
		piecesDynamiques.add(1);
		
		//List<Piece> algoDynamique = rechercheDynamique(piecesUtilisables, OBJECTIF);
		int i = piecesDynamiques.size();
		//System.out.println(piecesDynamiques+" - " + i);
		int j = OBJECTIF_DYNAMIQUE;
		
		
		/*for (int[] x : tab)
		{
		   for (int y : x)
		   {
			   if(y>0) {
				   System.out.print("+"+y+" ");
			   }
			   else {
				   System.out.print(y + " ");
			   }
		   }
		   System.out.println();
		}*/
		
		/*List<Integer> pieces = NBP(i, j, tab);
		System.out.println(pieces);*/
		//System.out.println(889 + " - " + NBP(8, 889, tab));
		//System.out.println(5364 + " - " + NBP(8, 5364, tab));
		//System.out.println();
		//System.out.println(NBP(8, 501, tab));
		//System.out.println(NBP(8, 501, tab));
		
		chrono.start(); 
		int[][] tab = remplirDynamiquement(i, j);
		List<Integer> listePD = NBP(piecesDynamiques.size(), monnaieARendrePD, tab);
		chrono.stop(); // arrêt
		duree=chrono.getDureeMs(); // affichage du résultat en millisecondes
		
		String centimes=""+monnaieARendrePD%100;
		if(monnaieARendrePD%100<10) {
			centimes="0"+centimes;
		}
		System.out.println("Meilleure solution programmation dynamique (en "+duree+"ms) pour rendre "+monnaieARendrePD/100+"."+centimes+"€ :");
		
		afficherSPD(listePD);
		System.out.println();
		System.out.println();
		
		/*** FIN PROGRAMMATION DYNAMIQUE ***/
		
		/*** ALGORITHME GLOUTON ***/
		List<Double> piecesUtilisables = new ArrayList<Double>();
		
		piecesUtilisables.add(1.0);
		piecesUtilisables.add(0.2);
		piecesUtilisables.add(0.1);
		piecesUtilisables.add(0.01);
		piecesUtilisables.add(0.05);
		piecesUtilisables.add(0.02);
		piecesUtilisables.add(2.0);
		piecesUtilisables.add(0.5);
		
		// Autre jeu de pieces
		/*
		List<Piece> autresPiecesUtilisables = new ArrayList<Piece>();
		
		autresPiecesUtilisables.add(new Piece(6));
		autresPiecesUtilisables.add(new Piece(4));
		autresPiecesUtilisables.add(new Piece(1));
		*/
		
		System.out.println("ALGORITHME GLOUTON");
		//Il faut prouver ou infirmer qu’un algorithme glouton est exact pour chaque probleme traite.
			// => Si non exact, contre exemple,
			// => Sinon Etablir par recurrence que la solution en construction est constamment optimale
			// => Ou partir de la solution gloutonne puis montrer que la transformation par echange de deux choix ne peut l'ameliorer
		
		// Principe pour les pieces de monnaie : 
		
		chrono.start(); 
		solutionEssaisSuccessifs(0);
		chrono.stop(); // arrêt
		duree=chrono.getDureeMs(); // affichage du résultat en millisecondes
		
		//affichage de la solution
		List<Double> listeG=combinaisonGlouton(piecesUtilisables, monnaieARendreG);
		System.out.println("Meilleure solution glouton (en "+duree+"ms) pour rendre "+monnaieARendreG+"€ :");
		afficherSG(listeG);
		/*** FIN ALGORITHME GLOUTON ***/
	}
	
	/*** ESSAIS SUCCESSIFS ***/
	static void solutionEssaisSuccessifs(int i) {//i représente l'indice actuel du vecteur de X
		
		nbAppelsEssaisSuccessifs++;
		
		int maxXi=((int) (monnaieARendreES/piecesUtilisables.get(i)))+2;//le nombre maximal de piece de cette valeur pour atteindre la monnaie
		
		for(int xi=0 ; xi<maxXi ; xi++) {//nb de pieces numéro i utilisees
			
			//satisfaisant ?
			if( somcour+piecesUtilisables.get(i)*xi<monnaieARendreES | egalDouble(somcour+piecesUtilisables.get(i)*xi,monnaieARendreES) ) {//somcour restera inferieur ou egal a monnaieARendre
				
				//enregistrer
				somcour+=piecesUtilisables.get(i)*xi;
				X.set(i, xi);
				nbPieces+=xi;	
				
				//solution trouvee ?
				if(egalDouble(somcour,monnaieARendreES)) {
					
					//meilleure ?
					if(nbPieces<bestNbPieces) {
						//mise a jour de la nouvelle meilleure solution
						Collections.copy(bestX, X);
						bestNbPieces=nbPieces;							
					}
					
				}else {
					
					//elagage

					//verifier que le nb de pieces peut etre ameliore
					boolean condElagage=true;
					if(nbPieces+1>=bestNbPieces) {
						condElagage=false;
					}
					
					boolean condElagage2=false;
					//verifier si toute les valeurs de pieces restantes ne sont pas trop elevees
					//sommme + le plus petit nombre restant a prendre doit etre <= monnaieARendre 
					//tres utile si les valeurs de la liste piecesUtilisables sont dans l'ordre croissant
					for(int j=i+1;j<n;j++) {
						if((monnaieARendreES-somcour)>=piecesUtilisables.get(j) | egalDouble((monnaieARendreES-somcour),piecesUtilisables.get(j))) { // >=
							condElagage2=true;
						}
					}
					
					//encore possible ?
					if(i<n-1 & condElagage & condElagage2 ) {
						solutionEssaisSuccessifs(i+1);//prochaine valeur de piece
					}
					
				}
				
				//defaire
				somcour-=piecesUtilisables.get(i)*xi;
				nbPieces-=xi;
				X.set(i, 0);
				
			}
		}
	}
		
	//initialise la liste avec des valeurs egal a 0
	private static List<Integer> initVecteur(List<Integer> X, int n) {
		X.clear();
		for(int i=0 ; i<n ; i++) {
			X.add(0);
		}
		return X;
	}

	//affiche la solution sous la forme de vecteur
	private static void afficherSolutionEssaisSuccessifs(List<Integer> bestX) {
		for(int i=0; i<bestX.size() ; i++) {
			System.out.println(piecesUtilisables.get(i)+"€ : "+bestX.get(i));
		}
	}
	
	//affiche S pour l'algo essais successifs
	private static void afficherSES(List<Integer> bestX) {
		List<Double> listDouble = new ArrayList<Double>();
		
		for(int i=0; i<bestX.size() ; i++) {
			for(int j=0 ; j < bestX.get(i) ; j++) {
				listDouble.add(piecesUtilisables.get(i));
			}
		}
		
		Collections.sort(listDouble);
		
		System.out.print("S = { ");
		for(int i=0; i<listDouble.size() ; i++) {
			System.out.print(listDouble.get(i)+"€ ");
		}
		System.out.print("}");
	}

	//comparaison de double
	private static boolean egalDouble(double a, double b) {
		double margeErreur=0.00001;
		double difference = Math.abs(a-b);
		if(difference<margeErreur) {
			return true;
		}else {
			return false;
		}
	}
	/*** FIN ESSAIS SUCCESSIFS ***/
	
	
	/*** PROGRAMMATION DYNAMIQUE */

	private static int[][] remplirDynamiquement(int i, int j) {
		int numPiece, valeurTotal;
		int[][] tab = new int[i][j];
		for( valeurTotal = 1 ; valeurTotal <= j ; valeurTotal++ ) {	// On parcourt les valeurs possibles de 1 à la valeur maximale (j)
			for( numPiece = 0 ; numPiece < i ; numPiece++ ) {		// On parcourt les pièces disponibles par inidice

				if(vide(numPiece-1, valeurTotal, tab)) {			// Case au dessus non remplie
					if(vide(numPiece, valeurTotal-piecesDynamiques.get(numPiece), tab)) { // Case du cas "on prend" non renseignée
						if( valeurTotal-piecesDynamiques.get(numPiece)==0) { // Si la pièce correspond pile à la somme attendue
							tab[numPiece][valeurTotal-1] = 1;
						}
						else {
							tab[numPiece][valeurTotal-1] = -1;
						}
					}
					else {	// On peut prendre la pièce
						tab[numPiece][valeurTotal-1] =  1 + tab[numPiece][valeurTotal-piecesDynamiques.get(numPiece)-1]; // Tab[numPiece, valeurTotal] = 1 + Tab[numPiece, valeurTotal-C[numPiece]] # Il existe une solution si on prend
					}
				}
				else {	// Case au dessus remplie	
					if(vide(numPiece, valeurTotal-piecesDynamiques.get(numPiece), tab)) { // Case du cas "on prend" non renseignée
						tab[numPiece][valeurTotal-1]= tab[numPiece-1][valeurTotal-1]; // On assigne la valeur au dessus
					}
					else if( valeurTotal-piecesDynamiques.get(numPiece)==0) { // Si la pièce correspond pile à la somme attendue
						tab[numPiece][valeurTotal-1] = 1;
					}
					else {	// On peut prendre la pièce, et il existe une solution avec un sous-ensemble de pièces
						tab[numPiece][valeurTotal-1] = Math.min(tab[numPiece-1][valeurTotal-1], 1 + tab[numPiece][valeurTotal-piecesDynamiques.get(numPiece)-1]); // Tab[numPiece, valeurTotal] = Min(Tab[numPiece-1, valeurTotal],1 + Tab[numPiece, valeurTotal-C[numPiece]]) # Il existe une solution : minimum entre les 2 cases
					}
				}
			}
		}
		return tab;
	}
	
	
	private static List<Integer> NBP(int i, int j, int[][] tab) {
		List<Integer> pieces = new ArrayList<Integer>();
		int num = i-1, val = j-1;
		while(val>0 && tab[num][val]!=-1) {
			while(num>0 && tab[num][val]==tab[num-1][val]) {
				num-=1;
			}
			pieces.add(piecesDynamiques.get(num));
			val=val-piecesDynamiques.get(num);
		}
		return pieces;
	}

	
	private static boolean vide(int i, int j, int[][] tab) {
		if(i<0 || j<1) {
			return true;
		}
		else if(tab[i][j-1]==-1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//affiche S pour l'algo prog dynamique
	private static void afficherSPD(List<Integer> liste) {
		System.out.print("S = { ");
		for(int i=0; i<liste.size() ; i++) {
			if(liste.get(i)<10) {
				System.out.print(liste.get(i)/100+".0"+liste.get(i)%100+"€ ");
			}else {
				System.out.print(liste.get(i)/100+"."+liste.get(i)%100+"€ ");
			}
			
		}
		System.out.print("}");
	}

	/*** FIN PROGRAMMATION DYNAMIQUE ***/
	
	/*** ALGORITHME GLOUTON ***/
	private static List<Double> combinaisonGlouton(List<Double> pieces, double objectif){
		boolean impossible = false;
		List<Double> solution = new ArrayList<Double>();
		
		while(objectif>0.0 && !impossible) {
			//System.out.println(objectif);
			double toAdd = findMaxValue(pieces, objectif);
			//System.out.println(toAdd.getValeur());
			if(toAdd != 0){
				solution.add(toAdd);
				objectif -= toAdd;
				objectif = Math.round(objectif*100.0)/100.0;
				//System.out.println(objectif);
			}
			else {
				impossible = true;
			}
		}
		return solution;
	}
	
	private static Double findMaxValue(List<Double> pieces, double objectif) {
		double maxVal = 0; // Une valeur monetaire est forcement positive
		double plusGrossePossible = 0;
		for(double piece : pieces) {
			double checkVal = piece;
			if(maxVal<checkVal && checkVal<=objectif) {
				plusGrossePossible = piece;
				maxVal = checkVal;
			}
		}
		return plusGrossePossible;
	}
	
	//affiche S pour l'algo glouton
	private static void afficherSG(List<Double> liste) {
		Collections.sort(liste);
		System.out.print("S = { ");
		for(int i=0 ; i<liste.size() ; i++) {
			System.out.print(liste.get(i)+"€ ");
		}
		System.out.print("}");
	}
	/*** FIN ALGORITHME GLOUTON ***/
	
}
