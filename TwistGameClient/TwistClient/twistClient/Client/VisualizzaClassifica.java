package twistClient.Client;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import twistClient.Interface.TwistClientGUI;
import twistClient.utilities.Costanti;
import utilities.SocketTCP;

/**
 * The Class VisualizzaClassifica. Gestisce la richiesta e la ricezione della
 * classifica generale
 */
public class VisualizzaClassifica implements Runnable {

	/** costatanti del client */
	private Costanti costanti;

	/** interfaccia grafica */
	private TwistClientGUI gui;

	/** utente loggato */
	private String user;

	/**
	 * Instantiates a new visualizza classifica.
	 *
	 * @param gui interfaccia grafica
	 * @param costanti costanti del client
	 * @param user utente loggato
	 */
	public VisualizzaClassifica(TwistClientGUI gui, Costanti costanti, String user) {
		this.costanti = costanti;
		this.gui = gui;
		this.user = user;
	}

	@Override
	public void run() { 
		ArrayList<String> classifica = null;
		try (SocketTCP socket = new SocketTCP(costanti.getIP_SERVER(), costanti.getPORTA_INIT());){
			// si connette in tcp al server (invia nomeUtente e richiesta)
			socket.sendMessage(user);
			socket.sendMessage(costanti.CLASSIFICA_GENERALE);

			// attende la classifica 
			classifica = socket.receiveObject();
		} catch (IOException e) {
			gui.fatalError("IOException", "Errore, impossibile comunicare con il server", true);
		} catch (ClassNotFoundException e) {
			gui.fatalError("IOException", "Errore, questo non dovrebbe verificarsi ", true);
		}
		
		// Crea la stringa da visualizzare (classifica)		
		StringBuilder ris = new StringBuilder();
		for (String riga : classifica){
			ris.append(riga+"\n");
		}

		//visualizza la classifica
		JOptionPane.showMessageDialog(gui, ris, "Classifica", JOptionPane.INFORMATION_MESSAGE);

	}

}
