package com.office.meong.core.model.trigger

import androidx.compose.runtime.Stable
import com.office.meong.core.navigation.Route

@Stable
class GlobalUiEventHolder(
    val dialogTrigger: DialogTrigger,
    val showToast: (String) -> Unit,
    val showSnackbar: (SnackbarState) -> Unit,
)
