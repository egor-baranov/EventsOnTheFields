package com.usualteam.eventsonthefields

import android.support.v4.app.INotificationSideChannel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.lang.ClassCastException

class LogFragment : Fragment() {
    interface  LogInteractionListener{

    }

    var mListener: LogInteractionListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{
            mListener = context as LogInteractionListener
        } catch (e: ClassCastException){
            throw ClassCastException(context.toString() + " должен реализовывать интерфейс LogInteractionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.log, container, false)
        return view
    }
}