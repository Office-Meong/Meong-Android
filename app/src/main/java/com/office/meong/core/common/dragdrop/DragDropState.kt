package com.office.meong.core.common.dragdrop

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
import kotlinx.coroutines.channels.Channel

@Composable
fun <T : Any> rememberDragDropState(
    lazyListState: LazyListState,
    items: SnapshotStateList<T>,
    key: (T) -> Any,
    onMove: (fromKey: Any, toKey: Any) -> Unit,
): DragDropState<T> {
    val currentKey by rememberUpdatedState(key)
    val currentOnMove = rememberUpdatedState(onMove)
    return remember(lazyListState, items) {
        DragDropState(
            lazyListState = lazyListState,
            items = items,
            key = { currentKey(it) },
            onMove = { fromKey, toKey -> currentOnMove.value(fromKey, toKey) }
        )
    }
}

/**
 * [LazyListState]에 있는 아이템을 드래그로 재정렬할 수 있게 해주는 범용 상태 홀더.
 *
 * 아이템 타입이나 도메인에 종속되지 않으며, [key]로 각 아이템을 식별한다.
 * */
@Stable
class DragDropState<T : Any>(
    private val lazyListState: LazyListState,
    private val items: SnapshotStateList<T>,
    private val key: (T) -> Any,
    private val onMove: (fromKey: Any, toKey: Any) -> Unit,
) {
    var draggingItemKey by mutableStateOf<Any?>(null)
        private set

    private var draggingItemInitialOffset by mutableIntStateOf(0)
    private var draggingItemDraggedDelta by mutableFloatStateOf(0f)
    private var lastMovedToKey: Any? = null
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

    fun onDragStart(key: Any) {
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
            .mapNotNull { item -> item.key?.let { item to it } }
            .firstOrNull { (item, targetKey) ->
                targetKey != draggingKey &&
                    items.any { key(it) == targetKey } &&
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
