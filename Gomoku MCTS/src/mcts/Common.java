package mcts;

/**Container for static objects used throughout the package  
 * @author Konstantinos Samaras-Tsakiris
 */
abstract class Common {
	static interface AbstractPlayer{
	  public void setId (int id);
	  public int getId ();
	  public void setName (String name);
	  public String getName ();
	  public void setScore (int score);
	  public int getScore ();
	  public int[] getNextMove (MctsBoard board);
	}
	interface BoardOperation{
		void operate(int x, int y, int color);
	}
	
	/**Collection of heuristic evaluation functions
	 * @author Konstantinos Samaras-Tsakiris
	 */
	static class Heuristics{
		 /**Heuristic evaluation function. It takes into account the potential
		   * to make 2,3,4,5-tuples, the domination difference between the 2 players in
		   * close proximity to (x,y) and the centrality of the position (measured in 
		   * how many possible quintuples occupy this position.
		   * @throws IllegalArgumentException if the (x,y) tile isn't empty 
		   * @return A value that assesses the winning potential for making the (x,y) move
		   */
		  static int heuristicEvaluate (int[] pos, MctsBoard board, int id) throws IllegalArgumentException{
			  if (board.getColor(pos[0],pos[1]) != 0)
				  throw new IllegalArgumentException("Tile("+pos[0]+","+pos[1]+") isn't empty!");
		  	  //Check if I or the opponent makes quintuple (depth-1 victory condition)
			  //These are the highest priority positions
			  if (makesNTuples(5, pos,board, id) >= 1) return Integer.MAX_VALUE;
			  if (makesNTuples(5, pos,board, oppID(id)) >= 1) return Integer.MAX_VALUE-1;
			  	  
			  // Check some predefined features of the position that contribute to its score
			  float[] d2= board.areaDomination(pos, 2);
			  float[] d3= board.areaDomination(pos, 3);
			  float[] d4= board.areaDomination(pos, 4);
			  //The domination difference in range-2 area of (x,y), domain [-10,10]
			  double range2DDomin= (d2[id-1]-d2[oppID(id)-1]) * 10;
			  //The domination difference in range-3 area of (x,y), domain [-10,10]
			  double range3DDomin= (d3[id-1]-d3[oppID(id)-1]) * 10;
			  //The domination difference in range-4 area of (x,y), domain [-10,10]
			  double range4DDomin= (d4[id-1]-d4[oppID(id)-1]) * 10;
			  //Take the square of the domination differences, to emphasize on areas
			  //occupied mostly by either player. Domain: [0,100]
			  range2DDomin*= range2DDomin;
			  range3DDomin*= range3DDomin;
			  range4DDomin*= range4DDomin;
			  
			  //Take a linear combination of the features, with every feature having a 
			  //domain of ~[0,100]
			  double[] w= {0.5,1,1.5, 3, 2,4,10,100};
			  int evaluation= (int)(
					  w[0]*range2DDomin + w[1]*range3DDomin + w[2]*range4DDomin +
					  w[3]*(5*partakesIn5Tuples(pos,board,id)) + 
					  w[4]*(75*makesNTuples(2,pos,board,id)) + 
					  w[5]*(75*makesNTuples(3,pos,board,id)) + 
					  w[6]*(75*makesNTuples(4,pos,board,id)) +
					  w[7]*(75*makesNTuples(4,pos,board,oppID(id)))
					  );
			  return evaluation;
		  }
		  
		  /**Checks if putting a tile of the specified player at the specified
		   * position on the board makes an N-tuple for that player. Domain: [0,4] but
		   * typically <=2
		   * @throws IllegalArgumentException if N<2 or N>5 
		   * @param N Whether looking for 5-tuples, 4-tuples, 3-tuples or 2-tuples
		   * @param x position
		   * @param y position
		   * @param board
		   * @param pid The player whose mark is to be put on the board at position (x,y)
		   * @return How many N-tuples it makes
		   */
		  static int makesNTuples(int N, int[] pos, MctsBoard board, int pid) throws IllegalArgumentException{
			  if(N<2 || N>5) throw new IllegalArgumentException("N out of bounds [2,5]!");
			  final int[] owner= {pid};
			  int[] lim= checkAreaAround(pos,board, N-1, owner);
			  
			  int row= (lim[0]+lim[1] >= N-1)?  1: 0;
			  int column= (lim[2]+lim[3] >= N-1)? 1: 0;
			  int ldiag= (lim[4]+lim[5] >= N-1)? 1: 0;
			  int rdiag= (lim[6]+lim[7] >= N-1)? 1: 0;
			  return row+column+ldiag+rdiag;
		  }
		  
		  /**Given the game state, if a tile is put at (x,y), in how many quintuples max
		   * could it possibly participate in the future? It is a better and dynamic 
		   * measure of the "centrality" of the position. (x,y) must be free. Domain: [0,20]
		   * @throws If the (x,y) tile isn't empty, throw IllegalArgumentException
		   * @param x
		   * @param y
		   * @param board
		   * @return The total number of quintuples that the position partakes in
		   * (by row, column, left & right diagonal)
		   */
		  static int partakesIn5Tuples(int[] pos, MctsBoard board, int id) throws IllegalArgumentException{
			  if (board.getColor(pos[0],pos[1]) != 0)
				  throw new IllegalArgumentException("Tile("+pos[0]+","+pos[1]+") isn't empty!");
			  //Limits on all directions
			  final int[] owners= {0,id};
			  int[] lim= checkAreaAround(pos,board, 5, owners);
			  //Row quintuples
			  int row= (lim[0]+lim[1] >= 4)? lim[0]+lim[1]-4: 0;
			  //Column quintuples
			  int column= (lim[2]+lim[3] >= 4)? lim[2]+lim[3]-4: 0;
			  //Left diagonal quintuples
			  int ldiag= (lim[4]+lim[5] >= 4)? lim[4]+lim[5]-4: 0;
			  //Right diagonal quintuples
			  int rdiag= (lim[6]+lim[7] >= 4)? lim[6]+lim[7]-4: 0;
			  return row+column+ldiag+rdiag;
		  }

		  /**In each direction around (x,y), check <code>range</code> tiles. If each
		   * tile belongs to an owner in the <code>owners</code> list, increase the 
		   * limit corresponding to that direction
		   * @param x
		   * @param y Position
		   * @param board
		   * @param range How far to look in each direction
		   * @param owners List of playerIDs for which tiles count
		   * @return An array with the limits at each direction in the following order:
		   * {x-,x+,y-,y+,ldiagUp,ldiagDown,rdiagUp,rdiagDown}
		   */
		  static int[] checkAreaAround(int[] pos, MctsBoard board, int range, int[] owners){
			  int x=pos[0], y=pos[1];
			  //Limits on all directions
			  //x1,x2,y1,y2,dl1,dl2,dr1,dr2
			  int[] lim= new int[8];
			  for(int i=0; i<8; i++) lim[i]=0;
			  int[] tmp= new int[8];
			  int checkOwnership=0;	//Support variable
			  //Assess how many tiles on each side are owned by players <own>
			  for(int i=1; i<=range; i++){
				  //Get ownership of nearby tiles IF they are on the board
				  if (x-i > 0) tmp[0]= board.getColor(x-i,y);
				  else tmp[0]= -1;
				  if (x+i < board.getSize()[0]) 
					  tmp[1]= board.getColor(x+i,y);
				  else tmp[1]= -1;
				  if (y-i > 0) tmp[2]= board.getColor(x,y-i);
				  else tmp[2]= -1;
				  if (y+i < board.getSize()[1])
					  tmp[3]= board.getColor(x,y+i);
				  else tmp[3]= -1;
				  if (x-i > 0 && y+i < board.getSize()[1])
					  tmp[4]= board.getColor(x-i,y+i);
				  else tmp[4]= -1;
				  if (x+i < board.getSize()[0] && y-i > 0)
					  tmp[5]= board.getColor(x+i,y-i);
				  else tmp[5]= -1;
				  if (x+i < board.getSize()[0] && y+i < board.getSize()[1])
					  tmp[6]= board.getColor(x+i,y+i);
				  else tmp[6]= -1;
				  if (x-i > 0 && y-i > 0) tmp[7]= board.getColor(x-i,y-i);
				  else tmp[7]= -1;
				  //For every direction, if that tile's ownership is included in <owners>
				  //then increase that direction's limit
				  for(int j=0; j<8; j++){
					  for(int owner : owners)
						  if(tmp[j] == owner) checkOwnership++;
					  if (checkOwnership > 0) lim[j]++;
					  checkOwnership= 0;
				  }
			  }
			  return lim;
		  }
		  
		  static int oppID(int id) {return (id==1)? 2: 1;}
	}
	static class Const {
		/**Region of Interest*/
		static int roi= 5;
		/**Tree expansion rate*/
		static int BRANCH_FACTOR= 2;
		/**UCB exploration-exploitation balance parameter*/
		static float C= 1;
		/***/
		static int[] pids= {1,2};
	}
	
}
