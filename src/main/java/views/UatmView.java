package views;

import designpatterns.creational.builder.entities.bank.BankAccount;
import designpatterns.creational.builder.entities.bank.BankCard;
import serviceImpl.CardSessionServiceImpl;
import services.UatmService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import static com.github.KishanSital.authenticator.serviceImpl.TriesValidationServiceImpl.resetTriesService;
import static com.github.KishanSital.authenticator.serviceImpl.TriesValidationServiceImpl.triesValidation;


public class UatmView {

    private final UatmService uatmService;
    private Scanner scanner = new Scanner(System.in);


    public UatmView(UatmService uatmService) {
        this.uatmService = uatmService;
    }

    public void viewTransactions() {
        if (!uatmService.getAllTransactions().isEmpty()) {
            for (int transactionNumber = 0; transactionNumber < uatmService.getAllTransactions().size(); transactionNumber++) {

                System.out.println(transactionNumber + 1 + ". " + uatmService.getAllTransactions().toArray()[transactionNumber]);
            }
        } else {
            System.out.println("\nYour transaction log is empty\n");
        }


    }

    public void clearTransactionLog() {
        int totalDeletedTransactions = uatmService.clearTransactionLog();
        System.out.println("\n" + (totalDeletedTransactions == 1 ? "1 Record has " : totalDeletedTransactions + " Records have ") + "been removed from the transaction log.\n");
    }

    public void viewBalance() {
        verifyCardSession();
        displayBalance();
    }

    private void verifyCardSession() {
        boolean isCardSessionExistent = CardSessionServiceImpl.bankCard != null;
        if (!isCardSessionExistent) {
            createNewCardSession();
        }
    }

    private BankCard createNewCardSession() {
        resetTriesService();
        boolean isCardValid = false;
        Integer selectedBank;
        boolean isSelectedBankValid;
        do {
            System.out.println("Please select your bank\n");
            displayBankOptions();
            try {
                selectedBank = scanner.nextInt();

            } catch (InputMismatchException e) {
                triesValidation();
                isSelectedBankValid = false;
                scanner.next();
                continue;
            }

            isSelectedBankValid = uatmService.getBankOptions().keySet().contains(selectedBank);

            if (!isSelectedBankValid) {
                System.out.println("Sorry, we don't provide any services for that selection yet.");
                triesValidation();
            } else {
                CardSessionServiceImpl.selectedBank = uatmService.getBankOptions().get(selectedBank);
                resetTriesService();
                long cardNumber;
                long bankPin;

                do {
                    try {
                        System.out.println("Please provide your card number");
                        cardNumber = scanner.nextLong();
                        System.out.println("Please provide your card pin");
                        bankPin = scanner.nextLong();
                        BankCard bankCard = uatmService.getBankCardByBankAndCardNumberAndBankPin(cardNumber, bankPin);

                        if (bankCard != null) {
                            isCardValid = true;
                            CardSessionServiceImpl.bankCard = bankCard;
                        }


                    } catch (InputMismatchException e) {
                        triesValidation();
                        isCardValid = false;
                        scanner.next();
                        continue;
                    }

                    if (!isCardValid) {
                        System.out.println("\nSomething went wrong please try again");
                        triesValidation();

                    } else {
                        return CardSessionServiceImpl.bankCard;
                    }
                } while (!isCardValid);
            }
        } while (!isSelectedBankValid);
        return null;
    }

    private BankAccount displayBalance() {
        boolean isAccountNumberValid;
        Long accountNumber;
        resetTriesService();
        do {
            System.out.println("Please enter your account number");
            try {
                accountNumber = scanner.nextLong();
            } catch (InputMismatchException e) {
                triesValidation();
                isAccountNumberValid = false;
                scanner.next();
                continue;
            }

            Long finalAccountNumber = accountNumber;
            isAccountNumberValid = uatmService.getAllAccountByCardNumber().stream().anyMatch(bankAccount -> bankAccount.getAccountNumber().equals(finalAccountNumber));

            if (!isAccountNumberValid) {
                System.out.println("This account number does not exist");
                triesValidation();
            }

            if (isAccountNumberValid) {
                BankAccount bankAccount = uatmService.getAllAccountByCardNumber().stream().filter(bA -> bA.getAccountNumber().equals(finalAccountNumber)).findFirst().get();
                printBalance(bankAccount);
                System.out.println();
                return bankAccount;
            }

        } while (!isAccountNumberValid);
        return null;
    }

    private BankAccount retrieveBankAccount(String bank) {
        boolean isAccountNumberValid;
        Long accountNumber;
        resetTriesService();
        do {
            System.out.println("Please enter the account number you'd like to send money to");
            try {
                accountNumber = scanner.nextLong();
            } catch (InputMismatchException e) {
                triesValidation();
                isAccountNumberValid = false;
                scanner.next();
                continue;
            }

            Long finalAccountNumber = accountNumber;
            isAccountNumberValid = uatmService.getAllAccountByCardNumber(bank).stream().anyMatch(bankAccount -> bankAccount.getAccountNumber().equals(finalAccountNumber));

            if (!isAccountNumberValid) {
                System.out.println("This account number does not exist");
                triesValidation();
            }

            if (isAccountNumberValid) {
                BankAccount bankAccount = uatmService.getAllAccountByCardNumber(bank).stream().filter(bA -> bA.getAccountNumber().equals(finalAccountNumber)).findFirst().get();
                return bankAccount;
            }

        } while (!isAccountNumberValid);
        return null;
    }

    public void viewBalanceForAllAccounts() {
        verifyCardSession();
        displayBalanceForAllBankUserAccounts();
        System.out.println();

    }

    private void displayBalanceForAllBankUserAccounts() {
        uatmService.getBankCardByBankAndCardNumberAndBankPin(CardSessionServiceImpl.bankCard.getCardNumber(),
                CardSessionServiceImpl.bankCard.getBankPin()).getBankAccounts().forEach(this::printBalance
        );
    }

    private void printBalance(BankAccount bankAccount) {
        String message = "Your balance for account type " + (bankAccount.getBankAccountType().getBankAccountTypeDescription().equalsIgnoreCase("spaar") ? "SPAAR" : "GIRO") + " is " + bankAccount.getBankCurrency().getCurrencyCode() + " " + NumberFormat.getCurrencyInstance().format(bankAccount.getBankBalance()).replace("\u20AC", "");
        System.out.println(message);
    }


    public void withdrawMoney() {
        verifyCardSession();
        BankAccount bankAccount = displayBalance();
        BigDecimal amountToWithdraw;
        boolean isAmountSufficient;
        BigDecimal balanceAfterWithdrawal;
        resetTriesService();
        do {
            System.out.println("Please enter a non-decimal amount which you'd like to withdraw from the above mentioned account");
            try {
                System.out.print(bankAccount.getBankCurrency().getCurrencyCode() + " ");
                amountToWithdraw = scanner.nextBigDecimal();
            } catch (InputMismatchException e) {
                triesValidation();
                isAmountSufficient = false;
                scanner.next();
                continue;
            }

            balanceAfterWithdrawal = bankAccount.getBankBalance().subtract(amountToWithdraw);
            isAmountSufficient = balanceAfterWithdrawal.compareTo(BigDecimal.valueOf(0)) >= 0;

            if (!isAmountSufficient) {
                System.out.println("The given amount of " + bankAccount.getBankCurrency().getCurrencyCode() + " " + amountToWithdraw + " is more than your current balance");
                triesValidation();
            }

            if (isAmountSufficient) {

                bankAccount = uatmService.withDrawMoney(bankAccount.getAccountNumber(), balanceAfterWithdrawal);

                uatmService.createTransationLog(bankAccount.getAccountNumber(), amountToWithdraw, "money has been withdrawn from local UATM machine\n\twithdrawal currency = " + bankAccount.getBankCurrency().getCurrencyCode());

                printBalance(bankAccount);
                System.out.println();
            }

        } while (!isAmountSufficient);


    }

    public void transferMoney() {
        verifyCardSession();
        BankAccount sendersBankAccount = displayBalance();
        BankAccount receiversBankAccount = null;
        BigDecimal amountToSend;
        boolean isAmountSufficient;
        BigDecimal sendersBalanceAfterWithdrawal;
        resetTriesService();
        Integer selectedBank;
        boolean isSelectedBankValid;

        do {
            System.out.println("Please select the destination bank\n");
            displayBankOptions();
            try {
                selectedBank = scanner.nextInt();

            } catch (InputMismatchException e) {
                triesValidation();
                isSelectedBankValid = false;
                scanner.next();
                continue;
            }

            isSelectedBankValid = uatmService.getBankOptions().keySet().contains(selectedBank);

            if (!isSelectedBankValid) {
                System.out.println("Sorry, we don't provide any services for that selection yet.");
                triesValidation();
            } else {

                receiversBankAccount = retrieveBankAccount(uatmService.getBankOptions().get(selectedBank));

                do {
                    System.out.println("Please enter a non-decimal amount which you'd like to transfer from the above mentioned account");
                    try {
                        System.out.print(sendersBankAccount.getBankCurrency().getCurrencyCode() + " ");
                        amountToSend = scanner.nextBigDecimal();
                    } catch (InputMismatchException e) {
                        triesValidation();
                        isAmountSufficient = false;
                        scanner.next();
                        continue;
                    }

                    sendersBalanceAfterWithdrawal = sendersBankAccount.getBankBalance().subtract(amountToSend);
                    isAmountSufficient = sendersBalanceAfterWithdrawal.compareTo(BigDecimal.valueOf(0)) >= 0;

                    if (!isAmountSufficient) {
                        System.out.println("The given amount of " + sendersBankAccount.getBankCurrency().getCurrencyCode() + " " + amountToSend + " is more than your current balance");
                        triesValidation();
                    }

                    if (isAmountSufficient) {

                        sendersBankAccount = uatmService.withDrawMoney(sendersBankAccount.getAccountNumber(), sendersBalanceAfterWithdrawal);

                        receiversBankAccount = uatmService.transferMoney(sendersBankAccount, receiversBankAccount, amountToSend, selectedBank);
                        System.out.println("Transaction was successful");
                        uatmService.createTransationLog(sendersBankAccount.getAccountNumber(), amountToSend, "money has been transferred to " + uatmService.getBankOptions().get(selectedBank) + "-" + receiversBankAccount.getBankCurrency().getCurrencyCode() + "-" + receiversBankAccount.getAccountNumber() + "\n\ttransaction currency = " + sendersBankAccount.getBankCurrency().getCurrencyCode());

                        printBalance(sendersBankAccount);
                        System.out.println();
                    }

                } while (!isAmountSufficient);
            }
        } while (!isSelectedBankValid);
    }

    private void displayBankOptions() {
        for (var entrySet : uatmService.getBankOptions().entrySet()) {
            System.out.println("Type " + entrySet.getKey() + " for " + entrySet.getValue());
        }
    }

}
