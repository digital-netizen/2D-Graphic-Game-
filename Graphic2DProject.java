import java.util.*;
import java.util.Random;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.awt.color.*;
import java.awt.Graphics2D;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Graphic2DProject{
	//declare a color variable
	private Color colors[];
	
	//build a constructor to initialize
	public Graphic2DProject() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}
				catch(UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException exc) {
					exc.getMessage();
				}
		
		//set the title of a window frame
		JFrame jfrm = new JFrame("Graphic Project");
		
		//set the specific size of a window frame
		//jfrm.setSize(400, 400);
		
		//set the specific layout of a window frame
		jfrm.setLayout(new GridLayout());
		
		//terminate the program if a user closes
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//make an array of choices for colors 
		colors = new Color[] {Color.RED, Color.BLUE, Color.BLACK, Color.YELLOW, Color.ORANGE, Color.PINK, Color.GREEN};
		
		//make a new class and add to the content pane
		jfrm.add(new MyCanvas());
		
		//set location relative to the frame
		jfrm.setLocationRelativeTo(null);
		
		//set pack
		jfrm.pack();
		
		//set visible
		jfrm.setVisible(true);
		}
	 });
  }
	
	//build an interface that contains abstract methods to task easier
	public interface DrawGraphics{
		
		//declare methods
		//this method is a method that controls animation objects within a frame
		//In other words, it upgrades whenever an object shifts
		public void upgrade(JComponent comp);
		
		//this method is a method that controls the shape and size of an object
		public void draw(Graphics g); 
	}
	
	//build a class for Calculate
	public class Calculate implements DrawGraphics{
		//declare variables 
		//these instances are the positions of each coordinate on a graph
		private int x_axis;
		private int y_axis;
		private int width = 10; //this variable is the width of an animation
		private int height = 10; //this variable is the height of an animation
		private Color colors;
		
		//these variables are the instances that will hold a random position that animations will begin
		private int x_point;
		private int y_point;
		
		
		//build a parameterized constructor
		public Calculate(int x_axis, int y_axis, Color colors) {
			this.x_axis = x_axis;
			this.y_axis =y_axis;
			this.colors = colors;
			
			//make animations commence in any position on a graph
			x_point = random();
			y_point = random();
		}
		

		//build a method for upgrade
		public void upgrade(JComponent comp) {
			x_axis += x_point;
			y_axis += y_point;
			
			//make conditions
			if(x_axis < 0) {
				x_axis = 0;
				x_point *= -1; //avoid negative numbers
			}
			else if(x_axis + width > comp.getWidth()) {
				x_axis = comp.getWidth() - width;
				x_point *= -1;
			}
			if(y_axis < 0) {
				y_axis = 0;
				y_point *= -1; //avoid negative numbers
			}
			else if(y_axis + height > comp.getHeight()) {
				y_axis = comp.getHeight() - height;
				y_point *= -1;
			}
		}
		
		//build a class for random
		//this method manipulates the distance & positions of each animation
		protected int random() {
			//declare a variable
			int value = 0;
			
			do {
				value = -2 * (int)(Math.random() * 4);
			}while(value == 0);
			
			return value;
		}
	
		
		//build a method for draw
		public void draw(Graphics g) {
			//set the shape of an object
			g.fillOval(x_axis, y_axis, width, height);
			//set colors
			g.setColor(this.colors);
		}	
	}
	
		//build a class for random colors for maximum and minimum
		public int randInt(int min, int max) {
			//declare a Random variable to generate random colors
			Random r = new Random();
			int random = r.nextInt((max - min) + 1) + min;
			
			return random;
		}
		
		//build a class named Calculate
		//this method is to calculate the number of quantity of each animation
		//and control the velocity of each object due to the timer
		public class MyCanvas extends JPanel{
			//declare an array list variable to implement an interface
			private ArrayList<DrawGraphics> drawCircles;
			
			//build a constructor
			public MyCanvas() {
				//declare a spinner to display the quantity
				JSpinner numberSpinnerQty;
				SpinnerNumberModel numberSpinnerModelQty;
				Integer currentQty = new Integer(1);
				Integer minQty = new Integer(1);
				Integer maxQty = new Integer(20);
				Integer stepQty = new Integer(1);
				
				numberSpinnerModelQty = new SpinnerNumberModel(currentQty, minQty, maxQty, stepQty);
				numberSpinnerQty = new JSpinner(numberSpinnerModelQty);
				
				//now, add changeListener for quantity spinners
				ChangeListener listenerQty = new ChangeListener() {
						public void stateChanged(ChangeEvent ce) {
							drawCircles = new ArrayList<>((int)numberSpinnerQty.getValue());
						
							for(int n = 0; n < (int)numberSpinnerQty.getValue(); n++) {
								int x = (int)(Math.random() * 590);
								int y = (int)(Math.random() * 590);
								Color c = colors[randInt(0, colors.length - 1)];
								drawCircles.add(new Calculate(x,y,c));
							}
						}
					};
					
					//add changeListener to the spinnerQty to conclude
					numberSpinnerQty.addChangeListener(listenerQty);
					add(numberSpinnerQty);
					
					//now, I increment the number of animations one by one
					drawCircles = new ArrayList<>(1);
					for(int n = 0; n < 1; n++) {
						int x = (int)(Math.random() * 590);
						int y = (int)(Math.random() * 590);
						Color c = colors[randInt(0, colors.length - 1)];
						drawCircles.add(new Calculate(x,y,c));
					}
					
					//make a timer for animations
					Timer timer = new Timer(5, new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							for(DrawGraphics DG: drawCircles) {
								DG.upgrade(MyCanvas.this);
							}
							//repaint animations
							repaint();
						}
					});
					timer.start();
				}
			
			//make a method for dimension
			public Dimension getPreferredSize() {
				return new Dimension(600,600);
			}
			
			//build a method for paintComponent to display graphics on a screen
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				for(DrawGraphics DG: drawCircles) {
					DG.draw(g);
				}
			}
		}
	
	public static void main(String args[]) {
		new Graphic2DProject();
	}
}
