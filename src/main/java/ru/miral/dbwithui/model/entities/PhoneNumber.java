package ru.miral.dbwithui.model.entities;

/**
 * todo Document type PhoneNumber
 */
public class PhoneNumber {
    private String number;
    private Subscriber subscriber;
    private Category categoryId; //тариф

    public PhoneNumber(){}
    public PhoneNumber(String number, Subscriber subscriber, Category categoryId) {
        this.number = number;
        this.subscriber = subscriber;
        this.categoryId = categoryId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Category getCategory() {
        return categoryId;
    }

    public void setCategory(Category categoryId) {
        this.categoryId = categoryId;
    }
}
