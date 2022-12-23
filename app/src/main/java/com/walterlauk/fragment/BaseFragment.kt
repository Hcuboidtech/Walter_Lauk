package com.walterlauk.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.walterlauk.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


open class BaseFragment : Fragment() {

    fun openDatePicker(context: Context, textview: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            context, R.style.DialogTheme,
            { view, year, monthOfYear, dayOfMonth ->

                calendar.set(year, monthOfYear, dayOfMonth)
                textview.text =
                    changeDateFormte("dd MMMM,yyyy", calendar).uppercase(Locale.getDefault())
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    fun changeDateFormte(format: String, date: Calendar): String {
        val dateFormat = SimpleDateFormat(format)
        val dateString: String = dateFormat.format(date.time)
        return dateString
    }

    fun parseDateFormate(time: String?,inputFormat:String,outputformat:String): String? {
        val inputPattern = inputFormat
        val outputPattern = outputformat
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }
}