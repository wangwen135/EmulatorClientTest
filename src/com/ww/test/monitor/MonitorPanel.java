package com.ww.test.monitor;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

public class MonitorPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * 日期格式1
	 */
	private SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm");

	/**
	 * 显示第二个时间
	 */
	private boolean showDate2 = true;

	/**
	 * 日期格式2
	 */
	private SimpleDateFormat dateFormat2 = new SimpleDateFormat("ss:SSS");

	/**
	 * Y轴的文本宽度
	 */
	private int YTextWidth = 40;
	/**
	 * X轴的文本高度
	 */
	private int XTextHeight = 40;

	/**
	 * 标题的高度
	 */
	private int titleHeight = 20;

	/**
	 * 标题
	 */
	private String title = "测试标题";

	/**
	 * 显示当前值的文本宽度
	 */
	private int currentTextWidth = 40;

	/**
	 * 背景色
	 */
	private Color backgroundColor = Color.BLACK;
	/**
	 * 线条颜色
	 */
	private Color lineColor = new Color(0,100,0);
	/**
	 * 曲线颜色
	 */
	private Color graphColor = Color.YELLOW;
	/**
	 * 字体颜色
	 */
	private Color fontColor = Color.BLACK;

	/**
	 * 图片
	 */
	private BufferedImage bimage;
	private Graphics2D big;

	/**
	 * 图片宽度
	 */
	private int imgWidth;
	/**
	 * 图片高度
	 */
	private int imgHeight;

	/**
	 * 曲线图的高度
	 */
	private int graphHeight;
	/**
	 * 曲线图的宽度
	 */
	private int graphWidth;

	/**
	 * 数据列表
	 */
	private List<Coordinate> clist;

	/**
	 * Y轴上的最大值
	 */
	private long maxYValue;

	/**
	 * Y轴上的最小值
	 */
	private long minYValue;

	/**
	 * X轴上的最小值
	 */
	private long minXValue;

	/**
	 * X轴上的最大值
	 */
	private long maxXValue;

	/**
	 * 最大Y坐标
	 */
	private long maxY;
	/**
	 * 最小Y坐标
	 */
	private long minY;

	/**
	 * Y轴间距--数值
	 */
	private long yAxisInterval;

	/**
	 * Y轴线条数量
	 */
	private int yAxisLineCount;

	/**
	 * Y轴上一个与像素点的比例
	 */
	private double yRatio;

	/**
	 * X轴上的一个与像素点的比例
	 */
	private double xRatio;

	/**
	 * X轴上默认的线与线之间的像素
	 */
	private int xAxisLineIntervalPixel = 80;

	/**
	 * <pre>
	 * 默认的用于计算用的Y轴的线条的数量
	 * 
	 * </pre>
	 */
	private int defaultYAxisLineCount = 10;

	public MonitorPanel() {

	}
	
	/**
	 * 构造函数
	 * @param title
	 */
	public MonitorPanel(String title){
		this.title = title;
	}

	/**
	 * Create the panel.
	 */
	public MonitorPanel(List<Coordinate> list) {
		this.clist = list;
	}

	/**
	 * <pre>
	 * 计算Y最大最小值
	 * </pre>
	 */
	private void calcMaxAndMinYValue() {
		// 先设置一次
		maxYValue = Long.MIN_VALUE;
		minYValue = Long.MAX_VALUE;
		for (Coordinate coordinate : clist) {
			if (coordinate.getyValue() > maxYValue) {
				maxYValue = coordinate.getyValue();
			}
			if (coordinate.getyValue() < minYValue) {
				minYValue = coordinate.getyValue();
			}
		}
	}

	/**
	 * <pre>
	 * 计算最大 、最小 Y轴值
	 * </pre>
	 */
	private void calcYAxis() {
		calcMaxAndMinYValue();

		long dValue = maxYValue - minYValue;
		if (dValue == 0) {
			maxY = maxYValue;
			minY = minYValue;
			return;
		}

		long interval = dValue / defaultYAxisLineCount;// 显示10条线
		if (interval == 0)
			interval = 1;
		long log = (long) Math.log10(interval);// 计算位数
		long _tmp = (long) Math.pow(10, log);
		yAxisInterval = Math.round((double) interval / _tmp) * _tmp;

		long maxc = maxYValue / yAxisInterval;
		maxY = (maxc + 1) * yAxisInterval;
		long minc = minYValue / yAxisInterval;
		minY = minc * yAxisInterval;
		if (minY > minYValue) {
			minY = minY - yAxisInterval;
		}

		yAxisLineCount = (int) ((maxY - minY) / yAxisInterval);

	}

	/**
	 * <pre>
	 * 计算Y轴比例 
	 * 既：值与显示区域的像素的比例
	 * </pre>
	 */
	private void calcYRatio() {
		calcYAxis();
		// 计算曲线图的高度
		graphHeight = imgHeight - titleHeight - XTextHeight;

		if (graphHeight < 0) {
			yRatio = 0;
			return;
		}
		long yd = maxY - minY;// Y轴
		if (yd < 0) {
			yRatio = 0;
			return;
		}

		yRatio = (double) graphHeight / (double) yd;// 得到比例

	}

	/**
	 * <pre>
	 * 计算X轴的比例
	 * 根据list中的元素个数
	 * </pre>
	 */
	private void calcXRatio() {

		// 计算曲线图的宽度
		graphWidth = imgWidth - YTextWidth - currentTextWidth;
		if (graphWidth < 0) {
			xRatio = 0;
			return;
		}
		minXValue = clist.get(0).getxValue();
		// 这里使用list，如果list会改变需要注意
		maxXValue = clist.get(clist.size() - 1).getxValue();
		long xv = maxXValue - minXValue;

		xRatio = (double) graphWidth / (double) xv;// 得到比例
	}

	/**
	 * <pre>
	 * 获取值对应X点坐标
	 * </pre>
	 * 
	 * @param value
	 * @return
	 */
	private int getXPoint(long value) {
		long v = value - minXValue;
		int x = (int) (v * xRatio);
		return x + YTextWidth;
	}

	/**
	 * <pre>
	 * 获取值对应Y点坐标
	 * </pre>
	 * 
	 * @param value
	 * @return
	 */
	private int getYPoint(long value) {
		long v = value - minY;
		int y = (int) (v * yRatio);
		return graphHeight - y + titleHeight;
	}

	/**
	 * <pre>
	 * 将内容画在图片上
	 * </pre>
	 */
	public void drawImage() {
		if (clist == null || clist.isEmpty()) {
			return;
		}
		int tw = getWidth();
		int th = getHeight();
		boolean rePaint = false;
		if (tw != imgWidth || th != imgHeight) {
			rePaint = true;
			// 大小有变化，重建一张图片
			imgWidth = tw;
			imgHeight = th;
			bimage = new BufferedImage(imgWidth, imgHeight,
					BufferedImage.TYPE_INT_RGB);
			big = bimage.createGraphics();
			// 设置“抗锯齿”的属性
			big.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_DEFAULT);
		}
		if (bimage == null) {
			return;
		}
		// 内容有变化
		rePaint = true;

		if (!rePaint) {
			return;
		}

		// 先计算title的高度

		// 技术X 和 Y 的比例
		calcYRatio();
		calcXRatio();

		// 清空图片
		big.clearRect(0, 0, imgWidth, imgHeight);
		big.setBackground(getBackground());

		// 获取字体

		// int titleWidth = fm.stringWidth(title);
		// 画标题
		drawTitle();

		// 画内容区域 背景
		drawGraphBackgound();

		// 画Y坐标
		drawYAxis();

		// Font font = big.getFont();
		// FontMetrics fm = big.getFontMetrics(font);
		// int ascent = fm.getAscent();
		// int descent = fm.getDescent();
		// int fHeight = fm.getHeight();

		// 画X坐标
		// drawXAxis();
		drawXAxis2();

		// 画曲线
		big.setColor(graphColor);

		int size = clist.size();
		if (size < 2) {
			return;
		}
		int[] xPoints = new int[size];
		int[] yPoints = new int[size];

		for (int i = 0; i < size; i++) {
			xPoints[i] = getXPoint(clist.get(i).getxValue());
			yPoints[i] = getYPoint(clist.get(i).getyValue());
		}

		big.drawPolyline(xPoints, yPoints, size);

		// 画一个当前值
		Font font = big.getFont();
		FontMetrics fm = big.getFontMetrics(font);
		int descent = fm.getDescent();
		big.setColor(fontColor);
		int _yPoint = yPoints[size - 1];
		int _xPoint = YTextWidth + graphWidth + 1;
		long currentValue = clist.get(clist.size() - 1).getyValue();
		String valueOf = String.valueOf(currentValue);
		big.drawString(valueOf, _xPoint, _yPoint - descent);
		big.drawLine(_xPoint, _yPoint, _xPoint + fm.stringWidth(valueOf),
				_yPoint);

		//SwingUtilities.i
		// 面板 重绘
		repaint();
	}

	/**
	 * <pre>
	 * 第二种方式画
	 * </pre>
	 */
	private void drawXAxis2() {

		Font font = big.getFont();
		FontMetrics fm = big.getFontMetrics(font);
		int ascent = fm.getAscent();
		// int descent = fm.getDescent();
		int fHeight = fm.getHeight();

		// 画X坐标
		// 在X轴能显示几条线
		int xLineCount = graphWidth / xAxisLineIntervalPixel;
		if (clist.size() <= xLineCount) {
			for (int i = 0; i < clist.size() - 1; i++) {
				long _xValue = clist.get(i).getxValue();
				int _xPoint = getXPoint(_xValue);
				Date date = new Date(_xValue);
				String dateStr = dateFormat1.format(date);

				big.setColor(lineColor);
				big.drawLine(_xPoint, titleHeight, _xPoint, titleHeight
						+ graphHeight);
				big.setColor(fontColor);
				big.drawString(dateStr, _xPoint, titleHeight + graphHeight
						+ ascent);

				if (showDate2) {
					String dateStr2 = dateFormat2.format(date);
					big.drawString(dateStr2, _xPoint, titleHeight + graphHeight
							+ ascent + fHeight);
				}
			}
		} else {
			double per = (double) graphWidth / xAxisLineIntervalPixel;

			// //long xMinV = clist.get(0).getxValue();
			// long xMaxV = clist.get(clist.size() - 1).getxValue();

			long xInterval = (long) ((maxXValue - minXValue) / per);

			for (int i = 0; i <= xLineCount; i++) {
				long _xValue = minXValue + (xInterval * i);
				int _xPoint = YTextWidth + (xAxisLineIntervalPixel * i);
				Date date = new Date(_xValue);
				String dateStr = dateFormat1.format(date);

				big.setColor(lineColor);
				big.drawLine(_xPoint, titleHeight, _xPoint, titleHeight
						+ graphHeight);
				big.setColor(fontColor);
				big.drawString(dateStr, _xPoint, titleHeight + graphHeight
						+ ascent);

				if (showDate2) {
					String dateStr2 = dateFormat2.format(date);
					big.drawString(dateStr2, _xPoint, titleHeight + graphHeight
							+ ascent + fHeight);
				}
			}
		}

	}

	/**
	 * <pre>
	 * 画X坐标轴
	 * </pre>
	 */
	@SuppressWarnings("unused")
	private void drawXAxis() {
		Font font = big.getFont();
		FontMetrics fm = big.getFontMetrics(font);
		int ascent = fm.getAscent();
		// int descent = fm.getDescent();
		int fHeight = fm.getHeight();

		// 画X坐标
		// 每隔间隔像素
		int xLineCount = graphWidth / xAxisLineIntervalPixel;
		if (clist.size() <= xLineCount) {
			for (int i = 0; i < clist.size() - 1; i++) {
				long _xValue = clist.get(i).getxValue();
				int _xPoint = getXPoint(_xValue);
				Date date = new Date(_xValue);
				String dateStr = dateFormat1.format(date);

				big.setColor(lineColor);
				big.drawLine(_xPoint, titleHeight, _xPoint, titleHeight
						+ graphHeight);
				big.setColor(fontColor);
				big.drawString(dateStr, _xPoint, titleHeight + graphHeight
						+ ascent);

				if (showDate2) {
					String dateStr2 = dateFormat2.format(date);

					big.drawString(dateStr2, _xPoint, titleHeight + graphHeight
							+ ascent + fHeight);

				}
			}
		} else {
			long xInterval = (maxXValue - minXValue) / xLineCount;

			for (int i = 0; i < xLineCount; i++) {
				long _xValue = minXValue + (xInterval * i);
				int _xPoint = getXPoint(_xValue);
				Date date = new Date(_xValue);
				String dateStr = dateFormat1.format(date);

				big.setColor(lineColor);
				big.drawLine(_xPoint, titleHeight, _xPoint, titleHeight
						+ graphHeight);
				big.setColor(fontColor);
				big.drawString(dateStr, _xPoint, titleHeight + graphHeight
						+ ascent);

				if (showDate2) {
					String dateStr2 = dateFormat2.format(date);
					big.drawString(dateStr2, _xPoint, titleHeight + graphHeight
							+ ascent + fHeight);
				}
			}
		}
	}

	/**
	 * <pre>
	 * 画Y轴
	 * </pre>
	 */
	private void drawYAxis() {
		Font font = big.getFont();
		FontMetrics fm = big.getFontMetrics(font);
		int descent = fm.getDescent();
		// 先设置字体
		big.setFont(font);
		// 画最大值
		// String maxYStr = String.valueOf(maxY);
		// big.drawString(maxYStr, YTextWidth - fm.stringWidth(maxYStr) - 1,
		// titleHeight + descent);
		// // 画最小值
		// String minYStr = String.valueOf(minY);
		// big.drawString(minYStr, YTextWidth - fm.stringWidth(minYStr) - 1,
		// titleHeight + graphHeight);

		// 每隔一定的像素画一条线

		for (int i = 0; i <= yAxisLineCount; i++) {
			long _l = minY + (yAxisInterval * i);
			String _yStr = String.valueOf(_l);
			int _yPosition = getYPoint(_l);
			// 数字
			big.setColor(fontColor);
			big.drawString(_yStr, YTextWidth - fm.stringWidth(_yStr) - 1,
					_yPosition + descent);

			// 画横线
			big.setColor(lineColor);
			big.drawLine(YTextWidth, _yPosition, YTextWidth + graphWidth,
					_yPosition);
		}
	}

	/**
	 * <pre>
	 * 画背景
	 * </pre>
	 */
	private void drawGraphBackgound() {
		big.setColor(backgroundColor);
		// big.fill3DRect(YTextWidth, titleHeight, imgWidth - YTextWidth
		// - cutlineWidth, imgHeight - titleHeight - XTextHeight, false);

		// 填充指定的矩形。该矩形左边缘和右边缘分别位于 x 和 x + width - 1。上边缘和下边缘分别位于 y 和 y + height -
		// 1。得到的矩形覆盖 width 像素宽乘以 height 像素高的区域。使用图形上下文的当前颜色填充该矩形。
		big.fillRect(YTextWidth, titleHeight, graphWidth, graphHeight);

		big.setColor(lineColor);
		// 绘制指定矩形的边框。矩形的左边缘和右边缘分别位于 x 和 x + width。上边缘和下边缘分别位于 y 和 y +
		// height。使用图形上下文的当前颜色绘制该矩形。
		big.drawRect(YTextWidth, titleHeight, graphWidth, graphHeight);

	}

	/**
	 * <pre>
	 * 画标题
	 * </pre>
	 */
	private void drawTitle() {
		if (title == null || title.isEmpty() || titleHeight < 1) {
			return;
		}
		big.setColor(fontColor);
		// Font font = big.getFont();
		Font font = new Font("宋体", Font.BOLD, 14);
		big.setFont(font);
		// ascent = (int) fm.getAscent();
		// descent = (int) fm.getDescent();

		FontMetrics fm = big.getFontMetrics(font);
		int titleWidth = fm.stringWidth(title);

		int titleX = (graphWidth - titleWidth) / 2 + YTextWidth;
		big.drawString(title, titleX, fm.getAscent());
	}

	@Override
	public void paint(Graphics g) {

		super.paint(g);
		// 用图片
		if (bimage != null)
			g.drawImage(bimage, 0, 0, this);

	}

	public SimpleDateFormat getDateFormat1() {
		return dateFormat1;
	}

	/**
	 * <pre>
	 * 设置第一个日期格式器
	 * </pre>
	 * 
	 * @param dateFormat1
	 */
	public void setDateFormat1(SimpleDateFormat dateFormat1) {
		this.dateFormat1 = dateFormat1;
	}

	public boolean isShowDate2() {
		return showDate2;
	}

	/**
	 * <pre>
	 * 设置是否显示第二个日期
	 * </pre>
	 * 
	 * @param showDate2
	 */
	public void setShowDate2(boolean showDate2) {
		this.showDate2 = showDate2;
	}

	public SimpleDateFormat getDateFormat2() {
		return dateFormat2;
	}

	/**
	 * <pre>
	 * 设置第二个日期格式器
	 * </pre>
	 * 
	 * @param dateFormat2
	 */
	public void setDateFormat2(SimpleDateFormat dateFormat2) {
		this.dateFormat2 = dateFormat2;
	}

	public int getYTextWidth() {
		return YTextWidth;
	}

	public void setYTextWidth(int yTextWidth) {
		YTextWidth = yTextWidth;
	}

	public int getXTextHeight() {
		return XTextHeight;
	}

	public void setXTextHeight(int xTextHeight) {
		XTextHeight = xTextHeight;
	}

	public int getTitleHeight() {
		return titleHeight;
	}

	public void setTitleHeight(int titleHeight) {
		this.titleHeight = titleHeight;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCurrentTextWidth() {
		return currentTextWidth;
	}

	public void setCurrentTextWidth(int currentTextWidth) {
		this.currentTextWidth = currentTextWidth;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getLineColor() {
		return lineColor;
	}

	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public Color getGraphColor() {
		return graphColor;
	}

	public void setGraphColor(Color graphColor) {
		this.graphColor = graphColor;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public List<Coordinate> getCoordinateList() {
		return clist;
	}

	public void setCoordinateList(List<Coordinate> clist) {
		this.clist = clist;
	}

	public int getxAxisLineIntervalPixel() {
		return xAxisLineIntervalPixel;
	}

	public void setxAxisLineIntervalPixel(int xAxisLineIntervalPixel) {
		this.xAxisLineIntervalPixel = xAxisLineIntervalPixel;
	}

	public int getDefaultYAxisLineCount() {
		return defaultYAxisLineCount;
	}

	public void setDefaultYAxisLineCount(int defaultYAxisLineCount) {
		this.defaultYAxisLineCount = defaultYAxisLineCount;
	}

	/**
	 * <pre>
	 * 设置标题
	 * </pre>
	 * 
	 * @param title
	 */
	public void setGraphTitle(String title) {
		this.title = title;
	}

}
