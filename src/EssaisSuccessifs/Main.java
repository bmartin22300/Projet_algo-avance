package EssaisSuccessifs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
	
	
	
	
private final static double OBJECTIF = 1.9;
	
	public static void main(String[] args) {
		
		//pieces utilisables
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
		
		
		/*//essais successifs
		 * 
		 * System.out.println(solutionEssaisSuccessifs3(1.8,piecesUtilisables));
		List<Piece> solution = new LinkedList<Piece>();
		//solution=solutionEssaisSuccessifs(10,piecesUtilisables);
		//System.out.println(solution);
		
		List<ArrayList<Piece>> s = calculerSi(piecesUtilisables);
		System.out.println();
		System.out.println("---END---");
		System.out.println();
		for(ArrayList<Piece> s2 : s) {
			System.out.println(s2);
		}*/
		
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
	
	
	//satisfaisant, enregistrer, soltrouvee, defaire
	
	/*
	 * v
	var ent xi;
	début
		calculer Si;
		pour xi parcourant Si 
		faire
			si satisfaisant(xi) alors enregistrer (xi) ;
				si soltrouvée alors
					si meilleure alors Y ← X ; 
						majvalopt 
					fsi
				sinon
					si encorepossible alors 
						solopt(i + 1) 
					fsi
				fsi ;
				défaire(xi)
			fsi
		fait
	fin ;
	 */
	
	//attention à garder les pieces utilisables dans l'ordre croissant
	private static List<Piece> solutionEssaisSuccessifs3(double monnaieARendre, List<Piece> piecesUtilisables) {
		List<Piece> x = new ArrayList<Piece>(); //la solution en cours
		List<Piece> bestSolution = new ArrayList<Piece>(); //la meilleure solution
		double somme=0.0;
		
		//Si=piecesUtilisables
			
		for(Piece xi : piecesUtilisables) { // pour chaque pièces de la solution
			
			//utilisation multiple de la meme piece
			boolean keepPiece=true;
			boolean satisfaisant = true;
			while(keepPiece) {
				
				//calcul de satisfaisant : SUM(xi) < monnaie
				//est-ce que nous ne rendons pas trop d'argent ?
				somme+=xi.getValeur();
				somme = Math.round(somme*10.0)/10.0; //approximation
				if(somme>monnaieARendre) {
					satisfaisant = false;
				}
				
				if(satisfaisant) {
					
					//calcul de enregistrer 
					x.add(xi);
					
					//calcul de soltrouve
					if(somme==monnaieARendre) {//somme correspond 
						keepPiece=false;
						//calcul de meilleur
						boolean nouvelleMeilleureSolution=false;
						if(x.size()<bestSolution.size()) {//est-ce que le nombre de pièces est inférieur à celui de l'actuelle meilleure solution
							nouvelleMeilleureSolution=true;
						}
						
						if(nouvelleMeilleureSolution) {
							bestSolution=x;
						}
						
					}else {										
						//si encorepossible alors //condition d'elagage ex : (p29)
						if( (somme + xi.getValeur()) <= monnaieARendre) {
							//on re essaie d'ajouter la meme piece --> creation d'une nouvelle liste 
							keepPiece=true;
						}else {
							keepPiece=false;
						}
					}
					//calcul de défaire 
					//rien car on peut utiliser autant de fois une piece que l'on souhaite
				}else {
					somme-=xi.getValeur();
					keepPiece=false;
				}
			}
		}
		
		return x;
		//return bestSolution;
	}
	
	private static List<Piece> solutionEssaisSuccessifs2(int monnaieARendre, List<Piece> piecesUtilisables) {
		List<Piece> x = new ArrayList<Piece>(); //la solution en cours
		List<Piece> bestSolution = new ArrayList<Piece>(); //la meilleure solution
		//List<ArrayList<Piece>> solutionsCandidates = calculerSi(piecesUtilisables);//ensemble des solutions candidates
		List<ArrayList<Piece>> solutionsCandidates = null;//ensemble infini ?!
		boolean satisfaisant = true;
		int somme=0;
		for(ArrayList<Piece> Si : solutionsCandidates) { //pour chaque solutions candidates
			
			for(Piece xi : Si) { // pour chaque pièces de la solution
				
				//calcul de satisfaisant : SUM(xi) < monnaie
				//est-ce que nous ne rendons pas trop d'argent ?
				somme+=xi.getValeur();
				if(somme>monnaieARendre) {
					satisfaisant = false;
				}
				
				if(satisfaisant) {
					//calcul de enregistrer 
					x.add(xi);
					
					//calcul de soltrouve
					if(somme==monnaieARendre) {//somme correspond 
						
						//calcul de meilleur
						boolean nouvelleMeilleureSolution=false;
						if(x.size()<bestSolution.size()) {//est-ce que le nombre de pièces est inférieur à celui de l'actuelle meilleure solution
							nouvelleMeilleureSolution=true;
						}
						
						if(nouvelleMeilleureSolution) {
							bestSolution=x;
						}
						
					}else {						
						//calcul du max
						double max=0;
						for(Piece piece : Si) {
							if(piece.getValeur()>max) {
								max=piece.getValeur();
							}
						}
						
						//si encorepossible alors //condition d'elagage ex : (p29) la somme + le plus grand nombre de la solution candidate < monnaie à rendre
						if( (somme + max) < monnaieARendre) {
							//solopt(i + 1) 
						}
					}
					
					//calcul de défaire 
					//...
				}
				
			}
			
			//reset
			somme=0;
			x=new ArrayList<Piece>();
		}
	
		return bestSolution;
	}
	
	
	
	
	private static List<Piece> solutionEssaisSuccessifs(int monnaieARendre, List<Piece> piecesUtilisables) {
		List<Piece> solution = null; //la meilleure solution
		int xi;
		//List<ArrayList<Piece>> solutionsCandidates = calculerSi(piecesUtilisables);//ensemble des solutions candidates
		List<ArrayList<Piece>> solutionsCandidates = null;//ensemble infini ?!
		boolean satisfaisant = true;
		int somme=0;
		for(ArrayList<Piece> Si : solutionsCandidates) {
			
			//calcul de satisfaisant : SUM(xi) < monnaie
			//est-ce que nous ne rendons pas trop d'argent ?
			for (Piece piece : Si) {
				somme+=piece.getValeur();
				if(somme>monnaieARendre) {
					satisfaisant = false;
				}
			}
			
			if(satisfaisant) {
				//calcul de enregistrer
				//...
				
				//calcul de soltrouve
				if(somme==monnaieARendre) {//somme correspond 
					
					//calcul de meilleur
					boolean nouvelleMeilleureSolution=false;
					if(solution.size()<Si.size()) {//est-ce que le nombre de pièces est inférieur à celui de l'actuelle meilleure solution
						nouvelleMeilleureSolution=true;
					}
					if(nouvelleMeilleureSolution) {
						solution=Si;
					}
				}else {
					//si encorepossible alors //condition d'elagage ex : (p29) la somme + le plus grand nombre de la solution candidate < monnaie à rendre
						//solopt(i + 1) 
					//fsi
				}
				
				//calcul de défaire 
				//...
			}
		}
	
		return solution;
	}

	//calcule l'ensemble des solutions candidates (une solution est un ensemble de pièces)
	private static List<ArrayList<Piece>> calculerSi(List<Piece> piecesUtilisables) {
		List<Piece> solutionEnCours = new ArrayList<Piece>();
		List<ArrayList<Piece>> solutionsCandidates = new ArrayList<ArrayList<Piece>>();
		
		//calcul de combinaison de k parmi n, k varie de 1 à n
		for(int k=1; k<=piecesUtilisables.size(); k++) {//k varie de 1 à n
			//calcul de combinaison de k parmi n
			for(Piece pieceEnCours : piecesUtilisables) {//pour chaque pieces
				solutionEnCours.add(pieceEnCours);
				for(Piece piece : piecesUtilisables) {//on essaie d'ajouter une nouvelle piece
					if(solutionEnCours.size()<k) {
						System.out.println(isDoublon(solutionsCandidates,solutionEnCours));//on élimine les doublons
						solutionEnCours.add(piece);
						//si la solution en cours n'est pas vide, on l'ajoute
						if(!solutionEnCours.isEmpty()) {
							solutionsCandidates.add((ArrayList<Piece>) solutionEnCours);
							solutionEnCours=new ArrayList<Piece>();
							if(piece!=piecesUtilisables.get(piecesUtilisables.size()-1)) {//si ce n'est pas la derniere pieces 
								solutionEnCours.add(pieceEnCours);//on sauvegarde la piece precedente dans la solution en cours
							}
							
						}
					}
					
				}
				//si la solution en cours n'est pas vide, on l'ajoute
				if(!solutionEnCours.isEmpty()) {
					solutionsCandidates.add((ArrayList<Piece>) solutionEnCours);
					solutionEnCours=new ArrayList<Piece>();
				}
			}
			
			System.out.println("Listes d'éléments à "+k+" éléments :");
			System.out.println(solutionsCandidates);
		}
		
		return solutionsCandidates;
	}

	private static boolean isDoublon(List<ArrayList<Piece>> solutionsCandidates, List<Piece> solutionATeste) {
		List<Piece> solutionATesteSave=new ArrayList<Piece>(solutionATeste);
		
		//attention a la taille
		for(ArrayList<Piece> solutionEnCours : solutionsCandidates) {//pour chaques solution
			boolean doublon=true;
			
			for(Piece p : solutionEnCours) {
				if(!solutionATesteSave.contains(p)) {
					doublon=false;
				}else {//on retire l'element de la solution a teste
					solutionATesteSave.remove(p);
				}
			}
			
			if(doublon) {//la solution en cours contient les memes elements que la solution a teste
				return true;
			}else {
				solutionATesteSave=new ArrayList<Piece>(solutionATeste);
			}
			
		}
		
		return false;//aucun doublon trouve
		
	}
	
}
