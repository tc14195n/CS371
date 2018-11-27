package nachos.threads;

public class Vehicle {
	//each vehicle is its' own thread
	String direction;
	private int priority;
	private int counter;
	Vehicle next = null;
	
	public Vehicle(String _direction, int _priority) {
		this.direction = _direction;
		this.priority = _priority;
	}
	
	public void exit() {
		//check if other cars on bridge
		//release lock if no more cars on bridge
		//if (number of cars on bridge <= 3)
		//->release same_direction lock
		//else
		//->
	}
	public void cross() {
		System.out.println("Crossing bridge");
	}
}
