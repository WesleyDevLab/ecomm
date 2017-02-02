(function ($, window) {

    var frm = window.frm;

    var FilteringComponent = frm.inheritance.inherits(frm.components.Component, {

        /**
         * Executed on component initialization
         */
        init: function () {
            this.content.find('.jsOnlyNumber').on('keypress', this.keypress.bind(this));
            this.content.find('.accordion')
                .accordion();
            this.content.find('.filter.sidebar').first()
                .sidebar('setting', 'dimPage', false)
                .sidebar('attach events', '.jsShowFiltering');
        },
        keypress: function (event) {
            if (event.keyCode < 48 || event.keyCode > 57) {
                return false;
            }
        }

    });
    frm.components.register('FilteringComponent', FilteringComponent);

})(jQuery, window);
