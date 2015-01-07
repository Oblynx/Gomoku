import java.util.ArrayList;
/**
 * @author Konstantinos Samaras-Tsakiris | kisamara@auth.gr    | AEM: 7972
 * @author Vangelis Kipouridis           | kipoujr@hotmail.com | AEM: 7899
 */
public class MinMaxPlayer implements AbstractPlayer
{

    int score;
    int id;
    String name;

    public MinMaxPlayer (Integer pid){
	id = pid;
	score = 0;
    }
    public MinMaxPlayer(Integer pid, int pscore, String pname){
	score = pscore;
	id = pid;
	name=pname;
    }

    public String getName (){ return "MinMax"; }
    public int getId (){ return id; }
    public void setScore (int score){ this.score = score; }
    public int getScore (){ return score; }
    public void setId (int id){ this.id = id; }
    public void setName (String name){ this.name = name; }
  
    public int oppID() {return (id==1)? 2: 1;}

    public int[] getNextMove ( Board board )
    {
	//Update board
	

      
      

    }

    private void createMySubTree (Node parent, int depth)
    {
	// TODO Fill the code
    }

    private void createOpponentSubTree (Node parent, int depth)
    {
	// TODO Fill the code
    }

    private int DFS (Node root)
    {

    }

}
