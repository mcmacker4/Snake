package com.mcmacker4.snake;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;

public class Engine implements Runnable {
	
	JFrame frame;
	Canvas canvas;
	BufferStrategy buffer;
	Random rand;
	
	Snake snake;
	
	private long last;
	
	public Engine() {
		snake = new Snake();
		frame = new JFrame("Snake");
		frame.setIgnoreRepaint(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas = new Canvas();
		canvas.setIgnoreRepaint(true);
		canvas.setSize(800, 600);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
		canvas.createBufferStrategy(2);
		canvas.addKeyListener(snake);
		buffer = canvas.getBufferStrategy();
		last = System.currentTimeMillis();
		rand = new Random();
		canvas.requestFocus();
	}
	
	@Override
	public void run() {
		Graphics graphics = null;
		while(true) {
			try {
				long now = System.currentTimeMillis();
				if(now - last >= 1000/60) {
					last = now;
					graphics = buffer.getDrawGraphics();
					graphics.setColor(Color.BLACK);
					graphics.fillRect(0, 0, 800, 600);
					
					snake.update();
					snake.draw(graphics);
					
					frame.setTitle("Snake - Length: " + snake.getLength());
					
					if(!buffer.contentsLost())
						buffer.show();
				}
			} finally {
				if(graphics != null)
					graphics.dispose();
			}
		}
	}
	
	public static void main(String[] args) {
		Thread thread = new Thread(new Engine());
		thread.start();
	}

}
