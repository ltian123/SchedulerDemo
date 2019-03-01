package cn.ltian.pageHelper.dao;

import cn.ltian.pageHelper.entity.Dept;
import cn.ltian.pageHelper.utils.MyMapper;
import org.springframework.stereotype.Repository;

@Repository("deptDao")
public interface DeptDao extends MyMapper<Dept> {
}
