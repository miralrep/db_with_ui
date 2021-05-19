package ru.miral.dbwithui.dao;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * todo Document type Repository
 */
public class Repository {
    final String URL  = "jdbc:postgresql://localhost:5433/miral_db";
    final String USER = "dbuser";
    final String PASSWORD = "dbpassword";

    Connection connection;


    public Repository(){

    }





}
