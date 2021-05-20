package ru.miral.dbwithui.model.entities;

import java.util.Map;

/**
 * todo Document type Category
 */
public class Category {
    private int id;
    private String name;
    private double subscriptionFee;
    Map<CallType, Double> fees;

    public Category(){}

    public Category(int id, String name, int subscriptionFee, Map<CallType, Double> fees) {
        this.id = id;
        this.name = name;
        this.subscriptionFee = subscriptionFee;
        this.fees = fees;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSubscriptionFee() {
        return subscriptionFee;
    }

    public void setSubscriptionFee(double subscriptionFee) {
        this.subscriptionFee = subscriptionFee;
    }

    public Map<CallType, Double> getFees() {
        return fees;
    }

    public double getFeeByCallType(CallType callType){
        return fees.get(callType);
    }

    public void setFeeByCallType(String callType, double fee) {
        try {
            fees.put(CallType.getCallTypeByName(callType), fee);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFees(Map<CallType, Double> fees) {
        this.fees = fees;
    }

    @Override
    public String toString() {
        return name;
    }
}






















/*DEFAULT("Стандарт", 0.00d,
        new HashMap<CallType, Double>() {
        {
            put(CallType.LOCAL, 5.00d);
            put(CallType.INTERCITY, 7.00d);
            put(CallType.INTERNATIONAL,10.00d);
        }
    }),
    UNLIMITED("Безлимит", 500.00d,
        new HashMap<CallType, Double>() {
        {
            put(CallType.LOCAL, 0.00d);
            put(CallType.INTERCITY, 0.00d);
            put(CallType.INTERNATIONAL,10.00d);
        }
    }),
    UNLIMITED_PLUS("Безлимит+", 1000.00d,
        new HashMap<CallType, Double>() {
        {
            put(CallType.LOCAL, 0.00d);
            put(CallType.INTERCITY,0.00d);
            put(CallType.INTERNATIONAL,0.00d);
        }
    });

    private final String name;
    private final double subscriptionFee;
    private final Map<CallType, Double> fees;

    CategoryId(String name, double subscriptionFee,  Map<CallType, Double> fees) {
        this.name = name;
        subscriptionFee = Math.floor(subscriptionFee*100)/100;
        this.subscriptionFee = subscriptionFee;
        this.fees = fees;
    }

    public String getName() {
        return name;
    }

    public double getSubscriptionFee() {
        return subscriptionFee;
    }

    public double getFeeByCallType(CallType callType) {
        return fees.get(callType);
    }*/
