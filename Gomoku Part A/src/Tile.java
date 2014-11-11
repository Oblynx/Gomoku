 
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
	/// All get methods follow the form _<property>()
	/// and all set methods <property>_(newValue)
	public int _id() {return id;}
	public void id_(int a) {id=a;}
	public int _x() {return x;}
	public void x_(int a) {x=a;}
	public int _y() {return y;}
	public void y_(int a) {y=a;}
	public int _color() {return color;}
	public void color_(int a) {color=a;}
	public int _playerId() {return playerId;}
	public void playerId_(int a) {playerId=a;}
	public boolean _mark() {return mark;}
	public void mark_(boolean a) {mark=a;}
	
}
