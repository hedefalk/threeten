/*
 * Copyright (c) 2008-2011, Stephen Colebourne & Michael Nascimento Santos
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

import static javax.time.calendar.ISODateTimeRule.DAY_OF_MONTH;
import static javax.time.calendar.ISODateTimeRule.MONTH_OF_YEAR;
import static javax.time.calendar.ISODateTimeRule.YEAR;
import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Locale;

import javax.time.calendar.Calendrical;
import javax.time.calendar.DateTimeRule;
import javax.time.calendar.LocalDate;
import javax.time.calendar.ZoneId;
import javax.time.calendar.ZoneOffset;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test DateTimeParseContext.
 *
 * @author Stephen Colebourne
 */
@Test
public class TestDateTimeParseContext {

    private DateTimeFormatSymbols symbols;
    private DateTimeParseContext context;

    @BeforeMethod
    public void setUp() {
        symbols = DateTimeFormatSymbols.of(Locale.GERMANY);
        context = new DateTimeParseContext(symbols);
    }

    //-----------------------------------------------------------------------
    public void test_constructor() throws Exception {
        assertEquals(context.getSymbols(), symbols);
        assertEquals(context.getLocale(), Locale.GERMANY);
        assertEquals(context.getParsed().size(), 0);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void test_constructor_null() throws Exception {
        new DateTimeParseContext(null);
    }

    //-----------------------------------------------------------------------
    public void test_caseSensitive() throws Exception {
        assertEquals(context.isCaseSensitive(), true);
        context.setCaseSensitive(false);
        assertEquals(context.isCaseSensitive(), false);
    }

    //-----------------------------------------------------------------------
    public void test_strict() throws Exception {
        assertEquals(context.isStrict(), true);
        context.setStrict(false);
        assertEquals(context.isStrict(), false);
    }

    //-----------------------------------------------------------------------
    public void test_getParsed_DateTimeRule_null() throws Exception {
        assertEquals(context.getParsed((DateTimeRule) null), null);
    }

    public void test_getParsed_DateTimeRule_notPresent() throws Exception {
        assertEquals(context.getParsed(DAY_OF_MONTH), null);
    }

    //-------------------------------------------------------------------------
    public void test_getParsed_Class_null() throws Exception {
        assertEquals(context.getParsed((Class<?>) null), null);
    }

    public void test_getParsed_Class_notPresent() throws Exception {
        assertEquals(context.getParsed(LocalDate.class), null);
    }

    //-----------------------------------------------------------------------
    public void test_getParsed_modifyList() throws Exception {
        assertEquals(context.getParsed().size(), 0);
        
        context.setParsedField(DAY_OF_MONTH, 2);
        assertEquals(context.getParsed().size(), 1);
        assertEquals(context.getParsed().get(0), DAY_OF_MONTH.field(2));
        
        context.getParsed().add(LocalDate.of(2010, 6, 30));
        assertEquals(context.getParsed().size(), 2);
        assertEquals(context.getParsed().get(0), DAY_OF_MONTH.field(2));
        assertEquals(context.getParsed().get(1), LocalDate.of(2010, 6, 30));
    }

    //-------------------------------------------------------------------------
    public void test_setParsed() throws Exception {
        context.setParsed(LocalDate.of(2010, 6, 30));
        assertEquals(context.getParsed().size(), 1);
        assertEquals(context.getParsed().get(0), LocalDate.of(2010, 6, 30));
        
        context.setParsed(LocalDate.of(2010, 9, 23));
        assertEquals(context.getParsed().size(), 2);
        assertEquals(context.getParsed().get(0), LocalDate.of(2010, 6, 30));
        assertEquals(context.getParsed().get(1), LocalDate.of(2010, 9, 23));
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void test_setParsed_null() throws Exception {
        context.setParsed(null);
    }

    //-------------------------------------------------------------------------
    public void test_setParsedField() throws Exception {
        context.setParsedField(YEAR, 2008);
        assertEquals(context.getParsed().size(), 1);
        assertEquals(context.getParsed().get(0), YEAR.field(2008L));
        
        context.setParsedField(MONTH_OF_YEAR, 6);
        assertEquals(context.getParsed().size(), 2);
        assertEquals(context.getParsed().get(0), YEAR.field(2008L));
        assertEquals(context.getParsed().get(1), MONTH_OF_YEAR.field(6));
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void test_setParsedField_null() throws Exception {
        context.setParsedField(null, 2008);
    }

    //-------------------------------------------------------------------------
    public void test_toCalendricalMerger() throws Exception {
        context.setParsedField(YEAR, 2008);
        context.setParsedField(MONTH_OF_YEAR, 6);
        
        assertEquals(context.getParsed().size(), 2);
        assertEquals(context.getParsed(YEAR), YEAR.field(2008L));
        assertEquals(context.getParsed(MONTH_OF_YEAR), MONTH_OF_YEAR.field(6L));
        List<Calendrical> list = context.toCalendricalMerger().getInput();
        assertEquals(list.size(), 2);
        assertEquals(list.get(0), YEAR.field(2008L));
        assertEquals(list.get(1), MONTH_OF_YEAR.field(6L));
    }

    //-----------------------------------------------------------------------
    public void test_toString() throws Exception {
        context.setParsedField(YEAR, 2008);
        context.setParsedField(MONTH_OF_YEAR, 6);
        context.setParsed(ZoneOffset.ofHours(16));
        context.setParsed(ZoneId.of(ZoneOffset.ofHours(18)));
        
        String str = context.toString();
        assertEquals(str.contains("MonthOfYear 6"), true);
        assertEquals(str.contains("Year 2008"), true);
        assertEquals(str.contains("UTC+18:00"), true);
        assertEquals(str.contains("+16:00"), true);
    }

}
