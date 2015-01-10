class MinMaxBoard {
    int[][] board;
    int availableMoves;

    MinMaxBoard(){
	board= new int[Const.columns][Const.rows];
	availableMoves = Const.columns*Const.rows;
	for(int i=0; i<Const.columns; i++)
	    for(int j=0; j<Const.rows; j++)
		board[i][j]= 0;	
    }

    //Copy constructor
    MinMaxBoard(MinMaxBoard that){
	board= that.board.clone();
    }

    /* Check if is already played and return an error
       void makeMove(int[] move, int pid) throws IllegalArgumentException{
       if (board[move[0]][move[1]] == 0) board[move[0]][move[1]]= pid;
       else throw new IllegalArgumentException("Made move on occupied tile!!");
       n       }

       But we prefer not checking, so that we can revert the board to it's previous state
    */

    int evaluate(int plays, int me) {
	int x, y, dep, player, dir, i;
	int[][] lim;
	int[][] win = new int[4][3];
	int[][] tuple = new int[5][3];

	for (x=0; x<Const.columns; ++x ) {
	    for ( y=0; y<Const.rows; ++y ) {
		//trap is already set
		if ( getColor(x,y) != 0 ) {
		    ++win[trap(x,y,getColor(x,y))][getColor(x,y)];
		}
		else {
		    //Check for 4/3/2 in a row, by checking the two empty squares next to them
		    //That means we double check everything, so we just divide by 2 at the end!
		    for ( player=1; player<=2; ++player ) {
			lim = checkAreaAround(x,y,player);
			for ( dir=0; dir<8; ++dir ) {
			    if ( lim[dir][0] >= 2 && lim[dir][0] <= 4 && lim[dir][2] + lim[(dir+4)%8][2] + 1 >= 5 ) {
				++tuple[ lim[dir][0] ][player];
			    }
			}
		    }
		}
	    }
	}

	for ( dep=0; dep<3; ++dep ) {
	    //If the one who plays has a trap at least as good as the other player, he won
	    if ( win[dep][plays] > 0 ) {
		return plays == me ? Integer.MAX_VALUE : Integer.MIN_VALUE;
	    }
	    //If the other player has, he wins
	    if ( win[dep][oppID(plays)] > 0 ) {
		return plays == me ? Integer.MIN_VALUE : Integer.MAX_VALUE;
	    }
	}

	//Enough with the traps, let's just check 4 in a row, 3 in a row, 2 in a row
	int[] w = {0,0,10,33,100};
	int eval = 0;
	for ( i=2; i<=4; ++i ) {
	    eval += w[i] * ( tuple[i][me] - tuple[i][oppID(me)] );
	}
	return eval;
    }
	
    void makeMove(int[] move, int pid) {
	board[ move[0] ][ move[1] ] = pid;
	if ( pid == 0 ) {
	    ++availableMoves;
	}
	else {
	    --availableMoves;
	}
    }

    int getAvailableMoves() {
	return availableMoves;
    }
    
    int getColor(int x, int y){
	if (x < 0 || x >= Const.columns || y < 0 || y >= Const.rows)
	    return -1;
	else return board[x][y];
    }
	
    int oppID(int id) {
	return id==1?2:1;
    }

    int trap (int x, int y, int id) {
	int[][] lim = checkAreaAround(x,y,id);
	int dir, trap;
	//Check if I make quintuple (depth-0 victory condition)
	//These are the highest priority positions
	if (makesNTuples(5, x,y, id,lim) >= 1) return 0;
	//Check if I have 3 (4 with x-y) in a row, two free cells on one side and one free cell on the other
	//(depth-1 victory condition)
	trap = 0;
	for ( dir=0; dir<4; ++dir ) {
	    if ( lim[dir][0] + lim[dir+4][0] == 3 ) {
		if ( lim[dir][1] >= 1 && lim[dir+4][1] >=1 ) {
		    return 1;
		}
		if ( lim[dir][1] + lim[dir+4][1] > 0 ) {
		    ++trap;
		}
	    }
	}
	if ( trap >= 2 ) {
	    return 1;
	}
	//Depth-2
	trap = 0;
	for ( dir=0; dir<4; ++dir ) {
	    if ( lim[dir][0] + lim[dir+4][0] == 2 ) {
		if ( ( lim[dir][1] >= 2 && lim[dir+4][1] >=1 ) || ( lim[dir][1] >=1 && lim[dir+4][1] >= 2 ) ) {
		    ++trap;
		}
	    }
	}
	if ( trap >= 2 ) {
	    return 2;
	}

	//nothing
	return 3;
    }

    int makesNTuples(int N, int x, int y, int pid,int[][] lim) throws IllegalArgumentException{
	if(N<2 || N>5) throw new IllegalArgumentException("N out of bounds [2,5]!");
	int ans, dir, tmp;
	
	ans = 0;
	for ( dir=0; dir<4; ++dir ) {
	    tmp = lim[dir][0] + lim[dir+4][0] >= N-1 ? 1 : 0;
	    ans += tmp;
	}

	return ans;
    }

    int partakesIn5Tuples(int x, int y,int id,int[][] lim) throws IllegalArgumentException{
	int ans, tmp, dir;

	ans = 0;
	for (dir=0; dir<4; ++dir ) {
	    tmp = lim[dir][2] + lim[dir+4][2] >= 4? lim[dir][2] + lim[dir+4][2] - 4 : 0;
	    ans += tmp;
	}

	return ans;
    }
    
    /*
     * lim[i][1] -> how many of pid in i direction
     * lim[i][2] -> how many free after that in i direction
     * lim[i][3] -> how many ( mixed mine and free ) until we hit wall
     */
    int[][] checkAreaAround(int x,int y, int pid){
	int[][] lim= new int[8][3];
	int[] move = {x,y};
	int i, dir, xt, yt, xPrev, yPrev, prevColor;

	//All directions
	prevColor = board[move[0]][move[1]];
	board[move[0]][move[1]] = pid;
	for ( dir=0; dir<8; ++dir ) {
	    for ( i=1; i<=5 ;++i ) {
		xt = x + i*Const.dx[dir];
		yt = y + i*Const.dy[dir];
		xPrev = xt - Const.dx[dir];
		yPrev = yt - Const.dy[dir];

		//If I am going out of bounds, or found an enemy tile, or finished free cells and going again to non-free, that's it
		if ( xt < 0 || xt >= Const.columns || yt < 0 || yt >= Const.rows || (getColor(xPrev,yPrev)==0 && getColor(xt,yt)==pid) || getColor(xt,yt) == oppID(pid) ) {
		    break;
		}
		if ( getColor(xt,yt) == pid ) {
		    ++lim[dir][0];
		}
		else {
		    ++lim[dir][1];
		}
	    }

	    lim[dir][2] = lim[dir][0] + lim[dir][1];
	    for ( ;i<=5;++i ) {
		xt = x + i*Const.dx[dir];
		yt = y + i*Const.dy[dir];
		if ( xt<0 || xt >= Const.columns || yt<0 || yt >= Const.rows || getColor(xt,yt) == oppID(pid) ) {
		    break;
		}
		++lim[dir][2];
	    }
	}
	board[move[0]][move[1]] = prevColor;
	
	return lim;
    }

    int evaluate (int x, int y, int id) throws IllegalArgumentException{
	if (getColor(x,y) != 0)
	    throw new IllegalArgumentException("Tile("+x+","+y+") isn't empty!");
	int[][] lim = checkAreaAround(x,y,id);
	int[][] lim2 = checkAreaAround(x,y,oppID(id));
	int dir, trap;
	//Check if I or the opponent makes quintuple (depth-1 victory condition)
	//These are the highest priority positions
	if (makesNTuples(5, x,y, id,lim) >= 1) return Integer.MAX_VALUE;
	if (makesNTuples(5, x,y, oppID(id), lim2) >= 1) return Integer.MAX_VALUE-1;
	//Check if I or the opponent has 3 in a row, two free cells on one side and one free cell on the other
	//(depth-2 victory condition)
	trap = 0;
	for ( dir=0; dir<4; ++dir ) {
	    if ( lim[dir][0] + lim[dir+4][0] == 3 ) {
		if ( lim[dir][1] >= 1 && lim[dir+4][1] >=1 ) {
		    return Integer.MAX_VALUE-2;
		}
		if ( lim[dir][1] + lim[dir+4][1] > 0 ) {
		    ++trap;
		}
	    }
	}
	if ( trap >= 2 ) {
	    return Integer.MAX_VALUE-2;
	}
	//Opponent depth-2
	trap = 0;
	for ( dir=0; dir<4; ++dir ) {
	    if ( lim2[dir][0] + lim2[dir+4][0] == 3 ) {
		if ( lim2[dir][1] >= 1 && lim2[dir+4][1] >=1 ) {
		    return Integer.MAX_VALUE-3;
		}
		if ( lim2[dir][1] + lim2[dir+4][1] > 0 ) {
		    ++trap;
		}
	    }
	}
	if ( trap >= 2 ) {
	    return Integer.MAX_VALUE-3;
	}
	//Depth-3 mine
	trap = 0;
	for ( dir=0; dir<4; ++dir ) {
	    if ( lim[dir][0] + lim[dir+4][0] == 2 ) {
		if ( ( lim[dir][1] >= 2 && lim[dir+4][1] >=1 ) || ( lim[dir][1] >=1 && lim[dir+4][1] >= 2 ) ) {
		    ++trap;
		}
	    }
	}
	if ( trap >= 2 ) {
	    return Integer.MAX_VALUE-4;
	}
	//Opponent depth-3
	trap = 0;
	for ( dir=0; dir<4; ++dir ) {
	    if ( lim2[dir][0] + lim2[dir+4][0] == 2 ) {
		if ( ( lim2[dir][1] >= 2 && lim2[dir+4][1] >= 1 ) || (lim2[dir][1] >=1 && lim2[dir+4][1] >=2) ) {
		    ++trap;
		}
	    }
	}
	if ( trap >= 2 ) {
	    return Integer.MAX_VALUE-5;
	}
	// Check some predefined features of the position that contribute to its score
	//The domination difference in range-2 area of (x,y), domain [-10,10]
	double range2DColor= (colorPercentage(x, y, 2, id) -
			      colorPercentage(x, y, 2, oppID(id))) * 10;
	//The domination difference in range-3 area of (x,y), domain [-10,10]
	double range3DColor= (colorPercentage( x, y, 3, id) -
			      colorPercentage( x, y, 3, oppID(id))) * 10;
	//The domination difference in range-4 area of (x,y), domain [-10,10]
	double range4DColor= (colorPercentage( x, y, 4, id) -
			      colorPercentage( x, y, 4, oppID(id))) * 10;
	//Take the square of the domination differences, to emphasize on areas
	//occupied mostly by either player. Domain: [0,100]
	range2DColor*= range2DColor;
	range3DColor*= range3DColor;
	range4DColor*= range4DColor;
	//Take a linear combination of the features, with every feature having a
	//domain of ~[0,100]
	double[] w= {5,10,15, 3, 2,4,10,100};
	int evaluation= (int)(
			      w[0]*range2DColor + w[1]*range3DColor + w[2]*range4DColor +
			      w[3]*(5*partakesIn5Tuples(x,y,id,lim)) +
			      w[4]*(75*makesNTuples(2,x,y,id,lim)) +
			      w[5]*(75*makesNTuples(3,x,y,id,lim)) +
			      w[6]*(75*makesNTuples(4,x,y,id,lim)) +
			      w[7]*(75*makesNTuples(4,x,y,oppID(id),lim2))
			      );
	return evaluation;
    }

    double colorPercentage(int x, int y, int radius, int player){
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
		    if (getColor(i,j)==player){
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
}
