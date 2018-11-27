package nachos.threads;

public class HashTable_assignment2_cv{
	
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
	private int n_buckets = 2;
	private final int max_elements = 4;

	public HashTable_assignment2_cv(){ 
		hash_table = new Node[n_buckets];
		
		for (int i = 0; i < hash_table.length; i++){
			hash_table[i] = new Node();
		}
	}
	
	private void resetHashTable(int num_buckets) {
		hash_table = new Node[num_buckets];
		
		for (int i = 0; i < hash_table.length; i++){
			hash_table[i] = new Node();
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
			System.out.println("Bucket overloaded. Dynamic split!\n\n");
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
		System.out.println("INSERT " + k + ":" + val + " into bucket#" + hash_key);
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
		System.out.println("Dynamic split complete. New bucket count: " + getBucketSize() + "\n\n");
	}
	
	public int query(int k){ //check if element is in the hash table
		int hash_key = Hash(k);
		System.out.print("Searching bucket#" + hash_key + "\t");
		if(hash_table[hash_key].data != 0 && hash_table[hash_key].key != 0) {
			Node head = hash_table[hash_key];
			//System.out.println("TEST   k:v -> " + head.key + ":" + head.data);
			
			if(head.key == k) {
				System.out.println("QUERY k:v -> " + head.key + ":" + head.data);
				return head.data;
			}
			
			Node next = head.next;

			while(next != null && head.key != k) {
				
				head = next;
				next = head.next;
				if(head.key == k) {
					System.out.println("QUERY k:v -> " + head.key + ":" + head.data);
					return head.data;
				}
			}
			if(head.key == k) {
				System.out.println("QUERY k:v -> " + head.key + ":" + head.data);
				return head.data;
			}
			System.out.println("QUERY " + k + " not found.");
			//return value? or key
		} else {
			
			System.out.println("QUERY " + k + " not found.");
		} 
		return k;
	}
	
	public int getBucketSize(){
		
		return n_buckets; 
	}
	
	public static void selfTest() {
		HashTable_assignment2_semaphore HashTable = new HashTable_assignment2_semaphore();
		HashTable.insert(1, 2);
		HashTable.insert(2, 3);
		HashTable.insert(19, 5);
		HashTable.insert(21, 5);
		HashTable.insert(23, 5);
		HashTable.insert(25, 5);
		HashTable.insert(3, 22);
		HashTable.insert(4, 33);
		HashTable.insert(199, 55);
		HashTable.insert(211, 70);
		HashTable.insert(230, 5000);
		HashTable.insert(250, 4);
		HashTable.query(2);
		HashTable.query(19);
		HashTable.query(23);
		HashTable.query(5);
		HashTable.remove(19);
		HashTable.remove(2);
		HashTable.remove(29);
		HashTable.insert(23, 50);
	}

	enum OperationType{
		query,
		remove,
		insert
	}
	/*
	enum ThreadOperation {
		int k;
		OperationType; 
		int result;
	};
	*/
	
//	public void batch(int n_ops, ThreadOperation[] ops){
//		
//	}
	
	
}


