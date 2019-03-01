package cn.ltian.pageHelper.service;



import cn.ltian.pageHelper.entity.Emp;
import cn.ltian.pageHelper.utils.PageResults;
import org.springframework.stereotype.Repository;

@Repository("empService")
public interface EmpService {
	


	public PageResults<Emp> queryUserListPaged(Emp emp, Integer page, Integer pageSize);
	

	
}
