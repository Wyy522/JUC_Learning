package guigu.completableFuture;

import java.util.concurrent.*;

public class CompletableFutureAPIDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<String> voidCompletableFuture = CompletableFuture.supplyAsync(()->{
            try {
                TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e) { e.printStackTrace();}
            return "abc";
        });
        voidCompletableFuture.get();
        voidCompletableFuture.get(2,TimeUnit.MILLISECONDS);
        voidCompletableFuture.getNow("xxx");
        voidCompletableFuture.join();
        System.out.println(voidCompletableFuture.complete("111"));


        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try { TimeUnit.SECONDS.sleep(1);  } catch (InterruptedException e) {e.printStackTrace();}
            return 1;
        }).thenApply(s->{
            System.out.println("-----1");
            //如果加上int error=1/0; 由于存在依赖关系(当前步错,不走下一步),当前步骤有异常的话就叫停
            //int error=1/0;
            return s+1;
        }).thenApply(s->{
            System.out.println("-----2");
            return s+2;
        }).whenComplete((v,e)->{
            if(e==null){
                System.out.println("result-----"+v);
            }
        }).exceptionally(e->{
            e.printStackTrace();
            return null;
        });
        System.out.println(Thread.currentThread().getName()+"\t"+"over....");
        try { TimeUnit.SECONDS.sleep(3);  } catch (InterruptedException e) {e.printStackTrace();}


        System.out.println(CompletableFuture.supplyAsync(() -> {
            return 1;
        }).handle((f,e) -> {
            System.out.println("-----1");
            return f + 2;
        }).handle((f,e) -> {
            System.out.println("-----2");
            //如果这里异常了,handle方法依旧可以继续执行下去
            /*
            -----1
            -----2
            -----3
            null
            java.util.concurrent.CompletionException: java.lang.NullPointerException
                at java.util.concurrent.CompletableFuture.encodeThrowable(CompletableFuture.java:273)
                at java.util.concurrent.CompletableFuture.completeThrowable(CompletableFuture.java:280)
                ... 3 more
            * */
            int error=1/0;
            return f + 3;
        }).handle((f,e) -> {
            System.out.println("-----3");
            return f + 4;
        }).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("----result: " + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            return null;
        }).join());
    }
}
