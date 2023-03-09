import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


import java.util.ArrayList;
import java.util.Random;

public class Game extends JFrame {
	JLabel count;
	JLabel time;
	final int SIZE = 20;
	final int BAR_WIDTH = 175;
	final int BAR_HEIGHT = 15;
	final int PANEL_WIDTH = 700;
	final int PANEL_HEIGHT = 500;
	Timer t;
	Timer keyInput;
	int timer = 0;
	int b_count = 10;
	int balllist = 0;
	int sec = 0;
	
	
	
	boolean KeyRight = false; //움직이는거 자연스럽게
	boolean KeyLeft = false;
	
	
	ArrayList<Ball> list = new ArrayList<Ball>();	// 공 객체가 들어가는 리스트	
	ArrayList<Bar> bar_list = new ArrayList<Bar>();	// qk 객체가 들어가는 리스트	
	
	
	public Game() {

		DrawPanel panel = new DrawPanel();			// 공이 그려질 패널
		JPanel textPanel = new JPanel();			// 시간, 개수 들어갈 패널
		
		count = new JLabel("잔여개수 : "+b_count);
		time = new JLabel("경과시간 : "+ timer);
		

		textPanel.add(time);
		textPanel.add(count);
		
		this.add(BorderLayout.CENTER, panel);		// 패널의 중앙배치
		this.add(BorderLayout.NORTH, textPanel);	// 시간, 개수 패널의 상단배치
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(700,500);
		this.setVisible(true);
		
		if(bar_list.isEmpty())
			bar_list.add(new Bar());
		
		t = new Timer(10, new MyClass());
		keyInput = new Timer(10, new keyProcess());
		
		// panel이 눌렸을 때, 작동하는 핸들러 장착
		panel.addMouseListener(panel);
		panel.addKeyListener(panel);
		
	
		
	}
	
	
	
	public static void main(String[] args) {	
		new Game();
	}
	
	
	
	// Timer와 연관된 핸들러
	private class MyClass implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if(sec++%100 == 0) // 1초마다(1000) 공 생성, 10밀리초마다 sec 1 카운트, 100번씩 카운트 시 공 생성, 스피드 100으로 나눌시 나머지가 0이면 100단위
				timer ++;
			if(balllist<9) {
				int x = (int) (Math.random()*400);
				int y = (int) (Math.random()*400);		
				list.add(new Ball(x,y,SIZE,SIZE));
				balllist ++;
			}
			if(bar_list.isEmpty()) {
				bar_list.add(new Bar());		
			}
		
			repaint();// 포함하는 프레임이 다시 그려지게 함.
		}								// 이에 의해 포함된 DrawPanel의 paintComponent 메소드가 실행됨
	}
	//&& bar_list.get(0).pX + BAR_WIDTH < 700
	class keyProcess implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			for(Bar b:bar_list ) {
				if(KeyRight == true) {
					b.moveR();
					System.out.println("야");
				}
				else if (KeyLeft == true && bar_list.get(0).x > 10)
					bar_list.get(0).moveL();
				}
			}
			
	}
	
	@SuppressWarnings("serial")
	private class DrawPanel extends JPanel implements MouseListener, KeyListener{
		public void paintComponent(Graphics g) {
			int w = this.getWidth();				// 현재의 패널 넓이 획득
			int h = this.getHeight();				// 현재의 패널 높이 획득
			g.setColor(Color.white);				
			g.fillRect(0, 0, w, h);					// 노란색으로 칠해 줌

			// 좌표를 조정하고 그림을 그려줌
			for (Ball pi : list) {
				pi.move(pi.moveX, pi.moveY); // 1픽셀 움직임
				if (pi.pX <= 0 || pi.pX >= w-SIZE)
					pi.moveX = pi.moveX * -1;
				if (pi.pY <= 0 || pi.pY >= h-SIZE)
					pi.moveY = pi.moveY * -1;

				pi.draw(g);			// 원을 그림
			}
			

			for(Bar b : bar_list) {
				
				b.draw(g);
			}
			
			time.setText("경과시간 : "+timer);
			count.setText("잔여개수 : "+b_count);
			keyInput.start();
			
			setFocusable(true);		// 중요***				
			requestFocus();			// 패널이 키보드에 반응하도록 포커싱 해 줌
			
		}
		
		
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			int keycode = e.getKeyCode();
			if(keycode == KeyEvent.VK_RIGHT ) {
				System.out.println("오른쪽");
				KeyRight = true;
			}
			else if(keycode ==  KeyEvent.VK_LEFT ) {
				KeyLeft = true;
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int keycode = e.getKeyCode();
			if(keycode == KeyEvent.VK_RIGHT)
				KeyRight = false;
			else if(keycode == KeyEvent.VK_LEFT) {
				KeyLeft = false;
			}
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			t.start();
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
	
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		
		
	}
	private class Ball {
		int pX;				// 그림의 X좌표
		int pY;				// 그림의 y좌표
		int width;			// 그림의 넓이
		int height;			// 그림의 높이
		int moveX=1, moveY=1; // 그림의 이동방향 및 보폭
		Color color = Color.green;		// 현재 공의 색. 처음에는 Green으로 시작

		// 생성자
		public Ball(int x, int y, int width, int height) {
			pX=x;
			pY=y;
			this.width = width;
			this.height = height;
		}
		public void setColor(Color color) {
			this.color = color;
		}

		public Color getColor() {
			return color;
		}

		// 그림의 이동
		public void move(int x, int y) {
			pX += x;
			pY += y;
		}
		// 그림대신 초록색 원 그리기
		public void draw (Graphics g) {
			g.setColor(color);
			g.fillOval(pX, pY, width, height);
		}
		// 어떤 주어진 좌표 p 와 그림의 중점과의 거리 (그림의 좌표는 왼쪽 모서리이므로... 폯과 높이를 감안하여 중점을 사용)
		public double distance (Point p) {
			return Math.sqrt(Math.pow((pX+width/2)-p.x, 2)+ Math.pow((pY+height/2)-p.y, 2));
		}
	}
	
	private class Bar extends JPanel{
		int x;
		int pY;
		int width;
		int height;
		Color color = Color.red;
		
		public Bar() {
			x = 268;
			pY = 600;
			width = BAR_WIDTH;
			height = BAR_HEIGHT;
		}
		public void moveR() {
			x += 10;
		}
		public void moveL() {
			x -= 10;
		}
		public int getX() {
			return x;
		}
		
		public void draw (Graphics g) {
			g.setColor(color);
			g.fillRect(263, 400, width, height);
		}
		// 어떤 주어진 좌표 p 와 그림의 중점과의 거리 (그림의 좌표는 왼쪽 모서리이므로... 폯과 높이를 감안하여 중점을 사용)
		public double distance (Point p) {
			return Math.sqrt(Math.pow((x+width/2)-p.x, 2)+ Math.pow((pY+height/2)-p.y, 2));
		}
	}

}
