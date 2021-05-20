package ru.miral.dbwithui.dao;

import ru.miral.dbwithui.model.entities.CallType;
import ru.miral.dbwithui.model.entities.Privilege;
import ru.miral.dbwithui.model.entities.Subscriber;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * todo Document type Repository
 */
public class Repository {
    final String URL = "jdbc:postgresql://localhost:5432/miral_db";
    final String USER = "dbuser";
    final String PASSWORD = "dbpassword";

    public Repository() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        initPrivileges();
        initCallTypes();
    }

    Set<Subscriber> getAllSubscribers() {
        Set<Subscriber> subscribers = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement getAllSubscribersStatement = connection
                 .createStatement();
             ResultSet resultSet = getAllSubscribersStatement
                 .executeQuery("SELECT id, surname, name, patronymic, address FROM subscriber")) {

            while (resultSet.next()) {
                Subscriber subscriber = new Subscriber();
                subscriber.setId(resultSet.getInt("id"));
                subscriber.setSurname(resultSet.getString("surname"));
                subscriber.setName(resultSet.getString("name"));
                subscriber.setPatronymic(resultSet.getString("patronymic"));
                subscriber.setAddress(resultSet.getString("address"));
                try (PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT p.name FROM subscriber s" +
                        " JOIN subscriber_privilege sp ON s.id = sp.subscriber_id" +
                        " JOIN privilege p on sp.privilege_id = p.id WHERE s.id = ?");
                     ResultSet privilegeSet = preparedStatement.executeQuery()
                ) {
                    while (privilegeSet.next()) {
                        try {
                            subscriber
                                .addPrivilege(Privilege.getPrivilegeByName(privilegeSet.getString("name")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                subscribers.add(subscriber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscribers;
    }

    Subscriber getSubscriberById(int id) {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement getSubscriberStatement = connection
            .prepareStatement("SELECT id, surname, name, patronymic, address" +
                " FROM subscriber WHERE id = ?")) {
            getSubscriberStatement.setInt(1, id);
            try (ResultSet resultSet = getSubscriberStatement
                .executeQuery()) {
                if (resultSet.next()) {
                    Subscriber subscriber = new Subscriber();
                    subscriber.setId(resultSet.getInt("id"));
                    subscriber.setSurname(resultSet.getString("surname"));
                    subscriber.setName(resultSet.getString("name"));
                    subscriber.setPatronymic(resultSet.getString("patronymic"));
                    subscriber.setAddress(resultSet.getString("address"));
                    try (PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT p.name FROM subscriber s" +
                            " JOIN subscriber_privilege sp ON s.id = sp.subscriber_id" +
                            " JOIN privilege p on sp.privilege_id = p.id WHERE s.id = ?");
                         ResultSet privilegeSet = preparedStatement.executeQuery()
                    ) {
                        while (privilegeSet.next()) {
                            try {
                                subscriber
                                    .addPrivilege(Privilege.getPrivilegeByName(privilegeSet.getString("name")));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return subscriber;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    private void initPrivileges() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement insertPrivilege = connection
                 .prepareStatement("INSERT INTO privilege(name, discount) VALUES(?, ?) ");) {

            for (Privilege privilege : Privilege.values()) {
                insertPrivilege.setString(1, privilege.getName());
                insertPrivilege.setDouble(2, privilege.getDiscount());
                insertPrivilege.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initCallTypes() {
        try {

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement insertPrivilege = connection
                     .prepareStatement("INSERT INTO call_type(name) VALUES(?) ");) {

                for (CallType callType : CallType.values()) {
                    insertPrivilege.setString(1, callType.getName());
                    insertPrivilege.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
