package restaurant.util;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import restaurant.Entrance;
import restaurant.meals.FoodType;

public class PropLoader {
    private static final Properties CONFIG = new Properties();
    private static final Properties EXTRAS = new Properties();
    private static final Properties MAINCOURSES = new Properties();

    private static final Map<String, FoodType> mainCourses = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, FoodType> extras = Collections.synchronizedMap(new HashMap<>());

    public static void init() {
        loadFiles();
        loadFood(MAINCOURSES, mainCourses);
        loadFood(EXTRAS, extras);
    }

    private static void loadFiles() {
        try {
            CONFIG.load(Entrance.class.getResourceAsStream("../config.properties"));
            EXTRAS.load(Entrance.class.getResourceAsStream("../extras.properties"));
            MAINCOURSES.load(Entrance.class.getResourceAsStream("../maincourses.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadFood(Properties source, Map<String, FoodType> target) {
        Set<Object> keys = source.keySet();
        for (Object entry : keys) {
            String key = (String) entry;
            if (key.endsWith(".type")) {
                FoodType foodType = loadFood(key, source);
                target.put(foodType.getType(), foodType);
            }
        }
    }

    private static FoodType loadFood(String key, Properties source) {
        String type = source.getProperty(key);

        String name = source.getProperty(type + ".name");
        long minCookTime = Long.valueOf(source.getProperty(type + ".mincooktime"));
        long maxCookTime = Long.valueOf(source.getProperty(type + ".maxcooktime"));
        long minEatTime = Long.valueOf(source.getProperty(type + ".mineattime"));
        long maxEatTime = Long.valueOf(source.getProperty(type + ".maxeattime"));
        double moralAddition = Double.valueOf(source.getProperty(type + ".moraladdition"));
        double moralMultiply = Double.valueOf(source.getProperty(type + ".moralmultiply"));
        double baseMoralMultiply = Double.valueOf(source.getProperty(type + ".basemoralmultiply"));

        FoodType foodType = new FoodType(type, name, minCookTime, maxCookTime, minEatTime, maxEatTime, moralAddition, moralMultiply, baseMoralMultiply);
        Logger.logToConsole(foodType.toString() + " is loaded.");
        return foodType;
    }

    public static synchronized Set<String> getMainCourseTypes() {
        return mainCourses.keySet();
    }

    public static synchronized Set<String> getExtraTypes() {
        return extras.keySet();
    }

    public static synchronized FoodType getMainCourse(String type) {
        return mainCourses.get(type);
    }

    public static synchronized FoodType getExtra(String type) {
        return extras.get(type);
    }

    public static synchronized long getLongProperty(String entry) {
        return Long.valueOf(CONFIG.getProperty(entry));
    }

    public static synchronized int getIntegerProperty(String entry) {
        return Integer.valueOf(CONFIG.getProperty(entry));
    }

    public static synchronized double getDoubleProperty(String entry) {
        return Double.valueOf(CONFIG.getProperty(entry));
    }
}
