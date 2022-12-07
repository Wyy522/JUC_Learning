package guigu.completableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

//Future缺点2.空轮询
public class Demo3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {

        long start= System.currentTimeMillis();
        FutureTask<String> f1=new FutureTask<String>(()->{
            TimeUnit.MILLISECONDS.sleep(300);
            return "f1";
        });
        Thread thread = new Thread(f1);
        thread.start();
        while(true){
            if (f1.isDone()){
                System.out.println(f1.get());
                break;
            }else {
                TimeUnit.MILLISECONDS.sleep(50);
                System.out.println("正在处理中...");
            }
        }
        long end= System.currentTimeMillis();
        System.out.println("耗费"+(end-start)+"ms");



    }
}
