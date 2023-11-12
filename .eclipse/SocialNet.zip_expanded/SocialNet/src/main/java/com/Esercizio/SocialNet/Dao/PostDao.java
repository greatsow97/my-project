package com.Esercizio.SocialNet.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.Esercizio.SocialNet.Model.Post;

public interface PostDao extends JpaRepository<Post,Integer> {

	@Query(value="select post.* from post join commenti on commenti.id_post = post.id where post.id_utente = :userId and commenti.visto = 0 group by post.id",nativeQuery = true)
	
List<Post> notifications(Integer userId);
	
}
