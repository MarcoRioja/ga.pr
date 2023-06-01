package Panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import DB.DB;
import Entities.PlayerE;
import Features.SoundPlayer;
import Frames.GameFrame;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

@SuppressWarnings("serial")
public class IntroPanel extends JPanel {
	ImageIcon title, cont, newG, quit;
	
	DB conDB;
	
	JFrame parentFrame; 
	
	PlayerE player;

	public IntroPanel(PlayerE nPlayer) throws SQLException {
		conDB = new DB();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(100, 10, 50, 10));
		
		final SoundPlayer soundPlayer = new SoundPlayer();
        soundPlayer.playSoundLooped("resources/audio/BattleTheme.wav");

		setPlayer(nPlayer);

		// Add "Gala's Adventure"
		title = new ImageIcon("resources/imgs/ui/title.png");
		JLabel titleLbl = new JLabel(title);
		titleLbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
		        soundPlayer.playSound("resources/audio/MainTheme.wav");
			}
		});
		titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(titleLbl);

		// Add New Game
		newG = new ImageIcon("resources/imgs/ui/newGBtn.png");
		JLabel newGLbl = new JLabel(newG);
		newGLbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				soundPlayer.playSound("resources/audio/click.wav");
				PlayerE player = new PlayerE((byte) 10, (byte) 1, (short) 480, JOptionPane.showInputDialog(null, "Player Name:"), (byte) 0, null, null);
					Random r = new Random();
					byte stageR = (byte) (r.nextInt(2) + 1);
					try {
						conDB.createPlayer(player,stageR);
						ChangeStage(player,stageR);
					} catch (SQLException | IOException e1) {
						e1.printStackTrace();
					}
			}
		});
		newGLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(Box.createRigidArea(new Dimension(0, 100)));
		add(newGLbl);

		// Add Continue
		cont = new ImageIcon("resources/imgs/ui/contBtn.png");
		JLabel contLbl = new JLabel(cont);
		contLbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//System.out.println("¡Continuar!");
				soundPlayer.playSound("resources/audio/click.wav");
					String pName = JOptionPane.showInputDialog(null, "Nombre del Jugador:");
					try {
						ChangeStage(conDB.loadPlayer(pName),conDB.requestStage(pName));
					} catch (SQLException | IOException e1) {
						e1.printStackTrace();
					}
			}
		});
		contLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(Box.createRigidArea(new Dimension(0, 20)));
		add(contLbl);

		// Add Quit
		quit = new ImageIcon("resources/imgs/ui/quitBtn.png");
		JLabel quitLbl = new JLabel(quit);
		quitLbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				soundPlayer.playSound("resources/audio/click.wav");
				System.exit(ABORT);
			}
		});
		quitLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(Box.createRigidArea(new Dimension(0, 20)));
		add(quitLbl);
	}
	
	public void ChangeStage(PlayerE player, byte stageId) throws SQLException {
		GameFrame gameFrame = new GameFrame(stageId,player, conDB);
		parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		parentFrame.dispose();
		gameFrame.setVisible(true);
	}
	
	public PlayerE getPlayer() {
		return player;
	}
	
	public void setPlayer(PlayerE player) {
		this.player = player;
	}
}
