package utilities;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The Interface TwistClientNotifyInterface.
 */
public interface TwistClientNotifyInterface extends Remote {

	/** Constant OBJECT_NAME. */
	public static final String OBJECT_NAME = "CLIENT_NOTIFY";

	/**
	 * Notifica al client l'invito ad una partita.
	 *
	 * @param utente utente che ha effettuato l'invito
	 * @param id_partita id della partita
	 * @throws RemoteException
	 *             if remote communication with the registry failed
	 */
	public void notifyInvito(String utente, int id_partita) throws RemoteException;
}
