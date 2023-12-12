import java.io.PrintWriter;

import com.sun.management.OperatingSystemMXBean;

public class SysPollThread extends Thread{
	
	private OperatingSystemMXBean systemRef;
	private PrintWriter output;
	private int CPUsend;
	private int MEMsend;
	
	public SysPollThread (OperatingSystemMXBean os, PrintWriter out) {
		this.systemRef=os;
		this.output=out;
	}
	
	public void run() {
		CPUsend=-1;
		MEMsend=-1;
		double cpu;
		double sysMEMtotal;
		double sysMEMfree;
		while (true) {
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
