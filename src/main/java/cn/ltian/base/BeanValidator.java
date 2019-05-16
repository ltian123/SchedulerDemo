package cn.ltian.base;

import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/**
 * 对象验证器
 * Created by ltian on 18/11/25.
 */
public class BeanValidator {



        /**
         * 验证某个bean的参数
         *
         * @param object 被校验的参数
         * @throws ValidationException 如果参数校验不成功则抛出此异常
         */
        public static <T> void validate(T object) {
            //获得验证器
            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            //执行验证
            Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
            //如果有验证信息，则取出来包装成异常返回
            if (CollectionUtils.isEmpty(constraintViolations)) {
                return;
            }
            throw new ValidationException(convertErrorMsg(constraintViolations));
        }

        /**
         * 转换异常信息
         * @param set
         * @param <T>
         * @return
         */
        private static <T> String convertErrorMsg(Set<ConstraintViolation<T>> set) {
            Map<String, StringBuilder> errorMap = new HashMap<>();
            String property;
            for (ConstraintViolation<T> cv : set) {
                //这里循环获取错误信息，可以自定义格式
                property = cv.getPropertyPath().toString();
                if (errorMap.get(property) != null) {
                    errorMap.get(property).append("," + cv.getMessage());
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(cv.getMessage());
                    errorMap.put(property, sb);
                }
            }
            return errorMap.toString();
        }
}
