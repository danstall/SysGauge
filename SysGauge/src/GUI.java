import java.awt.AWTException;
import java.awt.BorderLayout;
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
import javax.swing.JPanel;

public class GUI extends JFrame{
	
	private PrintWriter output;
	
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
	
	private class LITListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (Main.light==0) {
				Main.light=1;
				output.println("LIT%1!");
				System.out.println("Backlight Enabled");
				try {TimeUnit.MILLISECONDS.sleep(600);} catch (InterruptedException a) {}
			} else {
				Main.light=0;
				output.println("LIT%0!");
				System.out.println("Backlight Disabled");
				try {TimeUnit.MILLISECONDS.sleep(600);} catch (InterruptedException a) {}
			}
			
		}
	}
	
	public GUI (PrintWriter p) {
		this.output=p;
	}
	
	public void initGUI() {
		CPUListener CPUL = new CPUListener();
		MEMListener MEML = new MEMListener();
		LITListener LITL = new LITListener();
		
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
		
		JPanel topPanel = new JPanel(new GridBagLayout());
		gui.add(topPanel, BorderLayout.CENTER);
		topPanel.setBorder(BorderFactory.createTitledBorder("Display"));
		GridBagConstraints topConstraints = new GridBagConstraints();
		topConstraints.anchor = GridBagConstraints.CENTER;
		topConstraints.gridx = 0;
		topConstraints.gridy = 0;
		topConstraints.weightx=0.5;
		topConstraints.weighty=1;
		topConstraints.insets= new Insets(10, 20, 10, 20);
		
		JButton CPU = new JButton("CPU");
		CPU.addActionListener(CPUL);
		topPanel.add(CPU,topConstraints);
		topConstraints.gridx++;
		
		JButton MEM = new JButton("MEM");
		MEM.addActionListener(MEML);
		topPanel.add(MEM,topConstraints);
		
		JPanel bottomPanel = new JPanel(new BorderLayout());
		gui.add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setBorder(BorderFactory.createTitledBorder("Backlight"));
		
		JButton light = new JButton("Toggle");
		light.addActionListener(LITL);
		bottomPanel.add(light);
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
