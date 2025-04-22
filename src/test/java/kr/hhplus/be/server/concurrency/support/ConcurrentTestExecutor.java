package kr.hhplus.be.server.concurrency.support;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentTestExecutor {

    public static void execute(int numberOfThreads, int counter, List<Runnable> tasks) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(counter);


        for(int i = 0; i < counter; i ++){
            for (Runnable task : tasks) {
                executorService.execute(() ->{
                    try {
                        task.run();
                    }finally {
                        latch.countDown();
                    }
                });
            }
        }

        latch.await();
        executorService.shutdown();
    }

}
