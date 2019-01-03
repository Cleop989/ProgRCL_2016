package twistServer.Server;


import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import twistServer.utilities.DatiTwistGame;
import twistServer.utilities.Utente;
import utilities.SocketTCP;

/**
 * The Class TwistServerTCPaccept. Accetta le connessioni TCP dai
 * client per le richieste di nuova partita e classifica generale
 */
public class TwistServerTCPaccept implements Runnable {

	/** Dati del server. */
	private DatiTwistGame dati;

	/**
	 * Instantiates a new twist server TCP accept.
	 *
	 * @param dati dati del server
	 */
	public TwistServerTCPaccept(DatiTwistGame dati) {
		this.dati = dati;
	}

	@Override
	public void run() {
		ExecutorService es = Executors.newCachedThreadPool();

		try (ServerSocket server = new ServerSocket(dati.costanti.getPORTA_INIT());) {
			System.out.println("ServerTCP Started on Port Number " + dati.costanti.getPORTA_INIT() + " from "
					+ dati.costanti.getIP_SERVER());
			while (true) {
				SocketTCP client = new SocketTCP(server);

				// identifica cliente e inizializza la socket in utenti attivi
				// si assume che il client abbia gia' effettuato il login
				String username = client.receiveMessage();
				Utente user = dati.utentiRegistrati.get(username);
				TwistServerHandler task = new TwistServerHandler(user, dati,client);
				es.submit(task);
			}
		} catch (IOException e) {
			System.err.println("serverTCPaccept: " + e.getMessage());
		} finally {
			es.shutdown();
		}

	}

}
