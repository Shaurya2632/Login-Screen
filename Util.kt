package com.example.jetpack

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import androidx.core.graphics.createBitmap
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

object Util {
    val RAINBOW = setGradiant(listOf(
        Color.Red,
        toColor("FF7F00"),
        Color.Yellow,
        Color.Green,
        Color.Blue,
        toColor("4B0082"),
        toColor("8B00FF")
    ))

    val WH = setGradiant(listOf(
        Color.Black,
        Color.White
    ))

    fun toColor(hex: String): Color =
        Color("FF$hex".toLong(16))

    fun Modifier.setBG(color: String): Modifier =
        this
            .fillMaxSize()
            .background(toColor(color))

    fun Modifier.setBG(color: Color): Modifier =
        this
            .fillMaxSize()
            .background(color)

    @Composable
    fun <T> setChangeable(item: T): MutableState<T> {
        return remember { mutableStateOf(item) }
    }

    @Composable
    fun <T> save(item: T): MutableState<T> {
        return rememberSaveable { mutableStateOf(item) }
    }

    @Composable
    fun SetLottieAnimation(id: Int, runTimeSec: Float) {
        val comp by rememberLottieComposition(LottieCompositionSpec.RawRes(id))
        var progress by Util.setChangeable(1f)
        val dur = comp?.duration?.div(1000f) ?: 1f

        LaunchedEffect(comp) {
            val start = System.currentTimeMillis()
            while ((System.currentTimeMillis() - start) / 1000f < runTimeSec) {
                val elapsed = (System.currentTimeMillis() - start) / 1000f
                progress = (elapsed % dur) / dur
            }
        }
        LottieAnimation(comp, progress = { progress })
    }

    @Composable
    fun setCollection(collection: Any?): Any? {
        return when (collection) {
            is List<*> -> rememberSaveable {collection.toMutableList()}
            is Array<*> -> rememberSaveable {collection.toMutableList()}
            is Set<*> -> rememberSaveable {collection.toMutableSet()}
            else -> null
        }
    }

    fun setGradiant(colors: List<Color>): Brush{
        return Brush.linearGradient(colors)
    }

    @Composable
    fun getKeyboard(options: KeyboardType): KeyboardOptions {
        return KeyboardOptions(keyboardType = options)
    }

    @Composable
    fun makeLinker(name: String, url: String, color: Color): AnnotatedString {
        val uriHandler = LocalUriHandler.current

        return buildAnnotatedString {
            val link = LinkAnnotation.Url(
                url,
                TextLinkStyles(
                    SpanStyle(
                        color = color
                    )
                )
            ) {
                val url = (it as LinkAnnotation.Url).url
                uriHandler.openUri(url)
            }

            withLink(link) {
                append(name)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AdvTimePicker(
        onConfirm: (TimePickerState) -> Unit,
        onDismiss: () -> Unit
    ) {
        val currentTime = Calendar.getInstance()
        val timePickerState = rememberTimePickerState(
            currentTime.get(Calendar.HOUR_OF_DAY),
            currentTime.get(Calendar.MINUTE),
            true
        )
        var showDial by setChangeable(true)
        val toggleIcon = if (showDial) Icons.Default.EditCalendar else Icons.Default.AccessTime

        AdvTimePickerDialog(
            onDismiss = {onDismiss()},
            onConfirm = {onConfirm(timePickerState)},
            toggle = {
                IconButton({showDial = !showDial}) {
                    Icon(toggleIcon, null)
                }
            }
        ){
            if (showDial) TimePicker(timePickerState) else TimeInput(timePickerState)
        }


    }

    @Composable
    private fun AdvTimePickerDialog(
        title: String = "Select Time",
        onDismiss: () -> Unit,
        onConfirm: () -> Unit,
        toggle: @Composable () -> Unit = {},
        content: @Composable () -> Unit = {}
    ){
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)
                    .background(
                        shape = MaterialTheme.shapes.extraLarge,
                        color = MaterialTheme.colorScheme.surface
                    )
            ){
                Column(
                    Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        title,
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                    content()

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ){
                        toggle()
                        Spacer(Modifier.weight(1f))
                        TextButton(onDismiss) { Text("Cancel")}
                        TextButton(onConfirm) { Text("OK")}
                    }
                }
            }
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Searcher(
        textFieldState: TextFieldState,
        onSearch: (String) -> Unit,
        searchResults: List<String>,
        modifier: Modifier = Modifier
    ){
        var expanded by save(false)

        Box(
            modifier
                .fillMaxSize()
                .padding(16.dp)
                .semantics { isTraversalGroup = true }
        ){
            SearchBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .semantics { isTraversalGroup = true },
                inputField = {
                    InputField(
                        textFieldState.text.toString(),
                        {textFieldState.edit { replace(0, length, it)} },
                        onSearch = {
                            onSearch(textFieldState.text.toString())
                            expanded = false
                        },
                        expanded = expanded,
                        onExpandedChange = {expanded = it},
                        placeholder = {Text("Search")}
                    )
                },
                expanded = expanded,
                onExpandedChange = {expanded = it}
            ) {
                Column(Modifier.verticalScroll(rememberScrollState())){
                    searchResults.forEach {
                        ListItem(
                            headlineContent = {Text(it)},
                            Modifier
                                .clickable {
                                    textFieldState.edit { replace(0, length, it) }
                                    expanded = false
                                }
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }

    }

}

object MapUtil {
    fun checkForPermission(context: Context): Boolean {
        return !(ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED)
    }

    fun String.capitaliseIt() = this.lowercase().capitalize(Locale.current)

    fun calculateDistance(latlngList: List<LatLng>): Double {
        var totalDistance = 0.0

        for (i in 0 until latlngList.size - 1) {
            totalDistance += SphericalUtil.computeDistanceBetween(latlngList[i],latlngList[i + 1])

        }

        return (totalDistance * 0.001)
    }

    fun calculateSurfaceArea(latlngList: List<LatLng>): Double {
        if (latlngList.size < 3) {
            return 0.0
        }
        return SphericalUtil.computeArea(latlngList)
    }

    fun formattedValue(value: Double) = String.format(java.util.Locale.US,"%.2f",value)

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context, onLocationFetched: (location: LatLng) -> Unit) {
        var loc: LatLng
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    loc = LatLng(latitude,longitude)
                    onLocationFetched(loc)
                }
            }
            .addOnFailureListener { exception: Exception ->
                // Handle failure to get location
                Log.d("MAP-EXCEPTION",exception.message.toString())
            }

    }

    fun bitmapDescriptor(context: Context, resId: Int): BitmapDescriptor? {

        val drawable = ContextCompat.getDrawable(context, resId) ?: return null
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val bm = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)

        val canvas = android.graphics.Canvas(bm)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bm)
    }
}

object Mather{
    fun evaluate(expr: String): Double {
        val values = mutableListOf<Double>()
        val ops = mutableListOf<Char>()
        var i = 0

        fun precedence(c: Char) = if (c == '+' || c == '-') 1 else 2
        fun applyOp(a: Double, b: Double, op: Char) = when(op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> if (b == 0.0) throw ArithmeticException("Division by zero") else a / b
            else -> 0.0
        }

        while (i < expr.length) {
            val c = expr[i]

            when {
                c == ' ' -> {}
                c.isDigit() || c == '.' -> {
                    val sb = StringBuilder()
                    while (i < expr.length && (expr[i].isDigit() || expr[i] == '.')) { sb.append(expr[i]); i++ }
                    values.add(sb.toString().toDouble())
                    i--
                }
                c in "+-*/" -> {
                    // Handle unary + or -
                    if ((i == 0 || expr[i-1] in "+-*/") && c in "+-") {
                        val sb = StringBuilder().append(c)
                        i++
                        while (i < expr.length && (expr[i].isDigit() || expr[i] == '.')) { sb.append(expr[i]); i++ }
                        values.add(sb.toString().toDouble())
                        i--
                        i++
                        continue
                    }

                    while (ops.isNotEmpty() && precedence(c) <= precedence(ops.last())) {
                        val b = values.removeAt(values.lastIndex)
                        val a = values.removeAt(values.lastIndex)
                        values.add(applyOp(a, b, ops.removeAt(ops.lastIndex)))
                    }
                    ops.add(c)
                }
                else -> throw IllegalArgumentException("Invalid character: $c")
            }
            i++
        }

        while (ops.isNotEmpty()) {
            if (values.size < 2) throw IllegalArgumentException("Invalid expression")
            val b = values.removeAt(values.lastIndex)
            val a = values.removeAt(values.lastIndex)
            values.add(applyOp(a, b, ops.removeAt(ops.lastIndex)))
        }

        if (values.size != 1) throw IllegalArgumentException("Invalid expression")
        return values.first()
    }
}