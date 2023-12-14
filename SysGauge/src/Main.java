import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import com.fazecast.jSerialComm.*;
import com.sun.management.OperatingSystemMXBean;

public class Main {
	
	public static boolean paired = false;
	public static int dispVal = 0;
	public static int light = 0;
	
	public static boolean debug = false;
	public static int debugVal = 50;
	public static boolean ovr = false;
	
	public static void main(String[] args) {
		ExitIcon exitIcon = new ExitIcon();
		exitIcon.Toggle("on");
		SerialPort initPorts[] = SerialPort.getCommPorts();
		SerialPort comm = initPorts[0];
		while (paired==false) {
		SerialPort ports[] = SerialPort.getCommPorts();
		for (SerialPort port : ports) {
			System.out.println(port.getSystemPortName());
			port.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
			if (port.openPort()) {
				System.out.println("Port Opened");
				Scanner pair = new Scanner(port.getInputStream());
				SearchThread searching = new SearchThread(pair);
				searching.start();
				try {TimeUnit.SECONDS.sleep(3);} catch (InterruptedException e) {}
				if (paired==true) {
					comm=port;
					System.out.println("Pairing Successful");
				} else {
					searching.interrupt();
					if(port.closePort()) {
						System.out.println("Port Closed");
					}
				}
			}
			
		}
		try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {}
		}
		
		comm.openPort();
		System.out.println(comm.getSystemPortName());
		PrintWriter output = new PrintWriter(comm.getOutputStream());
		
		
		output.println(820);
		output.flush();
		
		exitIcon.Toggle("off");
		
		GUI gui = new GUI(output);
		gui.initGUI();
		
		OperatingSystemMXBean systemRef = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
		SysPollThread poll = new SysPollThread(systemRef,output,gui);
		poll.start();
		
		
	}

}
