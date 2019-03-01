package cn.ltian.pageHelper.controller;

import cn.ltian.pageHelper.entity.Emp;
import cn.ltian.pageHelper.service.EmpService;
import cn.ltian.pageHelper.utils.PageResults;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/emp")
public class EmpController {

	@Autowired
	private EmpService empService;

	@RequestMapping(value = "/all", produces="application/json;charset=UTF-8")
	@ResponseBody
	public String findAll() {
		PageResults<Emp> emps = empService.queryUserListPaged(new Emp(),0,5);
		Gson gson = new GsonBuilder().serializeNulls().create();
		System.out.println(	"json："+gson.toJson(emps)+"  emp:"+emps);
		return gson.toJson(emps);
	}
}
