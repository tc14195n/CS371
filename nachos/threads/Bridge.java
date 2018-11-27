package nachos.threads;

//import java.util.ArrayList;
//import java.util.LinkedList;

//import nachos.threads.Semaphore.PingTest;

//test program instantiates bridge
//then creates vehicles in different directions with different priorities

public class Bridge {
	static int num_cob = 0; //NUMBER OF CARS ON BRIDGE
	static Lock bridge_lock = new Lock();
	static Lock east_lock = new Lock();
	static Lock west_lock = new Lock();
	static Lock[] direction_lock = new Lock[2];
	static Condition[] cars_cv = new Condition[2];

	static int cars_waiting[] = new int[2];

	static int bridge_direction = 0; //0 = east or 1 = west

	public Bridge() {
		cars_waiting[0] = 0;
		cars_waiting[1] = 0;
		cars_cv[0] = new Condition(east_lock);
		cars_cv[1] = new Condition(west_lock);
		direction_lock[0] = east_lock;
		direction_lock[1] = west_lock;
	}


	public static void EnterBridge(int direction) {
		//bridge_lock.acquire();
		direction_lock[direction].acquire();
		cars_waiting[direction]++;
		//System.out.println("direction: " + direction + "	Bridge -> EnterBridge ->  waiting0: " + cars_waiting[0] + " waiting1: " + cars_waiting[1]);

		while(num_cob == 3 || bridge_direction != direction) {
			//bridge_lock.release();
			//System.out.println("EnterBridge -> sleep()");
			cars_cv[direction].sleep();
			//bridge_lock.acquire();
			if(cars_waiting[Math.abs(direction -1)] == 0 && num_cob == 0) {
				bridge_direction = direction;
			}
			//System.out.println("	Woke up...");
			if(cars_waiting[Math.abs(direction -1)] == 0 && num_cob == 0 && cars_waiting[direction] != 0) {
				//bridge_direction = direction;
				//System.out.println("SWITCHED DIRECTION -> " + bridge_direction);
				//cars_cv[direction].wakeAll();
				//System.out.println("SWITCHED DIRECTION -> " + bridge_direction);
			}
		}
		//System.out.println("");
		cars_waiting[direction]--;

		num_cob++;
		//bridge_lock.release();
		direction_lock[direction].release();
		
		//System.out.print("EnterBridge :: direction " + direction + " -> complete");
		//System.out.println(":: num_cob: " + num_cob + "  waiting0: " + cars_waiting[0] + " waiting1: " + cars_waiting[1]);

	}

	public static void CrossBridge(int direction) {
		System.out.println("	Crossing bridge. Direction: " + direction + " >>> num_cob: " + num_cob);
	}

	public static void ExitBridge(int direction) {
		//bridge_lock.acquire();
		direction_lock[direction].acquire();
		//System.out.println("Bridge -> ExitBridge -> After acquire");

		num_cob--;
		if(cars_waiting[direction] > 0) {
		cars_cv[direction].wakeAll();
		}

		//cars_waiting[direction]--;
		//System.out.print("ExitBridge :: direction " + direction + " -> complete");
		//System.out.println(":: num_cob: " + num_cob + "  waiting0: " + cars_waiting[0] + " waiting1: " + cars_waiting[1]);

		if(num_cob == 0 && cars_waiting[direction] == 0 && cars_waiting[Math.abs(direction - 1)] > 0) {
			//bridge_lock.acquire();
			bridge_direction = Math.abs(direction - 1);
			//System.out.println("SWITCHING DIRECTION");
				direction_lock[bridge_direction].acquire();
				//System.out.println("wakeAll()");
				cars_cv[bridge_direction].wakeAll();
				direction_lock[bridge_direction].release();
				//bridge_lock.release();
			

		} /*else {
			System.out.println("Bridge -> ExitBridge -> before wake");
			cars_cv[direction].wake();
			System.out.println("Bridge -> ExitBridge -> after wake");
		}*/
		direction_lock[direction].release();
		//bridge_lock.release();
		//System.out.println("ExitBridge  -> complete");
		//System.out.println(":: num_cob: " + num_cob);

	}
	private static class testRun implements Runnable{
		private int direction;
		testRun(int direction){
			this.direction = direction;
		}
		public void run() {
			EnterBridge(direction);
			CrossBridge(direction);
			ExitBridge(direction);
		}
	}


	public void selfTest() {
		System.out.println("Inside bridge.selfTest()");
		for(int i = 0; i < 300; i++) {
			new KThread(new testRun(i%2)).setName("car" + i).fork();
			System.out.println("car" + i);
		}
		/*for(int i = 0; i < 100; i++) {
			new KThread(new testRun(0)).setName("west_car" + i).fork();
			System.out.println("west " + i);
		}*/
		//private int direction;
		//this.direction = direction;

		//Bridge brooklyn = new Bridge();
		//brooklyn.EnterBridge(direction);
		//brooklyn.CrossBridge(direction);
		//brooklyn.ExitBridge(direction);
	}

}


