package twistServer.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

/**
 * The Class Costanti. Classe contenente le costanti di TwistGame. Viene
 * inizializzata predendo i parametri dal file "file/Costanti.ini", dove le
 * informazioni sono salvate nel formato NOME=VALORE
 */
public class Costanti {
	// stringhe per la comunicazione tra client e server.
	// essendo costanti necessarie al funzionamento interno del server e del
	// client non vengono inizializzate tramite il file Costanti.ini

	/** nuova partita. */
	public final String NUOVA_PARTITA = "nuova_partita";

	/** classifica generale. */
	public final String CLASSIFICA_GENERALE = "classifica_generale";

	/** partita annullata. */
	public final String PARTITA_ANNULLATA = "partita_annullata";

	/** conferma invio inviti. */
	public final String CONFERMA_INVIO_INVITI = "conferma_invio_inviti";

	/** invito convermato. */
	public final String INVITO_CONVERMATO = "conferma_invito";

	/** invito rifiutato. */
	public final String INVITO_RIFIUTATO = "rifiuto_invito";

	
	// costanti inizializzate tramite il file COstanti.ini
	/** ip server. */
	private String IP_SERVER;

	/** ip multicast. */
	private String IP_MULTICAST;

	/** registry port. */
	private int REGISTRY_PORT;

	/** porta init. */
	private int PORTA_INIT;

	// costatanti del server. 
	
	/** percorso file. */
	private String PATH;

	/** file utenti. */
	private String FILE_UTENTI;

	/** dizionario. */
	private String DICTIONARY;

	/**
	 * Instantiates a new costanti.
	 *
	 * @throws FileNotFoundException
	 *             if the file does not exist, is a directory rather than a
	 *             regular file, or for some other reason cannot be opened for
	 *             reading.
	 */
	public Costanti() throws FileNotFoundException {
		try {
			Properties p = new Properties();

			p.load(new FileInputStream("file/Costanti.ini"));

			IP_SERVER = InetAddress.getLocalHost().getHostAddress();
			IP_MULTICAST = p.getProperty("IP_MULTICAST");
			REGISTRY_PORT = Integer.parseInt(p.getProperty("REGISTRY_PORT"));
			PORTA_INIT = Integer.parseInt(p.getProperty("PORTA_INIT"));

			PATH = p.getProperty("PATH");
			FILE_UTENTI = PATH + p.getProperty("FILE_UTENTI");
			DICTIONARY = PATH + p.getProperty("DICTIONARY");
		} catch (IOException e) {
			;
		}

	}

	// metodi get per impedire la modifica delle costanti non file

	/**
	 * Gets the dictionary.
	 *
	 * @return DICTIONARY
	 */
	public String getDICTIONARY() {
		return DICTIONARY;
	}

	/**
	 * Gets the file utenti.
	 *
	 * @return FILE_UTENTI
	 */
	public String getFILE_UTENTI() {
		return FILE_UTENTI;
	}

	/**
	 * Gets the ip multicast.
	 *
	 * @return IP_MULTICAST
	 */
	public String getIP_MULTICAST() {
		return IP_MULTICAST;
	}

	/**
	 * Gets the ip server.
	 *
	 * @return IP_SERVER
	 */
	public String getIP_SERVER() {
		return IP_SERVER;
	}

	/**
	 * Gets the path.
	 *
	 * @return PATH
	 */
	public String getPATH() {
		return PATH;
	}

	/**
	 * Gets the porta init.
	 *
	 * @return PORTA_INIT
	 */
	public int getPORTA_INIT() {
		return PORTA_INIT;
	}

	/**
	 * Gets porta per multicast.
	 *
	 * @param id id partita
	 * @return porta del multicast per la partita
	 */
	public int getPortaMulticast(int id){
		return PORTA_INIT +id +1;
	}
	
	/**
	 * Gets porta partita.
	 *
	 * @param id id partita
	 * @return porta per la partita
	 */
	public int getPortaPartita(int id){
		return PORTA_INIT + id;
	}
	
	/**
	 * Gets the registry port.
	 *
	 * @return REGISTRY_PORT
	 */
	public int getREGISTRY_PORT() {
		return REGISTRY_PORT;
	}
	
	
	
	

}
