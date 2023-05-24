package com.progetto.backOffice.model;

import java.util.Arrays;

public class DatiJson {
	

	
	String img,subTitle,modalita,title,subSubTitle;
	String [] what,soft_skills,requisiti,cosa_offriamo,nice_to_have;

	public DatiJson() {
		// TODO Auto-generated constructor stub
	}
	

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getModalita() {
		return modalita;
	}

	public void setModalita(String modalita) {
		this.modalita = modalita;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubSubTitle() {
		return subSubTitle;
	}

	public void setSubSubTitle(String subSubTitle) {
		this.subSubTitle = subSubTitle;
	}

	public String[] getWhat() {
		return what;
	}

	public void setWhat(String[] what) {
		this.what = what;
	}

	public String[] getSoft_skills() {
		return soft_skills;
	}

	public void setSoft_skills(String[] soft_skills) {
		this.soft_skills = soft_skills;
	}

	public String[] getRequisiti() {
		return requisiti;
	}

	public void setRequisiti(String[] requisiti) {
		this.requisiti = requisiti;
	}

	public String[] getCosa_offriamo() {
		return cosa_offriamo;
	}

	public void setCosa_offriamo(String[] cosa_offriamo) {
		this.cosa_offriamo = cosa_offriamo;
	}

	public String[] getNice_to_have() {
		return nice_to_have;
	}

	public void setNice_to_have(String[] nice_to_have) {
		this.nice_to_have = nice_to_have;
	}

	@Override
	public String toString() {
		return "DatiJson [img=" + img + ", subTitle=" + subTitle + ", modalita=" + modalita + ", title=" + title
				+ ", subSubTitle=" + subSubTitle + ", what=" + Arrays.toString(what) + ", soft_skills="
				+ Arrays.toString(soft_skills) + ", requisiti=" + Arrays.toString(requisiti) + ", cosa_offriamo="
				+ Arrays.toString(cosa_offriamo) + ", nice_to_have=" + Arrays.toString(nice_to_have) + "]";
	}
	
	
}
