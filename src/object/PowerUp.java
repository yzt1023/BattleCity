package object;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class PowerUp{
	
	public static int destroy=0;
	public static int plus=1;
	public static int fire=2;
	private int type;
	private boolean currState; //道具状态
	Image iron;             //道具图标
	public int x,y;            //坐标
	int width = 40,height = 40;
	
	public PowerUp(){
		this.type = (int)(Math.random()*3);
    	this.currState = true;
		this.x=(int)(Math.random()*564);
		this.y=(int)(Math.random()*437);
		try{
			if(type==destroy)
				iron = ImageIO.read(new File("images/Powerup_Grenade.png"));
			else if(type==plus)
				iron = ImageIO.read(new File("images/Powerup_Tank.png"));
			else if(type==fire)
				iron = ImageIO.read(new File("images/Powerup_Star.png"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setType(int type){
		this.type = type;
	}

	public int getType(){
		return this.type;
	}
	
	public void setState(boolean state){
		this.currState = state;
	}
	
	public boolean getState(){
		return currState;
	}
	
	public Rectangle bounds() {
        return new Rectangle(x, y, width, height);
    }
	
	public void draw(Graphics g){
		g.drawImage(iron, x, y, null);
	}
}