package inlupp2_prog2;

public class NamedPlace extends Place {

    public NamedPlace(String category, Position p, String name) {
        super(category, p, name);
    }

    @Override
    public String toString() {
        return "Named," + getCategory() + "," + p.getXCoordinate() + "," + p.getYCoordinate() + "," + getName();
    }

}
