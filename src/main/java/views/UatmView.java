package views;

import designpatterns.creational.builder.entities.bank.BankAccount;
import designpatterns.creational.builder.entities.bank.BankCard;
import serviceImpl.CardSessionServiceImpl;
import services.UatmService;

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
        if(!uatmService.getAllTransactions().isEmpty()){
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

        if (CardSessionServiceImpl.bankCard == null) {
            resetTriesService();
            boolean isCardValid = false;
            Integer selectedBank;
            boolean isSelectedBankValid;
            boolean isAccountNumberValid = false;
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

                                Long finalAccountNumber= accountNumber;
                                isAccountNumberValid = CardSessionServiceImpl.bankCard.getBankAccounts().stream().anyMatch(bankAccount -> bankAccount.getAccountNumber().equals(finalAccountNumber));

                                if (!isAccountNumberValid) {
                                    System.out.println("This account number does not exist");
                                    triesValidation();
                                }

                                if (isAccountNumberValid) {
                                    BankAccount bankAccount = CardSessionServiceImpl.bankCard.getBankAccounts().stream().filter(bA -> bA.getAccountNumber().equals(finalAccountNumber)).findFirst().get();
                                    System.out.println("Your balance is " + bankAccount.getBankCurrency().getCurrencyCode() + " " + bankAccount.getBankBalance());
                                }

                            } while (!isAccountNumberValid);

                        }
                    } while (!isCardValid);
                }
            } while (!isSelectedBankValid);
        } else {


        }


    }

    public void viewBalanceForAllAccounts() {

    }

    public void withdrawMoney() {

    }

    public void transferMoney() {

    }

    public void logout() {

    }
}
