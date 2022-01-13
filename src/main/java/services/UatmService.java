package services;

import designpatterns.creational.builder.entities.bank.BankAccount;
import designpatterns.creational.builder.entities.bank.BankCard;
import designpatterns.creational.builder.entities.uatm.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface UatmService {
    List<Transaction> getAllTransactions();

    int clearTransactionLog();

    BigDecimal getAccountBalance(Long accountNumber);

    BankCard getBankCard();

    List<BankAccount> getAllAccountByCardNumber();

    BankAccount withDrawMoney(Long accountNumber, BigDecimal newBalance);

    void transferMoney(BigDecimal amountToTransfer);

    void logout();

    BankCard getBankCardByBankAndCardNumberAndBankPin(Long cardNumber, Long bankPin);

    Transaction createTransationLog(Long transactionAccountNumber,
                                    BigDecimal transactionAmount,
                                    String transactionDescription);
}
