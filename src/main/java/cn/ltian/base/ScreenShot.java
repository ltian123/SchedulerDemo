package cn.ltian.base;

public class ScreenShot {

    private String screenName;
    private String screenUrl;
    private String smallScreenUrl;
    private Integer screenSize;
    private Integer screenWidth;
    private Integer screenHeight;
    private Integer sortIndex;

    public String getSmallScreenUrl() {
        return smallScreenUrl;
    }

    public void setSmallScreenUrl(String smallScreenUrl) {
        this.smallScreenUrl = smallScreenUrl;
    }

    public Integer getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(Integer screenSize) {
        this.screenSize = screenSize;
    }

    public Integer getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(Integer screenWidth) {
        this.screenWidth = screenWidth;
    }

    public Integer getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(Integer screenHeight) {
        this.screenHeight = screenHeight;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getScreenUrl() {
        return screenUrl;
    }

    public void setScreenUrl(String screenUrl) {
        this.screenUrl = screenUrl;
    }

    @Override
    public String toString() {
        return "ScreenShot{" +
                "screenName='" + screenName + '\'' +
                ", screenUrl='" + screenUrl + '\'' +
                ", smallScreenUrl='" + smallScreenUrl + '\'' +
                ", screenSize=" + screenSize +
                ", screenWidth=" + screenWidth +
                ", screenHeight=" + screenHeight +
                ", sortIndex=" + sortIndex +
                '}';
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String str = "{\n" +
                "        \"smallScreenUrl\": \"https://developer-appstore.nubia.com/Developer/as/2018/02/06/091736/d11739bf31b24d6285f3396b7f0d5e3d.jpg\",\n" +
                "        \"screenUrl\": \"https://developer-appstore.nubia.com/Developer/as/2018/02/06/091736/d11739bf31b24d6285f3396b7f0d5e3d.jpg\",\n" +
                "        \"screenSize\": 96956,\n" +
                "        \"screenWidth\": 720,\n" +
                "        \"screenHeight\": 1280,\n" +
                "        \"sortIndex\": 0\n" +
                "      }";

        ScreenShot screenShot = com.alibaba.fastjson.JSON.parseObject(str,ScreenShot.class);
        System.out.println(screenShot.toString());
        if(screenShot.getScreenName() == null){
            System.out.println("alibaba NB, null is a null");
        }
        if("null".equals(screenShot.getScreenName())){
            System.out.println("null became a string");
        }
    }
}
