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

    public static CallType getCallTypeByName(String name) throws Exception {
        for(CallType callType: CallType.values()){
            if(callType.name.equals(name))
                return callType;
        }
        throw new Exception("Нет льгот с таким названием");
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }
}
