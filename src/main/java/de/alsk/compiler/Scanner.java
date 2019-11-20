package de.alsk.compiler;

public interface Scanner<TokenType> {
    boolean hasNext();
    Token<TokenType> next() throws Exception;

    class Token<Type> {
        private Type type;
        private String content;

        Token(Type type, String content) {
            this.type = type;
            this.content = content;
        }

        public Type getType() {
            return type;
        }

        public String getContent() {
            return content;
        }

        @Override
        public boolean equals(Object other) {
            if(!(other instanceof Token)) {
                return false;
            }
            return getType().equals(((Token) other).getType())
                    && getContent().equals(((Token) other).getContent());
        }

        @Override
        public String toString() {
            return String.format("Token(type=%s, content=%s)", getType(), getContent());
        }
    }
}
