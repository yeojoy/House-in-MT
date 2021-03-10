package me.yeojoy.hancahouse.util

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import me.yeojoy.hancahouse.app.Constants
import me.yeojoy.hancahouse.detail.ItemDetailContract

class WebPageManager<T> {
    companion object {
        val TAG = WebPageManager::class.simpleName
    }

    fun parseWholePage(presenter: ItemDetailContract.Presenter<T>, contents: String)
            : SpannableStringBuilder {
        Log.d(TAG, "contents : $contents")
        val spannableStringBuilder = SpannableStringBuilder()
        val indexOfEmail = contents.indexOf(Constants.TITLE_EMAIL) + Constants.TITLE_EMAIL.length
        val indexOfPhoneStart = contents.indexOf(Constants.TITLE_PHONE)
        val indexOfPhoneEnd = indexOfPhoneStart + Constants.TITLE_PHONE.length
        val indexOfRegion = contents.indexOf(Constants.TITLE_REGION)
        spannableStringBuilder.append(contents.substring(0, indexOfEmail))
        spannableStringBuilder.append(" ")

        var email = contents.substring(indexOfEmail, indexOfPhoneStart)
        email = email.trim { it <= ' ' }

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            val emailSpannableString = SpannableString(email)
            emailSpannableString.setSpan(object : ClickableSpan() {
                override fun onClick(view: View) {
                    presenter.emailClicked(emailSpannableString.toString())
                }
            }, 0, emailSpannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableStringBuilder.append(emailSpannableString)
        } else {
            spannableStringBuilder.append(email)
        }
        spannableStringBuilder.append(System.lineSeparator())
        spannableStringBuilder.append(contents.substring(indexOfPhoneStart, indexOfPhoneEnd))
        spannableStringBuilder.append(" ")
        val phoneNumber = contents.substring(indexOfPhoneEnd, indexOfRegion)
        val finalPhoneNumber = phoneNumber.trim { it <= ' ' }
        if (Patterns.PHONE.matcher(finalPhoneNumber).matches()) {
            val phoneSpannableString = SpannableString(finalPhoneNumber)
            phoneSpannableString.setSpan(object : ClickableSpan() {
                override fun onClick(view: View) {
                    presenter.telephoneNumberClicked(phoneSpannableString.toString())
                }
            }, 0, phoneSpannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableStringBuilder.append(phoneSpannableString)
        } else {
            spannableStringBuilder.append(finalPhoneNumber)
        }
        spannableStringBuilder.append(System.lineSeparator())
        spannableStringBuilder.append(contents.substring(indexOfRegion))
        return spannableStringBuilder
    }
}