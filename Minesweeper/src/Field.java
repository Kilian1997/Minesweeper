import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class Field extends JButton{
	
	private int x;
	private int y;
	private boolean isBomb;
	private Image img;
	private boolean inGame;
	



	public Field (int x,int y, boolean bomb) {
		super();
		this.x = x;
		this.y=y;
		this.isBomb=bomb;
		inGame=false;
		
			try {
				img = ImageIO.read(getClass().getResource("pictures/Bombe.png"));
//				f.setIcon(new ImageIcon(img.getScaledInstance(
//						(int)((Math.min(frame.getHeight(), frame.getWidth()) / realBoard.length)/1.25),
//						(int)((Math.min(frame.getHeight(), frame.getWidth()) / realBoard.length)/1.25), Image.SCALE_FAST)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}
	
	public boolean isInGame() {
		return inGame;
	}


	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
	

	public Image getImg() {

		return img;
	}


	public void setImg(Image img) {
		this.img = img;
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
