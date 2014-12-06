import java.util.ArrayList;

public class HeuristicPlayer implements AbstractPlayer
{

  int score;
  int id;
  String name;

  public HeuristicPlayer (Integer pid){
    id = pid;
    score = 0;
  }
  public HeuristicPlayer(Integer pid, int pscore, String pname){
	  score = pscore;
	  id = pid;
	  name=pname;
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
   */
  public int[] getNextMove (Board board)
  {
	 /**ArrayList<int[]> evaluatedPos= new ArrayList<int[]>();**/
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
			 /**evaluatedPos.add(singleEvalPos);**/
		 }
	 //Why did you tell us to use ArrayList???
	 return bestMove;
  }
  
  /**@brief Heuristic evaluation function
   * @return A value that assesses the winning potential for making the (x,y) move
   */
  int evaluate (int x, int y, Board board){
  	  //Check if I or the opponent makes quintuple (depth-1 victory condition)
	  if (makesQuint(x,y,board, id)) return Integer.MAX_VALUE;
	  if (makesQuint(x,y,board, oppID())) return Integer.MAX_VALUE-1;
	  
	  
	  return 0;
  }
  
  boolean makesQuint(int x, int y, Board board, int pid){
	  // TODO
  }
  
  int oppID() {return (id==1)? 2: 1;}
}
