package tac;

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
}
