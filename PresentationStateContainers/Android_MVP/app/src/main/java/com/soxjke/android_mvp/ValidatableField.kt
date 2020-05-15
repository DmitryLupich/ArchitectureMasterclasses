package com.soxjke.android_mvp

data class ValidatableField<ErrorType: Throwable>(
    var error: ErrorType? = null,
    var text: String = "",
    val startEditing: ((String) -> Unit)? = null,
    val changedEditing: ((String) -> Unit)? = null,
    val endEditing: ((String) -> Unit)? = null
)