package twistServer.utilities;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The Class Lettere. Gestione delle lettere di una partita.
 */
public class Lettere {

	/** lettere. */
	private ArrayList<Character> lettere;

	/**
	 * Instantiates a new lettere.
	 *
	 * @param parola parola da ricavare le lettere
	 */
	public Lettere(String parola) {
		this.lettere = new ArrayList<>();
		this.lettere = toArrayList(parola.toCharArray());
		// permuta le lettere
		Collections.shuffle(lettere);
	}

	/**
	 * Restituisce le lettere da usare nella partita.
	 *
	 * @return lettere della partita
	 */
	public String getLettere() {
		StringBuilder lettereDaInviare = new StringBuilder();
		for (Character c : lettere) {
			lettereDaInviare.append(c + "   ");
		}
		return lettereDaInviare.toString().toUpperCase();

	}

	/**
	 * Una parola è valida se usa solo le lettere a disposizione ed è inclusa
	 * nel dizionario.
	 * 
	 * @param parola parola di cui si vuole calcolare il punteggio
	 * @param dizionario dizionario di gioco
	 * @return punteggio: numero lettere se la parola è valida, 0 altrimenti
	 */
	public int getPunteggio(String parola, Dizionario dizionario) {
		int punti = 0;
		if (dizionario.contains(parola)) {
			ArrayList<Character> parolaDaTestare = this.toArrayList(parola.toCharArray());
			punti = parolaDaTestare.size();
			for (Character c : lettere) {
				parolaDaTestare.remove(c);
			}
			if (!parolaDaTestare.isEmpty())
				return 0;
		}
		return punti;
	}

	/**
	 * Trasforma la parola in un array di lettere.
	 *
	 * @param parola parola da trasformare
	 * @return ArrayList character con le lettere
	 */
	private ArrayList<Character> toArrayList(char[] parola) {
		ArrayList<Character> lettere = new ArrayList<>();
		for (char l : parola) {
			lettere.add(l);
		}
		return lettere;

	}

}
