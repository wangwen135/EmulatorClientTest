package com.ww.test.monitor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MonitorPanelTest extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private List<Coordinate> list;
	private MonitorPanel panel;
	private JButton btnNewButton_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MonitorPanelTest frame = new MonitorPanelTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MonitorPanelTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 644, 410);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		list = new ArrayList<Coordinate>();
		panel = new MonitorPanel(list);
		contentPane.add(panel, BorderLayout.CENTER);

		panel.setGraphColor(Color.YELLOW);
		// panel.setLineColor(new Color(0,100,0));
		// panel.setBackgroundColor(Color.BLACK);

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("启  动");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				start();
			}
		});
		panel_1.add(btnNewButton);

		btnNewButton_1 = new JButton("停  止");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				run = false;
			}
		});
		panel_1.add(btnNewButton_1);
	}

	private boolean run = true;

	private void start() {
		run = true;

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				int rint = 100;
				int i = 0;
				while (run) {
					i++;
					try {
						Thread.sleep(rint + 900);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Random r = new Random();
					rint = r.nextInt(100);
					System.out.println(rint);
					list.add(new Coordinate(System.currentTimeMillis(), rint
							+ i));
					panel.drawImage();
				}
			}
		});
		t.start();
	}
}
