package com.ww.test.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.ww.test.client.EmulatorClient;
import com.ww.test.monitor.Coordinate;
import com.ww.test.monitor.MonitorPanel;
import com.ww.test.task.ITask;
import com.ww.test.task.TaskManage;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainFrame extends JFrame {

	private static Logger logger = LoggerFactory.getLogger(MainFrame.class);

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
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
	public MainFrame() {
		setTitle("模拟CMSP客户端进行操作");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 655, 585);

		addMenuBar();

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "\u76D1\u63A7\u4FE1\u606F",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel.add(tabbedPane, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("总结果", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));

		JPanel panel_总监控面板 = new JPanel();
		panel_2.add(panel_总监控面板, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "\u5BA2\u6237\u7AEF",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		panel_1.setPreferredSize(new Dimension(10, 200));
		contentPane.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4, BorderLayout.WEST);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[] { 117, 0 };
		gbl_panel_4.rowHeights = new int[] { 23, 0, 0, 23, 0, 0 };
		gbl_panel_4.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panel_4.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		panel_4.setLayout(gbl_panel_4);

		JButton btn_addClient = new JButton("添加客户端");
		btn_addClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addClient();
			}
		});
		GridBagConstraints gbc_btn_addClient = new GridBagConstraints();
		gbc_btn_addClient.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn_addClient.insets = new Insets(0, 0, 5, 0);
		gbc_btn_addClient.gridx = 0;
		gbc_btn_addClient.gridy = 0;
		panel_4.add(btn_addClient, gbc_btn_addClient);

		JButton button = new JButton("全部启动");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startAllClient();
			}
		});
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 5, 0);
		gbc_button.gridx = 0;
		gbc_button.gridy = 1;
		panel_4.add(button, gbc_button);

		JButton button_1 = new JButton("全部停止");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopAllClient();
			}
		});
		GridBagConstraints gbc_button_1 = new GridBagConstraints();
		gbc_button_1.insets = new Insets(0, 0, 5, 0);
		gbc_button_1.gridx = 0;
		gbc_button_1.gridy = 2;
		panel_4.add(button_1, gbc_button_1);

		JButton btnNewButton = new JButton("清空全部客户端");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAllClient();
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 3;
		panel_4.add(btnNewButton, gbc_btnNewButton);

		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		model = new ClientTableModel(eList);
		table.setModel(model);

		table.getColumn("启动")
				.setCellRenderer(new ButtonTableCellRenderer("启动"));
		table.getColumn("启动").setCellEditor(
				new ButtonTableCellEdit("启动", eList));

		table.getColumn("停止")
				.setCellRenderer(new ButtonTableCellRenderer("停止"));
		table.getColumn("停止").setCellEditor(
				new ButtonTableCellEdit("停止", eList));

		scrollPane.setViewportView(table);

		init();

		// 启动绘图线程
		startDrawImageThread();
		// 表格刷新线程
		startTableRefreshThread();
	}

	private void init() {
		TaskManage.registerAll();
		//初始默认任务
		taskList = TaskManage.getDefaultTaskList();

	}

	private void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnS = new JMenu("设置");
		menuBar.add(mnS);

		JMenuItem mntmCmsp = new JMenuItem("全局设置");
		mnS.add(mntmCmsp);

		mntmCmsp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new SettingDialog(MainFrame.this).showDialog();

			}
		});

		JMenu mnE = new JMenu("任务设置");
		menuBar.add(mnE);

		JMenuItem menuItem = new JMenuItem("全局任务设置");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new TaskSettingDialog(MainFrame.this).showDialog();
			}
		});
		mnE.add(menuItem);
	}

	/**
	 * <pre>
	 * 懒得通知了,直接定时刷新
	 * </pre>
	 */
	private void startTableRefreshThread() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					model.fireTableDataChanged();
				}

			}
		}).start();
	}

	/**
	 * <pre>
	 * 启动绘图线程
	 * 定时刷新吧
	 * </pre>
	 */
	private void startDrawImageThread() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					// 每秒绘制一次
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					try {

						int index = tabbedPane.getSelectedIndex();
						if (index == -1) {
							return;
						}
						// 主面板绘制一个平均值
						// 每秒计算一次平均值，反应总响应时间的变化

						if (index == 0) {
							// 绘制主面板

						} else {
							// 绘制线程面板
							EmulatorClient client = eList.get(index - 1);
							MonitorPanel panel = mList.get(index - 1);
							List<Coordinate> list = client.getListResult();
							panel.setCoordinateList(list);
							panel.drawImage();
						}

					} catch (Exception e) {
						logger.error("绘图错误", e);
					}
				}
			}
		});
		t.start();
	}

	/**
	 * <pre>
	 * 添加一个客户端
	 * </pre>
	 */
	private void addClient() {
		nameAsceend++;
		EmulatorClient client = new EmulatorClient("客户端" + nameAsceend, host,
				port);
		client.setServerVersion(serverVersion);
		client.setThinkTime(thinkTime);
		//设置任务
		List<ITask> tmp;
		
		tmp = new ArrayList<ITask>();
		for (ITask iTask : taskList) {
			tmp.add(iTask.getTask());//构建一个新的
		}
		client.setTaskList(tmp);
		

		// 面板的数据需要之后动态添加
		MonitorPanel mPanel = new MonitorPanel("客户端" + nameAsceend
				+ " 每次任务耗时 (MS)");

		// 保存到集合中
		eList.add(client);
		mList.add(mPanel);

		// 添加一个面板

		JPanel _panel = new JPanel();

		tabbedPane.addTab(client.getClientName(), null, _panel, null);

		_panel.setLayout(new BorderLayout(0, 0));

		_panel.add(mPanel, BorderLayout.CENTER);

		model.fireTableDataChanged();
	}

	/**
	 * <pre>
	 * 全部启动
	 * </pre>
	 */
	private void startAllClient() {
		for (EmulatorClient client : eList) {
			if (!client.isStart() || client.isStoped()) {
				client.startClient();
			}
		}
	}

	/**
	 * <pre>
	 * 全部停止
	 * </pre>
	 */
	private void stopAllClient() {
		for (EmulatorClient client : eList) {
			client.stopClient();
		}
	}

	private void clearAllClient() {
		stopAllClient();
		eList.clear();
		mList.clear();
		for (; tabbedPane.getTabCount() > 1;) {
			tabbedPane.remove(1);
		}
		nameAsceend = 0;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
		// 更新所有已经添加的客户端
		for (EmulatorClient client : eList) {
			client.setHost(host);
		}
	}

	public int getPort() {
		return port;

	}

	public void setPort(int port) {
		this.port = port;
		// 更新所有已经添加的客户端
		for (EmulatorClient client : eList) {
			client.setPort(port);
		}
	}

	public long getThinkTime() {
		return thinkTime;
	}

	public void setThinkTime(long thinkTime) {
		this.thinkTime = thinkTime;

		// 更新所有已经添加的客户端
		for (EmulatorClient client : eList) {
			client.setThinkTime(thinkTime);
		}
	}

	public String getServerVersion() {
		return serverVersion;
	}

	public void setServerVersion(String serverVersion) {
		this.serverVersion = serverVersion;

		// 更新所有已经添加的客户端
		for (EmulatorClient client : eList) {
			client.setServerVersion(serverVersion);
		}
	}

	public List<ITask> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<ITask> taskList) {
		this.taskList = taskList;

		// 重新设置每个客户端的任务
		List<ITask> tmp;
		for (EmulatorClient client : eList) {
			tmp = new ArrayList<ITask>();
			for (ITask iTask : taskList) {
				tmp.add(iTask.getTask());
			}
			client.setTaskList(tmp);
		}
	}

	private int nameAsceend = 0;
	private List<EmulatorClient> eList = new ArrayList<EmulatorClient>();
	private List<MonitorPanel> mList = new ArrayList<MonitorPanel>();
	private JTabbedPane tabbedPane;
	private ClientTableModel model;
	// 全局的
	// 服务器地址
	private String host = "10.0.75.2";
	// 服务器端口
	private int port = 8989;
	// 思考时间
	private long thinkTime = 0;

	private String serverVersion = "V3.1.SP1";

	private List<ITask> taskList;

}
