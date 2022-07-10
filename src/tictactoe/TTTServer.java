package tictactoe;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TTTServer {
	
	private Player[] players;
	private ServerSocket servSoc;
	private ExecutorService playerThreadPool;
	private Condition otherPlayerConnected;
	private Condition otherPlayerTurn;
	private int currentPlayer;
	private boolean suspended = false;
	private String[] marks = {"X", "O"};
	private Lock gameLock;
	private String[] board = {" ", " ", " ", " ", " ", " ", " ", " ", " "};
	
	public TTTServer() {
		//players = new Player[2];
		System.out.println("TTTServer: entered constructor class of TTTServer");
		
		try {
			servSoc = new ServerSocket(12345);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		playerThreadPool = Executors.newFixedThreadPool(2);
		gameLock = new ReentrantLock();
		
		// condition variable for both players being connected
	    otherPlayerConnected = gameLock.newCondition();

	    // condition variable for the other player's turn
	    otherPlayerTurn = gameLock.newCondition();
	    
	}
	
	
	public boolean isGameOver() {
		boolean gameover = isBoardFull() || doesWinExist();
		System.out.println("TTTServer: inside isGameOver method, value is "+ gameover);
		return gameover;
	}
	
	public boolean isBoardFull() {	
		boolean full=true;
		
		for(int i=0; i<board.length; i++) {
			if(board[i] == " ") {
				full = false;
			}
		}
		
		System.out.println("TTTServer: Board is full: "+full);
		
		return full;
		
	}
	
	public boolean doesWinExist() {
		boolean win =  (board[0] != " " && board[0].equals(board[1]) && board[0].equals(board[2]))
			      || (board[3] != " " && board[3].equals(board[4]) && board[3].equals(board[5]))
			      || (board[6] != " " && board[6].equals(board[7]) && board[6].equals(board[8]))
			      || (board[0] != " " && board[0].equals(board[3]) && board[0].equals(board[6]))
			      || (board[1] != " " && board[1].equals(board[4]) && board[1].equals(board[7]))
			      || (board[2] != " " && board[2].equals(board[5]) && board[2].equals(board[8]))
			      || (board[0] != " " && board[0].equals(board[4]) && board[0].equals(board[8]))
			      || (board[2] != " " && board[2].equals(board[4]) && board[2].equals(board[6]));
		
		System.out.println("TTTServer: does win exist: "+win);
		
		return win;
	}
	
	public boolean isMoveValid(int location, int player) {
		boolean valid;
		
		System.out.println("TTTServer: inside ismovevalid");
		
		if(players[1] == null) {
			valid = false;
		}
		
		else {
			if(currentPlayer != player) {
				valid = false;
			}
			
			else {
				if(isGameOver()) {
					valid = false;
				} else {
				
					if(board[location] == " ") {
						valid = true;
					} else {
						valid = false;
					}
				}
			}
		}
		
		if(valid) {
			currentPlayer = 1 - currentPlayer;
		}
		
		System.out.println("TTTServer: is move valid: "+valid);
		
		return valid;
	}
	
//	public void validateAndMove(int location) {
//		board[location] = marks[playerNumber];
//		output.format("Player: opponent moved, your turn\n");
//		output.flush();
//	}
	
	// inner Player class
	private class Player implements Runnable{
		
		private Socket connection;
		private int playerNumber;
		private Scanner input;
		private Formatter output;
		
		private Player(Socket socket, int number) {
			
//			System.out.println(Thread.currentThread().getName());
			
			connection = socket;
			playerNumber = number;
			
			try {
				input = new Scanner(connection.getInputStream());
				output = new Formatter(connection.getOutputStream());
			} catch(IOException e) {
				e.printStackTrace();
			}
			
		}
		
		private void otherPlayerMoved(int location) {
			output.format("opponent moved\n");
			output.format("%d\n", location);
			output.flush();
		}

		@Override
		public void run() {
			System.out.println("Player: INSIDE PLAYER RUN");
//			System.out.println("player thread number "+playerNumber+" running");
			System.out.println("Player: thread name for player "+marks[playerNumber]+" is "+Thread.currentThread().getName());
			
			// assigning mark to the player
			output.format("%s\n",marks[playerNumber]);
			output.flush();
			
			//System.out.println("Player: flushed player type");
			
			if(marks[playerNumber].equals("X")) {
//				System.out.println("player o print");
				
				currentPlayer = playerNumber;
				System.out.println("Player: setting up currentplayer as "+playerNumber);
				
				output.format("Player: Waiting for Player O\n");
				output.flush();
				
				
//				gameLock.lock();
//				System.out.println("TTTServer: locked");
				
				
//				try {
//					while(true) {
//						System.out.println("TTTServer: Locked, awaiting for otherPlayerConnected");
//						otherPlayerConnected.await();
//						
//					}// wait for player's connection
//		        } catch (Exception exception) {
//		          System.out.println(exception.toString());
//		        } finally {
//		          gameLock.unlock(); // unlock game after waiting
//		        }
				
				
			}
			
			
			else {
				output.format("Player: you are player o, x's turn\n");
				output.flush();
//				gameLock.lock();
				
//				try{
//					System.out.println("Player: inside signal try block, player "+marks[playerNumber]);
//					otherPlayerConnected.signal();
//					otherPlayerTurn.await();
//				} catch(InterruptedException e){
//					e.printStackTrace();
//				} finally {
//					gameLock.unlock();
//					System.out.println("TTTServer: unlocked");
//				}
//				
//				System.out.println("TTTServer: Unlocked, signalled otherPlayerConnected");
				
//				String str = input.nextLine();
//				System.out.println("server read message "+str);
			}
			
//			String str = input.nextLine();
//			System.out.println("SERVER READ MESSAGE "+str);
			
			System.out.println("Player: end of if else");
			
//			while(input.hasNext()) {
//				String str = input.nextLine();
//				System.out.println("Player: "+str);
//			}
			
			while(isGameOver() == false) {
				System.out.println("##################################################Player: inside isgameover block");
				int location=0;
				if(input.hasNext()) {
					location = input.nextInt();
					input.nextLine();
					System.out.println("Player: location sent by client "+playerNumber+" : "+location);
				}

				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				
				if(isMoveValid(location, playerNumber)) {
					
					System.out.println("&&&&&&&&&&&&&&&&&&&&&&, gameover: " +isGameOver());
					
					board[location] = marks[playerNumber];
//					output.format("Player: opponent moved, your turn\n");
					output.format("Move\n");
					output.format("%d\n", location);
					output.flush();
					
//					playerNumber = 1 - playerNumber; // changing player
					
					players[1-playerNumber].otherPlayerMoved(location);
					
//					board[location] = marks[playerNumber];
//					output.format()
				}
			}
			
		}
		
	}
	
	public void startServer() {
		System.out.println("TTTServer: entered startServer()");
		players = new Player[2];
		System.out.println("TTTServer: players array initialised");
		
		for(int i=0; i<players.length; i++) {
			try {
				players[i] = new Player(servSoc.accept(), i);
				
				System.out.println("TTTServer: created player "+i);
				
				playerThreadPool.execute(players[i]);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
				
	}
	
	
	
	
	
}
