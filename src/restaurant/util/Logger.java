package restaurant.util;

import java.util.concurrent.LinkedBlockingQueue;

public class Logger implements Runnable {
    private enum Mode {
        ERR, OUT
    }

    private static int logMessageCount = 0;
    private static final String CONSOLE_SLEEP_TIME = "logger.consolesleeptime";
    private static final String ERROR_SLEEP_TIME = "logger.errorsleeptime";
    private static LinkedBlockingQueue<LogMessage> queue = new LinkedBlockingQueue<>();

    private long lastCheckTime = System.currentTimeMillis();
    private long consoleSleepTime = PropLoader.getLongProperty(CONSOLE_SLEEP_TIME);
    private long errorSleepTime = PropLoader.getLongProperty(ERROR_SLEEP_TIME);

    public static void logToConsole(String message) {
        queue.add(new LogMessage(Mode.OUT, message));
    }

    public static void logToErr(String message) {
        queue.add(new LogMessage(Mode.ERR, message));
    }

    @Override
    public void run() {
        while (true) {
            try {
                LogMessage message = queue.take();
                printQueueSize();
                printLog(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void printLog(LogMessage message) throws InterruptedException {
        logMessageCount++;
        switch (message.getMode()) {
        case OUT:
            System.out.println(message.getMessage());
            Thread.sleep(consoleSleepTime);
            break;
        case ERR:
            System.err.println(message.getMessage());
            Thread.sleep(errorSleepTime);
            break;
        }
    }

    public static int getLogMessageCount() {
        return logMessageCount;
    }

    private static class LogMessage {
        private final Mode mode;
        private final String message;

        public LogMessage(Mode type, String message) {
            mode = type;
            this.message = message;
        }

        public Mode getMode() {
            return mode;
        }

        public String getMessage() {
            return message;
        }
    }

    public static boolean isQueueEmpty() {
        return queue.size() == 0;
    }

    private void printQueueSize() throws InterruptedException {
        long currentTime = System.currentTimeMillis();
        if (currentTime > lastCheckTime + 10000) {
            lastCheckTime = currentTime;
            if (queue.size() > 10) {
                LogMessage message = new LogMessage(Mode.ERR, "Current log queue size: " + queue.size());
                printLog(message);
            }
        }
    }
}
