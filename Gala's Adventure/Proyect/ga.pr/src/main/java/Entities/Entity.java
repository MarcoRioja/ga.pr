package Entities;

import java.sql.SQLException;

public class Entity {

	byte lifes; // - Vidas - //
	short pos;
	short lastPos;
	byte baseAtk;

	public Entity(byte nLifes, byte nBaseAtk, short nPos) {
		this.setLifes(nLifes);
		this.setPos(nPos);
		this.setBaseAtk(nBaseAtk);
	}

	public Entity() {

	}

	public byte getLifes() {
		return lifes;
	}

	public void setLifes(byte nLifes) {
		lifes = nLifes;

		if (nLifes < 0) {
			lifes = 0;
		} else if (nLifes > 10) {
			lifes = 10;
		}
	}

	public short getPos() {
		return pos;
	}

	public void setPos(short nPos) {
		pos = nPos;
		if (nPos < 0) {
			pos = 0;
		}
	}
	
	public short getLastPos() {
		return lastPos;
	}
	
	public void setLastPos(short lastPos) {
		this.lastPos = lastPos;
	}

	public void moveX(byte pos) throws SQLException {
		setPos((short) (getPos() + pos));
	}

	public void moveY(byte pos) throws SQLException {
		setPos((short) (getPos() - (pos * 30)));
	}

	public byte getBaseAtk() {
		return baseAtk;
	}

	public void setBaseAtk(byte baseAtk) {
		this.baseAtk = baseAtk;
	}

	/**
	 * Quitar Vida Resta 1 de vida
	 */
	public void damage() {
		setLifes((byte) (getLifes() - 1));
	}

	/**
	 * Quitar Vida Resta x de vida
	 * 
	 * @param dmg Dmg to do
	 */
	public void damage(byte dmg) {
		setLifes((byte) (getLifes() - dmg));
	}

	public void heal(byte nLifes) {
		setLifes((byte) (getLifes() + nLifes));
	}
}
