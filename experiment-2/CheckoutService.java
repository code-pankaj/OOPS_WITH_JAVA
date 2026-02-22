public class CheckoutService {
    private final Student[] students;
    private final AssetStore assetStore;

    public CheckoutService(Student[] students, AssetStore assetStore) {
        if (students == null) {
            throw new IllegalArgumentException("students array is required");
        }
        if (assetStore == null) {
            throw new IllegalArgumentException("assetStore is required");
        }
        this.students = students;
        this.assetStore = assetStore;
    }

    public String checkout(CheckoutRequest req)
            throws IllegalArgumentException, IllegalStateException, SecurityException, NullPointerException {

        if (req == null) {
            throw new IllegalArgumentException("Request is required");
        }

        ValidationUtil.validateUid(req.uid);
        ValidationUtil.validateAssetId(req.assetId);
        ValidationUtil.validateHours(req.hoursRequested);

        Student student = findStudent(req.uid);
        Asset asset = assetStore.findAsset(req.assetId);

        int finalHours = applyRealisticConstraints(req.hoursRequested, asset);
        ValidationUtil.validateHours(finalHours);

        student.checkStudentPolicy();
        asset.checkAssetPolicy(student.uid);

        assetStore.markBorrowed(asset);
        student.currentBorrowCount = student.currentBorrowCount + 1;

        // Keeping it simple: hardcoded date part (22 Feb 2026)
        String date = "20260222";
        return "TXN-" + date + "-" + asset.assetId + "-" + student.uid;
    }

    private Student findStudent(String uid) throws NullPointerException {
        for (int i = 0; i < students.length; i++) {
            if (students[i] != null && students[i].uid.equals(uid)) {
                return students[i];
            }
        }
        throw new NullPointerException("Student not found: " + uid);
    }

    private int applyRealisticConstraints(int hoursRequested, Asset asset) {
        int hours = hoursRequested;

        if (hours == 6) {
            System.out.println("Note: Max duration selected. Return strictly on time.");
        }

        if (asset != null && asset.assetName != null
            && asset.assetName.contains("Cable") && hours > 3) {
            hours = 3;
            System.out.println("Policy applied: Cables can be issued max 3 hours. Updated to 3.");
        }

        return hours;
    }
}
