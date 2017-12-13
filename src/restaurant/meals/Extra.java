package restaurant.meals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import restaurant.util.PropLoader;
import restaurant.util.Random;

public class Extra implements Cookable, Eatable {

    private final Food food;

    private Extra(Food food) {
        this.food = food;
    }

    public static List<Extra> createRandomExtraList() {
        List<Extra> extras = new ArrayList<>();
        Set<String> extraTypes = PropLoader.getExtraTypes();
        for (String extraType : extraTypes) {
            if (Random.randBoolean()) {
                Food food = new Food(PropLoader.getExtra(extraType));
                Extra extra = new Extra(food);
                extras.add(extra);
            }
        }

        return extras;
    }

    @Override
    public void eat() {
        food.eat();
    }

    @Override
    public void cook() {
        food.cook();
    }

    @Override
    public boolean isEaten() {
        return food.isEaten();
    }

    @Override
    public boolean isCooked() {
        return food.isCooked();
    }

    @Override
    public void setEaten(boolean eaten) {
        food.setEaten(eaten);
    }

    @Override
    public void setCooked(boolean cooked) {
        food.setCooked(cooked);
    }

    @Override
    public String toString() {
        return food.toString();
    }
}
