package managedbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import sessionbeans.article.ArticleBeanLocal;
import sessionbeans.blogueur.BlogueurBeanLocal;
import persistence.Article;
import persistence.Blogueur;
import javax.faces.view.facelets.FaceletContext;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String login;
	private String pwd;
	private Blogueur blogueur;
	boolean logged;

	@EJB
	BlogueurBeanLocal blogueurBeanLocal;
	@EJB
	ArticleBeanLocal articleBeanLocal;
	private Article article = new Article();
	private Article articleDetail = new Article();
	private List<Article> articles = new ArrayList<Article>();

	@PostConstruct
	public void init() {
		// check if shop table is empty

		if (blogueurBeanLocal.getAllBloggers().size() == 0) {
			System.out.println("empty Blogueur table, filling up");
			// creating bloggers
			Blogueur blogueur = new Blogueur();

			blogueur.setLogin("ahmed");
			blogueur.setMotDePasse("esprit");
			blogueur.setNumTel(10203040);
			blogueurBeanLocal.addBlogger(blogueur);

			blogueur = new Blogueur();

			blogueur.setLogin("ali");
			blogueur.setMotDePasse("javaee");
			blogueur.setNumTel(80907050);
			blogueurBeanLocal.addBlogger(blogueur);

			if (articleBeanLocal.getAllArticles().size() == 0) {
				Article article = new Article();
				article.setTitre("javaEE 8");
				article.setContenu("Quelle nouveatés de javaEE 8");
				articleBeanLocal.addArticle(article);

				article = new Article();
				article.setTitre("Unity");
				article.setContenu("Controller pour le personnage d'un jeux");
				articleBeanLocal.addArticle(article);
			}

			List<Article> articles = new ArrayList<Article>();
			articles = articleBeanLocal.getAllArticles();

			assignerArticleToBlogger(1, articles);

		}

		articles = articleBeanLocal.findAllArticlesByBlogger(blogueur);

	}

	public void assignerArticleToBlogger(int idBlogger, List<Article> articles) {
		Blogueur blogueur = new Blogueur();
		blogueurBeanLocal.getBloggerById(idBlogger);
		for (Article article : articles) {
			article.setBlogueur(blogueur);
			articleBeanLocal.addArticle(article);
			System.out.println(blogueur.getIdBlogueur());
			articleBeanLocal.updateArticle(article);
		}

	}

	public void gotoDetail(int id) throws IOException {
		articleDetail = new Article();
		articleDetail = articleBeanLocal.getArticleById(id);
		FacesContext.getCurrentInstance().getExternalContext().redirect("/blogger/detail.jsf");
	}

	public void retour() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("/blogger/detail.jsf");

	}

	public String doLogin() {
		message = null;
		blogueur = blogueurBeanLocal.authentification(login, pwd);
		if (blogueur != null) {
			logged = true;
			articles = articleBeanLocal.findAllArticlesByBlogger(blogueur);
			return "/blogger/home?faces-redirect=true";
		}

		else {
			message = "Erreur d'authentification";
			return null;
		}

	}

	public String doRedirect() {

		return "/blogger/detail?faces-redirect=true";

	}

	public String logOut() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();

		return "/authentification.jsf?faces-redirect=true";
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public Blogueur getBlogueur() {
		return blogueur;
	}

	public void setBlogueur(Blogueur blogueur) {
		this.blogueur = blogueur;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	public Article getArticleDetail() {
		return articleDetail;
	}

	public void setArticleDetail(Article articleDetail) {
		this.articleDetail = articleDetail;
	}
}
