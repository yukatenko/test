package tac;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Security;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class Main {
	private JTextField keyTextField, inputFileTextField, incorrectFileTextField, TACTextField;
	private JButton addButton, selectButton;
	private JLabel addLabel, selectLabel;
	
	public Main() {
		JFrame frame = new JFrame("");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		panel.setLayout(new GridLayout(13, 1, 2, 2));
		
		// ключ
		panel.add(new JLabel("Файл с ключом шифрования"));
		panel.add(keyTextField = new JTextField());
		
		// добавление данных
		panel.add(new JLabel("Добавление записей в базу данных"));
		panel.add(new JLabel("Файл с данными"));
		panel.add(inputFileTextField = new JTextField());
		panel.add(new JLabel("Файл для записи некорректных строк"));
		panel.add(incorrectFileTextField = new JTextField());
		panel.add(addButton = new JButton("Добавить"));
		panel.add(addLabel = new JLabel());
				
		// выборка данных
		panel.add(new JLabel("Поиск записи по TAC"));
		panel.add(TACTextField = new JTextField());
		panel.add(selectButton = new JButton("Поиск"));
		panel.add(selectLabel = new JLabel());
		
		
		// задаем фрейм, устанавливаем панель
		frame.setContentPane(panel);
						
		// задаем размеры и видимость
		frame.setSize(400, 400);
		frame.setVisible(true);
		
		Engine engine = new Engine();
		addButton.addActionListener(engine);
		selectButton.addActionListener(engine);
		
	}
	
	class Engine implements ActionListener {
		private Session session;
		Engine() {
			Security.addProvider(new BouncyCastleProvider());
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
		}
	
		@Override
		public void actionPerformed(ActionEvent e) {
			Object src = e.getSource();
			if (src == addButton) {
				addLabel.setText(addData(session, inputFileTextField.getText(), incorrectFileTextField.getText(), true, keyTextField.getText()));
			}
			if (src == selectButton) {
				selectLabel.setText(selectData(session, TACTextField.getText(), keyTextField.getText()));
			}
		}
		
		// добавление данных из файла в БД
		// header - пропуск первой строки
		public String addData(Session session, String filename, String incorrectFilename, boolean header, String keyFile) {
			String result = null;
			try {
				String key = key(keyFile);
				if (key == null) {
					throw new Exception();
				}
				
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
					while ((str = bufferedReader.readLine()) != null) {
										
						if (isCorrect(str)) {
							String[] parts = {"", "", "", "", "", ""};
							String[] temp = str.split(",");
							System.arraycopy(temp, 0, parts, 0, temp.length);
							
							List<TAC> TACList = session.createCriteria(TAC.class).add(Restrictions.like("TAC", parts[0].getBytes())).list();
							if (TACList.isEmpty()) {
								TAC tac = new TAC(parts);
								tac.encrypt(key);
								session.save(tac);
							}							
							
						} else {
							System.out.println(isCorrect(str));
							try (FileWriter fileWriter = new FileWriter(incorrectFilename, true)) {
								fileWriter.write(str + "\n");
							} catch(IOException e) {
								result = "IOException";
							} 	
						}
					}
					session.getTransaction().commit();
					result = "Data saved";
				} catch(IOException e) {
					result = "IOException";
				} 			
				
				
			} catch (Exception e) {
				result = "Something is wrong";
				session.getTransaction().rollback();
			} finally {
				return result;
			}
		}
		
		// выбор из БД
		@SuppressWarnings("finally")
		public String selectData(Session session, String tacString, String keyFile) {
			String str = null;
			try {
				String key = key(keyFile);
				if (key == null) {
					throw new Exception();
				}
				List<TAC> TACList = null;				
				try {
					session.beginTransaction();
					TACList = session.createCriteria(TAC.class).add(Restrictions.like("TAC", tacString.getBytes())).setMaxResults(1).list();		
					session.getTransaction().commit();
				} catch (Exception e) {
					session.getTransaction().rollback();
					e.printStackTrace();
				} 
				if (TACList.isEmpty()) {
					str = "TAC not found";
				} else {
					TAC tac = TACList.get(0);				
					tac.decrypt(key);
					str = tac.toString();				
				}
			} catch (Exception e) {
				str = "Something is wrong";
			} finally {
				return str;
			}
		}
		
		//проверка корректности TAC
		public boolean isCorrect(String tac) {
			Pattern pattern = Pattern.compile("[0-9]{8},[^,]*,[^,]*,[^,]*,[^,]*,[^,]*");
			return pattern.matcher(tac).matches();
		}
		
		// ключ
		public String key(String keyFile) {
			String key;
			try (BufferedReader bufferedReader = new BufferedReader(new FileReader(keyFile))) {
				key = bufferedReader.readLine();
				if (key.length() != 16) {
					key = null;
				}
			} catch(IOException e) {
				System.out.println("IOException");
				key = null;
			}
			return key;
		}		
	}
	
	public static void main (String[] args) {
		new Main();
	}
}