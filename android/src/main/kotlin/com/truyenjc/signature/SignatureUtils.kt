package com.truyenjc.signature

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import com.google.common.io.BaseEncoding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


object SignatureUtils {
    fun getSignatures(pm: PackageManager, packageName: String): String? {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
                if (packageInfo?.signingInfo == null) {
                    return null
                }
                return if (packageInfo.signingInfo.hasMultipleSigners()) {
                    signatureDigest(packageInfo.signingInfo.apkContentsSigners[0])
                } else {
                    signatureDigest(packageInfo.signingInfo.signingCertificateHistory[0])
                }
            } else {
                @SuppressLint("PackageManagerGetSignatures")
                val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
                if (packageInfo?.signatures == null
                        || packageInfo.signatures.isEmpty()
                        || packageInfo.signatures[0] == null) {
                    return null
                }
                return signatureDigest(packageInfo.signatures[0])
            }
        } catch (e: PackageManager.NameNotFoundException) {
            return null
        }
    }

    private fun signatureDigest(sig: Signature): String? {
        val signature = sig.toByteArray()
        return try {
            val md = MessageDigest.getInstance("SHA1")
            val digest = md.digest(signature)
            BaseEncoding.base16().lowerCase().encode(digest)
        } catch (e: NoSuchAlgorithmException) {
            null
        }

    }
}