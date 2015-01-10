class Const {
    static int columns = GomokuUtilities.NUMBER_OF_COLUMNS;
    static int rows = GomokuUtilities.NUMBER_OF_ROWS;
    //Suppose I have some X,Y. Then X+dx[i],Y+dy[i] defines all possible moves from that X,Y position
    static int[] dx = {0,1,1,1,0,-1,-1,-1};
    static int[] dy = {1,1,0,-1,-1,-1,0,1};
    //Maximum depth of MiniMax algorithm
    static int maxDepth = 2;
}
