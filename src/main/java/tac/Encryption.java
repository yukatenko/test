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
		byte[] IV = Arrays.copyOf(tac.getTAC().getBytes(), 16);

		try {
			encryptCipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			// секретный ключ
			SecretKey keyValue = new SecretKeySpec(key.getBytes(), "AES");
			// вектор инициализации
			AlgorithmParameterSpec IVSpec = new IvParameterSpec(IV);
			// инициализация шифра
			encryptCipher.init(Cipher.ENCRYPT_MODE, keyValue, IVSpec);
			
			tac.setManufacturer(encryption(encryptCipher, tac.getManufacturer()));
			tac.setModel(encryption(encryptCipher, tac.getModel()));
			tac.setHWType(encryption(encryptCipher, tac.getHWType()));
			tac.setOS(encryption(encryptCipher, tac.getOS()));
			tac.setYear(encryption(encryptCipher, tac.getYear()));
		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
		}
	}

	public static void decrypt(TAC tac, String key) {
		Cipher decryptCipher = null;
		byte[] IV = Arrays.copyOf(tac.getTAC().getBytes(), 16);

		try {
			decryptCipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			// секретный ключ
			SecretKey keyValue = new SecretKeySpec(key.getBytes(), "AES");
			// вектор инициализации
			AlgorithmParameterSpec IVSpec = new IvParameterSpec(IV);
			// инициализация шифра
			decryptCipher.init(Cipher.DECRYPT_MODE, keyValue, IVSpec);
			
			tac.setManufacturer(encryption(decryptCipher, tac.getManufacturer()));
			tac.setModel(encryption(decryptCipher, tac.getModel()));
			tac.setHWType(encryption(decryptCipher, tac.getHWType()));
			tac.setOS(encryption(decryptCipher, tac.getOS()));
			tac.setYear(encryption(decryptCipher, tac.getYear()));
		} catch (Exception e) {
			System.out.println("Exception message: " + e.getMessage());
		}
	}

	public static byte[] encryption(Cipher cipher, byte[] input) {
		byte[] output = null;
		try {
			output = new byte[cipher.getOutputSize(input.length)];
			int len = cipher.update(input, 0, input.length, output, 0);
			len += cipher.doFinal(output, len);
			if (len < output.length) {
				output = Arrays.copyOf(output, len);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			output = null;
		}
		return output;
	}

}
