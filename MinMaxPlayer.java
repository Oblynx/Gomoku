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
  int[][] previousEvals;
  int[] previousBestMove;
  boolean reevaluate;

  public MinMaxPlayer (Integer pid){
	id = pid;
	score = 0;
	reevaluate= true;
	previousEvals= new int[GomokuUtilities.NUMBER_OF_COLUMNS][GomokuUtilities.NUMBER_OF_ROWS];
	previousBestMove= new int[3];
	previousBestMove[2]= 0;
  }
  public MinMaxPlayer(Integer pid, int pscore, String pname){
	  score = pscore;
	  id = pid;
	  name=pname;
	  reevaluate= true;
	  previousEvals= new int[GomokuUtilities.NUMBER_OF_COLUMNS][GomokuUtilities.NUMBER_OF_ROWS];
	  previousBestMove= new int[3];
	  previousBestMove[2]= 0;
  }

  public String getName (){ return "MinMax"; }
  public int getId (){ return id; }
  public void setScore (int score){ this.score = score; }
  public int getScore (){ return score; }
  public void setId (int id){ this.id = id; }
  public void setName (String name){ this.name = name; }
  
  public int oppID() {return (id==1)? 2: 1;}

  public int[] getNextMove ( Board board)
  {
      int x, y;

      

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
      int i, x, y, player;
      ArrayList<Node> MinMax = new ArrayList<Node>();
      Node current, neib, par;
      Board tmp;

      neib = new Node();
      
      MinMax.add(root);
      for ( i=0; i<MinMax.size(); ++i ) {
	  current = MinMax.get(i);
	  player = current.getDepth() % 2 == 1 ? id : oppID();

	  if ( GomokuUtilities.checkForWin( id, current.getBoard()) ) {
	      current.setEval ( Integer.MAX_VALUE );
	      MinMax.set(i,current);
	      continue;
	  }
	  else if ( GomokuUtilities.checkForWin( oppID() , current.getBoard()) ) {
	      current.setEval ( Integer.MAX_VALUE -1 );
	      MinMax.set(i,current);
	      continue;
	  }

	  //Find Neighbours
	  for ( x=0; x<GomokuUtilities.NUMBER_OF_COLUMNS; ++x ) {
	      for ( y=0; y<GomokuUtilities.NUMBER_OF_ROWS; ++y ) {
		  if ( current.getBoard().getTile(x,y).getColor() == 0 ) {
		      tmp = GomokuUtilities.cloneBoard(current.getBoard());
		      GomokuUtilities.playTile( tmp,x,y,player );
		      neib.setBoard( tmp );
		      neib.setDepth( current.getDepth() + 1 );
		      neib.setPar( i );
		      neib.setMove(x,y);
		      MinMax.add(neib);
		  }
	      }
	  }
      }

      //Backtracking
      for ( i=MinMax.size()-1; i>=1; --i ) {
	  current = MinMax.get(i);
	  par = MinMax.get( current.getPar() );
	  par.Update( current );
	  MinMax.set( current.getPar(), par );
      }
  }

}
