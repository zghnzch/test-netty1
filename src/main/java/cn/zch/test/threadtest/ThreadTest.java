package cn.zch.test.threadtest;
import java.io.IOException;
import java.util.concurrent.*;
/***
 * @class ThreadTest
 * @description TODO
 * @author zch
 * @date 2019/9/5
 * @version V0.0.1.201909051428.01
 * @modfiyDate 201909051428
 * @createDate 201909051428
 * @package cn.zch.util
 */
public class ThreadTest {
	public static void main(String[] args) {
		test();
	}
	public static void test() {
		try {
			int corePoolSize = 2;
			int maximumPoolSize = 4;
			long keepAliveTime = 10;
			TimeUnit unit = TimeUnit.SECONDS;
			BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);
			ThreadFactory threadFactory = new NameTreadFactory();
			RejectedExecutionHandler handler = new MyIgnorePolicy();
			ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
			// 预启动所有核心线程
			executor.prestartAllCoreThreads();
			for (int i = 1; i <= 10; i++) {
				MyTask task = new MyTask(String.valueOf(i));
				executor.execute(task);
			}
			//阻塞主线程
			int readInt = System.in.read();
			System.out.println(readInt);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
