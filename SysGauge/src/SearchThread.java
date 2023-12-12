import java.util.Scanner;

public class SearchThread extends Thread{

	private Scanner s;

	public SearchThread(Scanner pair) {
		this.s = pair;
	}

	public void run() {
		System.out.println("Thread Executed");
		int pairNum= 0;
		for (int i = 0; i<2; i++) {
			try{pairNum=Integer.parseInt(this.s.nextLine());}catch(Exception e){}
			if (pairNum==917) {
				Main.paired=true;
				break;
			}
		}
	}
}
