package speiger.reactorplanner.client.template;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
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
		
		JPanel classification = new JPanel();
		classification.setBackground(new Color(198, 198, 198));
		classification.setBorder(new CompoundBorder(new EmptyBorder(5, 0, 5, 0), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
		reactor_panel.add(classification, BorderLayout.NORTH);
		classification.setLayout(new BorderLayout(0, 0));
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setString("No Classification");
		progressBar.setStringPainted(true);
		progressBar.setBackground(new Color(0, 255, 0));
//		progressBar.setPreferredSize(new Dimension(146, 20));
//		progressBar.setMaximumSize(new Dimension(32767, 20));
//		progressBar.setMinimumSize(new Dimension(10, 20));
		classification.add(progressBar);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		reactor_panel.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel reactor_tab = new JPanel();
		reactor_tab.setBackground(new Color(192, 192, 192));
		tabbedPane.addTab("Nuclear Reactor", (Icon) null, reactor_tab, null);
		reactor_tab.setLayout(new BorderLayout(0, 0));
		
		JPanel Grid = new JPanel();
		Grid.setBackground(new Color(198, 198, 198));
		Grid.setBorder(null);
		reactor_tab.add(Grid, BorderLayout.NORTH);
		Grid.setLayout(new BoxLayout(Grid, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("D:\\Projects\\Reactor-Planner\\src\\main\\resources\\assets\\base\\ReactorGrid.png"));
		lblNewLabel.setAlignmentY(0.0f);
		Grid.add(lblNewLabel);
		
		JPanel reactor_controls = new JPanel();
		reactor_controls.setBackground(new Color(192, 192, 192));
		reactor_controls.setBorder(new EmptyBorder(5, 5, 5, 5));
		reactor_tab.add(reactor_controls, BorderLayout.CENTER);
		reactor_controls.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setMaximumSize(new Dimension(32767, 28));
		panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_1.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_1.setBackground(new Color(192, 192, 192));
		reactor_controls.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_1 = new JLabel("Reactor Chambers: ");
		lblNewLabel_1.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_1.add(lblNewLabel_1);
		
		JSlider slider = new JSlider();
		slider.setAlignmentY(Component.TOP_ALIGNMENT);
		slider.setValue(6);
		slider.setMaximum(6);
		slider.setBackground(new Color(192, 192, 192));
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		panel_1.add(slider);
		
		Component horizontalStrut = Box.createHorizontalStrut(4);
		panel_1.add(horizontalStrut);
		
		JLabel lblNewLabel_2 = new JLabel("6");
		lblNewLabel_2.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_1.add(lblNewLabel_2);
		slider.addChangeListener(T -> {
			lblNewLabel_2.setText(""+slider.getValue());
		});
				
		JPanel component_panel = new JPanel();
		component_panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.add(component_panel, BorderLayout.EAST);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
	}
}
