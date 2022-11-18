import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;


class spaceship{
	private int x,y;
	
	public spaceship(int x,int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
class alien{
	private int x,y;
	private boolean barrier;
	
	public alien(int x,int y, boolean barrier) {
		this.x = x;
		this.y = y;
		this.barrier = barrier;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public boolean getBarrier() {
		return barrier;
	}

	public void setBarrier(boolean barrier) {
		this.barrier = barrier;
	}
}
class Ates{
	private int x,y;
	private int angle;
	
	public Ates(int x,int y,int angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}
}
class powerup{
	private int x,y,time;
	private boolean type;
	
	public powerup(int x,int y,boolean type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	public boolean getType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}
	
}



public class Game extends JPanel implements KeyListener,ActionListener,MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Timer timer = new Timer(5,this);
	spaceship ship = new spaceship(10,10);
	private BufferedImage spaceship;
	private BufferedImage alienpng;
	private BufferedImage background;
	private BufferedImage barrier;
	
	static String nickname;
	
	JPanel info;
	JLabel Angle;
	JLabel AngleText;
	JLabel ScoreText;
	JLabel Score;
	JLabel TimeText;
	JLabel Time;
	JLabel Level;
	JLabel LevelText;
	JLabel PowerUpType;
	JLabel PowerUp;
	
	JPanel difficultyPanel;
	JRadioButton easy;
	JRadioButton medium;
	JRadioButton hard;
	ButtonGroup diffgroup;
	
	int speedboost = 2;
	int alienSpeed;
	double time = 0;
	double temptime = 0;
	boolean rotation = false;
	int angle = 0;
	int score = 0;
	int level = 1;

	private ArrayList<alien> aliens  = new ArrayList<alien>();
	private ArrayList<Ates> atesler  = new ArrayList<Ates>();
	private ArrayList<powerup> powerups = new ArrayList<powerup>();
	
	public void hit() {
		for (Ates ates:atesler) {
			for(alien alien:aliens) {
				if(new Rectangle(ates.getX(),ates.getY(),20,10).intersects(new Rectangle(alien.getX(), alien.getY(), 40, 40))) {
					if(alien.getBarrier()==true) {
						alien.setBarrier(false);
						aliens.add(alien);
					}else {
						aliens.remove(alien);
						atesler.remove(ates);
						score+=2;
						Random randLocation = new Random();
						Random randSpawn = new Random();
						int spawn= randSpawn.nextInt(3);
						if(spawn==1){
							Random effect=new Random();
							powerups.add(new powerup(randLocation.nextInt(500)+250,randLocation.nextInt(500),effect.nextBoolean()));
						}
					}
				}
			}
		}
	}
	
	
	private void display() {

		JTextArea instruction = new JTextArea("Arrow Down: Down \nArrow Up: Up \nLeft Arrow: Increase Angle\nRight Arrow: Decrease Angle \nFire: Z \nClick to collect powerup");
		instruction.setEditable(false);
		
		difficultyPanel = new JPanel(new FlowLayout());
		easy = new JRadioButton("Easy");
		medium = new JRadioButton("Medium");
		hard = new JRadioButton("Hard");
		difficultyPanel.add(easy);
		difficultyPanel.add(medium);
		difficultyPanel.add(hard);
		
		diffgroup = new ButtonGroup();
		diffgroup.add(easy);
		diffgroup.add(medium);
		diffgroup.add(hard);
		
        JTextField field1 = new JTextField("");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nickname"));
        panel.add(field1);
        panel.add(difficultyPanel);
        
        Object[] options = {"Start","Cancel"};
        
        int login = JOptionPane.showOptionDialog(null, panel, "Login", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]); 
        
        if (login == JOptionPane.YES_OPTION) {
            
        	nickname = field1.getText();
        	
        	if(easy.isSelected()) {
        		alienSpeed=0;
        	}else if(medium.isSelected()) {
        		alienSpeed=4;
        	}else if(hard.isSelected()) {
        		alienSpeed=8;
        	}else {
        		alienSpeed=8;
        	}
        	
        	JOptionPane.showMessageDialog(null, instruction, "HOW TO PLAY",JOptionPane.PLAIN_MESSAGE);
        } else {
            System.out.println("Cancelled");
            System.exit(0);
        }
    }
	public void start() {
		timer.start();
		
		powerups.removeAll(powerups);
		atesler.removeAll(atesler);
		
	    score=0;
	    Score.setText(Integer.toString(score));
	    level=1;
	    Level.setText(Integer.toString(level));
	    time=0;
	    Time.setText(Double.toString(time));
		
		aliens.add(new alien(700,10,false));
		aliens.add(new alien(700,250,true));
		aliens.add(new alien(700,500,false));
	}
	
	public Game() throws IOException {
		timer.start();
		display();
		
		try {
			spaceship  = ImageIO.read(new FileImageInputStream(new File("spaceship2.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			alienpng  = ImageIO.read(new FileImageInputStream(new File("alien.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			background  = ImageIO.read(new FileImageInputStream(new File("gora.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			barrier  = ImageIO.read(new FileImageInputStream(new File("barrier2.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setLayout(new BorderLayout());
		AngleText = new JLabel("Angle:");
		Angle = new JLabel("0");
		
		ScoreText = new JLabel("Score:");
		Score = new JLabel(Integer.toString(score));
		
		TimeText = new JLabel("Time");
		Time = new JLabel("0");
		
		LevelText = new JLabel("Level");
		Level = new JLabel("1");
		
		PowerUpType = new JLabel("Powerup:");
		PowerUp = new JLabel("");
		
		info = new JPanel(new GridLayout());
		add(info,BorderLayout.SOUTH);
		
		info.add(AngleText);
		info.add(Angle);
		info.add(ScoreText);
		info.add(Score);
		info.add(TimeText);
		info.add(Time);
		info.add(LevelText);
		info.add(Level);
		info.add(PowerUpType);
		info.add(PowerUp);
		
		aliens.add(new alien(700,10,false));
		aliens.add(new alien(700,250,true));
		aliens.add(new alien(700,500,false));
		
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage(background, 0, 0,800,580,null);
		time += 5;
		String angleString=Integer.toString(angle);
		Angle.setText(angleString);
		
		Level.setText(Integer.toString(level));
		
		String scoreString=Integer.toString(score);
		Score.setText(scoreString);
		
		String timeString=Double.toString(time/1000.0);
		if(Double.parseDouble(timeString)>10.0) { 
			timeString = timeString.substring(0, 4);
		}else {
			timeString = timeString.substring(0, 3);
		}
		Time.setText(timeString);
		
		
		if(aliens.size()==0) {
			
			if(level == 1) {
				level++;
				score+=10;
				aliens.add(new alien(700,10,true));
				aliens.add(new alien(700,250,true));
				aliens.add(new alien(700,500,true));
				aliens.add(new alien(600,10,false));
				aliens.add(new alien(600,250,false));
				aliens.add(new alien(600,500,false));
			}else if(level == 2) {
				level++;
				score+=10;
				aliens.add(new alien(700,10,true));
				aliens.add(new alien(700,250,true));
				aliens.add(new alien(700,500,true));
				aliens.add(new alien(600,10,true));
				aliens.add(new alien(600,250,true));
				aliens.add(new alien(600,500,true));
				aliens.add(new alien(500,10,false));
				aliens.add(new alien(500,250,false));
				aliens.add(new alien(500,500,false));
			}else if(level == 3) {
				score+=10;
				timer.stop();
			    try {
			        FileWriter myWriter = new FileWriter("scores.txt",true);
			        myWriter.write(nickname + " " +" Score: "+ score + " Time: "+time/1000.0 + "\n");
			        myWriter.close();
			        System.out.println("Successfully wrote to the file.");
			      } catch (IOException e) {
			        System.out.println("An error occurred.");
			        e.printStackTrace();
			      }
			    
			    ArrayList<String> allScores = new ArrayList<String>();
			    
			    try {
			        File myObj = new File("scores.txt");
			        Scanner myReader = new Scanner(myObj);
			        while (myReader.hasNextLine()) {
			          String data = myReader.nextLine();
			          
			          allScores.add(data);
			          
			          System.out.println(data);
			        }
			        myReader.close();
			      } catch (FileNotFoundException e) {
			        System.out.println("An error occurred.");
			        e.printStackTrace();
			      }
			    
			    String[] scoreArray = new String[allScores.size()];
			    
			    scoreArray = allScores.toArray(scoreArray);
			    
			    JList<String> scoreList = new JList<String>(scoreArray);
			    
//			    JOptionPane.showMessageDialog(this, scoreList, "SCOREBOARD",JOptionPane.PLAIN_MESSAGE);
			    
			    Object[] options = {"Play Again","Close"};
		        
		        int scoreboard = JOptionPane.showOptionDialog(null, scoreList, "SCOREBOARD", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]); 
		        
		        if (scoreboard == JOptionPane.YES_OPTION) {
		            
		        	start();
		        	
		        } else {
		            System.exit(0);
		        }
		        
			}
			
		}
		
		g2.drawImage(spaceship,ship.getX(),ship.getY(),spaceship.getWidth()*2,spaceship.getHeight()*2,this);
		
		
		g2.setColor(Color.green);
		for(alien alien:aliens) {
			g2.drawImage(alienpng,alien.getX(),alien.getY(),alienpng.getWidth(),alienpng.getHeight(),this);
			if(alien.getBarrier()==true) {
				g2.drawImage(barrier,alien.getX()-6,alien.getY(),barrier.getWidth(),barrier.getHeight(),this);
			}
			
		}
		
		
		for(Ates ates:atesler) {
			if(ates.getX()>800) {
				atesler.remove(ates);
			}
		}
		
		g2.setColor(Color.red);
		for(Ates ates:atesler) {
			g2.fillRect(ates.getX(),ates.getY() ,20 ,10 );
		}
		
		hit();
		
		for(powerup power:powerups) {
			if(power.getType()) {
				g2.setColor(Color.green);
				g2.fillRect(power.getX(),power.getY(),20 ,20 );
				power.setTime(power.getTime()+5);
				if(power.getTime() == 3000) {
					powerups.remove(power);
				}
			}else {
				g2.setColor(Color.red);
				g2.fillRect(power.getX(),power.getY(),20 ,20 );
				power.setTime(power.getTime()+5);
				if(power.getTime() == 3000) {
					powerups.remove(power);
				}
			}
		}
	}
	
	@Override
	public void repaint() {
		super.repaint();
	}
	
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		int c = e.getKeyCode();
		if(c == KeyEvent.VK_UP) {
			System.out.println(ship.getX()+" "+ship.getY());
			if(ship.getY()<=0) {
				ship.setY(0);
			}else {
				ship.setY(ship.getY()-10);
			}
			
		}else if(c == KeyEvent.VK_DOWN) {
			System.out.println(ship.getX()+" "+ship.getY());
			if(ship.getY()>=560) {
				ship.setY(560);
			}else {
				ship.setY(ship.getY()+10);
			}
		}else if(c == KeyEvent.VK_Z) {
			atesler.add(new Ates(ship.getX()+80,ship.getY()+35,angle));
			score--;
		}
		else if(c == KeyEvent.VK_RIGHT) {
			angle++;
		}else if(c == KeyEvent.VK_LEFT) {
			angle--;
		}
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Random rand = new Random();
		
		for(Ates ates:atesler) {
			ates.setX(ates.getX()+speedboost);
			ates.setY(ates.getY()+ates.getAngle());
		}
		
		if(time%300==0) {
			if(rotation == false) {
				for(alien alien:aliens) {
					if(alien.getY()>=500) {
						alien.setY(500);
					}else {
						alien.setY(alien.getY()+rand.nextInt(50));
						alien.setX(alien.getX()-alienSpeed);
						rotation = true;
					}
				}
			}else {
				for(alien alien:aliens) {
					if(alien.getY()<=0) {
						alien.setY(40);
					}else {
						alien.setY(alien.getY()-rand.nextInt(50));
						alien.setX(alien.getX()-alienSpeed);
						rotation = false;
					}
				}
			}
		}
		
		for(alien alien:aliens) {
			if(alien.getX()<=150) {
				JLabel gameover = new JLabel("GAME OVER",SwingConstants.CENTER);
				JOptionPane.showMessageDialog(this, gameover, null,JOptionPane.PLAIN_MESSAGE);
				
				System.exit(0);
			}
		}
		if(score==-6) {
			JLabel gameover = new JLabel("GAME OVER",SwingConstants.CENTER);
			JOptionPane.showMessageDialog(this, gameover, null,JOptionPane.PLAIN_MESSAGE);
			
			System.exit(0);
		}
		
		repaint();

	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		
	        int c = e.getButton();
	        if(c==MouseEvent.BUTTON1) {
	        	for(powerup power:powerups) {
	    			if(new Rectangle(power.getX(),power.getY(),20,20).intersects(new Rectangle(e.getX(), e.getY(), 1, 1))) {

	    				Random cases = new Random();
	    				if(power.getType()) {
	    					switch(cases.nextInt(3)) {
	    					case 0:
	    						speedboost++;
	    						PowerUp.setText("Increase");
	    						break;
	    					case 1:
	    						score++;
		    					PowerUp.setText("Score +1");
		    					break;
	    					case 2:
	    						alienSpeed--;
	    						PowerUp.setText("Slower");
	    						break;
	    					}
	    				}else if(!power.getType()) {
	    					switch(cases.nextInt(3)) {
	    					case 0:
	    						speedboost--;
	    						PowerUp.setText("Decrease");
	    						break;
	    					case 1:
	    						score--;
		    					PowerUp.setText("Score -1");
		    					break;
	    					case 2:
	    						alienSpeed+=4;
	    						PowerUp.setText("Faster");
	    						break;
	    					}
	    				}
	    				
	    				powerups.remove(power);
	    				
	    			}
	    		}
	        }
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {		
	}
}