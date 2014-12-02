


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
  
  }
  
  int evaluate (int x, int y, Board board){
  	  // TODO
  }

}
