package app;

import config.JPAConfiguration;
import dao.BankDAO;
import entities.bank.BankClient;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        JPAConfiguration jpaConfigurationUATM = new JPAConfiguration("UATM");

        JPAConfiguration jpaConfigurationCBVS = new JPAConfiguration("CBVS");
        BankDAO bankDAOCBVS = new BankDAO(jpaConfigurationCBVS.getEntityManager());
        List<BankClient> bankClientListCBVS = bankDAOCBVS.retrieveClientList();
        bankClientListCBVS.forEach(System.out::println);

    }
}
