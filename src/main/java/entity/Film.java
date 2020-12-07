package entity;

import java.io.IOException;
import java.time.LocalDate;

public class Film {

	private Integer id;
	private String titre;
	private String resume;
	private LocalDate dateSortie;
	private Integer duree;
	private String realisateur;
	private String acteur;
	private String imageName;
	private String urlBA;
	private Genre genre;
	private Integer valide;


	public Film() {};
	
	public Film(Integer id, String titre, String resume, LocalDate dateSortie, Integer duree, String realisateur,
				String acteur, String imageName, String urlBA, Genre genre, Integer valide) throws IOException {
		this.id = id;
		this.titre = titre;
		this.resume = resume;
		this.dateSortie = dateSortie;
		this.duree = duree;
		this.realisateur = realisateur;
		this.acteur = acteur;
		this.imageName = imageName;
		this.urlBA = urlBA;
		this.genre = genre;
		this.valide = valide;
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
	
	public String getResume() {
		return resume;
	}
	public void setResume(String resume) {
		this.resume = resume;
	}
	
	public LocalDate getDateSortie() {
		return dateSortie;
	}
	public void setDateSortie(LocalDate dateSortie) {
		this.dateSortie = dateSortie;
	}
	
	public Integer getDuree() {
		return duree;
	}
	public void setDuree(Integer duree) {
		this.duree = duree;
	}
	
	public String getRealisateur() {
		return realisateur;
	}
	public void setRealisateur(String realisateur) {
		this.realisateur = realisateur;
	}
	
	public String getActeur() {
		return acteur;
	}
	public void setActeur(String acteur) {
		this.acteur = acteur;
	}
	
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public String getUrlBA() {
		return urlBA;
	}
	public void setUrlBA(String urlBA) {
		this.urlBA = urlBA;
	}

	public Genre getGenre() {
		return genre;
	}
	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public Integer getValide() {
		return valide;
	}
	public void setValide(Integer valide) {
		this.valide = valide;
	}
}
