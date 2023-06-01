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
import javax.swing.UIManager;

import DB.DB;
import Entities.PlayerE;
import Items.Weapon;

@SuppressWarnings("serial")
public class SeeWeaponFrame extends JFrame {
	DB conDB;

	public SeeWeaponFrame(Weapon nWeapon, final PlayerE player, DB nConDB) {
		final JFrame frame = this;
		conDB = nConDB;
		
		this.setSize(400, 250);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		// Set values
		JLabel itemName = new JLabel(nWeapon.getName() + " ---> " + nWeapon.getDamage());
		ImageIcon icon = nWeapon.getImage();
		JTextArea itemDesc = new JTextArea(nWeapon.getDesc());
		itemDesc.setLineWrap(true);
		itemDesc.setBackground(UIManager.getColor("Label.background"));
		itemDesc.setEnabled(false);
		JButton acceptBtn = new JButton("Exit");

		// Item Name
		itemName.setHorizontalAlignment(SwingConstants.CENTER);
		itemName.setBounds(10, 11, 366, 14);
		getContentPane().add(itemName);

		// Accept Button
		acceptBtn.setBounds(158, 179, 89, 23);
		getContentPane().add(acceptBtn);

		acceptBtn.addActionListener(new ActionListener() {
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
