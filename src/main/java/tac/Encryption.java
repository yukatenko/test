package tac;

import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

	public static void encrypt(TAC tac, String key) {
		Cipher encryptCipher = null;
		byte[] plain = null;
		byte[] result = null;
		byte[] IV = Arrays.copyOf(tac.getTAC().getBytes(), 16);
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
			plain = tac.getManufacturer();
			result = new byte[encryptCipher.getOutputSize(plain.length)];
			len = encryptCipher.update(plain, 0, plain.length, result, 0);
			encryptCipher.doFinal(result, len);
			tac.setManufacturer(result);

			// model
			plain = tac.getModel();
			result = new byte[encryptCipher.getOutputSize(plain.length)];
			len = encryptCipher.update(plain, 0, plain.length, result, 0);
			encryptCipher.doFinal(result, len);
			tac.setModel(result);

			// HWType
			plain = tac.getHWType();
			result = new byte[encryptCipher.getOutputSize(plain.length)];
			len = encryptCipher.update(plain, 0, plain.length, result, 0);
			encryptCipher.doFinal(result, len);
			tac.setHWType(result);

			// OS
			plain = tac.getOS();
			result = new byte[encryptCipher.getOutputSize(plain.length)];
			len = encryptCipher.update(plain, 0, plain.length, result, 0);
			encryptCipher.doFinal(result, len);
			tac.setOS(result);

			// year
			plain = tac.getYear();
			result = new byte[encryptCipher.getOutputSize(plain.length)];
			len = encryptCipher.update(plain, 0, plain.length, result, 0);
			encryptCipher.doFinal(result, len);
			tac.setYear(result);

		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
		}
	}

	public static void decrypt(TAC tac, String key) {
		Cipher decryptCipher = null;
		byte[] ciphertext = null;
		byte[] plain = null;
		byte[] IV = Arrays.copyOf(tac.getTAC().getBytes(), 16);
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
			ciphertext = tac.getManufacturer();
			plain = new byte[decryptCipher.getOutputSize(ciphertext.length)];
			len = decryptCipher.update(ciphertext, 0, ciphertext.length, plain, 0);
			len += decryptCipher.doFinal(plain, len);
			plain = Arrays.copyOf(plain, len);
			tac.setManufacturer(plain);

			// model
			ciphertext = tac.getModel();
			plain = new byte[decryptCipher.getOutputSize(ciphertext.length)];
			len = decryptCipher.update(ciphertext, 0, ciphertext.length, plain, 0);
			len += decryptCipher.doFinal(plain, len);
			plain = Arrays.copyOf(plain, len);
			tac.setModel(plain);

			// HWType
			ciphertext = tac.getHWType();
			plain = new byte[decryptCipher.getOutputSize(ciphertext.length)];
			len = decryptCipher.update(ciphertext, 0, ciphertext.length, plain, 0);
			len += decryptCipher.doFinal(plain, len);
			plain = Arrays.copyOf(plain, len);
			tac.setHWType(plain);

			// OS
			ciphertext = tac.getOS();
			plain = new byte[decryptCipher.getOutputSize(ciphertext.length)];
			len = decryptCipher.update(ciphertext, 0, ciphertext.length, plain, 0);
			len += decryptCipher.doFinal(plain, len);
			plain = Arrays.copyOf(plain, len);
			tac.setOS(plain);

			// year
			ciphertext = tac.getYear();
			plain = new byte[decryptCipher.getOutputSize(ciphertext.length)];
			len = decryptCipher.update(ciphertext, 0, ciphertext.length, plain, 0);
			len += decryptCipher.doFinal(plain, len);
			plain = Arrays.copyOf(plain, len);
			tac.setYear(plain);

			

		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
		}
	}

}
