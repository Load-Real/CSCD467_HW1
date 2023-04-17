package ldemershw1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class myHW1 extends JFrame implements KeyListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea output;
	private Thread displayer, mainWindow;
	private String msg = "";
	
	public myHW1(String name) {
		super(name);
		
		//javax.swing Setup
		output = new JTextArea(20, 30);
		DefaultCaret caret = (DefaultCaret)output.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		add(new JScrollPane(output));
		setSize(500, 500);
		setVisible(true);
		
		displayer = new Thread(new Displayer(output, msg));
		
		mainWindow = new Thread(new mainWindow());
		
		//KeyListener Implementation
		output.addKeyListener(this);
		
		displayer.start();
		mainWindow.start();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		if (msg.equalsIgnoreCase("exit")) {
			System.exit(0);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (displayer != null && displayer.isAlive()) {
			displayer.interrupt();
			try {
				displayer.join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		if (!(e.getKeyCode() == KeyEvent.VK_SHIFT)) {
			msg += e.getKeyChar();
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			displayer = new Thread(new Displayer(output, msg));
			displayer.start();
			msg = "";
		}
		
		
		System.out.println("msg = " + msg);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		//unused, but required to implement
	}
	
	public static void main(String[] args) {
		myHW1 input = new myHW1("CSCD467 Assignment1");
		input.addWindowListener(
				new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
	}
}

class Displayer implements Runnable {
	private JTextArea output;
	String msg, msgStorage;
	
	public Displayer(JTextArea output, String msg) {
		super();
		this.output = output;
		this.msg = msg;
	}
	
	@Override
	public void run() {
		msgStorage = msg;
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return;
			}
			output.append(msgStorage);
		}
	}
}

class mainWindow implements Runnable {
	@Override
	public void run() {
		for (int i = 0; i < Integer.MAX_VALUE; i++) {}
	}
}
