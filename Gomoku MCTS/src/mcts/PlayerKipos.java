package mcts;

import java.util.ArrayList;

class Player {
    ArrayList<Node> tree = new ArrayList<Node>();
    MctsBoard board = new MctsBoard();

    //What to do with terminal nodes?
    //First move? Initialization of Arrayliste<Node> tree?
    public int[] getNextMove(Board b) {
		int victory, N, i;
		Node nextNode, best;
	
		//Keep only useful part of the tree
		updateTree();
	
		//Monte Carlo
		//Replace N with time
		N = 10;
		for ( i=1; i<=N; ++i ) {
		    nextNode = Selection();
		    victory = Expand(nextNode,0);
		    //backpropagate last inserted node of tree
		    BackPropagate( tree.get( tree.size() - 1 ), victory );
		    //backpropagate also erases the board to the root point
		}
	
		best = tree.get(1);
		for ( i=2; i<tree.size(); ++i ) {
		    //Ratio of best smaller than ratio of i
		    if ( best.getVi() * tree.get(i).getNi() < tree.get(i).getVi() / best.getNi() ) {
			 best = tree.get(i);
		    }
		}
	
		//Update the board to know our move, and replace root
		//We have 2 references to best, that's not a problem
		board.makeMove( best.getMove(), pid );
		best.setIsRoot(true);
		tree.set(0,best);
		return best.getMove();
    }

    int other ( int player ) {
    	return player==1?2:1;
    }

    void updateTree(int[] move) {
		int i, j, found;
		Node current;
		
		board.makeMove( move, other(pid) );
	
		//Keep only the subtree rooted at the child of root, with move[], if it exists
		found = -1;
		for ( i=0; i<tree.get(0).children.size(); ++i ) {
		    if ( tree.get(0).children.get(i).move[0] == move[0] && tree.get(0).children.get(i).move[1] == move[1] ) {
			found = i;
		    }
		}
	
		if ( found == -1 ) {
		    tree.clear(); // maybe it is erase, or wipeout, to be checked
		    current = new Node(move,true,0,0,pid,NULL); //null???
		}
		else {
		    ArrayList<Node> queue = new ArrayList<Node>();
		    queue.add( tree.get(found) );
		    //BFS to find all the useful ones
		    for ( i=0; i<queue.size(); ++i ) {
			for ( j=0; j<queue.get(i).children.size(); ++j ) {
			    queue.add( queue.get(i).children.get(j) );
			}
		    }
	
		    tree.clear(); // maybe it is erase, or wipeout, te be checked
		    tree = queue; // maybe need to say reference of tree = reference of queue
		}
    }
    
    Node Selection() {
		int i;
		Node up, leaf;
		
		//to-do ucb for root
		up = tree.get(0);
		
		//find max ucb
		for ( i=1; i<tree.size(); ++i ) {
		    if ( up.ucb() < tree.get(i).ucb() ) {
			up = tree.get(i);
		    }
		}
	
		//Found it, create the board
		leaf = up;
		while ( !up.isRoot() ) {
		    board.makeMove( up.getMove(), up.getPid() );
		    up = up.getParent();
		}
		
		//Finally Expand
		return leaf;
    }

    int[] Lahiri ( Node v ) {
		//Later check only 5x5 around last move, and 5x5 around opponent's move
		//Create an expansion node from each Region of Interest ( roi )
		int x, y, rand_pdf, tmp, maxim;
		int[] ans = new int[2];
	
		for ( x=0; x<Const.columns; ++x ) {
		    for ( y=0; y<Const.columns; ++y ) {
				if ( board.getColor(x,y) == 0 ) {
				    tmp = board.evaluate(x,y);
				    if ( tmp > maxim ) {
					maxim = tmp;
				    }
				}
		    }
		}
	
		do {
		    ans[0] = (int)(Math.random()*Const.columns);
		    ans[1] = (int)(Math.random()*Const.rows);
		    rand_pdf = (int)(Math.random()*maxim);
		}while ( board.evaluate(x,y) < rand_pdf );
	
		return ans;
    }

    //Returns 1 if the path was a winning one, 0 otherwise
    int Expand( Node v, int depth ) {
		int[] nextMove = Lahiri(v);
		int ans;
		
		Node next = new Node(nextMove,0,0,other(v.getPid()) ,v);
		board.makeMove(nextMove, other(v.getPid()) );
		
		if ( depth == 0 ) {
		    v.setChild(next);
		    tree.add(next);
		}
	
		ans = Expand(next, depth+1);
		return ans;
    }

    void BackPropagate( Node v, int victory ) {
		while ( !v.isRoot() ) {
		    v.setNi( v.getNi() + 1 );
		    v.setVi( v.getVi() + victory );
		    board.makeMove(v.getMove(),0);
		}
	
		v.setNi( v.getNi() + 1 );
		v.setVi( v.getVi() + victory );
    }
    
}
