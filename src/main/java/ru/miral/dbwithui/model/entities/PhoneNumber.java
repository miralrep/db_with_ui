package ru.miral.dbwithui.model.entities;

/**
 * todo Document type PhoneNumber
 */
public class PhoneNumber {
    private String number;
    private Category category; //тариф

    public PhoneNumber(){}

    public PhoneNumber(String number, Category category) {
        this.number = number;
        this.category = category;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return number;
    }
}
