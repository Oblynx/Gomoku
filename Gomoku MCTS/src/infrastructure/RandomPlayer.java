package infrastructure;


public class RandomPlayer implements AbstractPlayer
{
  // TODO Fill the class code.

  int score;
  int id;
  String name;

  public RandomPlayer (Integer pid)
  {
    id = pid;
    score = 0;

  }
 
  public RandomPlayer(Integer pid, int pscore, String pname){
	  score = pscore;
	  id = pid;
	  name=pname;
	  
  }

  public String getName ()
  {

    return "Random";

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
    // TODO Auto-generated method stub
    this.id = id;

  }

  public void setName (String name)
  {
    // TODO Auto-generated method stub
    this.name = name;

  }

  public int[] getNextMove (Board board)
  {

    // board.printBoard();
    int[] placedTile = new int[2];
    
    placedTile[0] = (int) (Math.random()*15);
    placedTile[1] = (int) (Math.random()*15);

   
    
    //System.out.println(placedTile[0]+" "+placedTile[1]);
    
  
    

    return placedTile;

  }

}
