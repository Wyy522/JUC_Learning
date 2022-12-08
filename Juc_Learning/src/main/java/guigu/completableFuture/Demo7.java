package guigu.completableFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


//get 和 join的区别在于编译期间会不会处理异常情况
public class Demo7 {
    static List<NetMall> list = Arrays.asList(
            new NetMall("jd"),
            new NetMall("tao bao"),
            new NetMall("ti mao")
    );

    //一个一个查
    public static List<String> getPrice(String productName){
        return list.stream().
                map(netMall -> String.format(productName+"in %s price is %.2f",
                        netMall.getNetMallName(),
                        netMall.calcPrice(productName)))
                .collect(Collectors.toList());
    }

    //同时查
    public static List<String> getPriceCurrent(String productName){
    return list.stream()
                .map(
                        netMall->CompletableFuture.supplyAsync(()->String.format(productName+" in %s price is %.2f",
                        netMall.getNetMallName(),
                        netMall.calcPrice(productName))))
                .collect(Collectors.toList())

                .stream()
                .map(s->s.join())
                .collect(Collectors.toList());

    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start= System.currentTimeMillis();
//        List<String> s1 = getPrice("mysql");
        List<String> s2 = getPriceCurrent("mysql");
//        for (String sss:s1) {
//            System.out.println(sss);
//        }
        for (String sss:s2) {
            System.out.println(sss);
        }
        long end= System.currentTimeMillis();
        System.out.println("耗费"+(end-start)+"ms");
    }
}

 class NetMall {
    private String NetMallName;

    public NetMall(String netMallName) {
        NetMallName = netMallName;
    }

    public double calcPrice(String productName) {
        try {TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e) { e.printStackTrace();}
        return ThreadLocalRandom.current().nextDouble() * 2 + productName.charAt(0);
    }

    public String getNetMallName() {
        return NetMallName;
    }

    public void setNetMallName(String netMallName) {
        NetMallName = netMallName;
    }
}
