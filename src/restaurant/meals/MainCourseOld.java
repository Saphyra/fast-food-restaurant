package restaurant.meals;

import java.util.Vector;

import restaurant.Entrance;
import restaurant.client.Client;
import restaurant.util.Random;

//Abstract class for Main Courses' data
public abstract class MainCourseOld implements Eatable, Cookable {

    private static final int MUSTARD_MORAL_INCREMENT = Integer.valueOf((String) Entrance.EXTRAS.get("mustard.moraladdition"));
    public static final int HOT_DOG = 1;
    public static final int CHIPS = 2;

    protected boolean cooked = false;
    protected final Client client;
    protected final long cookTime;

    protected final boolean withKetchup;
    protected final boolean withMustard;

    // Creating MealList for a Client
    public static Vector<MainCourseOld> mealListFactory(Client client) {
        Vector<MainCourseOld> mealList = new Vector<>();

        int mealNum = Random.randInt(minMealCount, maxMealCount);
        for (int x = 0; x < mealNum; x++) {
            mealList.add(mainCourseFactory(client));
        }
        return mealList;
    }

    // Creating one MainCourse
    private static MainCourseOld mainCourseFactory(Client client) {
        MainCourseOld mainCourse = null;
        switch (Random.randInt(1, 2)) {
        case HOT_DOG:
            mainCourse = new HotDog(client);
            break;
        case CHIPS:
            mainCourse = new Chips(client);
            break;
        }
        return mainCourse;
    }

    protected MainCourseOld(Client client, long cookTime) {
        withKetchup = Random.randBoolean();
        withMustard = Random.randBoolean();
        this.client = client;
        this.cookTime = cookTime;
    }

    // Eating the Meal
    @Override
    public void eat() {
        if (withMustard) {
            mustardEffect();
        } else {
            eatMainCourse();
            if (withKetchup) {
                ketchupEffect();
            }
        }
    }

    // Eating Mail Course
    protected abstract void eatMainCourse();

    // Eating Ketchup
    protected void ketchupEffect() {
        eatMainCourse();
    }

    // Eating Mustard
    protected void mustardEffect() {
        client.setMoral(client.getMoral() + MUSTARD_MORAL_INCREMENT);
    }

    public boolean isCooked() {
        return cooked;
    }

    public void setCooked(boolean cooked) {
        this.cooked = cooked;
    }

    public boolean isWithKetchup() {
        return withKetchup;
    }

    public boolean isWithMustard() {
        return withMustard;
    }

    @Override
    public long cook() {
        return cookTime;
    }

    public Client getClient() {
        return client;
    }

    protected abstract String getTypeName();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("MainCourse: " + getTypeName());

        if (withKetchup) {
            builder.append(" with ketchup");
        }
        if (withKetchup && withMustard) {
            builder.append(" and ");
        }
        if (withMustard) {
            builder.append(" with mustard");
        }

        return builder.toString();
    }
}