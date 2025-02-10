package com.mydoctor.ramcharacters.ui.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mydoctor.ramcharacters.R
import com.mydoctor.ramcharacters.model.Character
import com.mydoctor.ramcharacters.navigation.NavigationDestination
import com.mydoctor.ramcharacters.ui.theme.RAMCharactersTheme
import com.mydoctor.ramcharacters.utils.testTagDebug
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


/**
 * Destination for Characters screen
 */
object CharactersDestination : NavigationDestination {
    override val route = "characters"
}

@Composable
fun CharactersScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    charactersViewModel: CharactersViewModel = hiltViewModel()
) {
    val characters = charactersViewModel.characters.collectAsLazyPagingItems()
    CharactersScreenUI(
        charactersLazyPagingItems = characters,
        contentPadding = contentPadding,
        modifier = modifier.fillMaxWidth(),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreenUI(
    modifier: Modifier,
    charactersLazyPagingItems: LazyPagingItems<Character>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            Text(
                modifier = Modifier.padding(
                    top = 24.dp,
                    start = 24.dp
                ),
                text = "Characters",
                fontWeight = FontWeight.Bold,
                fontSize = 31.sp,
            )
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = charactersLazyPagingItems.loadState.refresh is LoadState.Loading,
            onRefresh = { charactersLazyPagingItems.refresh() },
            modifier = modifier
        ) {
            val refreshState = charactersLazyPagingItems.loadState.refresh
            if (refreshState is LoadState.Error) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val errorMsg = refreshState.error.localizedMessage ?: ""
                    Text(text = stringResource(
                        R.string.load_error,
                        errorMsg
                    ))
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { charactersLazyPagingItems.retry() }) {
                        Text(text = stringResource(R.string.repeat))
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(top = 24.dp)
                        .testTagDebug("charactersList"),
                ) {
                    items(
                        count = charactersLazyPagingItems.itemCount,
                        key = charactersLazyPagingItems.itemKey { it.id },
                        contentType = charactersLazyPagingItems.itemContentType { it::class.java }
                    ) { index ->
                        charactersLazyPagingItems[index]?.let {
                            CharacterCard(
                                character = it,
                            )
                        }
                    }
                    val isAppendError = charactersLazyPagingItems.loadState.append is LoadState.Error
                    if (isAppendError) {
                        val errorMsg = (charactersLazyPagingItems.loadState.append as LoadState.Error).error.localizedMessage ?: ""
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    stringResource(
                                        R.string.error_loading_additional_data,
                                        errorMsg
                                    ))
                                Button(onClick = { charactersLazyPagingItems.retry() }) {
                                    Text(stringResource(R.string.retry_download))
                                }
                            }
                        }
                    }
                    val endOfPaginationReached =
                        charactersLazyPagingItems.loadState.append.endOfPaginationReached
                    if (charactersLazyPagingItems.itemCount > 0 && !endOfPaginationReached && !isAppendError) {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                    .padding(
                                        bottom = 32.dp
                                    )
                                    .testTagDebug("loadingIndicator")
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterCard(
    character: Character,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                end = 24.dp,
                bottom = 32.dp
            )
    ) {
        val length = when (character.status) {
            "Alive" -> 8
            "Dead" -> 8
            "unknown" -> 6
            else -> 10
        }
        //val (nameString, nameTwoItemString) = character.name.splitStringByLength(length = length)
        //val showNameTwoItem = nameTwoItemString.isNotEmpty()
        val (
            name,
            nameTwoItem,
            status,
            speciesAndGender,
            locationIcon,
            location,
            image,
            episode,
        ) = createRefs()
        CharacterPhotoCard(
            modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    //bottom.linkTo(parent.bottom)
                }
                .size(120.dp),
            image = character.image,
        )
        var visibleCount by remember { mutableIntStateOf(0) }
        //val firstPart = character.name.take(visibleCount)
        val secondPart = character.name.drop(visibleCount)
        val showNameTwoItem = secondPart.isNotEmpty() && visibleCount > 0
        Text(
            modifier = Modifier.constrainAs(name) {
                top.linkTo(image.top)
                start.linkTo(image.end, margin = 18.dp)
                end.linkTo(status.start)
                //bottom.linkTo(nameTwoItem.top)
                bottom.linkTo(
                    if (showNameTwoItem) nameTwoItem.top else speciesAndGender.top
                )
                width = Dimension.fillToConstraints
            },
            text = character.name,
            fontSize = 21.sp,
            lineHeight = 21.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            onTextLayout = { layoutResult ->
                visibleCount = layoutResult.getLineEnd(0)
            }
        )
        if (showNameTwoItem) {
            Text(
                modifier = Modifier.constrainAs(nameTwoItem) {
                    top.linkTo(name.bottom)
                    bottom.linkTo(speciesAndGender.top)
                    start.linkTo(name.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
                text = secondPart,
                fontSize = 21.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Text(
            modifier = Modifier
                .constrainAs(speciesAndGender) {
                    top.linkTo(
                        if (showNameTwoItem) nameTwoItem.bottom else name.bottom
                    )
                    bottom.linkTo(episode.top)
                    start.linkTo(name.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .padding(top = 4.dp),
            text = stringResource(
                R.string.two_items_comma_separated,
                character.species,
                character.gender
            ),
            fontSize = 14.sp,
            lineHeight = 14.sp,
        )
        FilledTonalButton(
            modifier = Modifier.constrainAs(episode) {
                start.linkTo(locationIcon.start)
                top.linkTo(speciesAndGender.bottom, margin = 12.dp)
                bottom.linkTo(locationIcon.top, margin = 10.dp)
            },
            onClick = { /*TODO onClick*/ },
            shape = RoundedCornerShape(17.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x1AFF6B00),
                contentColor = Color(0xFFFF6B00)
            )
        ) {
            Icon(
                modifier = Modifier.height(12.dp),
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "",
            )
            Text(
                modifier = Modifier.padding(start = 6.dp),
                text = stringResource(R.string.watch_episodes),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )
        }
        Icon(
            modifier = Modifier
                .constrainAs(locationIcon) {
                    top.linkTo(episode.bottom)
                    bottom.linkTo(image.bottom)
                    start.linkTo(name.start)
                }
                .height(16.dp),
            imageVector = Icons.Default.LocationOn,
            contentDescription = "",
            tint = Color(0xFF525252)
        )
        Text(
            modifier = Modifier.constrainAs(location) {
                top.linkTo(locationIcon.top)
                bottom.linkTo(locationIcon.bottom)
                start.linkTo(locationIcon.end, margin = 6.dp)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            text = character.locationName,
            fontSize = 14.sp,
            lineHeight = 14.sp,
        )
        Box(
            modifier = Modifier
                .constrainAs(status) {
                    top.linkTo(image.top)
                    end.linkTo(parent.end)
                }
                .clip(RoundedCornerShape(25.dp))
                .background(color = Color(0xFFC7FFB9)),
            contentAlignment = Center
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                text = character.status.uppercase(),
                fontSize = 14.sp,
                lineHeight = 14.sp,
                color = Color(0xFF319F16)
            )
        }
    }
}

@Composable
fun CharacterPhotoCard(
    image: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(40.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current).data(image)
                .crossfade(true).build(),
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.character_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CharactersScreenPreview() {
    RAMCharactersTheme {
        val charactersList = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                gender = "Male",
                locationName = "Earth",
                locationUrl = "https://rickandmortyapi.com/api/location/1",
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                episode = listOf("https://rickandmortyapi.com/api/episode/1"),
            ),
            Character(
                id = 2,
                name = "Morty Smith Junior",
                status = "Alive",
                species = "Human",
                gender = "Male",
                locationName = "Earth",
                locationUrl = "https://rickandmortyapi.com/api/location/1",
                image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                episode = listOf("https://rickandmortyapi.com/api/episode/1"),
            )
        )

        val pagingData: Flow<PagingData<Character>> = flowOf(PagingData.from(charactersList))
        val lazyPagingItems: LazyPagingItems<Character> =
            pagingData.collectAsLazyPagingItems()

        CharactersScreenUI(
            modifier = Modifier,
            charactersLazyPagingItems = lazyPagingItems,
        )
    }
}