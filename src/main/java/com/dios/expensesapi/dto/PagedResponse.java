package com.dios.expensesapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "Paginated response wrapper")
public class PagedResponse<T> {

    // ================================
    // FIELDS
    // ================================

    @Schema(description = "List of items in current page")
    private List<T> content;

    @Schema(description = "Pagination metadata")
    private PageInfo pagination;

    // ================================
    // CONSTRUCTORS
    // ================================

    public PagedResponse() {}

    public PagedResponse(Page<T> page) {
        this.content = page.getContent();
        this.pagination = new PageInfo(page);
    }

    // ================================
    // GETTERS AND SETTERS
    // ================================


    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public PageInfo getPagination() {
        return pagination;
    }

    public void setPagination(PageInfo pagination) {
        this.pagination = pagination;
    }

    @Schema(description = "Pagination information")
    public static class PageInfo {
        @Schema(description = "Current page number (0-based)", example = "1")
        private int page;

        @Schema(description = "Number of items per page", example = "10")
        private int size;

        @Schema(description = "Total number of items", example = "12")
        private long totalElements;

        @Schema(description = "Total number of pages", example = "2")
        private int totalPages;

        @Schema(description = "Whether this is the first page", example = "false")
        private boolean first;

        @Schema(description = "Whether this is the last page", example = "true")
        private boolean last;

        @Schema(description = "Whether there is a next page", example = "false")
        private boolean hasNext;

        @Schema(description = "Whether there is a previous page", example = "true")
        private boolean hasPrevious;

        @Schema(description = "Sort information")
        private SortInfo sort;

        public PageInfo() {}

        public PageInfo(Page<?> page) {
            this.page = page.getNumber();
            this.size = page.getSize();
            this.totalElements = page.getTotalElements();
            this.totalPages = page.getTotalPages();
            this.first = page.isFirst();
            this.last = page.isLast();
            this.hasNext = page.hasNext();
            this.hasPrevious = page.hasPrevious();
            this.sort = new SortInfo(page.getSort());
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public long getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(long totalElements) {
            this.totalElements = totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public boolean isFirst() {
            return first;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }

        public boolean isLast() {
            return last;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

        public boolean isHasNext() {
            return hasNext;
        }

        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }

        public boolean isHasPrevious() {
            return hasPrevious;
        }

        public void setHasPrevious(boolean hasPrevious) {
            this.hasPrevious = hasPrevious;
        }

        public SortInfo getSort() {
            return sort;
        }

        public void setSort(SortInfo sort) {
            this.sort = sort;
        }

        @Schema(description = "Sorting information")
        public static class SortInfo {
            @Schema(description = "Whether the results are sorted", example = "true")
            private boolean sorted;

            @Schema(description = "Sort field and direction", example = "name: ASC")
            private String by;

            public SortInfo() {}

            public SortInfo(org.springframework.data.domain.Sort sort) {
                this.sorted = sort.isSorted();
                if (sort.isSorted()) {
                    this.by = sort.toString();
                }
            }

            public boolean isSorted() {
                return sorted;
            }

            public void setSorted(boolean sorted) {
                this.sorted = sorted;
            }

            public String getBy() {
                return by;
            }

            public void setBy(String by) {
                this.by = by;
            }
        }
    }
}
