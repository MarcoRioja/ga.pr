package Frames;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import DB.DB;
import Entities.PlayerE;
import Items.Item;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class ItemFrame extends JFrame {
	
	DB conDB;
	
	public ItemFrame(String nItemName, ImageIcon nItemIcon, String nItemDesc, final PlayerE player, DB nConDB) {
		
		final JFrame frame = this;
		
		final Item nItem = new Item(nItemName, nItemDesc, nItemIcon);
		conDB = nConDB;
		
		this.setSize(400,250);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		//Set values
		JLabel itemName = new JLabel(nItemName);
		ImageIcon icon = nItemIcon;
		JTextArea itemDesc = new JTextArea(nItemDesc);
		itemDesc.setEnabled(false);
		itemDesc.setBackground(UIManager.getColor("Label.background"));
		itemDesc.setLineWrap(true);
		JButton acceptBtn = new JButton("Collect");
		
		//Item Name
		itemName.setHorizontalAlignment(SwingConstants.CENTER);
		itemName.setBounds(10, 11, 366, 14);
		getContentPane().add(itemName);
		
		//Accept Button
		acceptBtn.setBounds(149, 179, 89, 23);
		getContentPane().add(acceptBtn);
		
		// Agregar ActionListener al botón
		acceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (nItem.getName()) {
				case "Small Potion":
					break;
				case "Normal Potion":
					break;
				case "Big Potion":
					break;
				case "Green Rupee":
					break;
				case "Blue Rupee":
					break;
				case "Red Rupee":
					break;
				case "Purple Rupee":
					break;
				default:
					player.addToInventory(nItem);
					break;
				}
				
				
				frame.dispose();
			}
        });
		
		//Resize Image
	    Image resizedImage = icon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
	    ImageIcon resizedIcon = new ImageIcon(resizedImage);
		JLabel itemImage = new JLabel(resizedIcon);
		itemImage.setBounds(158, 28, 65, 65);
		getContentPane().add(itemImage);
		
		//Item Desc
		itemDesc.setBounds(10, 104, 366, 65);
		getContentPane().add(itemDesc);
		
		this.setVisible(true);
	}
}
