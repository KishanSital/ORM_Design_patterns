package designpatterns.behavioral.strategy;

import designpatterns.creational.builder.entities.bank.BankAccount;

import java.math.BigDecimal;

public  interface UatmOperation {

    BigDecimal executeOperation(BankAccount receiversBankAccount, BigDecimal amountToSend);
}
