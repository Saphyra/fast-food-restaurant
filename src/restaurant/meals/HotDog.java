package restaurant.meals;

import restaurant.client.Client;
import restaurant.util.Constants;

public class HotDog extends MainCourse {

    private static final int HOT_DOG_MORAL_INCREMENT = 2;

    public HotDog(Client client) {
        super(client, Constants.HOT_DOG_COOKING_TIME);
    }

    @Override
    protected void eatMainCourse() {
        client.setMoral(client.getMoral() + HOT_DOG_MORAL_INCREMENT);
    }

}
