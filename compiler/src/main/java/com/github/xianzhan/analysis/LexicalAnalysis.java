package com.github.xianzhan.analysis;

import com.github.xianzhan.analysis.exception.LexicalAnalysisException;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 描述：词法分析器 Tokenizer, 一个状态机
 *
 * @author Lee
 * @since 2017/10/14
 */
public class LexicalAnalysis {

    public static enum State {
        /**
         * 初始状态, 可以转化成任何一种单词类型的状态
         */
        Normal,

        /**
         * 标示符
         */
        Identifier,
        /**
         * 符号
         */
        Sign,
        /**
         * 注释
         */
        Annotation,
        /**
         * 字符串
         */
        String,
        /**
         * 正则表达式
         */
        RegEx,
        /**
         * 空白
         */
        Space;
    }

    private static final char[] FilterChar = {
            '\b', '\f', '\r'
    };

    private static final char[]                        IdentifierSign     = {'_'};
    private static final char[]                        IdentifierRearSign = {'?', '!'};
    private static final char[]                        Space              = {' ', '\t'};
    private static final HashMap<Character, Character> StringTMMap        = new
            HashMap<>();

    static {
        StringTMMap.put('\"', '\"');
        StringTMMap.put('\'', '\'');
        StringTMMap.put('\\', '\\');
        StringTMMap.put('b', '\b');
        StringTMMap.put('f', '\f');
        StringTMMap.put('t', '\t');
        StringTMMap.put('r', '\r');
        StringTMMap.put('n', '\n');
    }

    private final Reader reader;

    private       Token             endToken    = null;
    private final LinkedList<Token> tokenBuffer = new LinkedList<>();
    private State state;
    private StringBuffer readBuffer             = null;
    private boolean      transferredMeaningSign = false;

    public LexicalAnalysis(Reader reader) {
        this.reader = reader;
        this.state = State.Normal;
    }

    /**
     * 当源代码读完了, 如果状态机处于 Normal 状态, 此时应该生成一个 EndSymbol,
     * 但如果此时不处于 Normal 状态, 那就有问题了, 必须抛出一个异常.
     * @return
     * @throws IOException
     * @throws LexicalAnalysisException
     */
    public Token read() throws IOException, LexicalAnalysisException {

        if (endToken != null) {
            return endToken;
        }
        while (tokenBuffer.isEmpty()) {
            int read = reader.read();
            char c = (read == -1 ? '\0' : (char) read);

            while (!readChar(c)) {}
        }

        Token token = tokenBuffer.removeLast();
        if (token.type == Token.Type.EndSymbol) {
            endToken = token;
        }

        return token;
    }

    private void refreshBuffer(char c) {
        readBuffer = new StringBuffer();
        readBuffer.append(c);
    }

    private void createToken(Token.Type type) {
        Token token = new Token(type, readBuffer.toString());
        tokenBuffer.addFirst(token);
        readBuffer = null;
    }

    private void createToken(Token.Type type, String value) {
        Token token = new Token(type, value);
        tokenBuffer.addFirst(token);
        readBuffer = null;
    }

    private boolean readChar(char c) throws LexicalAnalysisException {

        boolean moveCursor = true;
        Token.Type createType = null;

        if (!include(FilterChar, c)) {
            if (state == State.Normal) {
                if (inIdentifierSetButNotRear(c)) {
                    state = State.Identifier;
                } else if (SignParser.inCharSet(c)) {
                    state = State.Sign;
                } else if (c == '#') {
                    state = State.Annotation;
                } else if (c == '\"' || c == '\'') {
                    state = State.String;
                    transferredMeaningSign = false;
                } else if (c == '`') {
                    state = State.RegEx;
                    transferredMeaningSign = false;
                } else if (include(Space, c)) {
                    state = State.Space;
                } else if (c == '\n') {
                    createType = Token.Type.NewLine;
                } else if (c == '\0') {
                    createType = Token.Type.EndSymbol;
                } else {
                    throw new LexicalAnalysisException(c);
                }
                refreshBuffer(c);
            }
        } else if (state == State.Identifier) {
            if (inIdentifierSetButNotRear(c)) {
                readBuffer.append(c);

            } else if (include(IdentifierRearSign, c)) {
                createType = Token.Type.Identifier;
                readBuffer.append(c);
                state = State.Normal;

            } else {
                createType = Token.Type.Identifier;
                state = State.Normal;
                moveCursor = false;
            }
        } else if (state == State.Sign) {
            if (SignParser.inCharSet(c)) {
                readBuffer.append(c);

            } else {
                List<String> list = SignParser.parse(readBuffer.toString());
                for (String signStr : list) {
                    createToken(Token.Type.Sign, signStr);
                }
                createType = null;
                state = State.Normal;
                moveCursor = false;
            }
        } else if (state == State.Annotation) {
            if (c != '\n' & c != '\0') {
                readBuffer.append(c);

            } else {
                createType = Token.Type.Annotation;
                state = State.Normal;
                moveCursor = false;

            }
        } else if (state == State.String) {

            if (c == '\n') {
                throw new LexicalAnalysisException(c);

            } else if (c == '\0') {
                throw new LexicalAnalysisException(c);

            } else if (transferredMeaningSign) {

                Character tms = StringTMMap.get(c);
                if (tms == null) {
                    throw new LexicalAnalysisException(c);
                }
                readBuffer.append(tms);
                transferredMeaningSign = false;

            } else if (c == '\\') {
                transferredMeaningSign = true;

            } else {
                readBuffer.append(c);
                char firstChar = readBuffer.charAt(0);
                if (firstChar == c) {
                    createType = Token.Type.String;
                    state = State.Normal;
                }
            }
        } else if (state == State.RegEx) {
            if (transferredMeaningSign) {

                if (c != '`') {
                    throw new LexicalAnalysisException(c);
                }
                readBuffer.append(c);
                transferredMeaningSign = false;

            } else if (c == '\\') {
                transferredMeaningSign = true;

            } else if (c == '\0') {
                throw new LexicalAnalysisException(c);

            } else if (c == '`') {
                readBuffer.append(c);
                createType = Token.Type.RegEx;
                state = State.Normal;

            } else {
                readBuffer.append(c);
            }
        } else if (state == State.Space) {
            if (include(Space, c)) {
                readBuffer.append(c);

            } else {
                createType = Token.Type.Space;
                state = State.Normal;
                moveCursor = false;
            }
        }
        if (createType != null) {
            createToken(createType);
        }
        return moveCursor;
    }

    private boolean inIdentifierSetButNotRear(char c) {
        return (c >= 'a' & c <= 'z') | (c >= 'A' & c <= 'Z') | (c >= '0' & c <= '9') || include(IdentifierSign, c);
    }

    private boolean include(char[] range, char c) {
        boolean include = false;
        for (int i = 0; i < range.length; ++i) {
            if (range[i] == c) {
                include = true;
                break;
            }
        }
        return include;
    }
}
