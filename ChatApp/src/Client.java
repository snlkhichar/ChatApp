
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame {

	Socket socket;
	BufferedReader br;
	PrintWriter out;

	// Declare Component
	private JLabel heading = new JLabel("Client Area");
	private JTextArea messageArea = new JTextArea();
	private JTextField messageInput = new JTextField();
	private Font font = new Font("Roboto", Font.PLAIN, 20);

	public Client() {
		try {
			System.out.println("Sending request to server");
			socket = new Socket("127.0.0.1", 7778);
			System.out.println("Connection done");
			createGUI();
			handleEvents();
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());

			startReading();
			// startWriting();
		} catch (Exception ex) {

		}
	}

	private void handleEvents() 
	{
		messageInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
				// System.out.println("key released :"+e.getKeyCode());
				if (e.getKeyCode() == 10) {
					// System.out.println("you have pressed enter button");
					String contentToSend = messageInput.getText();
					messageArea.append("Me :" + contentToSend + "\n");
					out.println(contentToSend);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
				}
			}
		});

	}

	private void createGUI() {
		this.setTitle("Client Messagner[END]");
		this.setSize(600, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// coding for component
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		// heading.setIcon(new ImageIcon("DB.JPG"));
		heading.setHorizontalTextPosition(SwingConstants.CENTER);
		heading.setVerticalTextPosition(SwingConstants.BOTTOM);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		messageArea.setEditable(false);
		messageInput.setHorizontalAlignment(SwingConstants.CENTER);

		// frame ka layout set krenge
		this.setLayout(new BorderLayout());
		// adding the components to frame
		this.add(heading, BorderLayout.NORTH);
		JScrollPane jScrollPane = new JScrollPane(messageArea);
		this.add(jScrollPane, BorderLayout.CENTER);
		this.add(messageInput, BorderLayout.SOUTH);
		this.setVisible(true);
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
						System.out.println("Server terminated the chat");
						JOptionPane.showMessageDialog(this, "Server Terminated the chat");
						messageInput.setEnabled(false);
						socket.close();
						break;
					}
					// System.out.println("Server :" + msg);
					messageArea.append("Server :" + msg + "\n");
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
				System.out.println("connection closed");
			}

		};

		new Thread(r1).start();
	}

	// start writing and send method
	public void startWriting() {
		System.out.println("writer started");
		// Multithreading
		// thread - data user lega and the send krega client tak
		Runnable r2 = () -> {
			try {
				// if socket is closed then don't write
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
			}

			catch (Exception e) {
				e.printStackTrace();
				System.out.println("connection is closed");
			}
			System.out.println("connection is closed");

		};

		new Thread(r2).start();
	}

	public static void main(String[] args) {
		System.out.println("Client");
		new Client();
	}

}
