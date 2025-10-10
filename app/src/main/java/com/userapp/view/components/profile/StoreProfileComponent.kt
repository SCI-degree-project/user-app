package com.userapp.view.components.profile

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import com.userapp.R
import com.userapp.model.StoreDetails
import com.userapp.viewmodel.profile.StoreProfileViewModel

@Composable
fun StoreProfileComponent(
    store: StoreDetails
) {
//    val store = remember {
//        mutableStateOf(
//            StoreDto(
//                name = "Muebles Cochabamba",
//                description = "Tienda especializada en muebles de alta calidad para el hogar y oficina. Ofrecemos diseños modernos y clásicos, con materiales duraderos y precios competitivos. Visítanos o contáctanos para personalizar tus espacios.",
//                address = "Av. América #123, Cochabamba, Bolivia",
//                phone = "+591 70012345",
//                facebookURL = "https://www.facebook.com/muebles.cochabamba",
//                instagramURL = "https://www.instagram.com/mueblescocha",
//                tiktokURL = "https://www.tiktok.com/@muebles.cochita"
//            )
//        ).value
//    }

    var isExpanded by remember { mutableStateOf(false) }
    val maxLines = 3

    Column(
    modifier = Modifier
    .fillMaxSize()
    .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.rounded_home_28),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(120.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                .padding(4.dp)
                .background(Color.Gray, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = store.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            store.description?.let { description ->
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                    .animateContentSize()
                ) {

                    Text(
                        text = buildAnnotatedString {
                            append(description.take(if (isExpanded) description.length else maxLines * 44))
                            if (!isExpanded && description.length > maxLines * 50) {
                                pushStringAnnotation(tag = "verMas", annotation = "verMas")
                                append("...")
                                withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Bold)) {
                                    append(" Ver más ")
                                }
                                pop()
                            }
                        },
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        modifier = Modifier
                            .clickable {
                                if (!isExpanded) {
                                    isExpanded = true
                                }
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ContactInfoSection(
                phone = store.phone,
                address = store.address
            )

            Spacer(modifier = Modifier.height(16.dp))

            SocialMediaSection(
                facebookURL = store.facebookURL,
                instagramURL = store.instagramURL,
                tiktokURL = store.tiktokURL
            )
        }

    }
}