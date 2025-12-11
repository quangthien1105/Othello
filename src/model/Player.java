package model;

public abstract class Player {
    protected String name;
    protected int color;   // 1 = Đen, 2 = Trắng

    public Player(String name, int color) {
        this.name = name;
        this.color = color;
    }

    protected Player() {
    }

    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
    public abstract int[] makeMove(Board board);
}
