import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author xichengxml
 * @date 2019/1/25 17:00
 * 使用cyclicbarrier模拟高并发请求
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MockMultiThreadControllerTest {

    private static final int NUM = 5;
    private static int count = 0;

    @Test
    public void test(){
        Thread finishThread = new Thread(){
            @Override
            public void run() {
                System.out.println("Finished");
            }
        };
        System.out.println("Ready, Go!");
        CyclicBarrier cyclicBarrier = new CyclicBarrier(NUM, finishThread);
        waitAllArrrived(cyclicBarrier);
    }


    public static void waitAllArrrived(CyclicBarrier cyclicBarrier) {
        for (int i = 0; i < NUM; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    bizCode();
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
        }
    }

    /**
     * 业务调用，对数字进行加1操作
     */
    private static void bizCode() {
        for (int i = 0; i < 10; i++) {
            count++;
            System.out.println("This is bizCode--" + i + "count:" + count);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
