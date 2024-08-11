package src;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Board {
    private Position[][] board = new Position[8][8];
    private Scanner sc = new Scanner(System.in);
    private boolean validInput = false;
    private int row, col;
    private Game game;
    private Player p1;
    private Player p2;

    public Board(Player p1, Player p2, Game game) {
        this.p1 = p1;
        this.p2 = p2;
        this.game = game;

        // Initialize the board with empty positions
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = new PlayablePosition(Position.EMPTY);
            }
        }
    }
//starts the beginning of the process of board offset configuration
    public void boardConfig() {
        System.out.println("Select 0 for the Standard Starting Positions ");
        System.out.println("and select 1 for an Offset Starting Position: ");

        // Loop to handle valid boardType input
        while (!validInput) {
            try {
                int boardType = sc.nextInt();
                if (boardType == 0 || boardType == 1) {
                    validInput = true;
                    configureBoard(boardType);
                } else {
                    System.out.println("Please select 0 or 1");
                }
            } catch (InputMismatchException ex) {
                System.out.println("Please enter a number");
                sc.next(); // Clear the invalid input
            }
        }
    }
//continues the process mentiopned above
    private void configureBoard(int boardType) {
        if (boardType == 0) {
            // Standard starting position
            board[3][3] = new PlayablePosition(Position.WHITE);
            board[3][4] = new PlayablePosition(Position.BLACK);
            board[4][3] = new PlayablePosition(Position.BLACK);
            board[4][4] = new PlayablePosition(Position.WHITE);
        } else if (boardType == 1) {
            boolean validInput = false;
            while (!validInput) {
                System.out.println("Please select a number 1-4 to get an Offset Starting Position");
                try {
                    int offsetType = sc.nextInt();
                    if (offsetType >= 1 && offsetType <= 4) {
                        validInput = true;
                        switch (offsetType) {
                            case 1:
                                board[2][2] = new PlayablePosition(Position.WHITE);
                                board[2][3] = new PlayablePosition(Position.BLACK);
                                board[3][2] = new PlayablePosition(Position.BLACK);
                                board[3][3] = new PlayablePosition(Position.WHITE);
                                break;
                            case 2:
                                board[2][4] = new PlayablePosition(Position.WHITE);
                                board[2][5] = new PlayablePosition(Position.BLACK);
                                board[3][4] = new PlayablePosition(Position.BLACK);
                                board[3][5] = new PlayablePosition(Position.WHITE);
                                break;
                            case 3:
                                board[4][2] = new PlayablePosition(Position.WHITE);
                                board[4][3] = new PlayablePosition(Position.BLACK);
                                board[5][2] = new PlayablePosition(Position.BLACK);
                                board[5][3] = new PlayablePosition(Position.WHITE);
                                break;
                            case 4:
                                board[4][4] = new PlayablePosition(Position.WHITE);
                                board[4][5] = new PlayablePosition(Position.BLACK);
                                board[5][4] = new PlayablePosition(Position.BLACK);
                                board[5][5] = new PlayablePosition(Position.WHITE);
                                break;
                            default:
                                System.out.println("Unexpected boardType value");
                                break;
                        }
                    } else {
                        System.out.println("Please select a number between 1 and 4");
                    }
                } catch (InputMismatchException ex) {
                    System.out.println("Please enter a number");
                    sc.next(); // Clear the invalid input
                }
            }
        }
    }

//draws the characterboard associated to the code
    public void drawBoard() {
        char[][] charBoard = inToCharArray();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                System.out.print(charBoard[row][col] + " ");
            }
            System.out.println(row);
            if (row == 7) break;
            System.out.println();
        }
        for (int col = 0; col < 8; col++) {
            System.out.print(col + " ");
        }
    }

    public Position[][] getBoard() {
        return board;
    }
//turns the position board into a chararray
    public char[][] inToCharArray() {
        char[][] charArray = new char[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                charArray[row][col] = board[row][col].getPiece();
            }
        }
        return charArray;
    }

    public void takeTurn() {
        boolean validMoveMade = false;

        // Prompt the current player at the start of each turn
        while (!validMoveMade) {
            System.out.println();
            System.out.println("It's " + game.getCurrentPlayer().getName() + "'s turn. Choose an option:");
            System.out.println("1. Make a move");
            System.out.println("2. Save the game");
            System.out.println("3. Concede the game");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    if (canMove()) {
                        System.out.println("Insert the row and column you would like to put your piece in");
                        System.out.print("row: ");
                        int row = sc.nextInt();
                        System.out.print("col: ");
                        int col = sc.nextInt();

                        if (isValidMove(row, col, game.getCurrentPlayer().getColor())) {
                            board[row][col].setPiece(game.getCurrentPlayer().getColor()); // Place the current player's piece

                            // Flip opponent pieces
                            flipOpponentPieces(row, col, game.getCurrentPlayer().getColor());

                            drawBoard();
                            validMoveMade = true;
                            game.switchPlayer();
                        } else {
                            System.out.println("Invalid move. Try again.");
                        }
                    } else {
                        System.out.println("No valid moves available.");
                        NoMove();
                        validMoveMade = true;
                    }
                    break;

                case 2:
                    game.save();
                    System.out.println("Game saved. Exiting...");
                    System.exit(0);
                    break;

                case 3:
                    if (game.getCurrentPlayer() == p1) {
                        System.out.println(p1.getName() + " has conceded. " + p2.getName() + " wins!");
                    } else {
                        System.out.println(p2.getName() + " has conceded. " + p1.getName() + " wins!");
                    }
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
                    break;
            }
        }
    }



    public boolean checkWin() {
        int blackCount = 0;
        int whiteCount = 0;
        boolean movePossible = false;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                char piece = board[row][col].getPiece();
                if (piece == Position.BLACK) {
                    blackCount++;
                } else if (piece == Position.WHITE) {
                    whiteCount++;
                }

                if (piece == Position.EMPTY) {
                    if (isValidMove(row, col, game.getCurrentPlayer().getColor())) {
                        movePossible = true;
                    }
                }
            }
        }

        if (!movePossible) {
            if (blackCount > whiteCount) {
                System.out.println(p1.getName() + " (Black) wins!");
            } else if (whiteCount > blackCount) {
                System.out.println(p2.getName() + " (White) wins!");
            } else {
                System.out.println("It's a tie!");
            }
            return true;
        }

        return false;
    }

    private boolean isValidMove(int row, int col, char currentPlayerColor) {
        if (board[row][col].getPiece() != Position.EMPTY) {
            return false;
        }

        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {
                if (dRow == 0 && dCol == 0) continue;

                int r = row + dRow, c = col + dCol;
                boolean foundOpponent = false;

                while (r >= 0 && r < 8 && c >= 0 && c < 8 && board[r][c].getPiece() != Position.EMPTY) {
                    if (board[r][c].getPiece() == currentPlayerColor) {
                        if (foundOpponent) {
                            return true;
                        } else {
                            break;
                        }
                    } else {
                        foundOpponent = true;
                    }
                    r += dRow;
                    c += dCol;
                }
            }
        }
        return false;
    }

    private void NoMove() {
        System.out.println("Choose an option: \n1. Save the game\n2. Concede the game\n3. Forfeit turn");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                game.save();
                System.exit(0);
                break;
            case 2:
                if (game.getCurrentPlayer() == p1) {
                    System.out.println(p1.getName() + " has conceded. " + p2.getName() + " wins!");
                } else {
                    System.out.println(p2.getName() + " has conceded. " + p1.getName() + " wins!");
                }
                System.exit(0);
                break;
            case 3:
                System.out.println("Forfeiting turn. It is now " + (game.getCurrentPlayer() == p1 ? p2.getName() : p1.getName()) + "'s turn.");
                game.switchPlayer();
                break;
            default:
                System.out.println("Invalid choice. Try again.");
                NoMove();
                break;
        }
    }
    boolean canMove() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (isValidMove(row, col, game.getCurrentPlayer().getColor())) {
                    return true;
                }
            }
        }
        return false;
    }
    private void flipOpponentPieces(int row, int col, char currentColor) {
        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {
                if (dRow == 0 && dCol == 0) continue;

                int r = row + dRow, c = col + dCol;
                boolean foundOpponent = false;
                while (r >= 0 && r < 8 && c >= 0 && c < 8 && board[r][c].getPiece() != Position.EMPTY && board[r][c].getPiece() != currentColor) {
                    r += dRow;
                    c += dCol;
                    foundOpponent = true;
                }

                if (foundOpponent && r >= 0 && r < 8 && c >= 0 && c < 8 && board[r][c].getPiece() == currentColor) {
                    r -= dRow;
                    c -= dCol;
                    while (r != row || c != col) {
                        board[r][c].setPiece(currentColor);
                        r -= dRow;
                        c -= dCol;
                    }
                }
            }
        }
    }

}
