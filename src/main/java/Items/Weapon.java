package Items;

import javax.swing.ImageIcon;

public class Weapon extends Item {

	byte damage;

	public Weapon(String nName, String nDesc, String nImageLink, byte nDamage, Float probability) {
		super(nName, nDesc, nImageLink, probability);
		setDamage(nDamage);
	}
	
	public Weapon(String nName, String nDesc, ImageIcon nImage, byte nDamage) {
		super(nName, nDesc, nImage);
		setDamage(nDamage);
	}

	public byte getDamage() {
		return damage;
	}

	public void setDamage(byte nDamage) {
		damage = nDamage;
		if (nDamage < 0) {
			damage = 0;
		}
	}
	
	public void upgradeWeapon() {
		setDamage((byte) (getDamage()+1));
	}

}
