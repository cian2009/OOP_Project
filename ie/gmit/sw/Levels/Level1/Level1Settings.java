package ie.gmit.sw.Levels.Level1;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

import ie.gmit.sw.game.Collision;
import ie.gmit.sw.game.FindExit;
import ie.gmit.sw.game.Images;
import ie.gmit.sw.sprites.Direction;
import ie.gmit.sw.sprites.EnemyMovement;
import ie.gmit.sw.sprites.Point;
import ie.gmit.sw.sprites.Sprite;

public class Level1Settings extends JPanel implements ActionListener, KeyListener { 
	private static final long serialVersionUID = 777L;
	private static final int DEFAULT_IMAGE_INDEX = 0;
	
	public static final int DEFAULT_VIEW_SIZE = 1280;
	private static final int TILE_WIDTH = 128;
	private static final int TILE_HEIGHT = 64;
	private Sprite player;
	private Sprite[] enemies  = new Sprite[1];
	private JLabel infoLabel;
	private EnemyMovement enemy1Move = new EnemyMovement();
	
	//Do we really need two models like this?
	private int[][] matrix;
	private int[][] things;
	private int[] local;
	private JFrame frame;
	
	private BufferedImage[] tiles; //Note that all images, including sprites, have dimensions of 128 x 64. This make painting much simpler.
	private BufferedImage[] objects; //Taller sprites can be created, by using two tiles (head torso, lower body and legs) and improve animations
	private Color[] cartesian = {Color.GREEN, Color.GRAY, Color.DARK_GRAY, Color.ORANGE, Color.CYAN, Color.YELLOW, Color.PINK, Color.BLACK}; //This is a 2D representation
	
	private Timer timer; //Controls the repaint interval.
	private boolean isIsometric = true; //Toggle between 2D and Isometric (Z key)
	
	public Level1Settings(int[][] matrix, int[][] things, JLabel infoLabel, JFrame frame) throws Exception {
		init();
		this.matrix = matrix;
		this.things = things;
		this.infoLabel = infoLabel;
		this.local = FindExit.main(things);
		this.frame = frame;
		
		setBackground(Color.WHITE);
		setDoubleBuffered(true); //Each image is buffered twice to avoid tearing / stutter
		timer = new Timer(100, this); //calls the actionPerformed() method every 100ms
		timer.start(); //Start the timer
	}
	
	private void init() throws Exception {
		tiles = Images.loadImages("./resources/images/ground", tiles);
		objects = Images.loadImages("./resources/images/objects", objects);
		
		player = new Sprite("Player 1", new Point(1, 1), Images.loadImages("./resources/images/sprites/default", null), Direction.DOWN);
		
		enemies[0] = new Sprite("Enemy 1", new Point(1, 5), Images.loadImages("./resources/images/sprites/knight", null), Direction.RIGHT);
	}

	public void toggleView() {
		isIsometric = !isIsometric;
		this.repaint();
	}

	public void actionPerformed(ActionEvent e) { //This is called each time the timer reaches zero
		this.repaint();
	}

	public void paintComponent(Graphics g) { //This method needs to execute quickly...
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		int imageIndex = -1, x1 = 0, y1 = 0;
		Point point;
		
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[row].length; col++) {
				imageIndex = matrix[row][col];
				
				if (imageIndex >= 0 && imageIndex < tiles.length) {
					//Paint the ground tiles
					if (isIsometric) {
						x1 = getIsoX(col, row);
						y1 = getIsoY(col, row);
						
						g2.drawImage(tiles[DEFAULT_IMAGE_INDEX], x1, y1, null);
						if (imageIndex > DEFAULT_IMAGE_INDEX) {
							g2.drawImage(tiles[imageIndex], x1, y1, null);
						}
					} else {
						x1 = col * TILE_WIDTH;
						y1 = row * TILE_HEIGHT;
	        			if (imageIndex < cartesian.length) {
	        				g2.setColor(cartesian[imageIndex]);
	        			}else {
	        				g2.setColor(Color.WHITE);
	        			}
						
	        			g2.fillRect(x1, y1, TILE_WIDTH, TILE_WIDTH);
					}
					//Paint the object or things on the ground
					
					
					imageIndex = things[row][col];
					g2.drawImage(objects[imageIndex], x1, y1, null);
				}
			}
		}
		
		//Paint the player on  the ground
		point = getIso(player.getPosition().getX(), player.getPosition().getY());
		g2.drawImage(player.getImage(), point.getX(), point.getY(), null);
		
		point = getIso(enemies[0].getPosition().getX(), enemies[0].getPosition().getY());
		g2.drawImage(enemies[0].getImage(), point.getX(), point.getY(), null);
		
		enemy1Move.startMove(enemies[0], matrix, 5, 7);
		
		Collision.playerCollision(enemies, player, infoLabel, local, frame, 1, 1);
	}
	
	//This method breaks the SRP
	private int getIsoX(int x, int y) {
		int rshift = (DEFAULT_VIEW_SIZE/2) - (TILE_WIDTH/2) + (x - y); //Pan camera to the right
		return (x - y) * (TILE_WIDTH/2) + rshift;
	}

	//This method breaks the SRP
	private int getIsoY(int x, int y) {
		return (x + y) * (TILE_HEIGHT/2);
	}
	
	//This method breaks the SRP
	private Point getIso(int x, int y) {
		return new Point(getIsoX(x, y), getIsoY(x, y)); //Could be more efficient...
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.setDirection(Direction.RIGHT);
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.setDirection(Direction.LEFT);
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			player.setDirection(Direction.UP);
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			player.setDirection(Direction.DOWN);
		} else if (e.getKeyCode() == KeyEvent.VK_Z) {
			toggleView();
		} else if (e.getKeyCode() == KeyEvent.VK_X) {
			player.move(infoLabel, matrix);
		} else if (e.getKeyCode() == KeyEvent.VK_0) {
			System.out.println("Direction: " + player.getDirection());
			System.out.println("x: " + player.getPosition().getX());
			System.out.println("y: " + player.getPosition().getY());
			System.out.println("Tile: " + matrix[player.getPosition().getY()][player.getPosition().getX()]);
		} else {
			return;
		}
	}
	
	public void keyReleased(KeyEvent e) {
	} // Ignore
	
	public void keyTyped(KeyEvent e) {
	} // Ignore
}