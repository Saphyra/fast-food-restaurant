package restaurant.meals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import restaurant.util.PropLoader;
import restaurant.util.Random;

public class Extra extends MealPart {

    private Extra(Food food) {
        super(food);
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
}
