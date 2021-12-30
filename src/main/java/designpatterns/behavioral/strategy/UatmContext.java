package designpatterns.behavioral.strategy;

public class UatmContext {

    private UatmOperation uatmOperation;

    public UatmContext(UatmOperation uatmOperation) {
        this.uatmOperation = uatmOperation;
    }

    public void executeStrategy() {
        this.uatmOperation.executeOperation();
    }
}
