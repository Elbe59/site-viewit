package entity;

import java.io.IOException;
public class FilmDto {
	
	private Integer id;
	private String titre;
	private String avis;
	private Boolean favori;

	public FilmDto(Integer id, String titre, String avis, Boolean favori) throws IOException {
		this.id = id;
		this.titre = titre;
		this.avis = avis;
		this.favori = favori;
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
}
