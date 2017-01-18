package ru.ncedu.ecomm.servlets.services;

import ru.ncedu.ecomm.data.models.*;
import ru.ncedu.ecomm.servlets.models.CategoryViewModel;
import ru.ncedu.ecomm.servlets.models.ProductViewModel;
import ru.ncedu.ecomm.servlets.models.builders.CategoryViewBuilder;
import ru.ncedu.ecomm.servlets.models.builders.ProductItemsViewBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static ru.ncedu.ecomm.data.DAOFactory.getDAOFactory;

public class ProductViewService {

    public static final String DEFAULT_IMAGE_URL = "/images/defaultimage/image.png";
    public static final long CHARACTERISTIC_ID_FOR_IMAGE_URL = 28;

    private static final int CATEGORY_ID_FOR_BEST_OFFERS = 0;

    private ProductViewService() {
    }

    private static ProductViewService instance;

    public static synchronized ProductViewService getInstance() {
        if (instance == null) {
            instance = new ProductViewService();
        }
        return instance;
    }

    private long getCategoryId(String categoryIdByRequest) {
        if (categoryIdByRequest != null) {
            return Long.parseLong(categoryIdByRequest);
        } else {
            return 0;
        }
    }

    public List<CategoryViewModel> getBestOffersCategory() {
        List<CategoryViewModel> bestOffersCategory = new ArrayList<>();

        CategoryViewModel bestOffers = new CategoryViewBuilder()
                .setName("Best Offers")
                .setId(CATEGORY_ID_FOR_BEST_OFFERS)
                .setProducts(addProductToViewByCategoryId(CATEGORY_ID_FOR_BEST_OFFERS))
                .build();

        bestOffersCategory.add(bestOffers);
        return bestOffersCategory;
    }

    public List<CategoryViewModel> getCategoriesById(HttpServletRequest request) {

        List<CategoryViewModel> categoriesById = new ArrayList<>();

        List<Category> categories = getCategoryListByRequest(request);

        for (Category category : categories) {

            CategoryViewModel categoryByRequest = new CategoryViewBuilder()
                    .setId(category.getCategoryId())
                    .setName(category.getName())
                    .setProducts(addProductToViewByCategoryId(category.getCategoryId()))
                    .build();

            categoriesById.add(categoryByRequest);
        }
        return categoriesById;
    }


    private List<Category> getCategoryListByRequest(HttpServletRequest request) {
        List<Category> categories;
        String categoryIdByRequest = request.getParameter("category_id");

        if (categoryIdByRequest == null || getCategoryId(categoryIdByRequest) == 0) {
            categories = getParentCategory();
        } else {
            categories = getCategoriesById(
                    getCategoryId(categoryIdByRequest)
            );
        }
        return categories;
    }


    private List<Category> getCategoriesById(long categoryId) {
        return getDAOFactory()
                .getCategoryDAO()
                .getAllNotEmptyChildrenCategoryById(categoryId);
    }

    private List<Category> getParentCategory() {
        return getDAOFactory()
                .getCategoryDAO()
                .getParentCategory();
    }


    private List<ProductViewModel> addProductToViewByCategoryId(long categoryId) {
        return getProductsToView(getProductsById(categoryId));
    }

    private List<Product> getProductsById(long categoryId) {
        if (categoryId == CATEGORY_ID_FOR_BEST_OFFERS) {
            return getDAOFactory().getProductDAO()
                    .getBestOffersProducts();
        } else {
            return getDAOFactory().getProductDAO()
                    .getProductsByCategoryId(categoryId);
        }
    }

    private CharacteristicValue getImageUrl(long productId) {
        return getDAOFactory()
                .getCharacteristicValueDAO()
                .getCharacteristicValueByIdAndProductId(productId,
                        CHARACTERISTIC_ID_FOR_IMAGE_URL);
    }

    private Rating getRating(long productId) {
        return getDAOFactory()
                .getReviewDAO()
                .getAverageRatingByProductId(productId);
    }

    private long getDiscountPrice(long discountId, long price){
        return DiscountService
                .getInstance()
                .getDiscountPrice(discountId, price);
    }

    public List<ProductViewModel> getProductsToView (List<Product> products){

        List<ProductViewModel> productsView = new ArrayList<>();

        ProductViewModel itemForView;
        Rating productAverageRating;
        CharacteristicValue characteristicValue;

        for (Product product : products) {
            int productRating = 0;

            String imageUrl = DEFAULT_IMAGE_URL;

            characteristicValue = getImageUrl(product.getId());

            if (characteristicValue != null) {

                imageUrl = getImageUrlByCharacteristicList(characteristicValue);
            }

            productAverageRating = getRating(product.getId());

            if (productAverageRating != null) {
                productRating = productAverageRating.getRaiting();
            }


            itemForView = new ProductItemsViewBuilder()
                    .setProductId(product.getId())
                    .setName(product.getName())
                    .setPrice(product.getPrice())
                    .setImageUrl(imageUrl)
                    .setRating(productRating)
                    .build();

            if (product.getDiscountId() > 1) {
                itemForView.setDiscount(getDiscountPrice(product.getDiscountId(),
                        product.getPrice()));
            }

            productsView.add(itemForView);
        }
        return productsView;
    }

    private String getImageUrlByCharacteristicList(CharacteristicValue characteristicValue) {
        String imageURL = characteristicValue.getCharacteristicValue();
        String[] links = imageURL.trim().split(",");

        return links[0];
    }
}
