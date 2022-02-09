package designpatterns.structural.adapter;

import com.github.KishanSital.authenticator.models.DatabaseInfo;
import designpatterns.creational.builder.entities.connection.Database;


public interface DatabaseInfoAdapter {
    DatabaseInfo getDatabaseInfo();

    Database getDatabase();
}
