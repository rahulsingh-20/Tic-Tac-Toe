package tictactoe;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class StartScreen {
	
	public final JButton createClientButton = new JButton();
	

	public static void main(String[] args) {
		StartScreen ss = new StartScreen();
		ss.initUI();
		ss.activateClientButton();
		
		TTTServer server = new TTTServer();
		server.startServer();
     
		
	}
	
	private void initUI() {
		// creating frame object
		JFrame frame = new JFrame("Tic Tac Toe");
		frame.setLayout(new GridBagLayout());
				
		// creating panel object
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			
		JLabel welcomeLabel = new JLabel("Welcome to TicTacToe");
		welcomeLabel.setBorder(new EmptyBorder(0,0,50,0));
				
		createClientButton.setText("Create Client");
		createClientButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		panel.add(welcomeLabel);
		panel.add(createClientButton);
		
		frame.add(panel, new GridBagConstraints());
		
		frame.setSize(300, 300);
		frame.setLocationRelativeTo(null);  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setVisible(true);
	}
		
	private void activateClientButton() {
		
		System.out.println("StartScreen: inside activateclient");
		
		
		
		createClientButton.addActionListener(
				
				new ActionListener() {

					private int playerCount = 0;
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						if(playerCount > 1) {
							System.out.println("StartScreen: can't have more than 2 players");
						}
						else {
							playerCount++;
							System.out.println("StartScreen: Button clicked, playercount = "+playerCount);

						
							new Thread(new TTTClient()).start();
							
							System.out.println("StartScreen: name of actionlistener thread: "+Thread.currentThread().getName());
						}
					}
					
				}
		);
	}
}
