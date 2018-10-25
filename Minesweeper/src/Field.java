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
	
	public Field (int x,int y, boolean bomb, String titel) {
		super(titel);
		this.x = x;
		this.y=y;
		this.isBomb=bomb;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isBomb() {
		return isBomb;
	}

	public void setBomb(boolean isBomb) {
		this.isBomb = isBomb;
	}

}
