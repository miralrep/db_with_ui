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
    private int callTypeId;
    private Duration duration;
    private LocalDateTime dateTime;

    public Conversation(){}

    public Conversation
        (
            int id, String callingPhone, String takingPhone,
            int callTypeId, Duration duration, LocalDateTime dateTime
        ) {
        this.id = id;
        this.callingPhone = callingPhone;
        this.takingPhone = takingPhone;
        this.callTypeId = callTypeId;
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

    public int getCallTypeId() {
        return callTypeId;
    }

    public void setCallTypeId(int callTypeId) {
        this.callTypeId = callTypeId;
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
