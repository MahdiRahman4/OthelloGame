package src;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {
    private Board boardObject;
    private Scanner sc = new Scanner(System.in);
    private boolean validInput = false;
    private int input;
    private Player p1;
    private Player p2;
    private Player current;
    private Player opponent;

    public Game() {
        this.p1 = new Player();
        this.p2 = new Player();
    }


//menu screen for the start of the program
    public void menuScreen() {
        System.out.println("1. Quit");
        System.out.println("2. Load a Game");
        System.out.println("3. Start a New Game");

        while (!validInput) {
            try {
                input = sc.nextInt();
                if (input >= 1 && input <= 3) {
                    validInput = true;
                } else {
                    System.out.println("Please select a number between 1 and 3");
                }
            } catch (InputMismatchException ex) {
                System.out.println("Please enter a number");
                sc.next(); // Clear invalid input
            }
        }

        switch (input) {
            case 1:
                System.exit(0);
                break;
            case 2:
                load();
                break;
            case 3:
                start();
                break;
        }
    }
//start of the game
    public void start() {
        System.out.println("Enter Player 1's name: ");
        String nameP1 = sc.next();
        p1.setName(nameP1);
        p1.setColor(Position.BLACK);

        System.out.println("Enter Player 2's name: ");
        String nameP2 = sc.next();
        p2.setName(nameP2);
        p2.setColor(Position.WHITE);

        boardObject = new Board(p1, p2, this);
        boardObject.boardConfig();

        boardObject.drawBoard();
        current = p1;
        opponent = p2;

        // Game loop
        while (true) {
            if (!boardObject.canMove()) {
                if (boardObject.checkWin()) {
                    System.exit(0);
                }
                switchPlayer(); // Switch to the other player if no move can be made
            }
            boardObject.takeTurn();
        }
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("SavedGame.txt", false))) {
            writer.write(p1.getName());
            writer.newLine();
            writer.write(p2.getName());
            writer.newLine();
            writer.write(p1.getColor());
            writer.newLine();
            writer.write(p2.getColor());
            writer.newLine();

            Position[][] board = boardObject.getBoard();
            for (int j = 0; j < 8; j++) {
                for (int i = 0; i < 8; i++) {
                    writer.write(board[i][j].getPiece());
                }
                writer.newLine();
            }
        } catch (IOException ex) {
            System.err.println("Error writing to file: " + ex.getMessage());
        }

        System.out.println("Saved Board:");
        boardObject.drawBoard();
    }

    public void load() {
        try (BufferedReader reader = new BufferedReader(new FileReader("SavedGame.txt"))) {
            if (boardObject == null) {
                // Initialize the boardObject with current players
                boardObject = new Board(p1, p2, this);
            }

            p1.setName(reader.readLine());
            p2.setName(reader.readLine());
            p1.setColor(reader.readLine().charAt(0));
            p2.setColor(reader.readLine().charAt(0));

            Position[][] board = boardObject.getBoard();
            for (int j = 0; j < 8; j++) {
                String line = reader.readLine();
                for (int i = 0; i < 8; i++) {
                    board[i][j].setPiece(line.charAt(i));
                }
            }

            System.out.println("Loaded Board:");
            boardObject.drawBoard();
        } catch (IOException ex) {
            System.err.println("Error reading from file: " + ex.getMessage());
        }
    }

    public void switchPlayer() {
        if (current == p1) {
            current = p2;
            opponent = p1;
        } else {
            current = p1;
            opponent = p2;
        }
    }

    public Player getCurrentPlayer() {
        return current;
    }
}
