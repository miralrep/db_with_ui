package ru.miral.dbwithui.model.entities;

/**
 * todo Document type Privilege
 */
public enum Privilege {

    NONE("", 0.00d),
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

    @Override
    public String toString() {
        return name;
    }

    public static Privilege getPrivilegeByName(String name) throws Exception {
        for(Privilege privilege: Privilege.values()){
            if(privilege.name.equals(name))
                return privilege;
        }
        throw new Exception("Нет льгот с таким названием");
    }

    public String getName() {
        return name;
    }

    public double getDiscount() {
        return discount;
    }
}
