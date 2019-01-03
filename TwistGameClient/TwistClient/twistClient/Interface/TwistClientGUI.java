package twistClient.Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

/**
 * The Class TwistClientGUI. Classe astratta che fornisce i metodi per la
 * creazione e la gestione dell'interfaccia grafica del client dove possibile, o
 * rimanda la loro implemetazione alla classe che la estende.
 */
public abstract class TwistClientGUI extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3575556302733343738L;

	/** login button. */
	protected JButton loginButton;

	/** record button. */
	protected JButton recordButton;

	/** nuova partita button. */
	protected JButton nuovaPartitaButton;

	/** logout button. */
	protected JButton logoutButton;

	/** exit button. */
	protected JButton exitButton;

	/** classifica generale button. */
	protected JButton classificaGeneraleButton;

	/** visualizza inviti button. */
	protected JButton visualizzaInvitiButton;

	/** campo username. */
	protected JTextField usernameField;

	/** campo password. */
	protected JPasswordField passField;

	/** area dei messaggi. */
	protected JTextArea msgTextArea;

	/**
	 * Crea la finestra.
	 */
	public TwistClientGUI() {
		EmptyBorder bordoPannello = new EmptyBorder(5, 5, 5, 5);
		EmptyBorder bordoBottone = new EmptyBorder(10, 10, 10, 10);

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception evt) {
			;
		}

		setDefaultFontSize();

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				operazioniDiChiusura(true);
			}
		});
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.setTitle("Text-Twist Game");

		Container pane = this.getContentPane();
		JPanel main = new JPanel();
		main.setLayout(new BorderLayout());
		main.setBorder(new EmptyBorder(15, 15, 15, 15));
		pane.add(main);

		// Crea la toolbar contenente i bottoni per le operazioni
		JPanel operazioni = new JPanel();
		operazioni.setLayout(new BorderLayout());

		JPanel usernamePanel = new JPanel();
		usernamePanel.setLayout(new BorderLayout());
		JLabel usernameLabel = new JLabel("Username: ");
		usernameField = new JTextField();
		usernamePanel.add(usernameLabel, BorderLayout.WEST);
		usernamePanel.add(usernameField, BorderLayout.CENTER);

		JPanel passwordPanel = new JPanel();
		passwordPanel.setLayout(new BorderLayout());
		JLabel passwordLabel = new JLabel("Password:  ");
		passField = new JPasswordField();
		passwordPanel.add(passwordLabel, BorderLayout.WEST);
		passwordPanel.add(passField, BorderLayout.CENTER);

		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setBackground(Color.gray);

		loginButton = new JButton("Login");
		loginButton.setBorder(bordoBottone);

		exitButton = new JButton("Exit");
		exitButton.setBorder(bordoBottone);

		recordButton = new JButton("Registrati");
		recordButton.setBorder(bordoBottone);

		nuovaPartitaButton = new JButton("NuovaPartita");
		nuovaPartitaButton.setBorder(bordoBottone);

		classificaGeneraleButton = new JButton("ClassificaGenerale");
		classificaGeneraleButton.setBorder(bordoBottone);

		visualizzaInvitiButton = new JButton("VisualizzaInviti");
		visualizzaInvitiButton.setBorder(bordoBottone);

		logoutButton = new JButton("Logout");
		logoutButton.setBorder(bordoBottone);

		toolbar.add(exitButton);
		toolbar.addSeparator();
		toolbar.addSeparator();
		toolbar.add(loginButton);
		toolbar.addSeparator();
		toolbar.add(logoutButton);
		toolbar.addSeparator();
		toolbar.add(recordButton);
		toolbar.addSeparator();
		toolbar.addSeparator();
		toolbar.add(nuovaPartitaButton);
		toolbar.addSeparator();
		toolbar.add(classificaGeneraleButton);
		toolbar.addSeparator();
		toolbar.add(visualizzaInvitiButton);

		operazioni.add(toolbar, BorderLayout.NORTH);
		operazioni.add(usernamePanel, BorderLayout.CENTER);
		operazioni.add(passwordPanel, BorderLayout.SOUTH);

		logoutButton.setEnabled(false);
		nuovaPartitaButton.setEnabled(false);
		classificaGeneraleButton.setEnabled(false);
		visualizzaInvitiButton.setEnabled(false);
		operazioni.setBorder(bordoPannello);
		main.add(operazioni, BorderLayout.NORTH);

		// Crea il pannello contenete l'area di testo per i messaggi del server
		// e le parole inserire dall'utente
		JPanel AreeMessaggi = new JPanel();
		AreeMessaggi.setLayout(new BorderLayout());

		msgTextArea = new JTextArea(10, 0);
		msgTextArea.setEditable(false);
		JScrollPane messaggi = new JScrollPane(msgTextArea);

		AreeMessaggi.add(messaggi, BorderLayout.CENTER);
		AreeMessaggi.setBorder(bordoPannello);
		main.add(AreeMessaggi, BorderLayout.CENTER);

		// setta le dimensioni della finestra automaticamente secondo le
		// dimensioni dei componenti
		this.pack();
		// centra la finestra nello schermo
		this.setLocationRelativeTo(null);

		this.setVisible(true);

		usernameField.requestFocus();

	}

	/**
	 * Clear login.
	 */
	public void clearLogin() {
		usernameField.setText("");
		passField.setText("");
	}
	
	public void clearMessageArea(){
		msgTextArea.setText("");
	}

	/**
	 * Lancia un messaggio di errore e chiude il programma, eventualmente
	 * effettuando il logout.
	 *
	 * @param title titolo della finestra
	 * @param message messaggio d'errore
	 * @param logout true se deve provare a fare il logout
	 */
	public abstract void fatalError(String title, String message, boolean logout);

	/**
	 * Prende il contenuto del campo password.
	 *
	 * @return password
	 */
	public char[] getPassword() {
		return passField.getPassword();
	}

	/**
	 * Prende il contenuto del campo UserName.
	 *
	 * @return Nome utente
	 */
	public String getUserName() {
		return usernameField.getText();
	}

	/**
	 * Che cose si deve fare quando viene chiuso il client.
	 *
	 * @param logout true se deve provare a fare il logout
	 */
	public abstract void operazioniDiChiusura(boolean logout);

	/**
	 * Scrive un messaggio nell'area di testo apposita.
	 *
	 * @param message
	 *            messaggio da scrivere
	 */
	public void printMessage(String message) {
		SwingUtilities.invokeLater(new ShowMessage(message, msgTextArea));
	}

	/**
	 * Disabilita i bottoni nuovaPartita, logout, classificaGenerale,
	 * visualizzaInviti.
	 */
	public void setButtonGameFalse() {
		logoutButton.setEnabled(false);
		nuovaPartitaButton.setEnabled(false);
		classificaGeneraleButton.setEnabled(false);
		visualizzaInvitiButton.setEnabled(false);
	}

	/**
	 * Abilita i bottoni nuovaPartita, Logout, classificaGenerale e
	 * visualizzaInviti.
	 */
	public void setButtonGameTrue() {
		logoutButton.setEnabled(true);
		nuovaPartitaButton.setEnabled(true);
		classificaGeneraleButton.setEnabled(true);
		visualizzaInvitiButton.setEnabled(true);

	}

	/**
	 * Abilita/Disabilita bottoni login e registrati e i campi username e
	 * password.
	 *
	 * @param b true per abilitare i bottoni, false per disabilitarli
	 */
	public void setButtonLogin(boolean b) {
		loginButton.setEnabled(b);
		recordButton.setEnabled(b);
		usernameField.setEditable(b);
		passField.setEditable(b);

	}

	/**
	 * Imposta i bottoni in modo da poter effettuare solo il logout o uscire dal
	 * client.
	 */
	public void setButtonLoguotObbligatorio() {
		logoutButton.setEnabled(true);
		loginButton.setEnabled(false);
		recordButton.setEnabled(false);
		usernameField.setEditable(false);
		passField.setEditable(false);
	}

	/**
	 * Setta la grandezza del font in base alla risoluzione video.
	 */
	private void setDefaultFontSize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
		int fontSize = (int) Math.round(12.0 * screenRes / 76.0);

		Set<Object> keySet = UIManager.getLookAndFeelDefaults().keySet();
		Object[] keys = keySet.toArray(new Object[keySet.size()]);

		for (Object key : keys) {
			if (key != null && key.toString().toLowerCase().contains("font")) {
				Font font = UIManager.getDefaults().getFont(key);
				if (font != null) {
					font = font.deriveFont((float) fontSize);
					UIManager.put(key, font);
				}
			}
		}
	}

	/**
	 * Abilita/Disabilita bottoni nuovaParita e visualizzaInviti.
	 *
	 * @param b true attiva il bottone, false altrimenti
	 */
	public void setPartitaInCorso(boolean b) {
		nuovaPartitaButton.setEnabled(!b);
		visualizzaInvitiButton.setEnabled(!b);
	}

}
