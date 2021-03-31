package EssaisSuccessifs;

public class combinaison {

	//renvoi toutes les combinaisons d'une chaine de caractère
	static char[][] Combinaisons(String mot){
		int longueur=mot.length();
		int nbr=(int)Math.pow(2,longueur);//nbr=nombre de combinaisons
		char[][] comb=new char[nbr][longueur];
		int k=0;
		for(int i=0;i<nbr;i++){
			k=i;
			for(int j=longueur-1;j>=0;j--){
				if(k%2==0){
					k/=2;
				}
				else{
					comb[i][j]=mot.charAt(j);
					k/=2;
				}
			}
		}
		//System.out.println(comb);
		return comb;
	}
	
	static void afficher(char[][] a){
		for(int i=0;i<a.length;i++){
			for(int j=0;j<a[0].length;j++){
				System.out.print(a[i][j]);
			}
			System.out.println();
		}
	}
	public static void main(String[] args) {
		afficher(Combinaisons("abcd"));
		//Combinaisons("abcd");

	}

}

