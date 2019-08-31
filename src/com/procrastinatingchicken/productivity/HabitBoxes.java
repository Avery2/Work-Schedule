package com.procrastinatingchicken.productivity; //so the dots DO do something... huh (learned in commandline)

import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class HabitBoxes {

	// TODO keep on adding features - but keep look and feel... try to hide the settings
	// TODO then make methods to create new and old habit boxes
	// TODO it won't change colors if you press down move ONE PIXEL and press up (change if want)
	// TODO Mouse released within bounds of box
	// TODO hover for more info
	// TODO Start date and end date so that hover gives info
	// TODO Maybe fix error where MyOption window takes focus
	// TODO cannot populate the dropdown menu in options with the data from Arraylist from control

	class HabitBox {

		private int width;
		private int height;
		private int x;
		private int y;
		private boolean show;
		private boolean filled;

		HabitBox() {
			show = true;
			filled = false;
		}

		HabitBox(int x, int y, int width, int height) {
			show = true;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		HabitBox(int x, int y, int width, int height, boolean show, boolean filled) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.show = true;
			this.filled = false;
		}

		public void drawMe() {
//			System.out.println("drawMe");
			if (show) {
				if (filled) {
					MyGui.rectangleFilled(x, y, width, height);
				} else {
					MyGui.rectangle(x, y, width, height);
//					MyGui.rectangle(x-width/2, y-height/2, width, height);
				}
			}
		}

		public String toString() {
			return "habit box:" + "x=" + x + " y=" + y + " width=" + width + " height=" + height + " show=" + show
					+ " filled=" + filled;
		}

		/*
		 * Getters and setters
		 */

		public void setX(int x) {
			this.x = x;
		}

		public void setY(int y) {
			this.y = y;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public void setShow(boolean show) {
			this.show = show;
		}

		public void setFilled(boolean filled) {
			this.filled = filled;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		public boolean getShow() {
			return show;
		}

		public boolean getFilled() {
			return filled;
		}
	}

	class Control {
		volatile ArrayList<String> fileNames = new ArrayList<String>();
		volatile int selected;

		public void printFileNames() {
			fileNames.forEach(x -> System.out.println(x));
		}
	}

	private Control control = new Control();

	class MyOptions implements Runnable {

		@Override
		public void run() {
			System.out.println("Option Thread Created");
			JFrame optionFrame = new JFrame();
			optionFrame.setBounds(600, 0, 150, 150);
			optionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			optionFrame.setLayout(new FlowLayout());

			System.out.println("Starting to populate combo box array...");
			// wait so that the files can be read in
			// TODO Maybe you want some better thread handling such as waiting for a flag that tells that it is done reading in from the files
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			String[] arr = new String[control.fileNames.size()];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = control.fileNames.get(i);
//				System.out.println(i + ": " + arr[i]);
			}
			JComboBox<String> comboBox = new JComboBox<String>(control.fileNames.toArray(arr)); // TODO this is broken
			System.out.println("Combo box populated.");

			optionFrame.getContentPane().add(comboBox);
			JLabel label = new JLabel(comboBox.getItemAt(comboBox.getSelectedIndex()));
			optionFrame.getContentPane().add(label);
//			optionFrame.pack();

			while (true) {
				control.selected = comboBox.getSelectedIndex();
				label.setText(comboBox.getItemAt(comboBox.getSelectedIndex()));
				optionFrame.setVisible(true);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				control.fileNames = getStringArrayList(comboBox);
			}
		}

		// TODO add new file button here
		// TODO for some reason, Swing isn't thread safe... see their documentation on this.

		private ArrayList<String> getStringArrayList(JComboBox<String> cb) {
			ArrayList<String> arr = new ArrayList<String>();
			for (int i = 0; i < arr.size(); i++) {
				arr.add(cb.getItemAt(i));
			}
			return arr;
		}

	}

	class MyBoxes implements Runnable {

		@Override
		public void run() {
			System.out.println("Boxes Thread Created");
			MyGui.init(500, 500);
			MyGui.enableDoubleBuffering();

			ArrayList<HabitBox> arr = new ArrayList<HabitBox>();

			ArrayList<String> boxes = new ArrayList<String>(); // TODO for threading, make this volitile in the correct place
//			boxes.add("test");
//			boxes.add("justcheck"); // 4/21/19
//			boxes.add("run"); // 4/21/19
//			boxes.add("code"); // 5/11/19
//			boxes.add("forest"); // 5/11/19

			try {
				String curFileName = "";
				FileReader init = new FileReader(new File("hbdfiles.txt"));

				int in;
				do {
					in = init.read();
					while (in != 10 && in != -1) {
						curFileName += (char) in;
//						System.out.println((char) in + ": " + in);
						in = init.read();
					}
					boxes.add(curFileName);
//					System.out.println("added " + curFileName);
					curFileName = "";
				} while (in != -1);

				init.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			control.fileNames = boxes;

			// TODO I need someplace outside of this code (probably a file) to store all the made hbd files or read through all files in the directory that is accessable and add it there

			while (true) {

				// TODO have option thread change this option
				String fileName = "./" + boxes.get(control.selected) + "_hbd.bin"; // _HabitBoxesData
				File file = new File(fileName);
				try {
					readHBD(arr, file);
				} catch (IOException e) {
					e.printStackTrace();
				}

				for (int i = 0; i < 6; i++) {
					for (int j = 0; j < 6; j++) {
						if ((((MyGui.mouseClicked()) && !arr.get(i * 6 + j).getFilled()
								&& (MyGui.mouseX() > 100 + 50 * i)) && (MyGui.mouseX() < 150 + 50 * i))
								&& ((MyGui.mouseY() > 100 + 50 * j) && (MyGui.mouseY() < 150 + 50 * j))) {
							arr.get(i * 6 + j).setFilled(true);
							MyGui.setMouseClicked(false);
						} else if ((((MyGui.mouseClicked()) && arr.get(i * 6 + j).getFilled()
								&& MyGui.mouseX() > 100 + 50 * i) && (MyGui.mouseX() < 150 + 50 * i))
								&& ((MyGui.mouseY() > 100 + 50 * j) && (MyGui.mouseY() < 150 + 50 * j))) {
							arr.get(i * 6 + j).setFilled(false);
							MyGui.setMouseClicked(false);
						}
						arr.get(i * 6 + j).drawMe();
					}
				}

				MyGui.show();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				MyGui.clear();

				try {
					writeHBD(arr, file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * Will fill a _hbd file with zeros.
		 * 
		 * @param file
		 * @throws IOException
		 */
		private void initHBD(File file) throws IOException {
			FileOutputStream writer = new FileOutputStream(file, false);
			for (int i = 0; i < 36; i++) {
				writer.write(0);
			}
			writer.close();
		}

		/**
		 * Takes values from file and puts into the HabitBoxes in the array of HabitBoxes
		 * 
		 * @param arr
		 * @param file
		 * @throws IOException
		 */
		void readHBD(ArrayList<HabitBox> arr, File file) throws IOException {
			FileInputStream reader = new FileInputStream(file);

			int foo;

			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 6; j++) {
					arr.add(new HabitBox((100 + 50 * i), (100 + 50 * j), 50, 50));
					foo = reader.read();
					if (foo == 1) {
						arr.get(i * 6 + j).setFilled(true);
					} else if (foo == 0) {
						arr.get(i * 6 + j).setFilled(false);
					} else if (foo == -1) {
						initHBD(file);
					} else {
						System.out.println("Something's broken");
					}
				}
			}
			reader.close();
		}

		/**
		 * Writes the states of the HabitBoxes in arrays into a file
		 * 
		 * @param arr
		 * @param file
		 * @throws IOException
		 */
		void writeHBD(ArrayList<HabitBox> arr, File file) throws IOException {
			// have to declare here because otherwise it won't clear the file
			FileOutputStream writer = new FileOutputStream(file, false);
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 6; j++) {
					if (arr.get(i * 6 + j).getFilled()) {
						writer.write(1);
					} else {
						writer.write(0);
					}
				}
			}
			writer.close();
		}

	}

	private void startThreads() {
		MyOptions options = new MyOptions();
		MyBoxes boxes = new MyBoxes();
		Thread t1 = new Thread(options);
		Thread t2 = new Thread(boxes);
		t1.start();
		t2.start();
	}

	public static void main(String[] args) throws IOException {
		HabitBoxes hb = new HabitBoxes();
		hb.startThreads();
	}
}