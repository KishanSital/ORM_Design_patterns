package designpatterns.behavioral.strategy;

import designpatterns.creational.builder.entities.bank.BankAccount;

import java.math.BigDecimal;

public class UatmContext {

    private UatmOperation uatmOperation;

    public UatmContext(UatmOperation uatmOperation) {
        this.uatmOperation = uatmOperation;
    }

    public BigDecimal executeStrategy(BankAccount receiversBankAccount, BigDecimal amountToSend) {
        return this.uatmOperation.executeOperation(receiversBankAccount, amountToSend);
    }
}
