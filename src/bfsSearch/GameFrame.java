package bfsSearch;

import javax.swing.JFrame;


public class GameFrame extends JFrame{
	GameFrame(){
		this.add(new GamePanel());
		this.setTitle("BFS path finding");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
