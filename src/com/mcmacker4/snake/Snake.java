package com.mcmacker4.snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class Snake implements KeyListener {
	
	public static final Vec2
			RIGHT = new Vec2(1, 0),
			LEFT = new Vec2(-1, 0),
			UP = new Vec2(0, -1),
			DOWN = new Vec2(0, 1);
	
	private final int INIT_LENGTH = 1;
	private final int GROWTH = 5;
	
	private Vec2 positions[];
	private Vec2 direction;
	private Vec2 seed;
	
	private boolean growing;
	
	private ArrayList<KeyEvent> keyEvents;
	
	private long lastUpdate;
	
	private Random rand;
	
	public Snake() {
		positions = new Vec2[INIT_LENGTH];
		for(int i = 0; i < positions.length; i++) positions[i] = new Vec2(40, 30);
		direction = UP;
		rand = new Random();
		keyEvents = new ArrayList<>();
		createSeed();
	}
	
	public void update() {
		if(System.currentTimeMillis() - lastUpdate < 50) return;
		execOneKeyEvent();
		lastUpdate = System.currentTimeMillis();
		Vec2 newPos = createNewPos();
		if(occupied(newPos)) {
			try {
				Thread.sleep(2000);
				newPos = new Vec2(40, 30);
			} catch (InterruptedException e) {}
			reset();
		}
		if(growing) {
			grow();
			growing = false;
		}
		for(int i = positions.length - 1; i >= 0; i--) {
			try{
				positions[i] = positions[i - 1];
			} catch(Exception e) {}
		}
		positions[0] = newPos;
		if(positions[0].equals(seed)) {
			growing = true;
			createSeed();
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		for(int i = 0; i < positions.length; i++) {
			if(positions[i] == null) continue;
			g.fillRect(positions[i].x * 10, positions[i].y * 10, 10, 10);
		}
		g.setColor(Color.GREEN);
		g.fillRect(seed.x * 10, seed.y * 10, 10, 10);
	}
	
	public void setDirection(Vec2 direction) {
		this.direction = direction;
	}
	
	private void grow() {
		Vec2 newPositions[] = new Vec2[positions.length + GROWTH];
		for(int i = 0; i < positions.length; i++) {
			newPositions[i] = positions[i];
		}
		positions = newPositions;
	}
	
	private Vec2 createNewPos() {
		Vec2 newPos = positions[0].plus(direction);
		if(newPos.x > 79) newPos.x = 0;
		if(newPos.x < 0) newPos.x = 79;
		if(newPos.y > 59) newPos.y = 0;
		if(newPos.y < 0) newPos.y = 59;
		return newPos;
	}
	
	private void createSeed() {
		seed = new Vec2(rand.nextInt(80), rand.nextInt(60));
		while(occupied(seed)) {
			seed = new Vec2(rand.nextInt(80), rand.nextInt(60));
		}
	}
	
	private boolean occupied(Vec2 pos) {
		for(int i = 0; i < positions.length; i++) {
			if(positions[i] == null) continue;
			if(positions[i].equals(pos)) return true;
		}
		return false;
	}
	
	private void reset() {
		positions = new Vec2[INIT_LENGTH];
		for(int i = 0; i < positions.length; i++) positions[i] = new Vec2(40, 30);
		System.err.println("RESET!");
	}
	
	private void execOneKeyEvent() {
		if(keyEvents.isEmpty()) return;
		KeyEvent e = keyEvents.get(0);
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if(direction == DOWN) break;
			direction = UP;
			break;
		case KeyEvent.VK_DOWN:
			if(direction == UP) break;
			direction = DOWN;
			break;
		case KeyEvent.VK_RIGHT:
			if(direction == LEFT) break;
			direction = RIGHT;
			break;
		case KeyEvent.VK_LEFT:
			if(direction == RIGHT) break;
			direction = LEFT;
			break;
		}
		keyEvents.remove(0);
	}
	
	public int getLength() {
		return positions.length;
	}
	
	public int getRealLength() {
		int length = 0;
		try {
			while(positions[length++] != null) {}
		} catch(Exception e) {}
		return length - 1;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int c = e.getKeyCode();
		if(c == KeyEvent.VK_UP || c == KeyEvent.VK_DOWN || c == KeyEvent.VK_RIGHT || c == KeyEvent.VK_LEFT)
			if(keyEvents.size() < 2) keyEvents.add(e);
		else if(c == KeyEvent.VK_R)
			reset();
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}
