package window;
import java.awt.*;
import javax.swing.*;

import java.awt.event.*;

//主窗口 选择进入游戏 使用卡片布局
@SuppressWarnings("serial")
public class WindowFrame extends JFrame implements ActionListener{
	
	JPanel cardPanel;
	CardLayout cardLayout;
	MainPanel panel1;
	TSPanel panel2;
	NGPanel panel3;
	Image background;
	
	public WindowFrame(){
		super("Battle City");             //窗口名，即游戏名
		this.setSize(600, 500);           //设置窗口大小
		this.setLocationRelativeTo(null); //窗口位于屏幕的中央
		cardPanel = new JPanel();         //定义卡片布局的容器
		cardLayout = new CardLayout();    //定义卡片对象
		cardPanel.setLayout(cardLayout);  //设置使用卡片布局的容器为卡片布局
		panel1 = new MainPanel();         //主面板
		panel2 = new TSPanel();           //最高分面板
		panel3 = new NGPanel();           //新游戏界面（输入玩家姓名）
		//将面板添加到卡片布局的容器当中
		cardPanel.add(panel1, "1");
		cardPanel.add(panel2, "2");
		cardPanel.add(panel3, "3");
		getContentPane().add(cardPanel);  //当天窗口中添加卡片布局的容器
		//为按钮对象注册监听器
		panel1.btn_newGame.addActionListener(this);
		panel1.btn_topScorers.addActionListener(this);
		panel2.btn_back.addActionListener(this);
		panel3.btn_gameStart.addActionListener(this);
		panel3.btn_back.addActionListener(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
	}
	
	//实现按钮的监听触发时的处理，让容器显示相应的组件
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == panel1.btn_newGame){
			cardLayout.show(cardPanel, "3");
		}
		if(e.getSource() == panel1.btn_topScorers){
			cardLayout.show(cardPanel, "2");
		}
		if(e.getSource() == panel2.btn_back){
			cardLayout.show(cardPanel, "1");
		}
		if(e.getSource() == panel3.btn_back){
			cardLayout.show(cardPanel, "1");
		}
		if(e.getSource() == panel3.btn_gameStart){
			String name = panel3.name.getText();
			this.setVisible(false);
			new GameFrame(name);
		}
	}
	
	// 窗口关闭时退出程序
    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }
}