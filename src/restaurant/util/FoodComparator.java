package restaurant.util;

import java.util.Comparator;

import restaurant.meals.Food;

public class FoodComparator implements Comparator<Food> {
    @Override
    public int compare(Food food1, Food food2) {
        int result = 0;
        if (food1.getFoodType().getBaseMoralMultiply() < food2.getFoodType().getBaseMoralMultiply()) {
            result = -1;
        }
        return result;
    }
}
