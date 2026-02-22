public class ValidationUtil {

    public static void validateUid(String uid) throws IllegalArgumentException {
        if (uid == null) {
            throw new IllegalArgumentException("UID is required");
        }
        if (uid.contains(" ")) {
            throw new IllegalArgumentException("UID must not contain spaces");
        }
        int len = uid.length();
        if (len < 8 || len > 12) {
            throw new IllegalArgumentException("UID length must be 8..12");
        }
    }

    public static void validateAssetId(String assetId) throws IllegalArgumentException {
        if (assetId == null) {
            throw new IllegalArgumentException("AssetId is required");
        }
        if (assetId.startsWith("LAB-") == false) {
            throw new IllegalArgumentException("Invalid assetId: " + assetId);
        }

        if (assetId.length() <= 4) {
            throw new IllegalArgumentException("Invalid assetId: " + assetId);
        }

        for (int i = 4; i < assetId.length(); i++) {
            char ch = assetId.charAt(i);
            if (ch < '0' || ch > '9') {
                throw new IllegalArgumentException("Invalid assetId: " + assetId);
            }
        }
    }

    public static void validateHours(int hrs) throws IllegalArgumentException {
        if (hrs < 1 || hrs > 6) {
            throw new IllegalArgumentException("hoursRequested must be 1..6");
        }
    }
}
