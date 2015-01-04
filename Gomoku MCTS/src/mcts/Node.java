package mcts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.lang.Math;
import mcts.Common.*;

class Node {
	//General constructor
	Node(int[] move1, Node parent1, int n1, int v1, int pid1){
		move= move1;
		parent= parent1;
		children= new ArrayList<Node>();
		ni= n1; vi= v1; pid= pid1;
		isRoot= false; hasParent= true; simMode= false;
	}
	
	/**Root constructor*/
	Node(MctsBoard rootBoard1, int pid1, int n1, int v1){
		isRoot= true; hasParent= false; simMode= false;
		children= new ArrayList<Node>();
		parent= null;						//DEBUG Is this possible?
		rootBoard= rootBoard1;
		pid= pid1; ni= n1; vi= v1;
	}
	
	/**Root with "parent" constructor (here, parent shows move history)*/
	Node(MctsBoard rootBoard1, int pid1, int n1, int v1, int[] move1, Node parent1){
		isRoot= true; hasParent= true; simMode= false;
		children= new ArrayList<Node>();
		rootBoard= rootBoard1;
		pid= pid1; ni= n1; vi= v1;
		move= move1;
		parent= parent1;
	}
	
	/**Copy constructor*/
	Node(Node that){
		move= that.move.clone();
		isRoot= that.isRoot;
		hasParent= that.hasParent;
		simMode= that.simMode;
		ni= that.ni; vi= that.vi;
		pid= that.pid;
		parent= that.parent;		//It's only a reference after all
		children= that.children;	//Same here
		if (isRoot) rootBoard= new MctsBoard(that.rootBoard);
	}
	
	/**Generates a child of the current node by doing the <code>move</code>.
	 * @return The child (unlinked to the parent).
	 */
	Node child(int[] move){
		Node c= new Node(move, this, 0, 0, oppID()); 
		return c;
	}
	/**Links the node with its parent.*/
	void link(){parent.children.add(this);}
	
	/**@return A <b>copy of</b> the represented board*/
	MctsBoard getBoard(){
		Stack<int[]> moveHistory= new Stack<int[]>();
		Node tmpNode= this;
		while (!tmpNode.hasBoard()){
			moveHistory.push(tmpNode.move);
			tmpNode= tmpNode.parent;
		}
		MctsBoard board= new MctsBoard(tmpNode.rootBoard);
		int rootId= tmpNode.pid, otherId= tmpNode.oppID();
		for(int i=0; !moveHistory.isEmpty(); i++){
			if (i%2==0)	board.makeMove(moveHistory.pop(), rootId);
			else board.makeMove(moveHistory.pop(), otherId);
		}
		return board;
	}
	
	/**Prepares a node to enter simulation mode, by storing the represented
	 * board state for quicker access
	 */
	void entersSim(){
		simMode= true;
		rootBoard= getBoard();
	}
	/**<b>ucb</b> = <i>winrate</i> + c* sqrt(log(<i>parent_visits</i>)/<i>visits</i>)*/
	float ucb(){
		return (float)(((double)vi)/ni + c*Math.sqrt(Math.log(parent.ni)/ni));
	}
	
	/**Calculates available moves <b>within <i>roi</i></b> around last move made
	 * by each player. 
	 */
	int availableMovesRoi(){
		int availableMovesRoi= 0;
		int[] xRange= {-Const.roi+move[0], Const.roi+move[0]};
		int[] yRange= {-Const.roi+move[1], Const.roi+move[1]};
		availableMovesRoi+= getBoard().availableMoves(xRange, yRange);
		
		int[] myPrevMove= parent.move;
		int[] xRange2= {-Const.roi+myPrevMove[0], Const.roi+myPrevMove[0]};
		int[] yRange2= {-Const.roi+myPrevMove[1], Const.roi+myPrevMove[1]};
		availableMovesRoi+= getBoard().availableMoves(xRange2, yRange2);
		return availableMovesRoi;
	}
	/**
	 * @throws IllegalStateException if used on a Node that doesn't have a pre-
	 * computed board associated. That is, if <code>!simMode && !isRoot</code>.
	 */
	void makeMove(int[] playerMove, int pid) throws IllegalStateException{
		if (hasBoard()) rootBoard.makeMove(playerMove, pid);
		else throw new IllegalStateException("No board precomputed!");
	}
	boolean checkWin(int[] pMove, int pid){
		if (getBoard().getColor(pMove[0], pMove[1]) != -1)	//If (x,y) in bounds...
			return (Heuristics.makesNTuples(5,pMove,getBoard(),pid) > 0) ?
					true : false;
		else return false;
	}
	
	HashMap eval() throws IllegalStateException{
		if (!isRoot) throw new IllegalStateException("No board precomputed!");
		//Local class
		class HeuristicEval implements BoardOperation{
			HashMap<int[], Integer> evalMap;
			HeuristicEval(){
				evalMap= new HashMap<int[], Integer>();
			}
			@Override
			public void operate(int x, int y, int color) {
				if (color == 0) evalMap.put(
						new int[]{x,y},
						Heuristics.heuristicEvaluate(new int[]{x,y}, rootBoard, pid));
				else evalMap.put(new int[]{x,y}, 0);
			}
		}
		HeuristicEval evaluator= new HeuristicEval();
		
		//Evaluate around last opponent's move
		int[] xRange1={move[0]-Const.roi, move[0]+Const.roi};
		int[] yRange1={move[1]-Const.roi, move[1]+Const.roi};
		rootBoard.operateSelection(xRange1, yRange1, evaluator);
		//Evaluate around last own move
		int[] xRange2={parent.move[0]-Const.roi, parent.move[0]+Const.roi};
		int[] yRange2={parent.move[1]-Const.roi, parent.move[1]+Const.roi};
		rootBoard.operateSelection(xRange2, yRange2, evaluator);
		return evaluator.evalMap;
	}
	
	boolean isFull(){ return (getBoard().getTotalAvailableMoves() == 0)? true: false;}
	
	Node getParent() throws IllegalStateException{
		if (hasParent) return parent;
		else throw new IllegalStateException("No parent! Possibly root?");
	}
	void setParent(Node parent) {this.parent = parent;}
	int[] getMove() {return move;}
	boolean hasParent() {return hasParent;}
	boolean hasBoard() {return isRoot || simMode;}
	boolean isRoot() {return isRoot;}
	boolean simMode(){return simMode;}
	int getPid() {
		return pid;
	}

	/**Move from parent to this node*/
	private int[] move= new int[2];
	private Node parent;				//Reference to parent. This is important for the copy constructor
	ArrayList<Node> children;	//References
	private boolean hasParent;
	private boolean isRoot;
	private boolean simMode;
	/**Number of simulations and victories (accumulates from children)*/
	int ni, vi;
	/**Who is about to make a move*/
	private int pid;
	private MctsBoard rootBoard;
	final private float c= Const.C;
	private int oppID() {return (pid==1)? 2: 1;}
}
