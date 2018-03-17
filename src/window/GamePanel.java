package window;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.Timer;

import object.Base;
import object.Bomb;
import object.Bullet;
import object.PowerUp;
import object.Tank;
import object.Terrain;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements ActionListener{
	
	String name;   //玩家名
    Tank player;   //我方坦克
	Base base;     //我方基地
	PowerUp power; //道具
    ArrayList<Tank> enemies;      //敌方坦克
	ArrayList<Terrain> terrain;   //地形
    boolean gameover;   //保存游戏状态
	Timer timer;        //计时器
	ArrayList<Bomb> bombs=new ArrayList<Bomb>();
	int timeKeeper=0,current,x=150,y=500; //游戏结束时显示字符串位置
	//地形位置
	int coords[][] = 
	{
	{50,50},{150,150},{50,90},{150,190},{150,230},{0,360},{555,360},{555,320},{555,280},{190,150},{230,150},
	{270,150},{0,300},{0,260},{90,50},{200,50},{240,50},{280,50},{555,100},{300,300},{340,300},{40,360},{80,360},
	{515,360},{470,320},{400,100},{190,190},{230,230},{140,0},{410,0},{400,230},{230,190},{270,190},{310,190},
	{440,100},{440,140},{50,200},{90,200},{150,340},{120,430},{480,230},{520,230},{440,430},{270,340}
	};
	
	//基地周围地形位置
	int xy[][] = 
	{
	{250,430},{250,390},{290,390},{330,390},{330,430}
	};
	//敌方坦克起始位置
	int xy2[][] =
	{
	{0,0},{300,0},{555,0}
	};
	
	public GamePanel(String name) {
		this.name = name;
        this.addKeyListener(new TAdapter());
        this.setFocusable(true);
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);  //双缓冲区，避免闪屏
        this.gameover = false;
        this.player = new Tank("player",210,430);
		this.base = new Base();
        makeTerrain();
		makeEnemy();
		timer = new Timer(10, this);  //每5ms触发一次actionPerformed方法
        timer.start();
    }
	
	//初始化各种地形
	public void makeTerrain() {
        terrain = new ArrayList<Terrain>();
		int i=0;
		while(i<xy.length) {   //在基地周围画砖
			int type = 0;
            terrain.add(new Terrain(type,xy[i][0], xy[i][1]));
			i++;
        }
		i=0;
        while(i<coords.length) {  //在地图其他位置画各种地形
			int type = (int)(Math.random()*4);
            terrain.add(new Terrain(type,coords[i][0], coords[i][1]));
			i++;
        }
    }
	
	//初始化敌方坦克
	public void makeEnemy() {
        enemies = new ArrayList<Tank>();
		int i=0;
		while(i<3){
			enemies.add(new Tank("enemy",xy2[current][0],xy2[current][1]));
			current++;
			i++;
			if(current==3)
				current=0;
		}
    }
	
	//有敌方坦克死亡时，新增一个坦克
	public void addEnemy() {
		if(timeKeeper%100==0){
			enemies.add(new Tank("enemy",xy2[current][0],xy2[current][1]));
			current++;
			if(current==3)
				current=0;
		}
    }
	
	//重写paint()方法，绘制组件
	@Override
	public void paint(Graphics g){
		super.paint(g);
		//游戏未结束
		if(!gameover){
			//画玩家坦克
			player.drawPlayerTank(g);
			//画基地
			base.draw(g);
	         //画玩家坦克子弹
			ArrayList<Bullet> playerBullets = player.getBullets();
			for (int i = 0; i < playerBullets.size(); i++) {  
				Bullet bullet = playerBullets.get(i);
				bullet.draw(g);
            }
            //画敌方坦克
			for (int i = 0; i < enemies.size(); i++) {
				Tank enemy = enemies.get(i);
                if (!enemy.isDestroyed())
                    enemy.drawEnemyTank(g);
                else
                	enemies.remove(enemy);
                //画敌方坦克子弹
                ArrayList<Bullet> enemyBullets = enemy.getBullets();
				for (int j = 0; j < enemyBullets.size(); j++){ 
					Bullet bullet = enemyBullets.get(j);
					bullet.draw(g);
				}
            }
            //画地形
			for (int i = 0; i < terrain.size(); i++) {
                Terrain t = (Terrain)terrain.get(i);
                t.draw(g);
            }
            //画道具
			if(power!=null){
				power.draw(g);
			}
			//画爆炸效果
			for(int i=0;i<bombs.size();i++){
				Bomb bomb=bombs.get(i);
				if(bomb.getLife()<1)
					bombs.remove(i);
				else
					bomb.draw(g);
			}
			//窗口下方显示敌人剩余数目和玩家生命值
			g.setColor(Color.WHITE);
			g.drawString("Tank lives: "+player.getLife(), 500,464);
			g.drawString("Enemy left: " +
			        (Tank.numberOfEnemyTanks-Tank.numberOfEnemyDestroy), 0,464);
		}else{   //游戏结束
			Font s = new Font("Arial", Font.BOLD, 24);
			g.setColor(Color.white);
			g.setFont(s);
			g.drawString("Game Over! Tanks Killed: " + 
			       Tank.numberOfEnemyDestroy, x,y);
			this.y--;
			if(this.y < 250)
				y=250;
			writeScore();
		}
        g.dispose();
	}
	
	public void actionPerformed(ActionEvent e) {
		timeKeeper++;   //计时
		lookForCollisions();       //碰撞检测
		if(power!=null && power.getState()==false)
			power = null;
		if(timeKeeper % 800==0 && power!=null && power.getState())
			power=null;
		
		//玩家坦克子弹的位置
		ArrayList<Bullet> playerBullets = player.getBullets();
		for (int i = 0; i < playerBullets.size(); i++) {
            Bullet bullet = playerBullets.get(i);
            if (bullet.getState()) 
                bullet.moveForward();
            else playerBullets.remove(i);
        }
		
		//敌方坦克子弹的位置
		for (int i = 0 ;i < enemies.size() ; i++){
			Tank enemy = enemies.get(i);
			ArrayList<Bullet> enemyBullets = enemy.getBullets();
			for (int j = 0; j < enemyBullets.size(); j++){
				Bullet bullet = enemyBullets.get(j);
				if (bullet.getState()) 
	                bullet.moveForward();
	            else enemyBullets.remove(j);
			}
		}
		
		//被毁坏的地形的移除
		for (int i = 0; i < terrain.size(); i++) {
            Terrain f = (Terrain) terrain.get(i);
            if (!f.getState()) 
				terrain.remove(i);
        }
		
		player.move();
		for (int i = 0; i < enemies.size(); i++) {
            Tank f = (Tank) enemies.get(i);
            if(timeKeeper%400==0){  //每隔4秒改变一次方向
            	f.changeDirection();
            }
            if(timeKeeper%200==0){  //每隔2秒发射一次子弹
            	f.fireBullet(f.getDirection());
            }
            f.enemyMove();
            f.move();
            if (f.isDestroyed()){
				enemies.remove(i);
			}
        }
		if((enemies.size() >= 0 && enemies.size() < 3) && 
				((Tank.numberOfEnemyTanks-Tank.numberOfEnemyDestroy)>=3))
			addEnemy();
		
		//游戏结束条件
		if(!base.getState()){
			gameover = true;
		}
		if(Tank.numberOfEnemyTanks==Tank.numberOfEnemyDestroy){
			gameover = true;
		}
		if(player.isDestroyed()){
			gameover=true;
		}
		this.repaint();            //重绘窗口
    }
	
	//检查是否有碰撞发生
	public void lookForCollisions() {
		
        //检测坦克是否触碰到道具
        if(power!=null){
        	player.hit(power,enemies,bombs);
        }
        player.hit(base);
        
		//坦克只能通过森林，砖墙水都不能通过，所以要回到上一步
		for (int i = 0; i<terrain.size(); i++) {
			Terrain t = (Terrain) terrain.get(i);
			player.hit(t);
			//敌方坦克碰到除森林外地形不能前进
			for(int j=0;j<enemies.size();j++){
    			Tank tank=enemies.get(j);
				tank.hit(t);
			}
        }
		
		//玩家坦克碰到敌方坦克，双方坦克都不能前进
		for (int i = 0; i<enemies.size(); i++) {
            Tank tank = enemies.get(i);
            player.hit(tank);
            tank.hit(base);
        }
		
		//玩家坦克射出的子弹与路障的碰撞
        ArrayList<Bullet> list1 = player.getBullets();
        for (int i = 0; i < list1.size(); i++) {
        	Bullet bullet = list1.get(i);
        	for (int j = 0; j<terrain.size(); j++) {
                Terrain t = (Terrain)terrain.get(j);
                bullet.hit(t,bombs);
            }
        	//玩家坦克射出的子弹与敌方坦克的碰撞
            for (int j = 0; j<enemies.size(); j++) {
                Tank tank = enemies.get(j);
                if(bullet.hit(tank,bombs) && tank.getLevel()>2){
                	power=new PowerUp();
                }
            }
            bullet.hit(base,bombs);
        }
        
        //敌方坦克射出的子弹与路障的碰撞
        for(int k=0;k<enemies.size();k++){
        	Tank tank=enemies.get(k);
        	ArrayList<Bullet> list2 = tank.getBullets();
            for (int i = 0; i < list2.size(); i++) {
            	Bullet bullet = list2.get(i);
                for (int j = 0; j<terrain.size(); j++) {
                    Terrain t = (Terrain)terrain.get(j);
                    bullet.hit(t,bombs);
                }
                for (int j = 0; j < list1.size(); j++) {
                	Bullet bullet1 = list1.get(j);
                	bullet.hit(bullet1);
                }
                //敌方坦克射出的子弹与玩家坦克的碰撞
                bullet.hit(player,bombs);
                bullet.hit(base,bombs);
            }
        }
    }
	
	public void writeScore(){
		int scoreNew=player.getScore();
		File file = new File("doc/data.txt");
		int scoreOld=0;
		//取数据:
		try {  
	        DataInputStream in=new DataInputStream(new FileInputStream(file));  
	        try {
	        	scoreOld=in.readInt(); 
	            in.close();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } 
	    } catch (FileNotFoundException e) {  
	        e.printStackTrace();  
	    } 
		//存数据
		if(scoreNew>scoreOld){
			try{
				DataOutputStream out=new DataOutputStream(new FileOutputStream(file));
				try {
					out.writeInt(scoreNew);
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}catch (FileNotFoundException e) {  
		        e.printStackTrace(); 
			}
		}
	}
	
	private class TAdapter extends KeyAdapter {
        public void keyReleased(KeyEvent e) {
          player.keyReleased(e);
        }
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }
    }
}