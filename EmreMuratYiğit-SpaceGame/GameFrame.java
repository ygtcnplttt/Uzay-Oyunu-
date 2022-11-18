import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;

public class GameFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public GameFrame(String title) {
		super(title);
	}
	

	public static void main(String[] args) throws IOException {
		
		GameFrame frame = new GameFrame("Goralı");
		frame.setResizable(false);
		frame.setFocusable(false);
		
//		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Game game = new Game();
		
		game.requestFocus();
		game.setPreferredSize(new Dimension(800,600));
		
		game.addKeyListener(game);
		game.addMouseListener(game);
		
		game.setFocusable(true);
		game.setFocusTraversalKeysEnabled(false);
		
		
		frame.add(game);
		frame.setLocationRelativeTo(null);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(screenSize.width/2-400,screenSize.height/2-300);
		
		frame.setVisible(true);
		frame.pack();
		
		
	}

}
