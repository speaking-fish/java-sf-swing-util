package com.speakingfish.swing.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.text.DefaultFormatter;

public class RegexFormatter extends DefaultFormatter {

    protected final List<Pattern> _patterns;

    public RegexFormatter(List<Pattern> patterns) {
        super();
        _patterns = new ArrayList<Pattern>(patterns);
    }

    public Object stringToValue(String text) throws ParseException {
        if(0 == _patterns.size()) {
            return text;
        }
        
        for(final Pattern pattern : _patterns) {
            final Matcher matcher = pattern.matcher(text);

            if(matcher.matches()) {
                //setMatcher(matcher);
                return super.stringToValue(text);
            }
        }
        
        throw new ParseException("Pattern did not match", 0);
    }
    
}
