package window;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.event.*;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class NGPanel extends JPanel implements ActionListener{
	JButton btn_gameStart,btn_back;
	JTextArea name;
	Image img_background;
	Image img_tank,img_tank2;
	int x = 420,y=500;
	int x2 = 470,y2=600;
	Timer timer = new Timer(20,this);
		
	public NGPanel(){
		this.setLayout(null);  //空布局
		this.setBackground(Color.BLACK);
		//新建组件
		name = new JTextArea("");
		btn_gameStart = new JButton("Game Start");
		btn_back = new JButton("Back to Main Menu");
		//给组建定位
		name.setBounds(245,275,125,22);
		btn_gameStart.setBounds(245,315,125,30);
		btn_back.setBounds(225,353,163,30);
		//将组件添加到面板中
		this.add(name);
		this.add(btn_gameStart);
		this.add(btn_back);
		//加载图片
		try{
			img_background = ImageIO.read(new File("images/background.jpg"));
		    img_tank = ImageIO.read(new File("images/mytank.png"));
		    img_tank2 = ImageIO.read(new File("images/enemytank.png"));
		}catch(Exception e){
			e.printStackTrace();
		}
		timer.start();	
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(img_background,8,0,null);
		g.drawImage(img_tank,x,y,this);
		g.drawImage(img_tank2,x2,y2,this);
		setForeground(Color.WHITE);
		g.setFont(new Font("Georgia", Font.ITALIC, 18));
		g.drawString("Enter name:", 140,290);
		g.setFont(new Font("Georgia", Font.ITALIC, 12));
		g.drawString("by 15121295 Ziting You", 435,463);
		
		y-=2;
		y2-=3;
		if(y < -40){
			y = 500;
		}
		if(y2 < -40){
			y2 = 600;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		
		if(e.getSource()==timer){
			this.repaint();
		}
	}
}