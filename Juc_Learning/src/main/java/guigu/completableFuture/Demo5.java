package guigu.completableFuture;

import java.sql.Time;
import java.util.concurrent.*;


//whenComplete表示当上一步完成后执行
//解决阻塞问题和空轮询
public class Demo5 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executorService=Executors.newFixedThreadPool(3);

        try {
            CompletableFuture<String> objectCompletableFuture = CompletableFuture.supplyAsync(()->{
                System.out.println("当前线程为-->"+Thread.currentThread().getName());
                try {TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
                return "hello";
            },executorService).whenComplete((v,e)->{
                //v=="hello"  e== Exception
                if (e==null){
                    System.out.println("正确获得结果");
                }
            }).exceptionally(e->{
                e.printStackTrace();
                System.out.println("异常情况"+e.getCause()+"\t"+e.getMessage());
                return null;
            });
            System.out.println(objectCompletableFuture.get());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            executorService.shutdown();
        }

    }
}
