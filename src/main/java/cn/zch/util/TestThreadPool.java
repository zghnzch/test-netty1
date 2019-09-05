package cn.zch.util;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/***
 * @class TestThreadPool
 * @description TODO
 * @author zch
 * @date 2019/9/5
 * @version V0.0.1.201909051252.01
 * @modfiyDate 201909051252
 * @createDate 201909051252
 * @package cn.zch.util
 */
public class TestThreadPool {
	public static final ExecutorService executorService = Executors.newFixedThreadPool(10);
	public static void main(String[] args) {
		executorService.execute(new Thread());
	}
	public static void test(){
		System.out.println("123456");
	}
}
