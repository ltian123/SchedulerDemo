package cn.ltian.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class Demo3 {


    public static com.google.common.cache.CacheLoader<String, String> createCacheLoader() {
        return new com.google.common.cache.CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                System.out.println( "加载创建key:" + key);
                return key+"+value";
            }
        };
    }



    public static void main(String[] args) {
        /**
         *   //CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
         *                 = CacheBuilder.newBuilder()
         *                 //设置并发级别为8，并发级别是指可以同时写缓存的线程数
         *                 .concurrencyLevel(8)
         *                 //设置写缓存后8秒钟过期
         *                 .expireAfterWrite(8, TimeUnit.SECONDS)
         *                 //设置缓存容器的初始容量为10
         *                 .initialCapacity(10)
         *                 //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
         *                 .maximumSize(100)
         *                 //设置要统计缓存的命中率
         *                 .recordStats()
         *                 //设置缓存的移除通知
         *                 .removalListener(new RemovalListener<Object, Object>() {
         *                     @Override
         *                     public void onRemoval(RemovalNotification<Object, Object> notification) {
         *                         System.out.println(notification.getKey() + " was removed, cause is " + notification.getCause());
         *                     }
         *                 })
         */
        //CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(1000)
                //设置写缓存后30L过期
                .expireAfterAccess(30L, TimeUnit.MILLISECONDS)
                //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(createCacheLoader());
        //expireAfterAccess(long, TimeUnit)：缓存项在给定时间内没有被读/写访问，则回收。请注意这种缓存的回收顺序和基于大小回收一样。
        //expireAfterWrite(long, TimeUnit)：缓存项在给定时间内没有被写访问（创建或覆盖），则回 收。如果认为缓存数据总是在固定时候后变得陈旧不可用，这种回收方式是可取的。
        String key1 = "1";
        cache.put("xiaowang1","我是特别");
        //第一次获取创建
        String value1 = cache.getUnchecked("xiaowang1");
        System.out.println("value1:"+value1);
        //第二次获取从缓存中获取
        String value2 = cache.getIfPresent("xiaowang1");
        System.out.println("value2:"+value2);



        //放入缓存 cache.put(key,value);
        // 移除缓存 cache.invalidate(key);
        //批量删除缓存 cache.invalidateAll(keys);  List<String> keys
        //会重新加载创建cache   cache.getUnchecked(key);
        //不会重新加载创建cache   cache.getIfPresent(key);
        //获取,会抛出异常   cache.get(key);
        //任何时候，你都可以显式地清除缓存项，而不是等到它被回收：
        // 个别清除：Cache.invalidate(key)
        //批量清除：Cache.invalidateAll(keys)
        // 清除所有缓存项：Cache.invalidateAll()
        //正如LoadingCache.refresh(K)所声明，刷新表示为键加载新值，这个过程可以是异步的。在刷新操作进行时，缓存仍然可以向其他线程返回旧值，而不像回收操作，读缓存的线程必须等待新值加载完成。
        //expireAfterAccess(long, TimeUnit)：缓存项在给定时间内没有被读/写访问，则回收。请注意这种缓存的回收顺序和基于大小回收一样。
        //expireAfterWrite(long, TimeUnit)：缓存项在给定时间内没有被写访问（创建或覆盖），则回 收。如果认为缓存数据总是在固定时候后变得陈旧不可用，这种回收方式是可取的。


        LoadingCache<String, String> cache2 = CacheBuilder.newBuilder()
                //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(1000)
                //设置写缓存后30L过期
                .expireAfterAccess(2, TimeUnit.SECONDS)
                //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(createCacheLoader());
        //定时回收

        //第一次获取创建
        String value21 = cache2.getUnchecked("xiaowang21");
        System.out.println("value21:"+value21);
        try{
            TimeUnit.SECONDS.sleep(4);
        }catch (Exception e){
            e.printStackTrace();
        }

        //第一次获取创建
        String value22 = cache2.getUnchecked("xiaowang21");
        System.out.println("value21:"+value22);

        String value23 = cache.getIfPresent("xiaowang21"); //不会重新加载创建cache
        System.out.println("value23:"+value23);

        /**
         * 任何时候，你都可以显式地清除缓存项，而不是等到它被回收：
         * 个别清除：Cache.invalidate(key)
         * 批量清除：Cache.invalidateAll(keys)
         * 清除所有缓存项：Cache.invalidateAll()
         */

        /**
         * 正如LoadingCache.refresh(K)所声明，刷新表示为键加载新值，这个过程可以是异步的。在刷新操作进行时，缓存仍然可以向其他线程返回旧值，而不像回收操作，读缓存的线程必须等待新值加载完成。
         */
    }
}
