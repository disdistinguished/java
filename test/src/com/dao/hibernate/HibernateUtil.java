package com.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory = null;
	//线程管理
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	
	static{
		try{
			Configuration cfg = new Configuration().configure();
			sessionFactory = cfg.buildSessionFactory();
		}catch(Throwable t){
			throw new ExceptionInInitializerError(t);
		}
	}
	
	public static SessionFactory getSessionFactory(){
		return sessionFactory;
	}
	
	//获取线程安全的session
	public static Session getSession(){
		Session session = (Session)threadLocal.get();
		
		if(session==null||!session.isOpen()){
			if(sessionFactory==null){
				reBuildSessionFactory();
			}
			
			session = (sessionFactory!=null)?sessionFactory.openSession():null;
			
			threadLocal.set(session);
		}
		
		return session;
    }
	
	public static void closeSession(){
		Session session =(Session)threadLocal.get();
		threadLocal.set(null);
		if(session!=null){
			
			session.close();
		}
	}

	private static void reBuildSessionFactory() {

		try{
			Configuration cfg = new Configuration().configure();
			sessionFactory =cfg.buildSessionFactory();
		}catch(Throwable t){
			throw new ExceptionInInitializerError(t);
		}
	}
}