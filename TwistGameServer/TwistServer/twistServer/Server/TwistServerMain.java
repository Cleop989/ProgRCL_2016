package twistServer.Server;


import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import twistServer.utilities.DatiTwistGame;
import utilities.TwistServerInterface;

/**
 * The Class TwistServerMain. Fa il setup del server RMI e fa parite il thread
 * per accettare le richieste dei client (TCP). 
 */
/*
 * Il server deve essere avviato da terminale con i comandi
 *  javaw rmiregistry 
 *  java -jar TwistGameServer.jar
 */
public class TwistServerMain {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();
		try {
			DatiTwistGame dati = new DatiTwistGame();

			setupServerRMI(dati);

			/**
			 * Attende le connessioni TCP dai client per le operazioni di nuova
			 * partita e visualizza classifica generale. Prevede che il client
			 * si identifichi inviado il nome utente dopo aver effettuato la
			 * connessione.
			 */
			es.submit(new TwistServerTCPaccept(dati));

		} catch (FileNotFoundException e) {
			System.err.println("TwistServerMain: File inizializzazione non trovato");
			return;
		}

		es.shutdown();
	}
	

	/**
	 * Inizializza e crea il server che accetta le richieste dei client in RMI.
	 * Gestisce le operazioni di login/logout e registrazione
	 * 
	 * @param dati Insieme dei dati necessari al funzionamento del server
	 */
	private static void setupServerRMI(DatiTwistGame dati) {
		try {
			// export object
			TwistServerInterface managerStub = (TwistServerInterface) UnicastRemoteObject
					.exportObject(new TwistServerRMI(dati), 0);

			// register to RMI
			Registry registry = LocateRegistry.createRegistry(dati.costanti.getREGISTRY_PORT());
			registry.rebind(TwistServerInterface.OBJECT_NAME, managerStub);

			System.out.println("ServerRMI Finished setup");
		} catch (RemoteException e) {
			System.err.println("TwistGameMainRMI: Si è verificato un errore nel setup de server RMI");
			System.exit(0);
		}
	}

}
