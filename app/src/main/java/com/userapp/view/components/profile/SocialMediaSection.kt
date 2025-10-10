package com.userapp.view.components.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.userapp.R

@Composable
fun SocialMediaSection(
    facebookURL: String?,
    instagramURL: String?,
    tiktokURL: String?
) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        if (!facebookURL.isNullOrEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { openUrl(context, facebookURL) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.facebook),
                    contentDescription = "Facebook",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                extractUsernameFromUri(facebookURL)?.let {
                    Text(
                        text = "@${it}",
                        fontSize = 14.sp,
                    )
                }
            }
        }
        if (!instagramURL.isNullOrEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { openUrl(context, instagramURL) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.instagram),
                    contentDescription = "Instagram",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                extractUsernameFromUri(instagramURL)?.let {
                    Text(
                        text = "@${it}",
                        fontSize = 14.sp,
                    )
                }
            }
        }
        if (!tiktokURL.isNullOrEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { openUrl(context, tiktokURL) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.tiktok),
                    contentDescription = "TikTok",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                extractUsernameFromUri(tiktokURL)?.let {
                    Text(
                        text = "@${it}",
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}

fun openUrl(context: android.content.Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

fun extractUsernameFromUri(uri: String): String? {
    val facebookRegex = Regex("https?://(?:www\\.)?facebook\\.com/([^/]+)")
    val instagramRegex = Regex("https?://(?:www\\.)?instagram\\.com/([^/]+)")
    val tiktokRegex = Regex("https?://(?:www\\.)?tiktok\\.com/@([^/]+)")

    return when {
        facebookRegex.matches(uri) -> {
            facebookRegex.find(uri)?.groups?.get(1)?.value
        }
        instagramRegex.matches(uri) -> {
            instagramRegex.find(uri)?.groups?.get(1)?.value
        }
        tiktokRegex.matches(uri) -> {
            tiktokRegex.find(uri)?.groups?.get(1)?.value
        }
        else -> null
    }
}