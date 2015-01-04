package mcts;

import mcts.Common.Const;
import mcts.Common.BoardOperation;

class MctsBoard {
	private int[][] board;
	private int columns;
	private int rows;
	private int totalAvailableMoves;
	
	/**Blank board constructor*/
	MctsBoard(int col, int row){
		columns= col; rows= row;
		totalAvailableMoves= columns*rows;
		board= new int[col][row];
		for(int i=0; i<col; i++)
			for(int j=0; j<row; j++)
				board[i][j]= 0;	
	}
	//Copy constructor
	MctsBoard(MctsBoard that){
		board= that.board.clone();
		columns= that.columns;
		rows= that.rows;
		totalAvailableMoves= that.totalAvailableMoves;
	}
	void makeMove(int[] move, int pid) throws IllegalArgumentException{
		if (board[move[0]][move[1]] == 0) {
			board[move[0]][move[1]]= pid;
			totalAvailableMoves--;
		}
		else throw new IllegalArgumentException("Made move on occupied tile!!");
	}
	
	/**Checks if (x,y) is within bounds*/
	int getColor(int x, int y){
		if (x < 0 || x > columns || y < 0 || y > rows)
			return -1;
		else return board[x][y];
	}
	
	int availableMoves(int[] xRange, int[] yRange){
		int count=0;
		for(int x=xRange[0]; x<xRange[1]; x++)
			for(int y=yRange[0]; y<yRange[1]; y++)
				if (getColor(x,y) == 0) count++;
		return count;
	}
	
	/**Returns the number of each player's tiles over the total tiles of a square
	 * around pos.
	 * @param pos Square region center
	 * @param range Square region extends <code>range</code> on each side of <code>pos</code>.
	 * @return int[2]: {player1/total, player2/total}
	 */
	float[] areaDomination(int[] pos, int range){
		int[] xRange= {pos[0]-range, pos[0]+range};
		int[] yRange= {pos[1]-range, pos[1]+range};
		
		int total=0, p1=0, p2=0, tmp;
		for(int x=xRange[0]; x<xRange[1]; x++)
			for(int y=yRange[0]; y<yRange[1]; y++){
				tmp= getColor(x,y);
				if (tmp!=-1){
					if (tmp == Const.pids[0]) p1++;
					if (tmp == Const.pids[1]) p2++;
					total++;
				}
			}
		float[] res= {(float)p1/total, (float)p2/total};
		return res;
	}
	
	/**[columns,rows]*/
	int[] getSize(){
		int[] size= {columns, rows};
		return size;
	}
	int getTotalAvailableMoves(){ return totalAvailableMoves; }
	
	/**Generic function that checks the provided x,y region and passes the value
	 * of every tile in this region to <code>op</code>.
	 * @param xRange Checked
	 * @param yRange Checked
	 */
	void operateSelection(int[] xRange, int[] yRange, BoardOperation op){
		int[] checkedxRange= {(xRange[0]>0)? xRange[0]: 0, (xRange[1]<columns)? xRange[1]: columns};
		int[] checkedyRange= {(yRange[0]>0)? yRange[0]: 0, (yRange[1]<rows)? yRange[1]: rows};;
		for(int x=checkedxRange[0]; x<checkedxRange[1]; x++)
			for(int y=checkedyRange[0]; y<checkedyRange[1]; y++)
				op.operate(x,y,board[x][y]);
	}

}
