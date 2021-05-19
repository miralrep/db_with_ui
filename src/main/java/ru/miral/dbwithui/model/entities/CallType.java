package ru.miral.dbwithui.model.entities;

/**
 * todo Document type CallType
 */
public enum CallType {
    LOCAL("Местный"),
    INTERCITY("Междугородний"),
    INTERNATIONAL("Международный");

    final private String name;
    CallType(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return  name;
    }
}
