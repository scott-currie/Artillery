package artillery;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class Player {
	private Rectangle bound;
	private int xPos, yPos;
	private int angle;
	private int charge;
	public static boolean angleUp, angleDn, chargeUp, chargeDn, fire;
	private Projectile proj;
	private final int BARREL_LENGTH = 20;
	Line2D.Double barrel;
	GamePanel gp;
	
	public Player(GamePanel gp) {
		this.gp = gp;
		init();
	}
	
	public void init() {
		xPos = 60;
		yPos = gp.getLeftElev();
//		proj = null;
		barrel = new Line2D.Double((double)xPos, (double)yPos, (double)xPos + Math.cos(Math.toRadians(angle)) * BARREL_LENGTH, (double)yPos + Math.sin(Math.toRadians(angle)) * BARREL_LENGTH);
	}
	
	public void update() {
		if (chargeUp) {
			charge++;
			chargeUp = false;
		}
		if (chargeDn && charge > 0) {
			charge--;
			chargeDn = false;
		}
		if (angleUp && angle < 90) {
			angle++;
			angleUp = false;
		}
		if (angleDn && angle > 0) {
			angle--;
			angleDn = false;
		}
		if (fire && proj == null) {
			proj = new Projectile(xPos, yPos, angle, charge, gp);
			fire = false;
		}
		
		barrel.x2 = (double)xPos + Math.cos(Math.toRadians(angle)) * BARREL_LENGTH;
		barrel.y2 = (double)yPos + Math.sin(Math.toRadians(angle)) * BARREL_LENGTH;
		
		if (proj != null) {
			proj.update();
			if (proj.getXPos() >= GamePanel.WIDTH - 1 || proj.checkHitGround()) {
				System.out.println("Projectile OOB.");
				proj = null;
			}
			
			else if (proj.checkHitTarget()) {
				System.out.println("Boom!");
				gp.refreshState();
			}
		}
	}
	
	public void render(Graphics g) {
		if (proj != null) {
			proj.render(g);
		}
		g.setColor(Color.RED);
		if (this != null) {
			g.drawLine(xPos, GamePanel.HEIGHT - yPos, (int)barrel.x2, GamePanel.HEIGHT - (int)barrel.y2);
		}
	}
	
	public int getAngle() {
		return angle;
	}
	
	public int getCharge() {
		return charge;
	}
}
