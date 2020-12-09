package entity;

public class Utilisateur {

	private Integer id;
	private String prenom;
	private String nom;
	private String email;
	private String mdpHash;
	private boolean admin;
	
	public Utilisateur(Integer id, String prenom, String nom, String email,String mdpHash, boolean admin) {
		this.id = id;
		this.prenom = prenom;
		this.nom = nom;
		this.email = email;
		this.mdpHash=mdpHash;
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
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public String getMdpHash() { 
		return mdpHash; 
	}
	public void setMdpHash(String mdpHash) {
		this.mdpHash = mdpHash;	
	}
}
