package fifteenpuzzle;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;

public class FifteenPuzzle implements Comparable<FifteenPuzzle>{
	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;

	public int SIZE;

	private int board[][];
	public String move;// the move from previous node to get to current puzzle
	public ArrayList<Integer> solvedPortion;
	public double score;
	private int heuristic = 100;
	private int hashCode;
	private int cost;
	

	private void checkBoard() throws BadBoardException {
		int[] vals = new int[SIZE * SIZE];

		// check that the board contains all number 0...15
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (board[i][j]<0 || board[i][j]>=SIZE*SIZE)
					throw new BadBoardException("found tile " + board[i][j]);
				vals[board[i][j]] += 1;
			}
		}

		for (int i = 0; i < vals.length; i++)
			if (vals[i] != 1)
				throw new BadBoardException("tile " + i +
											" appears " + vals[i] + "");

	}

	/**
	 * @param fileName
	 * @throws FileNotFoundException if file not found
	 * @throws BadBoardException     if the board is incorrectly formatted Reads a
	 *                               board from file and creates the board
	 */
	public FifteenPuzzle(String fileName) throws IOException, BadBoardException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		SIZE = Integer.parseInt(br.readLine());
		board = new int[SIZE][SIZE];
		move = "";
		solvedPortion = curSolved();
		score = 0;
		cost = 0;
		heuristic = 100;
		int c1, c2, s;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				c1 = br.read();
				c2 = br.read();
				s = br.read(); // skip the space
				if (s != ' ' && s != '\n') {
					br.close();
					throw new BadBoardException("error in line " + i);
				}
				if (c1 == ' ')
					c1 = '0';
				if (c2 == ' ')
					c2 = '0';
				board[i][j] = 10 * (c1 - '0') + (c2 - '0');
			}
		}
		checkBoard();
		this.hashCode = hashCode();
		calcHeuristic();
		br.close();
	}

	public FifteenPuzzle(FifteenPuzzle p){
		this.SIZE = p.SIZE;
		this.board = new int[SIZE][SIZE];
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				this.board[i][j] = p.board[i][j];
			}
		}
		this.solvedPortion = curSolved();
		this.move = "";
		this.cost = 1;
		calcHeuristic();
		this.hashCode = hashCode();
		
		
	}

	public class Pair {
		int i, j;

		Pair(int i, int j) {
			this.i = i;
			this.j = j;
		}
	}

	public Pair findCoord(int tile) {
		int i = 0, j = 0;
		for (i = 0; i < SIZE; i++)
			for (j = 0; j < SIZE; j++)
				if (board[i][j] == tile)
					return new Pair(i, j);
		return null;
	}

	/**
	 * Get the number of the tile, and moves it to the specified direction
	 * 
	 * @throws IllegalMoveException if the move is illegal
	 */
	public void makeMove(int tile, int direction) throws IllegalMoveException {
		Pair p = findCoord(tile);
		if (p == null)
			throw new IllegalMoveException("tile " + tile + " not found");
		int i = p.i;
		int j = p.j;

		// the tile is in position [i][j]
		switch (direction) {
		case UP: {
			if (i > 0 && board[i - 1][j] == 0) {
				board[i - 1][j] = tile;
				board[i][j] = 0;
				break;
			} else
				throw new IllegalMoveException("" + tile + "cannot move UP");
		}
		case DOWN: {
			if (i < SIZE - 1 && board[i + 1][j] == 0) {
				board[i + 1][j] = tile;
				board[i][j] = 0;
				break;
			} else
				throw new IllegalMoveException("" + tile + "cannot move DOWN");
		}
		case RIGHT: {
			if (j < SIZE - 1 && board[i][j + 1] == 0) {
				board[i][j + 1] = tile;
				board[i][j] = 0;
				break;
			} else
				throw new IllegalMoveException("" + tile + "cannot move LEFT");
		}
		case LEFT: {
			if (j > 0 && board[i][j - 1] == 0) {
				board[i][j - 1] = tile;
				board[i][j] = 0;
				break;
			} else
				throw new IllegalMoveException("" + tile + "cannot move LEFT");
		}
		default:
			throw new IllegalMoveException("Unexpected direction: " + direction);
		}
	

	}

	/**
	 * @return true if and only if the board is solved, i.e., the board has all
	 *         tiles in their correct positions
	 */
	public boolean isSolved() {
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				if (board[i][j] != (SIZE * i + j + 1) % (SIZE*SIZE))
					return false;
		return true;
	}

	public ArrayList<Integer> curSolved() {
		ArrayList<Integer> s = new ArrayList<Integer>();
		int size = SIZE*SIZE;
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				if (board[i][j] == (SIZE * i + j + 1) % size){
					s.add( board[i][j]);
				} else{
					solvedPortion = s;
					return s;
				}
			solvedPortion = s;		
		return s;
	}

	private String num2str(int i) {
		if (i == 0)
			return "  ";
		else if (i < 10)
			return " " + Integer.toString(i);
		else
			return Integer.toString(i);
	}

	public int[][] getBoard(){
		return board;
	}

	public String toString() {
		String ans = "";
		for (int i = 0; i < SIZE; i++) {
			ans += num2str(board[i][0]);
			for (int j = 1; j < SIZE; j++)
				ans += " " + num2str(board[i][j]);
			ans += "\n";
		}
		return ans;
	}

	public String printBoard(){
		String ans = "";
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++)
				ans += "" + board[i][j];
		}
		return ans;
	}

	public Pair blankTile(){
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++)
				if( board[i][j] == 0)
				return new Pair(i, j);
		}
		return null;
	}

	public Integer nextMove(){
		// find which tile needs to be moved to its spot
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				if (board[i][j] != (4 * i + j + 1) % 16)
					return board[i][j];
		return null;
	}


	@Override
	public int hashCode() {
		this.hashCode = printBoard().hashCode();
		return this.hashCode;

	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(!(obj instanceof FifteenPuzzle)){
			return false;
		}
		if(this == obj){
			return true;
		}
		FifteenPuzzle p = (FifteenPuzzle) obj;
		if(this.getHashCode() == p.getHashCode()){
			return true;
		}
		return false;
	}
	



	public void calcHeuristic(){
		

		int r = 0;
		int count = 1;
		
		for (int i = 0; i < SIZE; i++){
			for (int j = 0; j < SIZE; j++) {
				if(board[i][j] != count){
					int value = board[i][j];
					int row = (value-1)/SIZE;
					int col = (value-1)%SIZE;
					r += Math.abs(row-i) + Math.abs(col -j);
				}
				count++;
				
			}
		}
			
		this.heuristic = r;
		

	}

	public double getScore(){
		return score;
	}

	@Override
	public int compareTo(FifteenPuzzle o) {
		int compare = Integer.compare(this.getHashCode(), o.getHashCode());
		
		if (compare == 0)
			return 0;

		if(compare == -1){
			return -1;
		}

		if(compare == 1){
			return 1;
		}
		return compare;
	}

	public int getHeuristic(){
		return heuristic;
	}

	public int getCost(){
		return cost;
	}

	public void setCost(AStarNodeWrapper a){

		a.getTotalCostFromStart();
	}

	public int getHashCode(){
		return hashCode;
	}

	public void setHeuristic(int num){
		heuristic = num;

	}

}