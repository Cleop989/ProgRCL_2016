package twistServer.Server;


import java.io.IOException;
import java.rmi.RemoteException;

import twistServer.utilities.DatiTwistGame;
import twistServer.utilities.Utente;
import utilities.TwistClientNotifyInterface;
import utilities.TwistServerInterface;

/**
 * The Class TwistServerRMI. Implementazione dei metodi messi a disposizione dal
 * server RMI
 */
public class TwistServerRMI implements TwistServerInterface {

	/** Dati del server */
	private DatiTwistGame dati;


	/**
	 * Instantiates a new twist server RMI.
	 *
	 * @param dati dati del server
	 */
	public TwistServerRMI(DatiTwistGame dati) {
		this.dati = dati;
	}

	@Override
	public int login(String username, String pwd, TwistClientNotifyInterface clientNotify) throws RemoteException {
		if (!dati.utentiRegistrati.containsKey(username))
			return 1;
		if (dati.utentiRegistrati.get(username).isAttivo())
			return 2;

		Utente u = dati.utentiRegistrati.get(username);
		if (u.verificaPassword(pwd)) {
			u.setAttivo(true);
			u.setClientNotify(clientNotify);
			System.out.println(username + " ha effettuato il login");
			return 0;
		}
		return 3;
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		Utente u = dati.utentiRegistrati.get(username);
		if (u == null)
			return false;
		u.setAttivo(false);
		System.out.println(username + " ha effettuato il logout");
		return true;
	}

	@Override
	public boolean registrazione(String username, String pwd) throws RemoteException {
		if (dati.utentiRegistrati.containsKey(username))
			return false;
		Utente u = new Utente(username, pwd);
		dati.utentiRegistrati.put(username, u);
		System.out.println(username + " ha effettuato la registrazione");
		try {
			Utente.serializeMap(dati.utentiRegistrati, dati.costanti.getFILE_UTENTI());
		} catch (IOException e) {
			System.out.println("registrazione: " + e.getMessage());
			return false;
		}
		return true;
	}

}
