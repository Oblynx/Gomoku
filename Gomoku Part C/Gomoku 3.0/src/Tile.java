

public class Tile implements Cloneable
{

  private int color;
  private boolean mark;
  protected int id;
  private int x;
  private int y;
  private int playerId;

  public Tile ()
  {

  }

  public Tile (int tid, int tx, int ty, int tcolor, boolean tmark, int tPlayerId)
  {
    x = tx;
    y = ty;
    id = tid;
    color = tcolor;
    mark = tmark;
    playerId = tPlayerId;
    
  }

  public void setMark (boolean mark)
  {
    this.mark = mark;
  }

  public boolean getMark ()
  {
    return mark;
  }

  public int getX ()
  {
    return x;
  }

  public int getY ()
  {
    return y;
  }

  public int getId ()
  {
    return id;
  }

  public int getColor ()
  {
    return color;
  }
  
  public int getPlayerId()
  {
    return playerId;
  }

  public void setX (int x)
  {
    this.x = x;
  }

  public void setY (int y)
  {
    this.y = y;
  }

  public void setColor (int color)
  {
    this.color = color;
  }

  public void setId (int id)
  {
    this.id = id;
  }
  
  public void setPlayerId (int id)
  {
    this.playerId = id;
  }
  

}
