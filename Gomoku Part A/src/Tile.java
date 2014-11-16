 
public class Tile implements Cloneable
{
	private	int id, x, y, color, playerId;
	private boolean mark;
	
	public Tile(int id0, int x0, int y0, int col0, boolean mark0, int plId0){
		id=id0;
		x=x0; y=y0;
		color=col0;
		mark=mark0;
		playerId=plId0;
	}
	
	public int getId() {return id;}
	public void setId(int a) {id=a;}
	public int getX() {return x;}
	public void setX(int a) {x=a;}
	public int getY() {return y;}
	public void setY(int a) {y=a;}
	public int getColor() {return color;}
	public void setColor(int a) {color=a;}
	public int getPlayerId() {return playerId;}
	public void setPlayerId(int a) {playerId=a;}
	public boolean getMark() {return mark;}
	public void setMark(boolean a) {mark=a;}
	
}
