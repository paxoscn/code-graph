/*
 * Copyright 2022 paxos.cn.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.paxos.codegraph;

import cn.paxos.codegraph.generated.JavaLexer;
import cn.paxos.codegraph.generated.JavaParser;
import cn.paxos.codegraph.node.Source;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SourceParser {

    public Source parse(String sourcePath) throws IOException {
        try (InputStream inputStream = new FileInputStream(sourcePath)) {
            Lexer lexer = new JavaLexer(CharStreams.fromStream(inputStream));
            TokenStream tokenStream = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokenStream);
            Source source = new Source();
            source.build(parser.compilationUnit());
            return source;
        }
    }
}
