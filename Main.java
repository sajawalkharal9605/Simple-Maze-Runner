import java.util.Scanner;

public class Main {

    static char[][] maze;
    static int playerX, playerY;
    static int steps;
    static int score;
    static int highScore = 0;
    static long startTime;

    static Scanner sc = new Scanner(System.in);

    // Simple color codes for terminal
    static final String RESET = "\u001B[0m";
    static final String BLUE = "\u001B[34m";
    static final String GREEN = "\u001B[32m";
    static final String RED = "\u001B[31m";
    static final String YELLOW = "\u001B[33m";
    static final String CYAN = "\u001B[36m";

    public static void main(String[] args) {
        while (true) {
            showMenu();
        }
    }

    // Main Menu
    public static void showMenu() {
        System.out.println(CYAN + "\n=== Maze Runner ===" + RESET);
        System.out.println("a. Play Game");
        System.out.println("b. Instructions");
        System.out.println("c. Credits");
        System.out.println("d. High Score");
        System.out.println("e. Exit");
        System.out.print(YELLOW + "Enter your choice: " + RESET);
        char choice = sc.next().toLowerCase().charAt(0);

        switch (choice) {
            case 'a': startNewGame(); break;
            case 'b': showInstructions(); break;
            case 'c': showCredits(); break;
            case 'd': showHighScore(); break;
            case 'e': exitGame(); break;
            default: System.out.println(RED + "Invalid choice! Try again." + RESET);
        }
    }

    // Start a new game
    public static void startNewGame() {
        initializeMaze();
        steps = 0;
        score = 0;
        startTime = System.currentTimeMillis();
        playGame();
    }

    // Initialize Maze Layout
    public static void initializeMaze() {
        maze = new char[][]{
                {'#', '#', '#', '#', '#', '#', '#'},
                {'#', 'P', '.', '.', '.', '.', '#'},
                {'#', '#', '#', '#', '.', '#', '#'},
                {'#', '.', '.', '.', '.', '.', '#'},
                {'#', '#', '#', '.', '.', '.', '#'},
                {'#', '.', '.', '.', '.', 'E', '#'},
                {'#', '#', '#', '#', '#', '#', '#'}
        };
        playerX = 1;
        playerY = 1;
    }

    // Print Maze with color
    public static void printMaze() {
        System.out.println();
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                char c = maze[i][j];
                switch (c) {
                    case '#': System.out.print(BLUE + "#" + RESET + " "); break;
                    case 'P': System.out.print(GREEN + "P" + RESET + " "); break;
                    case 'E': System.out.print(RED + "E" + RESET + " "); break;
                    default:  System.out.print("." + " "); break;
                }
            }
            System.out.println();
        }
    }

    // Check move
    public static boolean isValidMove(int newX, int newY) {
        return newX >= 0 && newX < maze.length &&
                newY >= 0 && newY < maze[0].length &&
                maze[newX][newY] != '#';
    }

    // Move player
    public static boolean movePlayer(char direction) {
        int newX = playerX;
        int newY = playerY;

        switch (Character.toLowerCase(direction)) {
            case 'w': newX--; break;
            case 's': newX++; break;
            case 'a': newY--; break;
            case 'd': newY++; break;
            case 'r':  // Added exit shortcut
                System.out.println(CYAN + "\nReturning to main menu..." + RESET);
                return true; // signals to end game early
            default:
                System.out.println(RED + "Invalid input! Use W/A/S/D or R to exit." + RESET);
                return false;
        }

        if (Character.toLowerCase(direction) == 'r') {
            return true;
        }

        if (!isValidMove(newX, newY)) {
            System.out.println(RED + "You bumped into a wall!" + RESET);
            return false;
        }

        // Move player
        maze[playerX][playerY] = '.';
        playerX = newX;
        playerY = newY;
        steps++;

        // Check if exit reached
        if (maze[newX][newY] == 'E') {
            return true;
        }

        maze[playerX][playerY] = 'P';
        return false;
    }

    // Game loop
    public static void playGame() {
        boolean exit = false;
        while (!exit) {
            printMaze();
            System.out.print(YELLOW + "Move (W/A/S/D) or R to return: " + RESET);
            char move = sc.next().charAt(0);

            // Check if user pressed R to return to menu
            if (Character.toLowerCase(move) == 'r') {
                System.out.println(CYAN + "\nYou chose to exit the game early." + RESET);
                break;
            }

            boolean reachedExit = movePlayer(move);
            if (reachedExit && maze[playerX][playerY] == 'E') {
                displayResult(true);
                return;
            }
        }
        System.out.println(CYAN + "Returning to main menu..." + RESET);
    }

    // show result
    public static void displayResult(boolean won) {
        long timeTaken = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println();
        if (won) {
            System.out.println(GREEN + "🎉 Congratulations! You escaped the maze!" + RESET);
        } else {
            System.out.println(RED + "Game Over!" + RESET);
        }
        System.out.println("Steps Taken: " + steps);
        System.out.println("Time Taken: " + timeTaken + " seconds");

        updateScore(timeTaken);
        if (score > highScore) {
            highScore = score;
            System.out.println(GREEN + "🌟 New High Score!" + RESET);
        }
    }

    // Update score
    public static void updateScore(long time) {
        score = 1000 - (int)(steps * 10 + time * 5);
        if (score < 0) score = 0;
        System.out.println("Your Score: " + YELLOW + score + RESET);
    }

    // Show instructions
    public static void showInstructions() {
        System.out.println(CYAN + "\n=== Instructions ===" + RESET);
        System.out.println("Use W (up), A (left), S (down), D (right) to move.");
        System.out.println("Avoid walls (#) and reach the exit (E) to win!");
        System.out.println("Your score depends on time and steps taken.");
        System.out.println("Press 'R' anytime during the game to return to the main menu.");
    }

    // Show credits
    public static void showCredits() {
        System.out.println(CYAN + "\n=== Credits ===" + RESET);
        System.out.println("Developed by Sajawal Khan");
        System.out.println("Course: CSCS 290");
        System.out.println("Institution: Forman Christian College University");
    }

    // Show high score
    public static void showHighScore() {
        System.out.println(CYAN + "\n=== High Score ===" + RESET);
        System.out.println("Highest Score: " + YELLOW + highScore + RESET);
    }

    // Exit game
    public static void exitGame() {
        System.out.println(GREEN + "\nThank you for playing Maze Runner!" + RESET);
        System.exit(0);
    }
}

