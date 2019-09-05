package cn.zch.test.threadtest;
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
	@Override
	public void run() {
		try {
			System.out.println(this.toString() + " is running!");
			Thread.sleep(3000); //让任务执行慢点
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
