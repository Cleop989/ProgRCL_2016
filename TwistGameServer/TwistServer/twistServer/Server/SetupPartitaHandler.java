package twistServer.Server;


import java.io.IOException;
import java.net.ServerSocket;

import twistServer.utilities.Costanti;
import twistServer.utilities.Partita;
import utilities.SocketTCP;

/**
 * The Class SetupPartitaHandler. Gestione dell'inizializzazione della partita.
 * Attende le conferme o i rifiuti degli utenti. In caso di un rifiuto oche
 * tutti gli invitati abbiano accettato l'invito, viene chiusa la socketServer
 * per forsare la chiusura anticipata dell'accept. L'interruzione del thread
 * dopo 7 minuti è lasciata al chiamante.
 */
public class SetupPartitaHandler implements Runnable {

	/** utente che ha avviato la partita. */
	private String utente;

	/** dati della partita. */
	private Partita partita;

	/** SocketServer che attende le connessioni. */
	private ServerSocket server;

	/** socket client. */
	private SocketTCP socketClient;

	/** costanti. */
	private Costanti costanti;


	/**
	 * Atende la conferma o il rifiuto dell'invito e lo inizializza come
	 * partecipante alla partita.
	 *
	 * @param username utente connesso
	 * @param partita partita a cui è stato invitato
	 * @param server SocketServer che attende le connessioni
	 * @param client socket client aperta
	 * @param costanti costanti del client
	 */
	public SetupPartitaHandler(String username, Partita partita, ServerSocket server, SocketTCP client, Costanti costanti) {
		this.utente = username;
		this.partita = partita;
		this.server = server;
		this.socketClient=client;
		this.costanti=costanti;
	}

	@Override
	public void run() {
		try {
			String message = socketClient.receiveMessage();
			if (message.equals(costanti.INVITO_CONVERMATO)) {
				partita.addPartecipanti(utente,socketClient);
				System.out.println("Partita"+partita.getId()+": "+utente+" ha accettato la partita ");
				if (partita.isValida()) {
					// interrompe l'accept
					server.close();
				}
			} 
			
			if (message.equals(costanti.INVITO_RIFIUTATO)){
				// chiude la socket
				socketClient.close();
				System.out.println("Partita"+partita.getId()+": "+utente+" non ha accettato la partita ");
				// interrompe l'accept
				server.close();
			}

		} catch (IOException e) {
			;
		}

	}

}
