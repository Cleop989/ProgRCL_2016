package twistServer.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import utilities.TwistClientNotifyInterface;

/**
 * The Class Utente. Dati di un utente registrato a TwistGame
 */
public class Utente implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2626440666895351913L;

	/**
	 * Deserializza utenti o inizializza una collezione vuota se non esiste.
	 *
	 * @param nameFile nome del file da deserializzare
	 * @return utentiRegistrati deserializzato o inizializzato, null se si è
	 *         verificato un errore
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Utente> deserializeMap(String nameFile) {
		synchronized (nameFile) {
			if (!(new File(nameFile).exists()))
				return Collections.synchronizedMap(new HashMap<>());

			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(nameFile));) {
				Map<String, Utente> mp = (Map<String, Utente>) in.readObject();
				return Collections.synchronizedMap(mp);
			}catch (IOException e) {
				System.err.println("deserializeMap-IOException: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				System.err.println("deserializeMap-ClassNotFoundException: " + e.getMessage());
			}
		}		
		return null;
	}

	/**
	 * Serializzazione utenti registrati.
	 *
	 * @param mp Map da serializzare
	 * @param nameFile file in cui salvare la serializzazione
	 * @throws IOException
	 *             if an I/O error occurs while writing stream header
	 * @throws FileNotFoundException
	 *             if the file exists but is a directory rather than a regular
	 *             file, does not exist but cannot be created, or cannot be
	 *             opened for any other reason
	 */
	public static void serializeMap(Map<String, Utente> mp, String nameFile) throws FileNotFoundException, IOException {
		synchronized (nameFile) {
			try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nameFile));) {
				out.writeObject(mp);
			}
		}
	}

	/** Nome utente. */
	private String userName;

	/** password. */
	private String password;

	/** Punteggio generale utente. */
	private int punteggio;

	/** stato dell'utente. */
	private transient boolean stato;

	/** Oggetto remoto per inviare l'invito. */
	private transient TwistClientNotifyInterface clientNotify;

	/**
	 * Instantiates a new utente.
	 *
	 * @param name username utente
	 * @param pwd password utente
	 */
	public Utente(String name, String pwd) {
		this.userName = name;
		this.password = pwd;
		this.punteggio = 0;
		this.clientNotify = null;
		this.stato = false;
	}

	/**
	 * Instantiates a new utente.
	 *
	 * @param name username utente
	 * @param pwd password utente
	 * @param punteggio punteggio ottenuto
	 */
	public Utente(String name, String pwd, int punteggio) {
		this.userName = name;
		this.password = pwd;
		this.punteggio = punteggio;
		this.clientNotify = null;
		this.stato = false;
	}

	/**
	 * Aggiunge il punteggio della partita a quello generale.
	 *
	 * @param punteggio punteggio da aggiungere
	 */
	public synchronized void addPunteggio(int punteggio) {
		this.punteggio += punteggio;
	}

	/**
	 * Due utenti sono uguali se hanno lo stesso userName.
	 *
	 * @param obj oggetto da comparare
	 * @return true, se se sono uguali
	 */
	@Override
	public boolean equals(Object obj) {
		return this.getUserName().equals(((Utente) obj).getUserName());
	}

	/**
	 * Prende l'oggetto remoto del client.
	 *
	 * @return clientNotify RMI
	 */
	public synchronized TwistClientNotifyInterface getClientNotify() {
		return clientNotify;
	}

	/**
	 * Punteggio generale utente.
	 *
	 * @return punteggio utente
	 */
	public synchronized int getPunteggio() {
		return punteggio;
	}

	/**
	 * Username utente.
	 *
	 * @return username utente
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Verifica se un utente è online.
	 *
	 * @return true se l'utente è online
	 */
	public synchronized boolean isAttivo() {
		return this.stato;
	}

	/**
	 * Abilita/disabilita utente online.
	 *
	 * @param b true se si vuole segnare l'utente come attivo, false
	 *          altrimenti
	 */
	public synchronized void setAttivo(boolean b) {
		this.stato = b;
	}

	/**
	 * Setta oggetto remoto client.
	 *
	 * @param clientNotify oggetto remoto
	 */
	public synchronized void setClientNotify(TwistClientNotifyInterface clientNotify) {
		this.clientNotify = clientNotify;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.userName);
		sb.append(" ");
		sb.append(this.password);
		sb.append(" ");
		sb.append(this.punteggio);
		return sb.toString();
	}

	/**
	 * Verifica password utente.
	 *
	 * @param pwd password da verificare
	 * @return true se la password è corretta
	 */
	public boolean verificaPassword(String pwd) {
		return password.equals(pwd);
	}

}
