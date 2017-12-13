package restaurant.meals;

import java.util.Set;

import restaurant.util.PropLoader;
import restaurant.util.Random;

public class MainCourse implements Eatable, Cookable {
    private final Food food;

    private MainCourse(Food food) {
        this.food = food;
    }

    public static MainCourse createRandomMainCourse() {
        Set<String> mainCourses = PropLoader.getMainCourseTypes();
        Object[] mainCourseArr = mainCourses.toArray();
        String type = (String) mainCourseArr[Random.randInt(0, mainCourseArr.length - 1)];
        Food food = new Food(PropLoader.getMainCourse(type));

        return new MainCourse(food);
    }

    @Override
    public void cook() {
        food.cook();

    }

    @Override
    public void eat() {
        food.eat();
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
