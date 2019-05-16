package cn.ltian.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSACoder {
    //非对称密钥算法
    public static final String KEY_ALGORITHM="RSA";
    /**
     * 密钥长度，DH算法的默认密钥长度是1024
     * 密钥长度必须是64的倍数，在512到65536位之间
     * */
    private static final int KEY_SIZE=1024;
    //公钥
    private static final String PUBLIC_KEY="xiaoxiaorenzhe";

    //私钥
    private static final String PRIVATE_KEY="dadapangzi";

    /**
     * 初始化密钥对
     * @return Map 甲方密钥的Map
     * */
    public static Map<String,Object> initKey() throws Exception{
        //实例化密钥生成器
        KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance(KEY_ALGORITHM);
        //初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE);
        //生成密钥对
        KeyPair keyPair=keyPairGenerator.generateKeyPair();
        //甲方公钥
        RSAPublicKey publicKey=(RSAPublicKey) keyPair.getPublic();
        System.out.println("系数："+publicKey.getModulus()+"  加密指数："+publicKey.getPublicExponent());
        //甲方私钥
        RSAPrivateKey privateKey=(RSAPrivateKey) keyPair.getPrivate();
        System.out.println("系数："+privateKey.getModulus()+"解密指数："+privateKey.getPrivateExponent());
        //将密钥存储在map中
        Map<String,Object> keyMap=new HashMap<String,Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;

    }

    /**MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC
     * MIGfMA0GCSqGSIb3DQEBAQUAA4GNAD
     * 私钥加密
     * @param data 待加密数据
     * @param key 密钥
     * @return byte[] 加密数据
     * */
    public static byte[] encryptByPrivateKey(byte[] data,byte[] key) throws Exception{

        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec=new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        PrivateKey privateKey=keyFactory.generatePrivate(pkcs8KeySpec);
        //数据加密
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥加密
     * @param data 待加密数据
     * @param key 密钥
     * @return byte[] 加密数据
     * */
    public static byte[] encryptByPublicKey(byte[] data,byte[] key) throws Exception{

        //实例化密钥工厂
        KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec=new X509EncodedKeySpec(key);
        //产生公钥
        PublicKey pubKey=keyFactory.generatePublic(x509KeySpec);

        //数据加密
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }
    /**
     * 私钥解密
     * @param data 待解密数据
     * @param key 密钥
     * @return byte[] 解密数据
     * */
    public static byte[] decryptByPrivateKey(byte[] data,byte[] key) throws Exception{
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec=new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        PrivateKey privateKey=keyFactory.generatePrivate(pkcs8KeySpec);
        //数据解密
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     * @param data 待解密数据
     * @param key 密钥
     * @return byte[] 解密数据
     * */
    public static byte[] decryptByPublicKey(byte[] data,byte[] key) throws Exception{

        //实例化密钥工厂
        KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec=new X509EncodedKeySpec(key);
        //产生公钥
        PublicKey pubKey=keyFactory.generatePublic(x509KeySpec);
        //数据解密
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     * @param keyMap 密钥map
     * @return byte[] 私钥
     * */
    public static byte[] getPrivateKey(Map<String,Object> keyMap){
        Key key=(Key)keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }
    /**
     * 取得公钥
     * @param keyMap 密钥map
     * @return byte[] 公钥
     * */
    public static byte[] getPublicKey(Map<String,Object> keyMap) throws Exception{
        Key key=(Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //初始化密钥
        //生成密钥对
        Map<String,Object> keyMap=RSACoder.initKey();
        //公钥
        byte[] publicKey=RSACoder.getPublicKey(keyMap);
        //byte[] publicKey = b;
        //私钥
        byte[] privateKey=RSACoder.getPrivateKey(keyMap);
        System.out.println("公钥："+ Base64.encodeBase64String(publicKey));
        System.out.println("私钥："+Base64.encodeBase64String(privateKey));
//
//        System.out.println("================密钥对构造完毕,甲方将公钥公布给乙方，开始进行加密数据的传输=============");
//        String str="aattaggcctegthththfef/aat.mp4";
//        System.out.println("===========甲方向乙方发送加密数据==============");
//        System.out.println("原文:"+str);
//        //甲方进行数据的加密
//        byte[] code1=RSACoder.encryptByPublicKey(str.getBytes(), publicKey);
//        System.out.println("甲方 使用乙方公钥加密后的数据："+Base64.encodeBase64String(code1));
//        System.out.println("===========乙方使用甲方提供的公钥对数据进行解密==============");
//        //乙方进行数据的解密
//        //byte[] decode1=RSACoder.decryptByPublicKey(code1, publicKey);
//        byte[] decode1=RSACoder.decryptByPrivateKey(code1, privateKey);
//        System.out.println("乙方解密后的数据："+new String(decode1)+"");
//
//        System.out.println("===========反向进行操作，乙方向甲方发送数据==============");
//
//        str="乙方向甲方发送数据RSA算法";
//
//        System.out.println("原文:"+str);
//
//        //乙方使用公钥对数据进行加密
//        byte[] code2=RSACoder.encryptByPublicKey(str.getBytes(), publicKey);
//        System.out.println("===========乙方使用公钥对数据进行加密==============");
//        System.out.println("加密后的数据："+Base64.encodeBase64String(code2));
//
//        System.out.println("=============乙方将数据传送给甲方======================");
//        System.out.println("===========甲方使用私钥对数据进行解密==============");
//
//        //甲方使用私钥对数据进行解密
//        byte[] decode2=RSACoder.decryptByPrivateKey(code2, privateKey);
//
//        System.out.println("甲方解密后的数据："+new String(decode2));
//
//
//
//        long now = System.currentTimeMillis();//当前时间
        String priKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAI3vr9WhLq4+HAQKnfUNKmkmR23x10uRm26AsFZVZhAKRCdDiJNUpf3cPmYi1C+n8sCYp5j9vdNDB4xwAW6rZSSai5csfy3caMTO67mRvs8O6+AH+ia5M8sfcJ8nMvmbQAESbx8EEhqtRKArW9tVkmEisVIdpyWgBjv1xZV/jHANAgMBAAECgYAtnfYuO7JsD8wjRGJF6uhRiRr16/8c63xABJ4n8SRvTU1gLSVM7Ky4rTtkyhbWBi1P1wAufIawyl83tJvRPMaxhWMukO/SHoxRrYetgjXPcV5eVgUcde0NKvy/Ei9iwPkGTi+NRzpXtuOjiWdTaqbRtSUg1sRpg67mk6YZN8WAwQJBAMPe+aFcznSrXKawlIdraMRJ37vn4E5IqyYao9KaqDOwjXGwRuO6LmQtWhBuBsthJVpeJYnjZdrSiOrtfZ341TECQQC5gh0Gh35O89eJ0s1QY8mTVAlobvSrVzWbjnkQhubrOjyLGdXw+MH8mfX2CxMxq/j46ywkFA81MjajrDHRxIGdAkBy8huU8p3GIfpRaDcB8aqd5qyB3WXpCwRFbETPhytGikm3ejdf1Rb8exDrq2YZXH1LNwzYirZvYDYxiAW7+xdBAkAOqCMW8vmdz1JGR2uFYHz6sPcVUz7tkrRfmAAkuCPijfVeoCnxIhZhmOCAEhvwHsBLGnmgWB1jfJYolGBTTI1ZAkAmbJJLQAa4axErfsKpDg1xLTeCZTEjCrTV8JelqONBGjbWwP4qFx0rH+0ZNYcL0kCe49qdkEz7fRzU2j/m1CAX";//拷贝的私钥
        String pubKey ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCN76/VoS6uPhwECp31DSppJkdt8ddLkZtugLBWVWYQCkQnQ4iTVKX93D5mItQvp/LAmKeY/b3TQweMcAFuq2UkmouXLH8t3GjEzuu5kb7PDuvgB/omuTPLH3CfJzL5m0ABEm8fBBIarUSgK1vbVZJhIrFSHacloAY79cWVf4xwDQIDAQAB";
    //    String content = "111" + now;
//        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(priKey));
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PrivateKey privateKey1 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
//        Signature signature = Signature.getInstance("SHA256WithRSA");
//        signature.initSign(privateKey1);
//        signature.update(content.getBytes());
//        String sign = Base64.encodeBase64String(signature.sign());//生成的数字签名
//        System.out.println("content   数据："+content);
//        System.out.println("ltian jiami 后的数据："+sign);
//
//        byte[] decode12=RSACoder.encryptByPrivateKey(content.getBytes(), Base64.decodeBase64(priKey));
//        System.out.println("ltian jiami 后的数据2："+Base64.encodeBase64String(decode12));
//
//        byte[] decode3 = RSACoder.decryptByPublicKey(decode12,Base64.decodeBase64(pubKey));
//
//        System.out.println("ltian jiemi 后的数据："+Base64.encodeBase64String(decode3));
//
//
//        //私钥加密公钥解密
//        //获得摘要
//        //byte[] sourcepri_pub = MdigestSHA("假设这是要加密的客户数据");
//        byte[] sourcepri_pub = ("13265986584||316494646546486498||01||private").getBytes("UTF-8");
//
//
//        //使用私钥对摘要进行加密 获得密文
//
//
//           System.out.println("私钥加密密文："+new String(Base64.encodeBase64(signpri_pub)));
//        //使用公钥对密文进行解密 返回解密后的数据
//        byte[] newSourcepri_pub=decryptByRSA1(Base64.decodeBase64(pubKey),Base64.decodeBase64(sign));
//
//        System.out.println("公钥解密："+new String(newSourcepri_pub,"UTF-8"));
        String IP = "http://127.0.0.1:8080";
        String CONNECT = "/manager/type/testCookie";
        long now = System.currentTimeMillis();//当前时间
        HttpPost post = new HttpPost(IP + CONNECT);
        StringKey str = new StringKey();
        str.setTime(now);
        str.setClientId("111");
        String content = "111" + now;
        //公钥加密，
        byte[] signpri_pub =RSACoder.encryptByPublicKey(content.getBytes(),Base64.decodeBase64(pubKey));
//        byte[] signpri_pub =encryptByRSA1(Base64.decodeBase64(priKey),Base64.decodeBase64(content));
        System.out.println("ltian Sign公钥加密："+Base64.encodeBase64String(signpri_pub)+"-------");
        str.setSign(Base64.encodeBase64String(signpri_pub));

        //私钥解密，
        byte[] newSourcepri_pub = RSACoder.decryptByPrivateKey(Base64.decodeBase64(str.getSign()), Base64.decodeBase64(priKey));
        System.out.println("newSourcepri_pub私钥解密："+new String(newSourcepri_pub));

//        //私钥加密，
//        byte[] signpri_pub123 =RSACoder.encryptByRSA1(Base64.decodeBase64(priKey),content.getBytes());
////        byte[] signpri_pub =encryptByRSA1(Base64.decodeBase64(priKey),Base64.decodeBase64(content));
//        System.out.println("ltian Sign私钥加密："+Base64.encodeBase64String(signpri_pub)+"-------");
//        str.setSign(Base64.encodeBase64String(signpri_pub123));
//
//        //公钥解密，
//        byte[] newSourcepri_pu2b = RSACoder.decryptByRSA1(Base64.decodeBase64(pubKey),Base64.decodeBase64(str.getSign()));
//        System.out.println("newSourcepri_pub公钥解密："+new String(newSourcepri_pu2b));

// 构造post请求
        Gson gson = new GsonBuilder()
//                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
//                .setDateFormat("yyyy-MM-dd HH:mm:ss")
//                .create();
				.serializeNulls()
				.create();
        System.out.println("ltian jiemi 后的数据："+gson.toJson(str));
        StringEntity entity = new StringEntity(gson.toJson(str), Charset.forName("UTF-8"));

/*entity.setContentEncoding("UTF-8");
entity.setContentType("application/json");*/

        post.setEntity(entity);
        post.setHeader("accept", "application/json");
        post.setHeader("Accept-Charset", "UTF-8");

// 设置CookieStore
        BasicCookieStore cookieStore = new BasicCookieStore();
// 构造HttpClient
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
// 发起post请求
        HttpResponse response = httpClient.execute(post);
        System.out.println("ltian jiemi 后的数据："+ EntityUtils.toString(response.getEntity()));

        HttpResponse response1 = httpClient.execute(post);
        System.out.println("ltian jiemi 后的数据："+EntityUtils.toString(response1.getEntity()));
    }

    /**
     * 使用RSA私钥加密数据
     *
     * @param  pubKeyInByte
     *            打包的byte[]形式私钥
     * @param data
     *            要加密的数据
     * @return 加密数据
     */
    public static byte[] encryptByRSA1(byte[] privKeyInByte, byte[] data) {
        try {
            PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(
                    privKeyInByte);
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = mykeyFactory.generatePrivate(priv_spec);
            Cipher cipher = Cipher.getInstance(mykeyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 用RSA公钥解密
     *
     * @param privKeyInByte
     *            公钥打包成byte[]形式
     * @param data
     *            要解密的数据
     * @return 解密数据
     */
    public static byte[] decryptByRSA1(byte[] pubKeyInByte, byte[] data) {
        try {
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(pubKeyInByte);
            PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
            Cipher cipher = Cipher.getInstance(mykeyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 加密过程
     * @param publicKey 公钥
     * @param plainTextData 明文数据
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws Exception 加密过程中的异常信息
     */
    public static byte[] encrypt(PublicKey publicKey, byte[] plainTextData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        if (publicKey == null) {
            return null;
        }

        Cipher cipher= null;
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] output = cipher.doFinal(plainTextData);
        return output;

    }

    /**
     * 解密过程
     * @param privateKey 私钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static byte[] decrypt(PrivateKey privateKey, byte[] cipherData) {
        if (privateKey == null) {
            throw new RunningErrorException(new RuntimeException("解密私钥为空, 请设置"));
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new RunningErrorException("无此解密算法", e);
        } catch (NoSuchPaddingException e) {
            throw new RunningErrorException("无此解密算法", e);
        }catch (InvalidKeyException e) {
            throw new RunningErrorException("解密私钥非法,请检查", e);
        } catch (IllegalBlockSizeException e) {
            throw new RunningErrorException("密文长度非法", e);
        } catch (BadPaddingException e) {
            throw new RunningErrorException("密文数据已损坏", e);
        }
    }

}