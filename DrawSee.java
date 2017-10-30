package project2;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JButton;

public class DrawSee extends JFrame implements Runnable {
	//private static final long serialVersionUID = 2L;
    private static final int sx = 50;//游戏区域10*10方块的起始横坐标
    private static final int sy = 50;//游戏区域10*10方块的起始纵坐标
    private static final int w = 10;//每个小方格的边长
    private static final int rw = 400;//游戏区域10*10方块的边长
    private static final int size = 40;
    private Graphics jg;  
    final static boolean  cell = true;
	final static boolean  blank = false;
	static boolean world[][] = new boolean[size][size];
	static boolean newworld[][] = new boolean[size][size];

	private Thread animator;
    private int delay;//延迟 
    private boolean running;//flag
    

    public DrawSee() { 
    	setBounds(100, 100, 500, 500);
         setVisible(true);

         getContentPane().setLayout(null);  
         animator = new Thread(this);
	     delay = 100;
         running = false;
         
         JButton btnNewButton = new JButton("\u5F00\u59CB/\u6682\u505C");
         btnNewButton.addMouseListener(new MouseAdapter() {
         	@Override
         	public void mouseClicked(MouseEvent e) {
         		running = !running;
         	}
         });
         btnNewButton.setBounds(200, 438, 93, 23);
         getContentPane().add(btnNewButton);
         setResizable(false);
         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
         try {     
             Thread.sleep(500);
         } catch (Exception e) {
             e.printStackTrace();
         }        
         // 获取专门用于在窗口界面上绘图的对象
         jg =  this.getGraphics();       
         // 绘制游戏区域
         paintComponents(jg);
         this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // 如果游戏结束，返回，不执行后面的代码
                 //获取鼠标点击的那一点的x，y坐标
            	if(running == false)
            	{
            		int x = e.getX(), y = e.getY();
            		int cx = (x - sx) / w, cy = (y - sy) / w;                
            		if(cx < 1 || cy < 1 || cx > size || cy > size) {
            			return;
            		}
            		else
            		{
            			if(world[cx][cy] != cell)
            			{
            				setColor(cx,cy,Color.black);
            				world[cx][cy] = cell;
            			}
            			else
            			{
            				setColor(cx,cy,Color.white);
            				world[cx][cy] = blank;
            			}
            		}
            	}        
             }
         });
               
         animator.start();
    }
    public void run() {
    	//long tm = System.currentTimeMillis();
        while (Thread.currentThread() == animator) {
            if (running == true) {
            	evolute();
                paintWorld(world);
            } 
            try {
               // tm += delay;//Math.max(0, tm - System.currentTimeMillis())
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                break;
            }
        }
        
    }
    public void paintWorld(boolean world[][]){
    	setWhite();
    	for(int i = 0;i<size;i++)
    		for(int j = 0;j<size;j++)
    		{
    			if(world[i][j] == cell)
    				setColor(i,j,Color.black);
    		}
    }
    public void paintComponents(Graphics g) {
         try { 
             // 设置线条颜色为红色
             g.setColor(Color.RED);             
            // 绘制外层矩形框
             g.drawRect(sx, sy, rw, rw);
             for(int i = 1; i < size; i ++) {
                 g.drawLine(sx + (i * w), sy, sx + (i * w), sy + rw);                
                 g.drawLine(sx, sy + (i * w), sx + rw, sy + (i * w));     
             }
             setWhite();
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
    private void setColor(int cx,int cy,Color color){
        jg.setColor(color);
        jg.fillRect(sx + (cx * w) + 1, sy + (cy * w) + 1, w-1 , w-1 );
     }
    
    private void setWhite(){
        jg.setColor(Color.white);
       for(int i = 0;i<size;i++)
    	   for(int j = 0;j<size;j++)
    		   jg.fillRect(sx + (i * w) + 1, sy + (j * w) + 1, w-1, w-1 );
     }
    public static int cellState(int a, int b){
		if(a<0||a>=size||b<0||b>=size||world[a][b] == blank)
			return 0;
		else return 1;
		
	}
    public static int getNeighbors(int y,int x){               
		int n = 0; 
        n += cellState( y - 1, x - 1);
        n += cellState( y - 1, x);
        n += cellState( y - 1, x + 1);
        n += cellState( y, x - 1);
        n += cellState( y, x + 1);
        n += cellState( y + 1, x - 1);
        n += cellState( y + 1, x);
        n += cellState( y + 1, x + 1);
        return n;
	}
	public void evolute()
	{
		for(int i = 0;i<size;i++)
		{
			for(int j = 0;j<size;j++)
			{

				if(getNeighbors(i,j) == 3)
					newworld[i][j] = cell;
				else if(getNeighbors(i,j) == 2)
					newworld[i][j] =world[i][j];
				else
					newworld[i][j] = blank;
			}
		}
		for(int i = 0;i<size;i++)
			for(int j =0;j<size;j++)
				world[i][j] = newworld[i][j];
	}
   
    public static void main(String[] args){   
         new DrawSee();
     }
	public void before()
	{
		init();	
		world[4][5] = cell;
		world[4][6] = cell;
		world[4][7] = cell;
		animator = Thread.currentThread();
		running = true;
	}
	public void init()
	{
		for(int i = 0;i<size;i++)
			for(int j =0;j<size;j++)
			{
				world[i][j] = blank;
				newworld[i][j] = blank;
			}
	}

}
