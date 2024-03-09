package com.isaev.dummyjson.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.isaev.dummyjson.Product
import com.isaev.dummyjson.R
import com.isaev.dummyjson.ui.theme.DummyJsonTheme
import kotlinx.serialization.json.Json

class ProductActivity : ComponentActivity() {
    private val product: Product by lazy {
        Json.decodeFromString<Product>(
            intent.extras!!.getString(
                PRODUCT_EXTRA_KEY
            )!!
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DummyJsonTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        ProductAppBar(product.title)
                        HorizontalDivider()

                        val pagerState = rememberPagerState(pageCount = { product.images.size })

                        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            val vertScrollState = rememberScrollState()

                            Column(Modifier.verticalScroll(vertScrollState)) {
                                ImagesPager(pagerState = pagerState)
                                Spacer(modifier = Modifier.height(12.dp))
                                Description()
                            }
                        } else {
                            Row {
                                ImagesPager(
                                    Modifier.weight(1f), pagerState
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                val scrollState = rememberScrollState()

                                Column(
                                    Modifier
                                        .weight(1f)
                                        .verticalScroll(scrollState)
                                ) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Description()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ImagesPager(modifier: Modifier = Modifier, pagerState: PagerState) {

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = modifier
        )
        {
            HorizontalPager(
                state = pagerState,
                Modifier
                    .fillMaxWidth()
                    .height(360.dp)
            ) { page ->
                AsyncImage(
                    model = ImageRequest.Builder(this@ProductActivity).data(product.images[page])
                        .crossfade(true).build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = product.title,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Row {
                repeat(pagerState.pageCount) { i ->
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (i == pagerState.currentPage) Color.DarkGray else Color.LightGray)
                    ) {}
                }
            }
        }
    }

    @Composable
    fun Description() {
        Column(Modifier.padding(horizontal = 8.dp)) {

            Row {
                Text(
                    text = "${product.price} $",
                    style = MaterialTheme.typography.headlineLarge,
                    maxLines = 1,
                    color = Color.White,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = product.rating.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        maxLines = 1,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.Star,
                        tint = Color.White,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = product.description, style = MaterialTheme.typography.headlineSmall
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ProductAppBar(title: String) {
        TopAppBar(title = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
            navigationIcon = {
                IconButton(onClick = { finish() }) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = "back"
                    )
                }
            })
    }

    companion object {
        const val PRODUCT_EXTRA_KEY = "product extra key"
    }
}

