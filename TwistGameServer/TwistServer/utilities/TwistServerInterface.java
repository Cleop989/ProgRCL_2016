package utilities;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The Interface TwistServerInterface.
 */
public interface TwistServerInterface extends Remote {

	/** Constant OBJECT_NAME. */
	public static final String OBJECT_NAME = "TWIST_SERVER_GAME";

	/**
	 * Effettua l'accesso al gioco.
	 *
	 * @param username nome utente
	 * @param pwd password
	 * @param client stub per le notifiche al client
	 * @return 0 se l'operazione ha avuto successo, 
	 * 		   1 se l'utente non è registrato, 
	 * 		   2 se l'utente è già loggato, 
	 * 		   3 se la password è errata
	 * @throws RemoteException
	 *             if remote communication with the registry failed
	 */
	public int login(String username, String pwd, TwistClientNotifyInterface client) throws RemoteException;

	/**
	 * Effettua il logout dal gioco.
	 *
	 * @param username nome utente
	 * @return true se l'operazione ha avuto successo, false se l'utente non è
	 *         loggato
	 * @throws RemoteException
	 *             if remote communication with the registry failed
	 */
	public boolean logout(String username) throws RemoteException;

	/**
	 * Registra un utente al gioco.
	 *
	 * @param username nome utente
	 * @param pwd password
	 * @return true se l'operazione ha avuto successo, false se username già
	 *         presente
	 * @throws RemoteException
	 *             if remote communication with the registry failed
	 */

	public boolean registrazione(String username, String pwd) throws RemoteException;

}
