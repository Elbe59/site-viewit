package utils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class MotDePasseUtils {
	private static final int LONGUEUR_SEL = 128;
	private static final int LONGUEUR_HASH = 128;

	private static final int NOMBRE_ITERATIONS = 5;
	private static final int MEMOIRE = 65536;
	private static final int PARALLELISME = 1;

	private static Argon2 instancierArgon2() {
		return Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2i, LONGUEUR_SEL, LONGUEUR_HASH);
	}

	public static String genererMotDePasse(String motDePasse) {
		return instancierArgon2().hash(NOMBRE_ITERATIONS, MEMOIRE, PARALLELISME, motDePasse);
	}

	public static boolean validerMotDePasse(String motDePasse, String hashCorrect) {
		boolean verif=instancierArgon2().verify(hashCorrect,motDePasse);
		return verif;
	}

	/*public static void main(String[] args) {
		String mdp=MotDePasseUtils.genererMotDePasse("123");
		String mdp1=MotDePasseUtils.genererMotDePasse("1234");
		String mdp2=MotDePasseUtils.genererMotDePasse("DenisDu59");
		String mdp3=MotDePasseUtils.genererMotDePasse("Yann1234");
		String mdp4=MotDePasseUtils.genererMotDePasse("motdepasse");
		String mdp5=MotDePasseUtils.genererMotDePasse("JPdu59");
		System.out.println(mdp);
		System.out.println(mdp1);
		System.out.println(mdp2);
		System.out.println(mdp3);
		System.out.println(mdp4);
		System.out.println(mdp5);
	}*/
}
