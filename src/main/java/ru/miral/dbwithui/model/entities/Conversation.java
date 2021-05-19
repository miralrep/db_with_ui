package ru.miral.dbwithui.model.entities;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * todo Document type Conversation
 */
public class Conversation {
    private int id;
    private PhoneNumber callingPhone;
    private PhoneNumber takingPhone;
    private CallType callType;
    private Duration duration;
    private LocalDateTime dateTime;

    public Conversation(){}

    public Conversation
        (
            int id, PhoneNumber callingPhone, PhoneNumber takingPhone,
            CallType callType, Duration duration, LocalDateTime dateTime
        ) {
        this.id = id;
        this.callingPhone = callingPhone;
        this.takingPhone = takingPhone;
        this.callType = callType;
        this.duration = duration;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PhoneNumber getCallingPhone() {
        return callingPhone;
    }

    public void setCallingPhone(PhoneNumber callingPhone) {
        this.callingPhone = callingPhone;
    }

    public PhoneNumber getTakingPhone() {
        return takingPhone;
    }

    public void setTakingPhone(PhoneNumber takingPhone) {
        this.takingPhone = takingPhone;
    }

    public CallType getCallType() {
        return callType;
    }

    public void setCallType(CallType callType) {
        this.callType = callType;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return id + " " + callingPhone + " " + takingPhone;
    }
}
