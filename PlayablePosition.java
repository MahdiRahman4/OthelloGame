package src;

public class PlayablePosition extends Position {


    public PlayablePosition(char piece) {
        this.piece = piece;
        this.canPlay = true;
    }
}
