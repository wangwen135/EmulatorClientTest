package com.ww.test.task;

import java.util.ArrayList;
import java.util.List;

import com.ww.test.task.impl.CustomBatchTask;
import com.ww.test.task.impl.CustomDeptTask;
import com.ww.test.task.impl.CustomTypeTask;
import com.ww.test.task.impl.DataBlockTask;
import com.ww.test.task.impl.GoodsCodeTask;
import com.ww.test.task.impl.HeartbeatTask;

public class TaskManage {
	public static List<ITask> list = new ArrayList<ITask>();

	public static List<ITask> defaultList = new ArrayList<ITask>();

	public static void registerTask(ITask task) {
		list.add(task);
	}

	public static void registerTask(ITask task, boolean def) {
		list.add(task);
		if (def) {
			defaultList.add(task);
		}
	}

	public static List<ITask> getTaskList() {
		return list;
	}

	public static List<ITask> getDefaultTaskList() {
		return defaultList;
	}

	public static void registerAll() {
		registerTask(new CustomBatchTask(), true);
		registerTask(new CustomDeptTask(), true);
		registerTask(new CustomTypeTask(), true);
		registerTask(new HeartbeatTask(), true);
		registerTask(new GoodsCodeTask());

		// 。。。。
		registerTask(new DataBlockTask(1024, true), true);
		registerTask(new DataBlockTask(5120, true));
		registerTask(new DataBlockTask(10240, true));
		registerTask(new DataBlockTask(51200, true));
		registerTask(new DataBlockTask(102400, true));
		registerTask(new DataBlockTask(512000, true));
		registerTask(new DataBlockTask(1024000, true));
		registerTask(new DataBlockTask(5120000, true));
		registerTask(new DataBlockTask(10240000, true));

		registerTask(new DataBlockTask(1024, false), true);
		registerTask(new DataBlockTask(5120, false));
		registerTask(new DataBlockTask(10240, false));
		registerTask(new DataBlockTask(51200, false));
		registerTask(new DataBlockTask(102400, false));
		registerTask(new DataBlockTask(512000, false));
		registerTask(new DataBlockTask(1024000, false));
		registerTask(new DataBlockTask(5120000, false));
		registerTask(new DataBlockTask(10240000, false));

	}

}
