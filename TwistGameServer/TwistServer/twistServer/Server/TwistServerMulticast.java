package twistServer.Server;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.BlockingQueue;

/**
 * The Class TwistServerMulticast. Server multicast che invia i risultati della
 * partita agli utenti partecipanti
 */
public class TwistServerMulticast implements Runnable {

	/** Lista sincronizzata dei messaggi da inviare ai client registrati */
	private BlockingQueue<String> listMsg;

	/** ip del server multicast */
	private String ip;

	/** porta del server multicast. */
	private int port;

	/** id partita. */
	private int idPartita;

	/**
	 * Instantiates a new twist server multicast.
	 *
	 * @param listMsg Lista sincronizzata dei messaggi da inviare
	 * @param ip ip del server multicast
	 * @param port porta del server multicast
	 * @param idPartita id della partita in corso
	 */
	public TwistServerMulticast(BlockingQueue<String> listMsg, String ip, int port, int idPartita) {
		this.listMsg = listMsg;
		this.ip = ip;
		this.port = port;
		this.idPartita=idPartita;
	}

	@Override
	public void run() {
		System.out.println("Partita"+idPartita+": MulticastServer is ready on port " + port);
		try (MulticastSocket server = new MulticastSocket(port);) {
			server.setTimeToLive(1);
			server.setLoopbackMode(false);
			server.setReuseAddress(true);
			InetAddress multicastGroup = InetAddress.getByName(ip);
			String message = "";
			while (!Thread.interrupted()) {
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(byteStream);
				out.writeUTF(listMsg.take());
				out.writeLong(System.currentTimeMillis());
				byte[] data = byteStream.toByteArray();
				DatagramPacket packet = new DatagramPacket(data, data.length, multicastGroup, port);
				server.send(packet);
				if (message.equals("#"))
					break;
			}
		} catch (IOException e) {
			System.err.println("TwistServerMulticast - Some error appeared: " + e.getMessage());
		} catch (InterruptedException e) {
			System.err.println("TwistServerMulticast - This should not happen: " + e.getMessage());
		}

		System.out.println("Partita"+idPartita+": MulticastServer on port " + port + " is close");
	}
}
