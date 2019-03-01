package cn.ltian.pageHelper.dao;

import cn.ltian.pageHelper.entity.Emp;
import cn.ltian.pageHelper.utils.MyMapper;
import org.springframework.stereotype.Repository;

@Repository("empDao")
public interface EmpDao extends MyMapper<Emp> {

}
