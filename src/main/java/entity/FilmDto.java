package entity;

import java.io.IOException;
public class FilmDto {
	
	private Integer id;
	private String titre;
	private String avis;
	private Boolean favori;
	private Integer pourcentage;

	public FilmDto(Integer id, String titre, String avis, Boolean favori, Integer pourcentage) throws IOException {
		this.id = id;
		this.titre = titre;
		this.avis = avis;
		this.favori = favori;
		this.pourcentage = pourcentage;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	
	public Boolean getFavori() {
		return favori;
	}
	public void setFavori(Boolean favori) {
		this.favori = favori;
	}
	
	public String getAvis() {
		return avis;
	}
	public void setAvis(String avis) {
		this.avis = avis;
	}

	public Integer getPourcentage() {
		return pourcentage;
	}

	public void setPourcentage(Integer pourcentage) {
		this.pourcentage = pourcentage;
	}
}
