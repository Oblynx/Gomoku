package infrastructure;


public interface AbstractPlayer
{
  public void setId (int id);

  public int getId ();

  public void setName (String name);

  public String getName ();

  public void setScore (int score);

  public int getScore ();

  public int[] getNextMove (Board board);

}
