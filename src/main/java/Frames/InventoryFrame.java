package Frames;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import Items.Item;

import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class InventoryFrame extends JFrame{
	
	ArrayList<String> invS = new ArrayList<>();
	
	public InventoryFrame(ArrayList<Item> inv) {
		
		this.setTitle("Inventory");
		
		final JFrame frame = this;
		
		if (inv.size() == 0) {
			invS.add("There is nothing here, NOTHING!!");
		} else {
			addItems(inv);
		}
		
		JList<String> invList = new JList<String>(invS.toArray(new String[0]));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane(invList);
		scrollPane.setBounds(10, 11, 464, 212);
		panel.add(scrollPane);
		
		JButton closeBtn = new JButton("Close");
		closeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		closeBtn.setBounds(198, 227, 89, 23);
		panel.add(closeBtn);
		
		this.setSize(500,300);
		this.setLocationRelativeTo(null);
	}
	
	private void addItems(ArrayList<Item> inv) {
		
		for (int i = 0; i < inv.size(); i++) {
			invS.add("-> " + inv.get(i).getName() + ": " + inv.get(i).getDesc());
		}
	}
}
