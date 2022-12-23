package com.walterlauk.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.walterlauk.R
import com.walterlauk.activity.AboutUsActivity
import com.walterlauk.activity.ContactUsActivity
import com.walterlauk.activity.SelectLanguageActivity
import com.walterlauk.databinding.SettingsFragmentBinding

class SettingsFragment : BaseFragment(), View.OnClickListener {

    lateinit var settingsFragmentBinding: SettingsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsFragmentBinding = SettingsFragmentBinding.inflate(inflater, container, false)
        return settingsFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsFragmentBinding.lnLanguage.setOnClickListener(this)
        settingsFragmentBinding.lnAbout.setOnClickListener(this)
        settingsFragmentBinding.lnContact.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ln_language -> {
                //creating activity just bcz only only screen after this frag,if in future if the flow continue we have to make select language in next activity's fragment
                requireActivity().startActivity(
                    Intent(
                        activity,
                        SelectLanguageActivity::class.java
                    )
                )
            }
            R.id.ln_about -> {
//creating activity just bcz only only screen after this frag,if in future if the flow continue we have to make select language in next activity's fragment
                requireActivity().startActivity(
                    Intent(
                        activity,
                        AboutUsActivity::class.java
                    )
                )
            }
            R.id.ln_contact -> {
//creating activity just bcz only only screen after this frag,if in future if the flow continue we have to make select language in next activity's fragment
                requireActivity().startActivity(
                    Intent(
                        activity,
                        ContactUsActivity::class.java
                    )
                )
            }
        }

    }
}