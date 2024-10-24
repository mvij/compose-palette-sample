package xyz.mvij.palletesample

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.mvij.palletesample.ui.theme.PalleteSampleTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PalleteSampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    modifier: Modifier = Modifier
) {

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var iBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var iPalette by remember { mutableStateOf<Palette?>(null) }
    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            imageUri = uri
            val data = updateData(context, imageUri)
            bitmap = data.first
            iBitmap = data.second
            iPalette = data.third
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    LaunchedEffect(true) {
        scope.launch(Dispatchers.IO) {
            val data = updateData(context, imageUri)
            bitmap = data.first
            iBitmap = data.second
            iPalette = data.third
        }
    }
    iBitmap?.let { imageBitmap ->
        iPalette?.let { palette ->
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(bitmap = imageBitmap, contentDescription = "picked image")
                palette.dominantSwatch?.let { swatch ->
                    Card(
                        modifier = Modifier.padding(top = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(swatch.rgb))
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(swatch.titleTextColor),
                                        fontSize = MaterialTheme.typography.headlineMedium.fontSize
                                    )
                                ) {
                                    append("Dominant: ")
                                }
                                withStyle(style = SpanStyle(color = Color(swatch.rgb))) {
                                    append(" ■ \n")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(swatch.bodyTextColor),
                                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                                    )
                                ) {
                                    append(swatch.toString())
                                }
                            },
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                palette.vibrantSwatch?.let { swatch ->
                    Card(
                        modifier = Modifier.padding(top = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(swatch.rgb))
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(swatch.titleTextColor),
                                        fontSize = MaterialTheme.typography.headlineMedium.fontSize
                                    )
                                ) {
                                    append("Vibrant: ")
                                }
                                withStyle(style = SpanStyle(color = Color(swatch.rgb))) {
                                    append(" ■ \n")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(swatch.bodyTextColor),
                                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                                    )
                                ) {
                                    append(swatch.toString())
                                }
                            },
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                palette.mutedSwatch?.let { swatch ->
                    Card(
                        modifier = Modifier.padding(top = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(swatch.rgb))
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(swatch.titleTextColor),
                                        fontSize = MaterialTheme.typography.headlineMedium.fontSize
                                    )
                                ) {
                                    append("Muted: ")
                                }
                                withStyle(style = SpanStyle(color = Color(swatch.rgb))) {
                                    append(" ■ \n")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(swatch.bodyTextColor),
                                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                                    )
                                ) {
                                    append(swatch.toString())
                                }
                            },
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                Button(onClick = {
                    pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                }, modifier = modifier) {
                    Text("Pick Image")
                }
            }
        }
    }
}

fun updateData(context: Context, imageUri: Uri?): Triple<Bitmap, ImageBitmap, Palette> {
    val bitmap =
        if (imageUri == null) getDefaultImageBitmap(context) else getBitmap(
            context,
            imageUri
        )
    val imageBitmap = bitmap.asImageBitmap()
    val palette = Palette.from(bitmap).generate()
    return Triple(bitmap, imageBitmap, palette)
}

fun getBitmap(context: Context, imageUri: Uri): Bitmap {
    return MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PalleteSampleTheme {
        Greeting()
    }
}

fun getDefaultImageBitmap(context: Context): Bitmap {
    val resources = context.resources
    val metrics = resources.displayMetrics
    val dstWidth = metrics.widthPixels
    val dstHeight = metrics.heightPixels
    val options = Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(resources, R.raw.bg_flower, options)
    options.inJustDecodeBounds = false
    options.inSampleSize = calculateSampleSize(
        options.outWidth, options.outHeight, dstWidth,
        dstHeight
    )
    options.inPreferredConfig = Bitmap.Config.ARGB_8888

    return BitmapFactory.decodeResource(resources, R.raw.bg_flower, options)
}

/**
 * Calculate optimal down-sampling factor given the dimensions of a source
 * image, the dimensions of a destination area and a scaling logic.
 *
 * @param srcWidth Width of source image
 * @param srcHeight Height of source image
 * @param dstWidth Width of destination area
 * @param dstHeight Height of destination area
 * @param scalingLogic Logic to use to avoid image stretching
 * @return Optimal down scaling sample size for decoding
 */
fun calculateSampleSize(
    srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int
): Int {
    val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
    val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

    return if (srcAspect > dstAspect) {
        srcWidth / dstWidth
    } else {
        srcHeight / dstHeight
    }
}