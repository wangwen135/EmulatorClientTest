package com.ww.test.ui;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ButtonTableCellRenderer implements TableCellRenderer {

	private JButton button;

	public ButtonTableCellRenderer(String text) {
		button = new JButton(text);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		button.setEnabled((Boolean) value);

		return button;
	}

}
