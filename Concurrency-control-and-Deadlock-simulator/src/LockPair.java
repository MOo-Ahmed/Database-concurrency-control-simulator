
public class LockPair {
	char dataItem ;
	int LockingTransactionNumber ;
	char lockType ;
	
	public LockPair() {}
	
	public LockPair(char c, int trans, char type) {
		this.dataItem = c ;
		this.LockingTransactionNumber = trans ;
		lockType = type ;
	}
	
	@Override
	public String toString() {
		return "(" + dataItem + " -> " + LockingTransactionNumber + ")" ;
	}
}
