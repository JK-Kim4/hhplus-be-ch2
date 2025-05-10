package kr.hhplus.be.server.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentTestExecutor {

    public static List<Throwable> execute(int numberOfThreads, List<Runnable> tasks) throws InterruptedException {
        List<Throwable> errors = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(tasks.size());

        for (Runnable task : tasks) {
            executorService.execute(() ->{
                try {
                    task.run();
                }catch (Throwable e) {
                    errors.add(e);
                    e.printStackTrace();
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        return errors;
    }

}
