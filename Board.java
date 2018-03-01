package project3;

public class Board
{
   private String[][] board;

   /*
   public static void main(String[] args)
   {
      new Board();
   }
   */

   public Board()
   {
      board = new String[9][9];
      resetBoard();
   }

   // make a blank board
   public void resetBoard()
   {
      for (int i = 0, ch = 64; i < board.length; i++, ch++)
      {
         for (int k = 0; k < board[0].length; k++)
         {
            // make row for numbers label
            if (i == 0)
            {
               // formatting: make the first space a blank space
               if (k == 0)
                  board[i][k] = " ";
               else
                  board[i][k] = k + "";
               }
            else
            {
               // make column for letters label
               if (k == 0)
                  board[i][k] = (char)ch + " ";
               else
                  board[i][k] = "_";
            }
         }
      }
   }

   // print the current board layout
   public void printBoard()
   {
      for (String[] row : board)
      {
         for (String col : row)
            System.out.printf("%-3s ", col);
         System.out.println();
      }
   }

   // place a marker depending on the player at the given position on the board
   // intended use for the user
   public void markMove(String move, String marker)
   {
      int col = Character.getNumericValue(move.charAt(1));;
      int row = -1;

      move = move.toUpperCase();
      switch (move.charAt(0))
      {
         case 'A':
            row = 1;
            break;
         case 'B':
            row = 2;
            break;
         case 'C':
            row = 3;
            break;
         case 'D':
            row = 4;
            break;
         case 'E':
            row = 5;
            break;
         case 'F':
            row = 6;
            break;
         case 'G':
            row = 7;
            break;
         case 'H':
            row = 8;
            break;
         default: // If nothing matches, break
            break;
      }
      
      // Used to debug invalid moves. Remove later
      try
      {
         if((row > 0 && row < 9) && (col > 0 && col < 9))
         {
            board[row][col] = marker;
         }
         else
         {
            throw new IllegalArgumentException("Invalid move detected! Please enter a valid coordinate.");
         }
      }
      catch(IllegalArgumentException e)
      {
          System.out.println(e.getMessage());
      }
   }

   // place a marker depending on the player at the given position on the board
   // intended use for the program
   public void markMove(int row, int col, String marker){board[row][col] = marker;}
   
   // Checks for horizontal win on board
   public boolean horizontalWin(String marker)
   {
      int counter = 0;
      for(int i = 1; i < 9; i++)
      {
         for(int j = 1; j < 9; j++)
         {
            if(board[i][j].equalsIgnoreCase(marker))
            {
               counter++; // Count if marker is there
            }
            else
            {
               counter = 0; // reset counter if marker not detected
            }
            
            if(counter >= 4) // If counter is greater than or equal to 4, winner
            {
               return true;
            }
            System.out.print(counter);
         }
      }
      return false;
   }
   
   // Checks for vertical win on board
   public boolean verticalWin(String marker)
   {
      int counter = 0;
      for(int i = 1; i < 9; i++)
      {
         for(int j = 1; j < 9; j++)
         {
            if(board[j][i].equalsIgnoreCase(marker))
            {
               counter++;
            }
            else
            {
               counter = 0;
            }
            
            if(counter >= 4) // If counter is greater than or equal to 4, winner
            {
               return true;
            }
            //System.out.println(counter);
         }
      }
      return false;
   }
   
   // return true if all spaces on the board are full, false otherwise
   public boolean isFull()
   {
      for (String[] row : board)
         for (String value : row)
            if (value.equals("_"))
               return false;
      return true;
   }
}