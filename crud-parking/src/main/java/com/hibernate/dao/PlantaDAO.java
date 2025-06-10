package com.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernate.model.Planta;
import com.hibernate.util.HibernateUtil;

public class PlantaDAO {
	//INSERTAR
	public void insertPlanta(Planta p) {
		Transaction transaction = null;
		try (Session session=HibernateUtil.getSessionFactory().openSession()){
			transaction = session.beginTransaction();
			session.persist(p);
			transaction.commit();
		}catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}
	//ACTUALIZAR
	public void updatePlanta(Planta p) {
		Transaction transaction = null;
		try (Session session=HibernateUtil.getSessionFactory().openSession()){
			transaction = session.beginTransaction();
			session.merge(p);
			transaction.commit();
		}catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}
	//ELIMINAR
	public void deletePlanta(int id) {
		Transaction transaction = null;
		Planta p = null;
		try (Session session=HibernateUtil.getSessionFactory().openSession()){
			transaction = session.beginTransaction();
			p = session.get(Planta.class, id);
			session.remove(p);
			transaction.commit();
		}catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}
	//SELECCIÓN SIMPLE
	public Planta selectPlantaById (int id) {
		Transaction transaction = null;
		Planta p = null;
		try (Session session=HibernateUtil.getSessionFactory().openSession()){
			transaction = session.beginTransaction();
			p = session.get(Planta.class, id);
			transaction.commit();
		}catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return p;
	}
	//SELECCIÓN MÚLTIPLE
	public List<Planta> selectAllPlantas(){
		Transaction transaction = null;
		List<Planta> plantas = null;
		try (Session session=HibernateUtil.getSessionFactory().openSession()){
			transaction = session.beginTransaction();
			plantas = session.createQuery("from Parking", Planta.class).getResultList();
			session.remove(p);
			transaction.commit();
		}catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}
}
