package restaurant.meals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import restaurant.util.PropLoader;
import restaurant.util.Random;

public class Food<T> implements ComparableFood, Cookable {
    private final FoodType foodType;
    private boolean cooked = false;
    private boolean eaten = false;

    public Food(FoodType foodType) {
        this.foodType = foodType;
    }

    public static List<Food<Extra>> createRandomExtraList() {
        List<Food<Extra>> extras = new ArrayList<>();
        Set<String> extraTypes = PropLoader.getExtraTypes();
        for (String extraType : extraTypes) {
            if (Random.randBoolean()) {
                Food<Extra> extra = new Food<Extra>(PropLoader.getExtra(extraType));
                extras.add(extra);
            }
        }

        return extras;
    }

    public static Food<MainCourse> createRandomMainCourse() {
        Set<String> mainCourses = PropLoader.getMainCourseTypes();
        Object[] mainCourseArr = mainCourses.toArray();
        String type = (String) mainCourseArr[Random.randInt(0, mainCourseArr.length - 1)];

        Food<MainCourse> food = new Food<>(PropLoader.getMainCourse(type));
        return food;
    }

    @Override
    public void cook() {
        try {
            Long minCookTime = foodType.getMinCookTime();
            Long maxCookTime = foodType.getMaxCookTime();
            long sleepTime = Random.randLong(minCookTime, maxCookTime);
            Thread.sleep(sleepTime);
            setCooked(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setCooked(boolean cooked) {
        this.cooked = cooked;
    }

    @Override
    public boolean isCooked() {
        return cooked;
    }

    @Override
    public void eat() {
        try {
            long sleepTime = Random.randLong(foodType.getMinEatTime(), foodType.getMaxEatTime());
            Thread.sleep(sleepTime);
            setEaten(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isEaten() {
        return eaten;
    }

    @Override
    public void setEaten(boolean eaten) {
        this.eaten = eaten;
    }

    @Override
    public FoodType getFoodType() {
        return foodType;
    }

    @Override
    public String toString() {
        return foodType.getName();
    }

    @Override
    public int compareTo(ComparableFood arg) {
        int result = 0;
        if (foodType.getBaseMoralMultiply() < arg.getFoodType().getBaseMoralMultiply()) {
            result = -1;
        }
        return result;
    }

}
