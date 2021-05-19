package ru.miral.dbwithui.model.entities;

/**
 * todo Document type Privilege
 */
public enum Privilege {

    DISABILITY("Инвалидность", 5.00d),
    MANY_CHILDREN("Многодетность", 2.00d),
    WAR_VETERAN("Ветеран войны", 7.00d);
    private final String name;
    private final double discount;

    Privilege(String name, double discount){
        this.name = name;
        discount = Math.floor(discount*100)/100;
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public double getDiscount() {
        return discount;
    }
}
