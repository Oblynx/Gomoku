/*
AEM : 7972 / 7899
Tel : 6998581293 / Den exo
email : kisamara@auth.gr / kipoujr@hotmail.com
Onomata : Konstantinos Samaras Tsakiris / Kipouridis Vangelis
*/
 
public class Tile implements Cloneable
{
	//Tile::properties
	private	int id, x, y, color, playerId;
	private boolean mark;
	
	/**The Tile class constructor
	 * @param id0
	 * @param x0
	 * @param y0
	 * @param col0
	 * @param mark0
	 * @param plId0
	 */
	public Tile(int id0, int x0, int y0, int col0, boolean mark0, int plId0){
		id=id0;
		x=x0; y=y0;
		color=col0;
		mark=mark0;
		playerId=plId0;
	}
	
	/** The Tile class reknowned get/set methods
	 * @return
	 */
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
//That's all, thank you.
