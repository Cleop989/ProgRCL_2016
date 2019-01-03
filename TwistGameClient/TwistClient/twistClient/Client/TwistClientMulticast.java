package twistClient.Client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import twistClient.Interface.TwistClientGUI;

/**
 * The Class TwistClientMulticast. Client multicast che attende i risultati
 * della partita in corso
 */
public class TwistClientMulticast implements Runnable {

	/** ip server multicast */
	private String ipServer;

	/** porta del server multicast */
	private int port;

	/** interfaccia grafica. */
	private TwistClientGUI gui;

	/**
	 * Instantiates a new twist client multicast.
	 *
	 * @param ipServer ip server multicast
	 * @param port porta server multicast
	 * @param gui interfaccia grafica
	 */
	public TwistClientMulticast(String ipServer, int port, TwistClientGUI gui) {
		this.ipServer = ipServer;
		this.port = port;
		this.gui = gui;
	}

	@Override
	public void run() {
		ArrayList<String> risultati = new ArrayList<>();
		try (MulticastSocket client = new MulticastSocket(port);) {
			InetAddress multicastGroup = InetAddress.getByName(ipServer);
			client.joinGroup(multicastGroup);
			DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
			String header;
			while (!Thread.interrupted()) {
				client.receive(packet);
				DataInputStream in = new DataInputStream(
						new ByteArrayInputStream(packet.getData(), packet.getOffset(), packet.getLength()));
				header = in.readUTF();
				if (header.equals("#"))
					break;
				risultati.add(header);
			}
		} catch (IOException e) {
			gui.fatalError("MulticastClient", e.getMessage(), true);
		}

		// visualizza i risultati sottoforma di finestra
		StringBuilder res = new StringBuilder();
		for (String s : risultati) {
			res.append(s + "\n");
		}
		JOptionPane.showMessageDialog(gui, res.toString(),"Risultati partita",JOptionPane.INFORMATION_MESSAGE);
	}
}
