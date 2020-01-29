package xianzhan.pascal.frontend.pascal.tokens;

import xianzhan.pascal.frontend.Source;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalToken;
import xianzhan.pascal.frontend.pascal.PascalTokenType;

/**
 * Pascal error token
 *
 * @author xianzhan
 * @since 2019-05-12
 */
public class PascalErrorToken extends PascalToken {
    /**
     * Constructor.
     *
     * @param source    the source from where to fetch subsequent characters.
     * @param errorCode the error code.
     * @param tokenText the text of the erroneous token.
     * @throws Exception if an error occurred.
     */
    public PascalErrorToken(Source source,
                            PascalErrorCode errorCode,
                            String tokenText) throws Exception {
        super(source);

        this.text = tokenText;
        this.type = PascalTokenType.ERROR;
        this.value = errorCode;
    }

    /**
     * Do nothing.  Do not consume any source characters.
     *
     * @throws Exception if an error occurred.
     */
    @Override
    protected void extract() throws Exception {
    }
}
