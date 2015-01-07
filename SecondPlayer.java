import java.util.ArrayList;

public class SecondPlayer implements AbstractPlayer {
    ArrayList<MctsNode> tree = new ArrayList<MctsNode>();
    MctsBoard board;
    int score;
    int pid;
    String name;
    int move;

    public SecondPlayer (Integer _pid){
	pid = _pid;
	score = 0;
	name = "Second";
    }
    public SecondPlayer(Integer _pid, int pscore, String pname){
	score = pscore;
	pid = _pid;
	name=pname;
    }

    public String getName (){ return "Second"; }
    public int getId (){ return pid; }
    public void setScore (int score){ this.score = score; }
    public int getScore (){ return score; }
    public void setId (int _pid){ this.pid = _pid; }
    public void setName (String name){ this.name = name; }
    
    public int[] getNextMove(Board b) {
	//Don't need new board, just getPreviousMove() from GomokuUtilities
	int victory, N, i, x, y, tile, size, bestEval, xEval, yEval, tmp;
	MctsNode nextNode, best;
	boolean firstMove, playSecond;
	ArrayList<MctsNode> neibs;

	//Find if it is the first move ( previous game ended or first game )
	firstMove = true;
	playSecond = false;
	for ( x=0; x<Const.columns; ++x ) {
	    for ( y=0; y<Const.rows; ++y ) {
		tile = b.getTile(x,y).getPlayerId();
		if ( tile == pid ) {
		    firstMove = false;
		}
		else if ( tile == other(pid) ) {
		    playSecond = true;
		}
	    }
	}

	if ( firstMove ) {
	    move = 1;
	    tree.clear();
	    board = new MctsBoard();
	    
	    if ( playSecond ) {
		board.makeMove( GomokuUtilities.getPreviousMove(), other(pid) );
	    }
	    int[] move = {0,0};
	    MctsNode first = new MctsNode( move, true, 0, 0, pid, null );
	    tree.add(first);
	}
	else {
	    //Keep only useful part of the treen
	    ++move;
	    updateTree(GomokuUtilities.getPreviousMove());
	}

	//Find if the move is definite, no need for Monte Carlo
	bestEval = -1;
	xEval = yEval = -1;
	for ( x=0; x<Const.columns; ++x ) {
	    for ( y=0; y<Const.rows; ++y ) {
		if ( board.getColor(x,y) == 0 ) {
		    tmp = board.evaluate(x,y,pid);
		    if ( tmp > bestEval ) {
			bestEval = tmp;
			xEval = x;
			yEval = y;
		    }
		}
	    }
	}

	if ( move <= 100 || bestEval >= Integer.MAX_VALUE-5 ) {
	    int[] move = {xEval, yEval};
	    System.out.println(bestEval);
	    best = new MctsNode ( move,true,0,0,pid,null );
	}
	else {
	    //Monte Carlo
	    //---------------Replace N with time
	    N = 2000;
	    for ( i=1; i<=N; ++i ) {
		nextNode = Selection();

		size = tree.size();
		victory = Expand(nextNode,0,nextNode);
	    
		//backpropagate last inserted node of tree, if some node was inserted
		if ( tree.size() == size+1 ) {
		    BackPropagate( tree.get( tree.size() - 1 ), victory );
		}
		else {
		    BackPropagate( nextNode, victory );
		}
		//backpropagate also erases the board to the root point
	    }

	    neibs = tree.get(0).getChildren();
	    best = neibs.get(0);
	    for ( i=1; i<neibs.size(); ++i ) {
		//Ratio of best smaller than ratio of i
		if ( best.getVi() * neibs.get(i).getNi() < neibs.get(i).getVi() * best.getNi() ) {
		    best = tree.get(i);
		}
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
	MctsNode current;
	
	board.makeMove( move, other(pid) );

	//Keep only the subtree rooted at the child of root, with move[], if it exists
	found = -1;
	for ( i=0; i<tree.get(0).children.size(); ++i ) {
	    if ( tree.get(0).children.get(i).move[0] == move[0] && tree.get(0).children.get(i).move[1] == move[1] ) {
		found = i;
		break;
	    }
	}

	if ( found == -1 ) {
	    tree.clear();
	    current = new MctsNode(move,true,0,0,pid,null);
	    tree.add(current);
	}
	else {
	    ArrayList<MctsNode> queue = new ArrayList<MctsNode>();
	    queue.add( tree.get(found) );
	    //BFS to find all the useful ones
	    for ( i=0; i<queue.size(); ++i ) {
		for ( j=0; j<queue.get(i).children.size(); ++j ) {
		    queue.add( queue.get(i).children.get(j) );
		}
	    }

	    tree.clear();
	    tree = queue;
	}
    }
    
    MctsNode Selection() {
	int i;
	MctsNode up, leaf;
	
	//******** to-do ucb for root
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
	    board.makeMove( up.getMove(), other(up.getPid()) );
	    up = up.getParent();
	}
	
	//Finally Expand
	return leaf;
    }

    int[] Lahiri ( MctsNode v ) {
	int x, y, rand_pdf, i;
	MctsNode neib;

	int[] ans = new int[3];
	int[][] pdf = new int[Const.columns][Const.rows];

	for ( i=0; i<v.getChildren().size(); ++i ) {
	    neib = v.getChildren().get(i);
	    pdf[ neib.getMove()[0] ][ neib.getMove()[1] ] = -1;
	}

	ans[2] = -1;
	for ( x=0; x<Const.columns; ++x ) {
	    for ( y=0; y<Const.columns; ++y ) {
		if ( pdf[x][y] != -1 && board.getColor(x,y) == 0 ) {
		    pdf[x][y] = board.evaluate(x,y,v.getPid());
		    
		    if ( pdf[x][y] > ans[2] ) {
			ans[2] = pdf[x][y];
			ans[0] = x;
			ans[1] = y;
		    }
		}
		else {
		    pdf[x][y] = -1;
		}
	    }
	}


	
	//No need for random playout if the move is definite
	//****If there is only one move, say it, so we never get a full board!	
	//Win-Lose-Tie
	if ( board.getAvailableMoves() == 1 ) {
	    ans[2] = -1;
	}
	if ( ans[2] >= Integer.MAX_VALUE - 5 || ans[2] == -1 ) {
	    return ans;
	}

	do {
	    ans[0] = (int)(Math.random()*Const.columns);
	    ans[1] = (int)(Math.random()*Const.rows);
	    rand_pdf = (int)(Math.random()*ans[2]);
	}while ( pdf[ ans[0] ][ ans[1] ] < rand_pdf );

	return ans;
    }

    //Returns 1 if the path was a winning one, 0 otherwise
    int Expand( MctsNode v, int depth, MctsNode lastGood ) {
	int[] lahirion = Lahiri(v);
	int[] nextMove = {lahirion[0],lahirion[1]};
	int ans = -1;

	if ( lahirion[2] == Integer.MAX_VALUE ) {
	    ans = v.getPid()==pid?1:0;
	}
	if ( lahirion[2] == -1 ) {
	    ans = 0;
	}

	if ( ans == -1 ) {
	    MctsNode next = new MctsNode(nextMove,false,0,0,other(v.getPid()) ,v);
	    //v.getPid() is : Who is ABOUT to make a move, not who MADE the last move
	    board.makeMove(nextMove, v.getPid() );
	
	    if ( depth == 0 ) {
		v.setChild(next);
		tree.add(next);
		lastGood = next;
	    }

	    ans = Expand(next, depth+1, lastGood);
	}
	else {
	    //Revert board to the point that BackPropagation takes control
	    //Do not update nodes, just revert
	    while ( v != lastGood ) {
		board.makeMove(v.getMove(),0);
		v = v.getParent();
	    }
	}
	return ans;
    }

    void BackPropagate( MctsNode v, int victory ) {
	while ( !v.isRoot() ) {
	    v.setNi( v.getNi() + 1 );
	    v.setVi( v.getVi() + victory );
	    board.makeMove(v.getMove(),0);
	    v = v.getParent();
	}

	v.setNi( v.getNi() + 1 );
	v.setVi( v.getVi() + victory );
    }
    
}
