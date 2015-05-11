package com.ww.test.task.impl;

import com.sf.module.baseFile.custombatch.action.ICustomBatchAction;
import com.sf.remoting.server.core.rmi.RMIProxy;
import com.ww.test.task.AbstractTask;

public class CustomBatchTask extends AbstractTask {

	@Override
	public String getTaskName() {

		return "获取所有的报关批次";
	}

	@Override
	public void doing(RMIProxy proxy) {
		proxy.getProxy(ICustomBatchAction.class).findAll();

	}

}
