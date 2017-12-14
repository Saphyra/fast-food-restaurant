package restaurant.meals;

import java.util.Set;

import restaurant.util.PropLoader;
import restaurant.util.Random;

public class MainCourse extends MealPart {
    private MainCourse(Food food) {
        super(food);
    }

    public static MainCourse createRandomMainCourse() {
        Set<String> mainCourses = PropLoader.getMainCourseTypes();
        Object[] mainCourseArr = mainCourses.toArray();
        String type = (String) mainCourseArr[Random.randInt(0, mainCourseArr.length - 1)];
        Food food = new Food(PropLoader.getMainCourse(type));

        return new MainCourse(food);
    }
}
