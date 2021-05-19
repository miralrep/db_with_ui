package ru.miral.dbwithui.model.entities;

/**
 * todo Document type Category
 */
public enum Category {

    DEFAULT("Стандарт", 0.00d),
    UNLIMITED("Безлимит", 500.00d),
    UNLIMITED_PLUS("Безлимит+", 1000.00d);

    private final String name;
    private final double subscriptionFee;
    Category(String name, double subscriptionFee) {
        this.name = name;
        subscriptionFee = Math.floor(subscriptionFee*100)/100;
        this.subscriptionFee = subscriptionFee;
    }

    public String getName() {
        return name;
    }

    public double getSubscriptionFee() {
        return subscriptionFee;
    }
}