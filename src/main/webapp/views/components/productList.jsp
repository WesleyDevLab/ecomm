<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="ui container jsProductListComponent" style="margin: 1.5em 0;">
    <c:forEach var="category" items="${requestScope.categories}">
        <c:if test="${category.getParentId() != 0}">
            <h2 class="ui center aligned header horizontal divider"><a href="#">${category.getName()}</a></h2>
            <div class="ui grid centered container">
                <c:forEach var="product" items="${requestScope.products}">
                    <c:if test="${product.getCategoryId() == category.getCategoryId()}">
                        <div class="five wide column">
                            <img class="ui fluid image" src="http://semantic-ui.com/images/wireframe/image.png">
                            <h3 class="ui center aligned header horizontal divider"><a href="#">
                                    ${product.getName()}
                            </a>
                            </h3>
                            <div class="ui center aligned massive rating" data-rating="1"></div>
                            <h3 class="ui center aligned grey header">
                                $${product.getPrice()}
                            </h3>
                            <button class="ui labeled icon  fluid blue button">
                                <i class="add to car icon"></i>
                                Add to card
                            </button>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </c:if>
    </c:forEach>
</div>
<script>
    window.frm.components.init('ProductListComponent', '.jsProductListComponent');
</script>