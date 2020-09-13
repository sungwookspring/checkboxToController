package com.checkbox.checkbox.service;

import com.checkbox.checkbox.domain.Dto.Category.CategoryRequestAddDto;
import com.checkbox.checkbox.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public Long save(CategoryRequestAddDto requestAddDto){
        Long saveId = categoryRepository.save(requestAddDto.toEntity()).getId();

        return saveId;
    }
}
