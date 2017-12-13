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

    public FoodType(String type, String name, long minCookTime, long maxCookTime, long minEatTime, long maxEatTime, double moralAddition,
            double moralMultiply) {
        this.type = type;
        this.name = name;
        this.minCookTime = minCookTime;
        this.maxCookTime = maxCookTime;
        this.minEatTime = minEatTime;
        this.maxEatTime = maxEatTime;
        this.moralAddition = moralAddition;
        this.moralMultiply = moralMultiply;
        System.out.println(this);
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Type: " + type).append(" - Name: " + name).append(" - minCookTime: " + minCookTime).append(" - maxCookTime: " + maxCookTime)
                .append(" - minEatTime: " + minEatTime).append(" - maxEatTime: " + maxEatTime).append(" - moralAddition: " + moralAddition)
                .append(" - moralMultiply: " + moralMultiply);

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (maxCookTime ^ (maxCookTime >>> 32));
        result = prime * result + (int) (maxEatTime ^ (maxEatTime >>> 32));
        result = prime * result + (int) (minCookTime ^ (minCookTime >>> 32));
        result = prime * result + (int) (minEatTime ^ (minEatTime >>> 32));
        long temp;
        temp = Double.doubleToLongBits(moralAddition);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(moralMultiply);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FoodType other = (FoodType) obj;
        if (maxCookTime != other.maxCookTime)
            return false;
        if (maxEatTime != other.maxEatTime)
            return false;
        if (minCookTime != other.minCookTime)
            return false;
        if (minEatTime != other.minEatTime)
            return false;
        if (Double.doubleToLongBits(moralAddition) != Double.doubleToLongBits(other.moralAddition))
            return false;
        if (Double.doubleToLongBits(moralMultiply) != Double.doubleToLongBits(other.moralMultiply))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
