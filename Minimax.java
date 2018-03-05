package project3;

import java.io.Serializable;
import java.util.*;   // DELETE AFTER TESTING

public class Minimax implements Serializable
{
    private ArrayList<String> aiMovesLog;
    private double maxTime;
    private Board board;
    private int[] aiMoves, playerMoves;
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

   public Minimax(Board board, double maxTime, String aiMarker, String playerMarker, int[] playerMoves)
   {
      this.maxTime = maxTime;
      this.board = board;

      aiMark = aiMarker;
      aiMoves = new int[64];
      aiMovesLog = new ArrayList<String>(32); // 32 = board size / 2 = 8*8 / 2 = 64 / 2 = 32

      playerMark = playerMarker;
      playerMoves = new int[64];
   }

   public void makeMove()
   {
       // disable until the required methods are completed
       //
       int[] moveIndex = getMove();
       aiMovesLog.add(convertMoveToString(moveIndex[0], moveIndex[1]));
       board.markMove(moveIndex[0],  moveIndex[1], aiMark);
   }

   // return an integer array containing the position of the best move to make
   public int[] getMove()
   {
       int bestValue = Integer.MIN_VALUE;
       int currentValue;
       int[] bestMove = new int[2];
       String[][] boardLayout = board.getBoard();

       long start = System.nanoTime();
       double elapsedTime = 0.0;

            // explore every empty space and calculate the cost of that move
            for (int row = 1; row < 9; row++)
            {
                for (int col = 1; col < 9 && elapsedTime < maxTime; col++)
                {
                    if (boardLayout[row][col].equals("_"))
                    {
                        // make a possible move
                        boardLayout[row][col] = aiMark;

                        // determine this moves cost
                        currentValue = minimax(0, true, Integer.MIN_VALUE, Integer.MAX_VALUE, start);

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
                    elapsedTime = ( (double)System.nanoTime() - start) / 1000000000.0;
                    System.out.println("elapsedTime: " + elapsedTime);
                }
            }

            System.out.println("BestValue: " + bestValue);
       return bestMove;
   }

   // use the minimax algorithm with alpha beta pruning to determine the best value
   // of the current board playout depending on the current player (max or min)
   // alpha = best (highest value) found for MAX so far
   // beta = best (lowest value) found for MIN so far
   public int minimax(int depth, boolean isMax, int alpha, int beta, long start)
   {
        // cut off point for search, THIS SHOULD AN BE OPTIMIZED FOR COMPETITION
        if (depth == 6)
        {
            //  RETURN THE COST OF THIS STATE
            int cost =  getCost();
            // System.out.println("Cost: " + cost);
            return cost;
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
                            board.getBoard()[row][col] = aiMark;

                            currentCost = minimax(depth+1, false, alpha, beta, start);
                            bestCost = max(bestCost, currentCost);
                            alpha = max(alpha, bestCost);

                            // reset board to the previous (original) state
                            board.getBoard()[row][col] = "_";

                            if (beta <= alpha)
                                break;
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
            {
                for (int row = 1; row < 9; row++)
                {
                    for (int col = 1; col < 9; col++)
                    {
                        if (board.getBoard()[row][col].equals("_"))
                        {
                            board.getBoard()[row][col] = playerMark;

                            currentCost = minimax(depth+1, true, alpha, beta, start);
                            bestCost = min(bestCost, currentCost);
                            beta = min(beta, bestCost);

                            // reset board to the previous (original) state
                            board.getBoard()[row][col] = "_";

                            if (beta <= alpha)
                                break;
                        }

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
        int cost = 0;
        int costAI = 0;
        int costPlayer = 0;

        int horCountAI = 0;
        int verCountAI = 0;

        int horCountPlayer = 0;
        int verCountPlayer = 0;

        int maximum = 10000;
        int minimum = 10000;

        String[][] boardLayout = board.getBoard();

        for (int i = 1; i < 9; i++)
        {
            for (int k = 1; k < 9; k++)
            {
                // check horizontal cases
                if (boardLayout[i][k].equalsIgnoreCase(aiMark))
                    horCountAI++;
                else if (boardLayout[i][k].equalsIgnoreCase(playerMark))
                    horCountPlayer++;

                // check vertical cases
                if (boardLayout[k][i].equalsIgnoreCase(aiMark))
                    verCountAI++;
                else if (boardLayout[k][i].equalsIgnoreCase(playerMark))
                    verCountPlayer++;

                // compute cost when at the edge of the board
                if (i == 8 || k == 8)
                {
                    // compute cost of the ai's horizonal line
                    if (horCountAI > 0)
                    {
                        if (horCountAI == 1)
                            cost += 1;
                        else if (horCountAI == 2)
                            cost += 10;
                        else if (horCountAI == 3)
                            cost += 100;
                        else if (horCountAI >= 4)
                            cost += maximum;

                        horCountAI = 0;    // reset counter
                    }

                    // compute cost of the ai's vertical line
                    if (verCountAI > 0)
                    {
                        if (verCountAI == 1)
                            cost += 1;
                        else if (verCountAI == 2)
                            cost += 10;
                        else if (verCountAI == 3)
                            cost += 100;
                        else if (verCountAI >= 4)
                           cost += maximum;

                        verCountAI = 0;    // reset counter
                    }

                    // compute cost of the player's horizonal line
                    if (horCountPlayer > 0)
                    {
                        if (horCountPlayer == 1)
                            cost -= 1;
                        else if (horCountPlayer == 2)
                            cost -= 10;
                        else if (horCountPlayer == 3)
                            cost -= 100;
                        else if (horCountPlayer >= 4)
                           cost -= minimum;

                        horCountPlayer = 0;    // reset counter
                    }

                    // compute cost of the player's vertical line
                    if (verCountPlayer > 0)
                    {
                        if (verCountPlayer == 1)
                            cost -= 1;
                        else if (verCountPlayer == 2)
                            cost -= 10;
                        else if (verCountPlayer == 3)
                            cost -= 100;
                        else if (verCountPlayer >= 4)
                           cost -= minimum;

                        verCountPlayer = 0;
                    }
                }

                // compute cost when not at the end of the board
                else if (i != 8 || k != 8)
                {
                    // compute cost of the ai's horizonal line
                    if (horCountAI > 0 && !boardLayout[i][k+1].equalsIgnoreCase(aiMark))
                    {
                        if (horCountAI == 1)
                            cost += 1;
                        else if (horCountAI == 2)
                            cost += 10;
                        else if (horCountAI == 3)
                            cost += 100;
                        else if (horCountAI >= 4)
                            cost += maximum;

                        horCountAI = 0;    // reset counter
                    }

                    // compute cost of the ai's vertical line
                    if (verCountAI > 0 && !boardLayout[k+1][i].equalsIgnoreCase(aiMark))
                    {
                        if (verCountAI == 1)
                            cost += 1;
                        else if (verCountAI == 2)
                            cost += 10;
                        else if (verCountAI == 3)
                            cost += 100;
                        else if (verCountAI >= 4)
                            cost += maximum;

                        verCountAI = 0;    // reset counter
                    }

                    // compute cost of the player's horizonal line
                    if (horCountPlayer > 0 && !boardLayout[i][k+1].equalsIgnoreCase(playerMark))
                    {
                        if (horCountPlayer == 1)
                            cost -= 1;
                        else if (horCountPlayer == 2)
                            cost -= 10;
                        else if (horCountPlayer == 3)
                            cost -= 100;
                        else if (horCountPlayer >= 4)
                            cost -= minimum;

                        horCountPlayer = 0;    // reset counter
                    }

                    // compute cost of the player's vertical line
                    if (verCountPlayer > 0 && !boardLayout[k+1][i].equalsIgnoreCase(playerMark))
                    {
                        if (verCountPlayer == 1)
                            cost -= 1;
                        else if (verCountPlayer == 2)
                            cost -= 10;
                        else if (verCountPlayer == 3)
                            cost -= 100;
                        else if (verCountPlayer >= 4)
                            cost -= minimum;

                        verCountPlayer = 0;
                    }
                }
            }
        }
        return cost;
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