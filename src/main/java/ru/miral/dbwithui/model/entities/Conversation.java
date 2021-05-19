package ru.miral.dbwithui.model.entities;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * todo Document type Conversation
 */
public class Conversation {
    private int id;
    private String callingPhone;
    private String takingPhone;
    private CallType callType;
    private Duration duration;
    private LocalDateTime dateTime;

    public Conversation(){}

    public Conversation
        (
            int id, String callingPhone, String takingPhone,
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

    public String getCallingPhone() {
        return callingPhone;
    }

    public void setCallingPhone(String callingPhone) {
        this.callingPhone = callingPhone;
    }

    public String getTakingPhone() {
        return takingPhone;
    }

    public void setTakingPhone(String takingPhone) {
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
}
