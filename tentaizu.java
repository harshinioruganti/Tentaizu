// Harshini Oruganti
// February 4, 2022
// COP3503 Tentaizu Problem

import java.util.*;

/* The game is played on a 7x7 board. Exactly 10 of the 49 squares are each
hiding a star. Your task is to determine which squares are hiding the stars.
Other squares in the board provide clues: A number in a square indicates
how many stars lie next to the square—in other words, how many adjacent
squares (including diagonally adjacent squares) contain stars. No square
with a number in it contains a star, but a star may appear in a square with
no adjacent numbers. */

public class tentaizu
{
	// constants
	private final static int SIDE = 7;
	private final static int STARS = 10;

	// 1-D board so don't have to worry about changing row index
	// easy to loop through without going out of bounds
	private static char[] board;

	public tentaizu(String input)
	{
		// need to create a new board for each case
		board = new char[SIDE * SIDE];

		// converts string into char to place into board array
		for (int i = 0; i < SIDE * SIDE; i ++)
			board[i] = input.charAt(i);
	}

	// checks whether a given cell is a valid cell in the bounds of the board
	public boolean isValid(int x, int y)
	{
		return (x >= 0 && x < SIDE && y >= 0 && y < SIDE);
	}

	// checks whether a star can be placed at a certain position
  // returns true if bomb cannot be placed, returns false if it can
	public boolean notBomb(int pos)
	{
		int row = pos / SIDE;
		int col = pos % SIDE;

		int count = 0;

		// NOTE TO SELF: make sure to initialize to row-1 and to bound to row + 1 so it checks all
		// surrounding cells from a position -- "like your direction arrays"
		for (int x = row - 1; x <= row + 1; x++)
		{
			for (int y = col - 1; y <= col + 1; y++)
			{
				if (isValid(x, y))
				{
					// if there is a 0 in an adjecent cell, surrounding cells cannot contain a star
					if (board[SIDE * x + y] == '0')
						return true;
				}
			}
		}

		return false;
	}

	// recursive function for playing game and solving the board
	public boolean playTentaizu (int k, int numStars) {

		// Stars cases that will cause function to return false

		// if by the time you are towards end of array, and there are too many stars left over
		// case to cut down on runtime
		if (SIDE * SIDE + numStars - k < STARS)
			return false;

		if (k == SIDE * SIDE && numStars != STARS)
			return false;

		if (numStars > STARS)
			return false;

		//base case 1: all stars placed, return final check
		boolean correct = checkBoard(k, numStars);

		if (!correct)
			return false;

		if (numStars == STARS)
			return true;

		// recursive call 1: after base case, no stars placed
		// checks to see if a star can't be placed, and moves on to next position
		if (board[k] != '.' || notBomb(k))
		{
			return playTentaizu(k + 1, numStars);
		}
		// recursive call 2: if safe to place a star, recursive with it being a star
		// if this returns false, make that position a dot again
		else
		{
			boolean res = false;

			// place a star and recurse to see if it is a valid position
			board[k] = '*';
			res = playTentaizu(k + 1, numStars + 1);

			if (res)
				return true;

			board[k] = '.';
			return playTentaizu(k + 1, numStars);
		}
	}

	// check the current board meets the requirements:
  //      ten stars are present
  //      each numbered square has the correct number of adjacent stars
	public boolean checkBoard(int k, int numStars) {

		// Look for number, count how many stars are surrounding that number cell
		for (int i = 0; i < board.length; i++)
		{
			if (board[i] >= '0' && board[i] <= '9')
			{
				int row = i / SIDE;
				int col = i % SIDE	;

				int count = 0;
				for (int x = row - 1; x <= row + 1; x++)
				{
					for (int y = col - 1; y <= col + 1; y++)
					{
						if (isValid(x,y) && board[SIDE * x + y] == '*')
							count++;
					}
				}

				// check if a square is surrounded by too many bombs
				// if so, return false
				if ((k == SIDE * SIDE || numStars == STARS) && (char)(count + '0') != board[i])
					return false;

				if ((char)(count + '0') > board[i])
					return false;

				// check for a no solution board
				if (i < k - SIDE - 1 && (char)(count + '0') != board[i])
					return false;
			}
		}

		return true;
	}

	// prints the board
	public void printBoard()
	{
		for (int i = 0; i < board.length; i++)
		{
			// NOTE TO SELF: place if statement first, so goes to next row before printing next 7 chars
			if (i % SIDE == 0 && i > 0)
				System.out.println("");

			System.out.print(board[i]);
		}
	}

	public static void main(String[] args)
	{
		Scanner stdin = new Scanner(System.in);
		int numBoards = stdin.nextInt();

		for (int loop = 1; loop <= numBoards; loop++)
		{
			// Declare other variables here
			String input = "";

			// Read in input for one case
			for (int i = 0; i < SIDE; i++)
			{
				input = input + stdin.next();
			}

			// creates new tentaizu object
			tentaizu myBoard = new tentaizu(input);

			// Process information
			myBoard.playTentaizu(0, 0);
			System.out.println("");

			// Print output for one case
			System.out.println("Tentaizu Board #" + loop + ":");
			myBoard.printBoard();
			System.out.println("");
		}
	}
}
