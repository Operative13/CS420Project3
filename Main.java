package project3;

public class Main
{
   public static void main (String args[])
   {
      // Initializes the User Interface and executes the Main Menu
      UserInterface ui = new UserInterface();
      ui.mainMenu();
   }
}

// Player First
/*
 *    
    1   2   3   4   5   6   7   8   
A   X   _   _   _   _   _   _   _   
B   X   X   O   _   X   _   _   _   
C   O   O   O   X   X   X   O   _   
D   _   X   X   O   O   O   O   _   
E   _   O   _   _   _   _   _   _   
F   _   O   X   _   _   _   _   _   
G   _   O   O   X   _   X   _   _   
H   _   X   _   _   _   _   _   _   

Player vs. Opponent
1. a1 c3
2. b1 c1
3. b2 c2
4. c4 b3
5. d3 d4
6. f3 g3
7. c6 g2
8. c5 c7
9. b5 d5
10. g4 f2
11. g6 e2
12. d2 d6
13. h2 d7

You Lose
 */

// AI First
/*
    1   2   3   4   5   6   7   8   
A   _   _   _   _   _   _   _   _   
B   _   X   X   O   O   O   O   _   
C   _   O   X   X   X   O   O   _   
D   _   X   O   _   _   X   X   _   
E   _   _   X   _   O   X   O   _   
F   _   _   O   _   X   X   O   _   
G   _   _   _   _   _   _   X   _   
H   _   _   _   _   _   _   O   _   

Opponent vs. Player
1. b2 h7
2. d2 c2
3. b3 b4
4. c3 d3
5. c4 f3
6. e3 e5
7. f5 b5
8. c5 c6
9. f6 f7
10. e6 e7
11. g7 c7
12. d7 b7
13. d6 b6

You Win
 */
