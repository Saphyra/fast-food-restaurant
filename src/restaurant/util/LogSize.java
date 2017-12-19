package restaurant.util;

public class LogSize {
    private final long time;
    private final int size;

    public LogSize(long time, int size) {
        this.time = time / 1000;
        this.size = size;
    }

    public void print() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "The log size at " + time + " seconds was " + size;
    }
}
