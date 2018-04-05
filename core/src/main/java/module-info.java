/**
 * 描述：
 *
 * @since 2017-09-22
 */
module core {
    requires java.instrument;

    // xianzhan.gui.fx
    requires javafx.graphics;
    requires javafx.controls;

    // xianzhan.gui.swing
    requires java.desktop;

    // 向 jdk 暴露
    exports xianzhan.gui.fx.form;
    exports xianzhan.gui.fx.hello;
}