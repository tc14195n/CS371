package nachos.threads;

//import nachos.threads.HashTable_assignment2_semaphore.Node;
//import nachos.threads.HashTable_assignment2_semaphore.OperationType;

public class HashTable{
	
	//public class LinkedList{
		
		public class Node {
			public int data;
			public int key;
			public Node next;
			//public Node prev;
			
			//public Node(Node prev, int data, int key, Node next){
			public Node(int data,  int key, Node next) {
				this.data = data;
				this.next = next;
				//this.prev = null;
				this.key = key;
			}
			public Node(){
			}

		} 
		//write method to add a node 
//}	
		//write method to remove a node
	

	
	public static Node[] hash_table; 

	//replace below lock[] with semaphore[]
	//use lock_list[hash_key].v() when a write/read action is finished in method
	public static Semaphore[] lock_list;
	//public static Lock[] lock_list;
	private int n_buckets = 2;
	private final int max_elements = 4;

	public HashTable(){ 
		hash_table = new Node[n_buckets];
		lock_list = new Semaphore[n_buckets];
		
		for (int i = 0; i < hash_table.length; i++){
			hash_table[i] = new Node();
			lock_list[i] = new Semaphore(0);
		}
	}
	public HashTable(int buckets){ 
		this.n_buckets = buckets;
		hash_table = new Node[n_buckets];
		lock_list = new Semaphore[n_buckets];
		
		for (int i = 0; i < hash_table.length; i++){
			hash_table[i] = new Node();
			lock_list[i] = new Semaphore(0);
		}
	}
	
	private void resetHashTable(int num_buckets) {
		hash_table = new Node[num_buckets];
		lock_list = new Semaphore[num_buckets];
		
		for (int i = 0; i < hash_table.length; i++){
			hash_table[i] = new Node();
			lock_list[i] = new Semaphore(0);
		}
	}
	
	 
	//f(e) = e%10 -- function to use for insertion of elements 
	
	//insert
	public int Hash(int k) {
		//what if it's 0?
		int hash_key = k % (n_buckets);
		return hash_key;
	}
	
	public void insert(int k, int val){

		int node_count = 0;
		
		int hash_key = Hash(k);
		//acquire lock here?
		
		//System.out.println("INSERT " + k + ":" + val + " into bucket#" + hash_key);
		if(hash_table[hash_key].data != 0 && hash_table[hash_key].key != 0) {
			Node head = hash_table[hash_key];
			if(head.key == k) {
				//System.out.println("INSERT failed. Duplicate key.");
			}
			node_count++;
			Node next = head.next; 
			/*if(next == null) {
				next = new Node(val, k, null);
			}*/
			while(next != null) {
				node_count++;
				//System.out.println("node_count in " + hash_key + ": " + node_count);
				head = next;
				if(head.key == k) {
					//System.out.println("INSERT failed. Duplicate key.");
				}
				next = head.next;
			}
			head.next = new Node(val, k, null);
			node_count++;
			//System.out.println("node_count:" + node_count);
		} else {
			
			
			hash_table[hash_key] = new Node(val, k, null);
			node_count++;
			//System.out.println("Initialized first value of LinkedList.");
		} 
		if(node_count > max_elements) {
			//System.out.println("Bucket overloaded. Dynamic split!");
			dynamicSplit();
			//insert code to double n_buckets, create temporary linkedList
			//re-insert all values into new hashtable
		}

		
		//use linked list add method to add a node passing k and val
		//bucket is index value of array 
	}
	
	public void insertDynamic(int k, int val){
		int node_count = 0;
		int hash_key = Hash(k);
		//System.out.println("INSERT " + k + ":" + val + " into bucket#" + hash_key);
		if(hash_table[hash_key].data != 0 && hash_table[hash_key].key != 0) {
			Node head = hash_table[hash_key];
			//hash_table[hash_key];
			node_count++;
			Node next = head.next;
			while(next != null) {
				node_count++;
				head = next;
				next = head.next;
			}
			head.next = new Node(val, k, null);
		} else {
			
			
			hash_table[hash_key] = new Node(val, k, null);
			//System.out.println("Initialized first value of LinkedList.");
		} 

		
		//use linked list add method to add a node passing k and val
		//bucket is index value of array 
	}
	
	public void remove(int k){
		int hash_key = Hash(k);
		if(hash_table[hash_key] != null) {
			Node head = hash_table[hash_key];
			if(head.key == k) {
				hash_table[hash_key] = head.next;
				//System.out.println("REMOVED k:v -> " + k + ":" + head.data);
				return;
			}
			Node next = head.next;
			Node prev = new Node();
			while(next != null && head.key != k) {
				prev = head;
				head = next;
				next = head.next;
				if(head.key == k) {
					prev.next = next;
					//System.out.println("REMOVED k:v -> " + k + ":" + head.data);
					return;
				}
			}
			if(head.key == k) {
				prev.next = null;
				//System.out.println("REMOVED k:v -> " + k + ":" + head.data);
				return;
			}
			//System.out.println("REMOVE failed. " + k + " not found.");

		} else {
			
			//System.out.println("REMOVE failed. " + k + " not found.");
		} 
	}
	public void dynamicSplit() {
		n_buckets = getBucketSize() * 2;
		Node[] oldHashTable = hash_table;
		resetHashTable(n_buckets);
		
		for(int i = 0; i < oldHashTable.length; i++) {
			if(oldHashTable[i] != null) {
				Node head = oldHashTable[i];
				insertDynamic(head.key, head.data);
				while(head.next != null) {
					head = head.next;
					insertDynamic(head.key, head.data);
					//System.out.println("INSERT Node into new bucket.");
				}
			}
				
		}
		System.out.println("Dynamic split complete. New bucket count: " + getBucketSize() + "");
	}
	
	public int query(int k){ //check if element is in the hash table
		int hash_key = Hash(k);
		//System.out.print("Searching bucket#" + hash_key + "\t");
		if(hash_table[hash_key].data != 0 && hash_table[hash_key].key != 0) {
			Node head = hash_table[hash_key];
			//System.out.println("TEST   k:v -> " + head.key + ":" + head.data);
			
			if(head.key == k) {
				//System.out.println("QUERY k:v -> " + head.key + ":" + head.data);
				return head.data;
			}
			
			Node next = head.next;

			while(next != null && head.key != k) {
				
				head = next;
				next = head.next;
				if(head.key == k) {
					//System.out.println("QUERY k:v -> " + head.key + ":" + head.data);
					return head.data;
				}
			}
			if(head.key == k) {
				//System.out.println("QUERY k:v -> " + head.key + ":" + head.data);
				return head.data;
			}
			//System.out.println("QUERY " + k + " not found.");
			//return value? or key
		} else {
			
			//System.out.println("QUERY " + k + " not found.");
		} 
		return k;
	}
	
	public int getBucketSize(){
		
		return n_buckets; 
	}
	
	public static void selfTest() {
		HashTable Hash_Table = new HashTable();
		Hash_Table.insert(1, 2);
		Hash_Table.insert(2, 3);
		Hash_Table.insert(19, 5);
		Hash_Table.insert(21, 5);
		Hash_Table.insert(23, 5);
		Hash_Table.insert(25, 5);
		Hash_Table.insert(3, 22);
		Hash_Table.insert(4, 33);
		Hash_Table.insert(199, 55);
		Hash_Table.insert(211, 70);
		Hash_Table.insert(230, 5000);
		Hash_Table.insert(250, 4);
		Hash_Table.query(2);
		Hash_Table.query(19);
		Hash_Table.query(23);
		Hash_Table.query(5);
		Hash_Table.remove(19);
		Hash_Table.remove(2);
		Hash_Table.remove(29);
		Hash_Table.insert(23, 50);
		//ThreadOperation batch1 = new ThreadOperation(5, OperationType.query, 7);
		//batch1.k = 1;
		//batch
		/*
		{1, insert, 2}
		2, insert, 3
		19, insert, 5
		21, insert, 5*/
	}

	enum OperationType{
		query,
		remove,
		insert
	}
	
	
	public class ThreadOperation {
		int k;
		OperationType op; 
		int result;
		
		public ThreadOperation(int key, OperationType oper, int r) {
			k = key;
			op = oper; 
			result = r;
		}
	};
	/*
	ThreadOperation[] test_batch = new ThreadOperation;
	
	test_batch[0] = new ThreadOperation(5, Operation.query, 8);
	
	
	
	public void batch(int n_ops, ThreadOperation[] ops) {//(int n_ops, ThreadOperation[] ops//		
		for(int i = 0; i < n_ops; i++) {
			new KThread().setName("batch_thread" + i);
		}
	}
	*/
	
	
}


