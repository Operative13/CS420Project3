//package project3;

import java.io.Serializable;
import java.util.*;   // DELETE AFTER TESTING

public class Minimax implements Serializable
{
   private ArrayList<String> aiMovesLog;
   private double maxTime;
   private Board board;
   private String marker;

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

           board.markMove(row, col, marker);
           aiMovesLog.add(convertMoveToString(row, col));
           break;
       }
   }

   public Minimax(double maxTime, String marker, Board board)
   {
      this.maxTime = maxTime;
      this.board = board;
      this.marker = marker;
      aiMovesLog = new ArrayList<String>(32); // 32 = board size / 2 = 8*8 / 2 = 64 / 2 = 32
   }

   public void makeMove()
   {
       // disable until come for the required methods are completed
       //
       // int[] moveIndex = findMove();
       // aiMovesLog.add(convertMoveToString(moveIndex[0], moveIndex[1]));

   }

   // minimax algorithm
   public int[] findMove()
   {

       return null;
   }

   public int[] heuristic()
   {

       return null;
   }

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
