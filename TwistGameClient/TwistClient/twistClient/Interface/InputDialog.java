package twistClient.Interface;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * The Class InputDialog. Crea una finestra per gestire input di più stringhe.
 * Permette la visualizzazione delle stringhe già inserite.
 */
public abstract class InputDialog extends JDialog {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 9117514684097972461L;

	/** campo input. */
	protected JTextField inputField;

	/** area messaggi. */
	protected JTextArea messageArea;

	/** bottone aggiungi. */
	protected JButton aggiungi;

	/** bottone chiudi. */
	protected JButton chiudi;

	/**
	 * Instantiates a new input dialog.
	 *
	 * @param parent finestra proprietaria
	 * @param message messaggio da visualizzare
	 */
	public InputDialog(TwistClientGUI parent, String message) {
		super(parent, "TwistGame input", true);

		Container main = this.getContentPane();
		main.setLayout(new BorderLayout());

		EmptyBorder bordo = new EmptyBorder(10, 10, 10, 10);

		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout());
		main.add(bottom, BorderLayout.SOUTH);

		JPanel center = new JPanel();
		center.setLayout(new BorderLayout());
		main.add(center, BorderLayout.CENTER);

		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		main.add(top, BorderLayout.NORTH);

		aggiungi = new JButton("Aggiungi");
		bottom.add(aggiungi);

		chiudi = new JButton("Chiudi");
		bottom.add(chiudi);

		JLabel msg = new JLabel(message);
		msg.setHorizontalAlignment((int) CENTER_ALIGNMENT);
		top.add(msg, BorderLayout.NORTH);

		inputField = new JTextField();
		inputField.setBorder(bordo);
		center.add(inputField, BorderLayout.SOUTH);

		messageArea = new JTextArea(5, 50);
		messageArea.setEditable(false);
		JScrollPane avversariPane = new JScrollPane(messageArea);
		avversariPane.setBorder(bordo);
		center.add(avversariPane, BorderLayout.CENTER);

		this.pack();
		this.setLocationRelativeTo(parent);

		inputField.requestFocus();
	}

}
