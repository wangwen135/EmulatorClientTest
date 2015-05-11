package com.ww.test.task.impl;

import com.sf.module.wwhTest.action.IWwhTestAction;
import com.sf.remoting.server.core.rmi.RMIProxy;
import com.ww.test.task.ITask;

public class DataBlockTask implements ITask {

	private int size;
	private boolean getNewBlock;

	public DataBlockTask(int size, boolean newBlock) {

		this.size = size;

		this.getNewBlock = newBlock;
	}

	@Override
	public String getTaskName() {
		if (getNewBlock) {
			return "★每次返回新的数据块，大小：" + size;
		} else {
			return "每次返回重复的数据块，大小：" + size;
		}

	}

	@Override
	public String getTimeDesc() {
		return "与size【" + size + "】成比例";
	}

	@Override
	public String getNetDesc() {
		return "与size【" + size + "】成比例";
	}

	@Override
	public ITask getTask() {
		return new DataBlockTask(size, getNewBlock);
	}

	@Override
	public void doing(RMIProxy proxy) {
		IWwhTestAction action6 = proxy.getProxy(IWwhTestAction.class);

		if (getNewBlock) {
			action6.getNewDataBlock(size);
		} else {
			action6.getDataBlock(size);
		}

	}

}
