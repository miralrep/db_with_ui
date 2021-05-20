package ru.miral.dbwithui.model.entities;

import java.util.Set;

/**
 * todo Document type Subscriber
 */
public class Subscriber {
    private int id;
    private String surname;
    private String name;
    private String patronymic;
    private String address;

    private Set<Privilege> privileges;
    private Set<PhoneNumber> phoneNumbers;

    public Subscriber() {
    }

    public Subscriber
        (
            int id, String surname, String name, String patronymic,
            String address, Set<Privilege> privileges, Set<PhoneNumber> phoneNumbers
        ) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.address = address;
        this.privileges = privileges;
        this.phoneNumbers = phoneNumbers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public void addPrivilege(Privilege privilege) {
        this.privileges.add(privilege);
    }

    public Set<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Set<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public void addPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumbers.add(phoneNumber);
    }

    @Override
    public String toString() {
        return id + ": " + surname + " " + name + " " + patronymic;
    }
}
