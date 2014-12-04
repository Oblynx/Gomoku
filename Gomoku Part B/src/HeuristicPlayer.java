import java.util.ArrayList;

public class HeuristicPlayer implements AbstractPlayer
{

  int score;
  int id;
  String name;

  public HeuristicPlayer (Integer pid)
  {
    id = pid;
    score = 0;

  }
 
  public HeuristicPlayer(Integer pid, int pscore, String pname){
	  score = pscore;
	  id = pid;
	  name=pname;
	  
  }

  public String getName ()
  {

    return "Heuristic";

  }

  public int getId ()
  {
    return id;
  }

  public void setScore (int score)
  {
    this.score = score;
  }

  public int getScore ()
  {
    return score;
  }

  public void setId (int id)
  {
   
    this.id = id;

  }

  public void setName (String name)
  {
    
    this.name = name;

  }

  public int[] getNextMove (Board board)
  {
	 // TODO
	 ArrayList<int[]> evaluatedPos= new ArrayList<int[]>();
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
			 evaluatedPos.add(singleEvalPos);
		 }
	 //Why did you tell us to use ArrayList???
	 return bestMove;
  }
  
  int evaluate (int x, int y, Board board){
  	  // TODO
	  
	  
	  return 0;
  }

}
