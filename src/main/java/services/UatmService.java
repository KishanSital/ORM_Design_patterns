package services;

import designpatterns.creational.builder.entities.bank.BankCard;
import designpatterns.creational.builder.entities.uatm.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface UatmService {
    List<Transaction> getAllTransactions();

    void clearTransactionLog();

    BigDecimal getAccountBalance(Long accountNumber);

    BankCard getBankCard();

    void displayAllAccountBalance();

    void withDrawMoney(BigDecimal amountToWithdraw);

    void transferMoney(BigDecimal amountToTransfer);

    void logout();

    BankCard getBankCardByBankAndCardNumberAndBankPin(String bank, Long cardNumber, Long bankPin);
}
