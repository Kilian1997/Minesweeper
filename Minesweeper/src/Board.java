import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Board implements ActionListener {
	
	private boolean [][] realBoard;
	LinkedList<Field> buttons;
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
			buttons = new LinkedList<Field>();

			int x = 0;
			int y = 0;
			for(int i=0;i<size*size;i++) { // zwei forschleifen! Überdenken
				buttons.add(new Field(x+1,y+1,realBoard[x][y])); // Muss noch angepasst werden an die Klasse Field 
				//buttons.add(new JButton("Normal"));
				panel.add(buttons.get(i));
				buttons.get(i).addActionListener(this);
				buttons.get(i).setFont(new Font("Monospaced", Font.ITALIC , 12));
				if(x<size-1) {
					x++;
				}else {
					x=0;
					y++;
				}
			}
			frame.add(panel,BorderLayout.CENTER);
			frame.setVisible(true);
			System.out.println(realBoard[x].length);
			
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
	
	private int bombsNextby (int x, int y) {
		int result =0 ;
		System.out.println(y+1);
		System.out.println(realBoard[x].length);
		if(x+1<=realBoard.length-1  &&realBoard[x+1][y]) {
			result++;
		}
		if(x+1<=realBoard.length-1&&y+1<=realBoard[x+1].length-1&&realBoard[x+1][y+1]) {
			result++;
		}
		if(x+1<=realBoard.length-1&&y-1>=0&&realBoard[x+1][y-1]) {
			result++;
		}
		if(x-1>=0&&y-1>=0&&realBoard[x-1][y-1]) {
			result++;
		}
		if(x-1>=0&&y+1<=realBoard[x-1].length-1&&realBoard[x-1][y+1]) {
			result++;
		}
		if(x-1>=0&&realBoard[x-1][y]) {
			result++;
		}
		if(y+1<=realBoard[x].length-1&&realBoard[x][y+1]) {
			result++;
		}
		if(y-1>=0&&realBoard[x][y-1]) {
			result++;
		}
		return result;
	}
	
	private void clickAllAround (int x,int y) {
		for(Field f: buttons) {
			if(f.getPositionX()+1==x && f.getPositionY()==y) {
				f.doClick();
			}
			if(f.getPositionX()-1==x && f.getPositionY()==y) {
				f.doClick();
			}
			if(f.getPositionX()==x && f.getPositionY()+1==y) {
				f.doClick();
			}
			if(f.getPositionX()==x && f.getPositionY()-1==y) {
				f.doClick();
			}
			if(f.getPositionX()+1==x && f.getPositionY()+1==y) {
				f.doClick();
			}
			if (f.getPositionX()+1==x && f.getPositionY()-1==y) {
				f.doClick();
			}
			if(f.getPositionX()-1==x && f.getPositionY()+1==y) {
				f.doClick();
			}
			if(f.getPositionX()-1==x && f.getPositionY()-1==y) {
				f.doClick();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		for(Field b : buttons) {
			if(e.getSource().equals(b)) {
				b.setEnabled(false);
				System.out.println("x: "+b.getPositionX()+", y: "+b.getPositionY());
				if(realBoard[b.getPositionX()-1][b.getPositionY()-1]) {
					
					b.setText("Bombe");
				}else {
					b.setText(""+bombsNextby(b.getPositionX()-1, b.getPositionY()-1));
					if(bombsNextby(b.getPositionX()-1, b.getPositionY()-1)==0) {
						clickAllAround(b.getPositionX(), b.getPositionY());
					}
				}
				
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
