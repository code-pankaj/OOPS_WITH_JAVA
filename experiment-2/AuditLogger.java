public class AuditLogger {

    public static void log(String msg) {
        System.out.println(msg);
    }

    public static void logError(Exception e) {
        if (e == null) {
            System.out.println("ERROR: null");
            return;
        }
        System.out.println("ERROR: " + e.getClass().getSimpleName() + " - " + e.getMessage());
    }
}
