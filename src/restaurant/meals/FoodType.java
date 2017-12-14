package restaurant.meals;

public class FoodType {
    private final String type;
    private final String name;
    private final long minCookTime;
    private final long maxCookTime;
    private final long minEatTime;
    private final long maxEatTime;
    private final double moralAddition;
    private final double moralMultiply;
    private final double baseMoralMultiply;

    public FoodType(String type, String name, long minCookTime, long maxCookTime, long minEatTime, long maxEatTime, double moralAddition, double moralMultiply,
            double baseMoralMultiply) {
        this.type = type;
        this.name = name;
        this.minCookTime = minCookTime;
        this.maxCookTime = maxCookTime;
        this.minEatTime = minEatTime;
        this.maxEatTime = maxEatTime;
        this.moralAddition = moralAddition;
        this.moralMultiply = moralMultiply;
        this.baseMoralMultiply = baseMoralMultiply;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public long getMinCookTime() {
        return minCookTime;
    }

    public long getMaxCookTime() {
        return maxCookTime;
    }

    public long getMinEatTime() {
        return minEatTime;
    }

    public long getMaxEatTime() {
        return maxEatTime;
    }

    public double getMoralAddition() {
        return moralAddition;
    }

    public double getMoralMultiply() {
        return moralMultiply;
    }

    public double getBaseMoralMultiply() {
        return baseMoralMultiply;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Type: " + type).append(" - Name: " + name).append(" - minCookTime: " + minCookTime).append(" - maxCookTime: " + maxCookTime)
                .append(" - minEatTime: " + minEatTime).append(" - maxEatTime: " + maxEatTime).append(" - moralAddition: " + moralAddition)
                .append(" - moralMultiply: " + moralMultiply).append(" - baseMoralMultiply: " + baseMoralMultiply);

        return builder.toString();
    }
}
