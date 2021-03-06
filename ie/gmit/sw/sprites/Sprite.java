package ie.gmit.sw.sprites;

/*
 *   
 *   BufferedImage[][]
 *   --------------------------
 *   {U0, U1, U2, U3}, =>Up
 *   {D0, D1, D2, D3}, =>Down
 *   {L0, L1, L2, L3}, =>Left
 *   {R0, R1, R2, R3}, =>Right
 * 
 */

import java.awt.image.*;

import javax.swing.JLabel;
public class Sprite { //Sprite belongs in some sort of hierarchy....
	private String name; //The name of the sprite
	private BufferedImage[][] images = new BufferedImage[4][3]; //The images used in the animation 
	private Direction direction; //The current orientation of the sprite
	private int index = 0; //The current image index.
	private Point position; //The current x, y position
	
	public Sprite(String name, Point p, Direction d) {
		super();
		this.name = name;
		this.position = p;
		this.direction = d;
	}
	
	public Sprite(String name, Point p, BufferedImage[] img, Direction d) {
		this(name, p, d);
		int row = 0, col = 0;
		for (int i = 0; i < img.length; i++) {
			images[row][col] = img[i];
			if (col == images[row].length - 1) {
				row++;
				col = 0;
			}else {
				col++;
			}
		}
	}
	
	public String getName() {
		return name;
	}

	public Point getPosition() {
		return position;
	}
	
	public void setPosition(int x, int y) {
		position.setX(x);
		position.setY(y);
	}

	public BufferedImage getImage() {
		return images[direction.getOrientation()][index];
	}
	
	public BufferedImage step(Direction d) {
		setDirection(d);
		if (index < images[direction.getOrientation()].length - 1) {
			index++;
		}else {
			index = 0;
		}
		
		return images[d.getOrientation()][index];
	}
	
	public void setDirection(Direction d) {
		direction = d;
	}
	
    public Direction getDirection() {
        return this.direction;
    }
	
	public void move(JLabel infoLabel, int[][] matrix)  { //This method is suspiciously like one I've seen already....
		step(direction);
		
		switch(direction.getOrientation()) {
		case 1: // Down
			if(position.getY()+1 < matrix.length && matrix[position.getY()+1][position.getX()] != 4)
				position.setY(position.getY() + 1); //UP
			else
				try{
					infoLabel.setText("You can go no further");
				}catch (NullPointerException e){
					
				}
			break;
		case 2: // Left
			if(position.getX()-1 > -1 && matrix[position.getY()][position.getX()-1] != 4)
				position.setX(position.getX() - 1); //DOWN
			else
				try{
					infoLabel.setText("You can go no further");
				}catch (NullPointerException e){
					
				}
			break;
		case 3: // Right
			if(position.getX()+1 < matrix.length && matrix[position.getY()][position.getX()+1] != 4)
				position.setX(position.getX() + 1); //LEFT
			else
				try{
					infoLabel.setText("You can go no further");
				}catch (NullPointerException e){
					
				}
			break;
		default: // Up
			if(position.getY()-1 > -1 && matrix[position.getY()-1][position.getX()] != 4)
				position.setY(position.getY() - 1); //RIGHT
			else
				try{
					infoLabel.setText("You can go no further");
				}catch (NullPointerException e){
					
				}
			break;
		}
	}
}