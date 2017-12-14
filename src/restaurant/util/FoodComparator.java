package restaurant.util;

import java.util.Comparator;

import restaurant.meals.ComparableFood;

public class FoodComparator implements Comparator<ComparableFood> {
    @Override
    public int compare(ComparableFood food1, ComparableFood food2) {
        return food1.compareTo(food2);
    }
}
