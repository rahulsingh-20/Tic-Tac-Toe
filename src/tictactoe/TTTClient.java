package tictactoe;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class TTTClient extends JFrame implements Runnable {
	
	public JTextArea serverMsgArea;
	public Square[][] board;
	public Socket clientSocket;
	public Scanner input;
	public Formatter output;
	private String myMark; // this client's mark
    private boolean myTurn; // determines which client's turn it is
    private final String X_MARK = "X"; // mark for first client
    private final String O_MARK = "O"; // mark for second client
	
	public TTTClient() {		
		createGUI();
		startClient();
		
	}
	
	public void startClient() {
		try {
			clientSocket = new Socket("localhost", 12345);
			
			input = new Scanner(clientSocket.getInputStream());
		    output = new Formatter(clientSocket.getOutputStream());
		} catch(IOException e){
			e.printStackTrace();
		}
		
		
		// create and start worker thread for this client
//	    ExecutorService worker = Executors.newFixedThreadPool(1);
//	    worker.execute(this); // execute client
		
//		output.format("hello\n");
//		output.flush();
	}
	
	public void createGUI() {
		
		JPanel boardPanel = new JPanel();
		
		boardPanel.setLayout(new GridLayout(3,3));
		
		board = new Square[3][3];
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[i].length; j++) {
				Square square = new Square(3*i + j);
				square.setLabel(i+" "+j);
				board[i][j] = square;
				boardPanel.add(square);
			}
		}
		

		add(boardPanel);
		
		serverMsgArea = new JTextArea(5, 5);
		serverMsgArea.setLineWrap(true);
		
		add(new JScrollPane(serverMsgArea), BorderLayout.SOUTH);
		
		setSize(300,300);
//		setTitle("Player X");
		setVisible(true);
	}
	
//	private void sendClickedSquare() {
//		
//		System.out.println("TTTClient: inside sendClickedSquare client, location: ");
//		
//		output.format("%d\n", 2);
//		output.flush();
//	}

	private class Square extends JButton {
		
		private int location;
		
		private Square(int squareLocation) {
			
			location = squareLocation;
			
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
//					setLabel(myMark);
					System.out.println("TTTClient: mouse click thread: "+Thread.currentThread().getName());

					output.format("%d\n", location);
					output.flush();
					
					//sendClickedSquare();
					System.out.println("TTTClient: just after sendClickedSquare");
//					output.format("%s %d\n", myMark, location);
				}
			});
		}
		
		
	}
	
	public void processMessage(String message) {
		System.out.println("TTTClient: inside processMessage");
		
		int location, row, col;
		
		if(message.equals("Player: Waiting for Player O") || message.equals("Player: you are player o, x's turn")) {
			System.out.println("TTTClient:************************* "+message);
			
			SwingUtilities.invokeLater(() -> {
				serverMsgArea.append(message+"\n");
			});
		}
		
		else {
			System.out.println("TTTClient:************************* "+message);
			switch(message) {
			case "Move":
				System.out.println("TTTClient: move");
				SwingUtilities.invokeLater(() -> {
					serverMsgArea.append(message+"\n");
				});
				location = input.nextInt();
				input.nextLine();
				row = location / 3;
				col = location % 3;
//				board[row][col].setLabel(myMark=="X" ? "O" : "X");
				SwingUtilities.invokeLater(() -> {
					Square x = board[row][col];
					System.out.println("TTTClient: setting label for "+ myMark+" ["+row+"] ["+col+"]");
					x.setLabel(myMark);
					x.repaint();
				});
				break;
			case "opponent moved":
				System.out.println("TTTClient: opponent moved");
				SwingUtilities.invokeLater(() -> {
					serverMsgArea.append(message+"\n");
				});
				location = input.nextInt();
				input.nextLine();
				row = location / 3;
				col = location % 3;
//				board[row][col].setLabel(myMark=="X" ? "O" : "X");
				SwingUtilities.invokeLater(() -> {
					Square x = board[row][col];
					System.out.println("TTClient: setting label for "+ myMark+" ["+row+"] ["+col+"]");
					x.setLabel(myMark.equals("X") ? "O" : "X");
					x.repaint();
				});
				break;
			}
		}	
	}
	
	
	public void run() {

		// TODO Auto-generated method stub
		String str = input.nextLine();
		myMark = str;
//		System.out.println("RUN METHOD OF TTTCLIENT CLASS");
		System.out.println("TTTClient: thread name for client "+myMark+" : "+Thread.currentThread().getName());
		
		// All GUI changes have to be made in event thread by calling invokeLater()
		SwingUtilities.invokeLater(() -> {
			setTitle("Player "+str);
		});
		
		
		while (true) {
//			System.out.println(input.hasNextLine());
			
	      if (input.hasNextLine()) {
	    	  //System.out.println("inside if");
	          processMessage(input.nextLine());
	      }
	    }
	}
}
