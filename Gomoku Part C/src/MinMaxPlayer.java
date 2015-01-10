/**
 * @author Konstantinos Samaras-Tsakiris | kisamara@auth.gr    | AEM: 7972
 * @author Vangelis Kipouridis           | kipoujr@hotmail.com | AEM: 7899
 */
public class MinMaxPlayer implements AbstractPlayer
{

    int score;
    int id;
    String name;
    MinMaxBoard board;

    public MinMaxPlayer (Integer pid){
        id = pid;
        score = 0;
    }
    public MinMaxPlayer(Integer pid, int pscore, String pname){
        score = pscore;
        id = pid;
        name=pname;
    }

    public String getName (){ return "MinMax"; }
    public int getId (){ return id; }
    public void setScore (int score){ this.score = score; }
    public int getScore (){ return score; }
    public void setId (int id){ this.id = id; }
    public void setName (String name){ this.name = name; }
  
    public int oppID() {return (id==1)? 2: 1;}

    /**
     * If it is the first move, play at the center.
     * Else do a minmax algorithm.
     * If minmax reports that some player has a trap set, then we switch to the evaluation function of part B, since it is sufficient, no need for the big insight of minmax
     * @param b : Gomoku Board
     * @return coordinates of our move
     */
    public int[] getNextMove ( Board b )
    {
        //Update board
        boolean[] firstMove = checkFirstMove(b);
        //firstMove[0] -> It is first move
        //firstMove[1] -> And I play first
        if ( firstMove[0] == true ) {
            board = new MinMaxBoard();
            int center = Const.columns/2;
            //his move
            if ( firstMove[1] == false ) {
                board.makeMove(GomokuUtilities.getPreviousMove(), oppID());
            }

            //my move
            if ( firstMove[1] == false && b.getTile(center,center).getColor() != 0 ) {
                int[] move = {center,center-1};
                board.makeMove(move,id);
                return move;
            }
            else {
                int[] move = {center,center};
                board.makeMove(move,id);
                return move;
            }
        }
        //Main part of the code
        else {
            board.makeMove(GomokuUtilities.getPreviousMove(),oppID());

            int[] move = DFS(0,Integer.MIN_VALUE,Integer.MAX_VALUE);

            //If we reached a trap ( definite move if played correct ), either by us or our opponent
            //Then we don't need the insight of minmax, the part-B heuristic player is sufficient
            //Let him take control from now on
            if ( move[2] == Integer.MAX_VALUE || move[2] == Integer.MIN_VALUE ) {
                int tmp;
                move[2] = Integer.MIN_VALUE;
                for ( int x=0; x<Const.columns; ++x ) {
                    for ( int y=0; y<Const.rows; ++y ) {
                        if ( board.getColor(x,y) == 0 ) {
                            tmp = board.evaluate(x,y,id);
                            if ( tmp > move[2] ) {
                                move[0] = x; move[1] = y; move[2] = tmp;
                            }
                        }
                    }
                }          
            }

            board.makeMove(move,id);
            return move;
        }
    }

    /** The main minmax algorithm, with AB-pruning
        No need to store the entire tree. It suffices to hold the nodes currently in my path, using a depth first search.
        Depth first search also gives the advantage that i don't need to store the current board for every Node.
        It suffices to have one, global, board, and to change it as I visit some node. When I finish with it, I undo the work i've done, and i'm back to the old board.
        @param dep : current depth
        @param a : alpha, from alpha beta pruning
        @param b : beta, from alpha beta pruning
        @return [x coordinate of best move, y coordinate, evaluation]
    */
    private int[] DFS (int dep, int a, int b)
    {
        int[] ans = new int[3];
        int evaluate = board.evaluate( dep%2==0 ?id :oppID() ,id);

        if ( dep == Const.maxDepth || evaluate == Integer.MAX_VALUE || evaluate == Integer.MIN_VALUE ) {
            ans[2] = evaluate;
            return ans;
        }

        int x, y, tmp;
        int[] move = new int[2];
        
        //If maximizer
        if ( dep % 2 == 0 ) {
            ans[2] = Integer.MIN_VALUE;
            //all children which touch a tile, the other are useless
            for ( x=0; x<Const.columns; ++x ) {
                for ( y=0; y<Const.rows; ++y ) {
                    if ( board.getColor(x,y)==0 && board.near(x,y) ) {
                        //update board
                        move[0] = x; move[1] = y;
                        board.makeMove(move,id);

                        //go to child and update values
                        tmp = DFS(dep+1,a,b)[2];
                        if ( tmp > ans[2] ) {
                            ans[0] = x; ans[1] = y; ans[2] = tmp;
                        }
                        a = Math.max ( a, ans[2] );

                        //undo move
                        board.makeMove(move,0);

                        //a-b pruning
                        if ( b <= a ) {
                            return ans;
                        }
                    }
                }
            }
            return ans;
        }
        //minimizer
        else {
            ans[2] = Integer.MAX_VALUE;
            //all children which touch a tile, the other are useless            
            for ( x=0; x<Const.columns; ++x ) {
                for ( y=0; y<Const.rows; ++y ) {
                    if ( board.getColor(x,y) == 0 && board.near(x,y) ) {
                        //update board
                        move[0] = x; move[1] = y;
                        board.makeMove(move,oppID());

                        //go to child and update values
                        tmp = DFS(dep+1,a,b)[2];
                        if ( tmp < ans[2] ) {
                            ans[0] = x; ans[1] = y; ans[2] = tmp;
                        }
                        b = Math.min ( b, ans[2] );

                        //undo move
                        board.makeMove(move,0);

                        //a-b pruning
                        if ( b <= a ) {
                            return ans;
                        }
                    }
                }
            }
            return ans;   
        }
    }


    /**
       @param b : Gomoku Board
       @return First value is true if it is our first move.
       Second value is true if I play before my opponent ( black tiles )
    */
    private boolean[] checkFirstMove( Board b ) {
        int x, y, moves;
        boolean[] ans = new boolean[2];

        moves = 0;
        for ( x=0; x<Const.columns; ++x ) {
            for ( y=0; y<Const.rows; ++y ) {
                if ( b.getTile(x,y).getColor() != 0 ) {
                    ++moves;
                }
            }
        }

        ans[0] = moves<2 ? true : false;
        ans[1] = moves==0 ? true : false;
        return ans;
    }

}
