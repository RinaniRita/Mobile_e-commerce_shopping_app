package com.example.uwe_shopping_app.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

// --------------------------------------------------------------------------------------------
// UNDERLINED TEXT FIELD
// --------------------------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnderlinedField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {

        Text(
            text = label,
            fontSize = 13.sp,
            color = Color(0xFFB4B1C2),
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(4.dp))

        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                color = Color(0xFF222222)
            )
        )

        Divider(
            color = Color(0xFFE5E5E5),
            thickness = 1.dp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetting() {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Profile Setting",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF222222)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            tint = Color.Unspecified,
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item { Spacer(Modifier.height(20.dp)) }

            // ---------------- AVATAR ----------------
            item {
                Box(
                    modifier = Modifier.size(150.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {

                    // Avatar KHÔNG nền hồng
                    Image(
                        painter = painterResource(id = R.drawable.ic_profile_placeholder),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )


                    // Nút camera đẹp như Figma
                    Box(
                        modifier = Modifier
                            .offset(x = (-6).dp, y = (-6).dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE0E0E0), CircleShape)
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = "Camera",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))
            }

            // ---------------- FIRST + LAST NAME ----------------
            item {
                var firstName by remember { mutableStateOf("Sunie") }
                var lastName by remember { mutableStateOf("Pham") }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    UnderlinedField("First Name", firstName, { firstName = it }, Modifier.weight(1f))
                    UnderlinedField("Last Name", lastName, { lastName = it }, Modifier.weight(1f))
                }

                Spacer(Modifier.height(20.dp))
            }

            // ---------------- EMAIL ----------------
            item {
                var email by remember { mutableStateOf("sunieux@gmail.com") }

                UnderlinedField("Email", email, { email = it })

                Spacer(Modifier.height(20.dp))
            }

            // ---------------- GENDER + PHONE ----------------
            item {
                var gender by remember { mutableStateOf("Female") }
                var phone by remember { mutableStateOf("(+1) 23456789") }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    UnderlinedField("Gender", gender, { gender = it }, Modifier.weight(1f))
                    UnderlinedField("Phone", phone, { phone = it }, Modifier.weight(1f))
                }

                Spacer(Modifier.height(50.dp))
            }

            // ---------------- SAVE BUTTON ----------------
            item {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF333333),
                        contentColor = Color.White
                    )
                ) {
                    Text("Save change", fontSize = 16.sp)
                }

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}



// --------------------------------------------------------------------------------------------
// PREVIEW
// --------------------------------------------------------------------------------------------
@Preview(showBackground = true, widthDp = 375, heightDp = 800)
@Composable
fun ProfileSettingview() {
    Uwe_shopping_appTheme {
        ProfileSetting()
    }
}
