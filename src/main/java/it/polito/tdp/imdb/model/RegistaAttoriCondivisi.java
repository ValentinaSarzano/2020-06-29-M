package it.polito.tdp.imdb.model;

public class RegistaAttoriCondivisi {
	
	private Director d;
	private Integer attoriCondivisi;
	
	public RegistaAttoriCondivisi(Director d, Integer attoriCondivisi) {
		super();
		this.d = d;
		this.attoriCondivisi = attoriCondivisi;
	}

	public Director getD() {
		return d;
	}

	public void setD(Director d) {
		this.d = d;
	}

	public Integer getAttoriCondivisi() {
		return attoriCondivisi;
	}

	public void setAttoriCondivisi(Integer attoriCondivisi) {
		this.attoriCondivisi = attoriCondivisi;
	}
	
	

}
