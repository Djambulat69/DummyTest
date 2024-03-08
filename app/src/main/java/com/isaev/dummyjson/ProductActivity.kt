package com.isaev.dummyjson

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
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
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ProductAppBar(title: String) {
        TopAppBar(
            title = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
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

