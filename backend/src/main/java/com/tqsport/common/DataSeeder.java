package com.tqsport.common;

import com.tqsport.auth.User;
import com.tqsport.auth.UserRole;
import com.tqsport.content.Banner;
import com.tqsport.product.Category;
import com.tqsport.product.Product;
import com.tqsport.product.ProductImage;
import com.tqsport.product.ProductVariant;
import com.tqsport.product.Team;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.Locale;

@ApplicationScoped
public class DataSeeder {
    @Transactional
    void seed(@Observes StartupEvent event) {
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
        if (User.count("email", email) > 0) return;
        User user = new User();
        user.fullName = fullName;
        user.email = email;
        user.passwordHash = "{bcrypt-placeholder}" + password;
        user.role = role;
        user.active = true;
        user.persist();
    }

    private Team ensureTeam(String name, String type, String country) {
        Team team = Team.find("slug", slugify(name)).firstResult();
        if (team != null) return team;
        team = new Team();
        team.name = name;
        team.slug = slugify(name);
        team.type = type;
        team.country = country;
        team.logoUrl = "";
        team.persist();
        return team;
    }

    private Category ensureCategory(String name) {
        Category category = Category.find("slug", slugify(name)).firstResult();
        if (category != null) return category;
        category = new Category();
        category.name = name;
        category.slug = slugify(name);
        category.persist();
        return category;
    }

    private void ensureProduct(String name, Team team, Category category, BigDecimal price, String imageUrl, int stock) {
        if (Product.count("slug", slugify(name)) > 0) return;
        Product product = new Product();
        product.name = name;
        product.slug = slugify(name);
        product.description = "Sản phẩm mặc định của TQSport.";
        product.price = price;
        product.team = team;
        product.category = category;
        product.status = "ACTIVE";
        product.featured = true;
        product.persist();

        ProductVariant variant = new ProductVariant();
        variant.product = product;
        variant.sku = product.slug + "-M";
        variant.size = "M";
        variant.color = "Default";
        variant.stockQuantity = stock;
        variant.persist();

        ProductImage image = new ProductImage();
        image.product = product;
        image.imageUrl = imageUrl;
        image.altText = name;
        image.sortOrder = 1;
        image.persist();
    }

    private void ensureBanner() {
        if (Banner.count() > 0) return;
        Banner banner = new Banner();
        banner.title = "TQSport";
        banner.subtitle = "Áo đấu, bộ tập và thời trang bóng đá cho câu lạc bộ và đội tuyển quốc gia.";
        banner.imageUrl = "https://images.unsplash.com/photo-1431324155629-1a6deb1dec8d?auto=format&fit=crop&w=1800&q=82";
        banner.linkUrl = "/products";
        banner.position = "HOME_HERO";
        banner.active = true;
        banner.sortOrder = 1;
        banner.persist();
    }

    private String slugify(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}
