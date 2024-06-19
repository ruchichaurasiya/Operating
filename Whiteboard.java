package src;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Whiteboard
{	
	//shapes in board
	public LinkedList<WhiteboardShape> shapes;
	
	//clients connected
	private LinkedList<Reference> clients;
	public String boardID;
	
	//new whiteboard
	public Whiteboard(String boardID)	
	{
		shapes = new LinkedList<WhiteboardShape>();
		clients = new LinkedList<Reference>();
		this.boardID = boardID;
	}
	
	public void createStartButton() {
        JFrame frame = new JFrame("Whiteboard Application");
        JButton startButton = new JButton("Start Client-Server Chat");

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ip = JOptionPane.showInputDialog("Enter Server IP:");
                String clientID = JOptionPane.showInputDialog("Enter Client ID:");

                if (ip == null || ip.isEmpty() || clientID == null || clientID.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "IP or Client ID cannot be empty!");
                    return;
                }

                // Start the server
                int port = 12345; // Change port number if needed
                ChatServer server = new ChatServer();
                Thread serverThread = new Thread(() -> server.startServer());
                serverThread.start();

                // Start the client
                ChatClient client = new ChatClient();
                client.setVisible(true);
            }
        });

        frame.getContentPane().add(startButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }
	
	//add a shape to the board
	public synchronized void add(WhiteboardShape s,Reference refInst)	
	{
		shapes.add(s);
		for(Reference r:clients)	//broadcast to all connected clients
		{
			if(r!=refInst)
				r.sendShape(s);
		}
	}
	
	//new client
	public void addClient(Reference refInst)	
	{
		clients.add(refInst);

	}
	
	//client diconnects
	public void removeClient(Reference refInst)	
	{
		clients.remove(refInst);
	}
	
}
