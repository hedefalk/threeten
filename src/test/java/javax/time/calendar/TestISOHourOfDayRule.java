/*
 * Copyright (c) 2011, Stephen Colebourne & Michael Nascimento Santos
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
package javax.time.calendar;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * Test ISO HourOfDay rule.
 *
 * @author Stephen Colebourne
 */
@Test
public class TestISOHourOfDayRule extends AbstractTestDateTimeRule {

    public TestISOHourOfDayRule() {
        super(LocalDateTime.of(2009, 12, 26, 13, 30, 40, 50), 13);
    }

    @Override
    protected DateTimeRule rule() {
        return ISODateTimeRule.HOUR_OF_DAY;
    }

    //-----------------------------------------------------------------------
    // Basics
    //-----------------------------------------------------------------------
    public void test_basics() throws Exception {
        DateTimeRule rule = ISODateTimeRule.HOUR_OF_DAY;
        assertEquals(rule.getType(), DateTimeField.class);
        assertEquals(rule.getName(), "HourOfDay");
        assertEquals(rule.getValueRange(), DateTimeRuleRange.of(0, 23));
        assertEquals(rule.getPeriodUnit(), ISOPeriodUnit.HOURS);
        assertEquals(rule.getPeriodRange(), ISOPeriodUnit.DAYS);
    }

    public void test_values() throws Exception {
        LocalDateTime dt = LocalDateTime.of(2009, 12, 26, 13, 30, 40, 50);
        for (int i = 0; i < 24; i++) {
            dt = dt.withHourOfDay(i);
            assertEquals(dt.get(rule()), rule().field(i));
        }
    }

    //-----------------------------------------------------------------------
    // getValue(Calendrical)
    //-----------------------------------------------------------------------
    public void test_getValue_Calendrical_time() {
        Calendrical cal = LocalTime.of(13, 30, 40, 50);
        assertEquals(rule().getValue(cal), rule().field(13));
    }

    public void test_getValue_Calendrical_dateTime() {
        Calendrical cal = LocalDateTime.of(2009, 12, 26, 13, 30, 40, 50);
        assertEquals(rule().getValue(cal), rule().field(13));
    }

    public void test_getValue_Calendrical_dateTimeFields() {
        Calendrical cal = DateTimeFields.of(rule(), 11);
        assertEquals(rule().getValue(cal), rule().field(11));
    }

}
