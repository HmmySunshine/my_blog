package com.fanzehao.blogsystem.response;

import com.fanzehao.blogsystem.model.Category;
import lombok.Data;
import java.util.List;

@Data
public class CategoryResponse {
    private List<Category> categories;
    private Long total;

    public CategoryResponse(List<Category> categories, Long total) {
        this.categories = categories;
        this.total = total;
    }
}
