package tac;

import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//TAC,Manufacturer,Model,HW-Type,OS,Year

@Entity
@Table(name = "TAC")
public class TAC {
	
	@Id
	@Column(name = "tac")
	private String TAC;
	
	@Column(name = "manufacturer")
	private String manufacturer;
	
	@Column(name = "model")
	private String model;
	
	@Column(name = "hw_type")
	private String HWType;
	
	@Column(name = "os")
	private String OS;
	
	@Column(name = "year")
	private String year;
	
	public TAC() {
		
	}
	
	public TAC(String[] args) {
		this.setTAC(args[0]);
		this.setManufacturer(args[1]);
		this.setModel(args[2]);
		this.setHWType(args[3]);
		this.setOS(args[4]);
		this.setYear(args[5]);
	}
	
	public String getTAC() {
		return TAC;
	}
	public void setTAC(String tAC) {
		TAC = tAC;
	}
	public String getModel() {
		return model;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getHWType() {
		return HWType;
	}
	public void setHWType(String hWType) {
		HWType = hWType;
	}
	public String getOS() {
		return OS;
	}
	public void setOS(String oS) {
		OS = oS;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String toString() {
		return TAC + " - " + manufacturer + " - " + model + " - " + HWType + " - " + OS + " - " + year;
	}
	
	public void encrypt(String key) {
		Cipher encryptCipher = null;		
		byte [] plain = null;
		byte [] result = null;
		byte[] IV = (TAC + TAC).getBytes();
		int len;
		
		try {
			//
			encryptCipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			
			// секретный ключ
			SecretKey keyValue = new SecretKeySpec(key.getBytes(), "AES");
			// вектор инициализации
			AlgorithmParameterSpec IVSpec = new IvParameterSpec(IV);
			// инициализация шифра
			encryptCipher.init(Cipher.ENCRYPT_MODE, keyValue, IVSpec);
			// 
			
			// manufacturer
			plain = manufacturer.getBytes();
			result = new byte[encryptCipher.getOutputSize(plain.length)];
			len = encryptCipher.update(plain, 0, plain.length, result, 0);
			encryptCipher.doFinal(result, len);
			setManufacturer(new String(result));
			
			// model
			plain = model.getBytes();
			result = new byte[encryptCipher.getOutputSize(plain.length)];
			len = encryptCipher.update(plain, 0, plain.length, result, 0);
			encryptCipher.doFinal(result, len);
			setModel(new String(result));
			
			// HWType
			plain = HWType.getBytes();
			result = new byte[encryptCipher.getOutputSize(plain.length)];
			len = encryptCipher.update(plain, 0, plain.length, result, 0);
			encryptCipher.doFinal(result, len);
			setHWType(new String(result));
			
			// OS
			plain = OS.getBytes();
			result = new byte[encryptCipher.getOutputSize(plain.length)];
			len = encryptCipher.update(plain, 0, plain.length, result, 0);
			encryptCipher.doFinal(result, len);
			setOS(new String(result));
			
			// year
			plain = year.getBytes();
			result = new byte[encryptCipher.getOutputSize(plain.length)];
			len = encryptCipher.update(plain, 0, plain.length, result, 0);
			encryptCipher.doFinal(result, len);
			setYear(new String(result));	
			
		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
		}
	}
	
	public void decrypt(String key) {
		Cipher decryptCipher = null;		
		byte [] ciphertext = null;
		byte [] plain = null;		
		byte[] IV = (TAC + TAC).getBytes();
		int len;
		
		try {
			//
			decryptCipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			
			// секретный ключ
			SecretKey keyValue = new SecretKeySpec(key.getBytes(), "AES");
			// вектор инициализации
			AlgorithmParameterSpec IVSpec = new IvParameterSpec(IV);
			// инициализация шифра
			decryptCipher.init(Cipher.DECRYPT_MODE, keyValue, IVSpec);
			// 
			
			// manufacturer
			ciphertext = manufacturer.getBytes();
			plain = new byte[decryptCipher.getOutputSize(ciphertext.length)];
			len = decryptCipher.update(ciphertext, 0, ciphertext.length, plain, 0);
			decryptCipher.doFinal(plain, len);
			setManufacturer(new String(plain));
			
			// model
			ciphertext = model.getBytes();
			plain = new byte[decryptCipher.getOutputSize(ciphertext.length)];
			len = decryptCipher.update(ciphertext, 0, ciphertext.length, plain, 0);
			decryptCipher.doFinal(plain, len);
			setModel(new String(plain));
			
			// HWType
			ciphertext = HWType.getBytes();
			plain = new byte[decryptCipher.getOutputSize(ciphertext.length)];
			len = decryptCipher.update(ciphertext, 0, ciphertext.length, plain, 0);
			decryptCipher.doFinal(plain, len);
			setHWType(new String(plain));
			
			// OS
			ciphertext = OS.getBytes();
			plain = new byte[decryptCipher.getOutputSize(ciphertext.length)];
			len = decryptCipher.update(ciphertext, 0, ciphertext.length, plain, 0);
			decryptCipher.doFinal(plain, len);
			setOS(new String(plain));
			
			// year
			ciphertext = year.getBytes();
			plain = new byte[decryptCipher.getOutputSize(ciphertext.length)];
			len = decryptCipher.update(ciphertext, 0, ciphertext.length, plain, 0);
			decryptCipher.doFinal(plain, len);
			setYear(new String(plain));	
			
		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
		}
	}
}
