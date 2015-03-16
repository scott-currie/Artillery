package artillery;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class Projectile {
	private List<Point> arc;
	private double x, y;
	private double xPos, yPos;
	private static final double GRAVITY = .0001;
	private int angle, charge;
	private Rectangle box;
	private final int WIDTH = 1, HEIGHT = 1;
	private GamePanel gp;
	
	public Projectile(int xPos, int yPos, int angle, int charge, GamePanel gp) {
		this.gp = gp;
		this.angle = angle;
		this.charge = charge;
		this.xPos = xPos;
		this.yPos = yPos;
		this.x = (charge * Math.cos(Math.toRadians(angle))) / 60;
		this.y = -(charge * Math.sin(Math.toRadians(angle))) / 60;
		xPos = 0;
		yPos = GamePanel.LEFT_ELEV;
		arc = new ArrayList<>();
		box = new Rectangle(xPos, yPos, WIDTH, HEIGHT);
	}
	
	public void update() {
		xPos += x;
		xPos += gp.getWindSpeed();
		y += GRAVITY;
		yPos -= y;
		if (arc.size() < 1) {
			arc.add(new Point((int)xPos, (int)yPos));
		}
		else {
			int prevXPos = arc.get(arc.size() - 1).x;
			int prevYPos = arc.get(arc.size() - 1).y;
//			System.out.println(xPos + "," + yPos);
			if ((int)xPos != prevXPos || (int)yPos != prevYPos) {
				arc.add(new Point((int)xPos, (int)yPos));
			}
		}
		try {
			Thread.sleep(1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		box.setLocation((int)xPos, (int)yPos);
	}
	
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		for (Point point : arc) {
			g.fillRect(point.x, GamePanel.HEIGHT - point.y, WIDTH, HEIGHT);
		}
	}
	
	public int getYPos() {
		return (int)yPos;
	}
	
	public int getXPos() {
		return (int)xPos;
	}
	
	public boolean checkHitGround() {
		if (GamePanel.getTerrain().intersects(box)) {
			return true;
		}
		return false;
	}
	
	public boolean checkHitTarget() {
//		if (box.intersects(GamePanel.getTarget().getBoundary())) {
		if (GamePanel.getTarget().getBoundary().intersects(box)) {
			System.out.println("Boom!");
			return true;
		}
		return false;
	}
}
