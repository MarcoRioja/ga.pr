package DB;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import Entities.PlayerE;
import Items.Item;
import Items.Weapon;

public class DB {

	Connection connection;
	public boolean connected = false;
	byte conections = 0;
	
	String db = "ga_data";
	String user = "root";
	String password = "";
	private Date actualDate;
	private SimpleDateFormat dateFormat;
	private String dateFormated;

	/**
	 * Only Construct the DB Object
	 * 
	 * @throws SQLException
	 * @throws IOException 
	 */
	public DB() throws SQLException {
		connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:13306/" + db, user, password);
		System.out.println("Connected to database " + db + " | User: " + user + " | Password: " + password);
	}


	// -------------------- SQL Sentences --------------------//
	/**
	 * Execute Update
	 * 
	 * @param sentence
	 * @throws SQLException
	 */
	public void insert(String sentence) throws SQLException {
		Statement statement = connection.createStatement();
		statement.executeUpdate(sentence);
	}

	// ------------------- Close Connection -------------------//
	/**
	 * Close the connection
	 * 
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		this.connection.close();
	}

	// -------------------- Selects -------------------//
	/**
	 * Select the Terrain of the Stage id
	 * 
	 * @param id of the Stage
	 * @return Terrain byte matrix
	 * @throws SQLException
	 */
	public byte[][] selectTerrain(byte id) throws SQLException {
		String selectQuery = "SELECT terrain FROM stages WHERE id = " + id;
		PreparedStatement statement = connection.prepareStatement(selectQuery);
		ResultSet resultSet = statement.executeQuery(selectQuery);

		String terrain = "";

		while (resultSet.next()) {
			terrain = resultSet.getString("terrain");
		}

		String[] rows = terrain.split(";");

		byte[][] matrix = new byte[20][30];

		for (int i = 0; i < rows.length; i++) {
			String[] values = rows[i].split(",");
			for (int j = 0; j < values.length; j++) {
				matrix[i][j] = Byte.parseByte(values[j]);
			}
		}

		return matrix;

	}

	/**
	 * Select the Entities of the Stage id
	 * 
	 * @param id of the Stage
	 * @return Entities byte matrix
	 * @throws SQLException
	 */
	public byte[][] selectEntities(byte id) throws SQLException {
		String selectQuery = "SELECT entities FROM stages WHERE id = " + id;
		PreparedStatement statement = connection.prepareStatement(selectQuery);
		ResultSet resultSet = statement.executeQuery(selectQuery);

		String entities = "";

		while (resultSet.next()) {
			entities = resultSet.getString("entities");
		}

		String[] rows = entities.split(";");

		byte[][] matrix = new byte[20][30];

		for (int i = 0; i < rows.length; i++) {
			String[] values = rows[i].split(",");
			for (int j = 0; j < values.length; j++) {
				matrix[i][j] = Byte.parseByte(values[j]);
			}
		}

		return matrix;

	}

	/**
	 * Transform a Byte Matrix to String
	 * 
	 * @param matrix
	 * @return
	 */
	public String makeString(byte[][] matrix) {
		String matrixToString = "";

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				matrixToString += matrix[i][j];
				if (j < matrix[i].length - 1) {
					matrixToString += ",";
				}
			}
			if (i < matrix.length - 1) {
				matrixToString += ";";
			}
		}

		return matrixToString;
	}

	/**
	 * Upload Stage to DB
	 * 
	 * @param id       of the new Stage
	 * @param name     of the Stage
	 * @param terrain  (Byte Matrix) of the Stage
	 * @param entities (Byte Matrix) of the Stage
	 * @throws SQLException
	 */
	public void uploadStage(byte id, String name, byte[][] terrain, byte[][] entities) throws SQLException {
		String query = "INSERT INTO stages (id,name,terrain,entities) VALUES ('" + id + "','" + name + "','"
				+ makeString(terrain) + "','" + makeString(entities) + "')";
		insert(query);
	}
	
	/*
	@SuppressWarnings("resource")
	public void createPlayer(PlayerE player, byte stage) throws SQLException {
		String pNameQuery;
		PreparedStatement statement;
		ResultSet resultSet;
		String pName = "Default";
		pNameQuery = "SELECT name FROM saves WHERE name = '" + player.getName() + "'";
		statement = connection.prepareStatement(pNameQuery);
		resultSet = statement.executeQuery(pNameQuery);
		while(resultSet.next()) {
		pName = JOptionPane.showInputDialog(null, "Nombre del Jugador Ocupado:");
		pNameQuery = "SELECT name FROM saves WHERE name = '" + pName + "'";
		statement = connection.prepareStatement(pNameQuery);
		resultSet = statement.executeQuery(pNameQuery);
		
		player.setName(pName);
		}
		
		String query = "INSERT INTO saves (name,lifes,coins,baseAtk,weapon,inventory,stage) VALUES ('" + player.getName() + "','" + player.getLifes() + "','" + player.getCoins() + "','"
				+ player.getBaseAtk() + "','" + getWeaponId(player.getWeapon()) + "','" + invToString(player.getIventory()) + "','" + stage + "')";
		insert(query);
	}
	*/
	
	public void createPlayer(PlayerE player, byte stage) throws SQLException, IOException {
	    createPlayerRecursive(player, stage);
	}
	
	private void createPlayerRecursive(PlayerE player, byte stage) throws SQLException, IOException {
	    String pNameQuery = "SELECT name FROM saves WHERE name = ?";
	    PreparedStatement statement = connection.prepareStatement(pNameQuery);
	    statement.setString(1, player.getName());
	    ResultSet resultSet = statement.executeQuery();
	    
	    if (player.getName().length() > 10) {
	    	JOptionPane.showMessageDialog(null, "Name Too Long (Please Less than 10 Characters)");
	    	String pName = JOptionPane.showInputDialog(null, "Player Name:");
	        if (pName == null || pName.isEmpty()) {
	            createPlayerRecursive(player, stage);
	        } else {
	            player.setName(pName);
	            createPlayerRecursive(player, stage);
	        }
	    	createPlayerRecursive(player, stage);
	    } else
	    if (resultSet.next()) {
	    	JOptionPane.showMessageDialog(null, "Name in Use");
	    	String pName = JOptionPane.showInputDialog(null, "Player Name:");
	        if (pName == null || pName.isEmpty()) {
	            createPlayerRecursive(player, stage);
	        } else {
	            player.setName(pName);
	            createPlayerRecursive(player, stage);
	        }
	    } else {
	        String query = "INSERT INTO saves (name,lifes,coins,baseAtk,weapon,inventory,stage) VALUES (?,?,?,?,?,?,?)";
	        PreparedStatement insertStatement = connection.prepareStatement(query);
	        insertStatement.setString(1, player.getName());
	        insertStatement.setInt(2, player.getLifes());
	        insertStatement.setInt(3, player.getCoins());
	        insertStatement.setInt(4, player.getBaseAtk());
	        insertStatement.setInt(5, getWeaponId(player.getWeapon()));
	        insertStatement.setString(6, invToString(player.getIventory()));
	        insertStatement.setByte(7, stage);
	        insertStatement.executeUpdate();
	        
	        actualDate = new Date();
	        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        dateFormated = dateFormat.format(actualDate);

	        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("resources/logFile.txt", true))) {
	            bufferedWriter.write("\n- " + dateFormated + "> User Created: " + player.getName());
	        } catch (IOException e) {
	        	System.out.println("An error occurred while writing to the file: " + e.getMessage());
	        }
	    }
	}
	
	public void savePlayer(PlayerE player, byte stage) throws SQLException {
		String query = "UPDATE saves SET lifes = '" + player.getLifes() + "', coins = '" + player.getCoins() + "', baseAtk = '" 
				+ player.getBaseAtk() + "', weapon = '" + getWeaponId(player.getWeapon()) + "', inventory = '" + invToString(player.getIventory()) + "', stage = '" + stage +"' WHERE name = '" + player.getName() + "'";
		insert(query);
		
		actualDate = new Date();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormated = dateFormat.format(actualDate);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("resources/logFile.txt", true))) {
            bufferedWriter.write("\n- " + dateFormated + "> Saved - User: " + player.getName());
        } catch (IOException e) {
        	System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
	}
	
	public PlayerE loadPlayer(String playerName) throws SQLException, IOException {
		String playerQuery = "SELECT name,lifes,coins,baseAtk,weapon,inventory FROM saves WHERE name = '" + playerName + "'";
		PreparedStatement statement = connection.prepareStatement(playerQuery);
		ResultSet rs = statement.executeQuery(playerQuery);
		
		actualDate = new Date();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormated = dateFormat.format(actualDate);

		while (rs.next()) {
			String name = rs.getString("name");
			byte lifes =  rs.getByte("lifes");
			byte coins = rs.getByte("coins");
			byte baseAtk = rs.getByte("baseAtk");
			Weapon weapon = selectWeapon((byte) rs.getInt("weapon"));
			ArrayList<Item> inventory = stringToInv(rs.getString("inventory"));
			
			PlayerE newPlayer = new PlayerE(lifes,baseAtk,(short) 480,name,coins,weapon,inventory);
			
			try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("resources/logFile.txt", true))) {
	            bufferedWriter.write("\n- " + dateFormated + "> User Loaded: " + newPlayer.getName());
	        } catch (IOException e) {
	            System.out.println("An error occurred while writing to the file: " + e.getMessage());
	        }
			
			return newPlayer;
		}
		PlayerE newPlayer = new PlayerE((byte) 10, (byte) 1, (short) 480, playerName, (byte) 0, null, null);
		createPlayer(newPlayer,(byte) 1);
		
		return newPlayer;
	}
	
	public byte requestStage(String playerName) throws SQLException {
		String playerQuery = "SELECT stage FROM saves WHERE name = '" + playerName + "'";
		PreparedStatement statement = connection.prepareStatement(playerQuery);
		ResultSet rs = statement.executeQuery(playerQuery);

		while (rs.next()) {
			return rs.getByte("stage");
		}
		return (byte) 1;
	}
	
	public void deletePlayer(String playerName) throws SQLException {
		String deleteQuery = "DELETE FROM saves WHERE name = '" + playerName + "'";
		insert(deleteQuery);
	}
	
	public byte countStages() throws SQLException {
		byte nStages = 0;
		String playerQuery = "SELECT COUNT(*) FROM stages";
		PreparedStatement statement = connection.prepareStatement(playerQuery);
		ResultSet rs = statement.executeQuery(playerQuery);
		
		if (rs.next()) {
			nStages = (byte) rs.getInt(1);
        }
		
		return nStages;
		
	}
	
	public byte countWeapons() throws SQLException {
		byte nWeapon = 0;
		String countWp = "SELECT COUNT(*) FROM weapons";
		PreparedStatement statement = connection.prepareStatement(countWp);
		ResultSet rs = statement.executeQuery(countWp);
		
		if (rs.next()) {
			nWeapon = (byte) rs.getInt(1);
        }
		
		return nWeapon;
		
	}
	
	public byte countItems() throws SQLException {
		byte nItems = 0;
		String countIm= "SELECT COUNT(*) FROM items";
		PreparedStatement statement = connection.prepareStatement(countIm);
		ResultSet rs = statement.executeQuery(countIm);
		
		if (rs.next()) {
			nItems = (byte) rs.getInt(1);
        }
		
		return nItems;
		
	}
	
	public byte getWeaponId(String nName) throws SQLException {
		String weaponQuery = "SELECT id FROM weapons WHERE name = '" + nName + "'";
		PreparedStatement statement = connection.prepareStatement(weaponQuery);
		ResultSet rs = statement.executeQuery(weaponQuery);

		while (rs.next()) {
			return rs.getByte("id");
		}
		return 0;
	}
	
	public byte getWeaponId(Weapon nWeapon) throws SQLException {
		String weaponQuery = "SELECT id FROM weapons WHERE name = '" + nWeapon.getName() + "'";
		PreparedStatement statement = connection.prepareStatement(weaponQuery);
		ResultSet rs = statement.executeQuery(weaponQuery);

		while (rs.next()) {
			return rs.getByte("id");
		}
		return 0;
	}
	
	public byte getItemId(Item nItem) throws SQLException {
		String itemQuery = "SELECT id FROM items WHERE name = '" + nItem.getName() + "'";
		PreparedStatement statement = connection.prepareStatement(itemQuery);
		ResultSet rs = statement.executeQuery(itemQuery);

		while (rs.next()) {
			return rs.getByte("id");
		}
		return 0;
	}
	
	public Weapon selectWeapon(byte nId) throws SQLException {
	    String weaponQuery = "SELECT name,description,image_link,damage,probability FROM weapons WHERE id = ?";
	    PreparedStatement statement = connection.prepareStatement(weaponQuery);
	    statement.setByte(1, nId);
	    ResultSet rs = statement.executeQuery();

	    String name = null;
	    String desc = null;
	    String imageLink = null;
	    byte damage = 0;
		Float probability = null;

	    while (rs.next()) {
	        name = rs.getString("name");
	        desc = rs.getString("description");
	        imageLink = rs.getString("image_link");
	        damage = (byte) rs.getInt("damage");
			probability = (float) rs.getInt("probability");
	    }
	    return new Weapon(name, desc, imageLink, damage, probability);
	}

	
	public Item selectItem(byte nId) throws SQLException {
		String itemQuery = "SELECT name,description,image_link,probability FROM items WHERE id = ?";
		PreparedStatement statement = connection.prepareStatement(itemQuery);
		statement.setByte(1, nId);
		ResultSet rs = statement.executeQuery();

		String name = null;
		String desc = null;
		String imageLink = null;
		Float probability = null;
		
		while (rs.next()) {
			name = rs.getString("name");
			desc = rs.getString("description");
			imageLink = rs.getString("image_link");
			probability = (float) rs.getInt("probability");
		}
		return new Item(name,desc,imageLink,probability);
	}
	
	public String invToString(ArrayList<Item> inventory) throws SQLException {
		String sInventory = "";
		for (byte i = 0; i < inventory.size()-1; i++) {
			sInventory += getItemId(inventory.get(i));
			sInventory += ",";
		}
		
		return sInventory;
	}
	
	public ArrayList<Item> stringToInv(String sInventory) throws SQLException {
		ArrayList<Item> inventory = new ArrayList<Item>();
		if (!sInventory.trim().equals("")) {
			String[] splitedInv = sInventory.split(",");
			for (byte i = 0; i < splitedInv.length; i++) {
				inventory.add(selectItem(Byte.parseByte(splitedInv[i].trim())));
			}
		}
		
		
		return inventory;
	}
	
}
