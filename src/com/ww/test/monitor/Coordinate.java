package com.ww.test.monitor;

/**
 * <pre>
 * 坐标点
 * y = 时间点上的值 
 * x = X轴 long 类型的时间
 * </pre>
 */
public class Coordinate {

	private long xValue;
	private long yValue;

	public Coordinate(long x, long y) {
		this.xValue = x;
		this.yValue = y;
	}

	public Coordinate() {
	}

	public long getxValue() {
		return xValue;
	}

	public void setxValue(long xValue) {
		this.xValue = xValue;
	}

	public long getyValue() {
		return yValue;
	}

	public void setyValue(long yValue) {
		this.yValue = yValue;
	}

}
