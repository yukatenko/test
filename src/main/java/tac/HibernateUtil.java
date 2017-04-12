package tac;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.postgresql.util.PSQLException;

public class HibernateUtil {
	
	private static SessionFactory sessionFactory = null;
	
	static {
		try {
			Configuration cfg = new Configuration().configure();
			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
					.applySettings(cfg.getProperties());
			cfg.addAnnotatedClass(TAC.class);
			
			sessionFactory = cfg.buildSessionFactory(builder.build());
		} catch (Exception e) {
			System.out.println("Initial SessionFactory failed: " + e.getMessage());
			System.exit(0);
		}
			
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
