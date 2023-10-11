package com.example.blogpessoal.repository;

import com.example.blogpessoal.model.Postagens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostagensRepository extends JpaRepository<Postagens,Long> {

    public List<Postagens> findAllByTituloContainingIgnoreCase(@Param("titulo") String titulo);
}
