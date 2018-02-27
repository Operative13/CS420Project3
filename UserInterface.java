package project3;

import java.util.*;

public class UserInterface
{
   public void error()
   {
      System.out.println("ERROR DETECTED");
   }
   public int maximumTime(int userInput)
   {
      if(userInput < 1 || userInput > 30)
      {
         error();
         System.out.println("Input must be between 1 and 30.");
      }
      
      return userInput;
   }
   
   public void execute()
   {
      boolean invalidInput = false;
      int maximumTime = 0;
      
      Scanner keyboard = new Scanner(System.in);
      
      while (!invalidInput)
      {
         System.out.println("Enter maximum time to generate moves (1-30): ");
         int userInput = 0;
         try
         {
            userInput = keyboard.nextInt();
            maximumTime = maximumTime(userInput);
            invalidInput = false;
         }
         catch(InputMismatchException e)
         {
            keyboard.nextLine();
            error();
         }
      }
      
      
      
   }
}
