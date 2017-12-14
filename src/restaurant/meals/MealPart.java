package restaurant.meals;

public abstract class MealPart implements Eatable, Cookable {
    protected final Food food;

    protected MealPart(Food food) {
        this.food = food;
    }

    @Override
    public void cook() {
        food.cook();
    }

    @Override
    public boolean isCooked() {
        return food.isCooked();
    }

    @Override
    public void setCooked(boolean cooked) {
        food.setCooked(cooked);

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
    public void setEaten(boolean eaten) {
        food.setEaten(eaten);
    }

    public Food getFood() {
        return food;
    }

    @Override
    public String toString() {
        return food.toString();
    }
}
