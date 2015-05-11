package com.ww.test.task.impl;

import com.sf.module.baseFile.goodsCode.action.IGoodsCodeAction;
import com.sf.remoting.server.core.rmi.RMIProxy;
import com.ww.test.task.AbstractTask;

public class GoodsCodeTask extends AbstractTask {

	@Override
	public String getTaskName() {
		return "获取所有的商品编码";
	}

	@Override
	public void doing(RMIProxy proxy) {
		IGoodsCodeAction action5 = proxy.getProxy(IGoodsCodeAction.class);
		action5.findAll();
	}

}
