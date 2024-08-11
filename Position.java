package src;

public abstract class Position {
    public static final char EMPTY = '.';
    public static final char BLACK = 'B';
    public static final char WHITE = 'W';
    protected char piece;
    protected boolean canPlay = false;

    public Position() {
        this.piece = EMPTY;
    }

    public char getPiece() {
        return piece;
    }

    public void setPiece(char piece) {
        this.piece = piece;
    }

    public boolean canPlay() {
        return canPlay;
    }
}
