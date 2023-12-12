import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitIcon {
	
	TrayIcon InitIcon;
	SystemTray systemTray;
	PopupMenu popMenu;
	MenuItem close;
	
	public ExitIcon() {
		this.systemTray = SystemTray.getSystemTray();
		this.popMenu = new PopupMenu();
		this.close = new MenuItem("Exit");
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent op) {
				systemTray.remove(InitIcon);
				System.exit(0);
			}	
		});
		popMenu.add(close);
		InitIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("gaugeIcon.png")));
		InitIcon.setPopupMenu(popMenu);
		InitIcon.setImageAutoSize(true);
	}
	
	public void Toggle(String input) {
		if (input=="on") {
			try { systemTray.add(InitIcon); } catch (AWTException e) {}
		}
		if (input=="off") {
			systemTray.remove(InitIcon);
		}
	}
}
