import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.userapp.R
import kotlinx.coroutines.launch
import androidx.core.net.toUri

data class SocialMediaLink(
    val platformName: String,
    val url: String
)

@Composable
fun SocialMediaSection(
    facebookURL: String?,
    instagramURL: String?,
    tiktokURL: String?
) {
    val socialLinks = remember(facebookURL, instagramURL, tiktokURL) {
        mutableListOf<SocialMediaLink>().apply {
            facebookURL?.takeIf { it.isNotEmpty() }?.let { add(SocialMediaLink("Facebook", it)) }
            instagramURL?.takeIf { it.isNotEmpty() }?.let { add(SocialMediaLink("Instagram", it)) }
            tiktokURL?.takeIf { it.isNotEmpty() }?.let { add(SocialMediaLink("TikTok", it)) }
        }.toList()
    }

    if (socialLinks.isEmpty()) {
        return
    }

    var showBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showBottomSheet = true }
                .padding(vertical = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_link_24),
                contentDescription = "Abrir redes sociales",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Redes Sociales (${socialLinks.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }

    if (showBottomSheet) {
        SocialMediaBottomSheet(
            socialLinks = socialLinks,
            onDismiss = { showBottomSheet = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialMediaBottomSheet(
    socialLinks: List<SocialMediaLink>,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = { onDismiss() }, // Handles backdrop click
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Redes Sociales",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                socialLinks.forEach { link ->
                    SocialMediaLinkItem(
                        link = link,
                        // Add on click logic to dismiss the sheet after opening the URL
                        onClick = { url ->
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onDismiss()
                                    openUrl(context, url)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SocialMediaLinkItem(
    link: SocialMediaLink,
    onClick: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(link.url) }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.outline_link_24),
            contentDescription = link.platformName,
            tint = MaterialTheme.colorScheme.onSurface, // Use a clear, visible color
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = link.url,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

fun openUrl(context: android.content.Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    context.startActivity(intent)
}