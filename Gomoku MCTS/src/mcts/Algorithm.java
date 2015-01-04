package mcts;

import java.util.HashMap;
import mcts.Common.*;

class Algorithm {
	AbstractPlayer[] players;
	
	void backpropagation(SimulationResults res){
		int i=0;
		for(Node node : res.nodes){
			while (!node.isRoot()){
				node.ni+= res.simulations;
				node.vi+= res.victories[i];
				node= node.getParent();
			}
			i++;
		}
	}
	
	/**
	 * n playouts from each gameState to game end. TRUSTS players to only make <b>legal moves</b>
	 * @param gameStates Array of tree nodes from which simulation begins
	 * @param nSim Array of number of simulations from each node
	 * @param nodes How many nodes?
	 * @return Simulation results
	 */
	SimulationResults simulation(Node[] gameStates, int nSim){
		SimulationResults res= new SimulationResults(gameStates.length, nSim);
		int nodeCount= 0;
		for(Node gameState : gameStates){
			gameState.entersSim();
			res.addResults(nodeCount, nSims(gameState, nSim));
			nodeCount++;
		}
		return res;
	}
	
	/**Expansion step of MCTS algorithm. Generates up to Common.Const.BRANCH_FACTOR
	 * new children, if possible, and adds them to the tree. Heuristically assigns
	 * a probability to each candidate child and randomly selects among them using the
	 * "Lahiri" selection method. Only tiles within ROI of last moves are considered.
	 * @param selectedNode Computed in "selection" step. Node from which children are generated
	 * @return An array of the new nodes
	 */
	Node[] expansion(Node selectedNode){
		HashMap<int[], Integer> heuristicPdf= selectedNode.eval();
		// TODO LahiriSelection()
		// TODO addToTree()
	}
	
	/**Find the node in the entire tree with the highest UCB
	 */
	Node selection(Tree gameTree){
		Node highestUCB=gameTree.get(0);
		for(Node node : gameTree)
			if (node.ucb() > highestUCB.ucb()) highestUCB= node;
		return highestUCB;
	}
	
	/**Performs the actual simulations. If the player who makes the winning move
	 * is the player who was about to play in gameState, it counts as a victory.
	 * Draws count <i>as losses</i>.
	 * @param gameState The node from which the simulations start
	 * @param nSim How many sims to do from gameState
	 */
	SimulationResults nSims(Node gameState, int nSim){
		SimulationResults res= new SimulationResults(gameState);
		int whoplays= gameState.getPid();
		int[] tmpMove;
		for(int i=0; i<nSim; i++)
			while (!gameState.isFull()){			//While not a draw...
				tmpMove= players[whoplays-1].getNextMove(gameState.getBoard());
				if (gameState.checkWin(tmpMove, players[whoplays-1].getId())){
					//If the player who makes the winning move is the player who
					//is actually about to move in this game state...
					if (gameState.getPid() == players[whoplays-1].getId()) res.victories[0]++;  
					break;
				}
				gameState.makeMove(tmpMove, whoplays);
				whoplays= (whoplays==1)? 2: 1;		//Change who plays
			}
		return res;
	}
	
	void addToTree(Node[] newLeaves){
		// TODO
	}
	
	class SimulationResults{
		int simulations;	//Assume same foreach node
		int[] victories;
		Node[] nodes;
		
		SimulationResults(int nNodes1, int nSim){
			simulations= nSim;
			victories= new int[nNodes1];
			nodes= new Node[nNodes1];
		}
		/**nSims constructor*/
		SimulationResults(Node node){
			victories= new int[1];
			victories[0]= 0;
			nodes= new Node[1];
			nodes[0]= node;
		}
		void addResults(int node, SimulationResults partial){
			victories[node]= partial.victories[0];
			nodes[node]= partial.nodes[0];
		}
	}
	
	/*For design purposes*/
	interface TreeInterface{
		Node get(int i);
	}
}
