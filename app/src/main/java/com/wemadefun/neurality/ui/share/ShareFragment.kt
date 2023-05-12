package com.wemadefun.neurality.ui.share

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.wemadefun.neurality.R
import com.wemadefun.neurality.authentication.Authentication
import com.wemadefun.neurality.databinding.FragmentShareBinding
import com.wemadefun.neurality.ext.easyToastLong
import com.wemadefun.neurality.ext.hideKeyboard
import com.wemadefun.neurality.ext.setImageWithGlide
import com.wemadefun.neurality.firebaseutils.FireAnalytics
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.utils.getCategoryString
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ShareFragment : Fragment() {

    private lateinit var binding: FragmentShareBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        FireAnalytics.eventShareScreen()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentShareBinding.inflate(inflater, container, false)
        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
            hideKeyboard()
        }
        binding.buttonShare.setOnClickListener { share() }
        binding.cardThumbnail.preventCornerOverlap = true

        try {
            val editable: Editable = SpannableStringBuilder(Authentication.getAuthName())
            binding.editTextName.text = editable
        } catch (e: Exception) {
            FireCrashlytics.report(e)
        }

        updateUI()
        return binding.root
    }

    private fun updateUI() {
        val context = requireContext()
        val gameData = ShareFragmentArgs.fromBundle(requireArguments()).argGameData
        val score = ShareFragmentArgs.fromBundle(requireArguments()).argScore
        binding.shareResult.visibility = View.VISIBLE
        binding.shareResult.alpha = 0F
        binding.imageView.setImageWithGlide(ContextCompat.getDrawable(context, gameData.drawableImgId))
        binding.textScore.text = score
        binding.textCategory.text = getCategoryString(gameData.category, context.resources)
        binding.textTitle.text = gameData.title
        binding.resultImage.setImageWithGlide(ContextCompat.getDrawable(context, gameData.drawableImgId))
        binding.resultTextScore.text = score
        binding.resultCategory.text = getCategoryString(gameData.category, context.resources)
        binding.resultTitle.text = gameData.title
    }

    private fun share() {
        try {
            hideKeyboard()
            binding.resultName.text = binding.editTextName.text.toString()
            val shareUri = getShareImageUri()
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "#Neurality #BrainTraining")
                putExtra(Intent.EXTRA_STREAM, shareUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "image/png"
            }
            startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.share)))
        } catch (e: Exception) {
            FireCrashlytics.report(e)
            easyToastLong(getString(R.string.feature_notavailable))
        }
    }

    private fun getShareImageUri(): Uri {
        val context = requireContext()
        binding.shareResult.alpha = 1F
        val screenBitmap = getBitmapFromView(binding.shareResult)
        binding.shareResult.alpha = 0F
        val fileName = "neurality_score_share.png"
        val folderPath = File(context.filesDir, "images")
        val filePath = File(folderPath, fileName)

        // Save bitmap to cache directory
        try {
            if (!folderPath.exists()) {
                folderPath.mkdirs() // don't forget to make the directory
            }
            val stream = FileOutputStream("$folderPath/$fileName") // overwrites this image every time
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            FireCrashlytics.report(e)
            e.printStackTrace()
        }

        return FileProvider.getUriForFile(context, "com.wemadefun.neurality.provider", filePath)
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) { bgDrawable.draw(canvas) }
        else { canvas.drawColor(Color.WHITE) }
        view.draw(canvas)
        return bitmap
    }


}