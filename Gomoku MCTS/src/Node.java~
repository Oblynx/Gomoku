
public class Node
{
    private int par, dep, x, y, eval;
    private Board board;
    
    public Node() {
    }

    public int getDepth() {
	return dep;
    }
    public int getPar() {
	return par;
    }
    public int getEval() {
	return eval;
    }
    public Board getBoard() {
	return board;
    }

    public void setDepth( int d ) {
	dep = d;
    }
    public void setPar( int p ) {
	par = p;
    }
    public void setEval ( int e ) {
	eval = e;
    }
    public void setBoard ( Board b ) {
	board = b;
    }
    public void setMove ( int _x, int _y ) {
	x = _x;
	y = _y;
    }

    public void Update ( Node child ) {
	if ( dep % 2 == 0 && child.getEval() > eval ) {
	    eval = child.getEval();
	    if ( dep == 0 ) {
		setMove ( child.x, child.y );
	    }
	}
	if ( dep % 2 == 1 && child.getEval() < eval ) {
	    eval = child.getEval();
	}
    }

    
}
