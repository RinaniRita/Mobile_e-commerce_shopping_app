package com.example.uwe_shopping_app.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uwe_shopping_app.R
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onEditClick: () -> Unit = {}) {

    // Không cần bottomBar ở đây nữa, vì sẽ được xử lý ở level cao hơn (MainScaffold hoặc tương tự)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(Modifier.height(16.dp))

        // ---------------- HEADER WITH AVATAR, NAME, EMAIL, SETTINGS ICON ----------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar with pink background
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFFFFC0CB), CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile_placeholder),
                    contentDescription = "Avatar",
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.width(16.dp))

            // Name and Email
            Column {
                Text(
                    text = "Sunie Pham",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222)
                )
                Text(
                    text = "sunieux@gmail.com",
                    fontSize = 14.sp,
                    color = Color(0xFF808080)
                )
            }

            Spacer(Modifier.weight(1f))

            // Settings icon (gear)
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Edit Profile",
                    tint = Color(0xFF222222)
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        // ---------------- MENU ITEMS ----------------
        val menuItems = listOf(
            "Address" to Icons.Default.LocationOn,
            "Payment method" to Icons.Default.Payment,
            "Voucher" to Icons.Default.LocalOffer,
            "My Wishlist" to Icons.Outlined.FavoriteBorder,
            "Rate this app" to Icons.Outlined.StarBorder,
            "Log out" to Icons.Default.ExitToApp
        )

        menuItems.forEach { (text, icon) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Handle click */ }
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF808080),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = text,
                    fontSize = 16.sp,
                    color = Color(0xFF808080)
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF222222)
                )
            }
        }
    }
}

// --------------------------------------------------------------------------------------------
// PREVIEW
// --------------------------------------------------------------------------------------------
@Preview(showBackground = true, widthDp = 375, heightDp = 800)
@Composable
fun ProfileScreenPreview() {  // Đổi tên lại để tránh conflict nếu cần
    Uwe_shopping_appTheme {
        ProfileScreen()
    }
}