package hcil.umd.edu.cmsc434calculator;

/**
 * Created by jonf on 3/15/2016.
 */
public final class CalcUtils {
    private CalcUtils(){

    }

    public static boolean isOperator(char c){
        return c == '/' ||
                c == '÷' ||
                c == '*' ||
                c == '-' ||
                c == '+';
    }

    // This code parses a arithmetic string (with *, +, -, and / operators supported) and provides
    // a proper mathematical result. If the string is not parsable, throws RuntimeException
    // Code from: http://stackoverflow.com/questions/3422673/evaluating-a-math-expression-given-in-string-form
    public static double evaluate(final String str){
        class Parser {
            int pos = -1, c;

            void eatChar() {
                c = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            void eatSpace() {
                while (Character.isWhitespace(c)) eatChar();
            }

            double parse() {
                eatChar();
                double v = parseExpression();
                if (c != -1) throw new RuntimeException("Unexpected: " + (char)c);
                return v;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor | term brackets
            // factor = brackets | number | factor `^` factor
            // brackets = `(` expression `)`

            double parseExpression() {
                double v = parseTerm();
                for (;;) {
                    eatSpace();
                    if (c == '+') { // addition
                        eatChar();
                        v += parseTerm();
                    } else if (c == '-') { // subtraction
                        eatChar();
                        v -= parseTerm();
                    } else {
                        return v;
                    }
                }
            }

            double parseTerm() {
                double v = parseFactor();
                for (;;) {
                    eatSpace();
                    if (c == '/' || c == '÷') { // division
                        eatChar();
                        v /= parseFactor();
                    } else if (c == '*' || c == '(') { // multiplication
                        if (c == '*') eatChar();
                        v *= parseFactor();
                    } else {
                        return v;
                    }
                }
            }

            double parseFactor() {
                double v;
                boolean negate = false;
                eatSpace();
                if (c == '+' || c == '-') { // unary plus & minus
                    negate = c == '-';
                    eatChar();
                    eatSpace();
                }
                if (c == '(') { // brackets
                    eatChar();
                    v = parseExpression();
                    if (c == ')') eatChar();
                } else { // numbers
                    int startIndex = this.pos;
                    while ((c >= '0' && c <= '9') || c == '.') eatChar();
                    if (pos == startIndex) throw new RuntimeException("Unexpected: " + (char)c);
                    v = Double.parseDouble(str.substring(startIndex, pos));
                }

                eatSpace();
                if (c == '^') { // exponentiation
                    eatChar();
                    v = Math.pow(v, parseFactor());
                }
                if (negate) v = -v; // unary minus is applied after exponentiation; e.g. -3^2=-9
                return v;
            }
        }
        return new Parser().parse();
    }
}
