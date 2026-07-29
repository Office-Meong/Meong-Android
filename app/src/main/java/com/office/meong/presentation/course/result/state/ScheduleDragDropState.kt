package com.office.meong.presentation.course.result.state

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import com.office.meong.presentation.course.result.model.ScheduleUiModel
import kotlinx.coroutines.channels.Channel

@Composable
fun rememberScheduleDragDropState(
    lazyListState: LazyListState,
    items: SnapshotStateList<ScheduleUiModel>,
    onMove: (fromId: String, toId: String) -> Unit,
): ScheduleDragDropState {
    val currentOnMove = rememberUpdatedState(onMove)
    return remember(lazyListState, items) {
        ScheduleDragDropState(
            lazyListState = lazyListState,
            items = items,
            onMove = { fromId, toId -> currentOnMove.value(fromId, toId) }
        )
    }
}

@Stable
class ScheduleDragDropState internal constructor(
    private val lazyListState: LazyListState,
    private val items: SnapshotStateList<ScheduleUiModel>,
    private val onMove: (fromId: String, toId: String) -> Unit,
) {
    var draggingItemKey by mutableStateOf<String?>(null)
        private set

    private var draggingItemInitialOffset by mutableIntStateOf(0)
    private var draggingItemDraggedDelta by mutableFloatStateOf(0f)
    private var lastMovedToKey: String? = null
    private var lastMovedDraggingItemOffset: Int? = null

    private val scrollChannel = Channel<Float>(capacity = Channel.CONFLATED)

    private val draggingItemLayoutInfo
        get() = lazyListState.layoutInfo.visibleItemsInfo
            .firstOrNull { it.key == draggingItemKey }

    val draggingItemOffset: Float
        get() = draggingItemLayoutInfo?.let { item ->
            draggingItemInitialOffset + draggingItemDraggedDelta - item.offset
        } ?: 0f

    suspend fun consumeScrollRequests(onScroll: suspend (Float) -> Unit) {
        for (diff in scrollChannel) {
            onScroll(diff)
        }
    }

    fun onDragStart(key: String) {
        lazyListState.layoutInfo.visibleItemsInfo
            .firstOrNull { it.key == key }
            ?.also {
                draggingItemKey = key
                draggingItemInitialOffset = it.offset
                draggingItemDraggedDelta = 0f
                lastMovedToKey = null
                lastMovedDraggingItemOffset = null
            }
    }

    fun onDrag(dragAmount: Offset) {
        draggingItemDraggedDelta += dragAmount.y

        val draggingKey = draggingItemKey ?: return
        val draggingItem = draggingItemLayoutInfo ?: return
        val startOffset = draggingItem.offset + draggingItemOffset
        val endOffset = startOffset + draggingItem.size
        val middleOffset = startOffset + (endOffset - startOffset) / 2f

        val targetMatch = lazyListState.layoutInfo.visibleItemsInfo
            .asSequence()
            .mapNotNull { item -> (item.key as? String)?.let { item to it } }
            .firstOrNull { (item, targetId) ->
                targetId != draggingKey &&
                    items.any { it.id == targetId } &&
                    middleOffset.toInt() in item.offset..(item.offset + item.size)
            }

        if (targetMatch != null) {
            val (_, targetKey) = targetMatch
            val isStaleRepeat = targetKey == lastMovedToKey && draggingItem.offset == lastMovedDraggingItemOffset
            if (!isStaleRepeat) {
                onMove(draggingKey, targetKey)
                lastMovedToKey = targetKey
                lastMovedDraggingItemOffset = draggingItem.offset
            }
            return
        }

        val overscroll = when {
            draggingItemDraggedDelta > 0 ->
                (endOffset - lazyListState.layoutInfo.viewportEndOffset).coerceAtLeast(0f)

            draggingItemDraggedDelta < 0 ->
                (startOffset - lazyListState.layoutInfo.viewportStartOffset).coerceAtMost(0f)

            else -> 0f
        }

        if (overscroll != 0f) {
            scrollChannel.trySend(overscroll)
        }
    }

    fun onDragEnd() {
        draggingItemKey = null
        draggingItemDraggedDelta = 0f
        draggingItemInitialOffset = 0
        lastMovedToKey = null
        lastMovedDraggingItemOffset = null
    }
}
