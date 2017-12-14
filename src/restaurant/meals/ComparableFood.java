package restaurant.meals;

public interface ComparableFood extends Eatable, Comparable<ComparableFood> {
    FoodType getFoodType();
}
