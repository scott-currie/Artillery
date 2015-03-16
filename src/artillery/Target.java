package artillery;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Target {
	private int xPos, yPos;
	private Rectangle boundary;
	private final int WIDTH = 30, HEIGHT = 30;
	
	public Target() {
		init();
	}
	
	public void init() {
		xPos = GamePanel.WIDTH - 100;
		yPos = GamePanel.getRightElev() + HEIGHT;
		boundary = new Rectangle(xPos, yPos, WIDTH, HEIGHT);
	}
	
	public void update() {}
	
	public void render(Graphics g) {
		g.setColor(Color.RED);
		if (this != null) {
			g.fillRect(xPos, GamePanel.HEIGHT - yPos, WIDTH, HEIGHT);
		}
	}
	
	public Rectangle getBoundary() {
		return boundary;
	}
	
}
