import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	private int nextGameSize;
	private int nextGameBombs;
	private boolean flagNoClickingAround;

	public Board(int size, int bombs) throws IOException {
		if (bombs + 9 > size * size) {
			throw new IllegalArgumentException("Mehr Bomben als Felder.");
		} else {
			nextGameSize = size;
			nextGameBombs = bombs;
			bombsGenerated = false;
			numberOfBombs = bombs;
			realBoard = new boolean[size][size];
			frame = new JFrame("KK-Sweeper");
			frame.setSize(600, 600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(true);
			frame.setLocationRelativeTo(null);
			menuBar = new JMenuBar();
			JMenu menu = new JMenu("Optionen");
			menuBar.add(menu);
			JMenuItem item = new JMenuItem("Neustart");
			JMenuItem item2 = new JMenuItem("Spiel beenden");
			JMenu menuInMenu = new JMenu("Modus");

			// JMenuItem item2 = new JMenuItem("Schriftgröße");
			// item2.add (new JSlider(8,45,20));
			item.addActionListener(new MenuListener());

			item2.addActionListener(new MenuListener());

			ButtonGroup group = new ButtonGroup();
			JRadioButtonMenuItem modusEinfach = new JRadioButtonMenuItem("Einfach (8x8)", true);
			JRadioButtonMenuItem modusMittel = new JRadioButtonMenuItem("Mittel (14x14)");
			JRadioButtonMenuItem modusSchwer = new JRadioButtonMenuItem("Schwer (20x20)");
			modusEinfach.addActionListener(new MenuListener());
			modusMittel.addActionListener(new MenuListener());
			modusSchwer.addActionListener(new MenuListener());
			group.add(modusEinfach);
			group.add(modusMittel);
			group.add(modusSchwer);
			menuInMenu.add(modusEinfach);
			menuInMenu.add(modusMittel);
			menuInMenu.add(modusSchwer);
			menu.add(menuInMenu);
			menu.add(item);
			menu.add(item2);
			// menu.add(item2);
			
			JMenu menu2 = new JMenu ("Help");
			menuBar.add(menu2);
			JMenuItem item3 = new JMenuItem("Anleitung");
			menu2.add(item3);
			item3.addActionListener(new MenuListener());
			JMenuItem item4 = new JMenuItem("Credits");
			menu2.add(item4);
			item4.addActionListener(new MenuListener());
			
			
			frame.setJMenuBar(menuBar);
			frame.setVisible(true);
			JProgressBar ladebalken = new JProgressBar(0, 400);
			ladebalken.setValue(0);
			ladebalken.setStringPainted(true);
			frame.add(ladebalken, BorderLayout.CENTER);
			buttons = new LinkedList<Field>();
			Image img = ImageIO.read(getClass().getResource("pictures/Bombe.png"));
			for (int i = 0; i < 20 * 20; i++) {
				ladebalken.setValue(i);
				buttons.add(new Field(0, 0, false,img));
			}
			frame.remove(ladebalken);

			createAndAddPanel();
		}
	}

	private void createAndAddPanel() {
		if (panel != null || label != null) {
			frame.remove(panel);
			frame.remove(label);
		}
		flagNoClickingAround = true;
		realBoard = new boolean[nextGameSize][nextGameSize];
		// placeBombs(numberOfBombs);
		panel = new JPanel();
		panel.setBackground(Color.white);
		panel.setLayout(new GridLayout(nextGameSize, nextGameSize));

		int x = 0;
		int y = 0;
		for (int i = 0; i < nextGameSize * nextGameSize; i++) {
			buttons.get(i).setText("");
			buttons.get(i).setIcon(null);
			buttons.get(i).setEnabled(true);
			buttons.get(i).setPositionX(x + 1);
			buttons.get(i).setPositionY(y + 1);
			buttons.get(i).setInGame(true);
			buttons.get(i).setBomb(realBoard[x][y]);
			// buttons.add(new Field(x + 1, y + 1, realBoard[x][y]));
			panel.add(buttons.get(i));
			buttons.get(i).addMouseListener(this);
			buttons.get(i).setFont(new Font("Monospaced", Font.ITALIC, 28));
			buttons.get(i).setMargin(new Insets(0, 0, 0, 0));
			if (x < nextGameSize - 1) {
				x++;
			} else {
				x = 0;
				y++;
			}
		}
		label = new JLabel("Noch zu findende Bomben:   " + nextGameBombs);
		numberOfBombs = nextGameBombs;
		bombsLeft = numberOfBombs;
		label.setBackground(Color.WHITE);
		frame.add(label, BorderLayout.SOUTH);
		frame.add(panel, BorderLayout.CENTER);

		frame.setVisible(true);
	}

	private void placeBombs(int numberOfBombs, Field f) {
		flagNoClickingAround=false;
		bombsGenerated = true;
		while (numberOfBombs > 0) {
			int p1 = (int) (Math.random() * realBoard.length);
			int p2 = (int) (Math.random() * realBoard.length);
			if (realBoard[p1][p2] == false && !(p1 == f.getPositionX() - 1 && p2 == f.getPositionY() - 1)
					&& !(p1 == f.getPositionX() && p2 == f.getPositionY() - 1)
					&& !(p1 == f.getPositionX() && p2 == f.getPositionY())
					&& !(p1 == f.getPositionX() && p2 == f.getPositionY() - 2)
					&& !(p1 == f.getPositionX() - 2 && p2 == f.getPositionY() - 1)
					&& !(p1 == f.getPositionX() - 2 && p2 == f.getPositionY())
					&& !(p1 == f.getPositionX() - 2 && p2 == f.getPositionY() - 2)
					&& !(p1 == f.getPositionX() - 1 && p2 == f.getPositionY())
					&& !(p1 == f.getPositionX() - 1 && p2 == f.getPositionY() - 2)) { // funzt nicht!!!!!
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
		if (x >= 0 && y >= 0) {
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
		}
		return result;
	}

	private void clickAllAround(int x, int y) {
		for (Field f : buttons) {
			if (f.getPositionX() + 1 == x && f.getPositionY() == y && !flagNoClickingAround) {
				pressField(f);
			}
			else if (f.getPositionX() - 1 == x && f.getPositionY() == y && !flagNoClickingAround) {
				pressField(f);
			}
			else if (f.getPositionX() == x && f.getPositionY() + 1 == y && !flagNoClickingAround) {
				pressField(f);
			}
			else if (f.getPositionX() == x && f.getPositionY() - 1 == y && !flagNoClickingAround) {
				pressField(f);
			}
			else if (f.getPositionX() + 1 == x && f.getPositionY() + 1 == y && !flagNoClickingAround) {
				pressField(f);
			}
			else if (f.getPositionX() + 1 == x && f.getPositionY() - 1 == y && !flagNoClickingAround) {
				pressField(f);
			}
			else if (f.getPositionX() - 1 == x && f.getPositionY() + 1 == y && !flagNoClickingAround) {
				pressField(f);
			}
			else if (f.getPositionX() - 1 == x && f.getPositionY() - 1 == y && !flagNoClickingAround) {
				pressField(f);
			}
		}
	}

	private void restart() {
		for (Field f : buttons) {
			f.removeMouseListener(this);
			f.setInGame(false);
		}
		bombsGenerated = false;
		createAndAddPanel();
	}

	private void loose() {
		ImageIcon scaledImg = null;
		for (Field f : buttons) {
			if(scaledImg==null) {
				scaledImg = new ImageIcon(f.getImg().getScaledInstance(
						(int) ((Math.min(frame.getHeight(), frame.getWidth()) / realBoard.length) / 1.25),
						(int) ((Math.min(frame.getHeight(), frame.getWidth()) / realBoard.length) / 1.25),
						Image.SCALE_FAST));
			}
			f.removeMouseListener(this);
			if (f.isBomb()) {
				f.setIcon(scaledImg);

			}
		}
		int again = JOptionPane.showConfirmDialog(frame, "Leider Verloren\nWollen Sie es nochmal versuchen?", "Verloren",
				JOptionPane.YES_NO_OPTION);
		if (again == 0) {
			restart();
		}else {
			System.exit(0);
			flagNoClickingAround=true;
		}
	}

	private void pressField(Field field) {
		if (field.isEnabled()) {
			field.setEnabled(false);
			if (field.isBomb()) {
				loose();
			} else {
				if(field.getIcon()!=null) {
					bombsLeft++;
				}
				field.setIcon(null);
				field.setText(bombsNextby(field.getPositionX() - 1, field.getPositionY() - 1) + "");
				if (field.getText().equals("0")) {
					field.setText("");
					clickAllAround(field.getPositionX(), field.getPositionY());
				}
			}

			if (checkGameWon()) {
				int again = JOptionPane.showConfirmDialog(frame,
						"Herzlichen Glückwunsch, Sie haben gewonnen!\nWollen Sie nocheinmal spielen?", "Gewonnen",
						JOptionPane.YES_NO_OPTION);
				for (Field f : buttons) {
					f.removeMouseListener(this);
				}
				if (again == 0) {
					restart();
				}else {
					flagNoClickingAround=true;
				}
			}
		}
	}

	private boolean checkGameWon() {
		if(flagNoClickingAround==false) {
		for (Field f : buttons) {
			if (!f.isBomb() && f.isEnabled() && f.isInGame()) {
				return false;
			}
		}
		return true;
		}else {
			return false;
		}
	}

	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getX() >= 0 && e.getY() >= 0) {

			for (Field b : buttons) {
				if (e.getSource().equals(b) && b.isEnabled()) {
					if (e.getX() <= b.getSize().getWidth() && e.getY() <= b.getSize().getHeight()) {
						if (SwingUtilities.isRightMouseButton(e)) {
							if (b.getIcon() != null) {
								bombsLeft++;
								b.setIcon(null);
							} else if (bombsLeft > 0) {
								try {
									Image img = ImageIO.read(getClass().getResource("pictures/Fahne.png"));
									b.setIcon(new ImageIcon(img.getScaledInstance(
											(int) ((Math.min(frame.getHeight(), frame.getWidth()) / realBoard.length)
													/ 1.75),
											(int) ((Math.min(frame.getHeight(), frame.getWidth()) / realBoard.length)
													/ 1.75),
											Image.SCALE_FAST)));
									bombsLeft--;
								} catch (IOException error) {
									System.err.println("Bild konnte nicht geladen werden!");
									error.printStackTrace();
								}
							}
							label.setText("Noch zu findende Bomben:   " + bombsLeft);
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
	}

	private class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() instanceof JMenuItem) {
				if (((JMenuItem) e.getSource()).getText().equals("Neustart")) {
					restart();
				} else if (((JMenuItem) e.getSource()).getText().equals("Spiel beenden")) {
					System.exit(0);
				} else if (((JMenuItem) e.getSource()).getText().equals("Einfach (8x8)")) {
					nextGameSize = 8;
					nextGameBombs = 10;
					restart();
				} else if (((JMenuItem) e.getSource()).getText().equals("Mittel (14x14)")) {
					nextGameSize = 14;
					nextGameBombs = 32;
					restart();
				} else if (((JMenuItem) e.getSource()).getText().equals("Schwer (20x20)")) {
					nextGameSize = 20;
					nextGameBombs = 80;
					restart();
				}else if (((JMenuItem) e.getSource()).getText().equals("Anleitung")) {

					FileReader fr;
					try {
						String anleitung ="";
						String zeile ="";
						File file = new File("Anleitung.txt");
						fr = new FileReader(file);
						System.out.println("_----------------------------------------------------");
						 BufferedReader br = new BufferedReader(fr);
						 while( (zeile = br.readLine()) != null ) {
								anleitung += zeile+"\n";
							}
						 br.close();
						 JOptionPane.showMessageDialog(frame, anleitung,"Anleitung",JOptionPane.INFORMATION_MESSAGE);
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(frame, "Anleitung konnte nicht geladen werden.", "Anleitung",JOptionPane.INFORMATION_MESSAGE);
					}
					
				}else if (((JMenuItem) e.getSource()).getText().equals("Credits")) {
					JOptionPane.showMessageDialog(frame, "Ein Spiel von Kilian Kraus mit Lizensfreien Grafiken von Pixabay.com","Credits",JOptionPane.INFORMATION_MESSAGE);
				}
			} else {
				System.err.println(
						"Wurde ein falscher Button diesem Listener hinzugefügt? Dieser Listener ist nur für das Menü gedacht.");
			}

		}

	}

}
