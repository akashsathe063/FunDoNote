package com.example.fundonote.view

import android.app.*
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fundonote.databinding.FragmentSetRemainderBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*


class SetRemainderFragment : Fragment() {
    private var _binding: FragmentSetRemainderBinding? = null
    private val binding get() = _binding!!
    private lateinit var picker : MaterialTimePicker
    private lateinit var calender : Calendar
    private lateinit var alarmManager : AlarmManager
    private lateinit var pendingIntet : PendingIntent


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSetRemainderBinding.inflate(inflater, container, false)
        return binding.root
        createNotificationChannel()
        binding.btnselectTime.setOnClickListener {
            showTimePicker()
        }
        binding.btnsetAlarm.setOnClickListener {
            setAlarm()
        }
        binding.btncanceledAlarm.setOnClickListener {

            cancelAlarm()
        }
    }

    private fun cancelAlarm() {


        alarmManager =  activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReciver::class.java)

        pendingIntet = PendingIntent.getBroadcast(context,0,intent,0)

        alarmManager.cancel(pendingIntet)
        Toast.makeText(context,"Alarm canceled",Toast.LENGTH_LONG).show()

    }

    private fun setAlarm() {

       alarmManager =  activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReciver::class.java)

        pendingIntet = PendingIntent.getBroadcast(context,0,intent,0)

        alarmManager.setRepeating(

            AlarmManager.RTC_WAKEUP,calender.timeInMillis,
            AlarmManager.INTERVAL_DAY,pendingIntet
        )
          Toast.makeText(context,"Alarm set success",Toast.LENGTH_LONG).show()
    }

    private fun showTimePicker() {
       picker = MaterialTimePicker.Builder()
           .setTimeFormat(TimeFormat.CLOCK_12H)
           .setHour(12)
           .setMinute(0)
           .setTitleText("Select Alarm Time")
           .build()
        picker.show( (context as AppCompatActivity).supportFragmentManager,"note")

        picker.addOnPositiveButtonClickListener {

            if(picker.hour > 12){
                binding.selectedTime.text =
                    String.format("%02d",picker.hour - 12) + " : " + String.format("%02d",picker.minute) + "PM"

            }
            else{
                String.format("%02d",picker.hour ) + " : " + String.format("%02d",picker.minute) + "AM"
            }
            calender = Calendar.getInstance()
            calender[Calendar.HOUR_OF_DAY] = picker.hour
            calender[Calendar.MINUTE] = picker.minute
            calender[Calendar.SECOND] = 0
            calender[Calendar.MILLISECOND] = 0
        }
    }

    private fun createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name : CharSequence = "noteRemainderChannel"
            val description = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("note",name, importance)
            channel.description = description
            val notificationManager = activity?.getSystemService(NotificationManager::class.java)

            notificationManager?.createNotificationChannel(channel)
        }

    }

}