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
	
	private final int INIT_LENGTH = 1;
	
	Vec2 positions[];
	Vec2 direction;
	Vec2 seed;
	
	private boolean growing;
	private boolean changingDirection;
	
	private long lastUpdate;
	
	private Random rand;
	
	public Snake() {
		positions = new Vec2[INIT_LENGTH];
		positions[0] = new Vec2(50, 50);
		direction = new Vec2(0, 1);
		rand = new Random();
		createSeed();
	}
	
	public void update() {
		if(System.currentTimeMillis() - lastUpdate < 50) return;
		lastUpdate = System.currentTimeMillis();
		Vec2 newPos = positions[0].plus(direction);
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
		if(positions[0].x == seed.x && positions[0].y == seed.y) {
			growing = true;
			createSeed();
		}
		changingDirection = false;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		for(int i = 0; i < positions.length; i++) {
			g.fillRect(positions[i].x * 10, positions[i].y * 10, 10, 10);
		}
		g.setColor(Color.BLUE);
		g.fillRect(seed.x * 10, seed.y * 10, 10, 10);
	}
	
	public void setDirection(Vec2 direction) {
		this.direction = direction;
	}
	
	private void grow() {
		Vec2 newPositions[] = new Vec2[positions.length + 1];
		for(int i = 0; i < positions.length; i++) {
			newPositions[i] = positions[i];
		}
		positions = newPositions;
	}
	
	private void createSeed() {
		seed = new Vec2(rand.nextInt(80), rand.nextInt(60));
		while(!validSeed()) {
			seed = new Vec2(rand.nextInt(80), rand.nextInt(60));
		}
	}
	
	private boolean validSeed() {
		for(int i = 0; i < positions.length; i++) {
			if(seed == positions[i]) return false;
		}
		return true;
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
