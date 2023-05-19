package Panels;

import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import DB.DB;
import Entities.Enemy;

public class EntityPanel extends JPanel {

	private DB conDB;

	private byte sId;
	private byte[][] entities;
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private byte[] trasEnt = { 0, // Air
			1, // Player
			10, // Rupee
			7, // Zombie
			8, // Zombie Rev
			11, // Esqueleton
			12, // Esqueleton Rev
			22, // Save Stone
			14, // Dog
			15, // Dog Rev
			5, //Atack
			6 //Atack Rev
	};

	public EntityPanel(byte nsId, DB database) throws SQLException {
		conDB = database;
		
		setsId(nsId);

		setLayout(new GridLayout(20, 30, 0, 0));
		setSize(30 * 32, 20 * 32);
		
		selectEntities(nsId);

		for (int i = 0; i < 600; i++) {
			JLabel etiqueta = new JLabel(new ImageIcon("resources/imgs/characters/" + selECell((short) i, nsId) + ".png"));
			add(etiqueta);

			if (selECell((short) i, nsId) == 7 || selECell((short) i, nsId) == 11 || selECell((short) i, nsId) == 14) {
				short nId = (short) enemies.size();
				enemies.add(new Enemy((short) i, (short) (enemies.size() + 1), selECell((short) i, nsId)));
			}
		}
		
		refreshPanel();
	}

	// ----- enemies -----//
	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public void setEnemies(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}
	
	//----- sId -----//
	public byte getsId() {
		return sId;
	}

	public void setsId(byte sId) {
		this.sId = sId;
	}
	
	//----- trasEnt -----//
	public byte[] getTrasEnt() {
		return trasEnt;
	}

	public void setTrasEnt(byte[] trasEnt) {
		this.trasEnt = trasEnt;
	}
	
	//----- Cells -----//
	public byte selECell(short nPos, byte nSId) throws SQLException {
		byte cell = entities[nPos / 30][nPos % 30];
		return cell;
	}

	public void replaceCell(short nPos, byte nValue) {
		entities[nPos / 30][nPos % 30] = nValue;
	}

	public void selectEntities(byte sId) throws SQLException {
		entities = conDB.selectEntities((byte) sId);
	}
	
	public Enemy getEnemy(short nPos) throws SQLException {
		for (int i = 0; i < enemies.size(); i++) {
			if (nPos == enemies.get(i).getPos()) {
				return enemies.get(i);
			}
		}
		return null;
	}

	public byte getEnemyIndex(short nPos) throws SQLException {
		for (int i = 0; i < enemies.size(); i++) {
			if (nPos == enemies.get(i).getPos()) {
				return (byte) i;
			}
		}
		return 0;
	}
	
	public void refreshPanel() throws SQLException {
		this.removeAll();
		
		for (short i = 0; i < 600; i++) {
			JLabel etiqueta = new JLabel(new ImageIcon("resources/imgs/characters/" + selECell(i, sId) + ".png"));
			this.add(etiqueta);
		}
		
		this.revalidate();
		this.repaint();
	}
	
	public void findEntity(byte id) throws SQLException {
		for (short i = 0; i < 600; i++) {
			if (selECell(i, sId) == id) {
				System.out.println(i);
			}
		}

	}
}
