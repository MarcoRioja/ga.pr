package Panels;

import java.awt.*;
import java.sql.SQLException;

import javax.swing.*;

import DB.DB;

@SuppressWarnings("serial")
public class TerrainPanel extends JPanel {

	DB conDB;

	public TerrainPanel(byte sId, DB database) throws SQLException {

		conDB = database; //Database
		
		setLayout(new GridLayout(20, 30, 0, 0));

		setSize(30 * 32, 20 * 32);

		for (int i = 0; i < 600; i++) {
			// if (selCell(i) != 00) {
			JLabel etiqueta = new JLabel(new ImageIcon("resources/imgs/terrain1/" + selTCell(i, sId) + ".png"));
			add(etiqueta);
			// }

		}

	}

	public byte selTCell(int num, byte sId) throws SQLException {
		byte cell = conDB.selectTerrain((byte) sId)[num / 30][num % 30];
		return cell;
	}

}
