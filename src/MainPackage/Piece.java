package MainPackage;

import java.util.List;

public class Piece {
	private double valeur;//en euros

	public Piece(double valeur) {
		super();
		this.valeur = valeur;
	}

	@Override
	public String toString() {
		return "Piece [valeur=" + valeur + "]";
	}

	public double getValeur() {
		return valeur;
	}

	public void setValeur(double valeur) {
		this.valeur = valeur;
	}
	
	
	
}
