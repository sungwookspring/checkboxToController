package com.checkbox.checkbox.repository;

import com.checkbox.checkbox.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("select distinct b from Book b join fetch b.bookCategories bc")
    List<Book> findAllWithCategoryName();

    Optional<Book> findByTitle(String title);
}
