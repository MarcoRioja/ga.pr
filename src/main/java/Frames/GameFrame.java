package Frames;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.*;

import DB.DB;
import Entities.PlayerE;
import Panels.GamePanel;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {

	byte panel;
	PlayerE player;

	public GameFrame(byte panel, PlayerE nPlayer, DB nConDB) throws SQLException {
		setPanel(panel);
		setTitle("Gala's Adventure"); // Establece el título del JFrame
		setSize((30 * 32) + 15, 21 * 32);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Establece la acción al cerrar el JFrame
		setLocationRelativeTo(null); // Centra el JFrame en la pantalla
		setVisible(true); // Muestra el JFrame

		setPlayer(nPlayer);

		final GamePanel gamePanel;

		if (panel == 0) {
			gamePanel = new GamePanel(player);
		} else {
			gamePanel = new GamePanel((byte) panel, nPlayer, nConDB);

			this.addKeyListener(new KeyListener() {
				public void keyTyped(KeyEvent e) {

					try {
						gamePanel.getGameController().typeKey(e.getKeyChar());
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

				}

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub

				}
			});
		}

		this.requestFocus();

		this.add(gamePanel.getLayeredPane());

		gamePanel.requestFocusInWindow();
	}

	public void setPanel(byte panel) {
		this.panel = panel;
	}

	public byte getPanel() {
		return panel;
	}

	public PlayerE getPlayer() {
		return player;
	}

	public void setPlayer(PlayerE player) {
		this.player = player;
	}
}
