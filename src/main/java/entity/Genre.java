package entity;

public class Genre {

	private Integer id;
	private String nom;
	
	public Genre(Integer id, String nom) {
		this.id = id;
		this.nom = nom;
	}

	public Genre(String nom) {
		this.nom = nom;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
}
