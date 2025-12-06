package model;

public abstract class Player {
    protected int color;
    public abstract int[] getMove(Board board);
}
