package twistClient.Client;

import twistClient.Interface.GestioneInputDiaolg;
import twistClient.Interface.InputDialog;
import twistClient.Interface.TwistClientGUI;
import twistClient.utilities.ListString;

/**
 * The Class TakeParole. Finestra che gestisce il gioco. Visualizza le lettere
 * della partita e permette di inserire le parole. Crea la finestra e gli action
 * listener
 */
public class TakeParole extends InputDialog {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2501252665721163110L;

	/**
	 * Instantiates a new take parole.
	 *
	 * @param parent finestra proprietaria
	 * @param parole strutture per salvare le parole inserite
	 * @param lettere lettere della partita
	 */
	public TakeParole(TwistClientGUI parent, ListString parole, String lettere) {
		super(parent, lettere);
		aggiungi.setActionCommand("AggiungiParola");
		inputField.setActionCommand("AggiungiParola");
		// EventListener
		aggiungi.addActionListener(new GestioneInputDiaolg(parole, messageArea, inputField));
		inputField.addActionListener(new GestioneInputDiaolg(parole, messageArea, inputField));
		chiudi.addActionListener(new GestioneInputDiaolg(null, null, null));
	}
}
