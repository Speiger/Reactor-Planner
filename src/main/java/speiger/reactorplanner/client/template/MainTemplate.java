package speiger.reactorplanner.client.template;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class MainTemplate {
	
	private JFrame frame;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainTemplate window = new MainTemplate();
					window.frame.setVisible(true);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public MainTemplate() {
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(new Color(198, 198, 198));
		frame.setBounds(100, 100, 666, 509);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(198, 198, 198));
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel stats_panel = new JPanel();
		stats_panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.add(stats_panel, BorderLayout.SOUTH);
		
		JPanel reactor_panel = new JPanel();
		reactor_panel.setBackground(new Color(198, 198, 198));
		reactor_panel.setBorder(new EmptyBorder(0, 10, 0, 0));
		panel.add(reactor_panel, BorderLayout.WEST);
		reactor_panel.setLayout(new BorderLayout(0, 0));
		
		JPanel reactor_layout = new JPanel();
		reactor_layout.setBackground(new Color(198, 198, 198));
		reactor_layout.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new EmptyBorder(5, 5, 5, 5)));
		reactor_panel.add(reactor_layout, BorderLayout.CENTER);
		reactor_layout.setLayout(new BorderLayout(0, 0));
		
		JPanel Grid = new JPanel();
		Grid.setBackground(new Color(198, 198, 198));
		Grid.setBorder(null);
		reactor_layout.add(Grid, BorderLayout.CENTER);
		Grid.setLayout(new BoxLayout(Grid, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("D:\\Projects\\Reactor-Planner\\src\\main\\resources\\assets\\base\\ReactorGrid.png"));
		lblNewLabel.setAlignmentY(0.0f);
		Grid.add(lblNewLabel);
		
		JPanel classification = new JPanel();
		classification.setBackground(new Color(198, 198, 198));
		classification.setBorder(new CompoundBorder(new EmptyBorder(5, 0, 5, 0), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
		reactor_panel.add(classification, BorderLayout.NORTH);
		classification.setLayout(new BorderLayout(0, 0));
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setString("No Classification");
		progressBar.setStringPainted(true);
		progressBar.setBackground(new Color(0, 255, 0));
		progressBar.setPreferredSize(new Dimension(146, 20));
		progressBar.setMaximumSize(new Dimension(32767, 20));
		progressBar.setMinimumSize(new Dimension(10, 20));
		classification.add(progressBar);
		
		
		JPanel component_panel = new JPanel();
		component_panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.add(component_panel, BorderLayout.EAST);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
	}
}
