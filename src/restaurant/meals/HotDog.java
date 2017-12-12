package restaurant.meals;

import restaurant.Entrance;
import restaurant.client.Client;

public class HotDog extends MainCourse {

    private static final long cookTime = Long.valueOf((String) Entrance.MAINCOURSES.get("hotdog.cooktime"));

    public HotDog(Client client) {
        super(client, cookTime);
    }

    @Override
    protected void eatMainCourse() {
        double moralIncrement = Double.valueOf((String) Entrance.MAINCOURSES.get("hotdog.moraladdition"));
        client.setMoral(client.getMoral() + moralIncrement);
    }

    @Override
    protected String getTypeName() {
        return "Hot-Dog";
    }
}
