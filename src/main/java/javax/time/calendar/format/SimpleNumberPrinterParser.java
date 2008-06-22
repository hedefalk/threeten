/*
 * Copyright (c) 2008, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.time.calendar.format;

import java.io.IOException;
import java.util.Locale;

import javax.time.calendar.DateTimeFieldRule;
import javax.time.calendar.FlexiDateTime;
import javax.time.calendar.format.DateTimeFormatterBuilder.SignStyle;

/**
 * Prints and parses a numeric date-time field with optional padding.
 *
 * @author Stephen Colebourne
 */
class SimpleNumberPrinterParser implements DateTimePrinter {
    // TODO: I18N: The numeric output varies by locale

    /**
     * The field to output, not null.
     */
    private final DateTimeFieldRule fieldRule;
    /**
     * The minimum width allowed, zero padding is used up to this width, from 1 to 10.
     */
    private final int minWidth;
    /**
     * The maximum width allowed, from 1 to 10.
     */
    private final int maxWidth;
    /**
     * The positive/negative sign style, not null.
     */
    private final SignStyle signStyle;

    /**
     * Constructor.
     *
     * @param fieldRule  the rule of the field to print, not null
     * @param minWidth  the minimum field width, from 1 to 10
     * @param maxWidth  the maximum field width, from 1 to 10
     * @param signStyle  the positive/negative sign style, not null
     */
    SimpleNumberPrinterParser(DateTimeFieldRule fieldRule, int minWidth, int maxWidth, SignStyle signStyle) {
        this.fieldRule = fieldRule;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.signStyle = signStyle;
    }

    /** {@inheritDoc} */
    public void print(Appendable appendable, FlexiDateTime dateTime, Locale locale) throws IOException {
        int value = dateTime.getRawValue(fieldRule);
        String str = (value == Integer.MIN_VALUE ? Long.toString(Math.abs((long) value)) : Integer.toString(Math.abs(value)));
        if (str.length() > maxWidth) {
            throw new CalendricalFormatFieldException(fieldRule, value, maxWidth);
        }
        signStyle.print(appendable, fieldRule, value, minWidth);
        for (int i = 0; i < minWidth - str.length(); i++) {
            appendable.append('0');
        }
        appendable.append(str);
    }

    /** {@inheritDoc} */
    public int parse(DateTimeParseContext context, String parseText, int position) {
        int length = parseText.length();
        if (position == length) {
            return ~position;
        }
        char sign = parseText.charAt(position);  // IOOBE if invalid position
        if (sign == '+') {
            switch (signStyle) {
                case ALWAYS:
                case EXCEEDS_PAD:
                    position++;
                    break;
                default:
                    return ~position;
            }
        } else if (sign == '-') {
            switch (signStyle) {
                case ALWAYS:
                case EXCEEDS_PAD:
                case NORMAL:
                    position++;
                    break;
                default:
                    return ~position;
            }
        }
        int minEndPos = position + minWidth;
        if (minEndPos > length) {
            return ~position;
        }
        int total = 0;
        while (position < minEndPos) {
            char ch = parseText.charAt(position++);
            int digit = context.digit(ch);
            if (digit < 0) {
                return ~(position - 1);
            }
            total *= 10;
            total += digit;
        }
        int maxEndPos = Math.max(position + maxWidth, length);
        while (position < maxEndPos) {
            char ch = parseText.charAt(position++);
            int digit = context.digit(ch);
            if (digit < 0) {
                position--;
                break;
            }
            total *= 10;
            total += digit;
        }
        total = (sign == '-' ? -total : total);
        context.setFieldValue(fieldRule, total);
        return position;
    }

}