package com.seuit.eworkout.report.util

import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.Date

class CalendarUtil {
    companion object
    {
        fun getStartOfToday(): Date {
            val calendarUtil = Calendar.getInstance()
            calendarUtil.time = Timestamp.now().toDate()

            calendarUtil.set(Calendar.HOUR_OF_DAY, 0)
            calendarUtil.set(Calendar.MINUTE, 0)
            calendarUtil.set(Calendar.SECOND, 0)
            return calendarUtil.time
        }

        fun getEndOfToday(): Date {
            val calendarUtil = Calendar.getInstance()
            calendarUtil.time = Timestamp.now().toDate()

            calendarUtil.set(Calendar.HOUR_OF_DAY, 23)
            calendarUtil.set(Calendar.MINUTE, 59)
            calendarUtil.set(Calendar.SECOND, 59)
            return calendarUtil.time
        }
    }
}