package app;

import dao.UatmDAO;
import designpatterns.creational.builder.entities.uatm.User;
import designpatterns.creational.factory.JPAConfiguration;
import designpatterns.creational.factory.JPAConfigurationFactory;
import designpatterns.structural.adapter.UserModelAdapter;
import designpatterns.structural.adapter.UserModelAdapterImpl;
import mypackage.application.MyPackageApplication;
import mypackage.models.UserModel;
import mypackage.utils.StringUtilsMyPackage;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        double overmaakKoersUSD = 23.00;
        double overmaakKoersEURO = 24.00;
               /*

        JPAConfiguration jpaConfigurationCBVS = new JPAConfiguration("CBVS");
        BankDAO bankDAOCBVS = new BankDAO(jpaConfigurationCBVS.getEntityManager());
        List<BankClient> bankClientListCBVS = bankDAOCBVS.retrieveClientList();
        bankClientListCBVS.forEach(System.out::println);*/

        // factory pattern used
        JPAConfiguration jpaConfigurationUATM = new JPAConfigurationFactory().getJPAConfiguration("UATM");
        UatmDAO uatmDAO = new UatmDAO(jpaConfigurationUATM.getEntityManager());

        StringUtilsMyPackage.displayWelcomeMessage();

        // adapter pattern used
        User user = uatmDAO.findUserByUsername("kishan");
        UserModelAdapter userModelAdapter = new UserModelAdapterImpl(user);
        UserModel expectedUser = userModelAdapter.getUserModel();
        //

        MyPackageApplication.startLoginService(expectedUser);


    }
}
