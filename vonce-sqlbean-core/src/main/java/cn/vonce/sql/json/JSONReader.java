package cn.vonce.sql.json;

/**
 * JSON读取器
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/11/22 19:05
 */
public class JSONReader {

    private final String json;
    private int position;

    public JSONReader(String json) {
        this.json = json;
        this.position = 0;
    }

    public char peek() throws Exception {
        if (position >= json.length()) {
            throw new IllegalStateException("Unexpected end of JSON");
        }
        return json.charAt(position);
    }

    public char next() throws Exception {
        char c = peek();
        position++;
        return c;
    }

    public void expect(char expected) throws Exception {
        char actual = next();
        if (actual != expected) {
            throw new IllegalArgumentException("Expected '" + expected + "' but got '" + actual + "'");
        }
    }

    public void expect(String expected) throws Exception {
        for (char c : expected.toCharArray()) {
            expect(c);
        }
    }

    public void skipWhitespace() {
        while (position < json.length() && Character.isWhitespace(json.charAt(position))) {
            position++;
        }
    }

    public String read(int length) throws Exception {
        if (position + length > json.length()) {
            throw new IllegalStateException("Unexpected end of JSON");
        }
        String result = json.substring(position, position + length);
        position += length;
        return result;
    }

}
