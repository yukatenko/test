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
@Entity
@Table(name = "TAC")
public class TAC {
	
	@Id
	@Column(name = "tac")
	private String TAC;
	
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

	public TAC() {
		
	}
	
	public TAC(String[] args) {
		setTAC(args[0]);
		setManufacturer(args[1].getBytes());
		setModel(args[2].getBytes());
		setHWType(args[3].getBytes());
		setOS(args[4].getBytes());
		setYear(args[5].getBytes());
	}

	public String getTAC() {
		return TAC;
	}

	public void setTAC(String tAC) {
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
	
}
