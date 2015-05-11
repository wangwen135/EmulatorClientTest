package com.ww.test.task.impl;

import com.sf.module.baseFile.areaManage.action.IAreaManageAction;
import com.sf.remoting.server.core.rmi.RMIProxy;
import com.ww.test.task.AbstractTask;

public class HeartbeatTask extends AbstractTask {

	@Override
	public String getTaskName() {
		return "心跳==空操作";
	}

	@Override
	public void doing(RMIProxy proxy) {
		IAreaManageAction action3 = proxy.getProxy(IAreaManageAction.class);
		action3.heartBitServer();
	}

}
