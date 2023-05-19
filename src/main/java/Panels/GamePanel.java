package Panels;

import java.awt.Image;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import DB.DB;
import Entities.PlayerE;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {

	
	//----- Panels -----//
	private JLayeredPane layeredPane = null;
	private TerrainPanel terrainPanel = null;
	private EntityPanel entityPanel = null;
	private IntroPanel introPanel = null;
	private GameController gameController = null;
	
	private PlayerE player; //Player
	
	private byte stageId; //Stage Id
	private DB conDB;

	//----- Stage Panel -----//
	public GamePanel(byte nStageId, PlayerE nPlayer, DB nConDb) throws SQLException {
		setPlayer(nPlayer);
		setStageId(nStageId);
		conDB = nConDb;
		
		JPanel backgroundPanel = new JPanel(); // Background Panel
		Image img = new ImageIcon("resources/imgs/backs/1.png").getImage();
		ImageIcon background = new ImageIcon(img.getScaledInstance(960, 640, Image.SCALE_SMOOTH));
		JLabel backgroundLabel = new JLabel(background);
		backgroundPanel.add(backgroundLabel);
		backgroundPanel.setSize(30 * 32, 20 * 32);

		terrainPanel = new TerrainPanel(stageId, conDB); // Terrain Panel
		terrainPanel.setOpaque(false);
		entityPanel = new EntityPanel(stageId,conDB); // Entities Panel
		entityPanel.setOpaque(false);
		setGameController(new GameController(stageId, player, conDB, entityPanel, terrainPanel)); // Game Controller Panel
		getGameController().setOpaque(false);
		getGameController().requestFocus();
		
		setLayeredPane(new JLayeredPane());

		getLayeredPane().add(backgroundPanel, Integer.valueOf(0));
		getLayeredPane().add(terrainPanel, Integer.valueOf(1));
		getLayeredPane().add(entityPanel, Integer.valueOf(2));
		getLayeredPane().add(getGameController(), Integer.valueOf(3));
	}

	//----- Intro Panel -----//
	public GamePanel(PlayerE nPlayer) throws SQLException {
		setPlayer(nPlayer);
		
		JPanel backgroundPanel = new JPanel(); // Background Panel
		Image img = new ImageIcon("resources/imgs/backs/introBackground.png").getImage();
		ImageIcon background = new ImageIcon(img.getScaledInstance(960, 640, Image.SCALE_SMOOTH));
		JLabel backgroundLabel = new JLabel(background);
		backgroundPanel.add(backgroundLabel);
		backgroundPanel.setSize(30 * 32, 20 * 32);

		introPanel = new IntroPanel(player);
		introPanel.setOpaque(false);
		introPanel.setSize(30 * 32, 20 * 32);

		setLayeredPane(new JLayeredPane());

		getLayeredPane().add(backgroundPanel, Integer.valueOf(0));
		getLayeredPane().add(introPanel, Integer.valueOf(1));

	}

	public PlayerE getPlayer() {
		return player;
	}

	public void setPlayer(PlayerE player) {
		this.player = player;
	}
	
	public byte getStageId() {
		return stageId;
	}

	public void setStageId(byte stageId) {
		this.stageId = stageId;
	}

	public JLayeredPane getLayeredPane() {
		return layeredPane;
	}

	public void setLayeredPane(JLayeredPane layeredPane) {
		this.layeredPane = layeredPane;
	}

	public GameController getGameController() {
		return gameController;
	}

	public void setGameController(GameController gameController) {
		this.gameController = gameController;
	}
	
}
