package twistClient.Interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import twistClient.Client.GestionePartita;
import twistClient.Client.NuovaPartita;
import twistClient.Client.TwistClientRMI;
import twistClient.Client.VisualizzaClassifica;
import twistClient.utilities.Costanti;

public class GestioneClientGUI implements ActionListener {

	/** client. */
	private TwistClientRMI client;

	/** costanti. */
	private Costanti costanti;

	/**
	 * Instantiates a new action listener gestione client GUI.
	 *
	 * @param client
	 *            il client
	 * @param costanti
	 */
	public GestioneClientGUI(TwistClientRMI client, Costanti costanti) {
		this.client = client;
		this.costanti = costanti;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		TwistClientGUI gui = (TwistClientGUI) ((JButton) e.getSource()).getTopLevelAncestor();
		String command = e.getActionCommand();
		switch (command) {

		case "Exit": // ActionListenere bottone "Exit"
			gui.operazioniDiChiusura(true);
			break;

		case "Login": // ActionListenere bottone "Login"
			String userName = gui.getUserName();
			if (userName.length() < 3) {
				JOptionPane.showMessageDialog(gui, "Inserire nome utente");
				break;
			}

			String password = trasformaPassword(gui.getPassword());

			if (this.client.login(userName, password)) {
				gui.setButtonLogin(false);
				gui.setButtonGameTrue();
			}
			break;

		case "Logout": // ActionListenere bottone "Logout"
			if (this.client.logout()) {
				gui.setButtonLogin(true);
				gui.setButtonGameFalse();
			}
			break;

		case "Registrati": // ActionListenere bottone "Registrati"
			String username = gui.getUserName();
			if (username.length() < 3) {
				JOptionPane.showMessageDialog(gui, "Inserire nome utente");
				break;
			}

			String pass = trasformaPassword(gui.getPassword());

			this.client.registrati(username, pass);
			break;

		case "ClassificaGenerale": // ActionListenere bottone "ClassificaGenerale"
			Thread visualizza = new Thread(new VisualizzaClassifica(gui, costanti, client.getUsername()));
			visualizza.start();
			break;

		case "NuovaPartita": // ActionListenere bottone "NuovaPartita"
			Thread nuovaPartita = new Thread(new NuovaPartita(gui, costanti, client.getUsername()));
			nuovaPartita.start();
			break;

		case "VisualizzaInviti": // ActionListenere bottone "VisualizzaInviti"
			Thread setup = new Thread(new GestionePartita(gui, costanti, client.getUsername(),client.getInvitiRicevuti()));
			setup.start();
			break;
		default:
			break;
		}

	}

	/**
	 * Trasforma il formato della password dell'interfaccia in una stringa.
	 *
	 * @param pwd
	 *            password presa dall'interfaccia
	 * @return la password sottoforma di stringa
	 */
	private String trasformaPassword(char[] pwd) {
		StringBuilder sb = new StringBuilder();
		int n = pwd.length;
		for (int i = 0; i < n; i++) {
			sb.append(pwd[i]);
		}

		return sb.toString();
	}
}
