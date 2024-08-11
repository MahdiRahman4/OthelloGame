package src;

public class UnplayablePosition extends Position {
    final char UNPLAYABLE = '*';

    public UnplayablePosition() {
        this.piece = UNPLAYABLE;
        this.canPlay = false;
    }
}
