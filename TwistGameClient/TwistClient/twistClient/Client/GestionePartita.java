package twistClient.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import twistClient.Interface.TwistClientGUI;
import twistClient.utilities.Costanti;
import twistClient.utilities.ListString;
import utilities.SocketTCP;
import utilities.SocketUDP;

/**
 * The Class GestionePartita. Gestione di una partita dalla visualizzazione
 * degli inviti e la loro gestione, alla ricezione dei risultati della
 * paritita.
 */
public class GestionePartita implements Runnable {

	/** interfaccia grafica. */
	private TwistClientGUI gui;

	/** lista degli inviti ricevuti. */
	private Vector<String> invitiRicevuti;

	/** costanti del client. */
	private Costanti costanti;

	/** utente che si è loggato. */
	private String user;

	/** id della partita corrente. */
	private int idPartita;

	/**
	 * Instantiates a new gestione partita.
	 *
	 * @param gui interfaccia grafica
	 * @param costanti costanti del client
	 * @param user utente loggato
	 * @param invitiRicevuti lista inviti ricevuti
	 */
	public GestionePartita(TwistClientGUI gui, Costanti costanti, String user, Vector<String> invitiRicevuti) {
		this.gui = gui;
		this.invitiRicevuti = invitiRicevuti;
		this.user = user;
		this.costanti = costanti;
	}

	@Override
	public void run() {
		String lettere = setupPartita();
		if (lettere != null)
			avvioPartita(lettere);
	}

	/**
	 * Setup partita. Visualizza l'inviti ricevuti e ne consente la selezione.
	 * Invia al server la conferma o il rifiuto dell'invito. In caso di conferma
	 * attende le lettere o il messaggio di cancellazione partita dal server
	 *
	 * @return lettere da inviare, o null se la partita è stata annullata
	 */
	private String setupPartita() {
		if (invitiRicevuti.isEmpty()) {
			JOptionPane.showMessageDialog(gui, "Nessun invito da visualizzare");
			return null;
		}

		// seleziona un invito
		String[] inviti = invitiRicevuti.toArray(new String[invitiRicevuti.size()]);
		String invitoSelezionato = (String) JOptionPane.showInputDialog(gui, "Scegli un invito...", "Inviti",
				JOptionPane.INFORMATION_MESSAGE, null, inviti, inviti[0]);

		if (invitoSelezionato == null)
			return null;

		String[] invitoSelezionatoSplit = invitoSelezionato.split("-");
		idPartita = Integer.parseInt(invitoSelezionatoSplit[0]);
		String partita = invitoSelezionatoSplit[1];

		// conferma invito
		String[] option = { "Accetto", "Rifiuto" };
		int conferma = JOptionPane.showOptionDialog(gui, "Hai selezionato l'invito di " + partita, "Conferma",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, null);

		if (conferma == JOptionPane.YES_OPTION) {
			// rifiuta gli altri inviti
			invitiRicevuti.remove(invitoSelezionato);

			for (String invito : invitiRicevuti) {
				String[] invitoSplit = invito.split("-");
				try (SocketTCP socket = new SocketTCP(costanti.getIP_SERVER(),
						costanti.getPortaPartita(Integer.parseInt(invitoSplit[0])));) {
					socket.sendMessage(user);
					socket.sendMessage(costanti.INVITO_RIFIUTATO);
				} catch (IOException e) {
					;
				}
			}
			invitiRicevuti.clear();
			gui.printMessage("Hai confermato l'invito di " + partita);
			try (SocketTCP socket = new SocketTCP(costanti.getIP_SERVER(), costanti.getPortaPartita(idPartita));) {
				// invia Conferma
				socket.sendMessage(user);
				socket.sendMessage(costanti.INVITO_CONVERMATO);
				gui.setPartitaInCorso(true);
				// attende le lettere o l'annullamento della partita
				gui.printMessage("Avvio partita attendere...");
				String lettere = socket.receiveMessage();
				if (lettere.equals(costanti.PARTITA_ANNULLATA)) {
					gui.printMessage("Partita annullata");
					gui.setPartitaInCorso(false);
					return null;
				} else
					return lettere;

			} catch (Exception e) {
				// invio messaggio dopo la chiusura del server per la conferma
				// invito
				gui.printMessage("Partita annullata");
				gui.setPartitaInCorso(false);
				return null;
			}

		} else {
			gui.printMessage("Hai rifiutato l'invito di " + partita);
			// rimuove l'invito dalla lista
			invitiRicevuti.remove(invitoSelezionato);

			// invio rifiuto
			try (SocketTCP socket = new SocketTCP(costanti.getIP_SERVER(), costanti.getPortaPartita(idPartita));) {
				socket.sendMessage(user);
				socket.sendMessage(costanti.INVITO_RIFIUTATO);
				return null;
			} catch (IOException e) {
				;
			}
		}
		return null;
	}

	/**
	 * Comunica che il gioco è pronto (l'avvio prevede clik sul tasto ok).
	 * Mostra lettere e accetta parole per 2 minuti, al termine ferma l'input
	 * dell'utente e invia le parole al server tramite UDP. Avvia il thread per
	 * la ricezione dei risultati su multicast
	 * 
	 * @param lettere
	 *            lettere della partita
	 */
	private void avvioPartita(String lettere) {
		// Avvia il thread multicast per l'attesa dei risultati
		Thread multicast = new Thread(
				new TwistClientMulticast(costanti.getIP_MULTICAST(), costanti.getPortaMulticast(idPartita), gui));
		multicast.start();

		//Avvia la partita
		JOptionPane.showMessageDialog(gui, "Partita pronta", "Avvio partita",JOptionPane.INFORMATION_MESSAGE, null);
		ListString parole = new ListString();
		try {
			// thread attivo per due minuti che apre una nuova finestra
			ExecutorService esPartita = Executors.newCachedThreadPool();
			List<Callable<Object>> callables = new ArrayList<>();
			callables.add(new Game(gui, lettere, parole));
			esPartita.invokeAll(callables, 120000, TimeUnit.MILLISECONDS);
			esPartita.shutdown();
			gui.printMessage("Partita terminata, attendi i risultati...");
		} catch (InterruptedException e1) {
			;
		}

		//crea la stringa da inviare al server
		ArrayList<String> paroleList = parole.takeList();
		StringBuilder sb = new StringBuilder();
		sb.append(user);
		for (String p : paroleList) {
			sb.append("_" + p);
		}
		String message = sb.toString();

		// invia il messaggio al server tramite UDP
		try (SocketUDP socketUDP = new SocketUDP(costanti.getIP_SERVER(), costanti.getPortaPartita(idPartita));) {
			socketUDP.sendMessage(message);
		} catch (IOException e) {
			gui.printMessage("Errore durante invio dei risultati");
		}

		// Attende la chiusura del server multicast
		try {
			multicast.join();
		} catch (InterruptedException e) {
			;
		}

		gui.printMessage("Risultati ricevuti");
		gui.setPartitaInCorso(false);
	}
}
