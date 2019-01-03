package twistClient.Client;

import java.io.FileNotFoundException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import twistClient.Interface.GestioneClientGUI;
import twistClient.Interface.TwistClientGUI;
import twistClient.utilities.Costanti;

/**
 * The Class TwistClientMain. Classe principale TwistGame client. Crea
 * l'interfaccia e si mette in acolto degli enventi
 */
public class TwistClientMain extends TwistClientGUI implements Runnable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4612762206048583036L;

	/** TwistGame client. */
	private TwistClientRMI client;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new TwistClientMain());
	}

	public TwistClientMain() {
		// crea la fiestra
		super();
	}

	@Override
	public void fatalError(String title, String message, boolean logout) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
		operazioniDiChiusura(logout);
	}

	@Override
	public void operazioniDiChiusura(boolean logout) {
		if (client != null) {
			if (logout)
				client.logout();
			client.clearRegistryRMI();
		}
		this.dispose();
		System.exit(0);
	}

	@Override
	public void run() {

		try {
			// inizializzo le costanti
			Costanti costanti = new Costanti();


			// crea il client
			client = new TwistClientRMI(this,costanti); // crea il client

			// EventListener
			exitButton.addActionListener(new GestioneClientGUI(null,null));
			loginButton.addActionListener(new GestioneClientGUI(client,costanti));
			recordButton.addActionListener(new GestioneClientGUI(client,costanti));
			logoutButton.addActionListener(new GestioneClientGUI(client,costanti));
			nuovaPartitaButton.addActionListener(new GestioneClientGUI(client,costanti));
			classificaGeneraleButton.addActionListener(new GestioneClientGUI(client,costanti));
			visualizzaInvitiButton.addActionListener(new GestioneClientGUI(client,costanti));
		} catch (FileNotFoundException e) {
			this.fatalError("FileNotFoundException", "File di inizializzazione non trovato", false);
		}
	}

}
