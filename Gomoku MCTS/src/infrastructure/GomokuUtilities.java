package infrastructure;

/**
 *
 */


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * <p>
 * Title: DataStructures2013
 * </p>
 * 
 * <p>
 * Description: Data Structures project: year 2013-2014
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * 
 * <p>
 * Company: A.U.Th.
 * </p>
 * 
 * @author Michael T. Tsapanos
 * @version 1.0
 */
public class GomokuUtilities
{
  // Color static variables
  public final static int GRAY = 0;
  public final static int BLACK = 1;
  public final static int WHITE = 2;
  public final static int YELLOW = 3;
  public final static int GREEN = 4;
  public final static int PURPLE = 5;
  public final static int BLUE = 6;
  public final static int RED = 7;


  // Player Static
  public final static int BLUE_PLAYER = 7;
  public final static int RED_PLAYER = 8;

  // Tile Ids constant
  public static int TILE_ID = 1;
  public static int NODE_ID = 1;

  // Direction static variables
  public final static int LEFT = 0;
  public final static int DOWN = 1;
  public final static int RIGHT = 2;
  public final static int UP = 3;

  // Window static variables
  public static final int WINDOW_WIDTH = 670;
  public static final int WINDOW_HEIGHT = 600;

  // Board static variables
  public static final int NUMBER_OF_ROWS = 15;
  public static final int NUMBER_OF_PLAYABLE_ROWS = 15;
  public static final int NUMBER_OF_COLUMNS = 15;
  public static final boolean BORDERS = true;

  public static final int STEP_LIMIT = 1000;
  public static int TIME_STEP = 100;
  public static int SCORE_LIMIT = 300;

  public static final int WIDTH = 35;
  public static final int HEIGHT = 35;

  public static final int GRAPHIC_WIDTH = 35;
  public static final int GRAPHIC_HEIGHT = 35;
  
  private static int[] previousMove= new int[2];

  public static Board cloneBoard (Board fullBoard)
  {
	  Board clone = new Board(true);

	  for (int i = 0; i < fullBoard.getCols(); i++) {
		  for (int j = 0; j < fullBoard.getRows(); j++) {
			  Tile a = fullBoard.getTile(i,j);
			  Tile tile =
					  new Tile(a.getId(), a.getX(), a.getY(), a.getColor(), a.getMark(),a.getPlayerId());
			  clone.setTile(tile);

		  }
	  }

	  return clone;

  }
  
  public static Board cloneBoard (Board fullBoard)
  {
	  Board clone = new Board(true);

	  for (int i = 0; i < fullBoard.getCols(); i++) {
		  for (int j = 0; j < fullBoard.getRows(); j++) {
			  Tile a = fullBoard.getTile(i,j);
			  Tile tile =
					  new Tile(a.getId(), a.getX(), a.getY(), a.getColor(), a.getMark(),a.getPlayerId());
			  clone.setTile(tile);

		  }
	  }

	  return clone;

  }

public static void playTile(Board board, int x, int y, int playerId)
  {
	  if (!board.getIsClone()){
		  System.out.println("The function playTile can only be used on a cloned Board");
	  }
	  else{
		  board.getTile(x, y).setColor(playerId);
		  board.getTile(x, y).setPlayerId(playerId);
	  }
  }

/*  public static Board boardAfterMove (Board board, int[] move)
  {
    Board clone = CrushUtilities.cloneBoard(board);
    // System.out.println("---------Print Clone---------");
    // clone.printFullBoard();
    // System.out.println("---------End Print Clone---------");

    int[] finalMove = CrushUtilities.calculateNextMove(move);

    clone.moveTile(finalMove[0], finalMove[1], finalMove[2], finalMove[3]);

    int currentPoints = clone.findCreatedNples();
    // System.out.println("Cur Point: "+currentPoints);

    clone.removeMarkedTilesForClone();

    return clone;
  }*/
  
  
/*  public static Board boardAfterDeletingNples (Board board)
  {
    Board clone = CrushUtilities.cloneBoard(board);
    int currentPoints = clone.findCreatedNples();
    clone.removeMarkedTilesForClone();
    return clone;
  }*/

/*  public static Board boardAfterFullMove (Board board, int[] move)
  {
    Board clone = CrushUtilities.cloneBoard(board);
    int[] finalMove = CrushUtilities.calculateNextMove(move);
    clone.moveTile(finalMove[0], finalMove[1], finalMove[2], finalMove[3]);

    while (true) {
      int currentPoints = clone.findCreatedNples();
      // System.out.println("Cur Point: "+currentPoints);
      if (currentPoints == 0)
        break;

      clone.removeMarkedTilesForClone();
      // System.out.println("Move: "+k+"---------Print Clone--------- chain :"+chain);
      // clone.printFullBoard();
      // System.out.println("---------End Print Clone---------");

    }

    for (int i = 0; i < board.getCols(); i++) {
      if (board.getTile(i, 9).getColor() == -1) {
        System.out.println("---------Print Clone---------");
        board.printFullBoard();
        System.out.println("---------End Print Clone---------");
        System.out.println("---------ABORT!!! ABORT!!---------");
        System.exit(1);
      }
    }

    return clone;

  }*/

/*  public static Board boardAfterMovingCandies (Board board, int[] move)
  {
    Board clone = CrushUtilities.cloneBoard(board);
    int[] finalMove = CrushUtilities.calculateNextMove(move);
    clone.moveTile(finalMove[0], finalMove[1], finalMove[2], finalMove[3]);
    return clone;
  }*/

  /*public static int[] calculateNextMove (int[] move)
  {

    int[] returnedMove = new int[4];

    if (move[2] == CrushUtilities.UP) {
      // System.out.println("UP");
      returnedMove[0] = move[0];
      returnedMove[1] = move[1];
      returnedMove[2] = move[0];
      returnedMove[3] = move[1] + 1;
    }
    if (move[2] == CrushUtilities.DOWN) {
      // System.out.println("DOWN");
      returnedMove[0] = move[0];
      returnedMove[1] = move[1];
      returnedMove[2] = move[0];
      returnedMove[3] = move[1] - 1;
    }
    if (move[2] == CrushUtilities.LEFT) {
      // System.out.println("LEFT");
      returnedMove[0] = move[0];
      returnedMove[1] = move[1];
      returnedMove[2] = move[0] - 1;
      returnedMove[3] = move[1];
    }
    if (move[2] == CrushUtilities.RIGHT) {
      // System.out.println("RIGHT");
      returnedMove[0] = move[0];
      returnedMove[1] = move[1];
      returnedMove[2] = move[0] + 1;
      returnedMove[3] = move[1];
    }
    return returnedMove;
  }*/

  public static ArrayList<int[]> getAvailableMoves (Board board)
  {
    ArrayList<int[]> availTriples = new ArrayList<int[]>();
    availTriples.clear();

    int x = board.getCols();
    int y = board.getPRows();

    for (int i = 0; i < x; i++) {
      for (int j = 0; j < y; j++) {

        if (board.avTriplesUp(board.getTile(i, j))) {
          int[] a = { i, j, GomokuUtilities.UP };
          availTriples.add(a);
        }

        if (board.avTriplesDown(board.getTile(i, j))) {
          int[] a = { i, j, GomokuUtilities.DOWN };
          availTriples.add(a);
        }

        if (board.avTriplesLeft(board.getTile(i, j))) {
          int[] a = { i, j, GomokuUtilities.LEFT };
          availTriples.add(a);
        }

        if (board.avTriplesRight(board.getTile(i, j))) {
          int[] a = { i, j, GomokuUtilities.RIGHT };
          availTriples.add(a);
        }

      }
    }
    return availTriples;
  }

  public static void createScoreFile (String[] names, ArrayList<int[]> scores)
    throws FileNotFoundException
  {

    PrintStream realSystemOut = System.out;

    String filename = names[0] + "-" + names[1] + ".txt";

    OutputStream output = new FileOutputStream(filename);
    PrintStream printOut = new PrintStream(output);
    System.setOut(printOut);

    System.out.println(names[0] + "-" + names[1]);
    for (int[] singleScore: scores)
      System.out.println(singleScore[0] + "-" + singleScore[1]);

    System.setOut(realSystemOut);
  }
  
  public static double colorPercentage(Board board, int x, int y, int radius, int player){
	  if (radius < 2){
		 // System.out.println("radius less than 2");
		  return -1;
	  }
	  if (radius >4){
		  //System.out.println("radius less than 2");
		  return -1;
	  }
	  int numOfTiles, playerTiles;
	  numOfTiles=0;
	  playerTiles = 0;
	  
	  int x_min = Math.max(0,x-radius);
	  int y_min = Math.max(0,y-radius);
	  int x_max = Math.min(GomokuUtilities.NUMBER_OF_ROWS-1,x+radius);	
	  int y_max = Math.min(GomokuUtilities.NUMBER_OF_COLUMNS-1,y+radius);
	  
	  for (int i = x_min; i<=x_max;i++){
		  for (int j = y_min; j<=y_max;j++){
			  if(i>-1 && i<15 && j>-1 && j<15){
				  numOfTiles++;
				  if (board.getTile(i, j).getPlayerId()==player){
					  playerTiles++;
				  }
				  
		  }
	  }
	}
	  if (numOfTiles== 0){
		  //System.out.println("numOfTiles 0");
		  return -1;
	  }
	  
	  //System.out.println("normal return, numOfTiles="+numOfTiles+", playerTiles="+playerTiles);
	  return (double)playerTiles/(double)numOfTiles;
  }
  
  public static boolean checkForWin(int id, Board board){		
		//check for win
		//check horizontally
		boolean end= false;
	for (int i=0;i<GomokuUtilities.NUMBER_OF_COLUMNS;i++){
			int count =0;
			for (int j=0; j < GomokuUtilities.NUMBER_OF_ROWS; j++ ){
				if (board.getTile(i, j).getPlayerId()==id){
					count++;
					if (count==5){
						//System.out.println(i+" "+j);
						board.getTile(i,j).setColor(3);
						end = true;
						break;
					}
				}
				else{
					count=0;
				}
			}
			if (end==true)
				break;
		}

		if (end){
			return true;
		}
		
		//check vertically
		for (int i=0;i<GomokuUtilities.NUMBER_OF_ROWS;i++){
			int count =0;
			for (int j=0; j < GomokuUtilities.NUMBER_OF_COLUMNS; j++ ){
				if (board.getTile(j, i).getPlayerId()==id){
					count++;
					if (count==5){
						//System.out.println(i+" "+j);
						board.getTile(j,i).setColor(3);
						end = true;
						break;
					}
				}
				else{
					count=0;
				}
			}
			if (end==true)
				break;
		}

		if (end){
			return true;
		}
		
		//check diagonally
		for (int i=0;i<GomokuUtilities.NUMBER_OF_COLUMNS;i++){
			for (int j=0; j < GomokuUtilities.NUMBER_OF_ROWS; j++ ){
				int count =0;
				for (int delta=0; delta<5 ; delta++){
					if(i+delta<15 && j+delta<GomokuUtilities.NUMBER_OF_COLUMNS){
						if (board.getTile(i+delta, j+delta).getPlayerId()==id){
							count++;
							if (count==5){
								//System.out.println(i+" "+j);
								board.getTile(i,j).setColor(3);
								end = true;
								break;

							}
						}
						else{
							count =0;
						}

					}
				
					
				}
				if (end==true)
					break;
			}
			if (end==true)
				break;
		}

		if (end){
			return true;
		}
		//check other diagonally
		for (int i=0;i<GomokuUtilities.NUMBER_OF_COLUMNS;i++){
			for (int j=0; j < GomokuUtilities.NUMBER_OF_ROWS; j++ ){
				int count =0;
				for (int delta=0; delta<5 ; delta++){
					if(i-delta>=0 && j+delta<GomokuUtilities.NUMBER_OF_COLUMNS){
						if (board.getTile(i-delta, j+delta).getPlayerId()==id){
							count++;
							if (count==5){
								//System.out.println(i+" "+j);
								board.getTile(i,j).setColor(3);
								end = true;
								break;

							}
						}
						else{
							count =0;
						}

					}
				
					
				}
				if (end==true)
					break;
			}
			if (end==true)
				break;
		}
		if (end){
			return true;
		}
		return false;
	}
  static void setPreviouusMove(int[] previMove){
	  //System.out.println(previMove[0]+"sdas");
	 
		  previousMove[0] = previMove[0];
		  previousMove[1] = previMove[1];
	  
	  
  }
  
 public static int[] getPreviousMove(){
	  return previousMove;
  }
  
	

}
