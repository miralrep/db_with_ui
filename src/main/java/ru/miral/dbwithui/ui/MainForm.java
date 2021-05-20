package ru.miral.dbwithui.ui;

import jdk.jshell.spi.ExecutionControl;
import ru.miral.dbwithui.dao.Repository;
import ru.miral.dbwithui.model.entities.Category;
import ru.miral.dbwithui.model.entities.Privilege;
import ru.miral.dbwithui.model.entities.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * todo Document type MainForm
 */
public class MainForm extends JFrame {
    private final int MAIN_MENU_WIDTH = 250;
    private final int MAIN_MENU_HEIGHT = 300;
    private final int ADD_MENU_WIDTH = 400;
    private final int ADD_MENU_HEIGHT = 400;

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

        getContentPane().setLayout(new FlowLayout());

        showMainMenu();
    }

    void showMainMenu() {

        setSizeAndCenter(250, 300);
        getContentPane().removeAll();

        JButton addSubscriber = new JButton("Добавить абонента");
        JButton addCategory = new JButton("Добавить тариф");
        JButton addPhoneNumber = new JButton("Добавить телефон");
        JButton addConversation = new JButton("Добавить звонок");
        add(addConversation);
        add(addCategory);
        add(addPhoneNumber);
        add(addSubscriber);

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
        addCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddCategoryMenu();
            }
        });
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
        JTextField surnameField = new JTextField(30);
        add(surnameField);
        JTextField nameField = new JTextField(30);
        add(nameField);
        JTextField patronymicField = new JTextField(30);
        add(patronymicField);
        JTextField addressField = new JTextField(30);
        add(addressField);
        JComboBox<Privilege> privilegesComboBox = new JComboBox<>(Privilege.values());
        add(privilegesComboBox);

        JButton submit = new JButton("Добавить");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*repository.saveSubscriber();*/
            }
        });
        add(submit);

        getContentPane().repaint();
    }

    private void showAddPhoneNumberMenu() {
        initAddMenu("Добавить телефон");
        JTextField telephoneField = new JTextField(20);
        JComboBox<Subscriber> subscribersComboBox = new JComboBox<>();//TODO подтянуть абонентов
        JComboBox<Category> categoryComboBox = new JComboBox<>();//TODO подтянуть тарифы

        getContentPane().repaint();
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
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });
        add(backButton);
    }

    private void add(JComponent component) {
        getContentPane().add(component);
    }
}