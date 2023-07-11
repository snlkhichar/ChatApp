import java.net.*;
import java.io.*;

public class Server {

	ServerSocket server;
	Socket socket;
	// Reading the data using br
	BufferedReader br;
	// Writing the data using
	PrintWriter out;

	public Server() {
		try {

			server = new ServerSocket(7778);
			System.out.println("Server is ready to accept connection");
			System.out.println("Waiting.......");
			socket = server.accept();

			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());

			startReading();
			startWriting();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startReading() {
		// Multithreading
		// thread - data read krke dega
		Runnable r1 = () -> {
			System.out.println("reader started");
			try {
				while (true) {
					String msg = br.readLine();
					if (msg.equals("exit")) {
						System.out.println("Client terminated the chat");
						socket.close();
						break;
					}
					System.out.println("Client :" + msg);
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
				System.out.println("connection is closed");
			}

		};

		new Thread(r1).start();
	}

	public void startWriting() {
		System.out.println("writer started");
		// Multithreading
		// thread - data user lega and the send krega client tak
		Runnable r2 = () -> {
			try {
				while (!socket.isClosed()) {
					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					String content = br1.readLine();
					out.println(content);
					out.flush();
					if (content.equals("exit")) {
						socket.close();
						break;
					}
				}
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("Connection closed");
			}

		};

		new Thread(r2).start();
	}

	public static void main(String[] args) {

		System.out.println("Hello Server");

		new Server();
	}

}
