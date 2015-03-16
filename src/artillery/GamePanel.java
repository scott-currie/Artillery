package artillery;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
	public static final int WIDTH = Game.WIDTH;
	public static final int HEIGHT = Game.HEIGHT;
	private static final int NO_DELAYS_PER_YIELD = 16;
	private static final int MAX_FRAME_SKIPS = 5;
	private static final int TARGET_FPS = 60;
	public boolean running = false;
	private Graphics2D bg; //Graphics buffering object
	private Image bImage;  //Buffering image for bg
	private Thread gameThread;
//	public Line2D.Double left, right, middle;
	private int slopeStart, slopeEnd;
	private static Line2D.Double[] horizon;
	
	private Player player;
	public static final int LEFT_ELEV = 60;
	private int leftElev;
	public static int rightElev;
//	public static final int GROUND_LEVEL = HEIGHT - 60;
	private static Target target;
	private static Polygon terrain;
	private double windSpeed;
	
	public GamePanel() {
		init();
	}
	
	public void init() {
		slopeStart = (int)((WIDTH / 2) - (WIDTH / 8));
		slopeEnd = slopeStart + (WIDTH / 8);
		Random r = new Random();
		leftElev = (int)(r.nextDouble() * HEIGHT / 2);
		rightElev = (int)(r.nextDouble() * (HEIGHT - (HEIGHT / 4)));
		player = new Player(this);
		target = new Target();
		windSpeed = r.nextDouble();
//		if (r.nextDouble() < .5) {
//			windSpeed = -windSpeed;
//		}
//		windSpeed /= 100;
		windSpeed = 0;
		generateTerrain();
		this.addKeyListener(new KeyManager());
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void run( )
		/* Repeatedly update, render, sleep so loop takes close
		to period nsecs. Sleep inaccuracies are handled.
		The timing calculation use the Java 3D timer.
		Overruns in update/renders will cause extra updates
		to be carried out so UPS ~== requested FPS
		*/
		{
		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long excess = 0L;
		long period = (1 / TARGET_FPS) * 1000000000; 
		beforeTime = System.nanoTime();
		running = true;
		while(running) {
			gameUpdate();
			gameRender();
			paintScreen();
			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			sleepTime = (period - timeDiff) - overSleepTime;
			if (sleepTime > 0) { // some time left in this cycle
				try {
					Thread.sleep(sleepTime/1000000L); // nano -> ms
				}
				catch(InterruptedException ex){}
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			}
			else { // sleepTime <= 0; frame took longer than the period
				excess -= sleepTime; // store excess time value
				overSleepTime = 0L;
				if (++noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield( ); // give another thread a chance to run
					noDelays = 0;
				}
			}
			beforeTime = System.nanoTime();
			/* If frame animation is taking too long, update the game state
			without rendering it, to get the updates/sec nearer to
			the required FPS. */
			int skips = 0;
			while((excess > period) && (skips < MAX_FRAME_SKIPS)) {
				excess -= period;
				gameUpdate( ); // update state but don't render
				skips++;
			}
		}
		System.exit(0);
	} // end of run( )
	
	public void gameUpdate() {
		player.update();
	}
	
	public void gameRender() {
		if (bImage == null) {
			bImage = createImage(WIDTH, HEIGHT);
		}
		else {
			bg = (Graphics2D)bImage.getGraphics();
			bg.setColor(Color.BLACK);
			bg.fillRect(0, 0, WIDTH, HEIGHT);
			bg.setColor(Color.WHITE);
			bg.drawString("Angle: " + player.getAngle(), 0, 16);
			bg.drawString("Charge: " + player.getCharge(), 60, 16);
			bg.drawString("Wind: " + (windSpeed * 10), 140, 16);
			bg.setColor(Color.GREEN);
			bg.fillPolygon(getInvertedTerrain());
			
			
			
			player.render(bg);
			target.render(bg);
			
		}
		
	}
	
	public void paintScreen() {
		Graphics2D g;
		try {
			g = (Graphics2D)this.getGraphics();
			if (g != null && bImage != null) {
				g.drawImage(bImage,  0, 0, null);
				Toolkit.getDefaultToolkit().sync();
				g.dispose();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static int getRightElev() {
		return rightElev;
	}
	
	public static Line2D.Double[] getHorizon() {
		return horizon;
	}
	
	public void generateTerrain() {
		terrain = new Polygon();
		
		//add points, starting with left horizon
		terrain.addPoint(0, leftElev);
		//beginning of middle slope
		terrain.addPoint(slopeStart, leftElev);
		//end of slope
		terrain.addPoint(slopeEnd, rightElev);
		//right edge
		terrain.addPoint(GamePanel.WIDTH - 1, rightElev);
		//bottom right corner
		terrain.addPoint(GamePanel.WIDTH - 1, 0);
		//bootom left corner
		terrain.addPoint(0, 0);
	}
	
	public Polygon getInvertedTerrain() {
		Polygon invTerrain = new Polygon();
		for (int i = 0; i < terrain.npoints ; i++) {
			invTerrain.addPoint(terrain.xpoints[i], HEIGHT - terrain.ypoints[i]);
		}
		return invTerrain;
	}
	
	public static Polygon getTerrain() {
		return terrain;
	}
	
	public static Target getTarget() {
		return target;
	}
	
	public void refreshState() {
		slopeStart = (int)((WIDTH / 2) - (WIDTH / 8));
		slopeEnd = slopeStart + (WIDTH / 8);
		Random r = new Random();
		leftElev = (int)(r.nextDouble() * (HEIGHT / 2));
		rightElev = (int)(r.nextDouble() * HEIGHT);
		player = new Player(this);
		target = new Target();
		generateTerrain();
//		windSpeed = r.nextDouble();
//		if (r.nextDouble() < .5) {
//			windSpeed = -windSpeed;
//		}
		windSpeed = 0;
	}
	
	public int getLeftElev() {
		return leftElev;
	}
	
	public double getWindSpeed() {
		return windSpeed;
	}
}
