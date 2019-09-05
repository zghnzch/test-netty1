package cn.zch.test.threadtest;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
/***
 * @class NameTreadFactory
 * @description TODO
 * @author zch
 * @date 2019/9/5
 * @version V0.0.1.201909051430.01
 * @modfiyDate 201909051430
 * @createDate 201909051430
 * @package cn.zch.test.threadtest
 */
public class NameTreadFactory implements ThreadFactory {
	private final AtomicInteger mThreadNum = new AtomicInteger(1);
	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, "my-thread-" + mThreadNum.getAndIncrement());
		System.out.println(t.getName() + " has been created");
		return t;
	}
}
