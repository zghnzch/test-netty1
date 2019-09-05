package cn.zch.test.threadtest;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
/***
 * @class MyIgnorePolicy
 * @description TODO
 * @author zch
 * @date 2019/9/5
 * @version V0.0.1.201909051431.01
 * @modfiyDate 201909051431
 * @createDate 201909051431
 * @package cn.zch.test.threadtest
 */
public class MyIgnorePolicy implements RejectedExecutionHandler {
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
		doLog(r, e);
	}
	private void doLog(Runnable r, ThreadPoolExecutor e) {
		// 可做日志记录等
		System.err.println(r.toString() + " rejected");
		//          System.out.println("completedTaskCount: " + e.getCompletedTaskCount());
	}
}
