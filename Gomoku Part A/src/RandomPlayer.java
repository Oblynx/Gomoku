
public class RandomPlayer implements AbstractPlayer
{
    private int id, score;
    private String name;

    public RandomPlayer(Integer pid) {
	id = pid.intValue();
    }

    public RandomPlayer(Integer pid,String name0,int score0) {
	id = pid.intValue();
	name = name0;
	score = score0;
    }

    ///All get methods follow the form _<property>()
    ///and all set methods <property>_(newValue)
    public int getId() {return id;}
    public void setId(int a) {id=a;}
    public String getName() {return name;}
    public void setName(String a) {name=a;}
    public int getScore() {return score;}
    public void setScore(int a) {score=a;}
    public int[] getNextMove( Board board ) {
	int[] xy = new int[2];

	do {
	    xy[0] = (int)(Math.random()*15);
	    xy[1] = (int)(Math.random()*15);
	}while (board.getTile(xy[0],xy[1]).getColor() != 0 );
	
	return xy;
    }
}
