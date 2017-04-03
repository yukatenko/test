package tac;

import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Main {
	public static void main (String[] args) {

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		
		List<TAC> TAC = null;
	
		try {
			session.beginTransaction();	
			
			SQLQuery query = session.createSQLQuery("COPY tac FROM 'c:/phone1.csv' DELIMITER ',' CSV HEADER");
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			session.close();
			sessionFactory.close();
		}
	}
}
