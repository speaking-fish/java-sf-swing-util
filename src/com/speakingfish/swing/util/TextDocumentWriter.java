package com.speakingfish.swing.util;

import java.io.IOException;
import java.io.Writer;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class TextDocumentWriter extends Writer {
    
    protected final Document _document;
    
    protected int _lastLineStart = 0;
    
    public TextDocumentWriter(Document document) {
        super();
        _document = document;
    }

    @Override public void write(char[] cbuf, int off, int len) throws IOException {
        String src = new String(cbuf, off, len);
        while(0 < src.length()) {
            final int cr = src.lastIndexOf('\r');
            if(0 <= cr) {
                // CR found
                final String sub = src.substring(0, cr);
                final int lf = sub.lastIndexOf('\n');
                if(0 <= lf) {
                    // last LF found before CR
                    try {
                        // write all before LF include LF
                        _document.insertString(_document.getLength(), sub.substring(0, lf + 1), null);
                    } catch(BadLocationException e) {
                        throw new IOException(e);
                    }
                    _lastLineStart = _document.getLength();
                    // continue write all after CR without CR
                    src = src.substring(cr + 1);
                } else {
                    // last LF not found before CR
                    if(_lastLineStart < _document.getLength()) {
                        try {
                            _document.remove(_lastLineStart, _document.getLength() - _lastLineStart);
                        } catch(BadLocationException e) {
                            throw new IOException(e);
                        }
                    }
                    // continue write all after CR without CR
                    src = src.substring(cr + 1);
                }
            } else {
                // CR not found
                try {
                    _document.insertString(_document.getLength(), src, null);
                } catch(BadLocationException e) {
                    throw new IOException(e);
                }
                
                final int lf = src.lastIndexOf('\n'); 
                if(0 <= lf) {
                    // last LF found
                    _lastLineStart = _document.getLength() - src.length() + lf + 1;
                }
                break;
                
            }
        }
    }

    @Override public void flush() throws IOException {
    }

    @Override public void close() throws IOException {
    }

}
