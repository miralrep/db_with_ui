package ru.miral.dbwithui.dao;

import org.postgresql.util.PGInterval;
import ru.miral.dbwithui.model.entities.*;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
        initCategory();
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

    public Subscriber getSubscriberByPhoneNumber(PhoneNumber number){
        Subscriber subscriber = new Subscriber();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement preparedStatement = connection.prepareStatement("" +
            "SELECT subscriber_id FROM phone_number WHERE number = ?")) {
            preparedStatement.setString(1, number.getNumber());
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    subscriber = getSubscriberById(resultSet.getInt("subscriber_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscriber;
    }

    public Set<PhoneNumber> getSubscribersPhoneNumbersById(int id) {
        Set<PhoneNumber> phoneNumbers = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement preparedStatement = connection
            .prepareStatement("" +
                "SELECT pn.number AS pnnumber, pn.category_id AS pncategory_id FROM phone_number pn " +
                "WHERE subscriber_id = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet phoneNumberSet = preparedStatement.executeQuery()) {
                {
                    while (phoneNumberSet.next()) {
                        PhoneNumber phoneNumber = new PhoneNumber();
                        try {
                            phoneNumber.setNumber(phoneNumberSet.getString("pnnumber"));
                            phoneNumber.setCategory(getCategoryById(phoneNumberSet.getInt("pncategory_id")));
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

    public Set<Category> getAllCategories() {
        Set<Category> allCategories = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet resultAllCategories = statement.executeQuery("" +
                "SELECT id, name, subscription_fee FROM category");

            while (resultAllCategories.next()) {
                Category category = new Category();
                category.setId(resultAllCategories.getInt("id"));
                category.setName(resultAllCategories.getString("name"));
                category.setSubscriptionFee(resultAllCategories.getDouble("subscription_fee"));
                try (PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "SELECT fee FROM category_call_type_fee WHERE call_type_id = ? AND category_id = ?")) {
                    preparedStatement.setInt(2, category.getId());
                    category.setFees(new HashMap<>());
                    for (CallType callType : CallType.values()) {
                        preparedStatement.setInt(1, getCallTypeId(callType));
                        ResultSet resultSetFee = preparedStatement.executeQuery();
                        if (resultSetFee.next()) {
                            category.setFeeByCallType(callType.getName(), resultSetFee.getDouble("fee"));
                        }
                    }
                }
                allCategories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCategories;
    }

    public Set<PhoneNumber> getAllPhoneNumbers() {
        Set<PhoneNumber> phoneNumbers = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement getAllPhoneNumbersStatement = connection
                 .createStatement();
             ResultSet resultSet = getAllPhoneNumbersStatement.executeQuery("" +
                 "SELECT number, category_id FROM phone_number")) {
            while (resultSet.next()) {
                PhoneNumber phoneNumber = new PhoneNumber();
                phoneNumber.setNumber(resultSet.getString("number"));
                phoneNumber.setCategory(getCategoryById(resultSet.getInt("category_id")));
                phoneNumbers.add(phoneNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phoneNumbers;
    }

    public Set<Conversation> getPhoneNumberConversationsByMonth(PhoneNumber phoneNumber, LocalDateTime month) {
        Set<Conversation> conversations = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement phoneNumberConversations = connection
                 .prepareStatement("" +
                     "SELECT name, duration " +
                     "FROM conversation JOIN call_type ct on ct.id = conversation.call_type_id" +
                     " WHERE calling_phone = ? AND date BETWEEN ? and ?")) {
            phoneNumberConversations.setString(1, phoneNumber.getNumber());
            phoneNumberConversations.setObject(2, month);
            phoneNumberConversations.setObject(3, month.plusMonths(1));
                try(ResultSet resultSet = phoneNumberConversations.executeQuery()){

                    while (resultSet.next()){
                        Conversation conversation = new Conversation();
                        PGInterval pgInterval = (PGInterval)resultSet.getObject("duration");
                        Duration duration = Duration.ofMinutes(pgInterval.getMinutes());
                        conversation.setDuration(duration);
                        conversation.setCallType(CallType.getCallTypeByName(resultSet.getString("name")));

                        conversations.add(conversation);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conversations;
    }

    /*public Set<CallType> getAllCallTypes(){
        Set<CallType> allCallTypes = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("" +
                "SELECT id, name FROM call_type");

            while (resultSet.next()){
                CallType callType = new Category();
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCallTypes;
    }*/

    public Category getCategoryById(int id) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("" +
                    "SELECT c.id AS id, c.name AS cname, c.subscription_fee AS sub_fee, " +
                    "ct.name AS ctname, cctf.fee AS fee FROM category c " +
                    "JOIN category_call_type_fee cctf ON c.id = cctf.category_id " +
                    "JOIN call_type ct ON cctf.call_type_id = ct.id " +
                    "WHERE c.id = ?");

            ) {
                preparedStatement.setInt(1, id);
                try (ResultSet categorySet = preparedStatement.executeQuery()) {
                    if (categorySet.next()) {
                        Category category = new Category();
                        category.setId(categorySet.getInt("id"));
                        category.setName(categorySet.getString("cname"));
                        category.setSubscriptionFee(categorySet.getDouble("sub_fee"));
                        category.setFees(new HashMap<>());
                        category.setFeeByCallType(
                            categorySet.getString("ctname"),
                            categorySet.getDouble("fee"));

                        while (categorySet.next()) {
                            category.setFeeByCallType(
                                categorySet.getString("ctname"),
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
                    "SELECT id, name, subscription_fee FROM category WHERE name = ?");

            ) {
                preparedStatement.setString(1, name);
                try (ResultSet categorySet = preparedStatement.executeQuery()) {
                    if (categorySet.next()) {
                        Category category = new Category();
                        category.setId(categorySet.getInt("id"));
                        category.setName(categorySet.getString("name"));
                        category.setSubscriptionFee(categorySet.getDouble("subscription_fee"));

                        category.setFees(getCategoryCallTypeFees(category));

                        return category;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Map<CallType, Double> getCategoryCallTypeFees(Category category) {
        Map<CallType, Double> categoryCallTypeFees = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement categoryCallTypeFeesStatement = connection.prepareStatement("" +
                 "SELECT ct.name, cctf.fee FROM category_call_type_fee cctf " +
                 "JOIN call_type ct on cctf.call_type_id = ct.id " +
                 "WHERE category_id = ?")) {
            categoryCallTypeFeesStatement.setInt(1, category.getId());
            ResultSet resultSet = categoryCallTypeFeesStatement.executeQuery();
            while (resultSet.next()) {
                categoryCallTypeFees.put(
                    CallType.getCallTypeByName(resultSet.getString("name")),
                    resultSet.getDouble("fee"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryCallTypeFees;
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
            try (Statement getMaxIdStatement = connection.createStatement()) {
                ResultSet resultSet = getMaxIdStatement.executeQuery("SELECT MAX(id) FROM subscriber");
                if (resultSet.next()) {
                    subscriber.setId(resultSet.getInt("max"));
                    saveSubscriberPrivileges(subscriber);
                }
            }
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
                saveSubscriberPrivileges.setInt(2, getPrivilegeId(privilege));
                saveSubscriberPrivileges.executeUpdate();
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

    public void savePhoneNumber(PhoneNumber phoneNumber, Subscriber subscriber) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);) {
            try (PreparedStatement savePhoneStatement = connection.prepareStatement("" +
                "INSERT INTO phone_number(number, subscriber_id, category_id) " +
                "VALUES (?, ?, ?)")) {
                savePhoneStatement.setString(1, phoneNumber.getNumber());
                savePhoneStatement.setInt(2, subscriber.getId());
                savePhoneStatement.setInt(3, phoneNumber.getCategory().getId());

                savePhoneStatement.executeUpdate();
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
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement insertPrivilege = connection
            .prepareStatement("INSERT INTO call_type(name) VALUES(?) ")) {

            for (CallType callType : CallType.values()) {
                insertPrivilege.setString(1, callType.getName());
                insertPrivilege.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initCategory() {
        saveCategory(new Category(
            0,
            "Стандарт",
            0.00d,
            new HashMap<CallType, Double>() {{
                put(CallType.LOCAL, 5.00d);
                put(CallType.INTERCITY, 7.00d);
                put(CallType.INTERNATIONAL, 30.00d);
            }}
        ));

        saveCategory(new Category(
            0,
            "Безлимит",
            500.00d,
            new HashMap<CallType, Double>() {{
                put(CallType.LOCAL, 0.00d);
                put(CallType.INTERCITY, 0.00d);
                put(CallType.INTERNATIONAL, 30.00d);
            }}
        ));

        saveCategory(new Category(
            0,
            "Безлимит+",
            700.00d,
            new HashMap<CallType, Double>() {{
                put(CallType.LOCAL, 0.00d);
                put(CallType.INTERCITY, 0.00d);
                put(CallType.INTERNATIONAL, 0.00d);
            }}
        ));
    }

    public void saveConversation(
        String callingPhone, String takingPhone,
        CallType callType,
        LocalDateTime callDateTime, int duration) {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("" +
                "INSERT INTO conversation(calling_phone, taking_phone, call_type_id, duration, date) " +
                "VALUES (?, ?, ?, ?, ?)")) {
                preparedStatement.setString(1, callingPhone);
                preparedStatement.setString(2, takingPhone);
                preparedStatement.setInt(3, getCallTypeId(callType));
                PGInterval pgInterval = new PGInterval();
                pgInterval.setMinutes(duration);
                preparedStatement.setObject(4, pgInterval);
                preparedStatement.setObject(5, callDateTime);
                preparedStatement.executeUpdate();
            }
        } catch (
            SQLException e) {
            e.printStackTrace();
        }
    }
}
