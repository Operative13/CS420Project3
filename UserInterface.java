package project3;

import java.lang.IllegalArgumentException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInterface
{
    private Scanner keyboard;

    public UserInterface()
    {
         keyboard = new Scanner(System.in);
    }

    public void error()
    {
       System.out.println("ERROR DETECTED");
    }
   
    // Asks the user for the max time and order of play
    // Executes the game with user-defined conditions
    public void mainMenu()
    {
       double maxTime = askMaxTime();
       boolean playerFirst = askOrder();
       playGame(maxTime, playerFirst);
    }

    // ask the user to the enter maximum amount of time the program will search for a solution
    // return the maximum time allowed
    public double askMaxTime()
    {
       double maxTime = 0.0;

       while (true)
       {
          System.out.println("Enter maximum time to generate moves (1-30): ");
          try
          {
             maxTime = keyboard.nextDouble();
             keyboard.nextLine();    // catch last line if valid input is entered
             if(maxTime < 1 || maxTime > 30)
                throw new IllegalArgumentException("Input must be between 1 and 30.");

             return maxTime;
          }
          catch(IllegalArgumentException e)
          {
             System.out.println(e.getMessage());
             continue;
          }
          catch(InputMismatchException e)
          {
             keyboard.nextLine();
             error();
          }
       }
    }

    // ask the user if they will start first
    // return true if user will go first, false otherwise
    public boolean askOrder()
    {
       String choice = "";

       while (true)
       {
          try
          {
             System.out.println("Do you want to start first? y/n");
             choice = keyboard.nextLine();
             if (!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n"))
                    throw new IllegalArgumentException("Invalid choice! Please enter either 'y' for yes or 'n' for no.");
               break;
           }
           catch(IllegalArgumentException e)
           {
               System.out.println(e.getMessage());
           }
       }
       if (choice.equalsIgnoreCase("y"))
          return true;
       else
          return false;
    }

    public String askUserMove()
    {
       while (true)
       {
          System.out.print("Enter your move: ");
          System.out.println();
          String move = keyboard.nextLine();

          if (!validateUserMove(move))
             System.out.println("Invalid move! Try again.");
          else
             return move;
       }
    }

    public boolean validateUserMove(String move)
    {
       if (move.equalsIgnoreCase("quit"))
          System.exit(0);

       // return false if move is not 2 characters long (i.e: a5)
       if (move.length() != 2)
          return false;

       // check for a valid letter
       move = move.toUpperCase();
       char ch = move.charAt(0);
       if (ch < 65 || ch > 72)  // A = 65, H = 72
          return false;

       // check for valid index
       int index = Character.getNumericValue(move.charAt(1));
       if (index < 1 || index > 8)
          return false;

       return true;
    }
    
    // checks if there is a valid win
    public boolean checkWin(Board board, String marker)
    {
       if(board.horizontalWin(marker) == true 
             || board.verticalWin(marker) == true)
       {
          System.out.println("HIT");
          return true;
       }
       
       return false;
    }

    public void playGame(double maxTime, boolean playerFirst)
    {
       Board board = new Board();
       String playerMark, aiMark;
       boolean wonGame = false;

       if (playerFirst)
       {
          playerMark = "X";
          aiMark = "O";
       }
       else
       {
          playerMark = "O";
          aiMark = "X";
       }

       Minimax ai = new Minimax(maxTime, aiMark, board);

       while (!board.isFull() || !wonGame)
       {
          if (playerFirst)
          {
             board.markMove(askUserMove(), playerMark);
             wonGame = checkWin(board, playerMark);
             
             ai.makeMove();
             wonGame = checkWin(board, aiMark);
          }
          else
          {
             ai.makeMove();
             wonGame = checkWin(board, aiMark);
             
             board.markMove(askUserMove(), playerMark);
             wonGame = checkWin(board, playerMark);
          }
          
          //System.out.println(wonGame);
          board.printBoard();
       }
    }
}