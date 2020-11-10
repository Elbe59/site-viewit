package entity;

import java.time.LocalDate;

public class Commentaire {

	private Integer id;
	private LocalDate date;
	private String contenu;
	private Film film;
	private Utilisateur user;
	
	public Commentaire(Integer id, LocalDate date, String contenu, Film film, Utilisateur user) {
		this.id = id;
		this.date = date;
		this.contenu = contenu;
		this.film = film;
		this.user = user;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getContenu() {
		return contenu;
	}
	public void setContenu(String contenu) {
		this.contenu = contenu;
	}
	public Film getFilm() {
		return film;
	}
	public void setFilm(Film film) {
		this.film = film;
	}
	public Utilisateur getUser() {
		return user;
	}
	public void setUser(Utilisateur user) {
		this.user = user;
	}
	
}
