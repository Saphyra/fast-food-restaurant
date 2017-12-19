package restaurant.meals;

//TODO Desk �s kliens azonos�t�val c�mzett Meal wraper 
public class MealPackage {
    private final String deskName;
    private final String clientName;
    private final Meal meal;

    public MealPackage(String deskName, String clientName, Meal meal) {
        this.deskName = deskName;
        this.clientName = clientName;
        this.meal = meal;
    }

    public String getDeskName() {
        return deskName;
    }

    public String getClientName() {
        return clientName;
    }

    public Meal getMeal() {
        return meal;
    }

}
