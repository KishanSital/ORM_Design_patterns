package mypackage.services;

import mypackage.annotations.Service;

import static mypackage.serviceImpl.TriesValidationServiceImpl.resetTriesService;

@Service
public abstract interface MenuService {
    // none of these abstract methods are required to be implement and overridden
    String toString();
    int hashCode();
    boolean equals(Object object);
    default void menuOptions() {}
    default void displayMenu() {}
    default void init() {}
    default void printOutMenuOptions(){};

    default void resetAllValidationServices() {
        reset();
    }
    private static void reset(){
        resetTriesService();
    }

}