package Panels;

import java.awt.HeadlessException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import DB.DB;
import Entities.Enemy;
import Entities.PlayerE;
import Errors.InvalidItemIdException;
import Errors.InvalidStageIdException;
import Errors.InvalidWeaponIdException;
import Features.SoundPlayer;
import Frames.GameFrame;
import Frames.InventoryFrame;
import Frames.ItemFrame;
import Frames.SeeWeaponFrame;
import Frames.WeaponFrame;
import Items.Item;
import Items.Weapon;

@SuppressWarnings("serial")
public class GameController extends JPanel {

	// ----- Database -----//
	private DB conDB;

	// ----- Player -----//
	private PlayerE player;
	private boolean playerInAir;
	private byte playerActId = 1;
	private boolean playerVolted;
	private short attackPos;
	private boolean attacking = false;
	
	// ----- Panels -----//
	private EntityPanel entityPanel;
	private TerrainPanel terrainPanel;
	private JFrame parentFrame;
	boolean starting = true;

	// ----- Stage -----//
	private byte actStage;

	// ----- Save Stone -----//
	private boolean hasStone;
	private short stonePos;

	// ----- Sound Player -----//
	private SoundPlayer soundPlayer = new SoundPlayer();

	public GameController(byte nSId, PlayerE nPlayer, DB nConDB, EntityPanel nEntityPanel, TerrainPanel nTerrainPanel)
			throws SQLException {
		actStage = nSId; // Actual Stage
		player = nPlayer; // Player
		entityPanel = nEntityPanel; // Entity Panel
		terrainPanel = nTerrainPanel; // Terrain Panel
		
		conDB = nConDB; // Database
		
		checkSaveStone();
		refreshFrame();

	}

	// ----- Moves -----//
	public void movePlayer(char nDirection) throws SQLException, IOException {
		refreshPanel();
		player.setLastPos(player.getPos());

		switch (nDirection) {
		case 'w':
			if (!playerInAir && (player.getPos() - (30 * 2)) > 0) {
				if (checkPassColl((short) (player.getPos() - (30 * 2)))) {
					soundPlayer.playSound("resources/audio/jump.wav");
					entityPanel.replaceCell(player.getPos(), (byte) 0);
					player.moveY(checkUpSpace((byte) 2));
					entityPanel.replaceCell(player.getPos(), (byte) playerActId);

					refreshFrame();
				}
			}
			break;
		case 'a':
			if (player.getPos() % 30 == 0)
				return;

			if (!playerVolted) {
				playerVolted = true;
				entityPanel.replaceCell(player.getPos(), (byte) 2);

				refreshPanel();
				refreshPlayerPos();
			} else {

				// Air //
				if (playerInAir && (player.getPos() + 30) < 599) {
					if ((terrainPanel.selTCell((short) (player.getPos() + 29), actStage) == 0)
							&& (checkPassColl((short) (player.getPos() + 29)))) {

						entityPanel.replaceCell(player.getPos(), (byte) 0);
						player.moveX((byte) -1);
						player.moveY((byte) -1);
						entityPanel.replaceCell(player.getPos(), (byte) playerActId);
					} else {
						entityPanel.replaceCell(player.getPos(), (byte) 0);
						player.moveY((byte) -1);
						entityPanel.replaceCell(player.getPos(), (byte) playerActId);
					}

					refreshFrame();
				} else

				// - Check Void - //
				if ((terrainPanel.selTCell((short) (player.getPos() - 1), actStage) == 0)
						&& (checkPassColl((short) (player.getPos() - 1)))) {
					entityPanel.replaceCell(player.getPos(), (byte) 0);
					player.moveX((byte) -1);
					entityPanel.replaceCell(player.getPos(), (byte) playerActId);

					refreshFrame();
				}

			}
			break;
		case 's':
			if (playerInAir) {
				if (checkPassColl((short) (player.getPos() + 30))) {
					entityPanel.replaceCell(player.getPos(), (byte) 0);
					player.moveY((byte) -1);
					entityPanel.replaceCell(player.getPos(), (byte) playerActId);

					refreshFrame();
				}
			}
			break;
		case 'd':
			
			if (playerVolted) {
				playerVolted = false;
				entityPanel.replaceCell(player.getPos(), (byte) 1);

				refreshPanel();
				refreshPlayerPos();
			} else {
				
				if (starting) {
					player.setPos((short) 480);
					starting = false;
				} else if (player.getPos() % 30 == 29) {
					entityPanel.replaceCell(player.getPos(), (byte) 0);
					player.setPos((short) 480);
					
					try {
						ChangeStage();
					} catch(InvalidStageIdException e1) {
						e1.printStackTrace();
						ChangeStage((byte) 0);
					}
					
				}

				// Air //
				if (playerInAir && (player.getPos() + 30) < 599) {
					if ((terrainPanel.selTCell((short) (player.getPos() + 31), actStage) == 0)
							&& (checkPassColl((short) (player.getPos() + 31)))) {

						entityPanel.replaceCell(player.getPos(), (byte) 0);
						player.moveX((byte) 1);
						player.moveY((byte) -1);
						entityPanel.replaceCell(player.getPos(), (byte) playerActId);
					} else {
						entityPanel.replaceCell(player.getPos(), (byte) 0);
						player.moveY((byte) -1);
						entityPanel.replaceCell(player.getPos(), (byte) playerActId);
					}

					refreshFrame();
				} else

				// - Check Void - //
				if (terrainPanel.selTCell((short) (player.getPos() + 1), actStage) == 0) {
					if (checkPassColl((short) (player.getPos() + 1))) {
						entityPanel.replaceCell(player.getPos(), (byte) 0);
						player.moveX((byte) 1);
						entityPanel.replaceCell(player.getPos(), (byte) playerActId);

						refreshFrame();
					}
				}

			}
			break;
		case ' ':
			if ((!playerInAir && (player.getPos() - (30 * 4)) > 0)
					&& (checkPassColl((short) (player.getPos() - (30 * 4))))) {
				soundPlayer.playSound("resources/audio/jump.wav");
				entityPanel.replaceCell(player.getPos(), (byte) 0);
				player.moveY(checkUpSpace((byte) 4));
				entityPanel.replaceCell(player.getPos(), (byte) playerActId);

				refreshFrame();
			}
			break;
		default:
			break;
		}
	}

	public void moveEnemy(Enemy enemy) throws SQLException {
		if (enemy.getDir() == 0) {
			if ((terrainPanel.selTCell((short) (enemy.getPos() + 31), actStage) != 0)
					&& (terrainPanel.selTCell((short) (enemy.getPos() + 1), actStage) == 0)
					&& ((entityPanel.selECell((short) (enemy.getPos() + 1), actStage) == 0)
							|| (entityPanel.selECell((short) (enemy.getPos() + 1), actStage) == 1)
							|| (entityPanel.selECell((short) (enemy.getPos() + 1), actStage) == 2))) {

				entityPanel.replaceCell(enemy.getPos(), (byte) 0);
				enemy.moveX((byte) +1);
				entityPanel.replaceCell(enemy.getPos(), (byte) enemy.getiId());
			} else {
				enemy.changeDir();
			}
		} else if (enemy.getDir() == 1) {
			if ((terrainPanel.selTCell((short) (enemy.getPos() + 29), actStage) != 0)
					&& (terrainPanel.selTCell((short) (enemy.getPos() - 1), actStage) == 0)
					&& ((entityPanel.selECell((short) (enemy.getPos() - 1), actStage) == 0)
							|| (entityPanel.selECell((short) (enemy.getPos() - 1), actStage) == 1)
							|| (entityPanel.selECell((short) (enemy.getPos() - 1), actStage) == 2))) {

				entityPanel.replaceCell(enemy.getPos(), (byte) 0);
				enemy.moveX((byte) -1);
				entityPanel.replaceCell(enemy.getPos(), (byte) (enemy.getiId() + 1));
			} else {
				enemy.changeDir();
			}
		}

	}

	public void moveEnemyR(Enemy enemy) throws SQLException {
		Random r = new Random();

		enemy.setDir((byte) r.nextInt(2));

		if (enemy.getDir() == 0) {
			if ((terrainPanel.selTCell((short) (enemy.getPos() + 31), actStage) != 0)
					&& (terrainPanel.selTCell((short) (enemy.getPos() + 1), actStage) != 0)
					&& ((entityPanel.selECell((short) (enemy.getPos() + 1), actStage) == 0)
							|| (entityPanel.selECell((short) (enemy.getPos() + 1), actStage) == 1)
							|| (entityPanel.selECell((short) (enemy.getPos() + 1), actStage) == 2))) {

				entityPanel.replaceCell(enemy.getPos(), (byte) 0);
				enemy.moveX((byte) +1);
				entityPanel.replaceCell(enemy.getPos(), (byte) enemy.getiId());
			}
		} else if (enemy.getDir() == 1) {
			if ((terrainPanel.selTCell((short) (enemy.getPos() + 29), actStage) != 0)
					&& (terrainPanel.selTCell((short) (enemy.getPos() - 1), actStage) != 0)
					&& ((entityPanel.selECell((short) (enemy.getPos() - 1), actStage) == 0)
							|| (entityPanel.selECell((short) (enemy.getPos() - 1), actStage) == 1)
							|| (entityPanel.selECell((short) (enemy.getPos() - 1), actStage) == 2))) {

				entityPanel.replaceCell(enemy.getPos(), (byte) 0);
				enemy.moveX((byte) -1);
				entityPanel.replaceCell(enemy.getPos(), (byte) (enemy.getiId() + 1));
			}
		}

	}

	// ----- Functions -----//
	public void attack() throws HeadlessException, SQLException {
		if (playerVolted) {
			attackPos = (short) (player.getPos() - 1);
			checkAtkColl(attackPos);
			entityPanel.replaceCell((short) (attackPos), (byte) 6);
			entityPanel.replaceCell(player.getPos(), (byte) 4);
			attacking = true;

		} else {
			attackPos = (short) (player.getPos() + 1);
			checkAtkColl(attackPos);
			entityPanel.replaceCell((short) (attackPos), (byte) 5);
			entityPanel.replaceCell(player.getPos(), (byte) 3);
			attacking = true;

		}

		refreshPanel();
	}

	public void drinkPotion() throws SQLException {
		if (player.getCoins() >= 20 && player.getLifes() < player.getMaxLifes()) {
			heal((byte) 1);
			removeCoins((byte) 20);
		} else if (player.getCoins() < 20) {
			JOptionPane.showMessageDialog(null, "(Requiere 20 Rupias)", "Curación", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Tienes la vida completa", "Curación", JOptionPane.ERROR_MESSAGE);
		}

		refreshPanel();
	}

	public void upgradeWeapon() throws SQLException {
		if (player.getCoins() >= 15 && player.findItem("Sharped Stone")) {
			player.getWeapon().upgradeWeapon();
			player.removeCoins((byte) 15);
			player.deleteItem("Piedra");

			JOptionPane.showMessageDialog(null,
					"Arma afilada, Daño +1 \n\nDaño: " + player.getWeapon().getDamage(), "Afilar",
					JOptionPane.PLAIN_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "(Require 15 Rupees and 1 Sharped Stone)", "Afilar",
					JOptionPane.ERROR_MESSAGE);
		}
		
		refreshPanel();
	}
	
	public void seeInventory() throws SQLException {
		if (player.getIventory().size() > 0) {
			new InventoryFrame(player.getIventory()).setVisible(true);;
		} else {
			new InventoryFrame(player.getIventory()).setVisible(true);;
		}

		refreshPanel();
	}
	
	public void seeWeapon() throws SQLException {
		new SeeWeaponFrame(player.getWeapon(), player, conDB);
		
		refreshPanel();
	}

	public void collCoin() {
		player.collCoin();
		soundPlayer.playSound("resources/audio/rupee.wav");
		refreshCoins();
	}

	public void collCoin(byte nCoins) {
		player.collCoin(nCoins);
		soundPlayer.playSound("resources/audio/rupee.wav");
		refreshCoins();
	}

	public void removeCoins(byte nCoins) {
		player.removeCoins(nCoins);
		refreshCoins();
	}

	public void spendCoins(byte nCoins) {
		player.removeCoins(nCoins);
		soundPlayer.playSound("resources/audio/spendRupee.wav");
		refreshCoins();
	}

	public void heal(byte nHeal) {
		player.heal(nHeal);
		soundPlayer.playSound("resources/audio/heal.wav");
		refreshLifes();
	}

	public void fall() {
		player.fall();
		soundPlayer.playSound("resources/audio/fall.wav");
		refreshLifes();
	}

	public void damage() {
		player.damage();
		soundPlayer.playSound("resources/audio/damage.wav");
		refreshLifes();
	}

	public void damage(byte dmg) {
		player.damage(dmg);
		soundPlayer.playSound("resources/audio/damage.wav");
		refreshLifes();
	}

	public void death() throws SQLException {
		player.death();
		refreshFrame();
	}

	// ----- Refresh -----//
	public void refreshFrame() throws SQLException {
		refreshStone();
		
		refreshEnemies();
		checkLastPositions();
		checkPlayerEnemiesColl();

		refreshLifes();
		refreshCoins();

		refreshAtk();
		checkPlayerFall();
		checkPlayerInAir();

		refreshPlayerPos();
		refreshPanel();
	}

	public void refreshLifes() {
		for (int i = 0; i < player.getMaxLifes(); i++) {
			if (i < player.getLifes())
				entityPanel.replaceCell((byte) i, (byte) 21);
			else
				entityPanel.replaceCell((byte) i, (byte) 20);
		}
	}

	public void refreshCoins() {
		byte coins = player.getCoins();
		byte n1 = (byte) (coins % 10);
		byte n2 = (byte) (coins / 10);

		entityPanel.replaceCell((short) 30, (byte) 13);
		entityPanel.replaceCell((short) 31, (byte) (30 + n2));
		entityPanel.replaceCell((short) 32, (byte) (30 + n1));
	}

	public void refreshEnemies() throws SQLException {
		for (int i = 0; i < entityPanel.getEnemies().size(); i++) {
			entityPanel.getEnemies().get(i).setLastPos(entityPanel.getEnemies().get(i).getPos());
			if (entityPanel.getEnemies().get(i).getPos() == entityPanel.getEnemies().get(i).getLastPos()) {
				entityPanel.replaceCell(entityPanel.getEnemies().get(i).getPos(), (byte) entityPanel.getEnemies().get(i).getiId());
			}
			
			moveEnemy(entityPanel.getEnemies().get(i));
		}
	}

	public void refreshPanel() throws SQLException {
		entityPanel.refreshPanel();
		refreshStone();
	}
	
	public void refreshStone() {
		if (hasStone)
			entityPanel.replaceCell(stonePos, (byte) 22);
	}

	public void refreshPlayerPos() throws SQLException {
		if (playerVolted) {
			entityPanel.replaceCell(player.getPos(), (byte) 2);
			playerActId = 2;
		} else {
			entityPanel.replaceCell(player.getPos(), (byte) 1);
			playerActId = 1;
		}
		refreshAtk();

		entityPanel.refreshPanel();
	}

	public void refreshAtk() {
		if (attacking) {
			entityPanel.replaceCell(attackPos, (byte) 0);
			attacking = false;
			refreshStone();
		}
	}

	// ----- Change Stage -----//
	public void ChangeStage() throws SQLException, IOException {
		Random r = new Random();
		byte stageR = (byte) (r.nextInt(conDB.countStages()) + 1);
		
		if (conDB.selectTerrain(stageR) == null || conDB.selectEntities(stageR) == null) {
			throw new InvalidStageIdException();
		}
		
		GameFrame gameFrame = new GameFrame(stageR, player, conDB);
		parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		parentFrame.dispose();
		gameFrame.setVisible(true);
	}
	
	public void ChangeStage(byte stageId) throws SQLException, IOException {
		GameFrame gameFrame = new GameFrame(stageId, player, conDB);
		parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		parentFrame.dispose();
		gameFrame.setVisible(true);
	}

	// ----- Boxes -----//
	public void getItem(Item nItem) throws SQLException {
		
		if (conDB.selectItem(playerActId) == null) {
			throw new InvalidItemIdException();
		} 
		
		if (conDB.selectWeapon(playerActId) == null) {
			throw new InvalidWeaponIdException();
		} 
		
		if (nItem instanceof Weapon) {
			Weapon nWeapon = (Weapon) nItem;
			new WeaponFrame(nItem.getName(), nItem.getImage(), nItem.getDesc(), nWeapon.getDamage(), player, conDB);

		} else {
			new ItemFrame(nItem.getName(), nItem.getImage(), nItem.getDesc(), player, conDB);
			switch (nItem.getName()) {
			case "Small Potion":
				heal((byte) 2);
				break;
			case "Normal Potion":
				heal((byte) 4);
				break;
			case "Big Potion":
				heal((byte) 6);
				break;
			case "Green Rupee":
				collCoin();
				break;
			case "Blue Rupee":
				collCoin((byte) 5);
				break;
			case "Red Rupee":
				collCoin((byte) 20);
				break;
			case "Purple Rupee":
				collCoin((byte) 50);
				break;
			default:
				break;
			}
			
			//JOptionPane.showMessageDialog(null, "Has conseguido un objeto: " + nItem.getName(), "Inventario",JOptionPane.PLAIN_MESSAGE);
		}
	}
	

	public void brokeBox() throws SQLException {
		Random r = new Random();
		byte rNumber = (byte) r.nextInt(100);
		byte type;
        if (rNumber < 10) {
        	type = 0; // 10% de probabilidad de obtener Arma
        } else {
        	type = 1; // 90% de probabilidad de obtener Objeto
        }
		byte itemR;
		Item item;
		
		if (type == 0) {
			itemR = (byte) r.nextInt(conDB.countWeapons());
			item = conDB.selectWeapon((byte) (itemR+1));
		} else {
			itemR = (byte) r.nextInt(conDB.countItems());
			item = conDB.selectItem((byte) (itemR+1));
		}
		try {
			getItem(item);
		} catch (InvalidItemIdException | InvalidWeaponIdException e) {
			e.printStackTrace();
			brokeBox((byte) 0);
		}
	}

	public void brokeBox(Item nItem) throws SQLException {
		getItem(nItem);
	}
	
	public void brokeBox(byte itemId) throws SQLException {
		Item item = conDB.selectWeapon((byte) itemId);
		
		getItem(item);
	}

	// ----- Checking -----//
	public byte checkUpSpace(byte num) throws SQLException {
		byte spaces = 0;
		for (int i = 1; i <= num; i++) {
			if (entityPanel.selECell((short) (player.getPos() - (30 * i)), actStage) == 10 && i < num) {
				entityPanel.replaceCell((short) (player.getPos() - (30 * i)), (byte) 0);
				collCoin();
			}
			if (terrainPanel.selTCell((short) (player.getPos() - (30 * i)), actStage) == 0) {
				spaces++;
			} else {
				break;
			}
		}
		return spaces;
	}

	public void checkLastPositions() {
		for (int i = 0; i < entityPanel.getEnemies().size(); i++) {
			if (entityPanel.getEnemies().get(i).getLastPos() == player.getPos()
					&& player.getLastPos() == entityPanel.getEnemies().get(i).getPos()) {
				damage(entityPanel.getEnemies().get(i).getBaseAtk());
			}
		}
	}

	public void checkPlayerEnemiesColl() {
		for (byte i = 0; i < entityPanel.getEnemies().size(); i++) {
			if (player.getPos() == entityPanel.getEnemies().get(i).getPos()) {
				damage(entityPanel.getEnemies().get(i).getBaseAtk());
				refreshLifes();
			}
		}
	}

	public void checkPlayerFall() {
		if (player.getPos() / 30 == 19) {
			entityPanel.replaceCell(player.getPos(), (byte) 0);
			fall();
			entityPanel.replaceCell(player.getPos(), (byte) 1);
		}
	}

	public boolean checkPassColl(short nPos) throws SQLException {
		boolean pass = false;
		byte id = entityPanel.selECell(nPos, actStage);

		for (byte i = 0; i < entityPanel.getTrasEnt().length; i++) {
			if (id == entityPanel.getTrasEnt()[i]) {
				pass = true;
			}
		}

		switch (id) {
		case 10:
			collCoin();
			break;
		default:
			break;
		}

		return pass;
	}

	public void checkAtkColl(short nPos) throws HeadlessException, SQLException {

		switch (entityPanel.selECell(nPos, actStage)) {
		case 0:
			soundPlayer.playSound("resources/audio/miss.wav");
			break;
		case 9:
			soundPlayer.playSound("resources/audio/box.wav");
			brokeBox();
			break;
		case 22:
			if (player.findItem("Sacred Stone")) {
				player.deleteItem("Sacred Stone");
				JOptionPane.showMessageDialog(null, "Has grabado tu nombre en esta piedra", "Guardado",
						JOptionPane.PLAIN_MESSAGE);
				conDB.savePlayer(player, actStage);
			} else {
				JOptionPane.showMessageDialog(null, "(Requiere 1 Piedra Sagrada)", "Guardado",
						JOptionPane.ERROR_MESSAGE);
			}
			break;
		default:
			for (byte i = 0; i < entityPanel.getEnemies().size(); i++) {
				if (nPos == entityPanel.getEnemies().get(i).getPos()) {
					soundPlayer.playSound("resources/audio/hit.wav");
					Enemy enemy = entityPanel.getEnemy(nPos);
					if (enemy != null) {
						enemy.damage(
								(player.getWeapon() == null) ? player.getBaseAtk() : player.getWeapon().getDamage());
						if (enemy.getLifes() <= 0) {
							brokeBox(conDB.selectItem(enemy.getReward()));
							ArrayList<Enemy> nList = entityPanel.getEnemies();
							nList.remove(entityPanel.getEnemyIndex(nPos));
							entityPanel.setEnemies(nList);

							entityPanel.replaceCell(nPos, (byte) 0);

							refreshPanel();
						}
					}
				}
			}
			break;
		}
	}

	public short findId(byte nId) throws SQLException {
		for (short i = 0; i < 600; i++) {
			if (entityPanel.selECell(i, actStage) == nId) {
				return i;
			}
		}
		return 0;
	}

	public void checkSaveStone() throws SQLException {
		stonePos = findId((byte) 22);
		if (stonePos != 0) {
			hasStone = true;
		} else {
			hasStone = false;
		}
	}

	public void checkPlayerInAir() throws SQLException {
		playerInAir = (terrainPanel.selTCell((short) (player.getPos() + 30), actStage) == 0)
				&& (entityPanel.selECell((short) (player.getPos() + 30), actStage) != 9);
	}

	// ----- Actions -----//
	public void typeKey(char nKey) throws SQLException, IOException {
		if (nKey == 'w' || nKey == 'a' || nKey == 's' || nKey == 'd' || nKey == ' ') {
			movePlayer(nKey);
		} else if (nKey == 'e') {
			attack();
		} else if (nKey == 'q') {
			drinkPotion();
		} else if (nKey == 'r') {
			upgradeWeapon();
		} else if (nKey == 'i') {
			seeInventory();
		} else if (nKey == 'o') {
			seeWeapon();
		}

	}

}
