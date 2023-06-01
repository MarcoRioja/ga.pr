package form.marco;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class ItemFrame extends JFrame {
	
	public ItemFrame(String nItemName, String nDescription, String nImageLink) throws IOException {
		
		final JFrame frame = this;
		
		this.setSize(400,250);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		//Set values
		JLabel itemName = new JLabel(nItemName);
		JTextArea itemDesc = new JTextArea(nDescription);
		itemDesc.setEnabled(false);
		itemDesc.setBackground(UIManager.getColor("Label.background"));
		itemDesc.setLineWrap(true);
		JButton acceptBtn = new JButton("Close");
		
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
				frame.dispose();
			}
        });
		
		//Resize Image
		Image image = ImageIO.read(new File(nImageLink)).getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(image);
		JLabel itemImage = new JLabel(imageIcon);
		itemImage.setBounds(145, 20, 100, 100);
		getContentPane().add(itemImage);
		
		//Item Desc
		itemDesc.setBounds(10, 115, 366, 65);
		getContentPane().add(itemDesc);
		
		this.setVisible(true);
	}
}
