package restaurant.meals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import restaurant.util.PropLoader;
import restaurant.util.Random;

public class MealOrder {
    private static final String MAX_MEAL_COUNT = "client.maxmealcount";
    private static final String MIN_MEAL_COUNT = "client.minmealcount";

    private final String clientName;
    private final String mainCourseType;
    private final List<String> extraTypes;
    private String deskName;

    private MealOrder(String clientName, String mainCourseType, List<String> extraTypes) {
        this.clientName = clientName;
        this.mainCourseType = mainCourseType;
        this.extraTypes = extraTypes;
    }

    public static List<MealOrder> createRandomMealOrderList(String clientName) {
        int minMealCount = PropLoader.getIntegerProperty(MIN_MEAL_COUNT);
        int maxMealCount = PropLoader.getIntegerProperty(MAX_MEAL_COUNT);
        int mealCount = Random.randInt(minMealCount, maxMealCount);

        List<MealOrder> mealList = new ArrayList<>(mealCount);
        for (int x = 0; x < mealCount; x++) {
            mealList.add(createRandomMealOrder(clientName));
        }

        return mealList;
    }

    private static MealOrder createRandomMealOrder(String clientName) {
        String mainCourseOrder = createRandomMainCourseOrder();
        List<String> extraOrder = createRandomExtraList();

        return new MealOrder(clientName, mainCourseOrder, extraOrder);
    }

    public static String createRandomMainCourseOrder() {
        Set<String> mainCourses = PropLoader.getMainCourseTypes();
        Object[] mainCourseArr = mainCourses.toArray();
        String mainCourseType = (String) mainCourseArr[Random.randInt(0, mainCourseArr.length - 1)];

        return mainCourseType;
    }

    public static List<String> createRandomExtraList() {
        List<String> extras = new ArrayList<>();
        Set<String> extraTypes = PropLoader.getExtraTypes();
        for (String extraType : extraTypes) {
            if (Random.randBoolean()) {
                extras.add(extraType);
            }
        }

        return extras;
    }

    public String getClientName() {
        return clientName;
    }

    public String getMainCourseName() {
        return mainCourseType;
    }

    public List<String> getExtraNames() {
        return extraTypes;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }
}
