package Entities;

public class Enemy extends Entity {

	short eId;
	short iId;
	byte dir;
	byte reward;
	type eType;
	public enum type {
        ZOMBIE,
        SKELETON,
        DOG
    }

	public Enemy(short nPos, short nEId, byte nIId) {
		seteId(nEId);
		setPos(nPos);
		setiId(nIId);

		switch (nIId) {
		case 7:
			setLifes((byte) 2);
			setBaseAtk((byte) 1);
			setReward((byte) 2);
			seteType(type.ZOMBIE);
			break;
		case 11:
			setLifes((byte) 3);
			setBaseAtk((byte) 2);
			setReward((byte) 3);
			seteType(type.SKELETON);
			break;
		case 14:
			setLifes((byte) 10);
			setBaseAtk((byte) 5);
			setReward((byte) 7);
			seteType(type.DOG);
			break;
		default:
			break;
		}
	}

	public short geteId() {
		return eId;
	}

	public void seteId(short eId) {
		this.eId = eId;
	}

	public short getiId() {
		return iId;
	}

	public void setiId(short nIID) {
		this.iId = nIID;
	}

	public byte getDir() {
		return dir;
	}

	public void setDir(byte dir) {
		this.dir = dir;
	}
	
	public byte getReward() {
		return reward;
	}
	
	public void setReward(byte b) {
		this.reward = b;
	}
	
	public type geteType() {
		return eType;
	}
	
	public void seteType(type eType) {
		this.eType = eType;
	}

	public void changeDir() {
		if (dir == 0) {
			dir = 1;
		} else {
			dir = 0;
		}
	}
}
