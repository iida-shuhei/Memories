package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Article;

/**
 * 記事を操作するリポジトリ.
 * 
 * @author iidashuhei
 *
 */
@Repository
public class ArticleRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<Article>ARTICLE_ROW_MAPPER = (rs,i) -> {
	
		Article article = new Article();
		article.setId(rs.getInt("id"));
		article.setTitle(rs.getString("title"));
		article.setName(rs.getString("name"));
		article.setPrefecture(rs.getString("prefecture"));
		article.setContent(rs.getString("content"));
		article.setPostDate(rs.getDate("post_date"));
		article.setImagePath(rs.getString("image_path"));
		return article;
	};
	/**
	 * 記事を全件検索する.
	 * 
	 * @return 全記事
	 */
	public List<Article> findAll(){
		String sql = "select id,title,name,prefecture,content,post_date,image_path from articles order by id";
		return template.query(sql, ARTICLE_ROW_MAPPER);
	}
	/**
	 * タイトルから曖昧検索をする.
	 * 
	 * @param name 名前
	 * @return 曖昧検索結果
	 */
	public List<Article> findByTitle(String title){
		String sql = "select id,title,name,prefecture,content,post_date,image_path from articles where title Ilike :title";
		SqlParameterSource param = new MapSqlParameterSource().addValue("title",'%' + title + '%');
		return template.query(sql, param, ARTICLE_ROW_MAPPER);
	}
	/**
	 * 投稿者名から曖昧検索をする.
	 * 
	 * @param name 名前
	 * @return 曖昧検索結果
	 */
	public List<Article> findByName(String name){
		String sql = "select id,title,name,prefecture,content,post_date,image_path from articles where name Ilike :name";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name",'%' + name + '%');
		return template.query(sql, param, ARTICLE_ROW_MAPPER);
	}
	/**
	 * 内容から曖昧検索をする.
	 * 
	 * @param name 名前
	 * @return 曖昧検索結果
	 */
	public List<Article> findByContent(String content){
		String sql = "select id,title,name,prefecture,content,post_date,image_path from articles where content Ilike :content";
		SqlParameterSource param = new MapSqlParameterSource().addValue("content",'%' + content + '%');
		return template.query(sql, param, ARTICLE_ROW_MAPPER);
	}
	/**
	 * 記事の詳細を表示する.
	 * 
	 * @param id ID
	 * @return 記事詳細
	 */
	public Article load(Integer id) {

		String sql = "select id,title,name,prefecture,content,image_path,post_date from articles where id =:id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		return template.queryForObject(sql, param, ARTICLE_ROW_MAPPER);
	}
	
	/**
	 * 記事情報を登録する.
	 * 
	 * @param article　
	 */
	public void insert(Article article) {
		String sql = "insert into articles(title,name,prefecture,content,post_date,image_path)values(:title,:name,:prefecture,:content,:postDate,:imagePath)";
		SqlParameterSource param = new BeanPropertySqlParameterSource(article);
		template.update(sql, param);
	
	}
	
	/**
	 * 記事を更新する.
	 * 
	 * @param article　記事
	 */
	public void update(Article article) {
		String sql = "update articles set title=:title,name=:name,prefecture=:prefecture,content=:content,post_date=:postDate,image_path=:imagePath where id=:id";
		SqlParameterSource param = new BeanPropertySqlParameterSource(article);
		template.update(sql, param);
	}
	
	/**
	 * 記事を削除する.
	 * 
	 * @param id ID
	 */
	public void delete(Integer id) {
		String sql = "delete from articles where id=:id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, param);
	}
	
	/** 記事のタイトル,投稿者名,内容のいずれかで記事を曖昧検索する
	 * @param title
	 * @param name
	 * @param content
	 * @return
	 */
	
	public List<Article> findByArticleInfo(String title,String name,String content){
		
		//条件式を指定するurl(タイトルでの曖昧検索）
		String whereTitleSql="";
		if(!title.equals("")) {
			whereTitleSql = "where title like :title";	
		}else {
			whereTitleSql ="";
		}
		
		//条件式を指定するurl(名前での曖昧検索）
		String whereNameSql="";
		if(!name.equals("")) {
			whereNameSql = "where name like : name";
		}else {
			whereNameSql = "";
		}
		
		//条件式を指定するurl(内容での曖昧検索)
		String whereContentSql="";
		if(!content.equals("")) {
			whereContentSql = "where content like :content";
		}else {
			whereContentSql="";
					
		}
		
		//sqlを発行
		String sql = "select id,title,name,prefecture,content,post_date,image_path from articles"+whereTitleSql+whereNameSql+whereContentSql;
		SqlParameterSource param = new MapSqlParameterSource().addValue("title", "%"+whereTitleSql+"%").addValue("name", "%"+whereNameSql+"%").addValue("content", "%"+whereContentSql+"%");
		List<Article> articleList = template.query(sql, param,ARTICLE_ROW_MAPPER);
		return articleList;
		
	}
}