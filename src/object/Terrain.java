package object;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

//地形 包括砖、墙、水、树
public class Terrain{
	
	String type;        //地形类型
	private boolean currState;
	Image iron;              //地形图标
	public int x,y;                    //坐标
	public static int width=40,height=40; 
	
	public Terrain(int n,int x,int y){
		if(n==0)
			this.type = "brick";
		if(n==1)
			this.type = "metal";
		if(n==2)
			this.type = "water";
		if(n==3)
			this.type = "tree";
		this.x=x;
		this.y=y;
		this.currState = true;
		try{
			if(this.type.equals("brick")){
				this.iron = ImageIO.read(new File("images/bricks.png")); //砖
			}
			if(this.type.equals("metal")){
				this.iron = ImageIO.read(new File("images/wall.png"));   //墙
			}
			if(this.type.equals("water")){
				this.iron = ImageIO.read(new File("images/water.png"));  //水
			}
			if(this.type.equals("tree")){
				this.iron = ImageIO.read(new File("images/trees.png"));  //树
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//类型
	public void setType(String type){
		this.type = type;
	}
	public String getType(){
		return this.type;
	}
	
	//状态
	public void setState(boolean state){
		this.currState = state;
	}
	public boolean getState(){
		return this.currState;
	}
	
    //边界
	public Rectangle bounds() {
        return new Rectangle(x, y, width, height);
    }
	
	public void draw(Graphics g){
		g.drawImage(iron, x, y, null);
	}
}