/*� ����� ������������� ��� ����:
 * 
 * ������ ��������, ��M: 7260 , email: csstefan@auth.gr , ��������: 6986634537
 * ������� �����������, ��M: 6959 , email: stefkini@gmail.com , ��������: 6947736733
 * 
 */

import java.util.ArrayList;

public class HeuristicPlayer implements AbstractPlayer
{
  // TODO Fill the class code.

  int score;
  int id;
  String name;

  public HeuristicPlayer (Integer pid)
  {
    id = pid;
    score = 0;

  }
 
  public HeuristicPlayer(Integer pid, int pscore, String pname){
	  score = pscore;
	  id = pid;
	  name=pname;
	  
  }

  public String getName ()
  {

    return "Heuristic";

  }

  public int getId ()
  {
    return id;
  }

  public void setScore (int score)
  {
    this.score = score;
  }

  public int getScore ()
  {
    return score;
  }

  public void setId (int id)
  {
    // TODO Auto-generated method stub
    this.id = id;

  }

  public void setName (String name)
  {
    // TODO Auto-generated method stub
    this.name = name;

  }

  /*
   * ���� � ��������� ����� �������� ��� ��� ������� ��� �������� �������. ������ ��� ���������� ���� ����� ��� ������ ��� ���������� ��� ���� �� ��� ���������� ����������.
   */
  public int[] getNextMove (Board board)
  {		
	  int[] pos;
	  //���������� ������ �� ���� ��� ������
	  ArrayList<int[]> positions = new ArrayList<int[]>();
	  for(int i=0;i<GomokuUtilities.NUMBER_OF_ROWS;i++) {
		  for(int j=0;j<GomokuUtilities.NUMBER_OF_COLUMNS;j++) {
			  pos = new int[3];
			  pos[0] = i;
			  pos[1] = j;
			  //���������� ���� �����
			  pos[2] = evaluate(i,j,board);
			  positions.add(pos);
		  }
	  }
	  int max = (int)(Double.NEGATIVE_INFINITY);
	  int x = 0, y = 0;

	  //��������� ����� �� ��� ���������� ����
	  for(int i=0;i<positions.size();i++) {
		  pos = positions.get(i);

		  if (pos[2]>max) {
			  max = pos[2];
			  x = pos[0];
			  y = pos[1];
		  }
	  }

	  int[] res = {x,y};
	  return res;
  
  }
  
  /*
   * ���� � ��������� ��������� ��� ���� ��� ������ ��� �� ���� ���������� ����� ��� ��� ������� ������. ���������� ����� ��� �������� (-INF, +INF).
   */
  int evaluate (int x, int y, Board board)
  {	
	  Tile tile = board.getTile(x, y);
	  int color = tile.getColor();
	  int result = 0;
	  if (color!=0) {
		  return (int)(Double.NEGATIVE_INFINITY);
	  }
	  else {
		  result = result + (int)(GomokuUtilities.colorPercentage(board, x, y, 3, id)*5);
		  if (id == 1)
			  result = result - (int)(GomokuUtilities.colorPercentage(board, x, y, 3, 2)*5);
		  else
			  result = result - (int)(GomokuUtilities.colorPercentage(board, x, y, 3, 1)*5);
		  result = result + (int)(central(x,y)*10);
		  result = result + createsBunch(x, y, board, id)*30;
		  if (id == 1)
			  result = result + createsBunch(x, y, board, 2)*10;
		  else
			  result = result + createsBunch(x, y, board, 1)*10;
		  return result;
	  }
  }
  
  /*
   * ���� � ��������� ���������� ��� ���� ��� �������� [0,1] ������� �� �� ���� ����� ��� ������ ��������� ��� ������ ��� ������.
   */
  float central (int x, int y) {

	  return (2-(Math.abs(x-(int)(GomokuUtilities.NUMBER_OF_ROWS/2))/((int)(GomokuUtilities.NUMBER_OF_ROWS/2)))-(Math.abs(y-(int)(GomokuUtilities.NUMBER_OF_COLUMNS/2))/((int)(GomokuUtilities.NUMBER_OF_COLUMNS/2))))/2;
  }
  
  /*
   * ���� � ��������� ������� �� ��� ������ �� ����������� ��� ����� ����� �������� ��� ���������� �� ������� ������� ���� ��� 4 ������������ ��� �������������.
   */
  int createsBunch(int x, int y, Board board, int pid) {
	  int right,left,up,down, uprightdiagx, uprightdiagy, downrightdiagx, downrightdiagy, upleftdiagx, upleftdiagy, downleftdiagx, downleftdiagy;
	  int hor, ver, diag1, diag2;
	  
	  //Check horizontal
	  for (right = x; right<GomokuUtilities.NUMBER_OF_COLUMNS && tileBelongs(right, y, board, pid); ++right) {}
	  right--;
	  for ( left = x; left>=0 && tileBelongs(left, y, board, pid); --left ) {}
	  left++;
	  hor = right - left + 1;
	  
	  //Check vertical
	  for (down = y; down<GomokuUtilities.NUMBER_OF_ROWS && tileBelongs(x, down, board, pid); ++down) {}
	  down--;
	  for (up = y; up>=0 && tileBelongs(x, up, board, pid); --up ) {}
	  up++;
	  ver = down - up + 1;
	  
	  //Check diagonal 1
	  for (uprightdiagx = x, uprightdiagy = y; uprightdiagx<GomokuUtilities.NUMBER_OF_ROWS && uprightdiagy>=0 && tileBelongs(uprightdiagx, uprightdiagy, board, pid); ++uprightdiagx, --uprightdiagy) {}
	  uprightdiagx--;
	  for (downleftdiagx = x, downleftdiagy = y; downleftdiagy<GomokuUtilities.NUMBER_OF_COLUMNS && downleftdiagx>=0 && tileBelongs(downleftdiagx, downleftdiagy, board, pid); --downleftdiagx, ++downleftdiagy) {}
	  downleftdiagx++;
	  diag1 = uprightdiagx - downleftdiagx + 1;
	  
	  //Check diagonal 2
	  for (downrightdiagx = x, downrightdiagy = y; downrightdiagx<GomokuUtilities.NUMBER_OF_ROWS && downrightdiagy<GomokuUtilities.NUMBER_OF_COLUMNS && tileBelongs(downrightdiagx, downrightdiagy, board, pid); ++downrightdiagx, ++downrightdiagy) {}
	  downrightdiagx--;
	  for (upleftdiagx = x, upleftdiagy = y; upleftdiagy>=0 && upleftdiagx>=0 && tileBelongs(upleftdiagx, upleftdiagy, board, pid); --upleftdiagx, --upleftdiagy) {}
	  upleftdiagx++;
	  diag2 = downrightdiagx - upleftdiagx + 1;
	  
	  //Find the max value
	  int maxVal = Math.max( hor, Math.max( ver, Math.max( diag1, diag2 ) ) );
	  
	  if (maxVal<0) {
		  return 0;
	  }
	  else {
		  return maxVal;
	  }
  }
  
  /*
   * ���� � ��������� ������� �� ��� ����� ������ ���� ������ �� id = pid.
   */
  boolean tileBelongs(int x, int y, Board board, int pid) {
	  Tile myTile = board.getTile(x,y);
	  int color = myTile.getColor();
	  if (color==pid) {
		  return true;
	  }
	  else {
		  return false;
	  }
  }
  
}
