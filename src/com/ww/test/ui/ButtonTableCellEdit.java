package com.ww.test.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.ww.test.client.EmulatorClient;

public class ButtonTableCellEdit extends AbstractCellEditor implements
		TableCellEditor {

	private static final long serialVersionUID = 1L;

	private List<EmulatorClient> eList;
	private EmulatorClient client;
	private String text;
	private JButton button;

	public ButtonTableCellEdit(String t, List<EmulatorClient> eList) {
		this.eList = eList;
		this.text = t;
		button = new JButton(t);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if("启动".equals(text)){
					client.startClient();
				}else if("停止".equals(text)){
					client.stopClient();
				}
				
				stopCellEditing();
			}
		});
	}

	@Override
	public Object getCellEditorValue() {
		// 无需返回值
		return null;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		client = eList.get(table.convertRowIndexToModel(row));
		
		button.setEnabled((Boolean) value);

		return button;
	}
	
	

}
