package twistClient.Client;

import java.util.concurrent.Callable;

import twistClient.Interface.TwistClientGUI;
import twistClient.utilities.ListString;

/**
 * The Class Game. Gestisce l'input dell'utente durante la parita: apre una
 * finestra che visualizza le lettere e consente l'inserimento delle parole
 */
public class Game implements Callable<Object> {

	/** Lettere della parita */
	private String lettere;

	/** Struttura che permette di salvare le parole inserite dall'utente */
	private ListString parole;

	/** Interfaccia grafica. */
	private TwistClientGUI gui;


	/**
	 * Instantiates a new game.
	 *
	 * @param gui interfaccia grafica
	 * @param lettere lettere partita
	 * @param parole struttura per il salvataggio delle parole inserite
	 */
	public Game(TwistClientGUI gui, String lettere, ListString parole) {
		this.gui = gui;
		this.parole = parole;
		this.lettere = lettere;
	}

	@Override
	public Object call() throws Exception {
		TakeParole d = new TakeParole(gui, parole, lettere);
		d.setVisible(true);

		d.dispose();
		return null;
	}

}
