import java.util.ArrayList;

public class Strict2PL {

	public ArrayList<Operation> SerializableConversion (ArrayList<Operation> oldSchedule){
		ArrayList<Operation> newSchedule = new ArrayList<Operation>()  ;
		ArrayList<LockPair> LockTable = new ArrayList<LockPair>();
		for(int i = 0 , k = 0; i < oldSchedule.size() ; i++ , k++) {
			String sentence = null ;
			Operation temp = oldSchedule.get(i);
			if(temp.type == 'C')	{
				removeLocks(temp.TransactionNumber, LockTable);
				continue ;
			}
			
			int TransNumber = DataItemIsLockedFor(temp.dataItem, LockTable) ;
			if(TransNumber == -1) {
				// The item isn't locked , Then we should add this data item in lock table
				if(temp.type == 'R') {
					if(isThereUpgrading(temp, oldSchedule) == true) {
						// There's upgrade
						LockTable.add(new LockPair(temp.dataItem, temp.TransactionNumber , 'W'));
						sentence = "X" + temp.TransactionNumber + "(" + temp.dataItem + ")";
					}
					else {
						// No upgrade
						LockTable.add(new LockPair(temp.dataItem, temp.TransactionNumber , 'S'));
						sentence = "S" + temp.TransactionNumber + "(" + temp.dataItem + ")";
					}
				}
				else {
					LockTable.add(new LockPair(temp.dataItem, temp.TransactionNumber , 'W'));
					sentence = "X" + temp.TransactionNumber + "(" + temp.dataItem + ")";
				}
			}
			else if(TransNumber == temp.TransactionNumber)	continue ;
			
			else {
				// The item is already locked to another transaction
				moveAllOperations(oldSchedule, i, temp.TransactionNumber, TransNumber);
				//i-- ;
				continue ;
			}
			
		}
		newSchedule.clear();
		newSchedule.addAll(oldSchedule);
		MakeNotRigourous(newSchedule);
		return newSchedule ;
	}
	
	public void MakeNotRigourous(ArrayList<Operation> Schedule) {
		ArrayList<LockPair> table = new ArrayList();
		for(int i = 0 ; i < Schedule.size() ; i++) {
			Operation o = Schedule.get(i);
			if(o.type == 'C') {
				for(int j = 0 ; j < table.size() ; j++) {
					if(table.get(j).LockingTransactionNumber == o.TransactionNumber) {
						table.remove(j);
						j-- ;
					}
				}
			}
			else if(o.type == 'R' && isThereUpgrading(o, Schedule) == false) {
				Schedule.add(i, new Operation("S"+o.TransactionNumber+"(" + o.dataItem + ")"));
				i++ ;
				int indexOfCommit = indexOfOperation('C', o.TransactionNumber, ' ', Schedule);
				Schedule.add(indexOfCommit, new Operation("U" + o.TransactionNumber + "(" + o.dataItem + ")"));
			}
			else if((o.type == 'R' && isThereUpgrading(o, Schedule) == true) || o.type == 'W') {
				if(DataItemIsLockedFor(o.dataItem, table) == -1) {
					Schedule.add(i, new Operation("X"+o.TransactionNumber+"(" + o.dataItem + ")"));
					i++ ;
					int indexOfCommit = indexOfOperation('C', o.TransactionNumber, ' ', Schedule);
					Schedule.add(indexOfCommit+1, new Operation("U" + o.TransactionNumber + "(" + o.dataItem + ")"));
					table.add(new LockPair(o.dataItem, o.TransactionNumber, 'X'));
				}
				else if(DataItemIsLockedFor(o.dataItem, table) == o.TransactionNumber) {
					continue ;
				}
			}
		}
	}	
	
	void removeLocks(int tNum, ArrayList<LockPair> LockTable) {
		for(int i = 0 ; i < LockTable.size() ; i++) {
			if(tNum == LockTable.get(i).LockingTransactionNumber) {
				LockTable.remove(i);
				i-- ;
			}
		}
	}
	
	void moveAllOperations(ArrayList<Operation> oldSchedule , int currentIndex, int TransNumberToBeDelayed, int TransNumberToBeEarly ) {
		int indexOfCommit1 = indexOfOperation('C', TransNumberToBeEarly, ' ' , oldSchedule);
		// First phase is to delay operations
		for(int i = currentIndex ; i < oldSchedule.size() ; i++) {
			Operation o = oldSchedule.get(i);
			if(o.type == 'C' && o.TransactionNumber == TransNumberToBeEarly)	break ;
			else if(o.TransactionNumber == TransNumberToBeDelayed) {
				oldSchedule.remove(i);
				oldSchedule.add(indexOfCommit1, o);
				i-- ;
			}
		}		
	}
	
	int indexOfOperation(char type , int transNumber, char DataItem, ArrayList<Operation> oldSchedule) {
		for(int i = 0 ; i < oldSchedule.size() ; i++) {
			if(oldSchedule.get(i).TransactionNumber == transNumber && oldSchedule.get(i).type == type) {
				if(type == 'C' || (type != 'C' && DataItem == oldSchedule.get(i).dataItem))		return i ;
			}
		}
		return -1 ;
	}
	
	boolean isThereUpgrading(Operation o , ArrayList<Operation> oldSchedule) {
		for(int i = 0 ; i < oldSchedule.size() ; i++) {
			Operation o2 = oldSchedule.get(i);
			if(o2.dataItem == o.dataItem && o2.type == 'W' && o.TransactionNumber == o2.TransactionNumber ) {
				return true ;
			}
		}
		return false ;
	} 
	
	int DataItemIsLockedFor(char di, ArrayList<LockPair> LockTable) {
		int tNum = -1;
		for(int i = 0 ; i < LockTable.size() ; i++) {
			if(LockTable.get(i).dataItem == di) {
				tNum = LockTable.get(i).LockingTransactionNumber ;
				break ;
			}
		}
		return tNum ;
	}

	public ArrayList<Operation> NotSerializableConversion (ArrayList<Operation> oldSchedule){
		
		ArrayList<Operation> newSchedule  = new ArrayList<Operation> (); ;
		// A suggestion here is to execute every transaction separately
		// We know we have 3 transactions, so we begin by separating them.
		ArrayList<Operation> T1 = separateTransactionOperations(oldSchedule, 1);
		ArrayList<Operation> T2 = separateTransactionOperations(oldSchedule, 2);
		ArrayList<Operation> T3 = separateTransactionOperations(oldSchedule, 3);
		newSchedule.addAll(T1);
		newSchedule.addAll(T2);
		newSchedule.addAll(T3);
		MakeNotRigourous(newSchedule);
		return newSchedule ;
	}
	
	private ArrayList<Operation> separateTransactionOperations (ArrayList<Operation> oldSchedule, int transNumber){
		ArrayList<Operation> Transaction = new ArrayList<Operation> ();
		for(int i = 0 ; i < oldSchedule.size() ; i++) {
			Operation temp = oldSchedule.get(i);
			if(temp.TransactionNumber == transNumber) {
				Transaction.add(temp);
			}
		}
		return Transaction ;
	}


	public boolean isStrict(ArrayList<Operation> oldSchedule) {
		for(int i = 0 ; i < oldSchedule.size() ; i++) {
			int indexOfCommit = -1;
			Operation o = oldSchedule.get(i);
			if(o.type == 'W') {
				indexOfCommit = indexOfOperation('C', o.TransactionNumber, ' ', oldSchedule);
				
				for(int j = i+1 ; j < oldSchedule.size() ; j++) {
					Operation o2 = oldSchedule.get(j);
					if((o2.type == 'W' || o2.type == 'R') && o2.dataItem == o.dataItem && j < indexOfCommit ) {
						System.out.println(o + " , " + indexOfCommit + " , "+ j);
						return false;
					}
				}
			}
		}
		
		return true;
	}
}
