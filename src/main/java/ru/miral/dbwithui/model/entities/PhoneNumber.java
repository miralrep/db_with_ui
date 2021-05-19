package ru.miral.dbwithui.model.entities;

/**
 * todo Document type PhoneNumber
 */
public class PhoneNumber {
    private String number;
    private int subscriberId;
    private int categoryId;

    public PhoneNumber(){}
    public PhoneNumber(String number, int subscriberId, int categoryId) {
        this.number = number;
        this.subscriberId = subscriberId;
        this.categoryId = categoryId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(int subscriberId) {
        this.subscriberId = subscriberId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
