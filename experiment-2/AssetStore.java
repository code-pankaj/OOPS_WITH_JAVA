public class AssetStore {
    private final Asset[] assets;

    public AssetStore(Asset[] assets) {
        if (assets == null) {
            throw new IllegalArgumentException("assets array is required");
        }
        this.assets = assets;
    }

    public Asset findAsset(String assetId) throws NullPointerException {
        for (int i = 0; i < assets.length; i++) {
            if (assets[i] != null && assets[i].assetId.equals(assetId)) {
                return assets[i];
            }
        }
        throw new NullPointerException("Asset not found: " + assetId);
    }

    public void markBorrowed(Asset a) throws IllegalStateException {
        if (a == null) {
            throw new IllegalStateException("Asset is null");
        }
        if (a.available == false) {
            throw new IllegalStateException("Asset already borrowed");
        }
        a.available = false;
    }
}
