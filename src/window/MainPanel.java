package window;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.event.*;

//主面板  让用户选择新游戏/最高分
@SuppressWarnings("serial")
public class MainPanel extends JPanel implements ActionListener{
	
	JButton btn_newGame,btn_topScorers;
	Image img_background;
	Image img_tank,img_tank2,img_trees;
	int x = 10,y = 500;
	int x2 = 52,y2 = 600;
	Timer timer = new Timer(20,this); //定时器每20ms触发一个ActionEvent
	
	public MainPanel(){
		this.setLayout(null);  //空布局，即绝对布局
		btn_newGame = new JButton("New Game");
		btn_newGame.setBounds(245,275,125,30);
		btn_topScorers = new JButton("Top Scorer");
		btn_topScorers.setBounds(245,315,125,30);
		this.add(btn_newGame);
		this.add(btn_topScorers);
		this.setBackground(new Color(0,0,0));
		
		//加载图片
		try{
			img_background = ImageIO.read(new File("images/background.jpg"));
		    img_tank = ImageIO.read(new File("images/mytank.png"));
		    img_tank2 = ImageIO.read(new File("images/enemytank.png"));
		    img_trees = ImageIO.read(new File("images/trees.png"));
		}catch(Exception e){
			e.printStackTrace();
		}
		timer.start();
	}
	
	//绘制组件
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);   //确保在显示新的图画之前清空视图区域
		g.drawImage(img_background,8,0,null);
		g.drawImage(img_tank,x,y,this);
		g.drawImage(img_tank2,x2,y2,this);
		g.drawImage(img_trees,10,50,this);
		g.drawImage(img_trees,10,90,this);
		setForeground(Color.WHITE);  //前景色
		g.setFont(new Font("Georgia", Font.ITALIC, 12)); //设置字体
		g.drawString("by 15121295 Ziting You", 435,463);
		
		y-=2;
		y2-=3;
		if(y<=89 && y>-40){ //经过森林后坦克变颜色
			try{
				img_tank = ImageIO.read(new File("images/mytank2.png"));
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if(y<-40){  //坦克到窗口外后重回窗口底部
			try{
				img_tank = ImageIO.read(new File("images/mytank.png"));
			}catch(Exception e){
				e.printStackTrace();
			}
			y = 500;
		}
		if(y2<-40){
			y2 = 600;
		}
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==timer){
			this.repaint();  //窗口重绘
		}
	}
}