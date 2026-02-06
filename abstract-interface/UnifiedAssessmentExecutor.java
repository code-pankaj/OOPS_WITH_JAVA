abstract class AbstractAssessmentFlow {

    public final void executeAssessment() {
        validate();
        prepare();
        evaluate();        // abstract step
        publishResult();
    }

    protected final void validate() {
        System.out.println("Validating assessment configuration...");
    }

    protected void prepare() {
        System.out.println("Preparing assessment environment...");
    }

    protected abstract void evaluate();

    protected void publishResult() {
        System.out.println("Publishing results...");
    }
}

interface AutoAssessment {
    default void evaluate() {
        System.out.println("Running AUTO evaluation engine...");
    }
}

interface ManualAssessment {
    default void evaluate() {
        System.out.println("Running MANUAL evaluation workflow...");
    }
}

public class UnifiedAssessmentExecutor
        extends AbstractAssessmentFlow
        implements AutoAssessment, ManualAssessment {

    private final boolean isProctored;

    public UnifiedAssessmentExecutor(boolean isProctored) {
        this.isProctored = isProctored;
    }

    public void evaluate() {
        if (isProctored) {
            ManualAssessment.super.evaluate();
        } else {
            AutoAssessment.super.evaluate();
        }
    }

    public static void main(String[] args) {
        UnifiedAssessmentExecutor auto =
                new UnifiedAssessmentExecutor(false);

        UnifiedAssessmentExecutor manual =
                new UnifiedAssessmentExecutor(true);

        System.out.println("AUTO MODE -");
        auto.executeAssessment();

        System.out.println("\nMANUAL MODE -");
        manual.executeAssessment();
    }
}
