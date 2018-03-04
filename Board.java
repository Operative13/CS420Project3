package project3;

import java.lang.Thread;
import java.lang.InterruptedException;
import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable
{
   private String[][] board;

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
      System.out.println();
   }

   // print the log of moves made
   public void printMovesLog(boolean playerFirst, ArrayList<String> playerMovesLog, ArrayList<String> aiMovesLog)
   {
       if (playerFirst)
       {
           System.out.println("Player vs. Opponent");
           for (int i = 0; i < playerMovesLog.size(); i++)
           {
               System.out.print((i+1) + ". " + playerMovesLog.get(i) + " ");
               if (i < aiMovesLog.size())
                   System.out.print(aiMovesLog.get(i));

               System.out.println();
           }
       }

       else
       {
           System.out.println("Opponent vs. Player");
           for (int i = 0; i < aiMovesLog.size(); i++)
           {
               System.out.print((i+1) + ". " + aiMovesLog.get(i) + " ");
               if (i < playerMovesLog.size())
                    System.out.print(playerMovesLog.get(i));

               System.out.println();
           }
       }

       System.out.println();
   }

   // place a marker depending on the current player at the given position on the board
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

   // return the current board layout as a String
   public String[][] getBoard(){return board;}
   
   public void setBoard(int row, int col, String marker) // NEWCODE
   {
      board[row][col] = marker;
   }

   // pause the program for the given amount of time in seconds
   public void pause(int waitTime)
   {
       try
       {
           Thread.sleep(waitTime * 1000);
       }
       catch (InterruptedException e)
       {
           e.printStackTrace();
       }
   }
}