package MainPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Main {
	
	
	
	static List<Integer> piecesUtilisables = new ArrayList<Integer>();//vecteur des valeurs de pieces utilisables
	static int n;//taille du vecteur
	
	/*** variables utilisees pour ESSAIS SUCCESSIFS **************************************************/
	//en euros
	static final int monnaieARendreES=511;
	//static int int monnaieARendreES=633;
	//static int int monnaieARendreES=1515;
	
	static List<Integer> X = new ArrayList<Integer>();//vecteur du nb de pieces utilise pour chaque valeur de piece
	static int nbPieces=0;//somme du vecteur X
	static int somcour=0;//somme courantes des valeurs des pieces de X
	
	static List<Integer> bestX = new ArrayList<Integer>();//le meilleur vecteur X (utilisant le moins de pieces)
	static int bestNbPieces=(int) (monnaieARendreES*100)+1;//on initialise au "pire cas" correspondant a (monnaieARendre*100) pieces de 0.01 
	
	static int nbAppelsEssaisSuccessifs=0;
	/*********************************************************************************************************/
	
	/*** variables utilisées pour PROGRAMMATION DYNAMIQUE **************************************************/
	//en centimes
	static int monnaieARendrePD = 511;
	//static int monnaieARendrePD = 633;
	//static int monnaieARendrePD = 1515;
	
	private final static int OBJECTIF_DYNAMIQUE = monnaieARendrePD; // Nombre de centimes en entier
	/*********************************************************************************************************/
	
	/*** variables utilisees pour GLOUTON **************************************************/
	//en euros
	static final int monnaieARendreG=511;
	//static final int monnaieARendreG=633;
	//static final int monnaieARendreG=1515;
	
	/*********************************************************************************************************/
	
	
	public static void main(String[] args) {
		
		Chrono chrono = new Chrono();
		long duree;
		
		/*** ESSAIS SUCCESSIFS ***/
		System.out.println("ESSAIS SUCCESSIFS");
		//initialiser les pieces utilisables
		piecesUtilisables.add(200);
		piecesUtilisables.add(100);
		piecesUtilisables.add(50);
		piecesUtilisables.add(20);
		piecesUtilisables.add(10);
		piecesUtilisables.add(5);
		piecesUtilisables.add(2);
		piecesUtilisables.add(1);
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
		System.out.println("Meilleure solution essais successifs (en "+nbAppelsEssaisSuccessifs+" appels) (en "+duree+"ms) pour rendre "+monnaieARendreES+" centimes :");
		afficherSES(bestX);
		System.out.println();
		System.out.println();
		/*** FIN ESSAIS SUCCESSIFS ***/
		
		/*** PROGRAMMATION DYNAMIQUE ***/
		System.out.println("PROGRAMMATION DYNAMIQUE");
		
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
		int[][] tab = remplirDynamiquement(n, j);
		List<Integer> listePD = pieces(piecesUtilisables.size(), monnaieARendrePD, tab);
		chrono.stop(); // arrêt
		duree=chrono.getDureeMs(); // affichage du résultat en millisecondes
		
		System.out.println("Meilleure solution programmation dynamique (en "+duree+"ms) pour rendre "+monnaieARendrePD+ " centimes :");
		
		afficherS(listePD);
		System.out.println();
		System.out.println();
		
		/*** FIN PROGRAMMATION DYNAMIQUE ***/
		
		/*** ALGORITHME GLOUTON ***/
		
		// Autre jeu de pieces
		/*
		List<Piece> autresPiecesUtilisables = new ArrayList<Piece>();
		
		autresPiecesUtilisables.add(new Piece(6));
		autresPiecesUtilisables.add(new Piece(4));
		autresPiecesUtilisables.add(new Piece(1));
		*/
		
		System.out.println("ALGORITHME GLOUTON");
		//Il faut prouver ou infirmer qu� centimes�un algorithme glouton est exact pour chaque probleme traite.
			// => Si non exact, contre exemple,
			// => Sinon Etablir par recurrence que la solution en construction est constamment optimale
			// => Ou partir de la solution gloutonne puis montrer que la transformation par echange de deux choix ne peut l'ameliorer
		
		// Principe pour les pieces de monnaie : 
		
		chrono.start(); 
		solutionEssaisSuccessifs(0);
		chrono.stop(); // arrêt
		duree=chrono.getDureeMs(); // affichage du résultat en millisecondes
		
		//affichage de la solution
		List<Integer> listeG=combinaisonGlouton(piecesUtilisables, monnaieARendreG);
		System.out.println("Meilleure solution glouton (en "+duree+"ms) pour rendre "+monnaieARendreG+" centimes :");
		afficherS(listeG);
		/*** FIN ALGORITHME GLOUTON ***/
	}
	
	/*** ESSAIS SUCCESSIFS ***/
	static void solutionEssaisSuccessifs(int i) {//i représente l'indice actuel du vecteur de X
		
		nbAppelsEssaisSuccessifs++;
		
		int maxXi=((int) (monnaieARendreES/piecesUtilisables.get(i)))+2;//le nombre maximal de piece de cette valeur pour atteindre la monnaie
		
		for(int xi=0 ; xi<maxXi ; xi++) {//nb de pieces numéro i utilisees
			
			//satisfaisant ?
			if( somcour+piecesUtilisables.get(i)*xi<monnaieARendreES | somcour+piecesUtilisables.get(i)*xi==monnaieARendreES) {//somcour restera inferieur ou egal a monnaieARendre
				
				//enregistrer
				somcour+=piecesUtilisables.get(i)*xi;
				X.set(i, xi);
				nbPieces+=xi;	
				
				//solution trouvee ?
				if(somcour==monnaieARendreES) {
					
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
						if((monnaieARendreES-somcour)>=piecesUtilisables.get(j) | monnaieARendreES+somcour==piecesUtilisables.get(j)) { // >=
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
			System.out.println(piecesUtilisables.get(i)+" centimes : "+bestX.get(i));
		}
	}
	
	//affiche S pour l'algo essais successifs
	private static void afficherSES(List<Integer> bestX) {
		List<Integer> listint = new ArrayList<Integer>();
		
		for(int i=0; i<bestX.size() ; i++) {
			for(int j=0 ; j < bestX.get(i) ; j++) {
				listint.add(piecesUtilisables.get(i));
			}
		}
		
		Collections.sort(listint);
		
		System.out.print("S = { ");
		for(int i=0; i<listint.size() ; i++) {
			System.out.print(listint.get(i)+" centimes ");
		}
		System.out.print("}");
	}

	/*//comparaison de int
	private static boolean egalint(int a, int b) {
		double margeErreur=0.00001;
		int difference = Math.abs(a-b);
		if(difference<margeErreur) {
			return true;
		}else {
			return false;
		}
	}*/
	
	/*** FIN ESSAIS SUCCESSIFS ***/
	
	
	/*** PROGRAMMATION DYNAMIQUE */

	private static int[][] remplirDynamiquement(int i, int j) {
		int numPiece, valeurTotal;
		int[][] tab = new int[i][j];
		for( valeurTotal = 1 ; valeurTotal <= j ; valeurTotal++ ) {	// On parcourt les valeurs possibles de 1 à la valeur maximale (j)
			for( numPiece = 0 ; numPiece < i ; numPiece++ ) {		// On parcourt les pièces disponibles par inidice

				if(vide(numPiece-1, valeurTotal, tab)) {			// Case au dessus non remplie
					if(vide(numPiece, valeurTotal-piecesUtilisables.get(numPiece), tab)) { // Case du cas "on prend" non renseignée
						if( valeurTotal-piecesUtilisables.get(numPiece)==0) { // Si la pièce correspond pile à la somme attendue
							tab[numPiece][valeurTotal-1] = 1;
						}
						else {
							tab[numPiece][valeurTotal-1] = -1;
						}
					}
					else {	// On peut prendre la pièce
						tab[numPiece][valeurTotal-1] =  1 + tab[numPiece][valeurTotal-piecesUtilisables.get(numPiece)-1]; // Tab[numPiece, valeurTotal] = 1 + Tab[numPiece, valeurTotal-C[numPiece]] # Il existe une solution si on prend
					}
				}
				else {	// Case au dessus remplie	
					if(vide(numPiece, valeurTotal-piecesUtilisables.get(numPiece), tab)) { // Case du cas "on prend" non renseignée
						tab[numPiece][valeurTotal-1]= tab[numPiece-1][valeurTotal-1]; // On assigne la valeur au dessus
					}
					else if( valeurTotal-piecesUtilisables.get(numPiece)==0) { // Si la pièce correspond pile à la somme attendue
						tab[numPiece][valeurTotal-1] = 1;
					}
					else {	// On peut prendre la pièce, et il existe une solution avec un sous-ensemble de pièces
						tab[numPiece][valeurTotal-1] = Math.min(tab[numPiece-1][valeurTotal-1], 1 + tab[numPiece][valeurTotal-piecesUtilisables.get(numPiece)-1]); // Tab[numPiece, valeurTotal] = Min(Tab[numPiece-1, valeurTotal],1 + Tab[numPiece, valeurTotal-C[numPiece]]) # Il existe une solution : minimum entre les 2 cases
					}
				}
			}
		}
		return tab;
	}
	
	// i : taille de l'ensemble de pi�ces utilisables,
	// j : valeur � rendre
	// tab : tableau retournant NBP(i,j) pour i et j donn�s
	private static List<Integer> pieces(int i, int j, int[][] tab) {
		List<Integer> pieces = new ArrayList<Integer>();
		int num = i-1, val = j;
		while(val>0 && tab[num][val-1]!=-1) {
			while(num>0 && tab[num][val-1]==tab[num-1][val-1]) {
				num-=1;
			}
			pieces.add(piecesUtilisables.get(num));
			val=val-piecesUtilisables.get(num);
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
	/*** FIN PROGRAMMATION DYNAMIQUE ***/
	
	/*** ALGORITHME GLOUTON ***/
	private static List<Integer> combinaisonGlouton(List<Integer> pieces, int objectif){
		boolean impossible = false;
		List<Integer> solution = new ArrayList<Integer>();
		
		while(objectif>0 && !impossible) {
			//System.out.println(objectif);
			int toAdd = findMaxValue(pieces, objectif);
			//System.out.println(toAdd.getValeur());
			if(toAdd != 0){
				solution.add(toAdd);
				objectif -= toAdd;
				//System.out.println(objectif);
			}
			else {
				impossible = true;
			}
		}
		return solution;
	}
	
	private static int findMaxValue(List<Integer> pieces, int objectif) {
		int i = 0;
		while(pieces.get(i)>objectif){
			i++;
		}
		return pieces.get(i);
	}
	/*** FIN ALGORITHME GLOUTON ***/
	
	//affiche S
	private static void afficherS(List<Integer> liste) {
		Collections.sort(liste);
		System.out.print("S = { ");
		for(int i=0 ; i<liste.size() ; i++) {
			System.out.print(liste.get(i)+" centimes ");
		}
		System.out.print("}");
	}
	
}
