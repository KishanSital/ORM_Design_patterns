package views;



import com.github.KishanSital.authenticator.services.MenuService;
import com.github.KishanSital.authenticator.utils.IntUtilsMyPackage;
import com.github.KishanSital.authenticator.utils.StringUtilsMyPackage;

import java.util.*;

import static com.github.KishanSital.authenticator.serviceImpl.TriesValidationServiceImpl.triesValidation;


public class LoggedInMenuView implements MenuService, Cloneable {
    private static List<String> menuOptionsList;
    private final int exitCode = 7;
    private Scanner scanner;
    private UatmView uatmView;

    public LoggedInMenuView(UatmView uatmView) {
        this.uatmView = uatmView;
        init();
    }


    @Override
    public void init() {
        scanner = new Scanner(System.in);
        resetAllValidationServices();
    }

    @Override
    public void displayMenu() {
        int choiceEntry = 0;

        do {
            NestedStaticMenuOptions.menuOptions();
            try {
                choiceEntry = scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.next();
                triesValidation();
                System.out.println(StringUtilsMyPackage.INVALID_NUMBER_MESSAGE.getStringValue());
                continue;
            }
            switch (choiceEntry) {
                case 1:
                    resetAllValidationServices();
                    uatmView.viewTransactions();
                    break;
                case 2:
                    resetAllValidationServices();
                    uatmView.clearTransactionLog();
                    break;
                case 3:
                    resetAllValidationServices();
                    uatmView.viewBalance();
                    break;
                case 4:
                    resetAllValidationServices();
                    uatmView.viewBalanceForAllAccounts();
                    break;
                case 5:
                    resetAllValidationServices();
                    uatmView.withdrawMoney();
                    break;
                case 6:
                    resetAllValidationServices();
                    uatmView.transferMoney();
                    break;
                case 7:
                    resetAllValidationServices();
                    System.out.println(StringUtilsMyPackage.LOGGED_OUT_MESSAGE.getStringValue());
                    System.exit(0);
                    break;
                default:
                    triesValidation();
                    System.out.println(StringUtilsMyPackage.STARTING_MENU_OPTIONS_MESSAGE.getStringValue()
                            + exitCode + "\n");

            }

        }
        while (choiceEntry != exitCode);
    }

    private static class NestedStaticMenuOptions {
        public static void menuOptions() {
            menuOptionsList = new ArrayList<>();
            menuOptionsList.add("Welcome to your very own Universal ATM machine.");
            menuOptionsList.add("to view all your transactions");
            menuOptionsList.add("to clear complete transaction log");
            menuOptionsList.add("to view your balance");
            menuOptionsList.add("to view balance of all your accounts");
            menuOptionsList.add("to withdraw money");
            menuOptionsList.add("to transfer money");
            menuOptionsList.add("to Log out and exit UATM");
            printOutMenuOptions();
        }

        private static void printOutMenuOptions() {
            for (int i = 0; i < menuOptionsList.size(); i++) {
                if (i == IntUtilsMyPackage.QUESTION_MESSAGE_INDEX.getIntValue()) {
                    System.out.println(menuOptionsList.get(i));
                    continue;
                } else {
                    System.out.println("Type " + i + " " + menuOptionsList.get(i));
                }
            }
        }
    }
}
