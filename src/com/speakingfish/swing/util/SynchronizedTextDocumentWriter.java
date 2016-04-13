package com.speakingfish.swing.util;

import java.io.IOException;

import javax.swing.text.Document;

import static javax.swing.SwingUtilities.*;

public class SynchronizedTextDocumentWriter extends TextDocumentWriter {

    public SynchronizedTextDocumentWriter(Document document) {
        super(document);
    }

    @Override public void write(char[] cbuf, int off, int len) throws IOException {
        final char[] value = new char[len];
        System.arraycopy(
            cbuf, off,
            value, 0,
            len
            );
        invokeLater(new Runnable() {
            public void run() {
                try {
                    SynchronizedTextDocumentWriter.super.write(value, 0, value.length);
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
