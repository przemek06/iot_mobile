package edu.pwr.iotmobile.androidimcs.ui.navigation

import androidx.navigation.NavBackStackEntry

private const val DIVIDER = "|"

fun String.appendArguments(
    vararg arguments: Any
): String {
    val pathWithoutArgument = this.split("/").getOrNull(0) ?: return this
    return if (arguments.size == 1) {
        pathWithoutArgument.plus("/${arguments[0]}")
    } else if (arguments.size > 1) {
        var newPath = pathWithoutArgument.plus("/${arguments[0]}")
        arguments.forEachIndexed { index, arg ->
            if (index != 0) {
                newPath = newPath.plus(DIVIDER).plus(arg)
            }
        }
        newPath
    }
    else this
}

fun NavBackStackEntry.getArguments(): List<String> {
    val arg = arguments?.getString("arguments")
    return arg?.split(DIVIDER) ?: emptyList()
}