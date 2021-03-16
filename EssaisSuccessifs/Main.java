package EssaisSuccessifs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		
		List<Piece> solution = new LinkedList<Piece>();
		solution=solutionEssaisSuccessifs(10);
		//System.out.println(solution);
	}
	
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
	
	private static List<Piece> solutionEssaisSuccessifs(int monnaieARendre) {
		List<Piece> solution = null; 
		int xi;
		List<ArrayList<Piece>> solutionsPossibles = calculerSi();//ensemble des solutions
		List<Piece> S = new ArrayList<Piece>();
		boolean satisfaisant = true;
		int somme=0;
		for(ArrayList<Piece> Si : solutionsPossibles) {
			for (Piece piece : Si) {
				somme+=piece.valeur;
				if(somme>monnaieARendre) {
					satisfaisant = false;
				}
			}
			if(satisfaisant) {
				if(somme==monnaieARendre) {
					boolean nouvelleMeilleureSolution=false;
					if(solution.size()<S.size()) {
						nouvelleMeilleureSolution=true;
					}
					if(nouvelleMeilleureSolution) {
						S=solution;
					}
				}
			}
		}
		
	
		return solution;
	}

	private static List<ArrayList<Piece>> calculerSi() {
		List<Piece> piecesUtilisables = new ArrayList<Piece>();
		Piece p1 = new Piece(0.1);
		Piece p2 = new Piece(0.2);
		Piece p3 = new Piece(0.5);
		Piece p4 = new Piece(1);
		Piece p5 = new Piece(2);
		Piece p6 = new Piece(5);
		Piece p7 = new Piece(10);
		
		piecesUtilisables.add(0, p1);
		
		return null;
	}
}
