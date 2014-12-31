

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Board extends JPanel
{

	protected int rows;
	protected int cols;
	protected int pRows;
	private int width;
	private int height;
	private ArrayList<ArrayList<Tile>> fullBoard;
	private int[] scores;
	private boolean isClone;

	private int graphicsWidth;
	private int graphicsHeight;

	@Override
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		// You add/change the statements here to draw
		// the picture you want.
		g2.setColor(Color.GRAY);
		g2.fillRect(0, 0, width, height);

		g2.setColor(Color.BLACK);

		Stroke drawingStroke =
				new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0);
		g2.setStroke(drawingStroke);
		for (int j = (pRows) - 1; j >= 0; j--) {

			for (int i = 0; i < cols; i++) {

				if (getTile(i, j).getColor() == GomokuUtilities.BLACK)
					g2.setColor(Color.BLACK);

				if (getTile(i, j).getColor() == GomokuUtilities.BLUE)
					g2.setColor(Color.BLUE);

				if (getTile(i, j).getColor() == GomokuUtilities.GREEN)
					g2.setColor(Color.GREEN);

				if (getTile(i, j).getColor() == GomokuUtilities.RED)
					g2.setColor(Color.RED);

				if (getTile(i, j).getColor() == GomokuUtilities.YELLOW)
					g2.setColor(Color.YELLOW);

				if (getTile(i, j).getColor() == GomokuUtilities.PURPLE)
					g2.setColor(Color.MAGENTA);

				if (getTile(i, j).getColor() == GomokuUtilities.GRAY)
					g2.setColor(Color.GRAY);
				
				if (getTile(i, j).getColor() == GomokuUtilities.WHITE)
					g2.setColor(Color.WHITE);

				if (getTile(i, j).getColor() == GomokuUtilities.BLUE_PLAYER)
					g2.setColor(Color.BLUE);

				if (getTile(i, j).getColor() == GomokuUtilities.RED_PLAYER)
					g2.setColor(Color.RED);

				if (getTile(i, j).getColor() == GomokuUtilities.RED_PLAYER
						|| getTile(i, j).getColor() == GomokuUtilities.BLUE_PLAYER)
					g.fillRect((2 * graphicsWidth + 1) + (i * graphicsWidth + 1),
							(cols * graphicsHeight + 1) - (j * graphicsHeight + 1),
							graphicsWidth, graphicsHeight);

				else
					g2.fillOval((2 * graphicsWidth + 1) + (i * graphicsWidth + 1),
							(cols * graphicsHeight + 1) - (j * graphicsHeight + 1),
							graphicsWidth, graphicsHeight);

			}

		}

		for (int j = (pRows) - 1; j >= 0; j--) {

			for (int i = 0; i < cols; i++) {

				g2.setColor(Color.BLACK);

				g2.drawLine((2 * graphicsWidth + 1) + (i * graphicsWidth + 1),
						(cols * graphicsHeight + 1) - (j * graphicsHeight + 1),
						(2 * graphicsWidth + 1) + (i * graphicsWidth + 1)
						+ graphicsWidth, (cols * graphicsHeight + 1)
						- (j * graphicsHeight + 1));

				g2.drawLine((2 * graphicsWidth + 1) + (i * graphicsWidth + 1),
						(cols * graphicsHeight + 1) - (j * graphicsHeight + 1),
						(2 * graphicsWidth + 1) + (i * graphicsWidth + 1),
						(cols * graphicsHeight + 1) - (j * graphicsHeight + 1)
						+ graphicsWidth);

				g2.drawLine((2 * graphicsWidth + 1) + (i * graphicsWidth + 1)
						+ graphicsWidth, (cols * graphicsHeight + 1)
						- (j * graphicsHeight + 1),
						(2 * graphicsWidth + 1) + (i * graphicsWidth + 1)
						+ graphicsWidth, (cols * graphicsHeight + 1)
						- (j * graphicsHeight + 1)
						+ graphicsWidth);

				g2.drawLine((2 * graphicsWidth + 1) + (i * graphicsWidth + 1),
						(cols * graphicsHeight + 1) - (j * graphicsHeight + 1)
						+ graphicsWidth, (2 * graphicsWidth + 1)
						+ (i * graphicsWidth + 1)
						+ graphicsWidth,
						(cols * graphicsHeight + 1) - (j * graphicsHeight + 1)
						+ graphicsWidth);

			}
		}
		/*g2.setFont(new Font("default", Font.BOLD, 16));
		g2.setColor(Color.BLUE);
		g2.drawString("     Player A", 10, 760);
		g2.drawString("Move Score: " + scores[0], 10, 780);
		g2.drawString("Total Score: " + scores[1], 10, 800);

		g2.setColor(Color.RED);
		g2.drawString("     Player B", 450, 760);
		g2.drawString("Move Score: " + scores[2], 450, 780);
		g2.drawString("Total Score: " + scores[3], 450, 800);
*/
	}

	@Override
	public Dimension getPreferredSize ()
	{
		return new Dimension(width, height);
	}

	public Board (boolean clone)
	{

		Properties boardProperties = new Properties();
		int numOfRowsFromProp = 0;

		try {
			//load a properties file
			boardProperties.load(new FileInputStream("boardConfig.properties"));

			//get the property value and print it out
			numOfRowsFromProp =Integer.parseInt(boardProperties.getProperty("numOfRows"));


			//System.out.println(numOfRowsFromProp);


		} catch (IOException ex) {
			ex.printStackTrace();
		}

		graphicsWidth = GomokuUtilities.WIDTH;
		graphicsHeight = GomokuUtilities.HEIGHT;
		scores = new int[4];
		scores[0] = 0;
		scores[1] = 0;
		scores[2] = 0;
		scores[3] = 0;

		this.cols = GomokuUtilities.NUMBER_OF_COLUMNS;
		if (numOfRowsFromProp == 0){
			this.rows = GomokuUtilities.NUMBER_OF_ROWS;
		}
		else{
			
			this.rows = numOfRowsFromProp;
			//System.out.println(this.rows);
		}
		this.pRows = GomokuUtilities.NUMBER_OF_PLAYABLE_ROWS;
		this.height = GomokuUtilities.WINDOW_HEIGHT;
		this.width = GomokuUtilities.WINDOW_WIDTH;
		this.isClone = clone;

		fullBoard = new ArrayList<ArrayList<Tile>>();
		for (int i = 0; i < cols; i++) {
			fullBoard.add(new ArrayList<Tile>());
		}
	}

	void initializeBoard ()
	{

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {

				setTile(GomokuUtilities.TILE_ID++, j, i, 0,
						false);
				/*while (checkForInitialTriple(j, i, CrushUtilities.LEFT)
						|| checkForInitialTriple(j, i, CrushUtilities.DOWN))
					getTile(j, i).setColor((int) (Math.random() * 7));*/

			}
		}
	}

	void resetBoard ()
	{
		for (int i = 0; i < cols; i++)
			fullBoard.get(i).clear();

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {

				setTile(GomokuUtilities.TILE_ID++, j, i, (int) (Math.random() * 7),
						false);
				while (checkForInitialTriple(j, i, GomokuUtilities.LEFT)
						|| checkForInitialTriple(j, i, GomokuUtilities.DOWN))
					getTile(j, i).setColor((int) (Math.random() * 7));

			}
		}

		System.out.println("NO MORE MOVES! BOARD RESETED");
	}
	public boolean getIsClone(){
		return isClone;
	}

	public int getCols ()
	{
		return cols;
	}

	public int getRows ()
	{
		return rows;
	}

	public int getPRows ()
	{
		return pRows;
	}

	void setTile (int id, int x, int y, int color, boolean mark)
	{
		fullBoard.get(x).add(new Tile(id, x, y, color, mark,0));
	}

	void setTile (Tile tile)
	{

		setTile(tile.id, tile.getX(), tile.getY(), tile.getColor(), tile.getMark());
	}

	public Tile getTile (int x, int y)
	{
		return fullBoard.get(x).get(y);
	}

	void removeTile (int x, int y)
	{
		fullBoard.get(x).remove(y);
		// fullBoard.get(x).add(new Tile(x,rows-1));
		setTile(GomokuUtilities.TILE_ID++, x, rows - 1, (int) (Math.random() * 7),
				false);

		for (int i = 0; i < cols; i++) {

			for (int j = 0; j < rows; j++) {

				fullBoard.get(i).get(j).setX(i);
				fullBoard.get(i).get(j).setY(j);

			}

		}

	}

	boolean checkForInitialTriple (int x, int y, int direction)
	{
		if (direction == GomokuUtilities.DOWN) {

			if (y < 2)
				return false;
			else {

				if (fullBoard.get(x).get(y).getColor() == fullBoard.get(x).get(y - 1)
						.getColor()
						&& fullBoard.get(x).get(y).getColor() == fullBoard.get(x)
						.get(y - 2).getColor())
					return true;
				else
					return false;
			}
		}

		if (direction == GomokuUtilities.LEFT) {
			if (x < 2)
				return false;
			else {

				if (fullBoard.get(x).get(y).getColor() == fullBoard.get(x - 1).get(y)
						.getColor()
						&& fullBoard.get(x).get(y).getColor() == fullBoard.get(x - 2)
						.get(y).getColor())
					return true;
				else
					return false;

			}

		}

		return false;

	}

	public void printBoard ()
	{
		System.out.println("---------START OF BOARD-------");

		for (int j = (pRows) - 1; j >= 0; j--) {

			for (int i = 0; i < cols; i++) {

				// System.out.print(getTile(i, j).getColor() + "/" + getTile(i,
				// j).getId()
				// + "/" + getTile(i, j).getX() + "/"
				// + getTile(i, j).getY() + " ");
				System.out.print(getTile(i, j).getColor() + " ");

			}

			System.out.println();

		}
		System.out.println("---------END OF BOARD-------");

	}

	public void printFullBoard ()
	{
		System.out.println("---------START OF FULL BOARD-------");

		for (int j = rows - 1; j >= 0; j--) {

			for (int i = 0; i < cols; i++) {

				// System.out.print(getTile(i, j).getColor() + "/" + getTile(i,
				// j).getId()
				// + "/" + getTile(i, j).getX() + "/"
				// + getTile(i, j).getY() + " ");
				System.out.print(getTile(i, j).getColor() + " ");

			}

			System.out.println();

		}
		System.out.println("---------END OF FULL BOARD-------");

	}

	ArrayList<int[]> checkForTriples ()
	{
		ArrayList<int[]> availTriples = new ArrayList<int[]>();
		availTriples.clear();

		int x = getCols();
		int y = getPRows();

		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {

				if (avTriplesUp(getTile(i, j))) {
					int[] a = { i, j, GomokuUtilities.UP };
					availTriples.add(a);
				}

				if (avTriplesDown(getTile(i, j))) {
					int[] a = { i, j, GomokuUtilities.DOWN };
					availTriples.add(a);
				}

				if (avTriplesLeft(getTile(i, j))) {
					int[] a = { i, j, GomokuUtilities.LEFT };
					availTriples.add(a);
				}

				if (avTriplesRight(getTile(i, j))) {
					int[] a = { i, j, GomokuUtilities.RIGHT };
					availTriples.add(a);
				}

			}
		}

		// for (int i = 0; i < availTriples.size(); i++) {
			// int[] b = availTriples.get(i);
		// System.out.print(b[0] + " " + b[1] + " " + b[2]);
		// System.out.println();
		// }
		// System.out.println("-----");

		return availTriples;

	}

	boolean avTriplesUp (Tile tile)
	{
		int x = tile.getX();
		int y = tile.getY();
		int color = tile.getColor();
		int sColumns = getCols() - 1;
		int sRows = getPRows() - 1;

		if ((y <= ((sRows) - 3) && color == getTile(x, y + 2).getColor() && color == getTile(
				x,
				y + 3)
				.getColor()))
			return true;

		if (x >= 2 && y <= (sRows - 1) && color == getTile(x - 2, y + 1).getColor()
				&& color == getTile(x - 1, y + 1).getColor())
			return true;

		if (x >= 1 && y <= (sRows - 1) && x <= sColumns - 1
				&& color == getTile(x - 1, y + 1).getColor()
				&& color == getTile(x + 1, y + 1).getColor())
			return true;

		if (x <= sColumns - 2 && y <= (sRows - 1)
				&& color == getTile(x + 1, y + 1).getColor()
				&& color == getTile(x + 2, y + 1).getColor())
			return true;

		return false;

	}

	boolean avTriplesDown (Tile tile)
	{
		int x = tile.getX();
		int y = tile.getY();
		int color = tile.getColor();
		int sColumns = getCols() - 1;

		if ((y >= 3 && color == getTile(x, y - 2).getColor() && color == getTile(
				x,
				y - 3)
				.getColor())
				|| (x >= 2 && y >= 1 && color == getTile(x - 2, y - 1).getColor() && color == getTile(
						x - 1,
						y - 1)
						.getColor())
						|| (x >= 1 && x <= sColumns - 1 && y >= 1
						&& color == getTile(x - 1, y - 1).getColor() && color == getTile(
								x + 1,
								y - 1)
								.getColor())
								|| (x <= sColumns - 2 && y >= 1
								&& color == getTile(x + 1, y - 1).getColor() && color == getTile(
										x + 2,
										y - 1)
										.getColor()))
			return true;
		else
			return false;

	}

	boolean avTriplesLeft (Tile tile)
	{
		int x = tile.getX();
		int y = tile.getY();
		int color = tile.getColor();
		int sColumns = getCols() - 1;

		if ((x >= 3 && color == getTile(x - 2, y).getColor() && color == getTile(
				x - 3,
				y)
				.getColor())
				|| (x >= 1 && y >= 2 && color == getTile(x - 1, y - 2).getColor() && color == getTile(
						x - 1,
						y - 1)
						.getColor())
						|| (y >= 1 && y <= sColumns - 1 && x >= 1
						&& color == getTile(x - 1, y - 1).getColor() && color == getTile(
								x - 1,
								y + 1)
								.getColor())
								|| (y <= sColumns - 2 && x >= 1
								&& color == getTile(x - 1, y + 1).getColor() && color == getTile(
										x - 1,
										y + 2)
										.getColor()))
			return true;
		else
			return false;

	}

	boolean avTriplesRight (Tile tile)
	{
		int x = tile.getX();
		int y = tile.getY();
		int color = tile.getColor();
		int sColumns = getCols() - 1;

		if ((x <= sColumns - 3 && color == getTile(x + 2, y).getColor() && color == getTile(
				x + 3,
				y)
				.getColor())
				|| (x <= sColumns - 1 && y >= 2
				&& color == getTile(x + 1, y - 2).getColor() && color == getTile(
						x + 1,
						y - 1)
						.getColor())
						|| (y >= 1 && y <= sColumns - 1 && x <= sColumns - 1
						&& color == getTile(x + 1, y - 1).getColor() && color == getTile(
								x + 1,
								y + 1)
								.getColor())
								|| (y <= sColumns - 2 && x <= sColumns - 1
								&& color == getTile(x + 1, y + 1).getColor() && color == getTile(
										x + 1,
										y + 2)
										.getColor()))
			return true;
		else
			return false;

	}

	void moveTile (int x1, int y1, int x2, int y2)
	{

		int colorA = getTile(x1, y1).getColor();
		int colorB = getTile(x2, y2).getColor();

		getTile(x1, y1).setColor(colorB);
		getTile(x2, y2).setColor(colorA);

	}

	int findCreatedNples ()
	{
		int points = 0;
		int countMarked = 0;

		for (int i = 0; i < getCols(); i++) {
			for (int j = 0; j < getPRows(); j++) {
				markNples(i, j, GomokuUtilities.LEFT);
				markNples(i, j, GomokuUtilities.DOWN);
			}
		}

		for (int i = 0; i < getCols(); i++)
			for (int j = 0; j < getPRows(); j++)
				if (getTile(i, j).getMark())
					countMarked++;

		points = countMarked;

		return points;
	}

	void markNples (int x, int y, int dir)
	{

		if (dir == GomokuUtilities.DOWN) {

			if (y >= 2) {

				if (getTile(x, y).getColor() == getTile(x, y - 1).getColor()
						&& getTile(x, y).getColor() == getTile(x, y - 2).getColor()) {
					getTile(x, y).setMark(true);
					getTile(x, y - 1).setMark(true);
					getTile(x, y - 2).setMark(true);

					if (y > 3 && getTile(x, y).getColor() == getTile(x, y - 3).getColor()) {
						getTile(x, y - 3).setMark(true);
						if (y > 4
								&& getTile(x, y).getColor() == getTile(x, y - 4).getColor()) {
							getTile(x, y - 4).setMark(true);
						}
					}

				}

			}

		}

		if (dir == GomokuUtilities.LEFT) {
			if (x >= 2) {

				if (getTile(x, y).getColor() == getTile(x - 1, y).getColor()
						&& getTile(x, y).getColor() == getTile(x - 2, y).getColor()) {
					getTile(x, y).setMark(true);
					getTile(x - 1, y).setMark(true);
					getTile(x - 2, y).setMark(true);
					if (x > 3 && getTile(x, y).getColor() == getTile(x - 3, y).getColor()) {
						getTile(x - 3, y).setMark(true);
						if (x > 4
								&& getTile(x, y).getColor() == getTile(x - 4, y).getColor()) {
							getTile(x - 4, y).setMark(true);
						}
					}

				}

			}

		}

	}

	void paintMarkedTiles (int id)
	{
		for (int i = 0; i < getCols(); i++) {
			for (int j = 0; j < getPRows(); j++) {
				if (getTile(i, j).getMark()) {
					if (id == 1)
						getTile(i, j).setColor(GomokuUtilities.BLUE_PLAYER);
					else
						getTile(i, j).setColor(GomokuUtilities.RED_PLAYER);
				}
			}
		}
	}

	void removeMarkedTiles ()
	{
		for (int k = 0; k < 3; k++)
			for (int i = 0; i < getCols(); i++)
				for (int j = 0; j < getPRows(); j++)
					if (getTile(i, j).getMark())
						removeTile(i, j);

	}

	boolean validMove (int[] move, ArrayList<int[]> availMoves)
	{
		int x = move[0];
		int y = move[1];
		int dir = -1;
		boolean answer = false;

		if (move[3] == move[1] + 1)
			dir = GomokuUtilities.UP;

		if (move[3] == move[1] - 1)
			dir = GomokuUtilities.DOWN;

		if (move[2] == move[0] - 1)
			dir = GomokuUtilities.LEFT;

		if (move[2] == move[0] + 1)
			dir = GomokuUtilities.RIGHT;

		for (int[] avMove: availMoves)
			if (x == avMove[0] && y == avMove[1] && dir == avMove[2])
				answer = true;

		return answer;

	}

	void getPlayersScore (int totalScoreA, int currentScoreA,
			int totalScoreB, int currentScoreB)
	{
		scores[1] = totalScoreA;
		scores[0] = currentScoreA;
		scores[3] = totalScoreB;
		scores[2] = currentScoreB;
	}

	void removeMarkedTilesForClone ()
	{
		for (int k = 0; k < 3; k++)
			for (int i = 0; i < getCols(); i++)
				for (int j = 0; j < getPRows(); j++)
					if (getTile(i, j).getMark())
						removeTileForClone(i, j);

	}

	void removeTileForClone (int x, int y)
	{
		fullBoard.get(x).remove(y);
		// fullBoard.get(x).add(new Tile(x,rows-1));
		setTile(GomokuUtilities.TILE_ID++, x, rows - 1, -1, false);

		for (int i = 0; i < cols; i++) {

			for (int j = 0; j < rows; j++) {

				fullBoard.get(i).get(j).setX(i);
				fullBoard.get(i).get(j).setY(j);

			}

		}

	}

}
