package twistClient.utilities;

import java.util.ArrayList;

/**
 * The Class ListString. Gestisce il salvataggio di stringhe e il loro recupero
 * su finestre diverse
 */
public class ListString {

	/** lista stringhe. */
	private ArrayList<String> list;

	/**
	 * Instantiates a new list string.
	 */
	public ListString() {
		this.list = new ArrayList<>();
	}

	/**
	 * Aggiunge una stringa alla lista.
	 *
	 * @param str stringa da inserire
	 */
	public void addString(String str) {
		list.add(str);
	}

	/**
	 * Verifica se la lista è vuota.
	 *
	 * @return true se la lista è vuota
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}

	/**
	 * Lista dati inseriti.
	 *
	 * @return lista contenente le stringhe inseriti
	 */
	public ArrayList<String> takeList() {
		return list;
	}
}
