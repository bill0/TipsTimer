package cn.iam.TipsTimer;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

class MyJDialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7204662927122618399L;
	JLabel jLabel = null;
    public MyJDialog(){
        super();//实例化一个JDialog类对象，指定对话框的父窗体、标题、类型
        Container container=getContentPane();//创建一个容器
        container.add(jLabel = new JLabel("内容"));
        setTitle("提醒");
        setSize(200,100);
        setLocationRelativeTo(null);
    }
}

public class TipsWindow extends JWindow {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1834657841230624383L;
	private static final String iconpath = "img/alarm.ico.png";
	private JLabel jLabel;
	private JLabel timeLabel;
	private MyJDialog jdialog;
	protected int yOld;
	protected int xOld;
	private TrayIcon trayIcon;
	private SystemTray systemTray;

	public TipsWindow() {
		super();

		// label
		jLabel = new javax.swing.JLabel();
		jLabel.setText("休息倒计时：");
		this.add(jLabel, null);

		// time label
		timeLabel = new javax.swing.JLabel();
		timeLabel.setText("45分钟");
		this.add(timeLabel, null);
		
		jdialog = new MyJDialog();
		jdialog.setAlwaysOnTop(true);
		
//		this.setTitle("EYE");
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		
		//处理窗体拖动事件  
	    this.addMouseListener(new MouseAdapter() { 
	        @Override  
	        public void mousePressed(MouseEvent e) {  
	            xOld = e.getX();  
	            yOld = e.getY();  
	        }  
	    });  
	    this.addMouseMotionListener(new MouseMotionAdapter() {  
	        @Override  
	        public void mouseDragged(MouseEvent e) {  
	            int xOnScreen = e.getXOnScreen();  
	            int yOnScreen = e.getYOnScreen();  
	            int xx = xOnScreen - xOld;  
	            int yy = yOnScreen - yOld;  
	            TipsWindow.this.setLocation(xx, yy);  
	        }  
	    });
	    
	    
	    this.setBounds(1024, 256, 100, 50);
		this.getContentPane().setLayout(null);
		this.setAlwaysOnTop(true);
		this.setLayout(new FlowLayout());
		this.initSystemTray();
		

	}
	
	
	public void initSystemTray(){
		 //获取默认的图片  
        if (SystemTray.isSupported()) {// 判断系统是否支持系统托盘  
            if (systemTray==null) {  
                systemTray=SystemTray.getSystemTray();//创建系统托盘  
                if (trayIcon!=null) {  
                    systemTray.remove(trayIcon);  
                }  
            }  
            
            //创建弹出式菜单  
            PopupMenu popup=new PopupMenu();  
            
            //主界面选项  
            MenuItem mainMenuItem=new MenuItem("主界面");  
//            mainMenuItem.setActionCommand("main menu");  
            mainMenuItem.addActionListener(new ActionListener(){  
                @Override  
                public void actionPerformed(ActionEvent e) {  
                    setVisible(true);  
//                  setAlwaysOnTop(true);  
//                  systemTray.remove(trayIcon);  
                }  
            });  
            
            //退出程序选项  
            MenuItem exitMenuItem=new MenuItem("退出");  
//            exitMenuItem.setActionCommand("exit");  
            exitMenuItem.addActionListener(new ActionListener(){  
                @Override  
                public void actionPerformed(ActionEvent e) {  
                	dispose();  
                    System.exit(0);  
                }  
            });  
            popup.add(mainMenuItem);  
           
            popup.addSeparator();  	 //弹出式菜单添加分割线  
            popup.add(exitMenuItem);  
            
            URL path = getClass().getClassLoader().getResource(iconpath);

            Image iconImage = Toolkit.getDefaultToolkit().getImage(path);  

            trayIcon=new TrayIcon(iconImage, this.getClass().getName(), popup);//创建trayIcon  
            trayIcon.setImageAutoSize(true);  
            trayIcon.addActionListener(new ActionListener() {  
                  
                @Override  
                public void actionPerformed(ActionEvent e) {  
                    // TODO Auto-generated method stub  
                    setVisible(true);  
//                  setAlwaysOnTop(true);  
//                  systemTray.remove(trayIcon);  
                }  
            });  
            try {  
                systemTray.add(trayIcon);  
            } catch (AWTException e1) {  
                // TODO Auto-generated catch block  
                e1.printStackTrace();  
            }  
        }  
	}
	
	
	public static void main(String[] args) {
		TipsWindow tipsWindow = new TipsWindow();
		tipsWindow.setVisible(true);

		final long TIMECOUNT = 45*60; // 计时秒数 1000 45*
		long timeCount = TIMECOUNT;
		boolean isWork = true;

		do {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
			tipsWindow.timeLabel.setText(timeCount / 60 + "分" + timeCount % 60 + "秒");

			System.out.println(timeCount);

			
			if (isWork){
				if (timeCount == 60) {
					tipsWindow.jdialog.jLabel.setText("还有一分钟休息！(^_^)");
					tipsWindow.jdialog.setVisible(true);//使MyJDialog窗体可见
				}
				if (timeCount == 0) {
					tipsWindow.jdialog.jLabel.setText("让眼睛休息一下了哦！(^_^)");
					tipsWindow.jdialog.setVisible(true);//使MyJDialog窗体可见
					
					timeCount = 3*60;
					isWork = false;
				}
			} else{
				if (timeCount == 0) {
					tipsWindow.jdialog.jLabel.setText("休息结束哦！(^_^)");
					tipsWindow.jdialog.setVisible(true);//使MyJDialog窗体可见
					
					timeCount = TIMECOUNT;
					isWork = true;
				}
			}
			timeCount -= 1;

		} while (true);

	}
}
