package tac;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class Main {
	public static void main (String[] args) {

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		
		
		String filename = "c:/phone.csv";

		addData(session, filename, true);
		
		List<TAC> TACList = selectData(session, "01124500");
		for (TAC tac : TACList) {
			System.out.println(tac.toString());
		}
		
		session.close();
		sessionFactory.close();

	}
	
	// добавление данных из файла в БД
	// header - пропуск первой строки
	public static void addData(Session session, String filename, boolean header) {
		try {	
			session.beginTransaction();				
			
			String str;			
						
			// необходимо проверять корректность каждой строки
			// открываем файл для чтения, считываем строку, делим ее на сегменты, помещаем их в массив
			// длина массива должна быть равна 6
			// корректные строки добавляем в таблицу
			try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
				
				// пропуск первой строки
				if (header) {
					str = bufferedReader.readLine();
				}
				
				// что делать, если строка кончается на ","?
				while ((str = bufferedReader.readLine()) != null) {
					
					// дописываем пробел, если строка кончается на "," - не очень изящно
					if (str.endsWith(",")) {
						str = str + " ";
					}
					
					String[] parts = str.split(",");
					
					// проверка уникальности TAC - нужен индекс?
					List<TAC> TACList = session.createCriteria(TAC.class).add(Restrictions.like("TAC", parts[0])).list();
					
					if ((parts.length == 6) && TACList.isEmpty()) {
						TAC tac = new TAC(parts);
						session.save(tac);
					} 
					
				}					
			} catch(IOException e) {
				System.out.println("IOException");
			}
			
			session.getTransaction().commit();
			
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} 
	}

	// выбор из БД
	public static List<TAC> selectData(Session session, String tac) {
		List<TAC> TACList = null;
	
		try {
			session.beginTransaction();
			TACList = session.createCriteria(TAC.class).add(Restrictions.like("TAC", tac)).list();		
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} 
		return TACList;
	}
}