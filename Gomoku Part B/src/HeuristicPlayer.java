import java.util.ArrayList;

public class HeuristicPlayer implements AbstractPlayer
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

  public HeuristicPlayer (Integer pid){
    id = pid;
    score = 0;
    reevaluate= true;
    previousEvals= new int[GomokuUtilities.NUMBER_OF_COLUMNS][GomokuUtilities.NUMBER_OF_ROWS];
    previousBestMove= new int[3];
  }
  public HeuristicPlayer(Integer pid, int pscore, String pname){
	  score = pscore;
	  id = pid;
	  name=pname;
	  reevaluate= true;
	  previousEvals= new int[GomokuUtilities.NUMBER_OF_COLUMNS][GomokuUtilities.NUMBER_OF_ROWS];
	  previousBestMove= new int[3];
  }

  public String getName (){
    return "Heuristic";
  }
  public int getId (){
    return id;
  }
  public void setScore (int score){
    this.score = score;
  }
  public int getScore (){
    return score;
  }
  public void setId (int id){
    this.id = id;
  }
  public void setName (String name){
    this.name = name;
  }
  
  /**Follows the suggested algorithm, only the use of an ArrayList is superfluous.
   * It is included simply for completeness.
   * @return the highest-evaluated move (x,y)
   **/
  public int[] getNextMove_all (Board board)
  {
	 //ArrayList<int[]> evaluatedPos= new ArrayList<int[]>();
	 int[] singleEvalPos= new int[3], bestMove= new int[3];
	 bestMove[2]= Integer.MIN_VALUE;
	 //How to get nRows, nColumns?
	 for(int x=0; x<GomokuUtilities.NUMBER_OF_COLUMNS; x++)
		 for(int y=0; y<GomokuUtilities.NUMBER_OF_ROWS; y++){
			 singleEvalPos[0]= x; singleEvalPos[1]= y;			//HATE Java for not allowing me to do this in the <for>!!!
			 singleEvalPos[2]= evaluate(x,y, board);
			 if (bestMove[2] < singleEvalPos[2])
				 bestMove= (int[]) singleEvalPos.clone();		//My exasperated intent is to DEEP COPY the old object into the new.
			 												//Tell me, dear Java, is this too much to ask of you??? :(
			 //evaluatedPos.add(singleEvalPos);
		 }
	 //Why did you tell us to use ArrayList???
	 return bestMove;
  }
  
  /**Checks for new evaluations only within the region of interest. If it works,
   * it might replace the other version of getNextMove
   * @return The highest-evaluated move on the board
   */
  public int[] getNextMove(Board board){
	  int[] lLimit= new int[2], uLimit= new int[2];		//Lower-upper limits that define a rectangle on the board
	  //If we want to evaluate the whole board...
	  if (reevaluate){
		  lLimit[0]=lLimit[1]= 0;
		  uLimit[0]= GomokuUtilities.NUMBER_OF_COLUMNS;
		  uLimit[1]= GomokuUtilities.NUMBER_OF_ROWS;
	  } else{											//...or a subsection thereof
		  int[] prevMove= GomokuUtilities.getPreviousMove();
		  lLimit[0]= prevMove[0]-roi; uLimit[0]= prevMove[0]+roi;
		  lLimit[1]= prevMove[1]-roi; uLimit[1]= prevMove[1]+roi;
	  }
	  
	  int[] bestMove= new int[3];
	  bestMove[2]= Integer.MIN_VALUE;
	  for(int x=lLimit[0]; x<uLimit[0]; x++)
		  for(int y=lLimit[1]; y<uLimit[1]; y++){
			  previousEvals[x][y]= evaluate(x, y, board);
			  if (bestMove[2] < previousEvals[x][y]){
				  bestMove[0]= x; bestMove[1]= y;
				  bestMove[2]= previousEvals[x][y];
			  }
		  }
	  
	  return (bestMove[2] > previousBestMove[2])? bestMove: previousBestMove;
  }
  
  /**@brief Heuristic evaluation function. It takes into account the potential
   * to make 2,3,4,5-tuples, the domination difference between the 2 players in
   * close proximity to (x,y) and the centrality of the position (measured in 
   * how many possible quintuples occupy this position.
   * @return A value that assesses the winning potential for making the (x,y) move
   */
  int evaluate (int x, int y, Board board){
  	  //Check if I or the opponent makes quintuple (depth-1 victory condition)
	  //These are the highest priority positions
	  if (makesNTuple(5, x,y,board, id) >= 1) return Integer.MAX_VALUE;
	  if (makesNTuple(5, x,y,board, oppID()) >= 1) return Integer.MAX_VALUE-1;
	  
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
	  double[] w= {0.5,1,1.5, 2.6, 1.2,2.3,6};
	  int evaluation= (int)(
			  w[0]*range2DColor + w[1]*range3DColor + w[2]*range4DColor +
			  w[3]*(5*partakesIn5Tuples(x,y,board)) + 
			  w[4]*(50*makesNTuple(2,x,y,board,id)) + 
			  w[5]*(50*makesNTuple(3,x,y,board,id)) + 
			  w[6]*(50*makesNTuple(4,x,y,board,id))
			  );	  
	  return evaluation;
  }
  
  /**@brief Checks if putting a tile of the specified player at the specified
   * position on the board makes an N-tuple for that player. Domain: [0,8] but
   * usually <=2
   * @param N Whether looking for 5-tuples, 4-tuples, 3-tuples or 2-tuples
   * @param x position
   * @param y position
   * @param board
   * @param pid The player whose mark is to be put on the board at position (x,y)
   * @return How many N-tuples it makes
   */
  int makesNTuple(int N, int x, int y, Board board, int pid){
	  // TODO
	  if(N<2 || N>5) return 0;
	  return 1;
  }
  
  /**Given the game state, if a tile is put at (x,y), in how many quintuples max
   * could it possibly participate in the future? It is a better and dynamic 
   * measure of the "centrality" of the position. Domain: [0,20]
   * @param x
   * @param y
   * @param board
   * @return The total number of quintuples that the position partakes in
   * (by row, column, left & right diagonal)
   */
  int partakesIn5Tuples(int x, int y, Board board){
	  // TODO
	  return 0;
  }
  
  int oppID() {return (id==1)? 2: 1;}
}
