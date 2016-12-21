/**
 * Created by Alexander on 08.12.2016.
 */
(function ($, window) {

    var frm = window.frm;
    var ProductComponent = frm.inheritance.inherits(frm.components.Component, {

        /**
         * Executed on component initialization
         */
        init: function () {
            this.content.find('.rating').rating({
                initialRating: 3,
                maxRating: 5
            });
        }

    });

    frm.components.register('productDetails', ProductComponent);

})(jQuery, window);
