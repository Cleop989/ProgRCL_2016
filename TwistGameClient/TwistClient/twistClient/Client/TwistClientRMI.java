package twistClient.Client;

import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import twistClient.Interface.TwistClientGUI;
import twistClient.utilities.Costanti;
import utilities.TwistClientNotifyInterface;
import utilities.TwistServerInterface;

/**
 * The Class TwistClientRMI. Client TwistGame RMI, fornisce i metodi per le operazioni
 * di login, logout, registrazione.
 */
public class TwistClientRMI {

	/** Interfaccia utente. */
	private TwistClientGUI gui;

	/** Oggetto RMI server. */
	private TwistServerInterface twistGameRMI;

	/** Oggetto RMI client. */
	private TwistClientNotifyInterface clientNotifyStub;

	/** Oggetto clientNotify esportato sul registro RMI. */
	private TwistClientNotify clientNotify;
	
	/** Utente che ha effettuato il login. */
	private String username;
	
	/** Vettore contenente le partite a cui si è sati invitati. */
	private Vector<String> invitiRicevuti;
	
	/**
	 * Instantiates a new twist client.
	 *
	 * @param gui interfaccia grafica
	 * @param costanti the costanti
	 */
	public TwistClientRMI(TwistClientGUI gui, Costanti costanti) {
		this.gui = gui;
		this.invitiRicevuti = new Vector<>();
		
		// Setup RMI
		try {
			clientNotify = new TwistClientNotify(gui,invitiRicevuti);
			clientNotifyStub = (TwistClientNotifyInterface) UnicastRemoteObject.exportObject(clientNotify, 0);
			
			Registry registry = LocateRegistry.getRegistry(costanti.getIP_SERVER(), costanti.getREGISTRY_PORT());
			twistGameRMI = (TwistServerInterface) registry.lookup(TwistServerInterface.OBJECT_NAME);
			gui.printMessage("Client pronto");
		} catch (AccessException e) {
			gui.fatalError("AccessException", e.getMessage(), false);
		} catch (RemoteException e) {
			gui.fatalError("RemoteException", "Errore durante la connessione al server", false);
		} catch (NotBoundException e) {
			gui.fatalError("NotBoundException", e.getMessage(), false);
		}
	}

	/**
	 * Rimuove l'oggetto RMI dal registro.
	 */
	public void clearRegistryRMI() {
		try {
			UnicastRemoteObject.unexportObject(clientNotify, true);
		} catch (NoSuchObjectException e) {
			gui.printMessage("Counld not unexport :" + e.getMessage());
		}
	}

	/**
	 * Gets inviti ricevuti.
	 *
	 * @return inviti ricevuti
	 */
	public Vector<String> getInvitiRicevuti() {
		return invitiRicevuti;
	}

	/**
	 * Gets username.
	 *
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Effettua l'operazione di login. Se si è verificato un errore stampa un
	 * messaggio
	 *
	 * @param user nome utente
	 * @param password password utente
	 * @return true se è avvenuta con successo, false altrimenti
	 */
	public boolean login(String user,String password) {
		try {
			switch (twistGameRMI.login(user, password, clientNotifyStub)) {
			case 0:
				this.username=user;
				gui.clearMessageArea();
				gui.printMessage("Login effettuato con successo");
				return true;
			case 1:
				gui.printMessage("E' necessario essere registrati");
				break;
			case 2:
				gui.printMessage("Login già effettuato");
				gui.setButtonLoguotObbligatorio();
				break;
			case 3:
				gui.printMessage("Password errata");
				break;
			default:
				gui.printMessage("Si è verificato un errore riprovare");
				break;
			}
		} catch (RemoteException e) {
			gui.fatalError("RemoteException", "Errore durante la connessione al server", false);
		}

		return false;
	}
		
	/**
	 * Effettua l'operazione di logout.
	 *
	 * @return true se è avvenuta con successo, false altrimenti
	 */
	public boolean logout() {
		try {
			if (twistGameRMI.logout(username)) {
				gui.clearMessageArea();
				gui.printMessage("Logout eseguito con successo");
				gui.clearLogin();
				gui.setButtonLogin(true);
				gui.setButtonGameFalse();
				return true;
			}
			gui.printMessage("Logout fallito");
		} catch (RemoteException e) {
			//gui.fatalError("RemoteException", "Errore durante la connessione al server", false);
		}
		return false;
	}
	
	/**
	 * Effettua l'operazione di registrazione.
	 *
	 * @param username nome utente
	 * @param password password utente
	 * @return true se è avvenuta con successo, false altrimenti
	 */
	public boolean registrati(String username,String password) {
		try {
			if (twistGameRMI.registrazione(username, password)) {
				gui.printMessage("Registrazione eseguita con successo");
				return true;
			}
			gui.printMessage("Registrazione fallita");
		} catch (RemoteException e) {
			gui.fatalError("RemoteException", "Errore durante la connessione al server", false);
		}

		return false;

	}
}
