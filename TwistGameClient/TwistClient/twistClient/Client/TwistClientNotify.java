package twistClient.Client;

import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.util.Vector;

import twistClient.Interface.TwistClientGUI;
import utilities.TwistClientNotifyInterface;

/**
 * The Class TwistClientNotify. Implementazione della callback RMI per la
 * notifica di un invito
 */
public class TwistClientNotify extends RemoteObject implements TwistClientNotifyInterface {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2279403047429203996L;

	/** vettore sincronizzato degli inviti ricevuti */
	private Vector<String> inviti;

	/** interfaccia grafica */
	private TwistClientGUI gui;

	/**
	 * Instantiates a new twist client notify.
	 *
	 * @param gui interfaccia grafica
	 * @param inviti vettore inviti ricevuti
	 */
	public TwistClientNotify(TwistClientGUI gui, Vector<String> inviti) {
		this.inviti = inviti;
		this.gui = gui;
	}

	@Override
	public void notifyInvito(String username, int id_partita) throws RemoteException {
		inviti.add(id_partita + "-" + username);
		gui.printMessage(username + " ti ha invitato");
	}

}
