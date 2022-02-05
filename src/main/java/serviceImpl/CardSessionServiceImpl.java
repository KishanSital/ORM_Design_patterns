package serviceImpl;

import designpatterns.creational.builder.entities.bank.BankCard;
import services.CardSessionService;

public class CardSessionServiceImpl implements CardSessionService {
    public static BankCard bankCard;
    public static String selectedBank;
    public static Long currentAccountNumber;

    public CardSessionServiceImpl(BankCard bankCard) {
        this.bankCard = bankCard;
    }
}
