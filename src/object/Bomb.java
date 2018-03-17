package object;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bomb {
	int x,y;
	int life=2;
	Image iron;
	public Bomb(int x,int y){
		this.x=x;
		this.y=y;
		appear();
	}
	public void decLife(){
		life--;
	}
	public int getLife(){
		return life;
	}
	public void appear(){
		try {
			if(life>1)
				iron=ImageIO.read(new File("images/die1.png"));
			else
				iron=ImageIO.read(new File("images/die2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void draw(Graphics g){
		appear();
		g.drawImage(iron,x, y,null);
		decLife();
	}
}
