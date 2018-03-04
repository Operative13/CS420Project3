package project3;

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
      
      //int[] moveIndex = getMove();
      //aiMovesLog.add(convertMoveToString(moveIndex[0], moveIndex[1]));
      int[] result = minimax(0, playerMark); // depth, max turn
      //return new int[] {result[1], result[2]};   // row, col
      aiMovesLog.add(convertMoveToString(result[1], result[2]));

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
      int marker = 0;
      if(!isMax)
      {
         marker = 1;
      }
        // cut off point for search, THIS SHOULD AN BE OPTIMIZED FOR COMPETITION
        if (depth == 1)
        {
            //  RETURN THE COST OF THIS STATE
            return getCost(marker);
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
   public int getCost(int marker) // NEWCODE
   {
      if(marker % 2 == 0)
      {
         return costCalculator(board.getBoard(), playerMark);
      }
      else
      {
         return costCalculator(board.getBoard(), aiMark);
      }
   }
   
   public int getCost(String marker)
   {
      if(marker == playerMark)
      {
         return costCalculator(board.getBoard(), playerMark);
      }
      else
      {
         return costCalculator(board.getBoard(), aiMark);
      }
   }
   
   public int costCalculator(String[][] board, String marker)
   {
      
      int cost = 0;

      int counter = 0;
      for(int i = 1; i < 9; i++)
      {
         for(int j = 1; j < 9; j++)
         {
            if(board[i][j].equalsIgnoreCase(marker))
            {
               counter++; // Count if marker is there
               if(counter == 1)
               {
                  cost += 1;
               }
               else if (counter == 2)
               {
                  cost += 10;
               }
               else if (counter == 3)
               {
                  cost += 100;
               }
               else if (counter >= 4)
               {
                  cost = 1000000;
                  return cost;
               }
            }
            else
            {
               if(!board[i][j].equalsIgnoreCase("_"))
               {
                  cost -= 1;
               }
               counter = 0; // reset counter if marker not detected
            }
         }
      }
      
      counter = 0;
      for(int i = 1; i < 9; i++)
      {
         for(int j = 1; j < 9; j++)
         {
            if(board[j][i].equalsIgnoreCase(marker))
            {
               counter++; // Count if marker is there
               if(counter == 1)
               {
                  cost += 1;
               }
               else if (counter == 2)
               {
                  cost += 10;
               }
               else if (counter == 3)
               {
                  cost += 100;
               }
               else if (counter >= 4)
               {
                  cost = 1000000;
                  return cost;
               }
            }
            else
            {
               if(!board[j][i].equalsIgnoreCase("_"))
               {
                  cost -= 1;
               }
               counter = 0; // reset counter if marker not detected
            }
         }
      }
      
      return cost;
      
   }

   public int[] minimax(int depth, String player) // NEWCODE
   {
      // Generate possible next moves in a List of int[2] of {row, col}.
      List<int[]> nextMoves = generateMoves();
 
      // mySeed is maximizing; while oppSeed is minimizing
      int bestScore = (player.equals(playerMark)) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
      int currentScore;
      int bestRow = -1;
      int bestCol = -1;
 
      if (nextMoves.isEmpty() || depth == 6)
      {
         // Gameover or depth reached, evaluate score
         bestScore = getCost(player);
      }
      else
      {
         for (int[] move : nextMoves)
         {
            // Try this move for the current "player"
            board.setBoard(move[0], move[1], player);
            //board.getBoard()[move[0]][move[1]].content = player;
            if (player == playerMark) {  // mySeed (computer) is maximizing player
               currentScore = minimax(depth + 1, aiMark)[0];
               if (currentScore > bestScore) {
                  bestScore = currentScore;
                  bestRow = move[0];
                  bestCol = move[1];
               }
            } else {  // oppSeed is minimizing player
               currentScore = minimax(depth + 1, playerMark)[0];
               if (currentScore < bestScore) {
                  bestScore = currentScore;
                  bestRow = move[0];
                  bestCol = move[1];
               }
            }
            // Undo move
            board.setBoard(move[0], move[1], "_");
            //cells[move[0]][move[1]].content = Seed.EMPTY;
         }
      }
      return new int[] {bestScore, bestRow, bestCol};
   }
   
   // Generates the list of all possible moves for a player // NEWCODE
   public List<int[]> generateMoves()
   {
      List<int[]> nextMoves = new ArrayList<int[]>(); // allocate List
 
      // If gameover, i.e., no next move
      if(board.isFull() || checkWin(aiMark) || checkWin(playerMark))
      {
         return nextMoves;
      }
      
      for (int row = 1; row < 9; row++)
      {
          for (int col = 1; col < 9; col++)
          {
              if (board.getBoard()[row][col].equals("_"))
              {
                 nextMoves.add(new int[] {row, col});
              }
          }
      }
      return nextMoves;
   }
   
   // Checks if the given player has won the game
   public boolean checkWin(String marker)
   {
      if(board.horizontalWin(marker) == true
            || board.verticalWin(marker) == true)
      {
         return true;
      }

      return false;
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