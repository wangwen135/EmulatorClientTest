package com.ww.test.task;

import com.sf.remoting.server.core.rmi.RMIProxy;

/**
 * <pre>
 * 任务接口
 * 用于指定模拟的客户端做什么操作
 * </pre>
 */
public interface ITask {
	
	/**
	 * <pre>
	 * 返回一个描述该任务的名字
	 * </pre>
	 * 
	 * @return
	 */
	String getTaskName();

	/**
	 * <pre>
	 * 获取时间描述
	 * </pre>
	 * 
	 * @return
	 */
	String getTimeDesc();

	/**
	 * <pre>
	 * 获取网络描述
	 * </pre>
	 * 
	 * @return
	 */
	String getNetDesc();

	/**
	 * <pre>
	 * 如果这个bean是有状态需要重写这个方法
	 * </pre>
	 * 
	 * @return
	 */
	ITask getTask();

	/**
	 * <pre>
	 * 做某个任务
	 * 从proxy 中获取RMI Action，然后调用方法
	 * </pre>
	 * 
	 * @param proxy
	 *            只能从这个Prox中获取Action对象
	 */
	void doing(RMIProxy proxy);

}
