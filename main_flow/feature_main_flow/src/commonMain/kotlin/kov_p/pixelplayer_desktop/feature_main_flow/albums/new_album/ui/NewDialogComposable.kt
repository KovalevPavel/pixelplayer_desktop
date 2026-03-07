package kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kov_p.pixelplayer_desktop.api_tags.TagsManager
import kov_p.pixelplayer_desktop.core_ui.EditDialog
import kov_p.pixelplayer_desktop.core_ui.FullScreenLoader
import kov_p.pixelplayer_desktop.core_ui.RegisterFilePicker
import kov_p.pixelplayer_desktop.core_ui.collectWithLifecycle
import kov_p.pixelplayer_desktop.core_ui.cover.CoverData
import kov_p.pixelplayer_desktop.core_ui.drag_drop.DragDropState
import kov_p.pixelplayer_desktop.core_ui.drag_drop.DraggableItem
import kov_p.pixelplayer_desktop.core_ui.drag_drop.dragContainer
import kov_p.pixelplayer_desktop.core_ui.drag_drop.rememberDragDropState
import kov_p.pixelplayer_desktop.feature_main_flow._di.LocalMainScope
import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.AvailableArtistVs
import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.NewAlbumAction
import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.NewAlbumEvent
import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.NewAlbumViewModel
import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.di.NewAlbumScope
import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.di.newAlbumModule
import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.ui.components.DialogHeader
import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.ui.components.NewTrack
import org.koin.compose.getKoin
import org.koin.compose.koinInject

internal typealias DisksList = List<List<NewAlbumAction.NewTrack>>

private fun DisksList.musicIsReady(): Boolean {
    return this.isNotEmpty() && this.all { d -> d.isNotEmpty() && d.all { it.isFilled } }
}

@Composable
internal fun NewDialog(
    removeFromComposition: (refresh: Boolean) -> Unit,
) {
    val koin = getKoin()

    val scope = remember {
        koin.loadModules(listOf(newAlbumModule))
        koin.createScope<NewAlbumScope>()
    }

    scope.linkTo(LocalMainScope.current)

    val viewModel: NewAlbumViewModel = remember { scope.get() }
    DisposableEffect(Unit) {
        onDispose { scope.close() }
    }

    viewModel.eventsFlow.collectWithLifecycle { event ->
        when (event) {
            is NewAlbumEvent.CloseDialog -> {
                removeFromComposition(true)
            }
        }
    }

    EditDialog(
        title = "New album",
        state = rememberDialogState(size = DpSize(900.dp, 500.dp)),
        resizable = true,
        onClose = { removeFromComposition(false) },
    ) {
        NewDialogContent(
            artists = viewModel.state.artists,
            isLoaderVisible = viewModel.state.isLoaderVisible,
            onAction = viewModel::handleAction,
        )
    }
}

@Composable
internal fun NewDialogContent(
    artists: List<AvailableArtistVs>,
    isLoaderVisible: Boolean,
    onAction: (NewAlbumAction) -> Unit,
) {
    val scope = rememberCoroutineScope { Dispatchers.Default }
    val manager = koinInject<TagsManager>()

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        var selectedArtist: AvailableArtistVs? by remember { mutableStateOf(null) }
        var newAlbumName by rememberSaveable { mutableStateOf("") }
        var cover by rememberSaveable { mutableStateOf<CoverData?>(null) }
        var year by rememberSaveable { mutableIntStateOf(0) }

        var trackPickerVisible by remember { mutableStateOf(false) }
        var disks: DisksList by remember { mutableStateOf(listOf(emptyList())) }

        val isButtonEnabled by remember {
            derivedStateOf {
                selectedArtist != null && newAlbumName.isNotEmpty() && cover != null && year > 0 && disks.musicIsReady()
            }
        }

        DialogHeader(
            selectedArtist = selectedArtist,
            artists = artists,
            albumName = newAlbumName,
            cover = cover,
            albumYear = year.takeIf { it > 0 }?.toString() ?: "",
            onArtistSelect = { selectedArtist = it },
            onAlbumNameChanged = { newAlbumName = it },
            onCoverSelected = { cover = it },
            onYearChanged = {
                val newYear = if (it == "") {
                    0
                } else {
                    it.toIntOrNull()?.takeIf { y -> y > 0 } ?: return@DialogHeader
                }
                year = newYear
            },
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DiskPager(
                disks = disks,
                isPickerVisible = trackPickerVisible,
                onDragAndDrop = { disk, list ->
                    disks = disks.toMutableList().apply { this[disk] = list }
                },
                onAddNewDisk = {
                    disks = disks.toMutableList().apply { add(emptyList()) }
                },
                onRemoveDisk = {
                    disks = disks.toMutableList().apply { removeAt(it) }
                },
                onTracksSelected = { disk, tracks ->
                    scope.launch {
                        val tracksData = tracks.mapIndexed { index, track ->
                            val meta = manager.getTrackMeta(track.path, index == 0)
                            meta?.albumTitle?.let { alb ->
                                if (newAlbumName.isEmpty()) {
                                    newAlbumName = alb
                                }
                            }
                            meta?.cover?.let {
                                if (cover == null) {
                                    cover = CoverData.Binary(bytes = it)
                                }
                            }
                            meta?.year?.let {
                                if (year == 0) {
                                    year = it
                                }
                            }
                            track.copy(title = meta?.title.orEmpty())
                        }

                        disks = disks.toMutableList().apply {
                            this[disk] = this[disk].toMutableList().apply { addAll(tracksData) }
                        }
                    }
                    trackPickerVisible = false
                },
                onTrackChanged = { disk, position, track ->
                    disks = disks.toMutableList().apply {
                        this[disk] = this[disk].mapIndexed { pos, tr ->
                            if (pos == position) track else tr
                        }
                    }
                },
                onTrackRemove = { disk, trackPos ->
                    disks = disks.toMutableList().apply {
                        this[disk] = this[disk].toMutableList().apply { removeAt(trackPos) }
                    }
                },
            )

            Column(
                modifier = Modifier.width(150.dp),
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onAction(
                            NewAlbumAction.CreateAlbum(
                                artistId = selectedArtist?.id ?: return@Button,
                                albumName = newAlbumName,
                                cover = cover ?: return@Button,
                                year = year,
                                disks = disks,
                            ),
                        )
                    },
                    enabled = isButtonEnabled,
                ) {
                    Text(text = "Create")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { trackPickerVisible = true },
                ) {
                    Text(text = "Select tracks")
                }
            }
        }
    }

    if (isLoaderVisible) {
        FullScreenLoader()
    }
}

@Composable
internal fun RowScope.DiskPager(
    disks: DisksList,
    isPickerVisible: Boolean,
    onDragAndDrop: (disk: Int, newList: List<NewAlbumAction.NewTrack>) -> Unit,
    onRemoveDisk: (Int) -> Unit,
    onAddNewDisk: () -> Unit,
    onTracksSelected: (disk: Int, tracks: List<NewAlbumAction.NewTrack>) -> Unit,
    onTrackChanged: (disk: Int, position: Int, track: NewAlbumAction.NewTrack) -> Unit,
    onTrackRemove: (disk: Int, trackPos: Int) -> Unit,
) {
    val pagerState = rememberPagerState { disks.size }
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.weight(1f).fillMaxHeight(),
    ) {
        PrimaryScrollableTabRow(
            modifier = Modifier.wrapContentSize(),
            selectedTabIndex = selectedTab,
        ) {
            repeat(pagerState.pageCount) { pageIndex ->
                Tab(
                    modifier = Modifier.padding(8.dp),
                    selected = pageIndex == selectedTab,
                    onClick = {
                        selectedTab = pageIndex
                        scope.launch {
                            pagerState.animateScrollToPage(pageIndex)
                        }
                    },
                ) {
                    Row(
                        modifier = Modifier.minimumInteractiveComponentSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Disk ${pageIndex + 1}")
                        if (pagerState.pageCount > 1) {
                            IconButton(
                                onClick = {
                                    val newDiskAmount = (disks.size - 1).coerceAtLeast(0)
                                    selectedTab = selectedTab.coerceIn(
                                        minimumValue = 0,
                                        maximumValue = (newDiskAmount - 1).coerceAtLeast(0),
                                    )
                                    onRemoveDisk(pageIndex)
                                },
                            ) {
                                Icon(imageVector = Icons.Default.Remove, contentDescription = null)
                            }
                        }
                    }
                }
            }

            Tab(
                selected = false,
                onClick = onAddNewDisk,
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }

        HorizontalPager(
            modifier = Modifier.weight(1f).fillMaxHeight()
                .border(width = 1.dp, color = Color.Gray, shape = RectangleShape),
            userScrollEnabled = false,
            state = pagerState,
        ) { pageIndex ->
            val dragDropState = rememberDragDropState(listState) { fromIndex, toIndex ->
                val newList = disks[pageIndex].toMutableList().apply { add(toIndex, removeAt(fromIndex)) }
                onDragAndDrop(pageIndex, newList)
            }
            TrackList(
                isPickerVisible = isPickerVisible,
                tracks = disks.getOrNull(pageIndex).orEmpty(),
                listState = listState,
                dragState = dragDropState,
                onTracksSelected = {
                    onTracksSelected(pageIndex, it)
                },
                onChanged = { position, track ->
                    onTrackChanged(pageIndex, position, track)
                },
                onRemoveTrack = { index ->
                    onTrackRemove(pageIndex, index)
                },
            )
        }
    }
}

@Composable
internal fun TrackList(
    isPickerVisible: Boolean,
    listState: LazyListState,
    dragState: DragDropState,
    tracks: List<NewAlbumAction.NewTrack>,
    onTracksSelected: (List<NewAlbumAction.NewTrack>) -> Unit,
    onChanged: (Int, NewAlbumAction.NewTrack) -> Unit,
    onRemoveTrack: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.dragContainer(dragState).fillMaxSize()
            .border(width = 1.dp, color = Color.Gray, shape = RectangleShape),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
    ) {
        itemsIndexed(items = tracks, key = { _, track -> track.path }) { i, track ->
            DraggableItem(
                dragDropState = dragState,
                index = i,
                content = { isDragging ->
                    Card(
                        modifier = Modifier.scale(scale = if (isDragging) 1.04f else 1f),
                        border = BorderStroke(width = 1.dp, color = Color.LightGray),
                    ) {
                        NewTrack(
                            modifier = Modifier.background(color = Color.White).padding(all = 16.dp),
                            position = i + 1,
                            viewState = track,
                            onTitleChanged = { onChanged(i, track.copy(title = it)) },
                            onRemove = { onRemoveTrack(i) },
                        )
                    }
                },
            )
        }
    }

    RegisterFilePicker(
        isVisible = isPickerVisible,
        fileExtensions = listOf("mp3"),
        allowMultiple = true,
        onSelected = { list ->
            list?.map { file ->
                val path = file.path
                val title = path.substringAfterLast("/").substringBeforeLast(".")
                NewAlbumAction.NewTrack(title = title, path = path)
            }?.let { onTracksSelected(it) }
        },
    )
}

@Preview
@Composable
private fun NewDialogPreview() {
    MaterialTheme {
        NewDialogContent(
            artists = listOf(),
            isLoaderVisible = false,
            onAction = {},
        )
    }
}
