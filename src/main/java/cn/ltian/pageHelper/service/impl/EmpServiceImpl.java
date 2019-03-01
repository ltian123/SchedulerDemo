package cn.ltian.pageHelper.service.impl;

import cn.ltian.pageHelper.dao.EmpDao;
import cn.ltian.pageHelper.entity.Emp;
import cn.ltian.pageHelper.service.EmpService;
import cn.ltian.pageHelper.utils.PageResults;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor = Exception.class)
public class EmpServiceImpl implements EmpService {

	@Autowired
	private EmpDao empDao;
	

    @Transactional(readOnly=true)
	public PageResults<Emp> queryUserListPaged(Emp emp, Integer page, Integer pageSize) {
		// 开始分页
        PageHelper.startPage(page, pageSize);
		//example
		Example example = new Example(Emp.class);
		Example.Criteria criteria = example.createCriteria();
		//查询
		List<Emp> userList = empDao.selectByExample(example);
		//分页工具类
		PageResults<Emp> result = new PageResults();
		//PageInfo 
		PageInfo<Emp> p = new PageInfo<Emp>(userList);
		//result  totalcount  查询总数
		result.setTotalCount((int)p.getTotal());
		result.setResults(userList);
		return result;
	}
    

}
