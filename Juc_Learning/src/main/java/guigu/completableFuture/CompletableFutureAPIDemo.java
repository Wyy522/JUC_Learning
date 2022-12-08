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


        CompletableFuture.supplyAsync(() -> {
            return 1;
        }).thenApply(f -> {
            return f+2;
        }).thenApply(f -> {
            return f+3;
        }).thenAccept(r -> System.out.println(r));
        // 任务A执行完执行B,并且B不需要A的结果
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenRun(() -> {}).join());
        // 任务A执行完成执行B,B需要A的结果,但是任务B无返回值
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenAccept(resultA -> {}).join());
        // 任务A执行完成执行B,B需要A的结果,同时任务B有返回值
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenApply(resultA -> resultA + " resultB").join());


        //这个方法表示的是,谁快就用谁的结果,类似于我们在打跑得快,或者麻将谁赢了就返回给谁
        //public <U> CompletableFuture<U> applyToEither(CompletionStage<? extends T> other, Function<? super T, U> fn);
        //下面这个在第一个中停留1s,在第二种停留2s,返回的结果是1
        System.out.println(CompletableFuture.supplyAsync(() -> {
            //暂停几秒钟线程
            try { TimeUnit.SECONDS.sleep(1);  } catch (InterruptedException e) {e.printStackTrace();}
            return 1;
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            try { TimeUnit.SECONDS.sleep(2);  } catch (InterruptedException e) {e.printStackTrace();}
            return 2;
        }), r -> {
            return r;
        }).join());
        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }

        //public <U,V> CompletableFuture<V> thenCombine
        //(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn)
        //两个CompletionStage任务都完成后,最终把两个任务的结果一起交给thenCombine来处理
        //先完成的先等着,等待其他分支任务
        System.out.println(CompletableFuture.supplyAsync(() -> {
            return 10;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            return 20;
        }), (r1, r2) -> {
            return r1 + r2;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            return 30;
        }), (r3, r4) -> {
            return r3 + r4;
        }).join());
        System.out.println(CompletableFuture.supplyAsync(() -> {
            return 10;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            return 20;
        }), (r1, r2) -> {
            return r1 + r2;
        }).join());

        CompletableFuture.supplyAsync(() -> {
                    return 10;
                })
                .thenAcceptBoth(CompletableFuture.supplyAsync(() -> {
                    return 20;
                }), (r1, r2) -> {
                    System.out.println(r1);//10
                    System.out.println(r2);//20
                });

        CompletableFuture<String> futureImg = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品的图片信息");
            return "hello.jpg";
        });

        CompletableFuture<String> futureAttr = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品的属性");
            return "黑色+256G";
        });

        CompletableFuture<String> futureDesc = CompletableFuture.supplyAsync(() -> {
            try { TimeUnit.SECONDS.sleep(3);  } catch (InterruptedException e) {e.printStackTrace();}
            System.out.println("查询商品介绍");
            return "华为";
        });
        //需要全部完成
//        futureImg.get();
//        futureAttr.get();
//        futureDesc.get();
        //CompletableFuture<Void> all = CompletableFuture.allOf(futureImg, futureAttr, futureDesc);
        //all.get();
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(futureImg, futureAttr, futureDesc);
        anyOf.get();
        System.out.println(anyOf.get());
        System.out.println("main over.....");

    }


}
