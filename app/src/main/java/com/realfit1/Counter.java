package com.realfit1;


/**
 * A Counter to keep a count value, which can be
 * incremented, decremented, and reset to zero.
 * 
 *  @author P15224747
 */

public  class Counter {

	//fields
	private int count;
	
	//constructors	
	public Counter() {
		count = 0;
	}

	public Counter(int count) {
		this.count = count;
	}
	
	//methods	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public void increment() {
		count += 100;
	}
	
	public void decrement() {
		count -= 100;
	}
	
	public void reset() {
		count = 0;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName()  + "[count=" + this.count + "]";
	}
	
}
