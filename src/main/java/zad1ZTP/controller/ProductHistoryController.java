package zad1ZTP.controller;

import zad1ZTP.model.ProductHistory;
import zad1ZTP.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductHistoryController {

    private final ProductService productService;

    @GetMapping("/{id}/history")
    public List<ProductHistory> history(@PathVariable Long id) {
        return productService.getHistory(id);
    }
}
