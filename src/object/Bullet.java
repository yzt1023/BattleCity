package object;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Bullet{
	
	private int bulletSpeed,firePower;     //子弹速度、火力值
	private boolean currState;             //子弹状态
	Image iron;                            //子弹图标
	int x,y;                               //子弹坐标
	public int width=8, height=8;          //子弹宽高
	String direction;                      //子弹方向	
	public Bullet(int x, int y,String d,int speed){
		this.bulletSpeed = 1;
		this.bulletSpeed = speed;
		this.firePower = bulletSpeed;
		this.currState = true;
		this.x = x;
		this.y = y;
		direction = d;
		try{
			String path="images/bullet"+direction+".png";
			this.iron = ImageIO.read(new File(path));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//根据子弹方向的不同确定子弹前进坐标
	public void moveForward(){
		if(direction.equals("N")){
			this.y -= this.bulletSpeed;
			if (y < 0)
				currState = false;
		}
		else if(direction.equals("W")){
			this.x -= this.bulletSpeed;
			if (x < 0)
				currState = false;
		}
		else if(direction.equals("E")){
			this.x += this.bulletSpeed;
			if (x > 600)
				currState = false;
		}
		else if(direction.equals("S")){
			this.y += this.bulletSpeed;
			if (y > 500)
				currState = false;
		}
	}
	
	//速度
	public void setSpeed(int n){
		this.bulletSpeed = n;
	}
	public int getSpeed(){
		return this.bulletSpeed;
	}
	public void incSpeed(){
		if(bulletSpeed<4){
			bulletSpeed++;
		}
	}
	public void decSpeed(){
		if(bulletSpeed>1){
			bulletSpeed--;
		}
	}
	
	//火力值
	public int getPower(){
		return this.firePower;
	}
	public void incFPower(){
		if(firePower<4)
			firePower++;
	}
	public void decFPower(){
		if(firePower>1)
			firePower++;
	}	
	
	//状态
	public void setState(boolean state){
		this.currState = state;
	}public boolean getState(){
		return this.currState;
	}
	
	public Rectangle bounds() {
        return new Rectangle(x, y, width, height);
    }
	public void hit(Terrain t,ArrayList<Bomb> bombs){
		Rectangle bound=t.bounds();
		if(bounds().intersects(bound)){
			if(t.getType().equals("brick")){
				setState(false);
				t.setState(false);
				Bomb b = new Bomb(x,y);
                bombs.add(b);
			}else if(t.getType().equals("metal")){
				if(firePower>3){
					t.setState(false);
				}
				setState(false);
				Bomb b = new Bomb(x,y);
                bombs.add(b);
			}
		}
	}
	
	public void hit(Base b,ArrayList<Bomb> bombs){
		Rectangle bound=b.bounds();
		if(bounds().intersects(bound)){
			b.setState(false);
			setState(false);
			Bomb bomb=new Bomb(x,y);
			bombs.add(bomb);
		}
	}
	
	public void hit(Bullet b){
		Rectangle bound=b.bounds();
		if(bounds().intersects(bound)){
			b.setState(false);
			setState(false);
		}
	}
	
	public boolean hit(Tank tank,ArrayList<Bomb> bombs){
		Rectangle bound=tank.bounds();
		if(bounds().intersects(bound)){
			tank.decLife();
			tank.decLevel();
			tank.decSpeed();
			if(tank.getType()=="enemy" && tank.isDestroyed()){
				Tank.incTankDest();
				tank.destroyed();
			}
			ArrayList<Bullet> bullets=tank.getBullets();
			for(int i=0;i<bullets.size();i++){
				Bullet bullet=bullets.get(i);
				bullet.decFPower();
				bullet.decSpeed();
			}
			setState(false);
			Bomb b = new Bomb(x,y);
	        bombs.add(b);
	        return true;
		}
		return false;
	}
	
	public void draw(Graphics g){
		g.drawImage(iron, x, y, null);
	}
}