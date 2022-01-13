package views;

import designpatterns.creational.builder.entities.bank.BankAccount;
import designpatterns.creational.builder.entities.bank.BankCard;
import serviceImpl.CardSessionServiceImpl;
import services.UatmService;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import static com.github.KishanSital.authenticator.serviceImpl.TriesValidationServiceImpl.resetTriesService;
import static com.github.KishanSital.authenticator.serviceImpl.TriesValidationServiceImpl.triesValidation;


public class UatmView {

    private final UatmService uatmService;
    private final Map<Integer, String> bankOptions;
    private Scanner scanner = new Scanner(System.in);

    public UatmView(UatmService uatmService, Map<Integer, String> bankOptions) {
        this.uatmService = uatmService;
        this.bankOptions = bankOptions;
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
        System.out.println("\n" + (totalDeletedTransactions + totalDeletedTransactions == 1 ? " Record has " : " Records have ") + "been removed from the transaction log.\n");
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
            System.out.println("Please select your bank\nType 1 for DSB\nType 2 for CBVS\nType 3 for HKB");
            try {
                selectedBank = scanner.nextInt();

            } catch (InputMismatchException e) {
                triesValidation();
                isSelectedBankValid = false;
                scanner.next();
                continue;
            }

            isSelectedBankValid = bankOptions.keySet().contains(selectedBank);

            if (!isSelectedBankValid) {
                System.out.println("Sorry, we don't provide any services for this bank yet.");
                triesValidation();
            } else {
                CardSessionServiceImpl.selectedBank = bankOptions.get(selectedBank);
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
                return bankAccount;
            }

        } while (!isAccountNumberValid);
        return null;
    }

    public void viewBalanceForAllAccounts() {
        verifyCardSession();
        displayBalanceForAllBankUserAccounts();
        System.out.println();

        // TODO: issue bij data ophalen na update en strategy pattern implementeren

    }

    private void displayBalanceForAllBankUserAccounts() {
        CardSessionServiceImpl.bankCard.getBankAccounts().forEach(bankAccount ->
                printBalance(bankAccount)
        );
    }

    private void printBalance(BankAccount bankAccount) {
        String message = "Your balance for account type "  + (bankAccount.getBankAccountType().getBankAccountTypeDescription().equalsIgnoreCase("spaar") ? "SPAAR" : "GIRO") + " is " + bankAccount.getBankCurrency().getCurrencyCode() + " " + NumberFormat.getCurrencyInstance().format(bankAccount.getBankBalance());
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

                uatmService.createTransationLog(bankAccount.getAccountNumber(), amountToWithdraw, "money has been withdrawn from local UATM machine\n\tWithdrawal currency = " + bankAccount.getBankCurrency().getCurrencyCode());

                printBalance(bankAccount);
            }

        } while (!isAmountSufficient);


    }

    public void transferMoney() {

    }

    public void logout() {

    }
}
