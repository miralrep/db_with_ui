package ru.miral.dbwithui.model.entities;

/**
 * todo Document type PhoneNumber
 */
public class PhoneNumber {
    private String number;
    private Category categoryId; //тариф

    public PhoneNumber(){}

    public PhoneNumber(String number, Category categoryId) {
        this.number = number;
        this.categoryId = categoryId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Category getCategory() {
        return categoryId;
    }

    public void setCategory(Category categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return number;
    }
}
