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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.postgresql.util.PSQLException;

public class Main {
	private JTextField keyTextField, inputFileTextField, incorrectFileTextField, TACTextField;
	private JButton addButton, selectButton;
	private JTextArea resultTextArea;

	public Main() {
		Engine engine = new Engine();
		JFrame frame = new JFrame("");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2, 2, 2));

		JPanel lPanel = new JPanel();
		lPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		lPanel.setLayout(new GridLayout(11, 1, 2, 2));

		// ключ
		lPanel.add(new JLabel("Файл с ключом шифрования"));
		lPanel.add(keyTextField = new JTextField());

		// добавление данных
		lPanel.add(new JLabel("Добавление записей в базу данных"));
		lPanel.add(new JLabel("Файл с данными"));
		lPanel.add(inputFileTextField = new JTextField());
		lPanel.add(new JLabel("Файл для записи некорректных строк"));
		lPanel.add(incorrectFileTextField = new JTextField());
		lPanel.add(addButton = new JButton("Добавить"));

		// выборка данных
		lPanel.add(new JLabel("Поиск записи по TAC"));
		lPanel.add(TACTextField = new JTextField());
		lPanel.add(selectButton = new JButton("Поиск"));

		JPanel rPanel = new JPanel();
		rPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rPanel.setLayout(new GridLayout(1, 1, 2, 2));
		rPanel.add(resultTextArea = new JTextArea());

		panel.add(lPanel);
		panel.add(rPanel);

		// задаем фрейм, устанавливаем панель
		frame.setContentPane(panel);

		// задаем размеры и видимость
		frame.setSize(600, 400);
		frame.setVisible(true);

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
				resultTextArea.setText(addData(session, keyTextField.getText(), inputFileTextField.getText(),
						incorrectFileTextField.getText(), true));
			}
			if (src == selectButton) {
				resultTextArea.setText(selectData(session, TACTextField.getText(), keyTextField.getText()));
			}
		}

		// добавление данных из файла в БД
		// header - пропуск первой строки
		public String addData(Session session, String keyFile, String filename, String incorrectFilename,
				boolean header) {
			String result = null;
			session.clear();
			try {
				String key;
				try {
					key = key(keyFile);
				} catch (Exception e) {
					throw e;
				}

				// необходимо проверять корректность каждой строки
				// открываем файл для чтения, считываем строку, делим ее на
				// сегменты, помещаем их в массив
				// длина массива должна быть равна 6
				// корректные строки добавляем в таблицу
				try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
						FileWriter fileWriter = new FileWriter(incorrectFilename, true)) {

					String str;
					// пропуск первой строки
					if (header) {
						str = bufferedReader.readLine();
					}
					session.beginTransaction();
					while ((str = bufferedReader.readLine()) != null) {

						if (isCorrect(str)) {
							String[] parts = { "", "", "", "", "", "" };
							String[] temp = str.split(",");
							System.arraycopy(temp, 0, parts, 0, temp.length);

							if (session.get(TAC.class, parts[0]) == null) {
								TAC tac = new TAC(parts);
								Encryption.encrypt(tac, key);
								session.save(tac);
							}
							

						} else {
							fileWriter.write(str + "\n");
						}
					}
					session.getTransaction().commit();
					result = "Data saved";
				} catch (IOException e) {
					session.getTransaction().rollback();
					result = "IOException " + e.getMessage();
				}

			} catch (Exception e) {
				result = "out" + e.getMessage();
			}
			return result;
		}

		// выбор из БД
		public String selectData(Session session, String tacString, String keyFile) {
			String result = null;
			try {
				String key = key(keyFile);
				List<TAC> TACList = null;
				try {
					session.beginTransaction();
					TACList = session.createCriteria(TAC.class).add(Restrictions.like("TAC", tacString)).list();
					session.getTransaction().commit();
				} catch (Exception e) {
					session.getTransaction().rollback();
					e.printStackTrace();
				}
				if (TACList.isEmpty()) {
					result = "TAC not found";
				} else {
					result = "";
					for (TAC tac : TACList) {
						Encryption.decrypt(tac, key);
						result += tac.toString() + "\n";
					}
					session.clear();
				}
			} catch (Exception e) {
				result = e.getMessage();
			}
			return result;
		}

		// проверка корректности TAC
		public boolean isCorrect(String tac) {
			Pattern pattern = Pattern.compile("[0-9]{8},[^,]*,[^,]*,[^,]*,[^,]*,[^,]*");
			return pattern.matcher(tac).matches();
		}

		// ключ
		public String key(String keyFile) throws Exception {
			String key = null;
			try (BufferedReader bufferedReader = new BufferedReader(new FileReader(keyFile))) {
				key = bufferedReader.readLine();
				if (key.length() != 16) {
					key = null;
					throw new Exception("Incorrect key");
				}
			} catch (IOException e) {
				throw new Exception("Key file exception");
			} catch (Exception e) {
				throw e;
			}
			return key;
		}
	}

	public static void main(String[] args) {
		new Main();
	}
}