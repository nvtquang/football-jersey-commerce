package com.tqsport.common;

import com.tqsport.auth.User;
import com.tqsport.auth.UserRole;
import com.tqsport.auth.repository.UserRepository;
import com.tqsport.content.Banner;
import com.tqsport.content.repository.BannerRepository;
import com.tqsport.product.Category;
import com.tqsport.product.Product;
import com.tqsport.product.ProductImage;
import com.tqsport.product.ProductVariant;
import com.tqsport.product.Team;
import com.tqsport.product.repository.CategoryRepository;
import com.tqsport.product.repository.ProductRepository;
import com.tqsport.product.repository.TeamRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.Locale;

@Component
public class DataSeeder implements CommandLineRunner {
    private final UserRepository users;
    private final TeamRepository teams;
    private final CategoryRepository categories;
    private final ProductRepository products;
    private final BannerRepository banners;

    public DataSeeder(UserRepository users, TeamRepository teams, CategoryRepository categories,
                      ProductRepository products, BannerRepository banners) {
        this.users = users;
        this.teams = teams;
        this.categories = categories;
        this.products = products;
        this.banners = banners;
    }

    @Override
    @Transactional
    public void run(String... args) {
        ensureUser("TQSport Admin", "admin@tqsport.vn", "12345678", UserRole.ADMIN);
        ensureUser("TQSport User", "user@tqsport.vn", "12345678", UserRole.USER);
        ensureUser("TQSport Super Admin", "superadmin@tqsport.vn", "12345678", UserRole.ADMIN);

        Team vietnam = ensureTeam("Vietnam", "NATIONAL", "Việt Nam");
        Team realMadrid = ensureTeam("Real Madrid", "CLUB", "Spain");
        Team argentina = ensureTeam("Argentina", "NATIONAL", "Argentina");
        Category home = ensureCategory("Áo sân nhà");
        Category training = ensureCategory("Bộ tập");
        Category away = ensureCategory("Áo sân khách");

        ensureProduct("Áo tuyển Việt Nam sân nhà 2026", vietnam, home, new BigDecimal("890000"), "https://images.unsplash.com/photo-1517466787929-bc90951d0974?auto=format&fit=crop&w=900&q=80", 42);
        ensureProduct("Bộ tập Real Madrid Pro Black", realMadrid, training, new BigDecimal("1190000"), "https://images.unsplash.com/photo-1522778119026-d647f0596c20?auto=format&fit=crop&w=900&q=80", 18);
        ensureProduct("Áo Argentina sân khách Heritage", argentina, away, new BigDecimal("990000"), "https://images.unsplash.com/photo-1574629810360-7efbbe195018?auto=format&fit=crop&w=900&q=80", 26);
        ensureBanner();
    }

    private void ensureUser(String fullName, String email, String password, UserRole role) {
        User user = users.findByEmail(email).orElseGet(User::new);
        user.fullName = fullName;
        user.email = email;
        user.passwordHash = "{bcrypt-placeholder}" + password;
        user.role = role;
        user.active = true;
        users.save(user);
    }

    private Team ensureTeam(String name, String type, String country) {
        return teams.findBySlug(slugify(name)).orElseGet(() -> {
            Team team = new Team();
            team.name = name;
            team.slug = slugify(name);
            team.type = type;
            team.country = country;
            team.logoUrl = "";
            return teams.save(team);
        });
    }

    private Category ensureCategory(String name) {
        return categories.findBySlug(slugify(name)).orElseGet(() -> {
            Category category = new Category();
            category.name = name;
            category.slug = slugify(name);
            return categories.save(category);
        });
    }

    private void ensureProduct(String name, Team team, Category category, BigDecimal price, String imageUrl, int stock) {
        if (products.existsBySlug(slugify(name))) return;
        Product product = new Product();
        product.name = name;
        product.slug = slugify(name);
        product.description = "Sản phẩm mặc định của TQSport.";
        product.price = price;
        product.team = team;
        product.category = category;
        product.status = "ACTIVE";
        product.featured = true;

        ProductVariant variant = new ProductVariant();
        variant.product = product;
        variant.sku = product.slug + "-M";
        variant.size = "M";
        variant.color = "Default";
        variant.stockQuantity = stock;
        product.variants.add(variant);

        ProductImage image = new ProductImage();
        image.product = product;
        image.imageUrl = imageUrl;
        image.altText = name;
        image.sortOrder = 1;
        product.images.add(image);

        products.save(product);
    }

    private void ensureBanner() {
        if (banners.count() > 0) return;
        Banner banner = new Banner();
        banner.title = "TQSport";
        banner.subtitle = "Áo đấu, bộ tập và thời trang bóng đá cho câu lạc bộ và đội tuyển quốc gia.";
        banner.imageUrl = "https://images.unsplash.com/photo-1431324155629-1a6deb1dec8d?auto=format&fit=crop&w=1800&q=82";
        banner.linkUrl = "/products";
        banner.position = "HOME_HERO";
        banner.active = true;
        banner.sortOrder = 1;
        banners.save(banner);
    }

    private String slugify(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}
