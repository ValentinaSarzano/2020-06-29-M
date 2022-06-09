package it.polito.tdp.imdb.model;

public class RegistaAttoriCondivisi {

	Director d;
	private int nAttoriCondivisi;
	
	public RegistaAttoriCondivisi(Director d, int nAttoriCondivisi) {
		super();
		this.d = d;
		this.nAttoriCondivisi = nAttoriCondivisi;
	}

	public Director getD() {
		return d;
	}

	public void setD(Director d) {
		this.d = d;
	}

	public int getnAttoriCondivisi() {
		return nAttoriCondivisi;
	}

	public void setnAttoriCondivisi(int nAttoriCondivisi) {
		this.nAttoriCondivisi = nAttoriCondivisi;
	}

	@Override
	public String toString() {
		return d.getFirstName() +" "+ d.getLastName() +" - " +"# attori condivisi: "+ nAttoriCondivisi;
	}
	
	
	
}
