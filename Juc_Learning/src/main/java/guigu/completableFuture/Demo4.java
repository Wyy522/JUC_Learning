package guigu.completableFuture;

import java.util.concurrent.*;


//CompletableFuture是Future的增强版,减少轮询和阻塞  默认是ForkJoinPool启用
public class Demo4 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.new 不推荐
        CompletableFuture completableFuture =new CompletableFuture();

        //2. runAsync(Runnable,Executor) 无返回值
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(()->{
            System.out.println("当前线程为-->"+Thread.currentThread().getName());
        });
        System.out.println(voidCompletableFuture.get());

        //3. runAsync(Runnable,Executor) 有返回值
        CompletableFuture<String> objectCompletableFuture = CompletableFuture.supplyAsync(()->{
            System.out.println("当前线程为-->"+Thread.currentThread().getName());
            return "hello";
        });
        System.out.println(objectCompletableFuture.get());
    }
}
