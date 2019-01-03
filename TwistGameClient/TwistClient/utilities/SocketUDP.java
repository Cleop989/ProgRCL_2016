package utilities;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * The Class SocketUDP. Gestione di una socket UDP
 */
public class SocketUDP implements AutoCloseable {

	/** socket. */
	protected DatagramSocket socket;

	/** remote address. */
	protected InetSocketAddress remoteAddress;

	/** buffer size. */
	protected int bufferSize;

	/**
	 * Instantiates a new socket UDP (server)
	 *
	 * @param portServer porta sulla quale crea la socket
	 * @throws SocketException
	 *             if the socket could not be opened, or the socket could not
	 *             bind to the specified local port.
	 */
	public SocketUDP(int portServer) throws SocketException {
		socket = new DatagramSocket(portServer);
		bufferSize = 1024;
	}

	/**
	 * Instantiates a new socket UDP (client)
	 *
	 * @param ipServer indirizzo server
	 * @param portServer porta server
	 * @throws UnknownHostException
	 *             if no IP address for the host could be found, or if a
	 *             scope_id was specified for a global IPv6 address.
	 * @throws SocketException
	 *             if the socket could not be opened, or the socket could not
	 *             bind to the specified local port.
	 */
	public SocketUDP(String ipServer, int portServer) throws UnknownHostException, SocketException {
		socket = new DatagramSocket();
		remoteAddress = new InetSocketAddress(InetAddress.getByName(ipServer), portServer);
		bufferSize = 1024;
	}

	/**
	 * Chiude la socket.
	 */
	@Override
	public void close() {
		socket.close();
	}

	/**
	 * Riceve un messaggio dalla socket.
	 *
	 * @return messaggio ricevuto
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public String getMessage() throws IOException {
		byte[] recvBuff = new byte[bufferSize];
		DatagramPacket recvPacket = new DatagramPacket(recvBuff, bufferSize);
		socket.receive(recvPacket);
		recvBuff = recvPacket.getData();

		return new String(recvBuff, 0, recvBuff.length, "UTF-8");
	}

	/**
	 * Prende l'indirizzo dell'host remoto.
	 *
	 * @return stringa contenente l'indirizzo
	 */
	public String getRemoteAddress() {
		return remoteAddress.getAddress().getHostAddress();
	}

	/**
	 * Prende la porta dell'host remoto.
	 *
	 * @return numero di porta
	 */
	public int getRemotePort() {
		return remoteAddress.getPort();
	}

	/**
	 * Riceve una richiesta dalla socket. Aggiorna l'indirizzo dell'host remoto
	 *
	 * @return messaggio
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String getRequest() throws IOException {
		byte[] recvBuffer = new byte[bufferSize];
		DatagramPacket recvPacket = new DatagramPacket(recvBuffer, bufferSize);
		socket.receive(recvPacket);
		recvBuffer = recvPacket.getData();
		remoteAddress = new InetSocketAddress(recvPacket.getAddress(), recvPacket.getPort());

		return new String(recvBuffer, 0, recvBuffer.length);
	}

	/**
	 * Verifica se la socket è chiusa.
	 *
	 * @return true se la socket è stata chiusa
	 */
	public boolean isClosed() {
		return socket.isClosed();
	}

	/**
	 * Invia un messaggio all'host a cui la socket è connessa.
	 *
	 * @param message messaggio da inviare
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void sendMessage(String message) throws IOException {
		byte[] sendBuffer = new byte[bufferSize];
		sendBuffer = message.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, remoteAddress);
		socket.send(sendPacket);
	}

	/**
	 * Setta un timeout per la receive.
	 *
	 * @param timeout
	 *            the new so timeout
	 * @throws SocketException
	 *             if there is an error in the underlying protocol, such as an
	 *             UDP error.
	 */
	public void setSoTimeout(int timeout) throws SocketException {
		socket.setSoTimeout(timeout);

	}

}