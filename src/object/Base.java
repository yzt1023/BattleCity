package object;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;

public class Base{
	
	Image iron;            //基地图标
	int x,y,width=40,height=40;  //坐标(290,430)
	boolean currState;
	
	public Base(){
		this.x = 290;
		this.y = 430;
		currState=true;
		try{
			iron=ImageIO.read(new File("images/base.png"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setState(boolean b){
		currState=b;
	}
	public boolean getState(){
		return currState;
	}
	
    //获取基地边界
	public Rectangle bounds() {
        return new Rectangle(x, y, width, height);
    }
	
	public void draw(Graphics g){
		g.drawImage(iron, x, y, null);
	}
}