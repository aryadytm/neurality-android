package com.wemadefun.neurality.ui.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wemadefun.neurality.R
import com.wemadefun.neurality.databinding.FragmentFeedbackBinding
import com.wemadefun.neurality.ext.hideKeyboard
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.firebaseutils.FireLogger


class FeedbackFragment : Fragment() {

    companion object {
        var GEO_JSON_STRING = ""
    }

    private lateinit var binding: FragmentFeedbackBinding
    private var contactType = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFeedbackBinding.inflate(inflater, container, false)

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
            hideKeyboard()
        }
        binding.buttonSubmit.setOnClickListener { sendFeedback() }

        ArrayAdapter.createFromResource(requireActivity(),
            R.array.pref_feedback_category,
            android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerType.adapter = adapter
            }

        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                contactType = position
            }
        }

        if (GEO_JSON_STRING.isEmpty()) {
            getGeoData()
        }

        return binding.root
    }

    private fun sendFeedback() {
        val message = binding.editText.text.toString()
        val email = binding.editTextEmail.text.toString()
        val newGeoHashMap = getGeoHashMap()

        if (message.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.please_enter_message), Toast.LENGTH_SHORT).show()
            return
        }
        if (email.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show()
            return
        }

        FireLogger.sendMessage(message, email, contactType, requireContext(), newGeoHashMap)
        when (contactType) {
            1 -> FireCrashlytics.report(Exception("Reported Bug / Error: $message"))
            2 -> FireCrashlytics.report(Exception("Reported Subscription: $message"))
        }

        Toast.makeText(requireContext(), getString(R.string.thanks_feedback), Toast.LENGTH_LONG).show()
        hideKeyboard()
        findNavController().navigateUp()
    }

    private fun getGeoHashMap(): HashMap<String, String> {
        val newGeoHashMap = hashMapOf<String, String>()

        if (GEO_JSON_STRING.length > 100) {
            try {
                val geoHashMap: HashMap<String, Any> = Gson().fromJson(
                    GEO_JSON_STRING, object:TypeToken<HashMap<String, Any>>() {}.type)
                for (key in geoHashMap.keys) { newGeoHashMap["info_$key"] = geoHashMap[key].toString() }
            } catch(e: Exception) { }
        }
        return newGeoHashMap
    }

    private fun getGeoData() {
        val queue = Volley.newRequestQueue(requireContext())
        val url = "https://api.ipgeolocation.io/ipgeo?apiKey=03b08e6a191845f0a8228e5532e75e51"
        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
                if (response.length > 100) {
                    GEO_JSON_STRING = response
                }
            },
            Response.ErrorListener {})
        queue.add(stringRequest)
        queue.start()
    }
}
