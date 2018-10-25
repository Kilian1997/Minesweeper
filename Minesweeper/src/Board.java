import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Board implements ActionListener {
	
	private boolean [][] realBoard;
	LinkedList<JButton> buttons;
	JFrame frame;
	JPanel panel;
	
	public Board (int size, int bombs) {
		if(bombs> size*size) {
			throw new IllegalArgumentException("Mehr Bomben als Felder.");
		}else {
			realBoard = new boolean [size][size];
			placeBombs(bombs);
			
			frame = new JFrame("Kilians Minesweeper");
			frame.setSize(600, 600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(true);
			frame.setLocationRelativeTo(null);
			panel= new JPanel();
			panel.setBackground(Color.white);
			panel.setLayout(new GridLayout(size,size));
			buttons = new LinkedList<JButton>();

			for(int i=0;i<size*size;i++) { // zwei forschleifen! Überdenken
				System.out.println("Bombssss");
				//buttons.add(new Field(i,j,realBoard[i][j],"hi")); // Muss noch angepasst werden an die Klasse Field 
				buttons.add(new JButton("ups"));
				panel.add(buttons.get(i));
				buttons.get(i).addActionListener(this);
			}
			
			frame.add(panel,BorderLayout.CENTER);
			frame.setVisible(true);
			
			
			//Ausgabe des Boards zu Testzweckens
//			for(int i=0;i<realBoard.length;i++) {
//				for(int a=0;a<realBoard.length;a++) {
//					System.out.print(realBoard[i][a]+", ");
//				}
//				System.out.println();
//			}
		}
	}
	
	public void placeBombs (int numberOfBombs) {
		while(numberOfBombs>0) {
			int p1 = (int)(Math.random()*realBoard.length);
			int p2 = (int)(Math.random()*realBoard.length);
			if(realBoard[p1][p2]==false) {
				realBoard[p1][p2] = true;
				numberOfBombs--;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		for(JButton b : buttons) {
			if(e.getSource().equals(b)) {
				b.setText("ich wurde gedrückt");
			}
		}
//		System.out.println(e.getActionCommand());
//		System.out.println(e.getSource());
//		System.out.println(e.toString());
//		buttons.get(0).setEnabled(false);
//		panel.remove(0);
//		frame.repaint();
//		frame.remove(panel);
//		frame.add(panel);
//		frame.repaint();
	}

}
