package restaurant.meals;

import restaurant.Entrance;
import restaurant.client.Client;

public class Chips extends MainCourse {

    private static final long cookTime = Long.valueOf((String) Entrance.MAINCOURSES.get("chips.cooktime"));

    public Chips(Client client) {
        super(client, cookTime);
    }

    @Override
    protected void eatMainCourse() {
        double moralIncrement = Double.valueOf((String) Entrance.MAINCOURSES.get("chips.moraladdition"));
        double moralMultiply = Double.valueOf((String) Entrance.MAINCOURSES.get("chips.moralmultiply"));
        client.setMoral(client.getMoral() * moralMultiply);
    }

    @Override
    protected String getTypeName() {
        return "Chips";
    }
}
