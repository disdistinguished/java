package com.unittest.hibernate;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.Query;
import org.junit.Test;

import movie.Actor;
import movie.Film;

public class HQLTest {
	Configuration cfg = null;
	SessionFactory factory = null;
	Session session = null;
	Transaction tx = null;
	
	@Test
	public void test() {
		// TODO Auto-generated method stub
		try{
			cfg = new Configuration().configure("/movie.cfg.xml");
			factory = cfg.buildSessionFactory();
			session = factory.openSession();
			//创建HQL
			String HQL="from Actor";
			//创建query
			Query query = session.createQuery(HQL);
			//执行query
			List<Actor> list = query.list();
			//遍历list
			for(Actor actor : list){
				System.out.println(actor.getFirstName()+"----"+actor.getLastName());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	public void test2() {
		// TODO Auto-generated method stub
		try{
			cfg = new Configuration().configure("/movie.cfg.xml");
			factory = cfg.buildSessionFactory();
			session = factory.openSession();
			//创建HQL
			String HQL="from Actor";
			//创建query
			Query query = session.createQuery(HQL);
			//执行query
			Iterator<Actor> list = query.iterate();
			//遍历list
			Actor actor = null;
			while(list.hasNext()){
				actor = list.next();
				System.out.println(actor.getFirstName()+"-----"+actor.getLastName());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	public void test3() {
		// TODO Auto-generated method stub
		try{
			cfg = new Configuration().configure("/movie.cfg.xml");
			factory = cfg.buildSessionFactory();
			session = factory.openSession();
			//创建HQL
			String HQL="from Film where length <:len and title like :pat";
			
			//创建query
			Query query = session.createQuery(HQL);
			
			query.setInteger("len", 50);
			query.setString("pat","A%");
			
			//执行query
			Iterator<Film> list = query.iterate();
			//遍历list
			Film film = null;
			while(list.hasNext()){
				film = list.next();
				System.out.println(film.getTitle()+"-----"+film.getLength());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	public void test4() {
		// TODO Auto-generated method stub
		try{
			cfg = new Configuration().configure("/movie.cfg.xml");
			factory = cfg.buildSessionFactory();
			session = factory.openSession();
			//创建HQL
			String HQL="select title from Film where length <:len and title like :pat";
			
			//创建query
			Query query = session.createQuery(HQL);
			
			query.setInteger("len", 50);
			query.setString("pat","A%");
			
			//执行query
			Iterator<String> list = query.iterate();
			//遍历list
			String film = null;
			while(list.hasNext()){
				film = list.next();
				System.out.println(film);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	public void test5() {
		// TODO Auto-generated method stub
		try{
			cfg = new Configuration().configure("/movie.cfg.xml");
			factory = cfg.buildSessionFactory();
			session = factory.openSession();
			//创建HQL
			String HQL="select title,length from Film where length <:len and title like :pat";
			
			//创建query
			Query query = session.createQuery(HQL);
			
			query.setInteger("len", 50);
			query.setString("pat","A%");
			
			//执行query
			Iterator<Object[]> list = query.iterate();
			//遍历list
			Object[] film = null;
			while(list.hasNext()){
				film = list.next();
				for(int i=0;i<film.length;i++){
					System.out.println(film[i]+"----");
				}
				
				System.out.println();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
}
