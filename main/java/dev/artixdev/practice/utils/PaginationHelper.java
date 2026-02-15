package dev.artixdev.practice.utils;

import java.util.Collections;
import java.util.List;

public class PaginationHelper {
    
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 20;

    public static PaginationResult paginate(List<String> items, int page, int pageSize) {
        if (items == null || items.isEmpty()) {
            return new PaginationResult(Collections.emptyList(), 0, 0, 0);
        }

        int totalItems = items.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;
        
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);
        
        List<String> pageItems = items.subList(startIndex, endIndex);
        
        return new PaginationResult(pageItems, page, totalPages, totalItems);
    }

    public static class PaginationResult {
        private final List<String> items;
        private final int currentPage;
        private final int totalPages;
        private final int totalItems;

        public PaginationResult(List<String> items, int currentPage, int totalPages, int totalItems) {
            this.items = items;
            this.currentPage = currentPage;
            this.totalPages = totalPages;
            this.totalItems = totalItems;
        }

        public List<String> getItems() {
            return items;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public int getTotalItems() {
            return totalItems;
        }

        public boolean hasNextPage() {
            return currentPage < totalPages;
        }

        public boolean hasPreviousPage() {
            return currentPage > 1;
        }
    }
}
