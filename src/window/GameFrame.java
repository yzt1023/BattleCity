package window;
import java.awt.AWTEvent;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameFrame extends JFrame{
	
	GamePanel gamePanel;
	String name;
	
    public GameFrame(String name) {
		this.name = name;
		gamePanel = new GamePanel(name);
        this.add(gamePanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setTitle("Battle City");
        setResizable(false);
        setVisible(true);
        this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
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