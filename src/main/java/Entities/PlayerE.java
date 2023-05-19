package Entities;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Features.AlertMessage;
import Items.Item;
import Items.Weapon;

public class PlayerE extends Entity {

	public byte id;
	byte rid;
	byte coins; // - Monedas - //
	String name; // - Nombre del Player - //
	Weapon weaponEquiped; // - Arma Equipada - //
	ArrayList<Item> inventory = new ArrayList<Item>(); // - Inventario - //

	byte maxLifes;
	boolean playerInAir;
	boolean playerAttacking;
	short attackPos;
	boolean playerVolted;

	public String alertMessage = "";
	public String alertSection = "";

	/**
	 * Constructor de Player
	 * 
	 * @param nLifes         Vidas
	 * @param nName          Nombre
	 * @param nCoins         Monedas
	 * @param nWeaponEquiped Arma Equipada
	 * @param nInventory     Inventario
	 * @throws SQLException
	 */
	public PlayerE(byte nLifes, byte nBaseAtk, short nPos, String nName, byte nCoins, Weapon nWeaponEquiped,
			ArrayList<Item> nInventory) {
		super(nLifes, nBaseAtk, nPos);
		setName(nName);
		setCoins(nCoins);
		setWeapon(nWeaponEquiped);
		setInventory(nInventory);
		
		setMaxLifes((byte) 10);
		
		id = 1;
		rid = 2;
	}

	/**
	 * Get Monedas
	 * 
	 * @return Monedas
	 */
	public byte getCoins() {
		return coins;
	}

	/**
	 * Set Monedas Si nCoins > 99 = 99 Si nCoins < 0 = 0
	 * 
	 * @param nCoins
	 */
	public void setCoins(byte nCoins) {
		coins = nCoins;
		if (nCoins > 99) {
			coins = 99;
		}

		if (nCoins < 0) {
			coins = 0;
		}
	}

	/**
	 * Get Nombre
	 * 
	 * @return Nombre
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set Nombre
	 * 
	 * @param nName
	 */
	public void setName(String nName) {
		name = nName;
		// Si el nombre no contiene nada poner Default
		if (nName == null || nName.replaceAll(" ", "").equals("")) {
			name = "Default";
		}
	}

	/**
	 * Get Arma Equipada
	 * 
	 * @return Arma Equipada
	 */
	public Weapon getWeapon() {
		return weaponEquiped;
	}

	/**
	 * Set Arma Equipada
	 * 
	 * @param nWeaponEquiped
	 */
	public void setWeapon(Weapon nWeaponEquiped) {
		weaponEquiped = nWeaponEquiped;
		// Si el Arma es nula
		if (nWeaponEquiped == null) {
			weaponEquiped = new Weapon("Scarlet", "The blade is dented, chipped and dirty. Its hilt is black with some worn leather straps and a red gemstone on the pommel. (Base Damage: 1)", "resources/imgs/icons/sw_1.png", (byte) 1, 0.00f);
		}
	}

	/**
	 * Get Inventario
	 * 
	 * @return Inventario
	 */
	public ArrayList<Item> getIventory() {
		return inventory;
	}

	/**
	 * Set Inventario
	 * 
	 * @param nInventory
	 */
	public void setInventory(ArrayList<Item> nInventory) {
		inventory = nInventory;
		// Si el Inventario es nulo crear uno vacío
		if (nInventory == null) {
			inventory = new ArrayList<Item>();
		}
	}

	public void addToInventory(Item nItem) {
		inventory.add(nItem);
	}
	
	public void removeItem(byte nId) {
		inventory.remove(nId);
	}

	/**
	 * Caer Resta 1 de vida y devuelve a posicion inicial
	 */
	public void fall() {
		setLifes((byte) (getLifes() - 1));
		setPos((short) 480);
	}

	/**
	 * Quitar Vida Resta 1 de vida
	 */
	@Override
	public void damage() {
		setLifes((byte) (getLifes() - 1));
	}

	/**
	 * Quitar Vida Resta x de vida
	 * 
	 * @param dmg Dmg to do
	 */
	@Override
	public void damage(byte dmg) {
		setLifes((byte) (getLifes() - dmg));
	}

	@Override
	public void setLifes(byte nLifes) {
		lifes = nLifes;

		if (nLifes <= 0) {
			lifes = 0;
		} else if (nLifes > 10) {
			lifes = 10;
		}

		if (lifes == 0) {
			death();
		}
	}

	public void death() {
		for (int i = 0; i < getIventory().size(); i++) {
			getIventory().remove(i);
		}

		if (getWeapon() != null) {
			setWeapon(null);
		}
		setCoins((byte) 0);
		setLifes((byte) 10);
		setPos((short) 480);

		JOptionPane.showMessageDialog(null,
				"Todos tus objetos han caido al abismo más profundo, por suerte por el poder de la amistad (#Pokemon) te has recuperado.",
				"Muerte", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Collect a coin
	 */
	public void collCoin() {
		setCoins((byte) (getCoins() + 1));
	}
	
	public void collCoin(byte nCoins) {
		setCoins((byte) (getCoins() + nCoins));

	}
	
	public void removeCoins(byte nCoins) {
		setCoins((byte) (getCoins()-nCoins));
	}
	
	public boolean findItem(String item) {
		
		for (int i = 0; i < getIventory().size(); i++) {
			if (getIventory().get(i).getName().equals(item)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void deleteItem(String item) {
		
		for (int i = 0; i < getIventory().size(); i++) {
			if (getIventory().get(i).getName().equals(item)) {
				getIventory().remove(i);
			}
		}
	}

	public byte getMaxLifes() {
		return maxLifes;
	}

	public void setMaxLifes(byte maxLifes) {
		if (maxLifes <= 0) {
			maxLifes = 1;
		}
		this.maxLifes = maxLifes;
	}
	
	public boolean isPlayerInAir() {
		return playerInAir;
	}

	public void setPlayerInAir(boolean playerInAir) {
		this.playerInAir = playerInAir;
	}

	public boolean isPlayerAttacking() {
		return playerAttacking;
	}

	public void setPlayerAttacking(boolean playerAttacking) {
		this.playerAttacking = playerAttacking;
	}

	public short getAttackPos() {
		return attackPos;
	}

	public void setAttackPos(short attackPos) {
		this.attackPos = attackPos;
	}

	public boolean isPlayerVolted() {
		return playerVolted;
	}

	public void setPlayerVolted(boolean playerVolted) {
		this.playerVolted = playerVolted;
	}
	

}
