package Items;

import javax.swing.ImageIcon;

public class Item {

	private String name;
	private String desc;
	private String imageLink;
	private Float probability;
	private ImageIcon image;

	public Item(String nName, String nDesc, String nImageLink, Float nProbability) {
		setName(nName);
		setDesc(nDesc);
		setImageLink(nImageLink);
		setProbability(nProbability);
	}
	
	public Item(String nName, String nDesc, ImageIcon nImage) {
		setName(nName);
		setDesc(nDesc);
		setImage(nImage);
	}

	public Float getProbability() {
		return probability;
	}

	public void setProbability(Float probability) {
		this.probability = probability;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String nName) {
		name = nName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
		setImage(new ImageIcon(imageLink));
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}

	
	
}
