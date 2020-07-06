import java.util.ArrayList;

public class TestClass {

	public static void main(String[] args) {
		/*
		TestClass test = new TestClass();
		test.test1();
		System.out.println("\n/_____/_____/_____/_____/_____/_____/_____/_____/_____/_____/\n");
		test.test2();
		System.out.println("\n/_____/_____/_____/_____/_____/_____/_____/_____/_____/_____/\n");
		test.test3();
		System.out.println("\n/_____/_____/_____/_____/_____/_____/_____/_____/_____/_____/\n");
		test.test4();
		System.out.println("\n/_____/_____/_____/_____/_____/_____/_____/_____/_____/_____/\n");
		test.test5();
		System.out.println("\n/_____/_____/_____/_____/_____/_____/_____/_____/_____/_____/\n");
		test.test6();
		System.out.println("\n/_____/_____/_____/_____/_____/_____/_____/_____/_____/_____/\n");
		test.test7();
		System.out.println("\n/_____/_____/_____/_____/_____/_____/_____/_____/_____/_____/\n");
		test.test8();
		System.out.println("\n/_____/_____/_____/_____/_____/_____/_____/_____/_____/_____/\n");
		*/
		TestClass test = new TestClass();
		test.test8();
		System.out.println("\n/_____/_____/_____/_____/_____/_____/_____/_____/_____/_____/\n");
		
		

	}


	public void test1() {
		System.out.println("Testing conversion of serializable schedule to strict 2pl");
		ArrayList<Operation> oldSchedule = new ArrayList<Operation>();
		oldSchedule.addAll(getSerializableSchedule());
		Strict2PL c = new Strict2PL();
		printSchedule(c.SerializableConversion(oldSchedule));	
	}

	public void test2() {
		System.out.println("Testing conversion of not serializable schedule to strict 2pl");
		ArrayList<Operation> oldSchedule = new ArrayList<Operation>();
		oldSchedule.addAll(getNotSerializableSchedule());
		Strict2PL c = new Strict2PL();
		printSchedule(c.NotSerializableConversion(oldSchedule));	
	}
	
	public void test3() {
		System.out.println("Testing approval of serializable schedule in terms of strict timestamp ordering");
		ArrayList<Operation> s1 = new ArrayList<Operation>();
		s1.addAll(getSerializableSchedule());
		Strict2PL c = new Strict2PL();
		boolean res = c.isStrict(s1);
		if(res == true) {
			System.out.println("Approved");
		}
		else {
			System.out.println("Not Approved");
		}
	}
	
	public void test4() {
		System.out.println("Testing approval of non-serializable schedule in terms of strict timestamp ordering");
		ArrayList<Operation> s1 = new ArrayList<Operation>();
		s1.addAll(getNotSerializableSchedule());
		Strict2PL c = new Strict2PL();
		boolean res = c.isStrict(s1);
		if(res == true) {
			System.out.println("Approved");
		}
		else {
			System.out.println("Not Approved");
		}
	}
	
	public void test5() {
		System.out.println("Testing using of Wound wait with serializable schedule");
		ArrayList<Operation> oldSchedule = new ArrayList<Operation>();
		oldSchedule.addAll(getSerializableSchedule());
		DeadLockTechniques c = new DeadLockTechniques();
		printSchedule(c.WoundWait(oldSchedule));
	}
	
	public void test6() {
		System.out.println("Testing using of Wound wait with not serializable schedule");
		ArrayList<Operation> oldSchedule = new ArrayList<Operation>();
		oldSchedule.addAll(getNotSerializableSchedule());
		DeadLockTechniques c = new DeadLockTechniques();
		printSchedule(c.WoundWait(oldSchedule));
	}
	
	public void test7() {
		System.out.println("Testing using of Cautious wait with serializable schedule");
		ArrayList<Operation> oldSchedule = new ArrayList<Operation>();
		oldSchedule.addAll(getSerializableSchedule());
		DeadLockTechniques c = new DeadLockTechniques();
		printSchedule(c.CautiousWait(oldSchedule));
	}
	
	public void test8() {
		System.out.println("Testing using of cautious wait with not serializable schedule");
		ArrayList<Operation> oldSchedule = new ArrayList<Operation>();
		oldSchedule.addAll(getNotSerializableSchedule());
		DeadLockTechniques c = new DeadLockTechniques();
		printSchedule(c.CautiousWait(oldSchedule));

	}
	
	public ArrayList<Operation> getSerializableSchedule() {
		// The serializable schedule is prepared here directly
		Operation o = new Operation("R1(x)");
		Operation o2 = new Operation("R2(z)");
		Operation o3 = new Operation("R3(z)");
		Operation o4 = new Operation("R3(x)");
		Operation o5 = new Operation("R3(y)");
		Operation o6 = new Operation("W1(x)");
		Operation o7 = new Operation("C1");
		Operation o8 = new Operation("W3(y)");
		Operation o9 = new Operation("C3");
		Operation o10 = new Operation("R2(y)");
		Operation o11 = new Operation("W2(z)");
		Operation o12 = new Operation("W2(y)");
		Operation o13 = new Operation("C2");
		ArrayList<Operation> oldSchedule = new ArrayList();
		oldSchedule.add(o); oldSchedule.add(o2); oldSchedule.add(o3);
		oldSchedule.add(o4); oldSchedule.add(o5); oldSchedule.add(o6);
		oldSchedule.add(o7); oldSchedule.add(o8); oldSchedule.add(o9);
		oldSchedule.add(o10); oldSchedule.add(o11); oldSchedule.add(o12);
		oldSchedule.add(o13);
		return oldSchedule;
	}

	public ArrayList<Operation> getNotSerializableSchedule(){
		// The not serializable schedule is prepared here directly
		Operation o = new Operation("R1(x)");
		Operation o2 = new Operation("R2(z)");
		Operation o3 = new Operation("R3(x)");
		Operation o4 = new Operation("R1(z)");
		Operation o5 = new Operation("R2(x)");
		Operation o6 = new Operation("R3(y)");
		Operation o7 = new Operation("W1(x)");
		Operation o8 = new Operation("C1");
		Operation o9 = new Operation("W2(z)");
		Operation o10 = new Operation("W3(y)");
		Operation o11 = new Operation("C3");
		Operation o12 = new Operation("W2(y)");
		Operation o13 = new Operation("C2");
		ArrayList<Operation> oldSchedule = new ArrayList();
		oldSchedule.add(o); oldSchedule.add(o2); oldSchedule.add(o3);
		oldSchedule.add(o4); oldSchedule.add(o5); oldSchedule.add(o6);
		oldSchedule.add(o7); oldSchedule.add(o8); oldSchedule.add(o9);
		oldSchedule.add(o10); oldSchedule.add(o11); oldSchedule.add(o12);
		oldSchedule.add(o13);
		return oldSchedule ;
	}
	
	public void printSchedule(ArrayList<Operation> schedule) {
		int rows = schedule.size() / 5 ;
		for(int i = 0 , j = 0 ; i <= rows ; i++) {
			for(int k = 0; j < schedule.size() && k < 5 ; j++, k++) {
				System.out.print(schedule.get(j));
				if(j != schedule.size() -1) System.out.print(" - ");
			}
			System.out.println();
		}
	}
}