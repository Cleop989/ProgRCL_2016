package twistServer.Server;


import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import twistServer.utilities.DatiTwistGame;
import twistServer.utilities.Partita;
import twistServer.utilities.Utente;
import utilities.SocketTCP;
import utilities.SocketUDP;

/**
 * The Class TwistServerTCPHandler. Classe principale della gestione del server.
 * Gestisce le richieste di una nuova partita e le operazioni conseguenti e la
 * richiesta della classifica generale.
 */
public class TwistServerHandler implements Runnable {

	/** dati partita corrente. */
	private Partita partita;

	/** utente che ha effettuato la connessione. */
	private Utente utente;

	/**
	 * Struttura contenente i dati: utentiAttivi, utentiRegistrati, costanti,
	 * dizionario, numPartite.
	 */
	private DatiTwistGame dati;

	/** socket cliente. */
	private SocketTCP socketClient;

	/**
	 * Instantiates a new twist server TCP handler.
	 *
	 * @param user utente che ha effettuato la richiesta
	 * @param dati collezione dei dati del server
	 * @param client socket con cui comunica
	 */
	public TwistServerHandler(Utente user, DatiTwistGame dati, SocketTCP client) {
		this.dati = dati;
		this.utente = user;
		this.socketClient=client;
	}

	/**
	 * Inizializzazione di una nuova partita: Riceve gli utenti invitati dal
	 * client, controlla che siano tutti online. Invia gli inviti tramite
	 * callback. Conferma l'inoltro e invia la notifica anche all'utente che ha
	 * avviato la partita.
	 *
	 * @return true, se l'operazione si è conclusa con successo
	 */
	private boolean nuovaPartita() {
		System.out.println(utente.getUserName() + " ha richiesto una nuova partita");
		// crea una nuova partita
		System.out.println("creazione nuova partita ...");
		partita = new Partita(dati.numPartite.getAndAdd(2), dati.dizionario);

		ArrayList<String> utentiInvitatiString;
		// socket per comunicare con l'utente
		try {
			// riceve utenti invitati dal client
			System.out.println("Partita"+partita.getId()+": attendo inviti ...");

			utentiInvitatiString = socketClient.receiveArray();

			// controlla che gli utenti siano attivi e registrati
			System.out.println("Partita"+partita.getId()+": controllo utenti online ...");
			for (String invitato : utentiInvitatiString) {
				Utente user = dati.utentiRegistrati.get(invitato);
				if ((user == null) || (!user.isAttivo())) {
					System.out.println("Partita"+partita.getId()+": Partita non valida");
					socketClient.sendMessage(dati.costanti.PARTITA_ANNULLATA);
					socketClient.close();
					return false;
				}

				// non consente di invitare se stesso per una partita 
				if (user.getUserName().equals(utente.getUserName())) {
					System.out.println("Partita"+partita.getId()+": Partita non valida");
					socketClient.sendMessage(dati.costanti.PARTITA_ANNULLATA);
					socketClient.close();
					return false;
				}

				// se tutto ok lo metto in attesa di conferma
				partita.addUtenteInvitato(user);
			}


			// chiama le callback. Nell'invito viene mandato anche l'id partita in modo 
			// da permettere al client di conoscere la porta a cui collegarsi
			System.out.println("Partita"+partita.getId()+": invio inviti ...");
			for (String u : partita.getUtentiInvitati()) {
				dati.utentiRegistrati.get(u).getClientNotify().notifyInvito(utente.getUserName(), partita.getId());
			}

			System.out.println("Partita"+partita.getId()+": conferma invio ...");
			socketClient.sendMessage(dati.costanti.CONFERMA_INVIO_INVITI);
			socketClient.close();
		} catch (IOException e) {
			System.out.println("nuovaPartita: Some error appared or client close connection");
			try {
				socketClient.close();
			} catch (IOException e1) {
				;
			}
			return false;
		}

		// notifica all'utente che ha avviato la partita
		try {	
			partita.addUtenteInvitato(utente);
			utente.getClientNotify().notifyInvito(utente.getUserName(), partita.getId());
		} catch (RemoteException e) {
			System.err.println("nuovaPartita: " + e.getMessage());
			return false;
		}

		System.out.println("Partita"+partita.getId()+": è stata inizializzata");

		return true;

	}

	/**
	 * Avvia il thread per accettare le connessioni e lo interrompe dopo 7
	 * minuti. Verifica che tutti gli utenti invitati abbiano accettato la
	 * partita. Se non tutti hanno accettato invia parita annullata a tutti
	 * coloro che avevano accettato.
	 *
	 * @return true, se l'operazione si è conclusa con successo
	 */
	private boolean setupNuovaPartita() {

		ExecutorService es = Executors.newCachedThreadPool();

		try (ServerSocket server = new ServerSocket(dati.costanti.getPortaPartita(partita.getId()));) {
			// setta il timeout della receive a 7 min
			server.setSoTimeout(420000);

			System.out.println("Partita"+partita.getId()+": setup started on Port Number " + dati.costanti.getPortaPartita(partita.getId()));

			while (!server.isClosed()) {
				SocketTCP client = new SocketTCP(server);
				// identifica cliente e inizializza la socket
				String username = client.receiveMessage();
				// la ServerSocket viene passata al Handler in modo da
				// poterla chidere e interrompere il thread se necessario
				SetupPartitaHandler task = new SetupPartitaHandler(username, partita, server,client,dati.costanti);
				es.submit(task);
			}
		} catch (IOException e) {
			;
		}

		es.shutdown();


		System.out.println("Partita"+partita.getId()+": setup on port number " + dati.costanti.getPortaPartita(partita.getId()) + " is closed");

		if (!partita.isValida()) {
			System.out.println("Partita"+partita.getId()+": Partita annullata");
			// annulla la partita
			for (String u : partita.getPartecipanti()) {
				try {	
					partita.getSocket(u).sendMessage(dati.costanti.PARTITA_ANNULLATA);
					partita.getSocket(u).close();
				} catch (IOException e) {
					;	
				} 
			}
			return false;
		}

		return true;
	}

	/**
	 * Crea il multicast su cui si registreranno i client per ricevere i
	 * risultati della partita. Invia lettere ai partecipanti e chiude le
	 * connessioni. Attende 5 minuti per ricevere le parole.
	 */
	private void avvioPartita() {
		// crea multicast
		BlockingQueue<String> listMsg = new LinkedBlockingQueue<>();

		Thread multicast = new Thread(new TwistServerMulticast(listMsg, dati.costanti.getIP_MULTICAST(),
				dati.costanti.getPortaMulticast(partita.getId()),partita.getId()));
		multicast.start();

		// setta le lettere, le invia e chiude le connessioni TCP
		System.out.println("Partita"+partita.getId()+": Invio lettere...");
		partita.setLettere();

		//numeroPartecipanti viene usato per interrompere l'attesa dei dati dai client
		int numeroPartecipanti=partita.getPartecipanti().size();
		for (String u : partita.getPartecipanti()) {
			try (SocketTCP socketTCP = partita.getSocket(u);) {
				socketTCP.sendMessage(partita.getLettere());
				socketTCP.close();
			} catch (IOException e) {
				//in caso un client sia stato chiso la partita prosegue
				System.out.println("Partita"+partita.getId()+" errore nell'inviare le lettere o un client ha chiuso la connessione");
				numeroPartecipanti--;
			}
		}
		System.out.println("Partita"+partita.getId()+": Attendo risultati gioco da elaborare ...");

		// attende risultati per 5 min
		ArrayList<String> risultati = new ArrayList<>();
		try (SocketUDP socketUDP = new SocketUDP(dati.costanti.getPortaPartita(partita.getId()));) {

			socketUDP.setSoTimeout(300000);
			while (risultati.size() != numeroPartecipanti) {
				risultati.add(socketUDP.getMessage());
			}

		} catch (IOException e) {
			System.out.println("Timeout udp receive");
		}

		risultatiPartita(risultati, multicast, listMsg);

	}

	/**
	 * Calcola i risultati e inserisce la classifica nella lista del multicast.
	 * Aggiorna il punteggio generale.
	 * 
	 * @param risultati vettore contenente le stringhe ricevute dai client
	 * @param multicast thread multicast
	 * @param listMsg lista messaggi da passare al multicast
	 */
	private void risultatiPartita(ArrayList<String> risultati, Thread multicast, BlockingQueue<String> listMsg) {
		System.out.println("Partita"+partita.getId()+": Calcolo risultati ...");
		
		for (String r : risultati) {
			String[] parole = r.split("_");
			if (parole.length==1) continue;
			//inserisce le parole in un Set in modo da eliminare eventuali doppioni
			Set<String> listParole = new HashSet<>();
			for (int i = 1; i < parole.length; i++) {
				listParole.add(parole[i]);
			}
			partita.calcoloPunteggio(parole[0], listParole);
		}
		System.out.println("Partita"+partita.getId()+": invio risultati partita");
		// invia il punteggio e aggiorna i punteggi generali
		try {
	
			for (String u : partita.getPartecipanti()) {
				int punti = partita.getPunteggio(u);
				listMsg.put(u + ": " + punti);
				dati.utentiRegistrati.get(u).addPunteggio(punti);
			}
			listMsg.put("#");

			// Salva Punteggi
			try {
				Utente.serializeMap(dati.utentiRegistrati, dati.costanti.getFILE_UTENTI());
			} catch (IOException e) {
				System.err.println("Partita"+partita.getId()+": risultatiPartita: " + e.getMessage());
			}
			System.out.println("Partita"+partita.getId()+": I punteggi sono stato aggiornati");

			// attendi fine multicast
			multicast.join();
		} catch (InterruptedException e) {
			System.err.println("risultatiPartita: " + e.getMessage());
		}

	}

	/**
	 * Invia classifica generale al client tramite connessione TCP.
	 */
	private void visualizzaClassifica() {
		System.out.println(utente.getUserName() + " ha richiesto la classifica generale");

		try {
			ArrayList<String> classifica = new ArrayList<>();
			for (String u : dati.utentiRegistrati.keySet()) {
				classifica.add(u + ": " + dati.utentiRegistrati.get(u).getPunteggio());
			}

			Collections.sort(classifica);
			socketClient.sendObject(classifica);
			socketClient.close();
		} catch (IOException e) {
			System.err.println("visualizzaClassifica: Some error appared or client close connection");
			try {
				socketClient.close();
			} catch (IOException e1) {
				;
			}
			return;
		}
	}

	@Override
	public void run() {
		String comando = null;

		try {
			comando = socketClient.receiveMessage();
		} catch (IOException e) {
			System.err.println("TwistServerWorkerMain: Some error appared or client close connection");
			return;
		}

		if (comando.equals(dati.costanti.NUOVA_PARTITA)) {
			if (nuovaPartita())
				if (setupNuovaPartita()) {
					avvioPartita();
				}
		}

		if (comando.equals(dati.costanti.CLASSIFICA_GENERALE)) {
			visualizzaClassifica();
		}

	}
}

