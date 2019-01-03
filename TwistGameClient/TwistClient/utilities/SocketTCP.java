package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * The Class SocketTCP. Gestione di una socket TCP
 */
public class SocketTCP implements AutoCloseable {

	/** Costante END_MESSAGE. */
	private static final String END_MESSAGE = "$$$";

	/** socket. */
	protected Socket socket = null;

	/** BufferedReader. */
	private BufferedReader reader;

	/** BufferedWriter. */
	private BufferedWriter writer;

	/** object reader. */
	private ObjectInputStream objectReader;

	/** object writer. */
	private ObjectOutputStream objectWriter;

	/**
	 * Accetta le connessioni da SocketServer.
	 *
	 * @param serverSocket ServerSocket
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public SocketTCP(ServerSocket serverSocket) throws IOException {
		socket = serverSocket.accept();
		openStrams();
	}

	/**
	 * Crea una socket ed effettua la connessione al server.
	 *
	 * @param ipServer ip server
	 * @param port porta su cui collegarsi
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws UnknownHostException
	 *             if the IP address of the host could not be determined.
	 */
	public SocketTCP(String ipServer, int port) throws IOException, UnknownHostException {
		socket = new Socket(ipServer, port);
		openStrams();
	}

	/**
	 * Chiude la connesione della soket.
	 *
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	@Override
	public void close() throws IOException {
		writer.close();
		reader.close();
		objectWriter.close();
		objectReader.close();
		socket.close();
	}

	/**
	 * Crea gli stream che usa la classe.
	 *
	 * @throws IOException
	 *             if an I/O error occurs when creating the input/output stream,
	 *             the socket is closed, the socket is not connected, or the
	 *             socket input has been shutdown using shutdownInput(), or
	 *             while reading /writing stream header
	 */
	private void openStrams() throws IOException {
		OutputStream outputStream = socket.getOutputStream();
		writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		objectWriter = new ObjectOutputStream(outputStream);

		InputStream inputStream = socket.getInputStream();
		reader = new BufferedReader(new InputStreamReader(inputStream));
		objectReader = new ObjectInputStream(inputStream);
	}

	/**
	 * Attende un array di messaggi sulla socket.
	 *
	 * @return messaggi ricevuti sottoforma di ArrayList
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public ArrayList<String> receiveArray() throws IOException {
		ArrayList<String> array = new ArrayList<>();
		String message = "";
		while (!(message = receiveMessage()).equals(END_MESSAGE)) {
			array.add(message);
		}
		return array;
	}

	/**
	 * Attende un messaggio sulla socket.
	 *
	 * @return messaggio ricevuto
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public String receiveMessage() throws IOException {
		String message = reader.readLine();
		return message;
	}

	/**
	 * Riceve un oggetto tramite la socket.
	 *
	 * @param <E> the element type
	 * @return oggetto ricevuto
	 * @throws IOException
	 *             if an I/O error occurs while reading stream header
	 * @throws ClassNotFoundException
	 *             Class of a serialized object cannot be found.
	 */
	@SuppressWarnings("unchecked")
	public <E> E receiveObject() throws IOException, ClassNotFoundException {
		return (E) objectReader.readObject();
	}

	/**
	 * Invia un array di stringhe tramite la socket.
	 *
	 * @param array stringhe da inviare
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public void sendArray(ArrayList<String> array) throws IOException {
		for (String message : array) {
			sendMessage(message);
		}
		sendMessage(END_MESSAGE);
	}

	/**
	 * Invia un messaggio sulla socket.
	 *
	 * @param msg messaggio da inviare
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public void sendMessage(String msg) throws IOException {
		writer.write(msg + "\r\n");
		writer.flush();
	}

	/**
	 * Invia un oggetto tramite la socket.
	 *
	 * @param <E> the element type
	 * @param obj oggetto da inviare
	 * @throws IOException
	 *             Any exception thrown by the underlying OutputStream.
	 */
	public <E> void sendObject(E obj) throws IOException {
		objectWriter.writeObject(obj);
		objectWriter.flush();
	}

}
