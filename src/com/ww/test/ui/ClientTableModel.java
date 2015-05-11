package com.ww.test.ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.ww.test.client.EmulatorClient;

public class ClientTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] columns = new String[] { "客户端", "运行状态", "启动", "停止",
			"完成任务次数" };
	private List<EmulatorClient> eList;

	public ClientTableModel(List<EmulatorClient> eList) {
		this.eList = eList;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		EmulatorClient client = eList.get(rowIndex);
		boolean runing = client.isRuning();
		boolean stoped = client.isStoped();
		Object value = null;
		switch (columnIndex) {
		case 0:
			value = client.getClientName();
			break;

		case 1:
			if (!client.isStart()) {
				value = "未启动";
				break;
			}
			// 这种状态变化得通过监听的形式实现

			if (runing) {
				value = "正在运行";
			} else if (stoped) {
				value = "已经停止";
			} else if (!runing && !stoped) {
				value = "正在停止";
			}

			break;
		case 2:
			// 启动

			if (!client.isStart()) {
				value = true;
				break;
			}
			// 这种状态变化得通过监听的形式实现
			if (runing) {
				value = false;
			} else if (stoped) {
				value = true;
			} else if (!runing && !stoped) {
				value = false;
			}
			break;
		case 3:
			// 停止

			if (!client.isStart()) {
				value = false;
				break;
			}
			// 这种状态变化得通过监听的形式实现
			if (runing) {
				value = true;
			} else if (stoped) {
				value = false;
			} else if (!runing && !stoped) {
				value = false;
			}
			break;

		case 4:
			value = client.getRunCount();
			break;

		default:
			value = "什么情况？";
			break;
		}
		return value;
	}

	@Override
	public int getRowCount() {
		return eList.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public String getColumnName(int column) {

		return columns[column];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 2 || columnIndex == 3) {
			return true;
		}

		return false;
	}
}
