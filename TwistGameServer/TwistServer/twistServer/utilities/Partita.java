package twistServer.utilities;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import utilities.SocketTCP;

/**
 * The Class Partita. Classe contenente i dati di una partita TwistGame
 */
public class Partita {

	/**
	 * The Class Partecipante. Raggruppa le informazione sull'utente
	 * partecipante necessarie per la gestione di una partita
	 */
	private class Partecipante {
		/** socket con cui comunuca con il client **/
		private SocketTCP socket;
		
		/** punteggio della partita **/
		private int punteggio;
	

		/**
		 * Instantiates a new partecipante.
		 *
		 * @param socket socket del partecipante
		 */
		public Partecipante(SocketTCP socket) {
			this.socket = socket;
		}

		/**
		 * Gets punteggio.
		 *
		 * @return punteggio partita
		 */
		public int getPunteggio() {
			return punteggio;
		}

		/**
		 * Gets the socket.
		 *
		 * @return the socket
		 */
		public SocketTCP getSocket() {
			return socket;
		}

		/**
		 * Sets punteggio.
		 *
		 * @param punteggio il nuovo punteggio
		 */
		public void setPunteggio(int punteggio) {
			this.punteggio = punteggio;
		}
	}

	/** id partita */
	private int id;

	/** Dizionario. */
	private Dizionario dizionario;

	/** Lettere della parita. */
	private Lettere lettere;

	/** Utenti che hanno accettato la partita e lo punteggio. */
	private Map<String, Partecipante> partecipanti;

	/** Untenti a cui è stato inviato l'invito. */
	private Set<String> utentiInvitati;

	/**
	 * Instantiates a new partita.
	 *
	 * @param id
	 *            identificatore partita
	 * @param dizionario
	 *            dizionario di gioco
	 */
	public Partita(int id, Dizionario dizionario) {
		this.id = id;
		this.partecipanti = Collections.synchronizedMap(new HashMap<>());
		this.utentiInvitati = new HashSet<>();
		this.dizionario = dizionario;
	}

	/**
	 * Aggiunge un utente alla lista dei partecipanti.
	 *
	 * @param utente utente partecipante
	 * @param socket socket con cui comuntica con il client
	 */
	public void addPartecipanti(String utente, SocketTCP socket) {
		this.partecipanti.put(utente, new Partecipante(socket));
	}

	/**
	 * Aggiunge un utente alla lista degli utenti invitati.
	 *
	 * @param utente utente invitato
	 */
	public void addUtenteInvitato(Utente utente) {
		this.utentiInvitati.add(utente.getUserName());
	}

	/**
	 * Calcola i punteggi della partita. Scarta le parole doppie
	 * 
	 * @param utente utente di cui deve essere calcolato il punteggio partita
	 * @param setParole insieme delle parole inserite dall'utente
	 */
	public void calcoloPunteggio(String utente, Set<String> setParole) {
		int puntiTot = 0;
		for (String parola : setParole) {		
			puntiTot = puntiTot + lettere.getPunteggio(parola.trim(), dizionario);
		}	
		partecipanti.get(utente).setPunteggio(puntiTot);
	}

	/**
	 * Identificatore partita.
	 *
	 * @return id partita
	 */
	public int getId() {
		return id;
	}

	/**
	 * Lettere della partita sottoforma di stringa.
	 *
	 * @return stringa contenente le lettere
	 */
	public String getLettere() {
		return lettere.getLettere();
	}

	/**
	 * Prende la lista degli utenti partecipanti alla partita (coloro che hanno
	 * accettato l'invito).
	 *
	 * @return set di stringhe con gli utenti partecipanti
	 */
	public Set<String> getPartecipanti() {
		return partecipanti.keySet();
	}

	/**
	 * Punteggio partita dell'utente.
	 *
	 * @param utente
	 *            utente di cui si vuole sapere il punteggio partita
	 * @return punteggio
	 */
	public Integer getPunteggio(String utente) {
		return partecipanti.get(utente).getPunteggio();
	}

	/**
	 * Gets socket partecipante ala partita.
	 *
	 * @param username nome utente
	 * @return socket del partecipante alla partita
	 */
	public SocketTCP getSocket(String username) {
		return partecipanti.get(username).getSocket();
	}

	/**
	 * Prende la lista degli utenti invitati.
	 *
	 * @return Set di stringhe con gli utenti invitati
	 */
	public Set<String> getUtentiInvitati() {
		return utentiInvitati;
	}

	/**
	 * Verifica se è stato invitato invitato.
	 *
	 * @param username nome utente
	 * @return true, se è stato invitato
	 */
	public boolean isInvitato(String username) {
		return utentiInvitati.contains(username);
	}
	
	/**
	 * Verifica se tutti gli utenti invitati hanno accettato.
	 *
	 * @return true se tutti gli utenti invitati hanno accettato
	 */
	public boolean isValida() {
		return utentiInvitati.size() == partecipanti.size();
	}

	/**
	 * Imposta le lette della partita partendo da una parola generata a caso dal
	 * dizionario.
	 */
	public void setLettere() {
		this.lettere = new Lettere(dizionario.getParolaRandom());
	}

	
}
