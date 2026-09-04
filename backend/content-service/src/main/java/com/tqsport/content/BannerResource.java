package com.tqsport.content;

import com.tqsport.content.repository.BannerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/banners", produces = MediaType.APPLICATION_JSON_VALUE)
public class BannerResource {
    private final BannerRepository banners;

    public BannerResource(BannerRepository banners) {
        this.banners = banners;
    }

    @GetMapping
    public List<Banner> active() {
        return banners.findAllByOrderBySortOrderAsc();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Banner create(@RequestBody Banner banner) {
        return banners.save(banner);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Banner update(@PathVariable Long id, @RequestBody Banner request) {
        Banner banner = banners.findById(id).orElseThrow();
        banner.title = request.title;
        banner.subtitle = request.subtitle;
        banner.imageUrl = request.imageUrl;
        banner.linkUrl = request.linkUrl;
        banner.position = request.position;
        banner.active = request.active;
        banner.sortOrder = request.sortOrder;
        return banners.save(banner);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        banners.deleteById(id);
    }
}
