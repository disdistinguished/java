package com.dao.hibernate;

import com.model.hibernate.Dept;

public interface DeptDAO {
         
	void save(Dept mydept);
	Dept find(int id);
	void update(Dept mydept);
	void delete(Dept mydept);
}
