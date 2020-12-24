package ru.itrequest.util


/**
 * We try to implement an enum Result like in a swift
 * see https://medium.com/@KaneCheshire/recreating-swifts-result-type-in-kotlin-f0a065fa6af1
 */
sealed class Result<out S, out F: Throwable> {
    data class Success<out S>(val success: S) : Result<S, Nothing>()
    data class Failure<out F: Throwable>(val error: F) : Result<Nothing, F>()
}
