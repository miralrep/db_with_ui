package ru.miral.dbwithui.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * todo Document type MainForm
 */
public class MainForm extends JFrame {
    private final int MAIN_MENU_WIDTH = 250;
    private final int MAIN_MENU_HEIGHT = 300;
    private final int ADD_MENU_WIDTH = 600;
    private final int ADD_MENU_HEIGHT = 300;
    public MainForm() {
        super("Главное меню");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 2, 2, 2));

        showMainMenu();
    }

    void showMainMenu() {

        setSizeAndCenter(250,300);
        getContentPane().removeAll();

        JButton addSubscriber = new JButton("Добавить абонента");
        JButton addCategory = new JButton("Добавить тариф");
        JButton addPhoneNumber = new JButton("Добавить телефон");
        JButton addConversation = new JButton("Добавить звонок");

        getContentPane().add(addConversation);
        getContentPane().add(addCategory);
        getContentPane().add(addPhoneNumber);
        getContentPane().add(addSubscriber);

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

        getContentPane().repaint();
    }

    private void showAddSubscriberMenu() {
        initAddMenu("Добавить абонента");

        getContentPane().repaint();
    }

    private void showAddPhoneNumberMenu() {
        initAddMenu("Добавить телефон");

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
    private void setSizeAndCenter(int width, int height){
        Dimension dim= Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(width, height);
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }

    private void initAddMenu(String name){
        setSizeAndCenter(ADD_MENU_WIDTH, ADD_MENU_HEIGHT);
        this.setTitle(name);
        getContentPane().removeAll();
    }

    private JButton backButton(){
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });
        return backButton;
    }

}