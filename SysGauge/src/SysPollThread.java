import java.io.PrintWriter;



import com.sun.management.OperatingSystemMXBean;

public class SysPollThread extends Thread{
	
	private OperatingSystemMXBean systemRef;
	private PrintWriter output;
	private int CPUsend;
	private int MEMsend;
	private GUI gui;
	
	public SysPollThread (OperatingSystemMXBean os, PrintWriter out, GUI g) {
		this.systemRef=os;
		this.output=out;
		this.gui=g;
	}
	
	public void run() {
		CPUsend=-1;
		MEMsend=-1;
		double cpu;
		double sysMEMtotal;
		double sysMEMfree;
		int targVal=0;
		while (true ) {
			if (Main.debug) {
				if (Main.dispVal == 0) {
					cpu = systemRef.getCpuLoad();
					try {Thread.sleep(500);} catch (InterruptedException e) {}
					cpu = systemRef.getCpuLoad();
					targVal = (int)(cpu*100);
					gui.targValUpdate(0, targVal);
					if (Main.ovr) {
					output.println("CPU%"+String.valueOf(Main.debugVal)+"!");
					output.flush();
					} else {
						output.println("CPU%"+String.valueOf(targVal)+"!");
						output.flush();
					}
				}
				if (Main.dispVal == 1) {
					sysMEMtotal = systemRef.getTotalMemorySize();
					sysMEMfree = systemRef.getFreeMemorySize();
					targVal = (int)((1-(sysMEMfree/sysMEMtotal))*100);
					gui.targValUpdate(1, targVal);
					try {Thread.sleep(500);} catch (InterruptedException e) {}
					if (Main.ovr) {
						output.println("MEM%"+String.valueOf(Main.debugVal)+"!");
						output.flush();
					} else {
						output.println("MEM%"+String.valueOf(targVal)+"!");
						output.flush();
					}
				}
			} else {
					if (Main.dispVal == 0) {
						cpu = systemRef.getCpuLoad();
						try {Thread.sleep(500);} catch (InterruptedException e) {}
						cpu = systemRef.getCpuLoad();
						CPUsend = (int)(cpu*100);
						output.println("CPU%"+String.valueOf(CPUsend)+"!");
						output.flush();
					}
					if (Main.dispVal == 1) {
						sysMEMtotal = systemRef.getTotalMemorySize();
						sysMEMfree = systemRef.getFreeMemorySize();
						MEMsend = (int)((1-(sysMEMfree/sysMEMtotal))*100);
						output.println("MEM%"+String.valueOf(MEMsend)+"!");
						output.flush();
						try {Thread.sleep(500);} catch (InterruptedException e) {}
					}
			}
		}
		}
}
