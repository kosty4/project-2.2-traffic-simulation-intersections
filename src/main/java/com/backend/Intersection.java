package com.backend;

public class Intersection {


	public SubIntersection getLeft() {
		return left;
	}

	public void setLeft(SubIntersection left) {
		this.left = left;
	}

	public SubIntersection getRight() {
		return right;
	}

	public void setRight(SubIntersection right) {
		this.right = right;
	}

	public SubIntersection getUp() {
		return up;
	}

	public void setUp(SubIntersection up) {
		this.up = up;
	}

	public SubIntersection getDown() {
		return down;
	}

	public void setDown(SubIntersection down) {
		this.down = down;
	}

	 private SubIntersection left;
	 private SubIntersection right;
	 private SubIntersection up;
	 private SubIntersection down;
	 
	 
}
