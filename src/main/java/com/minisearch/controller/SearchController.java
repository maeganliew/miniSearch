package com.minisearch.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.minisearch.model.Video;
import com.minisearch.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/mini-search/search")
public class SearchController {
    public final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    // Search Video
    @GetMapping
    public List<Video> searchVideo(@RequestParam String query, @RequestParam(required = false) String sortBy) throws JsonProcessingException {
        return searchService.searchVideo(query, sortBy);
    }
}
