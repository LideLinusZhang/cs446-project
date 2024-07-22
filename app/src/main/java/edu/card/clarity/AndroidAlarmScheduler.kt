package edu.card.clarity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.time.ZoneId
import javax.inject.Inject

class AndroidAlarmScheduler @Inject constructor(
    private val context: Context
): AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)
//    private val oneMonthInterval = 30L * 24 * 60 * 60 * 1000
    private val oneMinuteInterval = 30L * 1000

    override fun schedule(item: SchedulerAlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply{
            putExtra("EXTRA_MESSAGE", item.message)
        }
        Log.d("alarm", "alarm set for ${item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000}")
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            oneMinuteInterval,
            PendingIntent.getBroadcast(
                context,
                item.id.hashCode(), // request code
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(item: SchedulerAlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.id.hashCode(), // request code
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

}