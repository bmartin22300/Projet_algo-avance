package EssaisSuccessifs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EssaisSuccessifs {
	
	/*
	 * v
	var ent xi;
	début
		calculer Si des valeurs possibles de xi; 				//Si peut valoir 2 1 0.5 0.2 0.1
		pour xi parcourant Si 									//xi de 1 a +inf
		faire
			si satisfaisant(xi) alors enregistrer (xi) ; 		//somme += xi x Si[i] <= monnaieARendre ; X[i]=xi, somme+= xi x Si[i]
				si soltrouvée alors								//somme==monnaieARendre
					si meilleure alors Y ← X ; 					//nbPieces<nbPiecesBest
						majvalopt 
					fsi
				sinon
					si encorepossible alors 					// i<5 //Elagage : sommme + le plus petit nombre restant a prendre doit etre <= monnaieARendre 
						solopt(i + 1) 							//i représente la piece suivante dans la map
					fsi
				fsi ;
				défaire(xi)										//somme-= xi x Si[i] //liberer var globales ; clear X, somme=0
			fsi
		fait
	fin ;
	 */
	
	static final double monnaieARendre=8.9;//possibilite de modifier
	
	static List<Double> piecesUtilisables = new ArrayList<Double>();//vecteur des valeurs de pieces utilisables
	static int n;//taille du vecteur
	
	static List<Integer> X = new ArrayList<Integer>();//vecteur du nb de pieces utilise pour chaque valeur de piece
	static int nbPieces=0;//somme du vecteur X
	static double somcour=0.0;//somme courantes des valeurs des pieces de X
	
	static List<Integer> bestX = new ArrayList<Integer>();//le meilleur vecteur X (utilisant le moins de pieces)
	static int bestNbPieces=(int) (monnaieARendre*10)+1;//on initialise au "pire cas" correspondant a (monnaieARendre*10) pieces de 0.1 
	
	public static void main(String[] args) {
		//initialiser les pieces utilisables
		piecesUtilisables.add(1.0);
		piecesUtilisables.add(0.2);
		piecesUtilisables.add(0.1);
		piecesUtilisables.add(2.0);
		piecesUtilisables.add(0.5);
		n=piecesUtilisables.size();

		//initialisation des vecteurs X et bestX
		X=initVecteur(X);
		bestX=initVecteur(bestX);
		
		//appel a la fonction recursive
		solutionEssaisSuccessifs(0);
		
		//affichage de la solution
		System.out.println("Meilleure solution essais successifs pour rendre "+monnaieARendre+"€ :");
		afficherSolutionEssaisSuccessifs(bestX);
	}
	
	//solutionEssaisSuccessifs
	static void solutionEssaisSuccessifs(int i) {//i représente l'indice actuel du vecteur de X
		
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
						if((monnaieARendre-somcour)>piecesUtilisables.get(j) | egalDouble((monnaieARendre-somcour),piecesUtilisables.get(j))) { // >=
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
	private static List<Integer> initVecteur(List<Integer> X) {
		X.clear();
		X.add(0);
		X.add(0);
		X.add(0);
		X.add(0);
		X.add(0);
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
}
