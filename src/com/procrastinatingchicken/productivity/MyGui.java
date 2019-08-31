package com.procrastinatingchicken.productivity;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author averychan
 */
public class MyGui implements MouseListener, MouseMotionListener {

	private static JFrame frame;
	private static Graphics2D offGraphic, onGraphic;
	private static BufferedImage offImage, onImage;
	private static final Color CLEAR = Color.WHITE;
	private static final Color DEFAULT_PEN = Color.BLACK;
	private static int mouseX, mouseY;
	private static int width = 500, height = 500;
	private static boolean defer = false, mouseDown = false, mouseClicked = false, mouseDragged=  false;

	private static MyGui guiListener = new MyGui();

	// not sure when this would ever be used... always be used as static?
	MyGui() {
	}

	/**
	 * Setup a JFrame with a JLabel with specified image (BufferedImage) which can be drawn on by Graphic2D object
	 */
	protected static void init() {
//		setup
		frame = new JFrame();
//		sets bound of Window, position of window on screen and then dimentions
		frame.setBounds(100, 00, width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

//		BufferedImage types, not sure about the ARGB, it's something about image types
		offImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		onImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

//		Creates a Graphics2D, which can be used to draw into this BufferedImage.
		offGraphic = offImage.createGraphics();
		onGraphic = onImage.createGraphics();

//		Swing components can be decorated with an icon — a fixed-sized picture
		ImageIcon icon = new ImageIcon(onImage);
		JLabel draw = new JLabel(icon);

		frame.setContentPane(draw);
		frame.pack();

		draw.addMouseListener(guiListener);
		draw.addMouseMotionListener(guiListener);
//		frame.addMouseListener(guiListener);
//		frame.addMouseMotionListener(guiListener);

//		frame.pack();
		frame.setVisible(true);

		clear();
	}

	protected static void init(int w, int h) {
		width = w;
		height = h;
//		setup
		frame = new JFrame();
//		sets bound of Window, position of window on screen and then dimentions
		frame.setBounds(100, 00, width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

//		BufferedImage types, not sure about the ARGB, it's something about image types
		offImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		onImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

//		Creates a Graphics2D, which can be used to draw into this BufferedImage.
		offGraphic = offImage.createGraphics();
		onGraphic = onImage.createGraphics();

//		Swing components can be decorated with an icon — a fixed-sized picture
		ImageIcon icon = new ImageIcon(onImage);
		JLabel draw = new JLabel(icon);

		frame.setContentPane(draw);
		frame.pack();

		draw.addMouseListener(guiListener);
		draw.addMouseMotionListener(guiListener);
//		frame.addMouseListener(guiListener);
//		frame.addMouseMotionListener(guiListener);

//		frame.pack();
		frame.setVisible(true);

		clear();
	}

	/**
	 * Clears display by making displayed image a white box
	 */
	public static void clear() {
//		System.out.println("clearing...");
//		offGraphic.setColor(Color.RED); // Red so it is easier to see if clear has been called and has intended effect
		offGraphic.setColor(CLEAR);
		offGraphic.fillRect(0, 0, width, height);
		offGraphic.setColor(DEFAULT_PEN);
		draw();
	}

	/**
	 * Set color to draw into BufferedImage with.
	 * 
	 * @param c Color chosen.
	 */
	public static void setPen(Color c) {
		offGraphic.setColor(c);
	}

	/**
	 * Set color to draw into BufferedImage as the default (Color.BLACK).
	 */
	public static void setPen() {
		offGraphic.setColor(DEFAULT_PEN);
	}

	/**
	 * Draws offImage into onImage which is displayed in JFrame as long as defer is false
	 */
	public static void draw() {
//		System.out.println("drawing...");
		if (!defer)
			show();
	}

	/**
	 * Updates all deffered drawn shapes.
	 */
	public static void show() {
		onGraphic.drawImage(offImage, 0, 0, null); // The Graphic2D object is used to draw onto the BufferedImage "onImage" which is put into an image icon > J label > Jframe
		frame.repaint();
	}

	/**
	 * Draws a rectangle into buffered image.
	 * 
	 * @param x      the x position of the rectangle
	 * @param y      the y position of the rectangle
	 * @param width  the width of the rectangle
	 * @param height the height of the rectangle
	 */
	public static void rectangle(int x, int y, int width, int height) {
		offGraphic.draw(new Rectangle2D.Double(x, y, width, height));
		draw();
	}

	/**
	 * Draws a filled rectangle into buffered image.
	 * 
	 * @param x      the x position of the rectangle
	 * @param y      the y position of the rectangle
	 * @param width  the width of the rectangle
	 * @param height the height of the rectangle
	 */
	public static void rectangleFilled(int x, int y, int width, int height) {
		offGraphic.fill(new Rectangle2D.Double(x, y, width, height));
		draw();
	}

	/**
	 * Draws a ellipse into buffered image bounded by rectangle.
	 * 
	 * @param x      the x position of the ellipse
	 * @param y      the y position of the ellipse
	 * @param width  the width of the ellipse
	 * @param height the height of the ellipse
	 */
	public static void ellipse(int x, int y, int width, int height) {
		offGraphic.draw(new Ellipse2D.Double(x, y, width, height));
		draw();
	}

	/**
	 * Draws a filled ellipse into buffered image bounded by rectangle.
	 * 
	 * @param x      the x position of the ellipse
	 * @param y      the y position of the ellipse
	 * @param width  the width of the ellipse
	 * @param height the height of the ellipse
	 */
	public static void ellipseFilled(int x, int y, int width, int height) {
		offGraphic.fill(new Ellipse2D.Double(x, y, width, height));
		draw();
	}

	/**
	 * Draws a line into buffered image.
	 * 
	 * @param x1 the x position of the first point
	 * @param y1 the y position of the first point
	 * @param x2 the x position of the second point
	 * @param y2 the y position of the second point
	 */
	public static void line(int x1, int y1, int x2, int y2) {
		offGraphic.draw(new Line2D.Double(x1, y1, x2, y2));
		draw();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
//		System.out.println("Dragged: (" + e.getX() + ", " + e.getY() + ")");
//		System.out.println(e);
//		mouseX = e.getX();
//		mouseY = e.getY();
		mouseDragged = true;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
//		System.out.println(e);
		mouseX = e.getX();
		mouseY = e.getY();
		mouseClicked = false;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
//		System.out.println(e);
//		System.out.println("Click: (" + e.getX() + ", " + e.getY() + ")");
		mouseClicked = true;
//		mouseX = e.getX();
//		mouseY = e.getY();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
//		System.out.println("Pressed: (" + e.getX() + ", " + e.getY() + ")");
//		System.out.println(e);
		mouseDown = true;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
//		System.out.println("Released: (" + e.getX() + ", " + e.getY() + ")");
//		System.out.println(e);
		mouseDown = false;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
//		System.out.println("Mouse Entered: (" + e.getX() + ", " + e.getY() + ")");
//		System.out.println(e);
		mouseX = e.getX();
		mouseY = e.getY();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
//		System.out.println("Mouse Exited: (" + e.getX() + ", " + e.getY() + ")");
//		System.out.println(e);
		mouseX = e.getX();
		mouseY = e.getY();
	}

	/**
	 * Set boolean value of mouseClicked
	 * @param value the boolean value mouseClicked will take
	 */
	public static void setMouseClicked(boolean value) {
		mouseClicked = value;
	}
	
	/**
	 * @param value
	 */
	public static void setMouseDown(boolean value) {
		mouseDown = value;
	}
	
	/**
	 * @param value
	 */
	public static void setMouseDragged(boolean value) {
		mouseDragged = value;
	}

	/**
	 * @return the x position of the mouse in relation to the JComponent it's added to.
	 */
	public static int mouseX() {
		return mouseX;
	}

	/**
	 * @return the y position of the mouse in relation to the JComponent it's added to.
	 */
	public static int mouseY() {
		return mouseY;
	}

	/**
	 * @return Boolean value of mouse down (pressed down)
	 */
	public static boolean mouseDown() {
		return mouseDown;
	}

	/**
	 * @return Boolean vale of mouse clicked
	 */
	public static boolean mouseClicked() {
		return mouseClicked;
	}
	
	/**
	 * @return Boolean value of mouse dragged
	 */
	public static boolean mouseDragged() {
		return mouseDragged;
	}

	/**
	 * Enables double buffering. Double buffering waits until show() method is called rather than just the draw() method called by each shape drawing method. Double buffering is disabled by default.
	 */
	public static void enableDoubleBuffering() {
		defer = true;
	}

	/**
	 * Disables double buffering. Double buffering is disabled by default.
	 */
	public static void disableDoubleBuffering() {
		defer = false;
	}

	public static void main(String[] args) {
		init();
		mouseX = 0;
		mouseY = 0;
		while (true) {
			clear();
			ellipse(mouseX - 5, mouseY - 5, 10, 10);
			draw();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}