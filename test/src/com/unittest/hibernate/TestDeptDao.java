package com.unittest.hibernate;

import com.dao.hibernate.DeptDAO;
import com.daoimpl.hibernate.DeptDaoImpl;
import com.model.hibernate.Dept;
import org.junit.Test;

public class TestDeptDao {
	@Test
	public void testAdd() {

		DeptDAO dao = new DeptDaoImpl();
		Dept mydept = new Dept();
		mydept.setDepName("myname");
		mydept.setDeplocation("mylocation");
		
		dao.save(mydept);
	}
	@Test
	public void testFind() {

		DeptDAO dao = new DeptDaoImpl();
		Dept mydept = dao.find(1);
		System.out.println(mydept.getDepName()+"------"+mydept.getDeplocation()+"------");
	}
	@Test
	public void testUpdate(){
		
		DeptDAO dao = new DeptDaoImpl();
		Dept mydept = dao.find(1);
		mydept.setDepName("lisi");
		dao.update(mydept);
	}
	@Test
	public void testDelte(){
		
		DeptDAO dao = new DeptDaoImpl();
		Dept mydept = dao.find(1);
		dao.delete(mydept);
	}
	
}

