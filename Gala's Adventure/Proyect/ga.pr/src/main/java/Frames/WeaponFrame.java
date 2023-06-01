package Frames;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import DB.DB;
import Entities.PlayerE;
import Items.Weapon;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class WeaponFrame extends JFrame {
	
	DB conDB;

	public WeaponFrame(String nItemName, ImageIcon nItemIcon, String nItemDesc, byte nDamage, final PlayerE player, DB nConDB) {
		final JFrame frame = this;
		final Weapon nWeapon = new Weapon(nItemName, nItemDesc, nItemIcon, nDamage);
		conDB = nConDB;
		
		this.setSize(400, 250);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		// Set values
		JLabel itemName = new JLabel(nItemName);
		ImageIcon icon = nItemIcon;
		JTextArea itemDesc = new JTextArea(nItemDesc);
		itemDesc.setLineWrap(true);
		itemDesc.setBackground(UIManager.getColor("Label.background"));
		itemDesc.setEnabled(false);
		JButton acceptBtn = new JButton("Equip");
		JButton dropBtn = new JButton("Drop");

		// Item Name
		itemName.setHorizontalAlignment(SwingConstants.CENTER);
		itemName.setBounds(10, 11, 366, 14);
		getContentPane().add(itemName);

		// Accept Button
		acceptBtn.setBounds(260, 179, 89, 23);
		getContentPane().add(acceptBtn);

		// Drop Button
		dropBtn.setBounds(43, 179, 89, 23);
		getContentPane().add(dropBtn);

		// Agregar ActionListener al botón
		acceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.setWeapon(nWeapon);
				frame.dispose();
			}
		});
		
		dropBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		// Resize Image
		Image resizedImage = icon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(resizedImage);
		JLabel itemImage = new JLabel(resizedIcon);
		itemImage.setBounds(158, 28, 65, 65);
		getContentPane().add(itemImage);

		// Item Desc
        itemDesc.setBounds(10, 104, 366, 64);
		getContentPane().add(itemDesc);

		this.setVisible(true);
	}
}
