package twistClient.Interface;

import javax.swing.JTextArea;

/**
 * The Class ShowMessage. Stampa un messaggio sull'interfaccia grafica nell'apposita area
 */
public class ShowMessage implements Runnable {

	/** messaggio */
	private String message;

	/** area dei messaggi */
	private JTextArea msgTextArea;

	/**
	 * Instantiates a new show message.
	 *
	 * @param message messaggio da visuzlizzare
	 * @param msgTextArea area dei messaggi
	 */
	public ShowMessage(String message, JTextArea msgTextArea) {
		this.message = message;
		this.msgTextArea = msgTextArea;
	}

	@Override
	public void run() {
		msgTextArea.append(message + "\n");

	}

}
