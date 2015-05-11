package com.ww.test.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.ww.test.task.ITask;
import com.ww.test.task.TaskManage;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TaskSettingDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	private MainFrame mainFrame;
	private List<JCheckBox> cboxList;
	private JPanel panel;

	public void showDialog() {
		List<ITask> allTask = TaskManage.getTaskList();

		List<ITask> checkTask = mainFrame.getTaskList();

		cboxList = new ArrayList<JCheckBox>();

		for (ITask iTask : allTask) {
			JCheckBox _ckbox = new JCheckBox(iTask.getTaskName());
			if (checkTask.contains(iTask)) {
				_ckbox.setSelected(true);
			}

			cboxList.add(_ckbox);

			panel.add(_ckbox);
		}

		setVisible(true);
	}

	/**
	 * Create the dialog.
	 */
	public TaskSettingDialog(MainFrame frame) {
		super(frame);

		setTitle("全局任务设置");

		this.mainFrame = frame;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				panel = new JPanel();
				scrollPane.setViewportView(panel);
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						List<ITask> newTask = new ArrayList<ITask>();
						List<ITask> allTask = TaskManage.getTaskList();

						for (int i = 0; i < cboxList.size(); i++) {
							if (cboxList.get(i).isSelected()) {
								newTask.add(allTask.get(i));
							}
						}

						mainFrame.setTaskList(newTask);
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
