package artillery;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
	public KeyManager() {
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			Player.chargeUp = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			Player.chargeDn = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			Player.angleDn = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			Player.angleUp = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			Player.fire = true;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			Player.chargeUp = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			Player.chargeDn = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			Player.angleDn = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			Player.angleUp = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE){
			Player.fire = false;
		}
	}
	
	public void keyTyped(KeyEvent e) {
		
	}
}
