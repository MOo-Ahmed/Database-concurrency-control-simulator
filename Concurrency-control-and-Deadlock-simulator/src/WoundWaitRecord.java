import java.util.ArrayList;

public class WoundWaitRecord {
	char DataItem ;
	Transaction locking = new Transaction(-1, -1);
	ArrayList<Transaction> waitingQueue = new ArrayList();
	
	public WoundWaitRecord() {}
	
	public WoundWaitRecord(char item) {
		this.DataItem = item ;
	}
	
	@Override
	public String toString() {
		return "(" + DataItem + " | " + locking + " | " + waitingQueue + ")" ;
	}

}
