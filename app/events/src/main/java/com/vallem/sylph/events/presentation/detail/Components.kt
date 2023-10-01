package com.vallem.sylph.events.presentation.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vallem.componentlibrary.ui.bottomsheet.SylphBottomSheet
import com.vallem.componentlibrary.ui.loading.SylphLoading
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.sylph.shared.presentation.components.AlertLevel
import com.vallem.sylph.shared.presentation.components.AlertMessage

@Composable
internal fun DetailsLoading(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        SylphLoading.Circular()
    }
}

@Composable
internal fun DetailsRetrievalError(modifier: Modifier = Modifier) {
    AlertMessage(
        title = "Algo deu errado...",
        description = "Ocorreu um erro ao recuperar os detalhes desse evento.",
        level = AlertLevel.Error,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Preview
@Composable
private fun DetailsLoadingPreview() {
    SylphTheme {
        SylphBottomSheet {
            DetailsLoading(Modifier.padding(it))
        }
    }
}

@Preview
@Composable
private fun DetailsErrorPreview() {
    SylphTheme {
        SylphBottomSheet {
            DetailsRetrievalError(Modifier.padding(it))
        }
    }
}