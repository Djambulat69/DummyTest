package com.isaev.dummyjson

import android.os.Bundle
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.IntState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.isaev.dummyjson.ui.theme.DummyJsonTheme
import com.isaev.dummyjson.ui.theme.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val IMAGES_CHANGE_LAUNCH_KEY = 0

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val productsState = viewModel.products.observeAsState(initial = emptyList())

            DummyJsonTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    ProductList(productsState.value, viewModel)
                }
            }
        }
    }
}

@Composable
fun ProductList(products: List<Product>, viewModel: MainViewModel) {

    val listState = rememberLazyListState()
    val pagingState = viewModel.pagingState.observeAsState()

    val firstItemIndexState = remember { derivedStateOf { listState.firstVisibleItemIndex } }

    val photoIndex = rememberSaveable { mutableIntStateOf(0) }
    val MAX_IMAGES_COUNT = 10

    LaunchedEffect(IMAGES_CHANGE_LAUNCH_KEY) {
        launch {
            while (true) {
                delay(5000)
                photoIndex.intValue = (photoIndex.intValue + 1) % MAX_IMAGES_COUNT
            }
        }
    }

    LazyColumn(
        state = listState, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(products) {
            ProductItem(product = it, photoIndex)
        }

        if (pagingState.value == DataState.LOADING) {
            item {
                CircularProgressIndicator()
            }
        } else if (pagingState.value == DataState.FAILURE) {
            item {
                ErrorMessage(onRetry = { viewModel.getMoreProducts() })
            }
        }
    }

    if (firstItemIndexState.value >= products.size - 10 && pagingState.value == DataState.SUCCESS) {
        // Чтобы не было бесконечной загрузки в конце (Я так понял в бэкенде 100 элементов максимум)
        if (products.size < 100) {
            viewModel.getMoreProducts()
        }
    }
}

@Composable
fun ProductItem(product: Product, photoIndex: IntState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 6.dp)
            .wrapContentHeight()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(product.images[photoIndex.intValue % product.images.size]).transitionFactory(
                    CrossfadeTransitionFactoryFixed()
                ).build(),
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
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ErrorMessage(onRetry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.wrong_message)
        )
        Button(onClick = { onRetry() }) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}
