package com.kloc.ktadmin.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NameField(
    name: TextFieldValue,
    onNameValueChange: (newValue: TextFieldValue) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
    value = name,
    onValueChange = { newValue ->
        onNameValueChange(newValue)
    },
    label = {
        Text(
            text ="Name",
            color = Color.White,
            fontSize = 16.sp
        )
    },
    singleLine = true,
    keyboardOptions = KeyboardOptions(
    keyboardType = KeyboardType.Text
    ),
    modifier = Modifier
    .focusRequester(focusRequester)
    .padding(4.dp)
    .fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    textStyle = TextStyle(color = Color.White)
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}