package com.mcmacker4.snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Snake implements KeyListener {
	
	public static final Vec2
			RIGHT = new Vec2(1, 0),
			LEFT = new Vec2(-1, 0),
			UP = new Vec2(0, -1),
			DOWN = new Vec2(0, 1);
	
	private final int INIT_LENGTH = 20;
	private final int GROWTH = 5;
	
	private Vec2 positions[];
	private Vec2 direction;
	private Vec2 seed;
	
	private boolean growing;
	private boolean changingDirection;
	
	private long lastUpdate;
	
	private Random rand;
	
	public Snake() {
		positions = new Vec2[INIT_LENGTH];
		for(int i = 0; i < positions.length; i++) positions[i] = new Vec2(40, 30);
		direction = UP;
		rand = new Random();
		createSeed();
	}
	
	public void update() {
		if(System.currentTimeMillis() - lastUpdate < 50) return;
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
		changingDirection = false;
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
		if(newPos.x > 80) newPos.x = 0;
		if(newPos.x < 0) newPos.x = 80;
		if(newPos.y > 60) newPos.y = 0;
		if(newPos.y < 0) newPos.y = 60;
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

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if(direction == DOWN || changingDirection) return;
			direction = UP;
			changingDirection = true;
			break;
		case KeyEvent.VK_DOWN:
			if(direction == UP || changingDirection) return;
			direction = DOWN;
			changingDirection = true;
			break;
		case KeyEvent.VK_RIGHT:
			if(direction == LEFT || changingDirection) return;
			direction = RIGHT;
			changingDirection = true;
			break;
		case KeyEvent.VK_LEFT:
			if(direction == RIGHT || changingDirection) return;
			direction = LEFT;
			changingDirection = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}
