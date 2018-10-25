import javax.swing.JButton;

public class Field extends JButton{
	
	private int x;
	private int y;
	private boolean isBomb;
	
	public Field (int x,int y, boolean bomb) {
		super();
		this.x = x;
		this.y=y;
		this.isBomb=bomb;
	}
	

	public int getPositionX() {
		return x;
	}

	public void setPositionX(int x) {
		this.x = x;
	}

	public int getPositionY() {
		return y;
	}

	public void setPositionY(int y) {
		this.y = y;
	}

	public boolean isBomb() {
		return isBomb;
	}

	public void setBomb(boolean isBomb) {
		this.isBomb = isBomb;
	}

}
