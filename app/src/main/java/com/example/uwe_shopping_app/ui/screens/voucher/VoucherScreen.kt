package com.example.uwe_shopping_app.ui.screens.voucher

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uwe_shopping_app.ui.components.voucher.VoucherCard

@Composable
fun VoucherScreen(
    onBackClick: () -> Unit = {},
    onVoucherSelected: (String) -> Unit = {},
    viewModel: VoucherViewModel = viewModel()
) {
    val context = LocalContext.current
    val vouchers by viewModel.vouchers.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            }

            Text(
                text = "Voucher",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
                fontSize = 20.sp
            )
        }

        // Voucher list
        if (vouchers.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No vouchers available", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(vouchers, key = { it.id }) { voucher ->
                    VoucherCard(
                        voucher = voucher,
                        onCopyClick = { code ->
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = android.content.ClipData.newPlainText("Voucher Code", code)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Code copied: $code", Toast.LENGTH_SHORT).show()
                        },
                        onSelectClick = { selectedVoucher ->
                            // Thay vì văng ra, chúng ta thực hiện copy và ở lại màn hình hoặc xử lý theo yêu cầu mới
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = android.content.ClipData.newPlainText("Voucher Code", selectedVoucher.code)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Code copied: ${selectedVoucher.code}", Toast.LENGTH_SHORT).show()
                            
                            // Nếu bạn muốn khi ấn vào voucher là copy luôn (không cần nút copy riêng), 
                            // tôi đã thực hiện copy code ở trên. 
                            // Việc "văng ra" thường do gọi onVoucherSelected hoặc onBackClick() quá sớm.
                        }
                    )
                }
            }
        }
    }
}
