package guigu.completableFuture;

import java.util.concurrent.*;


//get 和 join的区别在于编译期间会不会处理异常情况
public class Demo6 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> objectCompletableFuture = CompletableFuture.supplyAsync(()->{
            System.out.println("当前线程为-->"+Thread.currentThread().getName());
            try {TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
            return "hello";
        });

        //有异常处理
        System.out.println(objectCompletableFuture.get());
        //没有异常处理
        System.out.println(objectCompletableFuture.join());
    }
}

