import java.util.Random;
import java.util.ArrayList;
import java.util.*;
// SlidingBoard has a public field called size
// A size of 3 means a 3x3 board

// SlidingBoard has a method to getLegalMoves
//   ArrayList<SlidingMove> legalMoves = board.getLegalMoves();

// You can create possible moves using SlidingMove:
// This moves the piece at (row, col) into the empty slot
//   SlidingMove move = new SlidingMove(row, col);

// SlidingBoard can check a single SlidingMove for legality:
//   boolean legal = board.isLegalMove(move);

// SlidingBoard can check if a position is a winning one:
//   boolean hasWon = board.isSolved();

// SlidingBoard can perform a SlidingMove:
//   board.doMove(move);

// You can undo a move by saying the direction of the previous move
// For example, to undo the last move that moved a piece down into
// the empty space from above use:
//   board.undoMove(m, 0);

// You can dump the board to view with toString:
//   System.out.println(board);
class Node
{
   public SlidingBoard board;
   public ArrayList<SlidingMove> path;

   public Node(SlidingBoard b, ArrayList<SlidingMove> p)
   {
    board=b;
    path=p;
   }//end of constructor
}//end of node class

class SmarterBot extends SlidingPlayer {
    ArrayList<SlidingMove> path;
    int move_num = -1;

    // The constructor gets the initial board
    public SmarterBot(SlidingBoard _sb) {
        super(_sb);
        path=idfs(_sb);
    }

    //This method implements iterative dfs
    public ArrayList<SlidingMove> idfs(SlidingBoard board)
    {
        int max_depth=4;
      for (int i=0; i<1000000;i++){
        HashSet<String> previous = new HashSet <String>();
        LinkedList<Node> stack= new LinkedList<Node>();
        Node current_node = new Node (board, new ArrayList<SlidingMove>());
        counter=0
        //run while board is not solved
        while ( counter!=max_depth)
        {
          counter++;
          ArrayList<SlidingMove> legal_move = current_node.board.getLegalMoves();
          for(SlidingMove move : legal_move)
          {
              SlidingBoard newboard= new SlidingBoard(current_node.board.size);
              newboard.setBoard(current_node.board);
              newboard.doMove(move);
              //check to see if board state is seen
              if(!previous.contains(newboard.toString()))
              {
                previous.add(newboard.toString());
                ArrayList<SlidingMove> newpath= (ArrayList<SlidingMove>) current_node.path.clone();
                newpath.add(move);
                //ad to end of list
                stack.add(new Node(newboard,newpath));
              }//end of if
            }//end of for loop
            //remove from end of list
            current_node= stack.removeLast();
            if(current_node.board.isSolved()) {return current_node.path;}
          }//end of while loop
          max_depth+=4
        }//end of outer most for loop
      }//end of dfs

    //this method does the search using Depth-First Search
    public ArrayList<SlidingMove> dfs(SlidingBoard board)
    {
      HashSet<String> previous = new HashSet <String>();
      LinkedList<Node> stack= new LinkedList<Node>();
      Node current_node = new Node (board, new ArrayList<SlidingMove>());
      //run while board is not solved
      while (!current_node.board.isSolved())
      {
         ArrayList<SlidingMove> legal_move = current_node.board.getLegalMoves();
         for(SlidingMove move : legal_move)
         {
            SlidingBoard newboard= new SlidingBoard(current_node.board.size);
            newboard.setBoard(current_node.board);
            newboard.doMove(move);
            //check to see if board state is seen
            if(!previous.contains(newboard.toString()))
            {
               previous.add(newboard.toString());
               ArrayList<SlidingMove> newpath= (ArrayList<SlidingMove>) current_node.path.clone();
               newpath.add(move);
               //ad to end of list
               stack.add(new Node(newboard,newpath));
            }//end of if
         }//end of for loop
         //remove from end of list
         current_node= stack.removeLast();
      }//end of while loop
      return current_node.path;
    }//end of dfs

    //this method does the searchign using breadth first search
    public ArrayList<SlidingMove> bfs(SlidingBoard board)
    {
      HashSet<String> previous = new HashSet <String>();
      LinkedList<Node> stack= new LinkedList<Node>();
      Node current_node = new Node (board, new ArrayList<SlidingMove>());

      while (!current_node.board.isSolved())
      {
         ArrayList<SlidingMove> legal_move = current_node.board.getLegalMoves();
         for(SlidingMove move : legal_move)
         {
            SlidingBoard newboard= new SlidingBoard(current_node.board.size);
            newboard.setBoard(current_node.board);
            newboard.doMove(move);
            if(!previous.contains(newboard.toString()))
            {
               previous.add(newboard.toString());
               ArrayList<SlidingMove> newpath= (ArrayList<SlidingMove>) current_node.path.clone();
               newpath.add(move);
               //add to end of list
               stack.add(new Node(newboard,newpath));
            }//end of if
         }//end of for loop
        //remove first
        current_node= stack.removeFirst();
      }//end of while loop
      return current_node.path;
    }//end of bfs


    // Perform a single move based on the current given board state
    public SlidingMove makeMove(SlidingBoard board) {
       move_num++;
       return path.get(move_num);

           }
}
