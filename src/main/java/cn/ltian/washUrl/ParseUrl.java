package cn.ltian.washUrl;

/**
 * 解析url
 * ltian
 */
public class ParseUrl {

    public static void main(String[] args) {
        String str = "https://89fe13943415c965441647de8437ca63.dd.cdntips.com/imtt.dd.qq.com/16891/F8ED63E12FB45B0EA8B6943EA6C1A37F.apk?mkey=5cd0660b314dce75&f=0c27&fsname=cn.gov.tax.its_1.1.8_10108.apk&csr=1bbd&cip=49.77.232.128&proto=https";
        boolean isApk = true;
        ParseUrl parseUrl = new ParseUrl();
        //1.过滤白名单，
        boolean status = parseUrl.filterUrl(str);
        if(status){
            System.out.println("白名单");
            return;
        }
        //判断是不是符合策略的apk下载
        isApk = parseUrl.checkUrlIsEffective(str);
        if(isApk){
            //处理url，解析成key
            String key = parseUrl.dealUrlToKey(str);
            System.out.println(key);
            //处理url，解析取到包名
            String name = parseUrl.parseUrlToName(str);
            System.out.println("name===>"+name);
        }else {
            System.out.println("不是apk下载");
        }


    }
    /**
     * 处理url，解析成key
     * @param url
     * @return
     */
    public String dealUrlToKey(String url){
        boolean status = false;
        String result = null;
                //应用宝
        status = url.contains("imtt.dd.qq.com");
        if(status){
            String strStart = "imtt.dd.qq.com";
            String strEnd = ".apk";
            /* 找出指定的2个字符在 该字符串里面的 位置 */
            int strStartIndex = url.indexOf(strStart);
            int strEndIndex = url.indexOf(strEnd);
            /* index 为负数 即表示该字符串中 没有该字符 */
            if (strEndIndex < 0) {
                result = url.substring(strStartIndex, url.length()).substring(strStart.length());
            }
            else{
                /* 开始截取 */
                result = url.substring(strStartIndex, strEndIndex).substring(strStart.length());
            }
            return result;

        }

        //PP助手
        status = url.contains("ucdl.25pp.com");
        if(status){
            String strStart = "ucdl.25pp.com";
            String strEnd = ".apk";
            /* 找出指定的2个字符在 该字符串里面的 位置 */
            int strStartIndex = url.indexOf(strStart);
            int strEndIndex = url.indexOf(strEnd);
            /* index 为负数 即表示该字符串中 没有该字符 */
            if (strEndIndex < 0) {
                result = url.substring(strStartIndex, url.length()).substring(strStart.length());
            }
            else{
                /* 开始截取 */
                result = url.substring(strStartIndex, strEndIndex).substring(strStart.length());
            }
            return result;
        }

        //百度
        status = url.contains("gdown.baidu.com");
        if(status){
            String strStart = "gdown.baidu.com";
            int strStartIndex = url.indexOf(strStart);
            /* index 为负数 即表示该字符串中 没有该字符 */
            result = url.substring(strStartIndex, url.length()).substring(strStart.length());
            return result;
        }

        //360手机助手
        status = url.contains("360tpcdn.com");
        if(status){
            String strStart = "360tpcdn.com";
            String strEnd = ".apk";
            /* 找出指定的2个字符在 该字符串里面的 位置 */
            int strStartIndex = url.indexOf(strStart);
            int strEndIndex = url.indexOf(strEnd);
            /* index 为负数 即表示该字符串中 没有该字符 */
            if (strEndIndex < 0) {
                result = url.substring(strStartIndex, url.length()).substring(strStart.length());
            }
            else{
                /* 开始截取 */
                result = url.substring(strStartIndex, strEndIndex).substring(strStart.length());
            }
            return result;
        }
        //豌豆荚
        status = url.contains("alissl.ucdl.pp.uc.cn");
        if(status){
            String strStart = "alissl.ucdl.pp.uc.cn";
            String strEnd = ".apk";
            /* 找出指定的2个字符在 该字符串里面的 位置 */
            int strStartIndex = url.indexOf(strStart);
            int strEndIndex = url.indexOf(strEnd);
            /* index 为负数 即表示该字符串中 没有该字符 */
            if (strEndIndex < 0) {
                result = url.substring(strStartIndex, url.length()).substring(strStart.length());
            }
            else{
                /* 开始截取 */
                result = url.substring(strStartIndex, strEndIndex).substring(strStart.length());
            }
            return result;
        }
        //小米应用商店
        status = url.contains("xiaomi.com");
        if(status){
            String strStart = "xiaomi.com";
            String strEnd = ".apk";
            /* 找出指定的2个字符在 该字符串里面的 位置 */
            int strStartIndex = url.indexOf(strStart);
            int strEndIndex = url.indexOf(strEnd);
            /* index 为负数 即表示该字符串中 没有该字符 */
            if (strEndIndex < 0) {
                result = url.substring(strStartIndex, url.length()).substring(strStart.length());
            }
            else{
                /* 开始截取 */
                result = url.substring(strStartIndex, strEndIndex).substring(strStart.length());
            }
            return result;
        }
        //taptap
        status = url.contains("c.tapimg.com");
        if(status){
            String strStart = "c.tapimg.com";
            String strEnd = ".apk";
            /* 找出指定的2个字符在 该字符串里面的 位置 */
            int strStartIndex = url.indexOf(strStart);
            int strEndIndex = url.indexOf(strEnd);
            /* index 为负数 即表示该字符串中 没有该字符 */
            if (strEndIndex < 0) {
                result = url.substring(strStartIndex, url.length()).substring(strStart.length());
            }else{
                /* 开始截取 */
                result = url.substring(strStartIndex, strEndIndex).substring(strStart.length());
            }

            return result;
        }

        //apk包下载
        status = url.contains(".apk");
        if(status){
            String strStart = "//";
            String strEnd = ".apk";
            /* 找出指定的2个字符在 该字符串里面的 位置 */
            int strStartIndex = url.indexOf(strStart);
            int strEndIndex = url.indexOf(strEnd);
            /* index 为负数 即表示该字符串中 没有该字符 */
            if (strEndIndex < 0) {
                result = url.substring(strStartIndex, url.length()).substring(strStart.length());
            }else{
                /* 开始截取 */
                result = url.substring(strStartIndex, strEndIndex).substring(strStart.length());
            }

            return result;
        }
        return result;
    }

    /**
     * 检查是否是 符合处理策略的apk包下载
     * @param url
     * @return
     */
    public boolean checkUrlIsEffective(String url){
        boolean status = false;
        boolean isApk = false;
        //应用宝
        status = url.contains("imtt.dd.qq.com");
        isApk = url.contains(".apk");
        if(status && isApk){
            return true;
        }
        //PP助手
        status = false;
        isApk = false;
        status = url.contains("ucdl.25pp.com");
        isApk = url.contains(".apk");
        if(status && isApk){
            return true;
        }
        //百度
        status = false;
        isApk = false;
        status = url.contains("gdown.baidu.com");
        if(status){
            return true;
        }
        //360手机助手
        status = false;
        isApk = false;
        status = url.contains("360tpcdn");
        isApk = url.contains(".apk");
        if(status && isApk){
            return true;
        }
        //豌豆荚
        status = false;
        isApk = false;
        status = url.contains("alissl.ucdl.pp.uc.cn");
        isApk = url.contains(".apk");
        if(status && isApk){
            return true;
        }
        //小米应用商店
        status = false;
        isApk = false;
        status = url.contains("xiaomi.com");
        isApk = url.contains(".apk");
        if(status && isApk){
            return true;
        }
        //taptap
        status = false;
        isApk = false;
        status = url.contains("c.tapimg.com");
        isApk = url.contains(".apk");
        if(status && isApk){
            return true;
        }
        //apk包下载
        isApk = false;
        isApk = url.contains(".apk");
        if(isApk){
            return true;
        }
        return false;
    }

    /**
     * 处理url，返回url中解析的包名，没有返回null
     * @param url
     * @return
     */
    public String parseUrlToName(String url){
        String result = null;
        boolean status = false;
        boolean isApk = false;
        boolean havePkg = false;

        //应用宝
        status = url.contains("imtt.dd.qq.com");
        isApk = url.contains(".apk");
        havePkg = url.contains("fsname=");
        if(status && isApk && havePkg){
            String strStart = "fsname=";
            String strEnd = "_";
            int strStartIndex = url.indexOf(strStart);
            result = url.substring(strStartIndex, url.length());
            int strEndIndex = result.indexOf(strEnd);
            strStartIndex = result.indexOf(strStart);
            /* index 为负数 即表示该字符串中 没有该字符 */
            if (strEndIndex < 0) {
                result = result.substring(strStartIndex, result.length()).substring(strStart.length());
            }else{
                /* 开始截取 */
                result = result.substring(strStartIndex, strEndIndex).substring(strStart.length());
            }

            return result;
        }
        //PP助手
        status = url.contains("ucdl.25pp.com");
        isApk = url.contains(".apk");
        havePkg = url.contains("pkg=");
        if(status && isApk && havePkg){
            String strStart = "pkg=";
            String strEnd = "&";
            int strStartIndex = url.indexOf(strStart);
            result = url.substring(strStartIndex, url.length());
            int strEndIndex = result.indexOf(strEnd);
            strStartIndex = result.indexOf(strStart);
            /* index 为负数 即表示该字符串中 没有该字符 */
            if (strEndIndex < 0) {
                result = result.substring(strStartIndex, result.length()).substring(strStart.length());
            }else{
                /* 开始截取 */
                result = result.substring(strStartIndex, strEndIndex).substring(strStart.length());
            }

            return result;
        }

        //360手机助手
        status = url.contains("360tpcdn.com");
        isApk = url.contains(".apk");
        if(status && isApk ){
            String strStart = "/";
            String strEnd = "_";
            /* 找出指定的2个字符在 该字符串里面的 位置 */
            int strStartIndex = url.lastIndexOf(strStart);
            int strEndIndex = url.lastIndexOf(strEnd);
            /* index 为负数 即表示该字符串中 没有该字符 */
            if (strEndIndex < 0) {
                result = url.substring(strStartIndex, url.length()).substring(strStart.length());
            }else{
                /* 开始截取 */
                result = url.substring(strStartIndex, strEndIndex).substring(strStart.length());
            }

            return result;
        }

        //豌豆荚
        status = url.contains("alissl.ucdl.pp.uc.cn");
        isApk = url.contains(".apk");
        havePkg = url.contains("pkg=");
        if(status && isApk && havePkg){
            String strStart = "pkg=";
            String strEnd = "&";
            int strStartIndex = url.indexOf(strStart);
            result = url.substring(strStartIndex, url.length());
            int strEndIndex = result.indexOf(strEnd);
            strStartIndex = result.indexOf(strStart);
            /* index 为负数 即表示该字符串中 没有该字符 */
            if (strEndIndex < 0) {
                result = result.substring(strStartIndex, result.length()).substring(strStart.length());
            }else{
                /* 开始截取 */
                result = result.substring(strStartIndex, strEndIndex).substring(strStart.length());
            }

            return result;
        }

        //小米应用商店
        status = url.contains("xiaomi.com");
        isApk = url.contains(".apk");
        if(status && isApk ){
            String strStart = "/";
            String strEnd = ".apk";
            /* 找出指定的2个字符在 该字符串里面的 位置 */
            int strStartIndex = url.lastIndexOf(strStart);
            int strEndIndex = url.lastIndexOf(strEnd);
            /* index 为负数 即表示该字符串中 没有该字符 */
            if (strEndIndex < 0) {
                result = url.substring(strStartIndex, url.length()).substring(strStart.length());
            }else{
                /* 开始截取 */
                result = url.substring(strStartIndex, strEndIndex).substring(strStart.length());
            }

            return result;
        }

        //taptap
        status = url.contains("c.tapimg.com");
        isApk = url.contains(".apk");
        havePkg = url.contains("_upd=");
        if(status && isApk && havePkg){
            String strStart = "upd=";
            String strEnd = "_";
            int strStartIndex = url.indexOf(strStart);
            result = url.substring(strStartIndex, url.length());
            int strEndIndex = result.indexOf(strEnd);
            strStartIndex = result.indexOf(strStart);
            /* index 为负数 即表示该字符串中 没有该字符 */
            if (strEndIndex < 0) {
                result = result.substring(strStartIndex, result.length()).substring(strStart.length());
            }else{
                /* 开始截取 */
                result = result.substring(strStartIndex, strEndIndex).substring(strStart.length());
            }

            return result;
        }


        return result;
    }

    /**
     * 过滤白名单，如果是白名单内 返回true，不是 则返回flase
     * @param url
     * @return
     */
    public boolean filterUrl(String url){
        boolean status = false;
        //努比亚应用中心域名 以及 cdn地址，过滤
        status = url.contains("m.appstore.nubia.com");
        if(status){
            return status;
        }
        return status;
    }


}
