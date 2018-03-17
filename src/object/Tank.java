package object;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.Timer;

import java.util.ArrayList;

public class Tank implements ActionListener{
	
	private String tankType;                        //坦克类型
	private int lifePoints,score=0;                 //生命值、分值
	private int moveSpeed,level=1;                  //坦克移动速度、等级
	public static int numberOfEnemyTanks = 30;      //敌方坦克数量
	public static int numberOfEnemyDestroy=0;       //敌方坦克死亡数
	private ArrayList<Bullet> bullets;              //子弹
	private String direction;                       //方向 n/s/w/e
	public static int width=40,height=40;           //坦克大小
	Image tank;          //坦克图标
	public int x,y;      //坦克坐标
	public int x1,y1;    //坦克上一步坐标
	int dx,dy;           //前进距离
	int enemyType;       //敌方坦克类型
	Timer timer=new Timer(10,this);
	int timeKeeper=0;
	
	//构造函数
	public Tank(String type,int x,int y){
		this.tankType = type;
		this.x = x;
		this.y = y;
		appear();
		this.moveSpeed = 1;
		bullets = new ArrayList<Bullet>();
	}
	
	//获取坦克图标
	public void appear(){
		try{
			if(this.tankType.equals("player")){
				direction="N";
				getPlayerImage();
				this.lifePoints = 3;
			}else if(this.tankType.equals("enemy")){
				direction="S";
				enemyType = (int)(Math.random()*5)+1;
				level=(int)(Math.random()*4)+1;
				getEnemyImage();
				lifePoints = enemyType;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Image getEnemyImage() {
		String path="images/T"+enemyType+"_L"+level+"_"+direction+"1.png";
		try {
			tank=ImageIO.read(new File(path));
		} catch(Exception e){
			e.printStackTrace();
		}
		return tank;
	}
	
	public Image getPlayerImage(){
		String path="images/P1_L"+level+"_"+direction+"1.png";
		try {
			tank = ImageIO.read(new File(path));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tank;
	}
	
	//类型
	public String getType(){
		return this.tankType;
	}
	public int getEnemyType(){
		return this.enemyType;
	}
	public void incLevel(){
		if(level<4)
			level++;
	}
	public void decLevel(){
		if(level>1)
			level--;
	}
	public int getLevel(){
		return level;
	}
	//画出坦克
	public void drawEnemyTank(Graphics g){
		g.drawImage(getEnemyImage(), x, y, null);
	}
	public void drawPlayerTank(Graphics g){
		g.drawImage(getPlayerImage(), x, y, null);
	}
	//分数
	public int getScore(){
		this.score = 10 * numberOfEnemyDestroy;
		return this.score;
	}
	
	//速度
	public int getSpeed(){
		return this.moveSpeed;
	}
    public void setSpeed(int n){
		this.moveSpeed = n;
	}
	public void incSpeed(){
		if(moveSpeed<4)
			moveSpeed++;
	}
	public void decSpeed(){
		if(moveSpeed>1)
			moveSpeed--;
	}
	
	//生命值
    public int getLife(){
		return this.lifePoints;
	}
	
	public void incLife(){
		this.lifePoints++;
	}
	
	public void decLife(){
		lifePoints--;
	}
	
	public void destroyed(){
		this.lifePoints=0;
	}
	
	public boolean isDestroyed(){
		return lifePoints == 0;
	}
	
	//敌方坦克死亡
	public static void incTankDest(){
		numberOfEnemyDestroy++;
	}
	
	//坦克消失
	public void disappear(){
		this.tank = null;
	}
	
	//发射子弹
	public void fireBullet(String direction){
		if(direction.equals("N"))
			this.bullets.add(new Bullet((x + width/2)-5, y,direction,this.moveSpeed+1));
		else if(direction.equals("W"))
			this.bullets.add(new Bullet(x , (y+height/2)-5,direction,this.moveSpeed+1));
		else if(direction.equals("E"))
			this.bullets.add(new Bullet(x +width, (y+height/2)-5,direction,this.moveSpeed+1));
		else if(direction.equals("S"))
			this.bullets.add(new Bullet((x +width/2)-5, y+height,direction,this.moveSpeed+1));
	}
	public ArrayList<Bullet> getBullets() {
        return this.bullets;
    }
	
	//系统坦克的随机转换方向
    public void changeDirection(){
    	int index =  (int)(Math.random()*4);
		String [] directions={"N","S","W","E"};
		direction=directions[index];
    }

	//坐标与边界
	public String getDirection(){
		return this.direction;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public Rectangle bounds() {
        return new Rectangle(x, y, width, height);
    }
	
	public void hit(Base base){
		Rectangle bound=base.bounds();
		if(bounds().intersects(bound)){
			x = x1;
			y = y1;
		}
	}
	public void hit(Tank tank){
		Rectangle bound=tank.bounds();
		if(bounds().intersects(bound)){
			x=x1;
			y=y1;
			tank.x=tank.x1;
			tank.y=tank.y1;
			if(timeKeeper % 10==0){
				tank.changeDirection();
			}
		}
	}
	public void hit(Terrain terrain){
		Rectangle bound=terrain.bounds();
		if(bounds().intersects(bound)){
			if(terrain.getType()!="tree"){
				x = x1;
				y = y1;
				if(timeKeeper % 10==0 && tankType=="enemy"){
					changeDirection();
				}
			}
		}
	}
	public void hit(PowerUp power,ArrayList<Tank> enemies,ArrayList<Bomb> bombs){
		Rectangle bound=power.bounds();
		if(bounds().intersects(bound)){
			if(power.getType()==PowerUp.fire){
				incLevel();
				incSpeed();
				for(int i=0;i<bullets.size();i++){
					Bullet bullet=bullets.get(i);
					bullet.incFPower();
					bullet.incSpeed();
				}
			}else if(power.getType()==PowerUp.plus){
				incLife();
			}else if(power.getType()==PowerUp.destroy){
				for(int i=0;i<enemies.size();i++){
					Tank enemy=enemies.get(i);
					Bomb b = new Bomb(enemy.getX(),enemy.getY());
			        bombs.add(b);
	                enemy.destroyed();
					Tank.incTankDest();
					
				}
			}
			power.setState(false);
		}
	}
	
	//监听按下键盘事件
	public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE) {
            fireBullet(direction);
        }
        if (key == KeyEvent.VK_LEFT) {
			dx = -moveSpeed;
			direction="W";
        }
        if (key == KeyEvent.VK_RIGHT) {
			dx = moveSpeed;
			direction="E";
        }

        if (key == KeyEvent.VK_UP) {
			dy = -moveSpeed;
			direction="N";
        }

        if (key == KeyEvent.VK_DOWN) {
			dy = moveSpeed;
			direction="S";
        }
    }
	
	public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }
	
	public void move() {
		
		x1=x;
		y1=y;
        x += dx;
        y += dy;
        
        if (x < 0) {
            x = 0;
            if(timeKeeper % 10==0 &&tankType=="enemy")
               changeDirection();
        }

        if (y < 0) {
            y = 0;
            if(timeKeeper % 10==0 &&tankType=="enemy")
                changeDirection();
        }
		if(x>= 560){
			x = 560;
			if(timeKeeper % 10==0 &&tankType=="enemy")
                changeDirection();
		}
		if(y>=425){
			y=425;
			if(timeKeeper % 10==0 &&tankType=="enemy")
                changeDirection();
		}
    }
	
	//系统坦克的移动
	public void enemyMove(){
		if(timeKeeper % 10==0){
			switch(direction){
		        case "N":
		        	dy = -moveSpeed;
		        	dx = 0;
		        	break;
		        case "S":
		        	dy = moveSpeed;
		        	dx = 0;
		        	break;
		        case "W":
		        	dx = -moveSpeed;
		        	dy = 0;
		        	break;
		        case "E":
		        	dx = moveSpeed;	
		        	dy = 0;
		        	break;
		    }
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		timeKeeper++;
	}
}