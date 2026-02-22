public class Student {
    public String uid;
    public String name;
    public int fineAmount;
    public int currentBorrowCount;

    public Student(String uid, String name, int fineAmount, int currentBorrowCount) {
        this.uid = uid;
        this.name = name;
        this.fineAmount = fineAmount;
        this.currentBorrowCount = currentBorrowCount;
    }

    public void checkStudentPolicy() throws IllegalStateException {
        if (fineAmount > 0) {
            throw new IllegalStateException("Fine pending: " + fineAmount);
        }
        if (currentBorrowCount >= 2) {
            throw new IllegalStateException("Borrow limit reached");
        }
    }
}
