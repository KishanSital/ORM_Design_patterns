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
import serviceImpl.UatmServiceImpl;
import serviceImpl.UatmSessionServiceImpl;
import views.LoggedInMenuView;
import views.UatmView;

import java.util.HashMap;
import java.util.Map;

public class Application {

    public static void main(String[] args) {

        Map<String, Double> overmaakKoersMap = new HashMap<>();
        overmaakKoersMap.put("overmaakKoersUSD", 23.00);
        overmaakKoersMap.put("overmaakKoersEURO", 24.00);

        // factory pattern used
        Map<String, JPAConfiguration> JPAConfigurationMap = new HashMap<>();
        JPAConfigurationMap.put("UATM", new JPAConfigurationFactory().getJPAConfiguration("UATM"));
        JPAConfigurationMap.put("CBVS", new JPAConfigurationFactory().getJPAConfiguration("CBVS"));
        JPAConfigurationMap.put("DSB", new JPAConfigurationFactory().getJPAConfiguration("DSB"));
        JPAConfigurationMap.put("HKB", new JPAConfigurationFactory().getJPAConfiguration("HKB"));
               /*

        JPAConfiguration jpaConfigurationCBVS = new JPAConfiguration("CBVS");
        BankDAO bankDAOCBVS = new BankDAO(jpaConfigurationCBVS.getEntityManager());
        List<BankClient> bankClientListCBVS = bankDAOCBVS.retrieveClientList();
        bankClientListCBVS.forEach(System.out::println);*/


        UatmDAO uatmDAO = new UatmDAO(JPAConfigurationMap.get("UATM").getEntityManager());

        StringUtilsMyPackage.displayWelcomeMessage();

        // adapter pattern used
        User user = uatmDAO.findUserByUsername("kishan");
        UatmSessionServiceImpl.user = user;
        UserModelAdapter userModelAdapter = new UserModelAdapterImpl(user);
        UserModel expectedUser = userModelAdapter.getUserModel();
        //

        MyPackageApplication.startLoginService(expectedUser);


        Map<Integer, String> bankOptions = Map.of(1, "DSB", 2, "CBVS", 3, "HKB");
        UatmServiceImpl uatmService = new UatmServiceImpl(JPAConfigurationMap, uatmDAO);

        var uatmView = new UatmView(uatmService, bankOptions);
        var loggedInMenuView = new LoggedInMenuView(overmaakKoersMap, uatmView);
        loggedInMenuView.displayMenu();
    }
}
