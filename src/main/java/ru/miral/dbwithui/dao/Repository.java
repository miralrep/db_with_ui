package ru.miral.dbwithui.dao;

import ru.miral.dbwithui.model.entities.*;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * todo Document type Repository
 */
public class Repository {
    final String URL = "jdbc:postgresql://localhost:5432/miral_db";
    final String USER = "dbuser";
    final String PASSWORD = "dbpassword";

    public Repository() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection tryConnection = DriverManager.getConnection(URL, USER, PASSWORD);
        tryConnection.close();
        initPrivileges();
        initCallTypes();
    }

    public Set<Subscriber> getAllSubscribers() {
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

                subscriber.setPrivileges(getSubscribersPrivilegesById(subscriber.getId()));
                subscriber.setPhoneNumbers(getSubscribersPhoneNumbersById(subscriber.getId()));

                subscribers.add(subscriber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscribers;
    }

    public Subscriber getSubscriberById(int id) {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement getSubscriberStatement = connection
            .prepareStatement("" +
                "SELECT id, surname, name, patronymic, address" +
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

                    subscriber.setPhoneNumbers(getSubscribersPhoneNumbersById(subscriber.getId()));
                    subscriber.setPrivileges(getSubscribersPrivilegesById(subscriber.getId()));

                    return subscriber;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public Set<PhoneNumber> getSubscribersPhoneNumbersById(int id) {
        Set<PhoneNumber> phoneNumbers = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement preparedStatement = connection
            .prepareStatement("" +
                "SELECT pn.number, pn.category_id FROM phone_number pn " +
                "WHERE subscriber_id = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet phoneNumberSet = preparedStatement.executeQuery()) {
                {
                    while (phoneNumberSet.next()) {
                        PhoneNumber phoneNumber = new PhoneNumber();
                        try {
                            phoneNumber.setNumber(phoneNumberSet.getString("pn.number"));
                            phoneNumber.setCategory(getCategoryById(phoneNumberSet.getInt("pn.category_id")));
                            phoneNumbers.add(phoneNumber);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        return phoneNumbers;
    }

    public Set<Privilege> getSubscribersPrivilegesById(int id) {
        Set<Privilege> privileges = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("" +
                    "SELECT p.name FROM subscriber s" +
                    " JOIN subscriber_privilege sp ON s.id = sp.subscriber_id" +
                    " JOIN privilege p on sp.privilege_id = p.id WHERE s.id = ?");
            ) {
                preparedStatement.setInt(1, id);
                try (ResultSet privilegeSet = preparedStatement.executeQuery()) {
                    while (privilegeSet.next()) {
                        try {
                            privileges
                                .add(Privilege.getPrivilegeByName(privilegeSet.getString("name")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return privileges;
    }

    //public Set<Conversation> get

    public Category getCategoryById(int id) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("" +
                    "SELECT c.id, c.name, c.subscription_fee, " +
                    "ct.name, cctf.fee FROM category c " +
                    "JOIN category_call_type_fee cctf ON c.id = cctf.category_id " +
                    "JOIN call_type ct ON cctf.call_type_id = ct.id " +
                    "WHERE c.id = ?");

            ) {
                preparedStatement.setInt(1, id);
                try (ResultSet categorySet = preparedStatement.executeQuery()) {
                    if (categorySet.next()) {
                        Category category = new Category();
                        category.setId(categorySet.getInt("id"));
                        category.setName(categorySet.getString("c.name"));
                        category.setSubscriptionFee(categorySet.getDouble("subscription_fee"));
                        category.setFeeByCallType(
                            categorySet.getString("ct.name"),
                            categorySet.getDouble("fee"));

                        while (categorySet.next()) {
                            category.setFeeByCallType(
                                categorySet.getString("ct.name"),
                                categorySet.getDouble("fee"));
                        }

                        return category;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Category getCategoryByName(String name) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("" +
                    "SELECT c.id, c.name, c.subscription_fee, " +
                    "ct.name, cctf.fee FROM category c " +
                    "JOIN category_call_type_fee cctf ON c.id = cctf.category_id " +
                    "JOIN call_type ct ON cctf.call_type_id = ct.id " +
                    "WHERE c.name = ?");

            ) {
                preparedStatement.setString(1, name);
                try (ResultSet categorySet = preparedStatement.executeQuery()) {
                    if (categorySet.next()) {
                        Category category = new Category();
                        category.setId(categorySet.getInt("id"));
                        category.setName(categorySet.getString("c.name"));
                        category.setSubscriptionFee(categorySet.getDouble("subscription_fee"));
                        category.setFeeByCallType(
                            categorySet.getString("ct.name"),
                            categorySet.getDouble("fee"));

                        while (categorySet.next()) {
                            category.setFeeByCallType(
                                categorySet.getString("ct.name"),
                                categorySet.getDouble("fee"));
                        }

                        return category;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getPrivilegeId(Privilege privilege) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement getPrivilegeIdStatement = connection.prepareStatement("" +
                 "SELECT id FROM privilege WHERE name = ?");
        ) {
            getPrivilegeIdStatement.setString(1, privilege.getName());
            ResultSet resultSet = getPrivilegeIdStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getCallTypeId(CallType callType) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement getCallTypeByIdStatement = connection.prepareStatement("" +
                 "SELECT id FROM call_type WHERE name = ?")) {

            getCallTypeByIdStatement.setString(1, callType.getName());
            ResultSet resultSet = getCallTypeByIdStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void saveSubscriber(Subscriber subscriber) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement saveSubscriberStatement = connection
                 .prepareStatement("" +
                     "INSERT INTO subscriber(surname, name, patronymic, address) " +
                     "VALUES (?, ?, ?, ?)")) {

            saveSubscriberStatement.setString(1, subscriber.getSurname());
            saveSubscriberStatement.setString(2, subscriber.getName());
            saveSubscriberStatement.setString(3, subscriber.getPatronymic());
            saveSubscriberStatement.setString(4, subscriber.getAddress());
            saveSubscriberStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void saveSubscriberPrivileges(Subscriber subscriber) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement saveSubscriberPrivileges = connection.prepareStatement("" +
                 "INSERT INTO subscriber_privilege(subscriber_id, privilege_id) " +
                 "VALUES (?, ?)");
        ) {
            for (Privilege privilege : subscriber.getPrivileges()) {
                saveSubscriberPrivileges.setInt(1, subscriber.getId());
                saveSubscriberPrivileges.setInt(1, getPrivilegeId(privilege));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCategory(Category category) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement saveCategoryStatement = connection.prepareStatement("" +
                 "INSERT INTO category(name, subscription_fee) " +
                 "VALUES (?, ?)")
        ) {
            saveCategoryStatement.setString(1, category.getName());
            saveCategoryStatement.setDouble(2, category.getSubscriptionFee());
            saveCategoryStatement.executeUpdate();

            saveCategoryCallTypeFee(category);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCategoryCallTypeFee(Category category) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            for (CallType callType : category.getFees().keySet()
            ) {
                try (PreparedStatement saveCallTypeFeeStatement = connection.prepareStatement("" +
                    "INSERT INTO category_call_type_fee(category_id, call_type_id, fee) " +
                    "VALUES (?, ?, ?)")) {
                    Category categoryDb = getCategoryByName(category.getName());
                    saveCallTypeFeeStatement.setInt(1, categoryDb.getId());
                    saveCallTypeFeeStatement.setInt(2, getCallTypeId(callType));
                    saveCallTypeFeeStatement.setDouble(3, category.getFees().get(callType));
                    saveCallTypeFeeStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
