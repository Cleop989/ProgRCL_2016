package twistClient.Client;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import twistClient.Interface.TwistClientGUI;
import twistClient.utilities.Costanti;
import twistClient.utilities.ListString;
import utilities.SocketTCP;

/**
 * The Class NuovaPartita. Runnable che gestisce la richiesta di una nuova
 * partita. Inviata la richiesta al server con la lista degli avversari, attende
 * la conferma.
 */
public class NuovaPartita implements Runnable {

	/** Interfaccia grafica */
	private TwistClientGUI gui;

	/** Costanti del client */
	private Costanti costanti;

	/** Utente che ha aperto il client */
	private String user;

	/**
	 * Instantiates a new nuova partita.
	 *
	 * @param gui Interfaccia grafica
	 * @param costanti Costanti del client
	 * @param user Utente che ha aperto il client
	 */
	public NuovaPartita(TwistClientGUI gui, Costanti costanti, String user) {
		this.gui = gui;
		this.costanti = costanti;
		this.user = user;
	}

	@Override
	public void run() {

		// prende avversari da invitare
		ListString avversari = new ListString();

		TakeAvversari d = new TakeAvversari(gui, avversari);
		d.setVisible(true);

		ArrayList<String> listaAvversari = avversari.takeList();
		if (listaAvversari.isEmpty()) {
			JOptionPane.showMessageDialog(gui, "Inserire almeno un avversario");
			return;
		}

		// invia avversari
		try (SocketTCP socket = new SocketTCP(costanti.getIP_SERVER(), costanti.getPORTA_INIT());) {
			// il server prevede l'identificazione tramite username prima
			// dell'invio del comando
			socket.sendMessage(user);
			socket.sendMessage(costanti.NUOVA_PARTITA);
			socket.sendArray(listaAvversari);
			
			// attende messaggio di conferma
			String message = socket.receiveMessage();
			if (message.equals(costanti.PARTITA_ANNULLATA)) {
				gui.printMessage("Un utente è risultato inattivo, partita cancellata");
			} else if (message.equals(costanti.CONFERMA_INVIO_INVITI)) {
				gui.printMessage("Invio inviti eseguito con successo");
			}
		} catch (IOException e) {
			gui.fatalError("IOException", "Si è verificato un errore o il server ha chiuso la connessione", true);
		} 

		

	}

}
