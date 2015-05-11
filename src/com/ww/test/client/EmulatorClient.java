package com.ww.test.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sf.module.loginmgmt.action.IUserAction;
import com.sf.module.loginmgmt.domain.RemoteUser;
import com.sf.remoting.jserial.SerializerImpl;
import com.sf.remoting.server.core.context.UserContext;
import com.sf.remoting.server.core.rmi.RMIProxy;
import com.sf.remoting.transport.client.ClientTransporter;
import com.ww.test.monitor.Coordinate;
import com.ww.test.task.ITask;

/**
 * 模拟客户端
 * 
 */
public class EmulatorClient implements Runnable {

	private static Logger logger = LoggerFactory
			.getLogger(EmulatorClient.class);

	private String serverVersion = "V1.8";

	/**
	 * 服务器地址
	 */
	private String host;
	/**
	 * 端口
	 */
	private int port;

	/**
	 * 客户端名字 == 线程名字
	 */
	private String clientName;

	/**
	 * 登录名
	 */
	private String userName = "admin";

	/**
	 * 密码
	 */
	private String password = "123";

	/**
	 * 思考时间==完成一次任务后休眠多久
	 */
	private long thinkTime = 0;

	/**
	 * 记录日志
	 */
	private BufferedWriter logWrite;

	private SimpleDateFormat sdf = new SimpleDateFormat("MMdd-Hms");

	private SimpleDateFormat sdf2 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss:SSS");

	private ClientTransporter transporter;

	private SerializerImpl serializer;

	private RMIProxy proxy;

	private List<ITask> taskList = new ArrayList<ITask>();

	/**
	 * 默认本地8080端口
	 */
	public EmulatorClient(String name) {
		this(name, "127.0.0.1", 8080);
	}

	public EmulatorClient(String name, String server, int port) {
		this(name, server, port, "admin", "123");
	}

	public EmulatorClient(String name, String server, int port,
			String userName, String password) {
		this.clientName = name;
		this.host = server;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}

	/**
	 * <pre>
	 * 先初始化一下
	 * </pre>
	 */
	private void init() {
		Integer rmiTimeOut = 300;
		serializer = new SerializerImpl();

		// 由于在发送请求时会从UserContext 中取值，而之前设计的是单例的形式，所以需要重写这个类

		// if (UserContext.getContext() == null)
		// new UserClientContext();

		transporter = new ClientTransporter(host, port, serializer);

		if (rmiTimeOut != null)
			transporter.setTimeout(rmiTimeOut.intValue());

		// 每个线程独自保留RMIProxy实例
		proxy = RMIProxy.newInstance();
		proxy.setTransporter(transporter);

		// RMIProxy.getInstance().setTransporter(transporter);

		logger.info("客户端【" + clientName + "】初始完成");
	}

	/**
	 * <pre>
	 * 操作之前先做一个登录动作
	 * </pre>
	 */
	private void login() throws Exception {

		IUserAction action = proxy.getProxy(IUserAction.class);
		RemoteUser remoteUser = action.login(userName, password, serverVersion);

		// 先不管这个
		/**
		 * <pre>
		 * RMIInvocationHandler 在包装请求时会从UserContext中取session
		 * 
		 *    request.setSession(UserContext.getSession());
		 *    request.setRequestId(requestIdSeq.getAndIncrement());
		 *    Class class1 = method.getDeclaringClass();
		 *    request.setBeanName(getBeanName(class1));
		 *    request.setMethod(method.getName());
		 *    request.setTimeout(getMethodTimeout(request.getBeanName(), request.getMethod(), class1));
		 *    request.setParams(aobj);
		 *    beforeInterceptor(request);
		 *    response = transporter.send(request);
		 *    checkResponse(response);
		 * </pre>
		 */
		// UserContext.getSession());

		// 设置到重写的类的线程局部变量中
		UserContext.setUserSession(remoteUser.getSession(),
				remoteUser.getUserName());

		/**
		 * <pre>
		 * // 原代码中的： 设置用户上下文
		 * UserContext.setUserSession(remoteUser.getSession(), remoteUser.getUserName());
		 * // user = new User();
		 * user.setUserName(remoteUser.getUserName());
		 * user.setPassWord(remoteUser.getPassword());
		 * user.setLdapPassword(remoteUser.getLdapPassword());
		 * user.setId((Long) remoteUser.getId());
		 * AppContext.getAppContext().putParameter(LoginManager.USER_KEY, user);
		 * AppContext.getAppContext().putParameter(Constants.REMOTEUSER, remoteUser);
		 * </pre>
		 */

		logger.info("客户端【" + clientName + "】登录动作完成");
	}

	// 控制参数
	private boolean start = false;
	private boolean runing = false;
	private boolean stoped = true;

	// 统计参数
	// private long count = 0;
	// private long allTime = 0;
	// private long maxTime = 0;

	// 弄一个定长的队列，保存每个节点的时间
	private int listMaxSize = 100000;
	// 保存结果
	private List<Coordinate> listCoordinate = new ArrayList<Coordinate>();
	/**
	 * 运行次数
	 */
	private long runCount = 0;

	private void addCoordinate(Coordinate coord) {
		runCount++;
		if (listCoordinate.size() >= listMaxSize) {
			// 应该会有并发的问题
			synchronized (this) {
				listCoordinate.remove(0);
			}

		}

		listCoordinate.add(coord);
	}

	/**
	 * <pre>
	 * 获取当前的结果集
	 * </pre>
	 * 
	 * @return
	 */
	public List<Coordinate> getListResult() {
		// 复制一个新的
		// 复制当前时间点上的长度--不考虑超不多时间点进来的数据
		List<Coordinate> rtList = new ArrayList<Coordinate>();
		synchronized (this) {
			int size = listCoordinate.size();
			for (int i = 0; i < size; i++) {
				rtList.add(listCoordinate.get(i));
			}
		}

		return rtList;
	}

	public long getRunCount() {
		return runCount;
	}

	/**
	 * <pre>
	 * 清空结果
	 * </pre>
	 */
	public void clearResult() {
		listCoordinate = new ArrayList<Coordinate>();
	}

	@Override
	public void run() {
		stoped = false;

		init();

//		try {
//			login();
//		} catch (Exception e1) {
//			logger.error(clientName + " 登录异常", e1);
//			runing = false;
//		}
		
		UserContext.setUserSession("1231312313312", "haha");

		createLogFile();

		// 运行doSomething方法，并记录时间，次数
		while (runing) {
			// 加上思考时间
			if (thinkTime > 0) {
				try {
					Thread.sleep(thinkTime);
				} catch (InterruptedException e) {
					logger.error("思考时间==线程休眠失败", e);
				}
			}
			// 开始时间
			try {
				long t1 = System.currentTimeMillis();
				doSomething();
				long t2 = System.currentTimeMillis();
				long time = t2 - t1;
				// count++;
				// if (time > maxTime) {
				// maxTime = time;
				// }
				// allTime += time;

				addCoordinate(new Coordinate(t2, time));
				// 记录日志
				recordLog(t2, time);

			} catch (Exception e) {
				logger.error("运行异常", e);
				runing = false;
			}

		}

		closeLogFile();

		try {
			logout();
		} catch (Exception e1) {
			logger.error(clientName + " 退出登录异常", e1);

		}

		// 标记为已经结束
		stoped = true;

		logger.info("线程【" + clientName + "】已结束运行");

	}

	/**
	 * <pre>
	 * 退出
	 * </pre>
	 */
	private void logout() {
		transporter.shutdown();
	}

	public void startClient() {
		if (runing) {
			throw new RuntimeException("不能重复的启动！");
		} else {
			runing = true;
		}
		start = true;
		logger.info(clientName + " 启动...");

		new Thread(this, clientName).start();
	}

	private void createLogFile() {
		// 将结果记录到日志文件中
		// 先创建一个新的文件
		File f = new File("csvLog");
		f.mkdirs();

		String formatDate = sdf.format(new Date());

		StringBuffer sbuf = new StringBuffer();
		sbuf.append(host);
		sbuf.append("=");
		sbuf.append(port);
		sbuf.append("_");
		sbuf.append(clientName);
		sbuf.append("_");
		sbuf.append(formatDate);
		sbuf.append(".CSV");

		File file = new File(f, sbuf.toString());

		try {
			file.createNewFile();

		} catch (IOException e) {
			logger.error("日志文件创建失败", e);
		}

		try {
			logWrite = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8"));

			// 加上BOM标记，否则Excel不认识
			// SB
			logWrite.write(0xEF);
			logWrite.write(0xBB);
			logWrite.write(0xBF);

			logWrite.write("时间,时间2,耗时");

			logWrite.newLine();

		} catch (IOException e) {
			logger.error("日志记录失败", e);
		}
	}

	private void recordLog(long time, long value) {
		if (logWrite != null) {
			Date d = new Date(time);
			try {
				logWrite.write(sdf2.format(d));
				logWrite.write(",");
				logWrite.write(String.valueOf(time));
				logWrite.write(",");
				logWrite.write(String.valueOf(value));
				logWrite.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void closeLogFile() {
		if (logWrite != null) {

			try {
				logWrite.close();
			} catch (IOException e) {
				logger.error("关闭日志文件异常", e);
			}

			logWrite = null;
		}
	}

	/**
	 * <pre>
	 * 停止需要一定的时间等待任务跑完
	 * </pre>
	 */
	public void stopClient() {
		// 得通知额
		runing = false;
	}

	/**
	 * <pre>
	 * 是否正在运行
	 * 
	 * 停止不代表立即结束
	 * </pre>
	 * 
	 * @return
	 */
	public boolean isRuning() {
		return runing;
	}

	/**
	 * <pre>
	 * 是否已经停止运行
	 * </pre>
	 * 
	 * @return
	 */
	public boolean isStoped() {
		return stoped;
	}

	/**
	 * <pre>
	 * 做一些增删改查的动作
	 * </pre>
	 */
	private void doSomething() {

		// IEDIReportAction action4 = proxy.getProxy(IEDIReportAction.class);
		// Map<String, String> cmap = new HashMap<String, String>();
		// cmap.put("customs_date", "2014-11-11");
		// cmap.put("cusbatch", "SZX1500KUL");
		// cmap.put("flightno", null);
		// action4.findPcustomByMapAllowNullLinkOriginal("SZX", "E", cmap,
		// true);

		// IWwhTestAction action6 = proxy.getProxy(IWwhTestAction.class);
		// DataBlock datab = action6.getDataBlock(10240000);

		for (ITask t : taskList) {
			t.doing(proxy);
		}
	}

	public boolean isStart() {
		return start;
	}

	public static void main(String[] args) {
		EmulatorClient tc1 = new EmulatorClient("demo1", "10.0.75.2", 8989);
		tc1.setServerVersion("V3.1.SP1");

		// EmulatorClient tc1 = new EmulatorClient("demo1");

		// EmulatorClient tc2 = new EmulatorClient("demo2");

		// EmulatorClient tc3 = new EmulatorClient("demo3");
		// TestClient tc4 = new TestClient("demo4");

		tc1.startClient();
		// new Thread(tc2).start();
		// new Thread(tc3).start();
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tc1.stopClient();
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public long getThinkTime() {
		return thinkTime;
	}

	public void setThinkTime(long thinkTime) {
		this.thinkTime = thinkTime;
	}

	public String getServerVersion() {
		return serverVersion;
	}

	public void setServerVersion(String serverVersion) {
		this.serverVersion = serverVersion;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<ITask> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<ITask> taskList) {
		if (taskList == null) {
			throw new NullPointerException("任务不能为空");
		}
		this.taskList = taskList;
	}

}
