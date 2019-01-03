package twistClient.Interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import twistClient.utilities.ListString;

public class GestioneInputDiaolg implements ActionListener {

	/** lista avversari o parole. */
	private ListString list;

	/** area messaggi. */
	private JTextArea messageArea;

	/** campo di input. */
	private JTextField inputField;

	/**
	 * Instantiates a new gestione input diaolg.
	 *
	 * @param list lista avversari o parole
	 * @param messageArea area messaggi
	 * @param inputField campo di input
	 */
	public GestioneInputDiaolg(ListString list, JTextArea messageArea, JTextField inputField) {
		this.list = list;
		this.messageArea = messageArea;
		this.inputField = inputField;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.equals("Chiudi"))
			((JDialog) ((JComponent) e.getSource()).getTopLevelAncestor()).dispose();
		else if (command.equals("AggiungiParola")){
			inputField.requestFocus();
			String parola = inputField.getText();
			list.addString(parola);
			messageArea.append(parola + "\n");
			inputField.setText("");
		}
		else if (command.equals("AggiungiAvversario")){
			inputField.requestFocus();
			String avversario = inputField.getText();
			if (avversario.length() < 3) {
				return;
			}
			list.addString(avversario);
			messageArea.append(avversario + "\n");
			inputField.setText("");
		}
	}

}
