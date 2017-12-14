package restaurant.meals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import restaurant.util.FoodComparator;
import restaurant.util.PropLoader;
import restaurant.util.Random;

public class Meal implements Cookable, Eatable {
    private static final String MAX_MEAL_COUNT = "client.maxmealcount";
    private static final String MIN_MEAL_COUNT = "client.minmealcount";
    private final Food<MainCourse> mainCourse;
    private final List<Food<Extra>> extras;

    private Meal(Food<MainCourse> mainCourse, List<Food<Extra>> extras) {
        this.mainCourse = mainCourse;
        this.extras = extras;
    }

    public static List<Meal> createRandomMealList() {
        int minMealCount = PropLoader.getIntegerProperty(MIN_MEAL_COUNT);
        int maxMealCount = PropLoader.getIntegerProperty(MAX_MEAL_COUNT);
        int mealCount = Random.randInt(minMealCount, maxMealCount);

        List<Meal> mealList = new ArrayList<>(mealCount);
        for (int x = 0; x < mealCount; x++) {
            mealList.add(createRandomMeal());
        }

        return mealList;
    }

    private static Meal createRandomMeal() {
        Food<MainCourse> mainCourse = Food.createRandomMainCourse();
        List<Food<Extra>> extras = Food.createRandomExtraList();

        return new Meal(mainCourse, extras);
    }

    @Override
    public void eat() {
        mainCourse.eat();
        for (Food<Extra> extra : extras) {
            extra.eat();
        }
    }

    public double moralIncrement(double baseMoral) {
        List<ComparableFood> foods = collectFoods();

        double allMoralAddition = 0;
        double allMoralMultiply = 1;

        for (ComparableFood food : foods) {
            FoodType type = food.getFoodType();
            if (type.getBaseMoralMultiply() == 0) {
                allMoralAddition = 0;
                allMoralMultiply = 1;
            } else {
                allMoralMultiply += 1 - type.getBaseMoralMultiply();
            }
            allMoralAddition += type.getMoralAddition();
            allMoralMultiply += type.getMoralMultiply() - 1;
        }

        baseMoral = (baseMoral + allMoralAddition) * allMoralMultiply;

        return baseMoral;
    }

    private List<ComparableFood> collectFoods() {
        List<ComparableFood> foods = new ArrayList<>();
        foods.add(mainCourse);
        for (Food<Extra> extra : extras) {
            foods.add(extra);
        }
        foods.sort(new FoodComparator());
        return foods;
    }

    @Override
    public void cook() {
        mainCourse.cook();
        for (Cookable extra : extras) {
            extra.cook();
        }
    }

    @Override
    public boolean isEaten() {
        boolean result = true;
        if (!mainCourse.isEaten()) {
            result = false;
        }
        for (Eatable extra : extras) {
            if (!extra.isEaten()) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean isCooked() {
        boolean result = true;
        if (!mainCourse.isCooked()) {
            result = false;
        }
        for (Cookable extra : extras) {
            if (!extra.isCooked()) {
                result = false;
            }
        }

        return result;
    }

    @Override
    public void setEaten(boolean eaten) {
        throw new UnsupportedOperationException("Meal cannot set itself eaten.");
    }

    @Override
    public void setCooked(boolean cooked) {
        throw new UnsupportedOperationException("Meal cannot set itself cooked.");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(mainCourse.toString());

        if (!extras.isEmpty()) {
            builder.append(" with ");
            Iterator<Food<Extra>> iter = extras.iterator();
            Food<Extra> first = iter.next();
            builder.append(first.toString());

            while (iter.hasNext()) {
                builder.append(", ").append(iter.next().toString());
            }
        }

        return builder.toString();
    }
}
