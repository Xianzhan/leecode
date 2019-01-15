package xianzhan.misc.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 描述：国际化
 *
 * @author Lee
 * @since 2019-01-14
 */
public class I18N {

    private static Locale defaultLocale = Locale.getDefault();

    private static ResourceBundle bundle;

    public static void setResource(String basename) {
        bundle = ResourceBundle.getBundle(basename, defaultLocale);
    }

    public static void setResource(String basename, Locale locale) {
        bundle = ResourceBundle.getBundle(basename, locale);
    }

    public static void main(String[] args) {
        setResource("i18n.direction");
        String string = bundle.getString(Direction.UP);
        System.out.println(string);
    }
}
