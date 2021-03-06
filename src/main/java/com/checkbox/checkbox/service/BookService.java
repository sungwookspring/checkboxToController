package com.checkbox.checkbox.service;

import com.checkbox.checkbox.domain.Book;
import com.checkbox.checkbox.domain.BookCategory;
import com.checkbox.checkbox.domain.Category;
import com.checkbox.checkbox.domain.Dto.Book.BookRequestAddDto;
import com.checkbox.checkbox.domain.Dto.Book.BookRequestUpdateDto;
import com.checkbox.checkbox.domain.Dto.Book.BookResponseFindOneDto;
import com.checkbox.checkbox.domain.Dto.Book.BookResponseListallDto;
import com.checkbox.checkbox.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    private final CategoryService categoryService;
    private final BookCategoryService bookCategoryService;

    @Transactional
    public Long save(BookRequestAddDto requestAddDto){
        Book book = requestAddDto.toEntity();

        return bookRepository.save(book).getId();
    }

    /***
     * 단순하게 모든 책을 조회
     * @return
     */
    public List<Book> findAll(){
        List<Book> books = bookRepository.findAll();

        return books;
    }

    /***
     * 뷰에서 보여주기 위해 모든 책을 조회
     *
     */
    public List<BookResponseListallDto> findAllForView(){
        List<Book> books = bookRepository.findAll();
        List<BookResponseListallDto> responseDto = books.stream()
                .map(book -> new BookResponseListallDto(book))
                .collect(Collectors.toList());



        return responseDto;
    }

    public Book findByTitle(String title){
        Book findBook = bookRepository.findByTitle(title)
                .orElseThrow(
                        () -> new IllegalStateException("존재하지 않은 책")
                );
        return findBook;
    }

    public BookResponseFindOneDto findOneById(Long id){
        Book book =  bookRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalStateException("존재하지 않은 ID")
                );

        return BookResponseFindOneDto.builder()
                .entity(book)
                .build();
    }

    @Transactional
    public void setRelationship(Book book, BookCategory bookCategory){
        book.setRelationWithBookCategory(bookCategory);
    }

    @Transactional
    public void setRelationship(String book_title, String category_name){
        // 카테고리를 찾고
        Book findBook = this.findByTitle(book_title);
        Category findCategory = categoryService.findByName(category_name);

        BookCategory bookCategory = BookCategory.builder()
                .category(findCategory)
                .build();

        bookCategoryService.save(bookCategory);

        // 연결
        findBook.setRelationWithBookCategory(bookCategory);
    }

    @Transactional
    public Long update(Long id, BookRequestUpdateDto requestUpdateDto){
        Book findBook = bookRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalStateException("존재하지 않은 ID")
                );

        findBook.update(requestUpdateDto.getTitle(),
                    requestUpdateDto.getAuthor(),
                    requestUpdateDto.isReaded());

        return id;
    }

    @Transactional
    public Long delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalStateException("존재하지 않은 ID로 삭제 실패")
                );

        bookRepository.delete(book);

        return id;
    }
}
