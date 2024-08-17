package speiger.reactorplanner.client.template;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.border.BevelBorder;
import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.Box;

public class ReactorWindow {
	
	private JFrame frame;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReactorWindow window = new ReactorWindow();
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
	public ReactorWindow() {
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 666, 509);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel stats_panel = new JPanel();
		stats_panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.add(stats_panel, BorderLayout.SOUTH);
		
		JPanel reactor_panel = new JPanel();
		reactor_panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.add(reactor_panel, BorderLayout.WEST);
		reactor_panel.setLayout(new BorderLayout(0, 0));
		
		JPanel Grid = new JPanel();
		reactor_panel.add(Grid);
		Grid.setLayout(new BoxLayout(Grid, BoxLayout.X_AXIS));
		
		Component horizontalStrut = Box.createHorizontalStrut(324);
		Grid.add(horizontalStrut);
		
		Component verticalStrut = Box.createVerticalStrut(214);
		Grid.add(verticalStrut);
		
		
		JPanel component_panel = new JPanel();
		component_panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.add(component_panel, BorderLayout.EAST);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
	}
}
