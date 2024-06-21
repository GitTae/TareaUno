package com.project.demo.rest.categorycontroller;

import com.project.demo.logic.entity.category.Category;
import com.project.demo.logic.entity.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/categorys")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;



    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'SUPER_ADMIN')")
    public List<Category> getAllCategorys() {
        return categoryRepository.findAll();
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PostMapping
    public Category addUser(@RequestBody Category category) {

        return categoryRepository.save(category);
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id).orElse(null);
    }


    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PutMapping("/{id}")
        public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(category.getName());
                    existingUser.setDescription(category.getDescription());
                    existingUser.setName(category.getName());
                    return categoryRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    category.setId(id);
                    return categoryRepository.save(category);
                });
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
    }


}
