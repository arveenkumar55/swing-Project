package inlupp2_prog2;

public class DescribedPlace extends Place {

    private String description;

    public DescribedPlace(String category, Position p, String name, String description) {
        super(category, p, name);
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Described place," + getCategory() + "," + p.getXCoordinate() + "," + p.getYCoordinate() + "," + getName() + "," + description;
    }

    

}
