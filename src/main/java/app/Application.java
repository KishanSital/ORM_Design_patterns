package app;

import com.github.KishanSital.authenticator.application.MyPackageApplication;
import com.github.KishanSital.authenticator.models.DatabaseInfo;
import com.github.KishanSital.authenticator.models.UserModel;
import com.github.KishanSital.authenticator.utils.StringUtilsMyPackage;
import dao.UatmDAO;
import designpatterns.creational.builder.entities.connection.Database;
import designpatterns.creational.builder.entities.uatm.User;
import designpatterns.creational.factory.JPAConfiguration;
import designpatterns.creational.factory.JPAConfigurationFactory;
import designpatterns.structural.adapter.DatabaseInfoAdapter;
import designpatterns.structural.adapter.DatabaseInfoAdapterImpl;
import serviceImpl.UatmServiceImpl;
import serviceImpl.UatmSessionServiceImpl;
import views.LoggedInMenuView;
import views.UatmView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class Application {

    public static void main(String[] args) {

        Locale.setDefault(new Locale.Builder().setLanguage("nl").setRegion("NL").build());

        Map<String, BigDecimal> overmaakKoersMap = new HashMap<>();
        overmaakKoersMap.put("USD->SRD", BigDecimal.valueOf(2.00));
        overmaakKoersMap.put("EURO->SRD", BigDecimal.valueOf(4.00));

        // factory pattern used
        Map<String, JPAConfiguration> JPAConfigurationMap = new HashMap<>();
        JPAConfigurationMap.put("UATM", new JPAConfigurationFactory().getJPAConfiguration("UATM"));
        JPAConfigurationMap.put("CBVS", new JPAConfigurationFactory().getJPAConfiguration("CBVS"));
        JPAConfigurationMap.put("DSB", new JPAConfigurationFactory().getJPAConfiguration("DSB"));
        JPAConfigurationMap.put("HKB", new JPAConfigurationFactory().getJPAConfiguration("HKB"));

        UatmDAO uatmDAO = new UatmDAO(JPAConfigurationMap.get("UATM").getEntityManager());

        StringUtilsMyPackage.displayWelcomeMessage();

        // adapter pattern used
        Database database = new Database.DatabaseBuilder()
                .dbUrl("jdbc:mysql://localhost/uatm")
                .dbUser("Kishan")
                .dbPassword("Kishan")
                .tableName("user")
                .userNameColumn("username")
                .passwordColumn("password")
                .build();

        DatabaseInfoAdapter databaseInfoAdapter = new DatabaseInfoAdapterImpl(database);
        DatabaseInfo databaseInfo = databaseInfoAdapter.getDatabaseInfo();
        //

        UserModel userModel = MyPackageApplication.startLoginService(databaseInfo);
        User user = uatmDAO.findUserByUsername(userModel.getUsername());
        UatmSessionServiceImpl.user = user;


        Map<Integer, String> bankOptions = new LinkedHashMap();
        bankOptions.put(1, "DSB");
        bankOptions.put(2, "CBVS");
        bankOptions.put(3, "HKB");
        UatmServiceImpl uatmService = new UatmServiceImpl(JPAConfigurationMap, uatmDAO, bankOptions, overmaakKoersMap);

        UatmView uatmView = new UatmView(uatmService);
        LoggedInMenuView loggedInMenuView = new LoggedInMenuView(uatmView);
        loggedInMenuView.displayMenu();
    }
}
