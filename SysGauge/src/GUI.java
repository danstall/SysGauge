import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI extends JFrame{
	
	private PrintWriter output;
	private JLabel targVal = new JLabel("Waiting...");
	public static JSlider slider = new JSlider(0,100,Main.debugVal);;
	public static JButton OVR = new JButton("Enable Override");
	public static JButton light = new JButton("OFF");
	
	private class CPUListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (Main.dispVal!=0) {
				Main.dispVal=0;
				output.println("DSP%0!");
				System.out.println("CPU Load Displayed");
				try {TimeUnit.MILLISECONDS.sleep(600);} catch (InterruptedException a) {}
			}
		}
	}
	
	private class MEMListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (Main.dispVal!=1) {
				Main.dispVal=1;
				output.println("DSP%1!");
				System.out.println("MEM Load Displayed");
				try {TimeUnit.MILLISECONDS.sleep(600);} catch (InterruptedException a) {}
			}
			
		}
	}
	
	private class ZERListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			output.println("ZER%0!");
			System.out.println("Gauge Zeroed");
			try {TimeUnit.MILLISECONDS.sleep(600);} catch (InterruptedException a) {}
		}
	}
	
	private class OVRL implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (Main.ovr) {
				Main.ovr=false;
				GUI.OVR.setText("Enable Overrride");
				try {TimeUnit.MILLISECONDS.sleep(600);} catch (InterruptedException a) {}
			} else {
				Main.ovr=true;
				GUI.OVR.setText("Disable Overrride");
				try {TimeUnit.MILLISECONDS.sleep(600);} catch (InterruptedException a) {}
			}
		}
	}
	
	private class DBGListener implements ActionListener {
		JFrame f;
		JPanel p1;
		JPanel p2;
		GridBagConstraints c;
		public void assignFrame(JFrame jf, JPanel jp1, JPanel jp2, GridBagConstraints gbc) {
			f=jf;
			p1=jp1;
			p2=jp2;
			c=gbc;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if (Main.debug) {
				System.out.println("Debug Disabled");
				Main.debug=false;
				f.setVisible(false);
				if (Main.ovr) {
					Main.ovr=false;
					GUI.OVR.setText("Enable Override");
				}
				p1.remove(p2);
				f.pack();
				f.setVisible(true);
			} else {
				System.out.println("Debug Enabled");
				Main.debug=true;
				f.setVisible(false);
				p1.add(p2,c);
				f.pack();
				f.setVisible(true);
			}
		}
	}
	
	public void targValUpdate(int dv, int v) {
		if (dv==0) {
			this.targVal.setText("CPU Load: " + String.valueOf(v) + "%");
		} else {
			this.targVal.setText("MEM Load: " + String.valueOf(v) + "%");
		}
	}
	
	private class LITListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (Main.light==0) {
				Main.light=1;
				output.println("LIT%1!");
				GUI.light.setText("ON");
				System.out.println("Backlight Enabled");
				try {TimeUnit.MILLISECONDS.sleep(600);} catch (InterruptedException a) {}
			} else {
				Main.light=0;
				output.println("LIT%0!");
				GUI.light.setText("OFF");
				System.out.println("Backlight Disabled");
				try {TimeUnit.MILLISECONDS.sleep(600);} catch (InterruptedException a) {}
			}
			
		}
	}
	
	private class DBSL implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			Main.debugVal=GUI.slider.getValue();
		}
	}
	
	public GUI (PrintWriter p) {
		this.output=p;
	}
	
	public void initGUI() {
		Border blackline = BorderFactory.createLineBorder(Color.black);
		CPUListener CPUL = new CPUListener();
		MEMListener MEML = new MEMListener();
		LITListener LITL = new LITListener();
		DBGListener DBGL = new DBGListener();
		ZERListener ZERL = new ZERListener();
		
		JFrame gui = new JFrame("Gauge Interface");
		if (SystemTray.isSupported()) {
			gui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		}
		SystemTray systemTray = SystemTray.getSystemTray();
		TrayIcon icon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("gaugeIcon.png")));
		PopupMenu popMenu = new PopupMenu();
		MenuItem open = new MenuItem("Open");
		MenuItem close = new MenuItem("Exit");
		
		gui.setLayout(new BorderLayout());
		
		JPanel mainPanel = new JPanel(new GridBagLayout());
		gui.add(mainPanel, BorderLayout.CENTER);
		GridBagConstraints mainConstraints = new GridBagConstraints();
		mainConstraints.anchor = GridBagConstraints.CENTER;
		mainConstraints.gridwidth=1;
		mainConstraints.gridx = 0;
		mainConstraints.gridy = 0;
		mainConstraints.weightx=0.5;
		mainConstraints.weighty=1;
		mainConstraints.fill=GridBagConstraints.BOTH;
		mainConstraints.insets= new Insets(2, 4, 2, 4);
		
		JPanel labelPanel = new JPanel(new GridBagLayout());
		labelPanel.setBorder(blackline);
		mainPanel.add(labelPanel, mainConstraints);
		mainConstraints.gridy++;
		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.anchor = GridBagConstraints.CENTER;
		labelConstraints.gridx = 0;
		labelConstraints.gridy = 0;
		labelConstraints.weightx=0.75;
		labelConstraints.weighty=1;
		labelConstraints.fill=GridBagConstraints.HORIZONTAL;
		labelConstraints.insets= new Insets(2, 4, 2, 4);
		
		JLabel title = new JLabel("System Gauge Interface");
		title.setFont(new Font(title.getFont().getName(), Font.BOLD, 14));
		labelPanel.add(title, labelConstraints);
		labelConstraints.gridx++;
		
		JButton DBG = new JButton("Debug");
		DBG.addActionListener(DBGL);
		labelConstraints.weightx=0.25;
		labelPanel.add(DBG, labelConstraints);
		
		
		JPanel topPanel = new JPanel(new GridBagLayout());
		mainPanel.add(topPanel, mainConstraints);
		mainConstraints.gridy++;
		topPanel.setBorder(BorderFactory.createTitledBorder(blackline, "Display"));
		GridBagConstraints topConstraints = new GridBagConstraints();
		topConstraints.anchor = GridBagConstraints.CENTER;
		topConstraints.gridx = 0;
		topConstraints.gridy = 0;
		topConstraints.weightx=0.5;
		topConstraints.weighty=1;
		topConstraints.fill=GridBagConstraints.BOTH;
		topConstraints.insets= new Insets(2, 4, 2, 4);
		
		JButton CPU = new JButton("CPU");
		CPU.addActionListener(CPUL);
		topPanel.add(CPU,topConstraints);
		topConstraints.gridx++;
		
		JButton MEM = new JButton("MEM");
		MEM.addActionListener(MEML);
		topPanel.add(MEM,topConstraints);
		
		JPanel bottomPanel = new JPanel(new GridBagLayout());
		mainPanel.add(bottomPanel, mainConstraints);
		mainConstraints.gridy++;
		bottomPanel.setBorder(BorderFactory.createTitledBorder(blackline, "Backlight"));
		GridBagConstraints botConstraints = new GridBagConstraints();
		botConstraints.anchor = GridBagConstraints.CENTER;
		botConstraints.gridx = 0;
		botConstraints.gridy = 0;
		botConstraints.weightx=0.5;
		botConstraints.weighty=1;
		botConstraints.fill=GridBagConstraints.BOTH;
		botConstraints.insets= new Insets(10, 20, 10, 20);
		
		
		light.addActionListener(LITL);
		bottomPanel.add(light, botConstraints);
		

		JPanel debugPanel = new JPanel(new GridBagLayout());
		debugPanel.setBorder(BorderFactory.createTitledBorder(blackline, "Debug"));
		GridBagConstraints dbgConstraints = new GridBagConstraints();
		dbgConstraints.anchor = GridBagConstraints.CENTER;
		dbgConstraints.gridx = 0;
		dbgConstraints.gridy = 0;
		dbgConstraints.weightx=0.5;
		dbgConstraints.weighty=0.25;
		dbgConstraints.fill=GridBagConstraints.BOTH;
		dbgConstraints.insets= new Insets(10, 20, 10, 20);
		
		OVR.addActionListener(new OVRL());
		debugPanel.add(OVR, dbgConstraints);
		dbgConstraints.gridy++;
		dbgConstraints.weighty=0.5;
		
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(10);
		slider.setPaintTrack(true);
		slider.setMajorTickSpacing(20);
		slider.setPaintLabels(true);
		slider.addChangeListener(new DBSL());
		debugPanel.add(slider,dbgConstraints);
		dbgConstraints.gridy++;
		
		JButton ZER = new JButton("Zero Gauge");
		ZER.addActionListener(ZERL);
		debugPanel.add(ZER,dbgConstraints);
		dbgConstraints.gridy++;
		
		debugPanel.add(targVal, dbgConstraints);
		
		DBGL.assignFrame(gui, mainPanel, debugPanel, mainConstraints);
		
		gui.pack();
		
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent op) {
				gui.setVisible(true);
			}	
		});
		
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent op) {
				output.println("DCN%0!");
				systemTray.remove(icon);
				try {TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException a) {}
				System.exit(0);
			}	
		});
		
		popMenu.add(open);
		popMenu.add(close);
		
		icon.setPopupMenu(popMenu);
		icon.setImageAutoSize(true);
		try { systemTray.add(icon); } catch (AWTException e) {}
	}
}