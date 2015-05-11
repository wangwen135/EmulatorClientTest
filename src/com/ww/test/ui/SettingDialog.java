package com.ww.test.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SettingDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txt_server;
	private JTextField txt_port;
	private MainFrame mainFrame;
	private JTextField txt_thinkTime;
	private JTextField txt_version;

	public void showDialog() {
		txt_server.setText(mainFrame.getHost());
		txt_port.setText(mainFrame.getPort() + "");
		txt_thinkTime.setText(mainFrame.getThinkTime() + "");
		txt_version.setText(mainFrame.getServerVersion());

		setVisible(true);
	}

	/**
	 * Create the dialog.
	 */
	public SettingDialog(MainFrame frame) {
		super(frame);
		setTitle("全局设置");
		
		this.mainFrame = frame;

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setBounds(100, 100, 446, 266);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel label = new JLabel("服务器地址:");
		label.setBounds(32, 31, 94, 15);
		contentPanel.add(label);

		JLabel lblNewLabel = new JLabel("端    口：");
		lblNewLabel.setBounds(32, 65, 94, 15);
		contentPanel.add(lblNewLabel);

		txt_server = new JTextField();
		txt_server.setText("127.0.0.1");
		txt_server.setBounds(122, 28, 128, 21);
		contentPanel.add(txt_server);
		txt_server.setColumns(10);

		txt_port = new JTextField();
		txt_port.setText("8080");
		txt_port.setBounds(122, 62, 66, 21);
		contentPanel.add(txt_port);
		txt_port.setColumns(10);

		JLabel label_1 = new JLabel("思考时间：");
		label_1.setBounds(32, 139, 94, 15);
		contentPanel.add(label_1);

		txt_thinkTime = new JTextField();
		txt_thinkTime.setText("0");
		txt_thinkTime.setColumns(10);
		txt_thinkTime.setBounds(122, 136, 66, 21);
		contentPanel.add(txt_thinkTime);

		JLabel label_2 = new JLabel("服务端版本：");
		label_2.setBounds(32, 96, 94, 15);
		contentPanel.add(label_2);

		txt_version = new JTextField();
		txt_version.setText("V1.8");
		txt_version.setColumns(10);
		txt_version.setBounds(122, 93, 66, 21);
		contentPanel.add(txt_version);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String text = txt_server.getText();
						String portStr = txt_port.getText();
						String ttStr = txt_thinkTime.getText();
						String vers = txt_version.getText();

						// 不校验了
						mainFrame.setHost(text);
						mainFrame.setPort(Integer.valueOf(portStr));
						mainFrame.setThinkTime(Long.valueOf(ttStr));
						mainFrame.setServerVersion(vers);

						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
