package twistServer.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * The Class Dizionario. Dizionario e funzioni necessarie per la gestione dei
 * risultati della partita
 */
public class Dizionario {

	/** dizionario. */
	private Set<String> dizionario;

	/**
	 * Instantiates a new dizionario.
	 *
	 * @param file file contenente l'elenco delle parole
	 * @throws FileNotFoundException
	 *             if the named file does not exist, is a directory rather than
	 *             a regular file, or for some other reason cannot be opened for
	 *             reading.
	 */
	public Dizionario(String file) throws FileNotFoundException {
		this.generaDizionario(file);
	}

	/**
	 * Verifica la presenza della parola nel dizionario.
	 *
	 * @param parola parola da verificare
	 * @return true se il dizionario contiene la parola, false altrimenti.
	 */
	public boolean contains(String parola) {
		return dizionario.contains(parola);
	}

	/**
	 * Crea un dizionario. Usa HashSet per avere una ricerca più veloce
	 *
	 * @param file file contenente l'elenco delle parole
	 * @throws FileNotFoundException
	 *             if the named file does not exist, is a directory rather than
	 *             a regular file, or for some other reason cannot be opened for
	 *             reading.
	 */
	private void generaDizionario(String file) throws FileNotFoundException {
		dizionario = Collections.synchronizedSet(new HashSet<>());
		try (BufferedReader in = new BufferedReader(new FileReader(file));) {
			String line = in.readLine();
			while (line != null) {
				dizionario.add(line);
				line = in.readLine();
			}
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prende una parola a caso dal dizionario che abbia almeno 6 lettere.
	 * 
	 * @return parola scelta
	 */
	public String getParolaRandom() {
		if (dizionario.size() == 0) {
			return null;
		}
		Random rand = new Random(System.currentTimeMillis());
		int index = rand.nextInt(dizionario.size());
		Iterator<String> iter = dizionario.iterator();
		for (int i = 0; i < index; i++) {
			iter.next();
		}
		String parola = "";
		while (((parola = iter.next()).length()) < 6) {
			iter.next();
		}
		return parola;
	}

}
