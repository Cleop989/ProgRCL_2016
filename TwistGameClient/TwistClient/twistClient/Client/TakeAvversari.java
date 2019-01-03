package twistClient.Client;

import twistClient.Interface.GestioneInputDiaolg;
import twistClient.Interface.InputDialog;
import twistClient.Interface.TwistClientGUI;
import twistClient.utilities.ListString;

/**
 * The Class TakeAvversari. Finestra per inserire gli avversari da invitare.
 * Crea la finestra e gli action listener
 */
public class TakeAvversari extends InputDialog {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 9117514684097972461L;

	/**
	 * Instantiates a new take avversari.
	 *
	 * @param parent finestra proprietaria
	 * @param avversari struttura per salvare gli avversari inseriti
	 */
	public TakeAvversari(TwistClientGUI parent, ListString avversari) {
		super(parent, "Inserisci avversari");
		aggiungi.setActionCommand("AggiungiAvversario");
		inputField.setActionCommand("AggiungiAvversario");
		// ActionListener
		aggiungi.addActionListener(new GestioneInputDiaolg(avversari, messageArea, inputField));
		inputField.addActionListener(new GestioneInputDiaolg(avversari, messageArea, inputField));
		chiudi.addActionListener(new GestioneInputDiaolg(null,null,null));
	}

}
