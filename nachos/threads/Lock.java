package nachos.threads;

import nachos.machine.*;

/**
 * A <tt>Lock</tt> is a synchronization primitive that has two states,
 * <i>busy</i> and <i>free</i>. There are only two operations allowed on a
 * lock:
 *
 * <ul>
 * <li><tt>acquire()</tt>: atomically wait until the lock is <i>free</i> and
 * then set it to <i>busy</i>.
 * <li><tt>release()</tt>: set the lock to be <i>free</i>, waking up one
 * waiting thread if possible.
 * </ul>
 *
 * <p>
 * Also, only the thread that acquired a lock may release it. As with
 * semaphores, the API does not allow you to read the lock state (because the
 * value could change immediately after you read it).
 */
public class Lock {
	/**
	 * Allocate a new lock. The lock will initially be <i>free</i>.
	 */
	private KThread lockHolder = null;
	private ThreadQueue waitQueue = ThreadedKernel.scheduler.newThreadQueue(true);

	public Lock() {
	}

	/**
	 * Atomically acquire this lock. The current thread must not already hold
	 * this lock.
	 */
	public void acquire() {
		//System.out.println("lockHolder: " + lockHolder);
		Lib.assertTrue(!isHeldByCurrentThread());
		//System.out.println("Lock -> acquire -> asserted true");
		boolean intStatus = Machine.interrupt().disable();
		KThread thread = KThread.currentThread();

		if (lockHolder != null) {
			//System.out.println("lockHolder != null");
			waitQueue.waitForAccess(thread);
			//System.out.println("Lock -> Sleep");
			KThread.sleep();
		}
		else {
			//System.out.println("lockHolder == null");
			waitQueue.acquire(thread);
			lockHolder = thread;
			//System.out.println("Lock -> else -> acquired");
		}

		Lib.assertTrue(lockHolder == thread);
		Machine.interrupt().restore(intStatus);
		//System.out.println("Lock -> acquired complete");
	}

	/**
	 * Atomically release this lock, allowing other threads to acquire it.
	 */
	public void release() {
		Lib.assertTrue(isHeldByCurrentThread());
		boolean intStatus = Machine.interrupt().disable();

		if ((lockHolder = waitQueue.nextThread()) != null)
			lockHolder.ready();
		Machine.interrupt().restore(intStatus);
	}

	/**
	 * Test if the current thread holds this lock.
	 *
	 * @return	true if the current thread holds this lock.
	 */
	public boolean isHeldByCurrentThread() {
		//System.out.println("Lock -> isHeldByCurrentThread");
		return (lockHolder == KThread.currentThread());
	}


}
