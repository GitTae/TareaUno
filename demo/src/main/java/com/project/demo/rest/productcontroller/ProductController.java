package com.project.demo.rest.productcontroller;

import com.project.demo.logic.entity.category.Category;
import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.product.ProductRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.rest.categorycontroller.CategoryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryController categoryController;



    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'SUPER_ADMIN')")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        System.out.println(product.getCategory());
        System.out.println(product.getCategorynumber());
        Category category =categoryController.getCategoryById(product.getCategorynumber());
        product.setCategory(category);
        return productRepository.save(product);
    }




    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Category category =categoryController.getCategoryById(product.getCategorynumber());
        product.setCategory(category);
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setCategorynumber(product.getCategorynumbers());
                    existingProduct.setCategory(product.getCategory());
                    return productRepository.save(existingProduct);
                })
                .orElseGet(() -> {
                    product.setId(id);
                    return productRepository.save(product);
                });
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
