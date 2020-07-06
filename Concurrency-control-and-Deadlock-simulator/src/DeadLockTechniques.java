import java.util.ArrayList;

public class DeadLockTechniques {

	public ArrayList<Operation> WoundWait(ArrayList<Operation> oldSchedule) {
		ArrayList<Operation> newSchedule = new ArrayList<Operation>();
		ArrayList<Operation> copy = new ArrayList<Operation>();
		copy.addAll(oldSchedule);
		ArrayList<Character> list = new ArrayList();
		list.add('x');
		list.add('y');
		list.add('z');
		WoundWaitTable LockingTable = new WoundWaitTable(list);
		int[] TimeStamps = { 100, 200, 300 }; // the index represents id of Transaction
		ArrayList<Integer> DiedTransactions = new ArrayList<Integer>();

		for (int i = 0; i < copy.size(); i++) {
			Operation o = copy.get(i);
			System.out.println("The operation is :  " + o);
			if (o.type == 'R' || o.type == 'W') {
				
				
				
				Transaction T = LockingTable.getLockingTrans(o.dataItem);
				if (T.id == -1) {
					// Not locked
					LockingTable.setLockingTrans(o.dataItem,
							new Transaction(o.TransactionNumber, TimeStamps[o.TransactionNumber - 1]));
					newSchedule.add(o);
				} else if (T.id == o.TransactionNumber) {
					newSchedule.add(o);
					//continue;
				} else {
					// Locked to a different transaction
					if (TimeStamps[o.TransactionNumber - 1] > T.timestamp) {
						// o Waits
						if (LockingTable.indexTranactionInWaitQueue(o.dataItem, o.TransactionNumber) == -1) {
							int indexOfMyCommit = indexOfOperation('C', o.TransactionNumber, ' ', copy);
							int indexOfOtherCommit = indexOfOperation('C', T.id, ' ', copy);
							if (indexOfMyCommit > indexOfOtherCommit) {
								// I commit after it
								copy.add(indexOfOtherCommit+1, o);
								copy.remove(i);
								i--;
							} else {
								// I commit before
								copy.add(indexOfOtherCommit + 1, copy.get(indexOfMyCommit));
								copy.remove(indexOfMyCommit);
								indexOfMyCommit = indexOfOperation('C', o.TransactionNumber, ' ', copy);
								int indexOfRead = indexOfOperation('R', T.id, o.dataItem, copy);
								int indexOfWrite = indexOfOperation('W', T.id, o.dataItem, copy);
								int indexOfOtherOperation = Math.max(indexOfRead, indexOfWrite);
								copy.add(indexOfOtherOperation+1, o);
								copy.remove(i);
								
								i--;
							}

							LockingTable.addTransactionToWait(
									new Transaction(o.TransactionNumber, TimeStamps[o.TransactionNumber - 1]),
									o.dataItem);
						}
					} else {
						// T Dies
						DiedTransactions.add(T.id);
						newSchedule.add(new Operation("A" + T.id));
						newSchedule.add(o);
						// here you should add abort and remove all operations of this trans
						removeAllOperations(i, T.id, copy);
						LockingTable.removeAllTransactionInstances(T.id);

					}
				}
			} else {
				newSchedule.add(o);
				LockingTable.removeAllTransactionInstances(o.TransactionNumber);
			}
			System.out.println("Locking Table :  " + LockingTable);
			System.out.println("Wounded Transactions :  " + DiedTransactions);
			System.out.println("/____________________________________________/");
			
			if(o.type == 'W') {
				int idx = LockingTable.indexOfItem(o.dataItem);
				if(LockingTable.records.get(idx).waitingQueue.isEmpty() == true) {
					LockingTable.setLockingTrans(o.dataItem, new Transaction(-1, -1));
				}
				else{
					LockingTable.setLockingTrans(o.dataItem, LockingTable.records.get(idx).waitingQueue.get(0));
				}
			}
		}

		return newSchedule;
	}
	
	void removeAllOperations(int index, int TID, ArrayList<Operation> Schedule) {
		for (int i = index; i < Schedule.size(); i++) {
			Operation o = Schedule.get(i);
			if (o.type != 'A' && o.TransactionNumber == TID) {
				Schedule.remove(i);
				i--;
			}
		}
	}

	void reOrganizeOperations(int currentIndex , int TransactionToAbort, ArrayList<Operation> Schedule , WoundWaitTable LockingTable) {
		ArrayList<Operation> operationsToBringForward = new ArrayList<Operation>();
		int TransactionToOrganize = getWaitingOnceTransaction(TransactionToAbort, LockingTable);
		for(int i = currentIndex ; i < Schedule.size() ; i++) {
			Operation o = Schedule.get(i);
			if(o.TransactionNumber == TransactionToOrganize && TransactionToOrganize != -1) {
				operationsToBringForward.add(o);
				Schedule.remove(i);
				i-- ;
			}
		}
		Schedule.addAll(currentIndex, operationsToBringForward);
	}
	
	int getWaitingOnceTransaction(int LockingTransaction , WoundWaitTable LockingTable) {
		for(int i = 0 ; i < LockingTable.records.size() ; i++) {
			if(LockingTable.records.get(i).locking.id == LockingTransaction) {
				int waiting = LockingTable.records.get(i).waitingQueue.get(0).id;
				Transaction temp = LockingTable.records.get(i).waitingQueue.get(0);
				LockingTable.records.get(i).waitingQueue.remove(0);
				if(LockingTable.isTransactionWaitingAlso(waiting) == false) {
					LockingTable.records.get(i).waitingQueue.add(0,temp);
					return waiting ;
				}
				LockingTable.records.get(i).waitingQueue.add(0,temp);
			}
		}
		return -1 ;
	}

	int indexOfOperation(char type, int transNumber, char DataItem, ArrayList<Operation> oldSchedule) {
		for (int i = 0; i < oldSchedule.size(); i++) {
			if (oldSchedule.get(i).TransactionNumber == transNumber && oldSchedule.get(i).type == type) {
				if (type == 'C' || (type != 'C' && DataItem == oldSchedule.get(i).dataItem))
					return i;
			}
		}
		return -1;
	}

	int indexOfLastOperation(int TransNumber, ArrayList<Operation> Schedule) {
		int idx = -1;
		for (int i = 0; i < Schedule.size(); i++) {
			if (Schedule.get(i).TransactionNumber == TransNumber && Schedule.get(i).type != 'C'
					&& Schedule.get(i).type != 'A' && i > idx) {
				idx = i;
			}
		}
		return idx;
	}

	public ArrayList<Operation> CautiousWait(ArrayList<Operation> oldSchedule) {
		ArrayList<Operation> newSchedule = new ArrayList<Operation>();
		ArrayList<Operation> copy = new ArrayList<Operation>();
		copy.addAll(oldSchedule);
		ArrayList<Character> list = new ArrayList();
		list.add('x');
		list.add('y');
		list.add('z');
		WoundWaitTable LockingTable = new WoundWaitTable(list);
		ArrayList<Integer> DiedTransactions = new ArrayList<Integer>();

		for (int i = 0; i < copy.size(); i++) {
			Operation o = copy.get(i);
			System.out.println("The operation is : " + o);
			if (o.type == 'R' || o.type == 'W') {
				
				
				Transaction T = LockingTable.getLockingTrans(o.dataItem);
				if (T.id == -1) {
					// Not locked
					LockingTable.setLockingTrans(o.dataItem, new Transaction(o.TransactionNumber, 0));
					newSchedule.add(o);
				} else if (T.id == o.TransactionNumber) {
					newSchedule.add(o);
					// continue ;
				} else {
					// Locked to a different transaction
					if (LockingTable.isTransactionWaitingAlso(T.id) == true) {
						// abort o
						DiedTransactions.add(o.TransactionNumber);
						newSchedule.add(new Operation("A" + o.TransactionNumber));
						removeAllOperations(i, o.TransactionNumber, copy);
						reOrganizeOperations(i, o.TransactionNumber, copy , LockingTable);
						i--;
						LockingTable.removeAllTransactionInstances(o.TransactionNumber);
					} else if (LockingTable.indexTranactionInWaitQueue(o.dataItem, o.TransactionNumber) == -1) {
						int indexOfMyCommit = indexOfOperation('C', o.TransactionNumber, ' ', copy);
						int indexOfOtherCommit = indexOfOperation('C', T.id, ' ', copy);
						if (indexOfMyCommit > indexOfOtherCommit) {
							// I commit after it
							int indexOfLastOperation = indexOfLastOperation(o.TransactionNumber, copy);
							copy.add(indexOfLastOperation, o);
							copy.remove(i);
							i--;
						} else {
							// I commit before
							copy.add(indexOfOtherCommit + 1, copy.get(indexOfMyCommit));
							copy.remove(indexOfMyCommit);
							int indexOfRead = indexOfOperation('R', T.id, o.dataItem, copy);
							int indexOfWrite = indexOfOperation('W', T.id, o.dataItem, copy);
							int indexOfOtherOperation = Math.max(indexOfRead, indexOfWrite);
							copy.add(indexOfOtherOperation+1, o);
							copy.remove(i);
							i--;
						}

						LockingTable.addTransactionToWait(new Transaction(o.TransactionNumber, 0), o.dataItem);
					}
				}

			} else {
				newSchedule.add(o);
				LockingTable.removeAllTransactionInstances(o.TransactionNumber);
			}
			System.out.println("Locking Table :  " + LockingTable);
			System.out.println("Wounded Transactions :   " + DiedTransactions);
			System.out.println("|----------------------------------------------------------------|");
			
			if(o.type == 'W') {
				int idx = LockingTable.indexOfItem(o.dataItem);
				if(LockingTable.records.get(idx).waitingQueue.isEmpty() == true) {
					LockingTable.setLockingTrans(o.dataItem, new Transaction(-1, -1));
				}
				else{
					LockingTable.setLockingTrans(o.dataItem, LockingTable.records.get(idx).waitingQueue.get(0));
				}
			}
		}

		return newSchedule;
	}
}
