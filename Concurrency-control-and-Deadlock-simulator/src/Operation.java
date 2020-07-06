
public class Operation {
	char type ;
	int TransactionNumber ;
	char dataItem ;
	
	Operation(){}
	
	Operation(String sentence){	// It will come in the format of :  W1(x)  or C1
		this.type = sentence.charAt(0) ;
		this.TransactionNumber = Integer.parseInt(sentence.toCharArray()[1] + "");
		if(type != 'C' && type != 'A') {
			this.dataItem = sentence.charAt(3);
		}
	}

	@Override
	public String toString() {
		String res = (type + "") + TransactionNumber ;
		if (type != 'C' && type != 'A')	res = res + "( " + dataItem + " )" ;
		return res ;
	}
}
