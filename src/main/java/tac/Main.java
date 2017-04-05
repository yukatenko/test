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
	
	// ���������� ������ �� ����� � ��
	// header - ������� ������ ������
	public static void addData(Session session, String filename, boolean header) {
		try {	
			session.beginTransaction();				
			
			String str;			
						
			// ���������� ��������� ������������ ������ ������
			// ��������� ���� ��� ������, ��������� ������, ����� �� �� ��������, �������� �� � ������
			// ����� ������� ������ ���� ����� 6
			// ���������� ������ ��������� � �������
			try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
				
				// ������� ������ ������
				if (header) {
					str = bufferedReader.readLine();
				}
				
				// ��� ������, ���� ������ ��������� �� ","?
				while ((str = bufferedReader.readLine()) != null) {
					
					// ���������� ������, ���� ������ ��������� �� "," - �� ����� ������
					if (str.endsWith(",")) {
						str = str + " ";
					}
					
					String[] parts = str.split(",");
					
					// �������� ������������ TAC - ����� ������?
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

	// ����� �� ��
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