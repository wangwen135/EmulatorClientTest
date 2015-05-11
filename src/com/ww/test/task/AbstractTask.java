package com.ww.test.task;

public abstract class AbstractTask implements ITask {

	@Override
	public String getTimeDesc() {
		return null;
	}

	@Override
	public String getNetDesc() {
		return null;
	}

	@Override
	public ITask getTask() {
		// 返回自己
		return this;
	}

}
