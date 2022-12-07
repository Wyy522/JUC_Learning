package guigu.completableFuture;

import java.util.concurrent.*;

//future 异步任务
public class Demo1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        long start= System.currentTimeMillis();
        ExecutorService service=Executors.newFixedThreadPool(3);
        FutureTask<String> f1=new FutureTask<String>(()->{
            TimeUnit.MILLISECONDS.sleep(300);
            return "f1";
        });

        FutureTask<String> f2=new FutureTask<String>(()->{
            TimeUnit.MILLISECONDS.sleep(500);
            return "f2";
        });
        TimeUnit.MILLISECONDS.sleep(400);
        service.submit(f1);
        service.submit(f2);
        service.shutdown();
        System.out.println(f1.get());
        System.out.println(f2.get());
        long end= System.currentTimeMillis();
        System.out.println("耗费"+(end-start)+"ms");

    }
}

