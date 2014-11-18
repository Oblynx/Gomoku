/*
AEM : 7972  / 7899
Tel : 6998581293 / Den exo
email : kisamara@auth.gr / kipoujr@hotmail.com
Onomata : Konstantinos Samaras Tsakiris / Kipouridis Vangelis
*/

//The following class embodies the spirit of our latest software
public class RandomPlayer implements AbstractPlayer
{
    private int id, score;					//These are the properties of our class
    private String name;

    /**This is a simple constructor that takes only the name of the new player
     * @param pid
     */
    public RandomPlayer(Integer pid) {
	id = pid.intValue();
    }
    
    /**Whereas this particular one is a complex player-generating constructor machine, that defines not only the players' identity
     * but also many other fabulous properties
     * @param pid
     * @param name0
     * @param score0
     */
    public RandomPlayer(Integer pid,String name0,int score0) {
	id = pid.intValue();
	name = name0;
	score = score0;
    }

    /**What follows is a magnificent streak of get/set methods. They are all here; so much so that defining private properties
     * is actually very useless
     * @return
     */
    public int getId() {return id;}
    public void setId(int a) {id=a;}
    public String getName() {return name;}
    public void setName(String a) {name=a;}
    public int getScore() {return score;}
    public void setScore(int a) {score=a;}
    
    /**And now marvel at the beating heart of our RandomPlayer:
     * you have the unique chance of admiring a method that returns 2 random numbers
     * but with great significance. They personify our next move on the board
     * @param board
     */
    public int[] getNextMove( Board board ) {
	int[] xy = new int[2];

	do {
	    xy[0] = (int)(Math.random()*GomokuUtilities.NUMBER_OF_COLUMNS);
	    xy[1] = (int)(Math.random()*GomokuUtilities.NUMBER_OF_ROWS);
	}while (board.getTile(xy[0],xy[1]).getColor() != 0 );
	
	return xy;
    }
}
/* Thank you for your attention
 * Sincerely, a domes_dedomenon Team
 */
