
public class Transaction {
	int id ;
	int timestamp ;
	
	Transaction(int id, int timestamp){
		this.id = id ;
		this.timestamp = timestamp ;
	}

	@Override
	public String toString() {
		return id + "" ;
	}
	
	public void Equals(int id, int time) {
		this.id = id ;
		this.timestamp = time ;
	}
	
	
}
