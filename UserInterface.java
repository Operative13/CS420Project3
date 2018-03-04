//package project3;

import java.io.*;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInterface implements Serializable
{
    private ArrayList<String> playerMovesLog;
    private Board board;
    private boolean playerFirst, savedFile, loadedFile;
    private double maxTime;
    private int[] playerMoves;
    private transient Scanner keyboard;         // transient means do not store this object when saving this class as an object
    Minimax ai;

    public UserInterface()
    {
        board = new Board();
        keyboard = new Scanner(System.in);
        loadedFile = false;
        playerFirst = false;
        playerMoves = new int[64];
        playerMovesLog = new ArrayList<String>(32); // 32 = board size / 2 = 8*8 / 2 = 64 / 2 = 32
        savedFile = false;
        maxTime = 5.0;
    }

    public void error()
    {
       System.out.print("ERROR DETECTED");
    }

    // Asks the user for the max time and order of play
    // Executes the game with user-defined conditions
    public void mainMenu()
    {
       // if the player does not load a previous file, ask them for ai's maxTime and who goes first
       if(!askLoad())
       {
           maxTime = askMaxTime();
           playerFirst = askOrder();
       }

       playGame();
    }

    public boolean askLoad()
    {
        String choice = "";
        while (true)
        {
           System.out.println("Load a previously saved game? y/n");
           choice = keyboard.nextLine();

           if (choice.equalsIgnoreCase("y"))
           {
               if (loadObjects())
                   return true;
           }
           else if (choice.equalsIgnoreCase("n"))
               return false;
           else
               System.out.println("Invalid choice! Please enter either 'y' for yes or 'n' for no.");
        }
    }

    public boolean loadObjects()
    {
        System.out.print("Enter the name of the file to load: ");
        String filename = keyboard.nextLine();
        System.out.println();

        // try to load the given file
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            UserInterface ui = (UserInterface)ois.readObject(); // load UserInterface object from file

            // initialize current UserInterface with properties from loaded object
            playerMovesLog = ui.playerMovesLog;
            board = ui.board;
            playerFirst = ui.playerFirst;
            savedFile = ui.savedFile;
            maxTime = ui.maxTime;
            keyboard = new Scanner(System.in);
            ai = ui.ai;
            loadedFile = true;
            ois.close();

            System.out.println("Successfully loaded file!");
            board.printBoard();
            board.printMovesLog(playerFirst, playerMovesLog, ai.getMovesLog());
            return true;
        }
        catch (IOException e)
        {
            System.out.println("ERROR! Could not load file " + filename);
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("ERROR! Board class not found. File NOT loaded.");
        }

        return false;
    }

    public void saveObjects()
    {
        System.out.print("Enter the name of the file to save: ");
        String filename = keyboard.nextLine();
        System.out.println();

        // try to save the file
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
            oos.writeObject(this);  // save this (UserInterface) object to a file
            oos.close();
            System.out.println("Successfully saved file: " + filename);
        }
        catch (IOException e)
        {
            System.out.println("ERROR! Could not save the file " + filename);
            e.printStackTrace();
        }
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

    // ask the player to enter a valid move
    // and return the it in integer form if valid
    public int[] askUserMove()
    {
        boolean validMove = false;
       while (true)
       {
          System.out.print("Enter your move: ");
          String move = keyboard.nextLine();
          System.out.println();

          validMove = validateUserMove(move);
          if (validMove)
          {
              playerMovesLog.add(move);
              return convertStringMove(move);
          }

          // if user has just saved the game, ask again for their move
          else if (savedFile)
              savedFile = false;
       }
    }

    // check if the player entered a valid moves
    // return true if they did, false otherwise
    public boolean validateUserMove(String move)
    {

       if (move.equalsIgnoreCase("quit"))
          System.exit(0);

       if (move.equalsIgnoreCase("save"))
       {
          saveObjects();
          savedFile = true;;
          return false;
      }

      try
      {
       // return false if move is not 2 characters long (i.e: a5)
       if (move.length() != 2)
           throw new IllegalArgumentException("Invalid move!");

       int[] moveIndex = convertStringMove(move);

       // check for valid indexes
       int row = moveIndex[0], col = moveIndex[1];
       if ( (row < 1 || row > 8) || (col < 1 || col > 8) )
           throw new IllegalArgumentException("ERROR! That move is out of range!");

       // check if a marker is already at the move's position
       if (!board.getBoard()[row][col].equals("_"))
            throw new IllegalArgumentException("ERROR! That spot is already taken!");
      }
      catch(IllegalArgumentException e)
      {
          System.out.println(e.getMessage());
          return false;
      }
      return true;
    }

    // convert the player's move to an integer array of size two
    // the first index of the array will contain the row and second the column of the player's move
    // return this integer array
    public int[] convertStringMove(String move)
    {
        int row = -1;   // set as an invalid position by default
        move = move.toUpperCase();

        // determine correct row position
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
        }
        int col = Character.getNumericValue(move.charAt(1));

        return new int[]{row, col};
    }

    // checks if there is a valid win
    public boolean checkWin(Board board, String marker)
    {
       if(board.horizontalWin(marker) == true
             || board.verticalWin(marker) == true)
       {
          return true;
       }

       return false;
    }

    public void playGame()
    {
       String playerMark, aiMark;
       boolean wonGame = false;
       boolean playerWin = false;

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

       int[] playerMove = new int[2];

       // initialize ai object if one was not loaded by the user
       if (!loadedFile)
       {
            ai = new Minimax(board, maxTime, aiMark, playerMark, playerMoves);
            loadedFile = false;     // reset boolean
       }

       while (!board.isFull() || !wonGame)
       {
          if (playerFirst)
          {
             // user makes move
             playerMove = askUserMove();
             playerMoves[playerMove[0]] = playerMove[1];
             board.markMove(playerMove[0], playerMove[1], playerMark);
             board.printBoard();
             board.printMovesLog(playerFirst, playerMovesLog, ai.getMovesLog());
             wonGame = checkWin(board, playerMark);

             if(wonGame)
             {
                playerWin = true;
                break;
             }

             // ai makes move
             ai.makeMove();
            //ai.testMove();   // DELETE AFTER TESTING
             board.printBoard();
             board.printMovesLog(playerFirst, playerMovesLog, ai.getMovesLog());
             wonGame = checkWin(board, aiMark);

             if(wonGame)
                break;
          }
          else
          {
             // ai makes move
             ai.makeMove();
             board.printBoard();
             board.printMovesLog(playerFirst, playerMovesLog, ai.getMovesLog());
             wonGame = checkWin(board, aiMark);

             if(wonGame)
                break;

             // user makes move
             playerMove = askUserMove();
             playerMoves[playerMove[0]] = playerMove[1];
             board.markMove(playerMove[0], playerMove[1], playerMark);
             board.printBoard();
             board.printMovesLog(playerFirst, playerMovesLog, ai.getMovesLog());
             wonGame = checkWin(board, playerMark);

             if(wonGame)
             {
                playerWin = true;
                break;
             }
          }
       }

       if(!wonGame)
          System.out.println("Draw");

       else if(playerWin)
          System.out.println("You Win");

       else
          System.out.println("You Lose");
    }
}
