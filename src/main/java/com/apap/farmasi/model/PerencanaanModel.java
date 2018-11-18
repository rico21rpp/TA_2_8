package com.apap.farmasi.model;


import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "perencanaan")
public class PerencanaanModel implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@Column(name = "tanggal", nullable = false)
	private Date tanggal;
	
	@NotNull
	@Size(max = 255)
	@Column(name = "status", nullable = false)
	private String status;
	
	@NotNull
	@Column(name = "jumlah", nullable = false)
	private Integer jumlah;
	
	@OneToMany(mappedBy = "perencanaan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<MedicalSuppliesModel> listMedicalSupplies;
	
	public List<MedicalSuppliesModel> getListMedicalSupplies() {
		return listMedicalSupplies;
	}

	public void setListMedicalSupplies(List<MedicalSuppliesModel> listMedicalSupplies) {
		this.listMedicalSupplies = listMedicalSupplies;
	}

	public Integer getJumlah() {
		return jumlah;
	}

	public void setJumlah(Integer jumlah) {
		this.jumlah = jumlah;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getTanggal() {
		return tanggal;
	}

	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}