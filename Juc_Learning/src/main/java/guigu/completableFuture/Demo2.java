package guigu.completableFuture;

import java.util.concurrent.*;

//Future缺点1.get()阻塞
public class Demo2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {

        long start= System.currentTimeMillis();
        FutureTask<String> f1=new FutureTask<String>(()->{
            TimeUnit.MILLISECONDS.sleep(300);
            return "f1";
        });

        System.out.println(f1.get());//
        System.out.println(f1.get(200,TimeUnit.MILLISECONDS));//强制不等待
        long end= System.currentTimeMillis();
        System.out.println("耗费"+(end-start)+"ms");



    }
}
