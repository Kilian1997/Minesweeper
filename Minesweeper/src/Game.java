import java.io.IOException;

public class Game {
	
	public static void main(String[] args) {
		try {
			Board board = new Board(8, 10);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
