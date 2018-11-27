package nachos.machine;
import nachos.threads.*;

public class TJ_test {

	TJ_test() throws InterruptedException{
		Bridge manhattan = new Bridge();
		for(int i = 0; i < 50; i++) {
			manhattan.newCar(0);
		}
		for(int i = 0; i < 50; i++) {
			manhattan.newCar(1);
		}
	}
}
