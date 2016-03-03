package com.mcmacker4.snake;

public class Vec2 {
	
	int x, y;
	
	public Vec2() {
		x = 0;
		y = 0;
	}
	
	public Vec2(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Vec2 add(Vec2 vec) {
		this.x += vec.x;
		this.y += vec.y;
		return this;
	}
	
	public Vec2 plus(Vec2 vec) {
		return new Vec2(this.x + vec.x, this.y + vec.y);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Vec2)) return false;
		Vec2 pos = (Vec2) obj;
		if(pos.x == this.x && pos.y == this.y) return true;
		return false;
	}

}
