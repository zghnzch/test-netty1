package cn.zch.test.threadtest;
import org.apache.log4j.Logger;
/***
 * @class MyTask
 * @description TODO
 * @author zch
 * @date 2019/9/5
 * @version V0.0.1.201909051431.01
 * @modfiyDate 201909051431
 * @createDate 201909051431
 * @package cn.zch.test.threadtest
 */
public class MyTask implements Runnable {
	private String name;
	public MyTask(String name) {
		this.name = name;
	}
	private static final Logger myLog = Logger.getLogger(MyTask.class);
	@Override
	public void run() {
		try {
			myLog.info(this.toString() + " is running!");
			//让任务执行慢点
			Thread.sleep(3000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return "MyTask [name=" + name + "]";
	}
}
