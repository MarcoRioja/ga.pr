package form.marco;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class FormWindow extends JFrame {
    private JTextField txtName;
    private JButton btnSearch;
    private Connection connection;

    public FormWindow() {
        connectToDatabase();

        setTitle("Form");
        setSize(300,100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JLabel lblName = new JLabel("Name:");
        txtName = new JTextField(10);

        btnSearch = new JButton("Show");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchItems();
            }
        });

        panel.add(lblName);
        panel.add(txtName);
        panel.add(btnSearch);

        add(panel);
    }

    private void connectToDatabase() {
    	String url = "jdbc:mysql://localhost:3306/ga_data";
        String user = "root";
        String password = "";

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.");
        }
    }

    private void searchItems() {
        String name = txtName.getText();
        
        ListWindow listWindow = new ListWindow(connection, name);
        listWindow.setVisible(true);
        setVisible(false);
    }
}