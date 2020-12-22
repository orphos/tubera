// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.oracle.truffle.api.TruffleFile;
import com.oracle.truffle.api.TruffleFile.FileTypeDetector;

public class OrphosDetector implements FileTypeDetector {

    @Override
    public String findMimeType(TruffleFile file) throws IOException {
        String name = file.getName();
        if (name != null && name.endsWith(".orp"))
            return OrphosLanguage.MIME_TYPE;
        String line = file.newBufferedReader().readLine();
        if (line != null && line.startsWith("#!") && (line.endsWith("/orphos") || line.endsWith(" orphos")))
            return OrphosLanguage.MIME_TYPE;
        return null;
    }

    @Override
    public Charset findEncoding(TruffleFile file) {
        return StandardCharsets.UTF_8;
    }

}