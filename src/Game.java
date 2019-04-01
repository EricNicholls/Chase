import java.awt.Color;
import java.awt.Font;

import edu.princeton.cs.introcs.StdDraw;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;


public class Game {
	
	private boolean gameInProgress = false;
	private int length; 	//only need one length, because lengths have to be equal 
	
	private boolean blacksTurn = true; 
	
	// Black Token
	private int xBlack;
	private int yBlack;
	private int xRed;
	private int yRed;
	
	int turn;
	
	private Color red = new Color (166, 0, 6);

	
	private int [][] arr;
	
	public Game (int n) {
		if (n%2 != 0) { 		//  ODD NUMBER
			try {
				throw new Exception ("Not an even side length, Enter an even number");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		length = n;
		
		xBlack = (length-2)/2;
		yBlack = length/2;
		
		xRed = length/2;
		yRed = (length-2)/2;
		
		turn = 1;
		
	}


	/**
	 * 
	 * @return true when game in progress, and false when it is not
	 * 
	 * This means a game is going, so when this method is called, the game begins, and when the 
	 * method quits, the game is over
	 */
	// returns true when a game is currently in progress, and 
	public void beginGame () {
		
		preGame ();			// set up before player makes a move
		
		
		gameInProgress = true;
		
		
		newTurn ();
		
	}
	
	public void newTurn () {
		
		printMatrix(arr);
		
		drawBoardState ();		
		
		if (!hasValidMove ()) {
			gameOver ();
		}
		
		int xBlackTemp = xBlack;
		int yBlackTemp = yBlack;
			
		int xRedTemp = xRed;
		int yRedTemp = yRed;	
		
		
		
		while (true) {
			
			
			if (StdDraw.isKeyPressed(KeyEvent.VK_NUMPAD1)) {		// 1
				
				if (blacksTurn) {
					xBlack --;
					yBlack --;
				}
				else {
					xRed --;
					yRed --;
				}
				drawBoardState();
				
				waitingForConfirmed(xRedTemp, yRedTemp, xBlackTemp, yBlackTemp);
				break;
			}
			else if (StdDraw.isKeyPressed(KeyEvent.VK_NUMPAD2)) {		// 2
				if (blacksTurn) {
					yBlack --;
				}
				else {
					yRed --;
				}
				drawBoardState();
				waitingForConfirmed(xRedTemp, yRedTemp, xBlackTemp, yBlackTemp);
				break;
				
			}
			else if (StdDraw.isKeyPressed(KeyEvent.VK_NUMPAD3)) {		// 3
				if (blacksTurn) {
					xBlack ++;
					yBlack --;
				}
				else {
					xRed++;
					yRed--;
				}
				drawBoardState();
				waitingForConfirmed(xRedTemp, yRedTemp, xBlackTemp, yBlackTemp);
				break;
			}
			else if (StdDraw.isKeyPressed(KeyEvent.VK_NUMPAD4)) {		// 4
				if (blacksTurn) {
					xBlack --;
				}
				else {
					xRed--;
				}
				drawBoardState();
				waitingForConfirmed(xRedTemp, yRedTemp, xBlackTemp, yBlackTemp);
				break;
			}
			else if (StdDraw.isKeyPressed(KeyEvent.VK_NUMPAD6)) {		// 6
				if (blacksTurn) {
					xBlack ++;
				}
				else {
					xRed++;
				}
				drawBoardState();
				waitingForConfirmed(xRedTemp, yRedTemp, xBlackTemp, yBlackTemp);
				break;
			}
			else if (StdDraw.isKeyPressed(KeyEvent.VK_NUMPAD7)) {		// 7
				if (blacksTurn) {
					xBlack --;
					yBlack ++;
				}
				else {
					xRed--;
					yRed++;
				}
				drawBoardState();
				waitingForConfirmed(xRedTemp, yRedTemp, xBlackTemp, yBlackTemp);
				break;
			}
			else if (StdDraw.isKeyPressed(KeyEvent.VK_NUMPAD8)) {		// 8
				if (blacksTurn) {
					yBlack ++;
				}
				else {
					yRed++;
				}
				drawBoardState();
				waitingForConfirmed(xRedTemp, yRedTemp, xBlackTemp, yBlackTemp);
				break;
			}
			else if (StdDraw.isKeyPressed(KeyEvent.VK_NUMPAD9)) {		// 9
				if (blacksTurn) {
					xBlack ++;
					yBlack ++;
				}
				else {
					xRed++;
					yRed++;
				}
				drawBoardState();
				waitingForConfirmed(xRedTemp, yRedTemp, xBlackTemp, yBlackTemp);
				break;
			}
		}
		
		// if invalid move because it is off the board
		if (xRed > (length-1) || xRed < 0 || yRed > (length-1) || yRed < 0 || xBlack > (length-1) || xBlack < 0 || yBlack > (length-1) || yBlack < 0) {
			System.out.println("Out of Bounds, reverted");
			xRed = xRedTemp;
			yRed = yRedTemp;
			xBlack = xBlackTemp;
			yBlack = yBlackTemp;
			
			// same turn, since it was illegal move
			newTurn ();	
		}
		
		// reverted because the cell it is trying to go is occupied by the other piece or by a blue/ pink marker.
		else if (Math.abs(arr[xRed][yRed]) == 1 || arr[xRed][yRed] == 3 || Math.abs(arr[xBlack][yBlack]) == 1 || arr[xBlack][yBlack] == 2) {
			System.out.println("On a Dormant Cell");
			xRed = xRedTemp;
			yRed = yRedTemp;
			xBlack = xBlackTemp;
			yBlack = yBlackTemp;
			
			// same turn, since it was illegal move
			newTurn ();
		}
		
		else {
			
			if (blacksTurn) {
				arr [xBlackTemp][yBlackTemp] = 1;
				arr [xRedTemp][yRedTemp] = 1;
			}
			else {
				arr [xBlackTemp][yBlackTemp] = -1;
				arr [xRedTemp][yRedTemp] = -1;
			}
			
			arr [xRed][yRed] = length/2 -1;
			arr [xBlack][yBlack] = length/2;
			blacksTurn = !blacksTurn;
			
			// add a new turn after full round
			if (blacksTurn) 
			{
				turn++;
			}
			newTurn ();
			
		}
	}
	
	private boolean hasValidMove () {
		
		int x;
		int y;
		if (blacksTurn) 
		{
			x = xBlack;
			y = yBlack;
		}
		else
		{
			x = xRed;
			y = yRed;
		}
		// topleft is valid move
		if (y+1 <= (length-1) && x-1 >= 0) 	
		{
			if (Math.abs(arr[x-1][y+1]) < 1) {
				return true;
			}
		}
		// top is valid move
		if (y + 1 <= (length-1) ) 	
		{
			if (Math.abs(arr[x][y+1]) < 1 ) 
			{
				return true;
			}
		}
		// topright is valid move
		if (x+1 <= (length-1) && y + 1 <= (length-1)) 	
		{
			if (Math.abs(arr[x+1][y+1]) < 1) {
				return true;
			}
		}
		// left is valid move
		if (x-1 >= 0) 	
		{
			if (Math.abs(arr[x-1][y]) < 1) {
				return true;
			}
		}
		// right is valid move
		if (x+1 <= (length-1)) 	
		{
			if (Math.abs(arr[x+1][y]) < 1 ) {
				return true;
			}
		}
		// bottomleft is valid move
		if (x-1 >= 0 && y-1 >= 0) 	
		{
			if (Math.abs(arr[x-1][y-1]) < 1 ) {
				return true;
			}
		}
		// bottom is valid move
		if (y-1 >= 0) 
		{
			if (Math.abs(arr[x][y-1]) < 1 ) {
				return true;
			}
		}
		// bottomright is valid move
		if (x+1 <= (length-1) && y-1 >= 0) 	
		{
			if (Math.abs(arr[x+1][y-1]) < 1 ) {
				return true;
			}
		}
		
		return false;
	}
	
	private void printMatrix (int [][] array) {
		StringBuilder sb = new StringBuilder ("[");
		
		for (int i = array.length-1; i >= 0; i--) {
			for (int j = 0; j < array[i].length; j++) {
				sb.append(array[j][i]);
			}
			sb.append("\n");
		}
		
		sb.append("]");
		System.out.println(sb.toString());
	}
	
	private boolean waitingForConfirmed (int xR, int yR, int xB, int yB) {
		while (!StdDraw.isKeyPressed (KeyEvent.VK_ENTER)) {
			if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) {
				xRed = xR;
				yRed = yR;
				xBlack = xB;
				yBlack = yB;
				
				// same turn, since it was illegal move
				newTurn ();
			}
		}
		return true;
	}
	
	private void gameOver ()
	{
		String winnerColor;
		if (blacksTurn) {
			StdDraw.clear(red);
			winnerColor = "RED";
		}
		else {
			StdDraw.clear(Color.BLACK);
			winnerColor = "BLACK";
		}
		
		StdDraw.setPenColor(Color.WHITE);
		StdDraw.setFont();
		StdDraw.setPenRadius(1.3);
		StdDraw.text(50, 55, "GAME OVER, " + winnerColor + " WINS (in " + turn + " turns)");
		
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	

	
	public void preGame () {
		
		
		// making the board as array list of array list.
		// example 6 x 6
		arr = new int [length][length];
		
		
		for (int x = 0; x < arr.length; x++) {
			for (int y = 0; y < arr[0].length; y++) {
				
				arr [x][y] = 0;
				
			}
		}
		
		//middle 2 pieces 
		arr [xBlack][yBlack] = 3;
		arr [xRed][yRed] = 2;
		
		drawBoardCanvas ();
		
	}
	
	public void drawBoardCanvas () {
		
		StdDraw.setCanvasSize(900, 900);
		StdDraw.setXscale(0, 100);
		StdDraw.setYscale(0, 100);
		
		
		StdDraw.setPenColor(StdDraw.BLACK);
	}
	
	public void drawBoardState () {
		
		StdDraw.clear(StdDraw.WHITE);
		
		StdDraw.setPenRadius(0.05);
		int lines = length+1;
		for (int i = 0; i <= lines; i++) {
			StdDraw.setPenColor();
			double lineDistance = (100*i)/length;
			StdDraw.line (lineDistance, 0, lineDistance, 100);
			StdDraw.line(0, lineDistance, 100, lineDistance);
		}
		

		StdDraw.setPenRadius(0.023);
		
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < length; y++) {		
				
				double centerOfCellX = (double)100*x/length + 50/length;
				double centerOfCellY = (double)100*y/length + 50/length;
				
				// alternating board color
				if ((x+y) % 2 == 1)
				{
					StdDraw.setPenColor(Color.GRAY);
					StdDraw.filledSquare (centerOfCellX, centerOfCellY, 50/length - 0.6);
				}
				
			
				
				// if 0, do nothing
				
				// blacks marker
				if (arr[x][y] == 1) {
					
					StdDraw.setPenColor(new Color (15, 239, 255));					// light blue 
					StdDraw.filledCircle(centerOfCellX, centerOfCellY, 3);
//					StdDraw.setPenColor();
//					StdDraw.text(centerOfCellX, centerOfCellY, "" + turn);
				}
				// reds marker
				if (arr [x][y] == -1) {
					StdDraw.setPenColor(new Color (255, 1, 187));					// pinkish
					StdDraw.filledCircle(centerOfCellX, centerOfCellY, 3);
//					StdDraw.setPenColor();
//					StdDraw.text(centerOfCellX, centerOfCellY, "" + turn);
				}
				
				
				if ( (x == xRed)&&(y == yRed) ) {
					StdDraw.setPenColor(red); 	// dark red
					StdDraw.filledCircle (centerOfCellX, centerOfCellY, 36/(length-1));
					
					// turn indicator circle
					if (!blacksTurn) {
						StdDraw.setPenColor(StdDraw.GREEN);
						StdDraw.circle (centerOfCellX, centerOfCellY, 36/(length-1));
					}
				}
				if ((x == xBlack) && (y == yBlack)) {
					StdDraw.setPenColor(StdDraw.BLACK);
					StdDraw.filledCircle (centerOfCellX, centerOfCellY, 36/(length-1));
					
					// turn indicator circle
					if (blacksTurn) {
						StdDraw.setPenColor(StdDraw.GREEN);
						StdDraw.circle (centerOfCellX, centerOfCellY, 36/(length-1));
					}
				}
				
			}
		}

		StdDraw.show(20);
	}
}

