import cn.paxos.codegraph.generated.JavaLexer;
import cn.paxos.codegraph.generated.JavaParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

class AntlrTest {

    @Test
    void parse() throws IOException {
        InputStream inputStream = new FileInputStream("./src/test/java/cn/paxos/codegraph/SourceParserTest.java");
        Lexer lexer = new JavaLexer(CharStreams.fromStream(inputStream));
        TokenStream tokenStream = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokenStream);
        print(parser.compilationUnit(), "");
    }

    private static void print(ParseTree parseTree, String prefix) {
        System.out.println(prefix + parseTree.getClass().getName().replace('$', '.') + ": " + parseTree.getText());
        for (int i = 0, c = parseTree.getChildCount(); i < c; i++) {
            ParseTree child = parseTree.getChild(i);

            print(child, prefix + "    ");
        }
    }
}