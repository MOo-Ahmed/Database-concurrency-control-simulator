import java.util.ArrayList;

public class WoundWaitTable {
	ArrayList <WoundWaitRecord> records = new ArrayList();
	
	public WoundWaitTable(ArrayList<Character> items) {
		for(int i = 0 ; i < items.size() ; i++) {
			records.add(new WoundWaitRecord(items.get(i)));
		}
	}
	
	public int indexOfItem(char c) {
		for(int i = 0 ; i < records.size() ; i++) {
			if(records.get(i).DataItem == c)	return i ;
		}
		return -1 ;
	}
	
	public Transaction getLockingTrans (char c) {
		return records.get(indexOfItem(c)).locking ;
	} 

	public void setLockingTrans (char c, Transaction T) {
		records.get(indexOfItem(c)).locking.Equals(T.id, T.timestamp); ;
	} 
	
	public void addTransactionToWait(Transaction T, char item) {
		records.get(indexOfItem(item)).waitingQueue.add(T);
	}
	
	public void removeTransactionFromWait(int TNum, char item) {
		if(records.get(indexOfItem(item)).waitingQueue.isEmpty() == false && TNum != -1)
		{
			for(int i = 0 ; i < records.get(indexOfItem(item)).waitingQueue.size() ; i++) {
				if(records.get(indexOfItem(item)).waitingQueue.get(i).id == TNum) {
					records.get(indexOfItem(item)).waitingQueue.
					  remove(records.get(indexOfItem(item)).waitingQueue.get(i));
				}
			}
		}
		
	}
	
	public int indexTranactionInWaitQueue(char c, int id) {
		int idx = indexOfItem(c);
		ArrayList<Transaction> temp = records.get(idx).waitingQueue;
		for(int i = 0 ; i < temp.size() ; i++) {
			if(temp.get(i).id == id)	return i ;
		}
		return -1 ;
	}
	
	public void removeAllTransactionInstances(int id) {
		for(int i = 0 ; i < records.size() ; i++) {
			if(records.get(i).locking.id == id) {
				if(records.get(i).waitingQueue.isEmpty()) {
					records.get(i).locking.Equals(-1, -1);
				}
				else {
					Transaction T = records.get(i).waitingQueue.get(0);
					records.get(i).locking.Equals(T.id, T.timestamp);
					records.get(i).waitingQueue.remove(0);
				}
			}
			else {
				removeTransactionFromWait(id, records.get(i).DataItem);
			}
		}
	}

	@Override
	public String toString() {
		String result = "";
		for(int i = 0 ; i < records.size() ; i++) {
			result = result + records.get(i).toString() + "  //  ";
		}
		return result ;
	}

	public boolean isTransactionWaitingAlso(int id) {
		for(int i = 0 ; i < records.size() ; i++) {
			if(indexTranactionInWaitQueue(records.get(i).DataItem, id) != -1) {
				return true ;
			}
		}
		return false ;
	}
	
}
