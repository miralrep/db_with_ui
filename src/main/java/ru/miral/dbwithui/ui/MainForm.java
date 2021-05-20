package ru.miral.dbwithui.ui;

import jdk.jshell.spi.ExecutionControl;
import ru.miral.dbwithui.dao.Repository;
import ru.miral.dbwithui.model.entities.Category;
import ru.miral.dbwithui.model.entities.PhoneNumber;
import ru.miral.dbwithui.model.entities.Privilege;
import ru.miral.dbwithui.model.entities.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Array;
import java.sql.SQLException;
import java.util.*;

/**
 * todo Document type MainForm
 */
public class MainForm extends JFrame {
    private final int MAIN_MENU_WIDTH = 250;
    private final int MAIN_MENU_HEIGHT = 300;
    private final int ADD_MENU_WIDTH = 400;
    private final int ADD_MENU_HEIGHT = 400;
    private final int ADD_MENU_TEXT_COLUMNS = 34;

    Repository repository;

    public MainForm() {
        super("Главное меню");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        try {
            repository = new Repository();
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(getContentPane(), "Не найден драйвер БД!");
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            e.printStackTrace();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(getContentPane(), "Не удалось подключиться к БД");
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            e.printStackTrace();
        }

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        showMainMenu();
    }

    void showMainMenu() {

        setSizeAndCenter(250, 300);
        getContentPane().removeAll();

        JButton addSubscriber = new JButton("Добавить абонента");
        //JButton addCategory = new JButton("Добавить тариф");
        JButton addPhoneNumber = new JButton("Добавить телефон");
        JButton addConversation = new JButton("Добавить звонок");
        add(addSubscriber);
        //add(addCategory);
        add(addPhoneNumber);
        add(addConversation);

        JTextField telephoneField = new JTextField(20);
        JLabel telephoneLabel = new JLabel("Номер телефона");
        JButton reportButton = new JButton("Составить отчет");
        add(telephoneLabel);
        add(telephoneField);
        add(reportButton);

        addConversation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddConversationMenu();
            }
        });
        /*addCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddCategoryMenu();
            }
        });*/
        addPhoneNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddPhoneNumberMenu();
            }
        });
        addSubscriber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddSubscriberMenu();
            }
        });

        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    throw new ExecutionControl.NotImplementedException("Создание отчета");
                } catch (ExecutionControl.NotImplementedException notImplementedException) {
                    notImplementedException.printStackTrace();
                }
            }
        });

        getContentPane().repaint();
    }

    private void showAddSubscriberMenu() {
        initAddMenu("Добавить абонента");
        JLabel surnameLabel = new JLabel("Фамилия");
        JTextField surnameField = new JTextField(ADD_MENU_TEXT_COLUMNS);
        add(surnameLabel);
        add(surnameField);
        JLabel nameLabel = new JLabel("Имя");
        JTextField nameField = new JTextField(ADD_MENU_TEXT_COLUMNS);
        add(nameLabel);
        add(nameField);
        JLabel patronymicLabel = new JLabel("Отчество");
        JTextField patronymicField = new JTextField(ADD_MENU_TEXT_COLUMNS);
        add(patronymicLabel);
        add(patronymicField);
        JLabel addressLabel = new JLabel("Адресс");
        JTextField addressField = new JTextField(ADD_MENU_TEXT_COLUMNS);
        add(addressLabel);
        add(addressField);
        JLabel privilegesLabel = new JLabel("Льготы");
        JList<Privilege> privilegesList = new JList<>(Privilege.values());
        add(privilegesLabel);
        add(privilegesList);

        JButton submit = new JButton("Добавить");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String surname = surnameField.getText();
                String name = nameField.getText();
                String patronymic = patronymicField.getText();
                String address = surnameField.getText();
                Set<Privilege> privileges = new HashSet<>(privilegesList.getSelectedValuesList());
                addNewSubscriber(surname, name, patronymic, address, privileges);
            }
        });
        add(submit);

        getContentPane().repaint();
    }

    private void showAddPhoneNumberMenu() {
        initAddMenu("Добавить телефон");
        JLabel telephoneLabel = new JLabel("Телефон");
        JTextField telephoneField = new JTextField(20);
        JComboBox<Subscriber> subscribersComboBox = new JComboBox<>();
        repository.getAllSubscribers().forEach(subscribersComboBox::addItem);
        JComboBox<Category> categoryComboBox = new JComboBox<>();
        repository.getAllCategories().forEach(categoryComboBox::addItem);
        JButton submit = new JButton("Добавить");

        add(telephoneLabel);
        add(telephoneField);
        add(subscribersComboBox);
        add(categoryComboBox);
        add(submit);

        getContentPane().repaint();

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String number = telephoneField.getText();
                Subscriber subscriber = (Subscriber) subscribersComboBox.getSelectedItem();
                Category category = (Category) categoryComboBox.getSelectedItem();
                PhoneNumber phoneNumber = new PhoneNumber(
                    number, category
                );
                repository.savePhoneNumber(phoneNumber, subscriber);
            }
        });
    }

    private void showAddCategoryMenu() {
        initAddMenu("Добавить тариф");

        getContentPane().repaint();
    }

    private void showAddConversationMenu() {
        initAddMenu("Добавить звонок");

        getContentPane().repaint();
    }

    private void setSizeAndCenter(int width, int height) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(width, height);
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }

    private void initAddMenu(String title) {
        setSizeAndCenter(ADD_MENU_WIDTH, ADD_MENU_HEIGHT);
        this.setTitle(title);
        getContentPane().removeAll();
        addBackButton();
    }

    private void addBackButton() {
        JButton backButton = new JButton("Назад");
        backButton.setSize(380, 30);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });
        add(backButton);
    }

    private void add(JComponent component) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(component);
    }

    private void addNewSubscriber(
        String name, String surname, String patronymic,
        String address, Set<Privilege> privileges){
        Subscriber subscriber = new Subscriber(
            -1, surname, name, patronymic, address, privileges, null
        );
        repository.saveSubscriber(subscriber);
    }
}