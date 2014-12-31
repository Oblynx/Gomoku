

import java.util.ArrayList;

public class Stopper extends Thread {
	int[] moves;
	AbstractPlayer player;
	Board board;
	ArrayList<int[]> availableMoves;
	
	Stopper(int[] moves,AbstractPlayer player,Board board,ArrayList<int[]> availableMoves){
		this.moves = null;
		this.player=player;
		this.board = board;
		this.availableMoves = availableMoves;
	}
	
	public void run() {

		//moves = player.getNextMove(availableMoves, board);
		

	}

}
