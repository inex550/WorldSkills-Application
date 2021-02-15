package com.example.worldskills.ui.user_fragments

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.worldskills.R

class AppInfoFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_app_info, container, false)

        val tv: TextView = v.findViewById(R.id.user_agree_tv)
        tv.movementMethod = LinkMovementMethod.getInstance()

        return v
    }
}