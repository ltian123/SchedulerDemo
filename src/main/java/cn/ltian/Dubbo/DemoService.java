package cn.ltian.Dubbo;

import java.util.List;

public interface DemoService {
    List<String> getPermissions(Long id);
}