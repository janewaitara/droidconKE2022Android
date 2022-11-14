/*
 * Copyright 2022 DroidconKE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android254.presentation.home.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android254.presentation.R
import com.android254.presentation.common.components.DroidconAppBar
import com.android254.presentation.common.components.DroidconAppBarWithFeedbackButton
import com.android254.presentation.common.components.SponsorsCard
import com.android254.presentation.common.theme.DroidconKE2022Theme
import com.android254.presentation.home.components.*
import com.android254.presentation.home.viewmodel.HomeViewModel
import com.android254.presentation.models.SessionPresentationModel
import com.android254.presentation.sessions.view.SessionsViewModel
import com.android254.presentation.speakers.SpeakersViewModel
import com.droidconke.chai.atoms.type.MontserratSemiBold

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    speakersViewModel: SpeakersViewModel = hiltViewModel(),
    sessionsViewModel: SessionsViewModel = hiltViewModel(),
    navigateToSpeakers: () -> Unit = {},
    navigateToSpeaker: (String) -> Unit = {},
    navigateToFeedbackScreen: () -> Unit = {},
    navigateToSessionScreen: () -> Unit = {},
    onActionClicked: () -> Unit = {},
    onSessionClicked: (SessionPresentationModel) -> Unit = {},
) {
    val homeViewState = homeViewModel.viewState
    val sponsorsLogos = listOf("Google", "Company Z", "Company Y")

    Scaffold(
        topBar = {
            HomeToolbar(
                isSignedIn = homeViewState.isSignedIn,
                navigateToFeedbackScreen = navigateToFeedbackScreen,
                onActionClicked = onActionClicked
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.home_header_welcome_label),
                modifier = Modifier.testTag("home_header"),
                fontFamily = MontserratSemiBold,
                fontSize = 16.sp
            )
            HomeBannerSection(homeViewState)
            HomeSpacer()
            HomeSessionSection(
                sessions = sessionsViewModel.sessions.value,
                onSessionClick = onSessionClicked,
                onViewAllSessionClicked = navigateToSessionScreen,
            )
            HomeSpacer()
            HomeSpeakersSection(
                speakers = speakersViewModel.getSpeakers(),
                navigateToSpeakers = navigateToSpeakers,
                navigateToSpeaker = navigateToSpeaker
            )
            HomeSpacer()
            SponsorsCard(sponsorsLogos = sponsorsLogos)
            HomeSpacer()
        }
    }
}

@Composable
fun HomeToolbar(
    isSignedIn: Boolean,
    navigateToFeedbackScreen: () -> Unit = {},
    onActionClicked: () -> Unit = {},
) {
    if (isSignedIn) {
        DroidconAppBarWithFeedbackButton(
            onButtonClick = {
                navigateToFeedbackScreen()
            },
            userProfile = "https://media-exp1.licdn.com/dms/image/C4D03AQGn58utIO-x3w/profile-displayphoto-shrink_200_200/0/1637478114039?e=2147483647&v=beta&t=3kIon0YJQNHZojD3Dt5HVODJqHsKdf2YKP1SfWeROnI",
        )
    } else {
        DroidconAppBar(
            onActionClicked = onActionClicked
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    DroidconKE2022Theme {
        HomeScreen()
    }
}