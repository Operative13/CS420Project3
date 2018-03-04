//package project3;

import java.io.Serializable;
import java.util.*;   // DELETE AFTER TESTING

public class Minimax implements Serializable
{
   private ArrayList<String> aiMovesLog;
   private double maxTime;
   private Board board;
   private String aiMark, playerMark;

   // DELETE AFTER TESTING

   // temp method for testing game with random ai moves
   public void testMove()
   {
       Random r = new Random();
       while(true)
       {
           int row = r.nextInt(8) + 1;  // range 1-8
           int col = r.nextInt(8) + 1;  // range 1-8

           // generate new row and col if the current space has been used
           if (!board.getBoard()[row][col].equals("_"))
                continue;

           board.markMove(row, col, aiMark);
           aiMovesLog.add(convertMoveToString(row, col));
           break;
       }
   }

   public Minimax(Board board, double maxTime, String aiMarker, String playerMarker)
   {
      this.maxTime = maxTime;
      this.board = board;
      aiMark = aiMarker;
      playerMark = playerMarker;
      aiMovesLog = new ArrayList<String>(32); // 32 = board size / 2 = 8*8 / 2 = 64 / 2 = 32
   }

   public void makeMove()
   {
       // disable until the required methods are completed
       //
       // int[] moveIndex = findMove();
       // aiMovesLog.add(convertMoveToString(moveIndex[0], moveIndex[1]));

   }

   // return the a integer array containing the position of the best move to make
   public int[] getMove()
   {
       int bestValue = Integer.MIN_VALUE;
       int currentValue;
       int[] bestMove = new int[2];
       String[][] boardLayout = board.getBoard();

       // make the current state the root node
       // Node root = new Node(board.getBoard());

       // explore every empty space and calculate the cost of that move
       for (int row = 1; row < 9; row++)
       {
           for (int col = 1; col < 9; col++)
           {
               if (boardLayout[row][col].equals("_"))
               {
                   // make a possible move
                   boardLayout[row][col] = aiMark;

                   // determine this moves cost
                   currentValue = minimax(0, true, Integer.MIN_VALUE, Integer.MAX_VALUE);

                   // restore the previous board layout
                   boardLayout[row][col] = "_";

                   // save the best cost move
                   if (currentValue > bestValue)
                   {
                       bestValue = currentValue;
                       bestMove[0] = row;
                       bestMove[1] = col;
                   }
               }
           }
       }

       return bestMove;
   }

   // use the minimax algorithm with alpha beta pruning to determine the best value
   // of the current board playout depending on the current player (max or min)
   // alpha = best (highest value) found for MAX so far
   // beta = best (lowest value) found for MIN so far
   public int minimax(int depth, boolean isMax, int alpha, int beta)
   {
        // cut off point for search, THIS SHOULD AN BE OPTIMIZED FOR COMPETITION
        if (depth == 6)
        {
            //  RETURN THE COST OF THIS STATE
            return getCost();
        }

        int bestCost, currentCost;

        // maximizer
        // determine the best cost as the largest cost of the visited states
        if (isMax)
        {
            bestCost = Integer.MIN_VALUE;      // default to lowest possible value

            for (int row = 1; row < 9; row++)
            {
                for (int col = 1; col < 9; col++)
                {
                    if (board.getBoard()[row][col].equals("_"))
                    {
                        board.getBoard()[row][col] = playerMark;

                        currentCost = minimax(depth+1, !isMax, alpha, beta);
                        bestCost = max(bestCost, currentCost);
                        alpha = max(alpha, bestCost);

                        if (beta <= alpha)
                            break;

                        // reset board to the previous (original) state
                        board.getBoard()[row][col] = "_";
                    }
                }
            }
            return bestCost;
        }

        // minimizer
        // determine the best cost as the smallest cost of the visited states
        else
        {
            bestCost = Integer.MAX_VALUE;      // default to largest possible value

            for (int row = 1; row < 9; row++)
            {
                for (int col = 1; col < 9; col++)
                {
                    if (board.getBoard()[row][col].equals("_"))
                    {
                        board.getBoard()[row][col] = playerMark;

                        currentCost = minimax(depth+1, isMax, alpha, beta);
                        bestCost = min(bestCost, currentCost);
                        alpha = min(alpha, bestCost);

                        if (beta <= alpha)
                            break;

                        // reset board to the previous (original) state
                        board.getBoard()[row][col] = "_";
                    }
                }
            }
            return bestCost;
        }
    }

    // heuristic
    // calculate the cost of the current state
   public int getCost()
   {
         // <<<<<<<<<<<<< INSERT CODE THAT DETERMINES COST OF THE CURRENT (non-win) STATE >>>>>>>>>>>>>>>>>>>>>

         return 0;
    }

   public int max(int x, int y){return x > y ? x : y;}
   public int min(int x, int y){return x < y ? x : y;}

   public String convertMoveToString(int row, int col)
   {
       String move = "";
       switch (row)
       {
          case 1:
             move += "a";
             break;
          case 2:
             move += "b";
             break;
          case 3:
             move += "c";
             break;
          case 4:
             move += "d";
             break;
          case 5:
             move += "e";
             break;
          case 6:
             move += "f";
             break;
          case 7:
             move += "g";
             break;
          case 8:
             move += "h";
             break;
       }

       return move += col;
   }

   public ArrayList<String> getMovesLog(){return aiMovesLog;}
}
