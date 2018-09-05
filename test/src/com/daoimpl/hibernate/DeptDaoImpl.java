package com.daoimpl.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.dao.hibernate.DeptDAO;
import com.dao.hibernate.HibernateUtil;
import com.model.hibernate.Dept;

public class DeptDaoImpl implements DeptDAO{
	
	//读取配置文件
	Configuration cfg = null;
	//工厂类
	SessionFactory sessionFactory = null;
	//数据会话
	Session session = null;
	//事物
	Transaction tx = null;
	
	@Override
	public void save(Dept mydept) {

		try{
			cfg =new Configuration().configure();
			sessionFactory = cfg.buildSessionFactory();
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.save(mydept);
			
			//提交事务
			tx.commit();
		}catch(HibernateException ex){
			ex.printStackTrace();
			tx.rollback();
		}finally{
			session.close();
		}
	}
	
	@Override
	public Dept find(int id) {
		Dept dep = null;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			dep = (Dept)session.get(Dept.class, id);
			tx.commit();
		}catch(HibernateException ex){
			ex.printStackTrace();
			tx.rollback();
		}finally{
			HibernateUtil.closeSession();
		}
		
		return dep;
	}
	
	@Override
	public void update(Dept mydept) {

		try{
			session = HibernateUtil.getSession();
			tx =session.beginTransaction();
			session.update(mydept);
			tx.commit();
		}catch(HibernateException ex){
			ex.printStackTrace();
			tx.rollback();
		}finally{
			HibernateUtil.closeSession();
		}
	}
	
	@Override
	public void delete(Dept mydept) {

		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			session.delete(mydept);
			tx.commit();
		}catch(HibernateException ex){
			ex.printStackTrace();
			tx.rollback();
		}finally{
			HibernateUtil.closeSession();
		}
	}
	public void setState(){
		try{
			session = HibernateUtil.getSession();
			tx =session.beginTransaction();
			
			//瞬间状态
			Dept mydept =new Dept();
			mydept.setDepName("changeState");
			mydept.setDeplocation("locationState");
			
			//持久状态
			session.save(mydept);
			
			mydept.setDepName("xxxx");
			
			tx.commit();
		}catch (HibernateException ex){
			ex.printStackTrace();
			tx.rollback();
		}finally{
			HibernateUtil.closeSession();
		}
	}
	public void setState2(){
		try {
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			//持久状态
			Dept mydept =session.get(Dept.class, 10);
			
			//游离状态
			session.clear();
			
			//瞬间状态
			mydept.setDepName("xxxxx");
			
			tx.commit();
		}catch(HibernateException ex){
			ex.printStackTrace();
			tx.rollback();
		}finally{
			HibernateUtil.closeSession();
		}
	}
	public void setState3(){
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			//游离状态
			Dept mydept=new Dept();
			mydept.setDepNo(11);
			mydept.setDepName("changeState2");
			mydept.setDeplocation("locationState2");
			Dept d2= session.get(Dept.class, 11);
			
			session.merge(mydept);
			
			tx.commit();
		}catch(HibernateException ex){
			ex.printStackTrace();
			tx.rollback();
		}finally{
			HibernateUtil.closeSession();
		}
	}
}
