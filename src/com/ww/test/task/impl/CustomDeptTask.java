package com.ww.test.task.impl;

import com.sf.module.baseFile.customdept.action.ICustomDeptAction;
import com.sf.remoting.server.core.rmi.RMIProxy;
import com.ww.test.task.AbstractTask;

public class CustomDeptTask extends AbstractTask{

	@Override
	public String getTaskName() {
		return "查询所有报关部门";
	}

	@Override
	public void doing(RMIProxy proxy) {
		ICustomDeptAction action2 = proxy.getProxy(ICustomDeptAction.class);
		action2.findAll();
	}

}
