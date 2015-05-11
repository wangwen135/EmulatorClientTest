package com.ww.test.task.impl;

import com.sf.module.baseFile.customtype.action.ICustomTypeAction;
import com.sf.remoting.server.core.rmi.RMIProxy;
import com.ww.test.task.AbstractTask;

public class CustomTypeTask extends AbstractTask{

	
	@Override
	public String getTaskName() {
		return "根据报关部门三字代码{SZX}和进出口标示{I}统计最大价值";
	}

	@Override
	public void doing(RMIProxy proxy) {
		ICustomTypeAction action = proxy.getProxy(ICustomTypeAction.class);
		action.countByCusDeptCode("SZX", "I");
	}

}
