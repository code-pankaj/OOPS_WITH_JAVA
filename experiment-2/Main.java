public class Main {
    public static void main(String[] args) {
        Student[] students = new Student[] {
                new Student("KRG20281", "Kiran", 0, 0),
                new Student("STD12345", "Amit", 0, 0),
                new Student("KRG90001", "Kavya", 50, 0)
        };

        Asset[] assets = new Asset[] {
                new Asset("LAB-101", "HDMI Cable", true, 1),
                new Asset("LAB-102", "Oscilloscope", true, 3),
                new Asset("LAB-103", "Ethernet Cable", false, 1),
                new Asset("LAB-104", "Raspberry Pi Kit", true, 2)
        };

        AssetStore store = new AssetStore(assets);
        CheckoutService service = new CheckoutService(students, store);

        CheckoutRequest[] requests = new CheckoutRequest[] {
                new CheckoutRequest("KRG20281", "LAB-101", 5), 
                new CheckoutRequest("KRG20281", "LAB101", 2),   
                new CheckoutRequest("STD12345", "LAB-102", 1)   
        };

        for (CheckoutRequest req : requests) {
            try {
                String receipt = service.checkout(req);
                System.out.println("SUCCESS: " + receipt);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid request: " + e.getMessage());
                AuditLogger.logError(e);
            } catch (NullPointerException e) {
                System.out.println("Not found: " + e.getMessage());
                AuditLogger.logError(e);
            } catch (SecurityException e) {
                System.out.println("Security blocked: " + e.getMessage());
                AuditLogger.logError(e);
            } catch (IllegalStateException e) {
                System.out.println("Policy blocked: " + e.getMessage());
                AuditLogger.logError(e);
            } finally {
                AuditLogger.log("Audit: attempt finished for UID=" + (req == null ? null : req.uid)
                        + ", asset=" + (req == null ? null : req.assetId));
                System.out.println("----");
            }
        }
    }
}
