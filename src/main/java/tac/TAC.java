package tac;

import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//TAC,Manufacturer,Model,HW-Type,OS,Year

/**
 * @author Prptsh
 *
 */
/**
 * @author Prptsh
 *
 */
@Entity
@Table(name = "TAC")
public class TAC {
	
	@Id
	@Column(name = "tac")
	private byte[] TAC;
	
	@Column(name = "manufacturer")
	private byte[] manufacturer;
	
	@Column(name = "model")
	private byte[] model;
	
	@Column(name = "hw_type")
	private byte[] HWType;
	
	@Column(name = "os")
	private byte[] OS;
	
	@Column(name = "year")
	private byte[] year;
	
	/**
	 * 
	 */
	public TAC() {
		
	}
	
	public TAC(String[] args) {
		setTAC(args[0].getBytes());
		setManufacturer(args[1].getBytes());
		setModel(args[2].getBytes());
		setHWType(args[3].getBytes());
		setOS(args[4].getBytes());
		setYear(args[5].getBytes());
	}

	public byte[] getTAC() {
		return TAC;
	}

	public void setTAC(byte[] tAC) {
		TAC = tAC;
	}

	public byte[] getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(byte[] manufacturer) {
		this.manufacturer = manufacturer;
	}

	public byte[] getModel() {
		return model;
	}

	public void setModel(byte[] model) {
		this.model = model;
	}

	public byte[] getHWType() {
		return HWType;
	}

	public void setHWType(byte[] hWType) {
		HWType = hWType;
	}

	public byte[] getOS() {
		return OS;
	}

	public void setOS(byte[] oS) {
		OS = oS;
	}

	public byte[] getYear() {
		return year;
	}

	public void setYear(byte[] year) {
		this.year = year;
	}
	
	public String toString() {
		return new String(TAC) + " - " + new String(manufacturer) + " - " + new String(model) + " - " + new String(HWType) + " - "
				+ new String(OS) + " - " + new String(year);		
	}
	
	
	
	
	public void encrypt(String key) {
		Cipher encryptCipher = null;		
		byte [] plain = null;
		byte [] result = null;
		byte[] IV = Arrays.copyOf(TAC, 16);
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
			plain = manufacturer;
			result = new byte[encryptCipher.getOutputSize(plain.length)];
			len = encryptCipher.update(plain, 0, plain.length, result, 0);
			encryptCipher.doFinal(result, len);
			setManufacturer(result);
			
			// model
			plain = model;
			result = new byte[encryptCipher.getOutputSize(plain.length)];
			len = encryptCipher.update(plain, 0, plain.length, result, 0);
			encryptCipher.doFinal(result, len);
			setModel(result);
			
			// HWType
			plain = HWType;
			result = new byte[encryptCipher.getOutputSize(plain.length)];
			len = encryptCipher.update(plain, 0, plain.length, result, 0);
			encryptCipher.doFinal(result, len);
			setHWType(result);
			
			// OS
			plain = OS;
			result = new byte[encryptCipher.getOutputSize(plain.length)];
			len = encryptCipher.update(plain, 0, plain.length, result, 0);
			encryptCipher.doFinal(result, len);
			setOS(result);
			
			// year
			plain = year;
			result = new byte[encryptCipher.getOutputSize(plain.length)];
			len = encryptCipher.update(plain, 0, plain.length, result, 0);
			encryptCipher.doFinal(result, len);
			setYear(result);	
			
		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
		}
	}
	
	public void decrypt(String key) {
		Cipher decryptCipher = null;		
		byte [] ciphertext = null;
		byte [] plain = null;		
		byte[] IV = Arrays.copyOf(TAC, 16);
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
			ciphertext = manufacturer;
			plain = new byte[decryptCipher.getOutputSize(ciphertext.length)];
			len = decryptCipher.update(ciphertext, 0, ciphertext.length, plain, 0);
			len += decryptCipher.doFinal(plain, len);
			plain = Arrays.copyOf(plain, len);
			setManufacturer(plain);
			
			// model
			ciphertext = model;
			plain = new byte[decryptCipher.getOutputSize(ciphertext.length)];
			len = decryptCipher.update(ciphertext, 0, ciphertext.length, plain, 0);
			len += decryptCipher.doFinal(plain, len);
			plain = Arrays.copyOf(plain, len);
			setModel(plain);
			
			// HWType
			ciphertext = HWType;
			plain = new byte[decryptCipher.getOutputSize(ciphertext.length)];
			len = decryptCipher.update(ciphertext, 0, ciphertext.length, plain, 0);
			len += decryptCipher.doFinal(plain, len);
			plain = Arrays.copyOf(plain, len);
			setHWType(plain);
			
			// OS
			ciphertext = OS;
			plain = new byte[decryptCipher.getOutputSize(ciphertext.length)];
			len = decryptCipher.update(ciphertext, 0, ciphertext.length, plain, 0);
			len += decryptCipher.doFinal(plain, len);
			plain = Arrays.copyOf(plain, len);
			setOS(plain);
			
			// year
			ciphertext = year;
			plain = new byte[decryptCipher.getOutputSize(ciphertext.length)];
			len = decryptCipher.update(ciphertext, 0, ciphertext.length, plain, 0);
			len += decryptCipher.doFinal(plain, len);
			plain = Arrays.copyOf(plain, len);
			setYear(plain);	
			
		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
		}
	}
}
