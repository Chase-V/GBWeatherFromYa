package com.tashev.weatherie.view.contentProvider

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tashev.weatherie.R
import com.tashev.weatherie.databinding.ContentProviderFragmentBinding

class ContentProviderFragment : Fragment(R.layout.content_provider_fragment) {

    private val REQUEST_CODE_CONTACTS = 111
    private val REQUEST_CODE_PHONE = 222


    companion object {
        fun newInstance() = ContentProviderFragment()
    }


    private var _binding: ContentProviderFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = ContentProviderFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()

    }

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> getContacts()

                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    showDialog()
                }

                else -> requestPermission()
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE_CONTACTS)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_CONTACTS) {

            when {
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {
                    getContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    showDialog()
                }
                else -> {
                    Log.d("", "КОНЕЦ")
                }
            }
        } else if (requestCode == REQUEST_CODE_PHONE) {
            when {
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {
                    makeCall()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    showDialog()
                }
                else -> {
                    makeCall()
                }
            }
        }

    }

    private fun getContacts() {
        context?.let { it ->
            val contentResolver = it.contentResolver
            val cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
            cursor?.let { cursor ->
                for (i in 0 until cursor.count) {
                    cursor.moveToPosition(i)
                    val name =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                    val contactId =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                    val number = getNumberFromID(contentResolver, contactId)
                    addView(name, number)
                }
            }
            cursor?.close()
        }
    }

    private fun getNumberFromID(cr: ContentResolver, contactId: String): String {
        val phones = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null
        )
        var number: String = "none"
        phones?.let { cursor ->
            while (cursor.moveToNext()) {
                number =
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
            }
        }
        return number
    }

    private fun addView(name: String, number: String) {
        binding.containerForContacts.addView(TextView(requireContext()).apply {
            text = "$name:\n$number"
            textSize = 30f
            setOnClickListener {
                numberCurrent = number
                makeCall()
            }
        })
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Необходимы разрешения")
            .setMessage(
                "Для дальнейшей работы необходимо предоставить доступ к контактам для их отображения, " +
                        "а так же разрешение на совершение звонков для того, что бы иметь возможность позвонить на необходимый номер не выходя из приложения."
            )
            .setPositiveButton("Предоставить доступ") { _, _ ->
                requestPermission()
            }
            .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private var numberCurrent: String = "none"

    private fun makeCall() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$numberCurrent"))
            startActivity(intent)
        } else {
            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CODE_PHONE)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}