package com.procrastinatingchicken.productivity;

public class MultiThreading {

	class Control {
		public volatile boolean foo = true;
	}

	final Control ctrl = new Control();

	class T1 implements Runnable {
		@Override
		public void run() {
			while (true) {
				System.out.println("foo: " + ctrl.foo);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class T2 implements Runnable {
		@Override
		public void run() {
			while (true) {
				ctrl.foo = false;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ctrl.foo = true;
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void test() {
		T1 print = new T1();
		T2 change = new T2();
		Thread t1 = new Thread(print);
		Thread t2 = new Thread(change);
		t1.start();
		t2.start();
	}

	public static void main(String[] args) {
		MultiThreading test = new MultiThreading();
		
//		test.test();
		
		MultiThreading.T1 print = test.new T1();
		MultiThreading.T2 change = test.new T2();
		Thread t1 = new Thread(print);
		Thread t2 = new Thread(change);
		t1.start();
		t2.start();
	}
}
