package com.kloc.ktadmin.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kloc.ktadmin.firestoredb.viewmodel.FirestoreViewModel
import com.kloc.ktadmin.firestoredb.viewmodel.singleState
import com.kloc.ktadmin.R
import com.kloc.ktadmin.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTopBar(
    user: singleState,
    signOut: () -> Unit,
    revokeAccess: () -> Unit,
    viewmodel: FirestoreViewModel = hiltViewModel()
) {
    var openMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Icon(
//                    imageVector = Icons.Default.AccountCircle,
//                    contentDescription = "",
//                    modifier = Modifier.size(36.dp) // Increase size of Account Circle icon
//                )
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Red, CircleShape)
                )
                Spacer(modifier = Modifier.padding(6.dp))
                Text(text = "Hii, ${user.data?.user?.name}",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Serif
                )
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.coins),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Yellow, CircleShape)
                    )
                    Spacer(modifier = Modifier.padding(6.dp))
                    Text(text = "${user.data?.user?.coins}",
                        fontSize =18.sp, color = Color.White)
                    Spacer(modifier = Modifier.padding(10.dp))
                    IconButton(
                        onClick = {
                            openMenu = !openMenu
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        },
        actions = {
            DropdownMenu(
                expanded = openMenu,
                onDismissRequest = {
                    openMenu = !openMenu
                }
            ) {
                DropdownMenuItem(
                    onClick = {
                        signOut()
                        openMenu = !openMenu
                    }
                ) {
                    Text(
                        text = Constants.SIGN_OUT_ITEM
                    )
                }
                DropdownMenuItem(
                    onClick = {
                        revokeAccess()
                        openMenu = !openMenu
                    }
                ) {
                    Text(
                        text = Constants.REVOKE_ACCESS_ITEM
                    )
                }
            }
        },
        colors =  TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF6650a4),
            titleContentColor =Color.Transparent,
        )

    )
}
