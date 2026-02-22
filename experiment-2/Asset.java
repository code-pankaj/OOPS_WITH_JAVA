public class Asset {
    public String assetId;
    public String assetName;
    public boolean available;
    public int securityLevel;

    public Asset(String assetId, String assetName, boolean available, int securityLevel) {
        this.assetId = assetId;
        this.assetName = assetName;
        this.available = available;
        this.securityLevel = securityLevel;
    }

    public void checkAssetPolicy(String uid) throws IllegalStateException, SecurityException {
        if (available == false) {
            throw new IllegalStateException("Asset not available");
        }
        if (securityLevel == 3) {
            if (uid == null || uid.startsWith("KRG") == false) {
                throw new SecurityException("Security level 3: Only KRG UID allowed");
            }
        }
    }
}
