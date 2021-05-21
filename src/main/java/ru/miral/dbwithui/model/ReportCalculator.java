package ru.miral.dbwithui.model;

import ru.miral.dbwithui.dao.Repository;
import ru.miral.dbwithui.model.entities.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Set;

/**
 * todo Document type ReportCalculator
 */
public class ReportCalculator {
    Repository repository;

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public String getReportText(PhoneNumber phoneNumber, LocalDateTime yearMonth) {

        Subscriber subscriber = repository.getSubscriberByPhoneNumber(phoneNumber);
        Set<Conversation> conversationSet = repository.getPhoneNumberConversationsByMonth(phoneNumber, yearMonth);
        Category category = phoneNumber.getCategory();

        double sum = 0;
        double subscriptionFee = phoneNumber.getCategory().getSubscriptionFee();
        double feeSum = 0;
        for (Conversation conversation : conversationSet) {
            long duration = conversation.getDuration().toMinutes();
            double febuf = category.getFeeByCallType(conversation.getCallType());
            feeSum += conversation.getDuration().toMinutes() * category.getFeeByCallType(conversation.getCallType());
        }
        double discount = 0;
        for (Privilege privilege : subscriber.getPrivileges()) {
            discount += privilege.getDiscount();
        }

        sum = (feeSum + subscriptionFee) / 100 * (100 - discount);

        return subscriber + "\n" +

            yearMonth.getYear() + " "
            + yearMonth.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "\n" +
            "Ежемесячный платеж: " + subscriptionFee + "\n" +
            "Сумма звонков: " + feeSum + "\n" +
            "Скидка по льготам: " + discount + "\n" +
            "\nИтого: " + sum;
    }
}
