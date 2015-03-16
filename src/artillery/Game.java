package artillery;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Game {

	public static final int WIDTH = 800, HEIGHT = 600;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GamePanel panel = new GamePanel();
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.add(panel);
		frame.setSize(frame.getContentPane().getPreferredSize());
		frame.pack();
		panel.setFocusable(true);
		panel.requestFocus();
		frame.setVisible(true);

	}

}
