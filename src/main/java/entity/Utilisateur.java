package entity;

public class Utilisateur {

	private Integer id;
	private String prenom;
	private String nom;
	private String email;
	private String mdp;
	private boolean admin;
	
	public Utilisateur(Integer id, String prenom, String nom, String email, String mdp, boolean admin) {
		this.id = id;
		this.prenom = prenom;
		this.nom = nom;
		this.email = email;
		this.mdp = mdp;
		this.admin = admin;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMdp() {
		return mdp;
	}
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}
