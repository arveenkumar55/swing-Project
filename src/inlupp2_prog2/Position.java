package inlupp2_prog2;

public class Position {

    int x, y;

    public Position(int x, int y) {

        this.x = x;
        this.y = y;

    }

    public int hashCode() {
        return x * 100 + y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position other = (Position) obj;
            return (x == other.x && y == other.y);
        }
        return false;
    }

    public int getXCoordinate() {
        return x;
    }

    public int getYCoordinate() {
        return y;
    }

    @Override
    public String toString() {
        return "{ " + getXCoordinate() + "," + getYCoordinate() + " }";
    }
}
