package cn.ltian.washUrl;

import cn.nubia.framework.redis.JedisClusterTemplate;

import javax.annotation.Resource;

/**
 * 业务处理，需要操作redis类
 * ltian
 */
public class JedisDealUtil {

    @Resource
    public JedisClusterTemplate jedisClusterTemplate;

    private static String url_list_rediskey = "WASH_API_URL_LIST";

    /**
     * 保存url到redis
     * @param url
     * @return
     */
    public void saveUrl(String url){
         jedisClusterTemplate.lpush(url_list_rediskey,url);
    }


    /**
     * 取出list，并删除
     * @return
     */
    public String getUrls(){
        return jedisClusterTemplate.rpop(url_list_rediskey);
    }

    /**
     * 通过key 获取 packageName，
     * @param key
     * @return
     */
    public String getPackageByKey(String key){
        //通过key，crc64，获取redis hash key
        String key_package_rediskey = CreateKey(key);;
        String result =jedisClusterTemplate.hget(key_package_rediskey,key);
        return result;
    }

    /**
     * 移除hash，根据key，rediskey
     * @param key
     * @return
     */
    public boolean romovePackageName(String key){
        //通过key，crc64，获取redis hash key
        String key_package_rediskey = CreateKey(key);
        Long flag = jedisClusterTemplate.hdel(key_package_rediskey,key);

        if(flag == null || flag == 0){
            return false;
        }
        return true;
    }

    /**
     * 保存redis，包名
     * @param key
     * @param packageName
     * @return
     */
    public boolean savePackageName(String key,String packageName){
        //通过key，crc64，获取redis hash key
        String key_package_rediskey = CreateKey(key);
        jedisClusterTemplate.hset(key_package_rediskey,key,packageName);
        return true;
    }

    /**
     * 传入str，获取redis分层key，wash-0   wash-15
     * @param str
     * @return
     */
    public static String CreateKey(String str){
        if(str == null || "".equals(str)){
            return "wash-1";
        }
        long washNum_1 = Crc64.crc64Long(str);
        long washNum_2 = (washNum_1 & 0X0F);
        return "wash-"+washNum_2;
    }
}
