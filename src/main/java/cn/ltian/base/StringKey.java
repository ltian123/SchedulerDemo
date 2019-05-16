package cn.ltian.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import net.sf.json.JSONObject;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class StringKey {
    @Pattern(regexp = "^[1-9][0-9]?$", message = "格式错误")
//    @Length(max = 2, message = "{valid.length}")
//    @NotNull
    private  String clientId;
    @NotNull
    private long time;
    @NotEmpty(message = "不能为空")
    @JsonIgnore
    @JsonIgnoreProperties
    @JSONField(serialize = false)
    @Expose(serialize = false, deserialize = false)
    private String sign;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "StringKey{" +
                "clientId='" + clientId + '\'' +
                ", time=" + time +
                ", sign='" + sign + '\'' +
                '}';
    }

    public static void main(String[] args) {
        StringKey stringKey = new StringKey();
        stringKey.setClientId("22");
        stringKey.setSign("11");
        stringKey.setTime(122233443);
        try{
            BeanValidator.validate(stringKey);
        }catch(ValidationException e){
            System.out.println("校验失败："+e.getMessage());
        }
        Gson gson = new GsonBuilder()
//				.registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
//		        .setDateFormat("yyyy-MM-dd HH:mm:ss")
//		        .create();
                .serializeNulls()
//                .excludeFieldsWithoutExposeAnnotation()//启用	@Expose
                .create();
        System.out.println(gson.toJson(stringKey));
        //1、使用JSONObject
        JSONObject json = JSONObject.fromObject(stringKey);
        String strJson=json.toString();
        System.out.println("strJson:"+strJson);
    }
}
