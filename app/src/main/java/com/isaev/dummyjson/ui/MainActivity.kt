package com.isaev.dummyjson.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.isaev.dummyjson.DataState
import com.isaev.dummyjson.Product
import com.isaev.dummyjson.R
import com.isaev.dummyjson.ui.theme.DummyJsonTheme
import com.isaev.dummyjson.ui.theme.MainViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        MainAppBar()
                        HorizontalDivider()
                        ProductGrid(products = productsState.value)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainAppBar() {
        TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
    }

    @Composable
    fun ProductGrid(products: List<Product>) {
        val gridState = rememberLazyGridState()
        val firstItemIndexState = remember { derivedStateOf { gridState.firstVisibleItemIndex } }

        val pagingState = viewModel.pagingState.observeAsState()

        Column {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 4.dp),
                state = gridState
            ) {
                val productType = 0
                items(products, contentType = { productType }, key = { it.id }) { product ->
                    ProductGridItem(product)
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (pagingState.value == DataState.LOADING) {
                            CircularProgressIndicator(modifier = Modifier.size(60.dp))
                        } else if (pagingState.value == DataState.FAILURE) {
                            ErrorMessage(onRetry = { viewModel.getMoreProducts() })
                        }
                    }
                }
            }

            if (firstItemIndexState.value >= products.size - 6 && pagingState.value == DataState.SUCCESS) {
                // Чтобы не было бесконечной загрузки в конце (Я так понял в бэкенде 100 элементов максимум)
                if (products.size < 100) {
                    viewModel.getMoreProducts()
                }
            }
        }
    }

    @Composable
    fun ProductGridItem(product: Product) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(360.dp)
            .clickable {
                startActivity(Intent(this, ProductActivity::class.java).apply {
                    putExtra(
                        ProductActivity.PRODUCT_EXTRA_KEY, Json.encodeToString(product)
                    )
                })
            }) {
            AsyncImage(
                model = ImageRequest.Builder(this@MainActivity).data(product.thumbnail)
                    .crossfade(true).build(),
                contentScale = ContentScale.Crop,
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${product.price} $",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                color = Color.White,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
