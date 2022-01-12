package views;

import designpatterns.creational.builder.entities.bank.BankAccount;
import designpatterns.creational.builder.entities.bank.BankCard;
import serviceImpl.CardSessionServiceImpl;
import services.UatmService;

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

                System.out.println(transactionNumber + ". " + uatmService.getAllTransactions().toArray()[transactionNumber]);
            }
        }

        System.out.println("\nYour transaction log is empty\n");

    }

    public void clearTransactionLog() {
        uatmService.clearTransactionLog();
        System.out.println("\nTransaction log has been cleared.");
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
                resetTriesService();
                long cardNumber;
                long bankPin;

                do {
                    try {
                        System.out.println("Please provide your card number");
                        cardNumber = scanner.nextLong();
                        System.out.println("Please provide your card pin");
                        bankPin = scanner.nextLong();
                        BankCard bankCard = uatmService.getBankCardByBankAndCardNumberAndBankPin(bankOptions.get(selectedBank), cardNumber, bankPin);

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
                        System.out.println("Something went wrong please try again");
                        triesValidation();

                    } else {
                        return CardSessionServiceImpl.bankCard;
                    }
                } while (!isCardValid);
            }
        } while (!isSelectedBankValid);
        return null;
    }

    private void displayBalance() {
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
            isAccountNumberValid = CardSessionServiceImpl.bankCard.getBankAccounts().stream().anyMatch(bankAccount -> bankAccount.getAccountNumber().equals(finalAccountNumber));

            if (!isAccountNumberValid) {
                System.out.println("This account number does not exist");
                triesValidation();
            }

            if (isAccountNumberValid) {
                BankAccount bankAccount = CardSessionServiceImpl.bankCard.getBankAccounts().stream().filter(bA -> bA.getAccountNumber().equals(finalAccountNumber)).findFirst().get();
                System.out.println("Your balance is for account type " + bankAccount.getBankAccountType().getBankAccountTypeDescription() + " is " + NumberFormat.getCurrencyInstance().format(bankAccount.getBankCurrency().getCurrencyCode()) + " " + bankAccount.getBankBalance() + "\n");
            }

        } while (!isAccountNumberValid);
    }

    public void viewBalanceForAllAccounts() {
        verifyCardSession();
        displayBalanceForAllBankUserAccounts();

    }

    private void displayBalanceForAllBankUserAccounts() {
        CardSessionServiceImpl.bankCard.getBankAccounts().parallelStream().forEach(bankAccount ->
                System.out.println("Your balance for account type " + bankAccount.getBankAccountType().getBankAccountTypeDescription() + " is " + bankAccount.getBankCurrency().getCurrencyCode() + " " + bankAccount.getBankBalance() + "\n")
        );
    }

    public void withdrawMoney() {

    }

    public void transferMoney() {

    }

    public void logout() {

    }
}
