package mcts;

import java.util.ArrayList;
import java.lang.Math;

class Node {
	int[] move= new int[2];
	int ni, vi;
	Node parent;
	ArrayList<Node> children;
	
	float ucb(){
		return (float)(((double)vi)/ni + c*Math.sqrt(Math.log(parent.ni)/ni));
	}
	
	private float c;
}
