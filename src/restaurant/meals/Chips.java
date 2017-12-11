package restaurant.meals;

import restaurant.client.Client;
import restaurant.util.Constants;

public class Chips extends MainCourse {

    private static final double CHIPS_MORAL_INCREMENT = 1.05;

    public Chips(Client client) {
        super(client, Constants.CHIPS_COOKING_TIME);
    }

    @Override
    protected void eatMainCourse() {
        client.setMoral(client.getMoral() * CHIPS_MORAL_INCREMENT);
    }

    @Override
    protected String getTypeName() {
        return "Chips";
    }
}
