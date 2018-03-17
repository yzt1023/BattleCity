package window;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.event.*;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class TSPanel extends JPanel implements ActionListener{
	
	JButton btn_back;
	JTextArea field1,field2;
	Image img_tank,img_tank2;
	int x = 10,y=500;
	int x2 = 550,y2=600;
	Timer timer = new Timer(10,this); //定时器每20ms触发一个ActionEvent
	
	public TSPanel(){
		this.setLayout(null);
		btn_back = new JButton("Back to Main Menu");
		field1 = new JTextArea(10,20);  //文本域大小，10行20列
		field1.setText("Scorer："+getTopScore());
		field2 = new JTextArea("destroy all enemy tanks and protect your base.",10,20);
		//给组件定位
		btn_back.setBounds(235,410,175,30);
		field1.setBounds(100,40,375,150);
		field2.setBounds(100,240,375,150);
		field1.setEditable(false);  //不可编辑
		field2.setEditable(false);
		//将组件添加到面板中
		this.add(btn_back);
		this.add(field1);
		this.add(field2);
		this.setBackground(Color.BLACK);
		//加载坦克图片
		try{
		    img_tank = ImageIO.read(new File("images/mytank.png"));
		    img_tank2 = ImageIO.read(new File("images/enemytank4.png"));
		}catch(Exception e){
			e.printStackTrace();
		}
		timer.start();  //定时器开始计时
	}
	
	private int getTopScore() {
		File file = new File("doc/data.txt");
		int score=0;
		//取数据:
		try {  
	        DataInputStream in=new DataInputStream(new FileInputStream(file));  
	        try {
	        	score=in.readInt(); 
	            in.close();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } 
	    } catch (FileNotFoundException e) {  
	        e.printStackTrace();  
	    } 
		return score;
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(img_tank,x,y,this);
		g.drawImage(img_tank2,x2,y2,this);
		setForeground(Color.WHITE);
		g.setFont(new Font("Georgia", Font.ITALIC, 18));
		g.drawString("Top Scorer:", 250,25);
		g.drawString("How to play:", 250,220);
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