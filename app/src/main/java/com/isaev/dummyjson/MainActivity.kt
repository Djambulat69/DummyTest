package com.isaev.dummyjson

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.isaev.dummyjson.ui.theme.DummyJsonTheme
import com.isaev.dummyjson.ui.theme.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val products = viewModel.products.observeAsState(initial = emptyList())

            DummyJsonTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {

                    ProductList(products) {
                        viewModel.getMoreProducts()
                    }
                }
            }
        }
    }
}

@Composable
fun ProductList(products: State<List<Product>>, getMoreProducts: () -> Unit) {

    val listState = rememberLazyListState()

    val firstItemIndexState = remember { derivedStateOf { listState.firstVisibleItemIndex } }

    LazyColumn(
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(products.value) {
            ProductItem(product = it)
        }

        item {
            CircularProgressIndicator()
        }
    }

    if (firstItemIndexState.value >= products.value.size - 10) {
        Log.i(
            "TAG",
            "firstItemIndex = ${firstItemIndexState.value}, products.size = ${products.value.size}"
        )
        getMoreProducts()
    }
}

@Composable
fun ProductItem(product: Product) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .wrapContentHeight()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(product.thumbnail)
                .crossfade(true).build(),
            contentScale = ContentScale.Crop,
            contentDescription = product.title,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = product.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductItemPreview() {
    ProductItem(
        Product(
            312213,
            "PRODUCT TITLE",
            "DESCRIPTION",
            1000,
            0.5,
            0.7,
            2000,
            "BRAND",
            "CATEGORY",
            "THUMBNAIL",
            emptyList()
        )
    )
}

