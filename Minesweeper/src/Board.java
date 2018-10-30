import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Board implements MouseListener {

	private boolean[][] realBoard;
	private LinkedList<Field> buttons;
	private JFrame frame;
	private JPanel panel;
	private int numberOfBombs;
	private boolean bombsGenerated;
	private JLabel label;
	private int bombsLeft;
	private JMenuBar menuBar;

	public Board(int size, int bombs) {
		if (bombs + 9 > size * size) {
			throw new IllegalArgumentException("Mehr Bomben als Felder.");
		} else {
			bombsGenerated = false;
			numberOfBombs = bombs;
			realBoard = new boolean[size][size];
			frame = new JFrame("Kilians Minesweeper");
			frame.setSize(600, 600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(true);
			frame.setLocationRelativeTo(null);
			menuBar = new JMenuBar();
			JMenu menu = new JMenu("Optionen");
			menuBar.add(menu);
			JMenuItem item = new JMenuItem("Neustart");
			item.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					restart();
					
				}
			});
			menu.add(item);
			frame.setJMenuBar(menuBar);

			createAndAddPanel();

			// Ausgabe des Boards zu Testzweckens
			// for(int i=0;i<realBoard.length;i++) {
			// for(int a=0;a<realBoard.length;a++) {
			// System.out.print(realBoard[i][a]+", ");
			// }
			// System.out.println();
			// }
		}
	}

	private void createAndAddPanel() {
		if (panel != null || label !=null) {
			frame.remove(panel);
			frame.remove(label);
		}
		realBoard = new boolean[realBoard.length][realBoard.length];
		// placeBombs(numberOfBombs);
		panel = new JPanel();
		panel.setBackground(Color.white);
		panel.setLayout(new GridLayout(realBoard.length, realBoard.length));
		buttons = new LinkedList<Field>();

		int x = 0;
		int y = 0;
		for (int i = 0; i < realBoard.length * realBoard.length; i++) {
			buttons.add(new Field(x + 1, y + 1, realBoard[x][y]));
			panel.add(buttons.get(i));
			buttons.get(i).addMouseListener(this);
			buttons.get(i).setFont(new Font("Monospaced", Font.ITALIC, 12));
			buttons.get(i).setMargin(new Insets(0, 0, 0, 0));
			if (x < realBoard.length - 1) {
				x++;
			} else {
				x = 0;
				y++;
			}
		}
		label = new JLabel("Noch zu findende Bomben:   " +numberOfBombs);
		bombsLeft = numberOfBombs;
		label.setBackground(Color.WHITE);
		frame.add(label, BorderLayout.SOUTH);
		frame.add(panel, BorderLayout.CENTER);
		
		frame.setVisible(true);
	}

	private void placeBombs(int numberOfBombs, Field f) {
		bombsGenerated = true;
		while (numberOfBombs > 0) {
			int p1 = (int) (Math.random() * realBoard.length);
			int p2 = (int) (Math.random() * realBoard.length);
			if (realBoard[p1][p2] == false && !(p1 == f.getPositionX() - 1 && p2 == f.getPositionY() - 1)
					&& !(p1 == f.getPositionX() && p2 == f.getPositionY() - 1)
					&& !(p1 == f.getPositionX() && p2 == f.getPositionY())
					&& !(p1 == f.getPositionX() && p2 == f.getPositionY() - 2)
					&& !(p1 == f.getPositionX()-2 && p2 == f.getPositionY() - 1)
					&& !(p1 == f.getPositionX()-2 && p2 == f.getPositionY() )
					&& !(p1 == f.getPositionX()-2 && p2 == f.getPositionY() - 2)
					&& !(p1 == f.getPositionX() - 1 && p2 == f.getPositionY())
					&& !(p1 == f.getPositionX() - 1 && p2 == f.getPositionY() - 2)
					) { // funzt nicht!!!!!
				realBoard[p1][p2] = true;
				numberOfBombs--;
			}
		}
		int x = 0;
		int y = 0;
		for (int i = 0; i < realBoard.length * realBoard.length; i++) {
			buttons.get(i).setBomb(realBoard[x][y]);
			if (x < realBoard.length - 1) {
				x++;
			} else {
				x = 0;
				y++;
			}
		}
	}

	private int bombsNextby(int x, int y) {
		int result = 0;
		if (x + 1 <= realBoard.length - 1 && realBoard[x + 1][y]) {
			result++;
		}
		if (x + 1 <= realBoard.length - 1 && y + 1 <= realBoard[x + 1].length - 1 && realBoard[x + 1][y + 1]) {
			result++;
		}
		if (x + 1 <= realBoard.length - 1 && y - 1 >= 0 && realBoard[x + 1][y - 1]) {
			result++;
		}
		if (x - 1 >= 0 && y - 1 >= 0 && realBoard[x - 1][y - 1]) {
			result++;
		}
		if (x - 1 >= 0 && y + 1 <= realBoard[x - 1].length - 1 && realBoard[x - 1][y + 1]) {
			result++;
		}
		if (x - 1 >= 0 && realBoard[x - 1][y]) {
			result++;
		}
		if (y + 1 <= realBoard[x].length - 1 && realBoard[x][y + 1]) {
			result++;
		}
		if (y - 1 >= 0 && realBoard[x][y - 1]) {
			result++;
		}
		return result;
	}

	private void clickAllAround(int x, int y) {
		for (Field f : buttons) {
			if (f.getPositionX() + 1 == x && f.getPositionY() == y) {
				pressField(f);
			}
			if (f.getPositionX() - 1 == x && f.getPositionY() == y) {
				pressField(f);
			}
			if (f.getPositionX() == x && f.getPositionY() + 1 == y) {
				pressField(f);
			}
			if (f.getPositionX() == x && f.getPositionY() - 1 == y) {
				pressField(f);
			}
			if (f.getPositionX() + 1 == x && f.getPositionY() + 1 == y) {
				pressField(f);
			}
			if (f.getPositionX() + 1 == x && f.getPositionY() - 1 == y) {
				pressField(f);
			}
			if (f.getPositionX() - 1 == x && f.getPositionY() + 1 == y) {
				pressField(f);
			}
			if (f.getPositionX() - 1 == x && f.getPositionY() - 1 == y) {
				pressField(f);
			}
		}
	}
	
	private void restart () {
		for (Field f : buttons) {
			f.removeMouseListener(this);
		}
		bombsGenerated = false;
		createAndAddPanel();
	}

	private void loose() {
		for (Field f : buttons) {
			f.removeMouseListener(this);
			if (f.isBomb()) {
				Image img;
				try {
					img = ImageIO.read(getClass().getResource("pictures/Bombe.png"));
					f.setIcon(new ImageIcon(img.getScaledInstance(
							(int)((Math.min(frame.getHeight(), frame.getWidth()) / realBoard.length)/1.25),
							(int)((Math.min(frame.getHeight(), frame.getWidth()) / realBoard.length)/1.25), Image.SCALE_FAST)));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		int again = JOptionPane.showConfirmDialog(null, "Leider Verloren\nWollen Sie es nochmal versuchen?", "Verloren",
				JOptionPane.YES_NO_OPTION);
		if (again == 0) {
			restart();
		}
	}

	private void pressField(Field field) {
		if (field.isEnabled()) {
			field.setEnabled(false);
			if (field.isBomb()) {
				loose();
			} else {
				field.setText(bombsNextby(field.getPositionX() - 1, field.getPositionY() - 1) + "");
				if (field.getText().equals("0")) {
					field.setText("");
					clickAllAround(field.getPositionX(), field.getPositionY());
				}
			}

			if (checkGameWon()) {
				int again = JOptionPane.showConfirmDialog(null,
						"Herzlichen Glückwunsch, Sie haben gewonnen!\nWollen Sie nocheinmal spielen?", "Gewonnen",
						JOptionPane.YES_NO_OPTION);
				for (Field f : buttons) {
					f.removeMouseListener(this);
				}
				if (again == 0) {
					restart();
				}
			}
		}
	}

	private boolean checkGameWon() {
		for (Field f : buttons) {
			if (!f.isBomb() && f.isEnabled()) {
				return false;
			}
		}
		return true;
	}

	@Override
	// public void actionPerformed(ActionEvent e) {
	//
	// for(Field b : buttons) {
	// if(e.getSource().equals(b)) {
	// b.setEnabled(false);
	// System.out.println("x: "+b.getPositionX()+", y: "+b.getPositionY());
	// if(realBoard[b.getPositionX()-1][b.getPositionY()-1]) {
	//
	// b.setText("Bombe");
	// }else {
	// b.setText(""+bombsNextby(b.getPositionX()-1, b.getPositionY()-1));
	// if(bombsNextby(b.getPositionX()-1, b.getPositionY()-1)==0) {
	// clickAllAround(b.getPositionX(), b.getPositionY());
	// }
	// }
	//
	// }
	// }
	// System.out.println(e.getActionCommand());
	// System.out.println(e.getSource());
	// System.out.println(e.toString());
	// buttons.get(0).setEnabled(false);
	// panel.remove(0);
	// frame.repaint();
	// frame.remove(panel);
	// frame.add(panel);
	// frame.repaint();
	// }

	public void mouseClicked(MouseEvent e) {

		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (Field b : buttons) {
			if (e.getSource().equals(b) && b.isEnabled()) {

				if (SwingUtilities.isRightMouseButton(e)) {
					if (b.getIcon() != null) {
						bombsLeft++;
						b.setIcon(null);
					} else if(bombsLeft>0) {
						try {
							Image img = ImageIO.read(getClass().getResource("pictures/Fahne.png"));
							b.setIcon(new ImageIcon(img.getScaledInstance(
									(int) ((Math.min(frame.getHeight(), frame.getWidth()) / realBoard.length) / 1.75),
									(int) ((Math.min(frame.getHeight(), frame.getWidth()) / realBoard.length) / 1.75),
									Image.SCALE_FAST)));
							bombsLeft--;
						} catch (IOException error) {
							// TODO Auto-generated catch block
							error.printStackTrace();
						}
					}
					label.setText("Noch zu findende Bomben:   "+ bombsLeft);
				} else if (b.getIcon() == null) {
					if (!bombsGenerated) {
						placeBombs(numberOfBombs, b);
					}
					pressField(b);

				}
			}
		}
	}

}
