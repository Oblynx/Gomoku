package infrastructure;


import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

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

public class MainPlatform
{

	private static int limit;

	protected static JFrame frame;
	protected static Board board;

	private static JComboBox teamOne;
	private static JComboBox teamTwo;

	private static JButton generateMaze;
	private static JButton chase;
	private static JButton swapChase;
	private static JButton quit;
	private static JSlider gameSpeed;

	private static AbstractPlayer playerA;
	private static AbstractPlayer playerB;

	private static String PlayerOne;
	private static String PlayerTwo;

	private static long waitingTime;

	private static ArrayList<int[]> scores = new ArrayList<int[]>();
	private static String[] names = new String[2];

	private static String filename = "";

	private static String[] teamNames = { "Random Player","Heuristic Player", "Player No2"};
	private static String[] teamClasses =
		{ "RandomPlayer","HeuristicPlayer","SecondHeuristicPlayer"};
	
	public static boolean checkForWin(AbstractPlayer player, Board board){		
		//check for win
		//check horizontally
		boolean end= false;
	for (int i=0;i<GomokuUtilities.NUMBER_OF_COLUMNS;i++){
			int count =0;
			for (int j=0; j < GomokuUtilities.NUMBER_OF_ROWS; j++ ){
				if (board.getTile(i, j).getPlayerId()==player.getId()){
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
				if (board.getTile(j, i).getPlayerId()==player.getId()){
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
						if (board.getTile(i+delta, j+delta).getPlayerId()==player.getId()){
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
						if (board.getTile(i-delta, j+delta).getPlayerId()==player.getId()){
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
	
	

	private static void showScore ()
	{
		int scoreA = 0, scoreB = 0;
		String result =
				"Player A: " + playerB.getName() + "\t|\t Player B: " + playerA.getName()
				+ "\n";

		for (int[] singleScore: scores) {
			result += "\t\t\t\t" + singleScore[0] + "\t|\t\t" + singleScore[1] + "\n";
			scoreA += singleScore[0];
			scoreB += singleScore[1];
		}

		result += "___________________________________________ \n";
		result += " Overall: \t\t\t" + scoreA + "\t|\t" + scoreB + "\n";

		JOptionPane.showMessageDialog(null, result, "SCORE TABLE",
				JOptionPane.INFORMATION_MESSAGE);

	}

	private static void createAndShowGUI ()
	{


		JFrame.setDefaultLookAndFeelDecorated(false);

		frame = new JFrame("Crush Board");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board = new Board(false);
		JPanel buttonPanel = new JPanel();
		JPanel teamsPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		generateMaze = new JButton("Generate Board");
		chase = new JButton("Play");
		chase.setEnabled(false);
		swapChase = new JButton("Swap & Play");
		swapChase.setEnabled(false);
		quit = new JButton("Quit");

		gameSpeed = new JSlider(JSlider.HORIZONTAL, 1, 3000, 1500);
		gameSpeed.addChangeListener(new SliderListener());

		gameSpeed.setMajorTickSpacing(10);
		gameSpeed.setPaintTicks(true);
		Font font = new Font("Serif", Font.ITALIC, 15);
		gameSpeed.setFont(font);
		Hashtable labelTable = new Hashtable();
		labelTable.put(new Integer(1), new JLabel("Fast"));
		labelTable.put(new Integer(3000), new JLabel("Slow"));
		gameSpeed.setLabelTable(labelTable);

		gameSpeed.setPaintLabels(true);

		teamOne = new JComboBox(teamNames);
		teamOne.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent arg0)
			{
				//System.out.println("ALLAKSE");
				chase.setEnabled(true);
				swapChase.setEnabled(false);
				scores.clear();
			}
		});
		teamTwo = new JComboBox(teamNames);
		teamTwo.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e)
			{
				//System.out.println("ALLAKSE 2");
				chase.setEnabled(true);
				swapChase.setEnabled(false);
				scores.clear();
			}
		});

		JLabel label = new JLabel("THE THMMY GOMOKU GAME!!!", JLabel.CENTER);
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add("North", label);
		centerPanel.add("Center", gameSpeed);

		teamsPanel.setLayout(new BorderLayout());
		teamsPanel.add("West", teamOne);
		teamsPanel.add("East", teamTwo);
		teamsPanel.add("Center", centerPanel);
		teamsPanel.add("South", buttonPanel);

		buttonPanel.add(generateMaze);
		buttonPanel.add(chase);
		buttonPanel.add(swapChase);
		buttonPanel.add(quit);

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add("Center", teamsPanel);
		// frame.add("South", buttonPanel);

		frame.pack();
		frame.setVisible(true);

		// ---------ActionListeners------//
		quit.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent evt)
			{
				System.exit(0);
			}
		});

		generateMaze.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent evt)
			{

				Properties boardProperties = new Properties();


				try {
					//load a properties file
					boardProperties.load(new FileInputStream("boardConfig.properties"));

					//get the property value and print it out
					waitingTime =Long.parseLong(boardProperties.getProperty("waitingTime"));


					//System.out.println(numOfRowsFromProp);


				} catch (IOException ex) {
					ex.printStackTrace();
				}
				chase.setEnabled(false);
				swapChase.setEnabled(false);
				generateMaze.setEnabled(false);

				frame.repaint();
				frame.remove(board);
				board = new Board(false);
				board.initializeBoard();

				// board.printBoard();

				// PlayerOne = teamClasses[teamOne.getSelectedIndex()];
				// PlayerTwo = teamClasses[teamTwo.getSelectedIndex()];

				frame.getContentPane().add("North", board);
				frame.pack();
				chase.setEnabled(true);
				swapChase.setEnabled(false);
				generateMaze.setEnabled(false);
			}
		});

		chase.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent evt)
			{

				chase.setEnabled(false);
				swapChase.setEnabled(false);
				generateMaze.setEnabled(false);

				PlayerOne = teamClasses[teamOne.getSelectedIndex()];
				PlayerTwo = teamClasses[teamTwo.getSelectedIndex()];

				Thread t = new Thread(new Runnable() {
					public void run ()
					{

						// Get the players
						playerA = null;

						try {


							Class playerAClass = Class.forName(PlayerOne);
							Class partypes[] = new Class[1];
							partypes[0] = Integer.class;

							Constructor playerAArgsConstructor =
									playerAClass.getConstructor(partypes);
							Object arglist[] = new Object[1];
							arglist[0] = new Integer(1);
							Object playerObject = playerAArgsConstructor.newInstance(arglist);

							playerA = (AbstractPlayer) playerObject;

						}
						catch (ClassNotFoundException ex) {
							ex.printStackTrace();
						}
						catch (IllegalAccessException ex) {
							ex.printStackTrace();
						}
						catch (InstantiationException ex) {
							ex.printStackTrace();
						}
						catch (NoSuchMethodException ex) {
							ex.printStackTrace();
						}
						catch (InvocationTargetException ex) {
							ex.printStackTrace();
						}

						playerB = null;
						try {
							Class playerBClass = Class.forName(PlayerTwo);
							Class partypes[] = new Class[1];
							partypes[0] = Integer.class;
							Constructor playerBArgsConstructor =
									playerBClass.getConstructor(partypes);
							Object arglist[] = new Object[1];
							arglist[0] = new Integer(2);
							Object playerObject = playerBArgsConstructor.newInstance(arglist);
							playerB = (AbstractPlayer) playerObject;
						}
						catch (ClassNotFoundException ex) {
							ex.printStackTrace();
						}
						catch (IllegalAccessException ex) {
							ex.printStackTrace();
						}
						catch (InstantiationException ex) {
							ex.printStackTrace();
						}
						catch (NoSuchMethodException ex) {
							ex.printStackTrace();
						}
						catch (InvocationTargetException ex) {
							ex.printStackTrace();
						}

						names[0] = playerA.getName();
						names[1] = playerB.getName();

						boolean end = false;
						while (limit < GomokuUtilities.NUMBER_OF_COLUMNS*GomokuUtilities.NUMBER_OF_ROWS) {

							frame.remove(board);

							// -----Move for PlayerA----------------
							//System.out.println(playerA.getId());
						
							frame.getContentPane().add("North", board);
							frame.validate();
							frame.pack();
							frame.repaint();

							int[] moveOfA = new int[2];
							moveOfA = playerA.getNextMove(board);
							GomokuUtilities.setPreviouusMove(moveOfA);
							//System.out.println(moveOfA[0]+" "+ moveOfA[1]);
							 /*while(board.getTile(moveOfA[0], moveOfA[1]).getPlayerId()!=0){
								 moveOfA[0] = (int) (Math.random()*GomokuUtilities.NUMBER_OF_COLUMNS);
								 moveOfA[1] = (int) (Math.random()*GomokuUtilities.NUMBER_OF_COLUMNS);
							    }*/
							 
							
							/*board.getTile(moveOfA[0], moveOfA[1]).setColor(GomokuUtilities.BLACK);
							board.getTile(moveOfA[0], moveOfA[1]).setPlayerId(1);*/
							
							if(board.getTile(moveOfA[0], moveOfA[1]).getPlayerId()!=0){
								System.out.println("The tile you chose is already taken");
							}
							
							if(board.getTile(moveOfA[0], moveOfA[1]).getPlayerId()==0){
								board.getTile(moveOfA[0], moveOfA[1]).setColor(GomokuUtilities.BLACK);
								board.getTile(moveOfA[0], moveOfA[1]).setPlayerId(1);
							}
							
							if(board.getTile(moveOfA[0], moveOfA[1]).getPlayerId()==1){
								board.getTile(moveOfA[0], moveOfA[1]).setColor(GomokuUtilities.BLACK);
								board.getTile(moveOfA[0], moveOfA[1]).setPlayerId(1);
							}
							
							if(board.getTile(moveOfA[0], moveOfA[1]).getPlayerId()==2){
								board.getTile(moveOfA[0], moveOfA[1]).setColor(GomokuUtilities.WHITE);
								board.getTile(moveOfA[0], moveOfA[1]).setPlayerId(2);
							}
								
							frame.getContentPane().add("North", board);
							frame.validate();
							frame.pack();
							frame.repaint();
							
							
							

							if (checkForWin(playerA, board)){
								playerA.setScore(1);
								break;
							}
								
						
									//Reads and applies the diseired game speed
							GomokuUtilities.TIME_STEP = gameSpeed.getValue();
							try {
								Thread.sleep(GomokuUtilities.TIME_STEP);
							}
							catch (InterruptedException e) {
							}
									//----------------------------------------------------

									// -----Move for PlayerB----------------

							frame.remove(board);
							frame.getContentPane().add("North", board);
							frame.validate();
							frame.pack();
							frame.repaint();
									
							int[] moveOfB = new int[2];
							moveOfB = playerB.getNextMove(board);
							GomokuUtilities.setPreviouusMove(moveOfB);
							/*while(board.getTile(moveOfB[0], moveOfB[1]).getPlayerId()!=0){
								moveOfB[0] = (int) (Math.random()*15);
								moveOfB[1] = (int) (Math.random()*15);
							    }
							board.getTile(moveOfB[0], moveOfB[1]).setColor(GomokuUtilities.WHITE);
							board.getTile(moveOfB[0], moveOfB[1]).setPlayerId(2);*/
							
							if(board.getTile(moveOfB[0], moveOfB[1]).getPlayerId()!=0){
								System.out.println("The tile you chose is already taken");
							}
							if(board.getTile(moveOfB[0], moveOfB[1]).getPlayerId()==0){
								board.getTile(moveOfB[0], moveOfB[1]).setColor(GomokuUtilities.WHITE);
								board.getTile(moveOfB[0], moveOfB[1]).setPlayerId(2);
							}
							
							if(board.getTile(moveOfB[0], moveOfB[1]).getPlayerId()==1){
								board.getTile(moveOfB[0], moveOfB[1]).setColor(GomokuUtilities.BLACK);
								board.getTile(moveOfB[0], moveOfB[1]).setPlayerId(1);
							}
							
							if(board.getTile(moveOfB[0], moveOfB[1]).getPlayerId()==2){
								board.getTile(moveOfB[0], moveOfB[1]).setColor(GomokuUtilities.WHITE);
								board.getTile(moveOfB[0], moveOfB[1]).setPlayerId(2);
							}
									
							frame.getContentPane().add("North", board);
							frame.validate();
							frame.pack();
							frame.repaint();
							
							if (checkForWin(playerB, board)){
								playerB.setScore(1);
								break;
							}							

									if (end)
										break;
									GomokuUtilities.TIME_STEP = gameSpeed.getValue();

									try {
										Thread.sleep(GomokuUtilities.TIME_STEP);
									}
									catch (InterruptedException e) {
									}

						}

						int[] temp = { playerA.getScore(), playerB.getScore() };
						scores.add(temp);

						for (int[] singleScore: scores)
							//System.out.println(Arrays.toString(singleScore));

							try {
								GomokuUtilities.createScoreFile(names, scores);
							}
						catch (FileNotFoundException e) {
							e.printStackTrace();
						}

						if (playerA.getScore() > playerB.getScore()) {

							JOptionPane.showMessageDialog(null, "WINNER IS (Black Player): "
									+ playerA.getName()
									+ "   Score of winner: "
									+ playerA.getScore(),
									"Results...",
									JOptionPane.INFORMATION_MESSAGE);
						}
						else {

							JOptionPane.showMessageDialog(null, "WINNER IS (White Player): "
									+ playerB.getName()
									+ "   Score of winner: "
									+ playerB.getScore(),
									"Results...",
									JOptionPane.INFORMATION_MESSAGE);

						}

						//System.out.println(Arrays.toString(names));
						chase.setEnabled(false);
						swapChase.setEnabled(false);
						generateMaze.setEnabled(false);

					}
				});
				t.start();
			}
		});

		/*swapChase.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent evt)
			{
				chase.setEnabled(false);
				swapChase.setEnabled(false);
				generateMaze.setEnabled(false);

				Thread t = new Thread(new Runnable() {
					public void run ()
					{

						// Get the players
						playerA = null;
						try {
							Class playerAClass = Class.forName(PlayerTwo);
							Class partypes[] = new Class[1];
							partypes[0] = Integer.class;

							Constructor playerAArgsConstructor =
									playerAClass.getConstructor(partypes);
							Object arglist[] = new Object[1];
							arglist[0] = new Integer(1);
							Object playerObject = playerAArgsConstructor.newInstance(arglist);

							playerA = (AbstractPlayer) playerObject;

						}
						catch (ClassNotFoundException ex) {
							ex.printStackTrace();
						}
						catch (IllegalAccessException ex) {
							ex.printStackTrace();
						}
						catch (InstantiationException ex) {
							ex.printStackTrace();
						}
						catch (NoSuchMethodException ex) {
							ex.printStackTrace();
						}
						catch (InvocationTargetException ex) {
							ex.printStackTrace();
						}

						playerB = null;
						try {
							Class playerBClass = Class.forName(PlayerOne);
							Class partypes[] = new Class[1];
							partypes[0] = Integer.class;
							Constructor playerBArgsConstructor =
									playerBClass.getConstructor(partypes);
							Object arglist[] = new Object[1];
							arglist[0] = new Integer(2);
							Object playerObject = playerBArgsConstructor.newInstance(arglist);
							playerB = (AbstractPlayer) playerObject;
						}
						catch (ClassNotFoundException ex) {
							ex.printStackTrace();
						}
						catch (IllegalAccessException ex) {
							ex.printStackTrace();
						}
						catch (InstantiationException ex) {
							ex.printStackTrace();
						}
						catch (NoSuchMethodException ex) {
							ex.printStackTrace();
						}
						catch (InvocationTargetException ex) {
							ex.printStackTrace();
						}

						boolean end = false;

						while (limit < CrushUtilities.STEP_LIMIT) {

							frame.remove(board);

							// -----Move for PlayerA----------------
							ArrayList<int[]> availMoves = new ArrayList<int[]>();
									availMoves = board.checkForTriples();
									while (availMoves.isEmpty()) {
										board.resetBoard();
										availMoves = board.checkForTriples();

									}

									int movesOfA[] = new int[4];

									Stopper st  = new Stopper(movesOfA,playerA,board,availMoves);
									st.start();
									long a = System.currentTimeMillis();
									try {
										st.join(waitingTime);
										movesOfA = st.moves;
										if (movesOfA==null ){
											System.out.println("$$$$$$$ "+playerA.getName()+"  TIME OUT $$$$$$$");

											if ((Math.random()) <= 0.5) {
												System.out.println(playerA.getName()+" played a Random Move");


												movesOfA = CrushUtilities.calculateNextMove(availMoves.get((int) (availMoves.size()*Math.random())));
											}
											else {
												System.out.println(playerA.getName()+" played a Wrong Move");

												movesOfA = new int[4];

												//System.out.println("Player " + getName() + " played a wrong move.");

											}
											//movesOfA = availMoves.get((int) (availMoves.size()*Math.random()));
											long b = System.currentTimeMillis();
											//System.out.println(b-a);

										}
										else{

											//System.out.println("!!!!!!! PlayerA  Played !!!!!");
											//long b = System.currentTimeMillis();
											//System.out.println(b-a);



										}
									} catch (InterruptedException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}


									//movesOfA = playerA.getNextMove(availMoves, board);

									if (board.validMove(movesOfA, availMoves)) {

										board.moveTile(movesOfA[0], movesOfA[1], movesOfA[2],
												movesOfA[3]);

										frame.getContentPane().add("North", board);
										frame.validate();
										frame.pack();
										frame.repaint();
										try {
											Thread.sleep(CrushUtilities.TIME_STEP);
										}
										catch (InterruptedException e) {
										}

										// Prosthese to pointsOfA sto scor toy playerA
										int pointsOfA = 0;
										// for (int j = 0; j < 10; j++) {
										while (true) {
											// board.printBoard();
											int currentPoints = board.findCreatedNples();

											if (currentPoints == 0)
												break;
											pointsOfA = pointsOfA + currentPoints;
											board.paintMarkedTiles(playerB.getId());
											playerA.setScore(playerA.getScore() + currentPoints);

											board.getPlayersScore(playerA.getScore(), pointsOfA,
													playerB.getScore(), 0);

											frame.getContentPane().add("North", board);
											frame.validate();
											frame.pack();
											frame.repaint();

											try {
												Thread.sleep(CrushUtilities.TIME_STEP);
											}
											catch (InterruptedException e) {
											}

											board.removeMarkedTiles();

											frame.getContentPane().add("North", board);
											frame.validate();
											frame.pack();
											frame.repaint();
											board.getPlayersScore(playerA.getScore(), pointsOfA,
													playerB.getScore(), 0);
										}

										if (playerA.getScore() >= CrushUtilities.SCORE_LIMIT)
											end = true;

									}

									if (end)
										break;

									// -----Move for PlayerB----------------

									frame.remove(board);

									availMoves.clear();
									availMoves = new ArrayList<int[]>();
									availMoves = board.checkForTriples();
									while (availMoves.isEmpty()) {
										board.resetBoard();
										System.out.println("Board Initialized");
										availMoves = board.checkForTriples();

									}

									int movesOfB[] = new int[4];

									Stopper stb  = new Stopper(movesOfB,playerB,board,availMoves);
									stb.start();
									a = System.currentTimeMillis();
									try {
										stb.join(waitingTime);
										movesOfB = stb.moves;
										if (movesOfB==null ){
											System.out.println("$$$$$$$ "+playerB.getName()+"  TIME OUT $$$$$$$");

											if ((Math.random()) <= 0.5) {
												

												movesOfB = CrushUtilities.calculateNextMove(availMoves.get((int) (availMoves.size()*Math.random())));
												System.out.println(playerB.getName()+" played a Random Move");
											}
											else {
												movesOfB = new int[4];
												System.out.println(playerB.getName()+" played a Wrong Move");


												//System.out.println("Player " + getName() + " played a wrong move.");

											}
											//movesOfA = availMoves.get((int) (availMoves.size()*Math.random()));
											long b = System.currentTimeMillis();
											//System.out.println(b-a);

										}
										else{

											//System.out.println("!!!!!!! PlayerA  Played !!!!!");
											//long b = System.currentTimeMillis();
											//System.out.println(b-a);



										}
									} catch (InterruptedException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}


									//movesOfB = playerB.getNextMove(availMoves, board);

									if (board.validMove(movesOfB, availMoves)) {

										board.moveTile(movesOfB[0], movesOfB[1], movesOfB[2],
												movesOfB[3]);

										frame.getContentPane().add("North", board);
										frame.validate();
										frame.pack();
										frame.repaint();
										try {
											Thread.sleep(CrushUtilities.TIME_STEP);
										}
										catch (InterruptedException e) {
										}

										// Prosthese to pointsOfA sto scor toy playerA
										int pointsOfB = 0;
										// for (int j = 0; j < 10; j++) {
										while (true) {
											// board.printBoard();
											int currentPoints = board.findCreatedNples();

											if (currentPoints == 0)
												break;
											pointsOfB = pointsOfB + currentPoints;
											board.paintMarkedTiles(playerA.getId());
											playerB.setScore(playerB.getScore() + currentPoints);

											board.getPlayersScore(playerA.getScore(), 0,
													playerB.getScore(), pointsOfB);

											frame.getContentPane().add("North", board);
											frame.validate();
											frame.pack();
											frame.repaint();

											try {
												Thread.sleep(CrushUtilities.TIME_STEP);
											}
											catch (InterruptedException e) {
											}

											board.removeMarkedTiles();

											frame.getContentPane().add("North", board);
											frame.validate();
											frame.pack();
											frame.repaint();
											board.getPlayersScore(playerA.getScore(), 0,
													playerB.getScore(), pointsOfB);
										}

										if (playerB.getScore() >= CrushUtilities.SCORE_LIMIT)
											end = true;

									}

									if (end)
										break;
									CrushUtilities.TIME_STEP = gameSpeed.getValue();

									try {
										Thread.sleep(CrushUtilities.TIME_STEP);
									}
									catch (InterruptedException e) {
									}

						}

						int[] temp = { playerB.getScore(), playerA.getScore() };
						scores.add(temp);

						for (int[] singleScore: scores)
							//System.out.println(Arrays.toString(singleScore));

							try {
								CrushUtilities.createScoreFile(names, scores);
							}
						catch (FileNotFoundException e) {
							e.printStackTrace();
						}

						if (playerA.getScore() > playerB.getScore()) {

							JOptionPane.showMessageDialog(null, "WINNER IS (Blue Player): "
									+ playerA.getName()
									+ "   Score of winner: "
									+ playerA.getScore(),
									"Results...",
									JOptionPane.INFORMATION_MESSAGE);
						}
						else {

							JOptionPane.showMessageDialog(null, "WINNER IS (Red Player): "
									+ playerB.getName()
									+ "   Score of winner: "
									+ playerB.getScore(),
									"Results...",
									JOptionPane.INFORMATION_MESSAGE);

						}

						//System.out.println(Arrays.toString(names));
						chase.setEnabled(true);
						swapChase.setEnabled(true);
						generateMaze.setEnabled(true);
						showScore();

					}
				});
				t.start();
			}
		});
*/
	}

	public static void main (String[] args)
	{

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run ()
			{
				createAndShowGUI();
			}
		});

	}

	public static AbstractPlayer getPlayerA ()
	{
		return playerA;
	}

	public static AbstractPlayer getPlayerB ()
	{
		return playerB;
	}

}
