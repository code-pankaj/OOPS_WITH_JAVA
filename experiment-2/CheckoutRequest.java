public class CheckoutRequest {
    public String uid;
    public String assetId;
    public int hoursRequested;

    public CheckoutRequest(String uid, String assetId, int hoursRequested) {
        this.uid = uid;
        this.assetId = assetId;
        this.hoursRequested = hoursRequested;
        ValidationUtil.validateHours(hoursRequested);
    }
}
