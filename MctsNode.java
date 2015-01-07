import java.util.ArrayList;

public class MctsNode {
	int[] move= new int[2];		//From parent to this node
	boolean isRoot;
	int ni, vi;
	int pid; 					//Who is about to make a move
	MctsNode parent;				//Reference to parent. This is important for the copy constructor
    private double c=1.414213;
    ArrayList<MctsNode> children;	//References
	
	//Default
    MctsNode(int _pid) {
	isRoot = true;
	ni = 0;
	vi = 0;
	pid = _pid;
    }
    MctsNode(int[] _move, boolean _isRoot, int _ni, int _vi, int _pid, MctsNode v){
	move = _move;
	isRoot = _isRoot;
	if ( isRoot && _ni == 0 ) {
	    ni = 1;
	}
	else {
	    ni = _ni;
	}
	vi = _vi;
	pid = _pid;
	parent = v;
	children = new ArrayList<MctsNode>();
	}
	
	//Copy constructor
	MctsNode(MctsNode that){
		move= that.move.clone();
		isRoot= that.isRoot;
		ni= that.ni; vi= that.vi;
		pid= that.pid;
		parent= that.parent;	//It's only a reference after all
		children= that.children;//Same
		c= that.c;
	}

    boolean isRoot() {
	return isRoot;
    }

    int[] getMove() {
	return move;
    }

    int getNi() {
	return ni;
    }

    int getVi() {
	return vi;
    
    }
    int getPid() {
	return pid;
    }

    MctsNode getParent() {
	return parent;
    }

    ArrayList<MctsNode> getChildren() {
	return children;
    }
	
    void setNi(int _ni) {
	ni = _ni;
    }

    void setVi(int _vi) {
	vi = _vi;
    }
    
    void setChild(MctsNode child) {
	children.add(child);
    }
    
    void setIsRoot(boolean _isRoot) {
	isRoot = _isRoot;
	if ( isRoot ) {
	    parent = null;
	}
    }
    
    double ucb() {
    	//May need to change the if's, I did it just to have some value
    	if ( parent == null ) {
	    return vi/ni + c*Math.sqrt(Math.log(ni)/ni);
    	}
    	return vi/ni + c*Math.sqrt(Math.log(parent.getNi())/ni);
    }

}
