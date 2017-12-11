package restaurant.util;

import java.util.concurrent.LinkedBlockingQueue;

public class Logger implements Runnable {
    private enum Mode {
        ERR, OUT
    }

    private static LinkedBlockingQueue<LogMessage> queue = new LinkedBlockingQueue<>();

    public static void logToConsole(String message) {
        queue.add(new LogMessage(Mode.OUT, message));
    }

    public static void logToErr(String message) {
        queue.add(new LogMessage(Mode.ERR, message));
    }

    @Override
    public void run() {
        while (true) {
            LogMessage message;
            try {
                if (Random.randInt(1, 100) < 10) {
                    System.out.println("Log queue size: " + queue.size());
                    Thread.sleep(10);
                }

                message = queue.take();

                switch (message.getMode()) {
                case OUT:
                    System.out.println(message.getMessage());
                    Thread.sleep(100);
                    break;
                case ERR:
                    System.err.println(message.getMessage());
                    Thread.sleep(1000);
                    break;
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    private static class LogMessage {
        private final Mode mode;
        private final String message;

        public LogMessage(Mode type, String message) {
            this.mode = type;
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
}
