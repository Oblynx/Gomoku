public class MinMaxPlayer implements AbstractPlayer
{
  // TODO Fill the class code.

  int score;
  int id;
  String name;

  public MinMaxPlayer (Integer pid)
  {
    id = pid;
    score = 0;
  }

  public String getName ()
  {

    return "MinMax";

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

  public int[] getNextMove ( Board board)
  {

    // TODO Fill the code

   

    int[] bestTile = {0,0};

    return bestTile;
    
  }

  private void createMySubTree (Node parent, int depth)
  {
    // TODO Fill the code
  }

  private void createOpponentSubTree (Node parent, int depth)
  {
    // TODO Fill the code
  }

  private int chooseMove (Node root)
  {

    // TODO Fill the code
  }

}
