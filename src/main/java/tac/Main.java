package tac;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.Security;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class Main {
	public static void main (String[] args) {
		
		Security.addProvider(new BouncyCastleProvider());
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		
		
		String filename = "c:/phone1.csv";
		String key = "0000000000000000";

		addData(session, filename, true, key);
		
/*		List<TAC> TACList = selectData(session, "01124500");
		for (TAC tac : TACList) {
			tac.decrypt(key);
			System.out.println(tac.toString());
		}
*/
	/*	String test = "01161200,Apple,iPhone 3G,smartphone,iOS, ";
		TAC tac = new TAC(test.split(","));
		
		tac.encrypt(key);
		System.out.println(tac.toString());
		String problem = tac.toString();
		byte [] problemArr = problem.getBytes();
		for (byte b : problemArr) {
			System.out.print(b + " ");
		}*/
		
		session.close();
		sessionFactory.close();

	}
	
	// ���������� ������ �� ����� � ��
	// header - ������� ������ ������
	public static void addData(Session session, String filename, boolean header, String key) {
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
						tac.encrypt(key);
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