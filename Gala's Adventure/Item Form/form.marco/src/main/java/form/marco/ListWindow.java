package form.marco;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;

@SuppressWarnings("serial")
public class ListWindow extends JFrame {
    private JPanel panel;
    private JButton btnBack;
    private Connection connection;
    private String searchName;

    public ListWindow(Connection connection, String searchName) {
        this.connection = connection;
        this.searchName = searchName;

        setTitle("List");
        setSize(550, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                FormWindow formWindow = new FormWindow();
                formWindow.setVisible(true);
            }
        });

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(btnBack, BorderLayout.SOUTH);

        setContentPane(contentPane);

        getItems();
    }

    private void getItems() {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM items WHERE name LIKE '%" + searchName + "%'";
            ResultSet resultSet = statement.executeQuery(query);

            int gridY = 0;

            while (resultSet.next()) {
                final String itemName = resultSet.getString("name");
                final String description = resultSet.getString("description");
                final String imageLink = resultSet.getString("image_link");

                JButton btnSee = new JButton("See");
                btnSee.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Handle button click event
                        try {
							showItemDetails(itemName, description, imageLink);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
                    }
                });
                
                

                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = 0;
                constraints.gridy = gridY;
                constraints.weightx = 1.0;
                constraints.fill = GridBagConstraints.HORIZONTAL;
                constraints.anchor = GridBagConstraints.NORTH;
                constraints.insets = new Insets(5, 5, 5, 5);

                final JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setPreferredSize(new Dimension(500, 25));
                itemPanel.add(new JLabel(itemName), BorderLayout.WEST);
                itemPanel.add(btnSee, BorderLayout.CENTER);
                
                panel.add(itemPanel, constraints);
                
                JButton btnHide = new JButton("Hide");
                btnHide.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	panel.remove(itemPanel);
                        panel.revalidate();
                        panel.repaint();
                    }
                });
                itemPanel.add(btnHide, BorderLayout.EAST);

                gridY++;
            }

            revalidate();
            repaint();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving items from the database.");
        }
    }

    private void showItemDetails(String itemName, String description, String imageLink) throws IOException {
        ItemFrame itemFrame = new ItemFrame(itemName, description, imageLink);
        itemFrame.setVisible(true);
    }
}
