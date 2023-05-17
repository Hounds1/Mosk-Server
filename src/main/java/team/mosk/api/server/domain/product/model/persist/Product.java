package team.mosk.api.server.domain.product.model.persist;

import lombok.*;
import team.mosk.api.server.domain.category.model.persist.Category;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroup;
import team.mosk.api.server.domain.product.dto.UpdateProductRequest;
import team.mosk.api.server.domain.product.model.vo.Selling;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;

    private String description;

    private Long price;

    @Enumerated(EnumType.STRING)
    private Selling selling;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private ProductImg productImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OptionGroup> optionGroups;

    /**
     * methods
     */

    public void update(final UpdateProductRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.price = request.getPrice();
    }

    public void changeSellingStatus(final Selling selling) {
        this.selling = selling;
    }

    public void initCategory(final Category category) {
        this.category = category;
    }

    public void initStore(final Store store) {
        this.store = store;
    }

    public void initProductImg(final ProductImg productImg) {
        this.productImg = productImg;
    }
}
