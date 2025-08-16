package com.example.jetpack.apps

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack.Util.setBG
import com.example.jetpack.R
import com.example.jetpack.Util

@Preview(device = "spec:width=1080px,height=2200px,dpi=460", showSystemUi = true)
@Composable
fun LoginScreen() {
    var email by Util.setChangeable("")
    var password by Util.setChangeable("")

    Column(Modifier.setBG("ffffff"), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Login",
            Modifier.padding(top = 180.dp, end = 180.dp),
            fontSize = 45.sp,
            fontFamily = getFont(R.font.inter_bold)
        )
        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = { Text("Email") },
            modifier = Modifier.width(300.dp).offset(y = 10.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = { Text("Password") },
            modifier = Modifier.width(300.dp).offset(y = 20.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = Util.getKeyboard(KeyboardType.Password)
        )
        Text(
            buildAnnotatedString {
                append("Don't Have An Account? ")
                withStyle(style = SpanStyle(color = Color.Blue)) {
                    append("Sign Up")
                }
            },
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 34.dp).width(360.dp),
            textAlign = TextAlign.Center,
            fontFamily = getFont(R.font.poppins),
            letterSpacing = 0.8.sp
        )

        Text(
            "Other Options",
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 50.dp).width(360.dp),
            textAlign = TextAlign.Center,
            fontFamily = getFont(R.font.poppins),
            letterSpacing = 0.8.sp
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            OutlinedButton(onClick = {}, modifier = Modifier.padding(top = 10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(text = "Sign in with")
                    Spacer(Modifier.width(4.dp))
                    Image(
                        painter = painterResource(id = R.drawable.google_logo),
                        contentDescription = "Google Logo",
                        modifier = Modifier.size(21.dp)
                    )
                }
            }

            OutlinedButton(onClick = {}, modifier = Modifier.padding(top = 10.dp, start = 6.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(text = "Sign in with")
                    Spacer(Modifier.width(4.dp))
                    Image(
                        painter = painterResource(id = R.drawable.facebook_logo),
                        contentDescription = "FacebookLogo",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Button(
            onClick = {},
            Modifier.padding(top = 45.dp)
        ) {
            Text("Login", fontSize = 20.sp, fontFamily = getFont(R.font.inter_bold))
        }
    }
}

@Composable
fun getFont(id: Int): FontFamily {
    return FontFamily(Font(id, FontWeight.Normal))
}

