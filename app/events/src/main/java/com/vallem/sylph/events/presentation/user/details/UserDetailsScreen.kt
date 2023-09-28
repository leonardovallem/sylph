package com.vallem.sylph.events.presentation.user.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseUser
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vallem.componentlibrary.ui.appbar.SylphTopBar
import com.vallem.componentlibrary.ui.data.ProportionChart
import com.vallem.componentlibrary.ui.theme.TransFlagColors
import com.vallem.componentlibrary.ui.theme.zoneEventColors
import com.vallem.componentlibrary.ui.user.UserInfo
import com.vallem.componentlibrary.ui.user.UserInfoSkeleton
import com.vallem.sylph.shared.Routes
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.model.UserDetails
import com.vallem.sylph.shared.domain.model.UserEventsMetaData
import com.vallem.sylph.shared.presentation.components.AlertLevel
import com.vallem.sylph.shared.presentation.components.AlertMessage
import com.vallem.sylph.shared.util.isZeroOrOne

@Destination(route = Routes.Screen.UserDetails)
@Composable
fun UserDetailsScreen(
    userId: String,
    navigator: DestinationsNavigator,
    currentUser: FirebaseUser?,
    viewModel: UserDetailsViewModel = hiltViewModel()
) {
    val result by viewModel.result.collectAsState()

    BackHandler(onBack = navigator::navigateUp)

    LaunchedEffect(userId) {
        viewModel.retrieveUserDetails(userId)
    }

    Scaffold(
        topBar = {
            SylphTopBar(
                title = "Detalhes do usuário",
                navigationIcon = {
                    IconButton(onClick = navigator::popBackStack) {
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = "Menu de navegação"
                        )
                    }
                },
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { pv ->
        Box(
            modifier = Modifier
                .padding(pv)
                .fillMaxSize(),
        ) {
            when (val res = result) {
                is Result.Success -> res.data?.let { UserDetailsScreenBase(it) }

                is Result.Failure -> AlertMessage(
                    title = "Algo deu errado...",
                    description = "Ocorreu um erro ao recuperar os detalhes do usuário.",
                    level = AlertLevel.Error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                )

                Result.Loading -> UserDetailsSkeleton()
            }
        }
    }
}

@Composable
private fun UserDetailsScreenBase(userDetails: UserDetails) {
    val upVotesColor = MaterialTheme.zoneEventColors.safety
    val downVotesColor = MaterialTheme.zoneEventColors.danger

    val chartEntries = remember {
        with(userDetails.eventsMetaData) {
            listOf(
                ProportionChart.Entry(
                    proportion = eventsUpVotes / totalEventsVotes.toFloat(),
                    label = "Aprovações de seus eventos por outros usuários",
                    color = upVotesColor,
                    value = eventsUpVotes,
                ),
                ProportionChart.Entry(
                    proportion = eventsDownVotes / totalEventsVotes.toFloat(),
                    label = "Reprovações de seus eventos por outros usuários",
                    color = downVotesColor,
                    value = eventsDownVotes,
                ),
            )
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp)
    ) {
        UserInfo(
            name = userDetails.name,
            picUrl = userDetails.picUrl,
        )

        Text(
            text = buildAnnotatedString {
                withStyle(MaterialTheme.typography.titleMedium.toSpanStyle()) {
                    append(
                        if (userDetails.eventsMetaData.totalPublishedEvents == 0) "Nenhum"
                        else userDetails.eventsMetaData.totalPublishedEvents.toString()
                    )
                }

                withStyle(MaterialTheme.typography.bodyLarge.toSpanStyle()) {
                    append(
                        if (userDetails.eventsMetaData.totalPublishedEvents.isZeroOrOne()) " evento publicado"
                        else " eventos publicados"
                    )
                }
            },
            modifier = Modifier
                .background(
                    color = if (userDetails.eventsMetaData.totalPublishedEvents < 5) TransFlagColors.BlueSecondary
                    else TransFlagColors.Blue,
                    shape = CircleShape
                )
                .padding(horizontal = 12.dp, vertical = 12.dp)
        )

        ProportionChart(
            entries = chartEntries,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

@Preview
@Composable
fun UserDetailsSkeleton() {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp)
    ) {
        UserInfoSkeleton()

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape
                )
                .size(width = 180.dp, height = 36.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .height(20.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .height(20.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    }
}

@Preview
@Composable
private fun UserDetailsScreenPreview() {
    UserDetailsScreenBase(
        userDetails = UserDetails(
            name = "User Name",
            picUrl = null,
            eventsMetaData = UserEventsMetaData(
                totalPublishedEvents = 4,
                eventsUpVotes = 59,
                eventsDownVotes = 23,
            )
        )
    )
}