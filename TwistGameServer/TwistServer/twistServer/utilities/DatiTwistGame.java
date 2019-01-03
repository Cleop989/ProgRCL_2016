package twistServer.utilities;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Class DatiTwistGame. Struttura che raggruppa i dati necessari al server
 */
public class DatiTwistGame {
	/*
	 * vengono usate delle mappe per salvare gli utenti attivi e quelli
	 * registrati per renderne più veloce la ricerca
	 */

	/** Utenti registrati a TwistGame. */
	public Map<String, Utente> utentiRegistrati;

	/** Dizionario del gioco. */
	public Dizionario dizionario;

	/** Costanti usate dal server. */
	public Costanti costanti;
	/**
	 * numPartite serve per creare idPartita univoco necessario alla gestione
	 * delle porte
	 */
	public AtomicInteger numPartite;

	/**
	 * Instantiates a new dati twist game.
	 *
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	public DatiTwistGame() throws FileNotFoundException {
		costanti = new Costanti();
		dizionario = new Dizionario(costanti.getDICTIONARY());
		utentiRegistrati = Utente.deserializeMap(costanti.getFILE_UTENTI());
		numPartite = new AtomicInteger(1);
	}
}
