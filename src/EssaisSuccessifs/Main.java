package EssaisSuccessifs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {
	private final static double OBJECTIF = 1.9;
	
	//variables necessaires pour essais successifs///////////////////////////////////////////////////////////
	static final double monnaieARendre=0.1;//possibilite de modifier
	
	static List<Double> piecesUtilisables = new ArrayList<Double>();//vecteur des valeurs de pieces utilisables
	static int n;//taille du vecteur
	
	static List<Integer> X = new ArrayList<Integer>();//vecteur du nb de pieces utilise pour chaque valeur de piece
	static int nbPieces=0;//somme du vecteur X
	static double somcour=0.0;//somme courantes des valeurs des pieces de X
	
	static List<Integer> bestX = new ArrayList<Integer>();//le meilleur vecteur X (utilisant le moins de pieces)
	static int bestNbPieces=(int) (monnaieARendre*100)+1;//on initialise au "pire cas" correspondant a (monnaieARendre*100) pieces de 0.01 
	
	static int nbAppelsEssaisSuccessifs=0;
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args) {
		
		//essais successifs/////////////////////////////////////////////////
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
		solutionEssaisSuccessifs(0);
		
		//affichage de la solution
		System.out.println("Meilleure solution essais successifs (en "+nbAppelsEssaisSuccessifs+" appels) pour rendre "+monnaieARendre+"€ :");
		afficherSolutionEssaisSuccessifs(bestX);
		System.out.println();
		////////////////////////////////////////////////////////////////////
		
		List<Piece> piecesUtilisables = new ArrayList<Piece>();
		
		piecesUtilisables.add(new Piece(2));
		piecesUtilisables.add(new Piece(1));
		piecesUtilisables.add(new Piece(0.5));
		piecesUtilisables.add(new Piece(0.2));
		piecesUtilisables.add(new Piece(0.1));
		
		// Autre jeu de pieces
		List<Piece> autresPiecesUtilisables = new ArrayList<Piece>();
		
		autresPiecesUtilisables.add(new Piece(6));
		autresPiecesUtilisables.add(new Piece(4));
		autresPiecesUtilisables.add(new Piece(1));
		
		/*** ALGORITHME GLOUTON ***/
		//Il faut prouver ou infirmer qu’un algorithme glouton est exact pour chaque probleme traite.
			// => Si non exact, contre exemple,
			// => Sinon Etablir par recurrence que la solution en construction est constamment optimale
			// => Ou partir de la solution gloutonne puis montrer que la transformation par echange de deux choix ne peut l'ameliorer
		
		// Principe pour les pieces de monnaie : 
		
		List<Piece> algoGlouton = combinaisonGlouton(piecesUtilisables, OBJECTIF);
		System.out.println("Objectif : "+OBJECTIF+" :\n"+combinaisonGlouton(piecesUtilisables, OBJECTIF));
		
		System.out.println("Objectif : "+8.0+" :\n"+combinaisonGlouton(autresPiecesUtilisables, 8.0));
		
		System.out.println("Objectif : "+29.0+" :\n"+combinaisonGlouton(autresPiecesUtilisables, 29.0));
		/*** FIN ALGORITHME GLOUTON ***/
		
	}
	
	//solutionEssaisSuccessifs///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	static void solutionEssaisSuccessifs(int i) {//i représente l'indice actuel du vecteur de X
		
		nbAppelsEssaisSuccessifs++;
		
		int maxXi=((int) (monnaieARendre/piecesUtilisables.get(i)))+2;//le nombre maximal de piece de cette valeur pour atteindre la monnaie
		
		for(int xi=0 ; xi<maxXi ; xi++) {//nb de pieces numéro i utilisees
			
			//satisfaisant ?
			if( somcour+piecesUtilisables.get(i)*xi<monnaieARendre | egalDouble(somcour+piecesUtilisables.get(i)*xi,monnaieARendre) ) {//somcour restera inferieur ou egal a monnaieARendre
				
				//enregistrer
				somcour+=piecesUtilisables.get(i)*xi;
				X.set(i, xi);
				nbPieces+=xi;	
				
				//solution trouvee ?
				if(egalDouble(somcour,monnaieARendre)) {
					
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
						if((monnaieARendre-somcour)>=piecesUtilisables.get(j) | egalDouble((monnaieARendre-somcour),piecesUtilisables.get(j))) { // >=
							condElagage2=true;
						}
					}
					
					//encore possible ?
					//if(i<n-1 & condElagage & condElagage2 ) {
					if(i<n-1) {
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
	//fin essais successifs////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*** ALGORITHME GLOUTON ***/
	private static List<Piece> combinaisonGlouton(List<Piece> pieces, double objectif){
		boolean impossible = false;
		List<Piece> solution = new ArrayList<Piece>();
		
		while(objectif>0 && !impossible) {
			//System.out.println(objectif);
			Piece toAdd = findMaxValue(pieces, objectif);
			//System.out.println(toAdd.getValeur());
			if(toAdd != null){
				solution.add(toAdd);
				objectif -= toAdd.getValeur();
				objectif = Math.round(objectif*10.0)/10.0;
				//System.out.println(objectif);
			}
			else {
				impossible = true;
			}
		}
		return solution;
	}
	
	private static Piece findMaxValue(List<Piece> pieces, double objectif) {
		double maxVal = 0; // Une valeur monetaire est forcement positive
		Piece plusGrossePossible = null;
		for(Piece piece : pieces) {
			double checkVal = piece.getValeur();
			if(maxVal<checkVal && checkVal<=objectif) {
				plusGrossePossible = piece;
				maxVal = checkVal;
			}
		}
		return plusGrossePossible;
	}
	/*** FIN ALGORITHME GLOUTON ***/
	
}
