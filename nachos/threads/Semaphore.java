package nachos.threads;

import nachos.machine.*;

/**
 * A <tt>Semaphore</tt> is a synchronization primitive with an unsigned value.
 * A semaphore has only two operations:
 *
 * <ul>
 * <li><tt>P()</tt>: waits until the semaphore's value is greater than zero,
 * then decrements it.
 * <li><tt>V()</tt>: increments the semaphore's value, and wakes up one thread
 * waiting in <tt>P()</tt> if possible.
 * </ul>
 *
 * <p>
 * Note that this API does not allow a thread to read the value of the
 * semaphore directly. Even if you did read the value, the only thing you would
 * know is what the value used to be. You don't know what the value is now,
 * because by the time you get the value, a context switch might have occurred,
 * and some other thread might have called <tt>P()</tt> or <tt>V()</tt>, so the
 * true value might now be different.
 */
public class Semaphore {
	/**
	 * Allocate a new semaphore.
	 *
	 * @param	initialValue	the initial value of this semaphore.
	 */
	public Semaphore(int initialValue) {
		value = initialValue;
	}

	/**
	 * Atomically wait for this semaphore to become non-zero and decrement it.
	 */
	public void P() {
		boolean intStatus = Machine.interrupt().disable();

		if (value == 0) {
			waitQueue.waitForAccess(KThread.currentThread());
			KThread.sleep();
		}
		else {
			value--;
		}

		Machine.interrupt().restore(intStatus);
	}

	/**
	 * Atomically increment this semaphore and wake up at most one other thread
	 * sleeping on this semaphore.
	 */
	public void V() {
		boolean intStatus = Machine.interrupt().disable();

		KThread thread = waitQueue.nextThread();
		if (thread != null) {
			thread.ready();
		}
		else {
			value++;
		}
		Machine.interrupt().restore(intStatus);
	}

	private static class PingTest implements Runnable {
		PingTest(Semaphore ping, Semaphore pong) {
			this.ping = ping;
			this.pong = pong;
		}

		public void run() {
			for (int i=0; i<10; i++) {
				ping.P();
				pong.V();
			}
		}
		//LOCK MECHANICS
		//lock array mimics link list
		//get key of target
		//enter/convert hashKey into lockArray
		//if null, then
		//	lock bucket
		//
		//
		//
		//if !nulllookup by target.getKey
		//	if key exists and write operation
		//		lock tail
		//		update tail.next
		//		create real next/future tail
		//	if key exists and delete operation
		//		same as above, find tail, lock it
		//	if key exists and read operation
		//		
		//create lock object: 3 names/int, pred, target, tail
		//lock bucket
		//lock target
		//lock pred
		//lock
		//	int key
		//  int value
		//	node next


		//Bucket_lock array
		//any write, delete operation first locks the array,
		//then
		//	locks pred, target key, and next
		//	releases bucket_lock
		//(concurrent requests may now look through the bucket to see if they may interact on other values
		//	write/delete completes
		//if delete: identify target key
		//get temp location of corresponding lock
		//delete target key
		//remove corresponding lock from lock array
		//remove lock on 

		private Semaphore ping;
		private Semaphore pong;
	}

	/**
	 * Test if this module is working.
	 */
	public static void selfTest() {
		Semaphore ping = new Semaphore(0);
		Semaphore pong = new Semaphore(0);

		new KThread(new PingTest(ping, pong)).setName("ping").fork();

		for (int i=0; i<10; i++) {
			ping.V();
			pong.P();
		}
	}

	private int value;
	private ThreadQueue waitQueue =
			ThreadedKernel.scheduler.newThreadQueue(false);
}
