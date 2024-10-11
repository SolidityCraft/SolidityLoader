package io.soliditycraft.solidityloader.utils;

import java.util.List;

public class PaginationHelper<T> {

    private final List<T> items;
    private final int itemsPerPage;

    public PaginationHelper(List<T> items, int itemsPerPage) {
        this.items = items;
        this.itemsPerPage = itemsPerPage;
    }

    // Get the total number of pages
    public int getTotalPages() {
        return (int) Math.ceil((double) items.size() / itemsPerPage);
    }

    // Get the items for a specific page
    public List<T> getPage(int page) {
        if (page < 1 || page > getTotalPages()) {
            throw new IllegalArgumentException("Invalid page number: " + page);
        }

        int startIndex = (page - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, items.size());

        return items.subList(startIndex, endIndex);
    }
}
