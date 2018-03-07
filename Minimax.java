import java.io.Serializable;
import java.util.ArrayList;

public class Minimax implements Serializable
{
    private ArrayList<String> aiMovesLog;
    private Board board;
    private boolean endEarly;
    private double elapsedTime, maxTime;
    private String aiMark, playerMark;

   public Minimax(Board board, double maxTime, String aiMarker, String playerMarker, int[] playerMoves)
   {
      this.maxTime = maxTime;
      this.board = board;

      aiMark = aiMarker;
      aiMovesLog = new ArrayList<String>(32); // 32 = board size / 2 = 8*8 / 2 = 64 / 2 = 32

      elapsedTime = 0.0;
      endEarly = false;

      playerMark = playerMarker;
   }

   public void makeMove(int row)
   {
      int[] moveIndex;

      if(row < 5)
         moveIndex = getMove();
      else
         moveIndex = getOtherMove();

       aiMovesLog.add(convertMoveToString(moveIndex[0], moveIndex[1]));
       board.markMove(moveIndex[0], moveIndex[1], aiMark);
   }

   // return an integer array containing the position of the best move to make
   public int[] getMove()
   {
       int bestValue = Integer.MIN_VALUE;
       int currentValue;
       int[] bestMove = new int[2];
       String[][] boardLayout = board.getBoard();
       String[][] previousBoard;

       long start = System.nanoTime();
       elapsedTime = 0.0;
       endEarly = false;

        // explore every empty space and calculate the cost of that move
        for (int row = 1; row < 9; row++)
        {
            for (int col = 1; col < 9 && elapsedTime < maxTime; col++)
            {
                if (boardLayout[row][col].equals("_"))
                {
                    // store the current board  layout in case minimax breaks early
                    previousBoard = boardLayout;

                    // make a possible move
                    boardLayout[row][col] = aiMark;

                    // determine this moves cost
                    currentValue = minimax(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE, start);

                    // restore the previous board layout
                    boardLayout[row][col] = "_";

                    if (endEarly)
                    {
                        boardLayout = previousBoard;
                        break;
                    }

                    // save the best cost move
                    else if (currentValue > bestValue)
                    {
                        bestValue = currentValue;
                        bestMove[0] = row;
                        bestMove[1] = col;
                    }
                }
                elapsedTime = ((double) System.nanoTime() - start) / 1000000000.0;
            }
        }
       return bestMove;
   }

   public int[] getOtherMove()
   {
      int bestValue = Integer.MIN_VALUE;
      int currentValue;
      int[] bestMove = new int[2];
      String[][] boardLayout = board.getBoard();
      String[][] previousBoard;

      long start = System.nanoTime();
      elapsedTime = 0.0;
      endEarly = false;

       // explore every empty space and calculate the cost of that move
       for (int row = 8; row > 0; row--)
       {
           for (int col = 8; col > 0 && elapsedTime < maxTime; col--)
           {
               if (boardLayout[row][col].equals("_"))
               {
                   // store the current board  layout in case minimax breaks early
                   previousBoard = boardLayout;

                   // make a possible move
                   boardLayout[row][col] = aiMark;

                   // determine this moves cost
                   currentValue = minimaxOther(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE, start);

                   // restore the previous board layout
                   boardLayout[row][col] = "_";

                   if (endEarly)
                   {
                       boardLayout = previousBoard;
                       break;
                   }

                   // save the best cost move
                   else if (currentValue > bestValue)
                   {
                       bestValue = currentValue;
                       bestMove[0] = row;
                       bestMove[1] = col;
                   }
               }
               elapsedTime = ((double) System.nanoTime() - start) / 1000000000.0;
           }
       }
      return bestMove;
  }

   // use the minimax algorithm with alpha beta pruning to determine the best value
   // of the current board playout depending on the current player (max or min)
   // alpha = best (highest value) found for MAX so far
   // beta = best (lowest value) found for MIN so far
    public int minimax(int depth, boolean isMax, int alpha, int beta, long start)
    {
        elapsedTime = ((double) System.nanoTime() - start) / 1000000000.0;
        if (elapsedTime > maxTime)
            endEarly = true;

        // cut off point for search
        if (depth == 3)
            return getCost(start);

        int bestCost, currentCost;

        // maximizer
        // determine the best cost as the largest cost of the visited states
        if (isMax)
        {
            bestCost = Integer.MIN_VALUE; // default to lowest possible value

            for (int row = 1; row < 9; row++)
            {
                for (int col = 1; col < 9; col++)
                {
                    if (board.getBoard()[row][col].equals("_"))
                    {
                        board.getBoard()[row][col] = aiMark;

                        currentCost = minimax(depth + 1, false, alpha, beta, start);
                        bestCost = max(bestCost, currentCost);
                        alpha = max(alpha, bestCost);

                        // reset board to the previous (original) state
                        board.getBoard()[row][col] = "_";

                        // prune
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
            bestCost = Integer.MAX_VALUE; // default to largest possible value

            for (int row = 1; row < 9; row++)
            {
                for (int col = 1; col < 9; col++)
                {
                    if (board.getBoard()[row][col].equals("_"))
                    {
                        board.getBoard()[row][col] = playerMark;

                        currentCost = minimax(depth + 1, true, alpha, beta, start);
                        bestCost = min(bestCost, currentCost);
                        beta = min(beta, bestCost);

                        // reset board to the previous (original) state
                        board.getBoard()[row][col] = "_";

                        // prune
                        if (beta <= alpha)
                            break;
                    }

                }
            }
        }
        return bestCost;
    }

    public int minimaxOther(int depth, boolean isMax, int alpha, int beta, long start)
    {
        elapsedTime = ((double) System.nanoTime() - start) / 1000000000.0;
        if (elapsedTime > maxTime)
            endEarly = true;

        // cut off point for search
        // depth should be an even number
        if (depth == 3)
            return getCostOther(start);

        int bestCost, currentCost;

        // maximizer
        // determine the best cost as the largest cost of the visited states
        if (isMax)
        {
            bestCost = Integer.MIN_VALUE; // default to lowest possible value

            for (int row = 8; row > 0; row--)
            {
                for (int col = 8; col > 0; col--)
                {
                    if (board.getBoard()[row][col].equals("_"))
                    {
                        board.getBoard()[row][col] = aiMark;

                        currentCost = minimaxOther(depth + 1, false, alpha, beta, start);
                        bestCost = max(bestCost, currentCost);
                        alpha = max(alpha, bestCost);

                        // reset board to the previous (original) state
                        board.getBoard()[row][col] = "_";

                        // prune
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
            bestCost = Integer.MAX_VALUE; // default to largest possible value

            for (int row = 8; row > 0; row--)
            {
                for (int col = 8; col > 0; col--)
                {
                    if (board.getBoard()[row][col].equals("_"))
                    {
                        board.getBoard()[row][col] = playerMark;

                        currentCost = minimaxOther(depth + 1, true, alpha, beta, start);
                        bestCost = min(bestCost, currentCost);
                        beta = min(beta, bestCost);

                        // reset board to the previous (original) state
                        board.getBoard()[row][col] = "_";

                        // prune
                        if (beta <= alpha)
                            break;
                    }

                }
            }
        }
        return bestCost;
    }

    // heuristic
    // calculate the cost of the current state
    public int getCost(long start)
    {

       int maximum = Integer.MAX_VALUE;
       int minimum = Integer.MIN_VALUE;

        String[] rowCol = new String[2];
        int cost = 0;

        // evaluate the board assuming AI = O, Player = X
        for (int i = 1; i < 9; i++)
        {
           elapsedTime = ((double) System.nanoTime() - start) / 1000000000.0;
           if (elapsedTime > maxTime)
           {
               endEarly = true;
               break;
           }

           rowCol = board.getRowCol(i, i);
           String row = rowCol[0];
           String col = rowCol[1];

           // check win or lose states
           if (row.contains("XXXX") || col.contains("XXXX"))
               return minimum;

           if (row.contains("OOOO") || col.contains("OOOO"))
               return maximum;

           // check for player killer moves in row
           if (row.contains("_XXX_"))
               cost -= 10000;

           else if (row.contains("XXX_") || row.contains("_XXX") || row.contains("XX_X") || row.contains("X_XX"))
               cost -= 1000;
           else if (row.contains("XXX"))
              cost -= 100;

           // check for player killer moves in column
           if (col.contains("_XXX_"))
               cost -= 10000;

           else if (col.contains("XXX_") || col.contains("_XXX") || col.contains("XX_X") || col.contains("X_XX"))
               cost -= 1000;
           else if (col.contains("XXX"))
              cost -= 100;

           // check for ai killer moves in row
           if (row.contains("_OOO_"))
               cost += 10000;

           else if (row.contains("OOO_") || row.contains("_OOO") || row.contains("OO_O") || row.contains("O_OO"))
               cost += 1000;
           else if (row.contains("OOO"))
              cost += 100;

           // check for ai killer moves in column
           if (col.contains("_OOO_"))
               cost += 10000;

           else if (col.contains("OOO_") || col.contains("_OOO") || col.contains("OO_O") || col.contains("O_OO"))
               cost += 1000;
           else if (col.contains("OOO"))
              cost += 100;

           if (row.contains("_XX_"))
               cost -= 50;
           else if (row.contains("_XX") || row.contains("XX_") || row.contains("X_X"))
                 cost -= 25;
           else if (row.contains("XX"))
               cost -= 10;

           if (col.contains("_XX_"))
               cost -= 50;
           else if (col.contains("_XX") || col.contains("XX_") || col.contains("X_X"))
              cost -= 25;
           else if (col.contains("XX"))
               cost -= 10;

           if (row.contains("_OO_"))
               cost += 50;
           else if (row.contains("_OO") || row.contains("OO_") || row.contains("O_O"))
              cost += 25;
           else if (row.contains("OO"))
               cost += 10;

           if (col.contains("_OO_"))
               cost += 50;
           else if (col.contains("_OO") || col.contains("OO_") || col.contains("O_O"))
              cost += 25;
           else if (col.contains("OO"))
               cost += 10;

           if (row.contains("_X_"))
              cost -= 5;
           else if (row.contains("X_") || row.contains("_X"))
              cost -= 2;
           else if (row.contains("X"))
               cost -= 1;

           if (col.contains("_X_"))
               cost -= 5;
           else if (col.contains("X_") || col.contains("_X"))
              cost -= 2;
           else if (col.contains("X"))
               cost -= 1;

           if (row.contains("_O_"))
              cost += 5;
           else if (row.contains("O_") || row.contains("_O"))
              cost += 2;
           else if (row.contains("O"))
              cost += 1;

           if (col.contains("_O_"))
              cost += 5;
           else if (col.contains("O_") || col.contains("_O"))
              cost += 2;
           else if (col.contains("O"))
              cost += 1;

        }
        // the above code assumes that the ai is O
        // change the sign of cost if ai is X
        if (!aiMark.equals("O"))
            return -cost;
        else
            return cost;
    }

    public int getCostOther(long start)
    {

        int maximum = Integer.MAX_VALUE;
        int minimum = Integer.MIN_VALUE;

        String[] rowCol = new String[2];
        int cost = 0;

        // evaluate the board assuming AI = O, Player = X
        for (int i = 8; i > 0; i--)
        {
            elapsedTime = ((double) System.nanoTime() - start) / 1000000000.0;
            if (elapsedTime > maxTime)
            {
                endEarly = true;
                break;
            }

            rowCol = board.getRowCol(i, i);
            String row = rowCol[0];
            String col = rowCol[1];

            // check win or lose states
            if (row.contains("XXXX") || col.contains("XXXX"))
                return minimum;

            if (row.contains("OOOO") || col.contains("OOOO"))
                return maximum;

            // check for player killer moves in row
            if (row.contains("_XXX_"))
                cost -= 10000;
            else if (row.contains("XXX_") || row.contains("_XXX") || row.contains("XX_X") || row.contains("X_XX"))
                cost -= 1000;
            else if (row.contains("XXX"))
               cost -= 100;

            // check for player killer moves in column
            if (col.contains("_XXX_"))
                cost -= 10000;
            else if (col.contains("XXX_") || col.contains("_XXX") || col.contains("XX_X") || col.contains("X_XX"))
                cost -= 1000;
            else if (col.contains("XXX"))
               cost -= 100;

            // check for ai killer moves in row
            if (row.contains("_OOO_"))
                cost += 10000;
            else if (row.contains("OOO_") || row.contains("_OOO") || row.contains("OO_O") || row.contains("O_OO"))
                cost += 1000;
            else if (row.contains("OOO"))
               cost += 100;

            // check for ai killer moves in column
            if (col.contains("_OOO_"))
                cost += 10000;
            else if (col.contains("OOO_") || col.contains("_OOO") || col.contains("OO_O") || col.contains("O_OO"))
                cost += 1000;
            else if (col.contains("OOO"))
               cost += 100;

            if (row.contains("_XX_"))
                cost -= 50;
            else if (row.contains("_XX") || row.contains("XX_") || row.contains("X_X"))
                  cost -= 25;
            else if (row.contains("XX"))
                cost -= 10;

            if (col.contains("_XX_"))
                cost -= 50;
            else if (col.contains("_XX") || col.contains("XX_") || col.contains("X_X"))
               cost -= 25;
            else if (col.contains("XX"))
                cost -= 10;

            if (row.contains("_OO_"))
                cost += 50;
            else if (row.contains("_OO") || row.contains("OO_") || row.contains("O_O"))
               cost += 25;
            else if (row.contains("OO"))
                cost += 10;

            if (col.contains("_OO_"))
                cost += 50;
            else if (col.contains("_OO") || col.contains("OO_") || col.contains("O_O"))
               cost += 25;
            else if (col.contains("OO"))
                cost += 10;

            if (row.contains("_X_"))
               cost -= 5;
            else if (row.contains("X_") || row.contains("_X"))
               cost -= 2;
            else if (row.contains("X"))
                cost -= 1;

            if (col.contains("_X_"))
                cost -= 5;
            else if (col.contains("X_") || col.contains("_X"))
               cost -= 2;
            else if (col.contains("X"))
                cost -= 1;

            if (row.contains("_O_"))
               cost += 5;
            else if (row.contains("O_") || row.contains("_O"))
               cost += 2;
            else if (row.contains("O"))
               cost += 1;

            if (col.contains("_O_"))
               cost += 5;
            else if (col.contains("O_") || col.contains("_O"))
               cost += 2;
            else if (col.contains("O"))
               cost += 1;

        }
        // the above code assumes that the ai is O
        // change the sign of cost if ai is X
        if (!aiMark.equals("O"))
            return -cost;
        else
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
