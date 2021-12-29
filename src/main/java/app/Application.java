package app;

import config.JPAConfiguration;
import dao.UatmDAO;
import entities.uatm.User;
import mypackage.application.MyPackageApplication;
import mypackage.models.UserModel;
import mypackage.services.UserModelWithArguments;
import mypackage.utils.StringUtilsMyPackage;
import structural.adapter.UserModelAdapter;
import structural.adapter.UserModelAdapterImpl;

public class Application {
    public static void main(String[] args) {
               /*

        JPAConfiguration jpaConfigurationCBVS = new JPAConfiguration("CBVS");
        BankDAO bankDAOCBVS = new BankDAO(jpaConfigurationCBVS.getEntityManager());
        List<BankClient> bankClientListCBVS = bankDAOCBVS.retrieveClientList();
        bankClientListCBVS.forEach(System.out::println);*/

        JPAConfiguration jpaConfigurationUATM = new JPAConfiguration("UATM");
        UatmDAO uatmDAO = new UatmDAO(jpaConfigurationUATM.getEntityManager());
        User user = uatmDAO.findUserByUsername("kishan");

        UserModelAdapter userModelAdapter = new UserModelAdapterImpl(user);

        StringUtilsMyPackage.displayWelcomeMessage();

        UserModel expectedUser = userModelAdapter.getUserModel();

        MyPackageApplication.startLoginService(expectedUser);


    }
}
