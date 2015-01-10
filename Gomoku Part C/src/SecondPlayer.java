/**
 * @author Konstantinos Samaras-Tsakiris | kisamara@auth.gr    | AEM: 7972
 * @author Vangelis Kipouridis           | kipoujr@hotmail.com | AEM: 7899
 */
public class SecondPlayer implements AbstractPlayer
{

  int score;
  int id;
  String name;
  /**Region of interest: Suppose that the evaluation function will give the same
   * result for a position (x_eval,y_eval) that appears in 2 consecutive board states, if
   * the added mark is far from (x_eval,y_eval). The orthogonal distance beyond
   * which the evaluation function is presumed static is the region of interest.
   */
  static int roi= 5;	// Always >=4
  int[][] previousEvals;
  int[] previousBestMove;
  boolean reevaluate;

  public SecondPlayer (Integer pid){
	id = pid;
	score = 0;
	reevaluate= true;
	previousEvals= new int[GomokuUtilities.NUMBER_OF_COLUMNS][GomokuUtilities.NUMBER_OF_ROWS];
	previousBestMove= new int[3];
	previousBestMove[2]= 0;
  }
  public SecondPlayer(Integer pid, int pscore, String pname){
	  score = pscore;
	  id = pid;
	  name=pname;
	  reevaluate= true;
	  previousEvals= new int[GomokuUtilities.NUMBER_OF_COLUMNS][GomokuUtilities.NUMBER_OF_ROWS];
	  previousBestMove= new int[3];
	  previousBestMove[2]= 0;
  }

  public String getName (){ return "Heuristic"; }
  public int getId (){ return id; }
  public void setScore (int score){ this.score = score; }
  public int getScore (){ return score; }
  public void setId (int id){ this.id = id; }
  public void setName (String name){ this.name = name; }
  
  /**Follows the suggested algorithm, only the use of an ArrayList is superfluous.
   * It is included simply for completeness.
   * @return the highest-evaluated move (x,y)
   **/
  public int[] getNextMove (Board board)
  {
	 //ArrayList<int[]> evaluatedPos= new ArrayList<int[]>();
	 int singleEval;
	 int[] bestMove= new int[3];
	 bestMove[2]= Integer.MIN_VALUE;
	 //How to get nRows, nColumns?
	 for(int x=0; x<GomokuUtilities.NUMBER_OF_COLUMNS; x++){
		 for(int y=0; y<GomokuUtilities.NUMBER_OF_ROWS; y++){
			 if(board.getTile(x, y).getColor() == 0){
				 singleEval= evaluate(x,y, board);
				 if (bestMove[2] < singleEval){
					 bestMove[0]= x; bestMove[1]= y;
					 bestMove[2]= singleEval;
				 }
				 //evaluatedPos.add(singleEvalPos);
			 }
		 }
	 }
	 //Why did you tell us to use ArrayList???
	 return bestMove;
  }
  
  /**@brief Heuristic evaluation function. It takes into account the potential
   * to make 2,3,4,5-tuples, the domination difference between the 2 players in
   * close proximity to (x,y) and the centrality of the position (measured in 
   * how many possible quintuples occupy this position.
   * @throws If the (x,y) tile isn't empty, throw IllegalArgumentException
   * @return A value that assesses the winning potential for making the (x,y) move
   */
  int evaluate (int x, int y, Board board) throws IllegalArgumentException{
	  if (board.getTile(x, y).getColor() != 0)
		  throw new IllegalArgumentException("Tile("+x+","+y+") isn't empty!");
  	  //Check if I or the opponent makes quintuple (depth-1 victory condition)
	  //These are the highest priority positions
	  if (makesNTuples(5, x,y,board, id) >= 1) return Integer.MAX_VALUE;
	  if (makesNTuples(5, x,y,board, oppID()) >= 1) return Integer.MAX_VALUE-1;
	  	  
	  // Check some predefined features of the position that contribute to its score
	  //The domination difference in range-2 area of (x,y), domain [-10,10]
	  double range2DColor= (GomokuUtilities.colorPercentage(board, x, y, 2, id) -
			  GomokuUtilities.colorPercentage(board, x, y, 2, oppID())) * 10;
	  //The domination difference in range-3 area of (x,y), domain [-10,10]
	  double range3DColor= (GomokuUtilities.colorPercentage(board, x, y, 3, id) -
			  GomokuUtilities.colorPercentage(board, x, y, 3, oppID())) * 10;
	  //The domination difference in range-4 area of (x,y), domain [-10,10]
	  double range4DColor= (GomokuUtilities.colorPercentage(board, x, y, 4, id) -
			  GomokuUtilities.colorPercentage(board, x, y, 4, oppID())) * 10;
	  //Take the square of the domination differences, to emphasize on areas
	  //occupied mostly by either player. Domain: [0,100]
	  range2DColor*= range2DColor;
	  range3DColor*= range3DColor;
	  range4DColor*= range4DColor;
	  
	  //Take a linear combination of the features, with every feature having a 
	  //domain of ~[0,100]
	  double[] w= {0.5,1,1.5, 3, 2,4,10,100};
	  int evaluation= (int)(
			  w[0]*range2DColor + w[1]*range3DColor + w[2]*range4DColor +
			  w[3]*(5*partakesIn5Tuples(x,y,board)) + 
			  w[4]*(75*makesNTuples(2,x,y,board,id)) + 
			  w[5]*(75*makesNTuples(3,x,y,board,id)) + 
			  w[6]*(75*makesNTuples(4,x,y,board,id)) +
			  w[7]*(75*makesNTuples(4,x,y,board,oppID()))
			  );
	  return evaluation;
  }
  
  /**Checks if putting a tile of the specified player at the specified
   * position on the board makes an N-tuple for that player. Domain: [0,4] but
   * typically <=2
   * @throws If N<2 or N>5 throws IllegalArgumentException
   * @param N Whether looking for 5-tuples, 4-tuples, 3-tuples or 2-tuples
   * @param x position
   * @param y position
   * @param board
   * @param pid The player whose mark is to be put on the board at position (x,y)
   * @return How many N-tuples it makes
   */
  int makesNTuples(int N, int x, int y, Board board, int pid) throws IllegalArgumentException{
	  if(N<2 || N>5) throw new IllegalArgumentException("N out of bounds [2,5]!");
	  final int[] owner= {pid};
	  int[] lim= checkAreaAround(x,y,board, N-1, owner);
	  
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
  int partakesIn5Tuples(int x, int y, Board board) throws IllegalArgumentException{
	  if (board.getTile(x, y).getColor() != 0)
		  throw new IllegalArgumentException("Tile("+x+","+y+") isn't empty!");
	  //Limits on all directions
	  final int[] owners= {0,id};
	  int[] lim= checkAreaAround(x,y,board, 5, owners);
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
  int[] checkAreaAround(int x,int y,Board board,int range, int[] owners){
	  //Limits on all directions
	  //x1,x2,y1,y2,dl1,dl2,dr1,dr2
	  int[] lim= new int[8];
	  for(int i=0; i<8; i++) lim[i]=0;
	  int[] tmp= new int[8];
	  int checkOwnership=0;	//Support variable
	  //Assess how many tiles on each side are owned by players <own>
	  for(int i=1; i<=range; i++){
		  //Get ownership of nearby tiles IF they are on the board
		  if (x-i > 0) tmp[0]= board.getTile(x-i,y).getColor();
		  else tmp[0]= -1;
		  if (x+i < GomokuUtilities.NUMBER_OF_COLUMNS) 
			  tmp[1]= board.getTile(x+i,y).getColor();
		  else tmp[1]= -1;
		  if (y-i > 0) tmp[2]= board.getTile(x,y-i).getColor();
		  else tmp[2]= -1;
		  if (y+i < GomokuUtilities.NUMBER_OF_ROWS)
			  tmp[3]= board.getTile(x,y+i).getColor();
		  else tmp[3]= -1;
		  if (x-i > 0 && y+i < GomokuUtilities.NUMBER_OF_ROWS)
			  tmp[4]= board.getTile(x-i,y+i).getColor();
		  else tmp[4]= -1;
		  if (x+i < GomokuUtilities.NUMBER_OF_COLUMNS && y-i > 0)
			  tmp[5]= board.getTile(x+i,y-i).getColor();
		  else tmp[5]= -1;
		  if (x+i < GomokuUtilities.NUMBER_OF_COLUMNS && y+i < GomokuUtilities.NUMBER_OF_ROWS)
			  tmp[6]= board.getTile(x+i,y+i).getColor();
		  else tmp[6]= -1;
		  if (x-i > 0 && y-i > 0) tmp[7]= board.getTile(x-i,y-i).getColor();
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
  
  int oppID() {return (id==1)? 2: 1;}
}
