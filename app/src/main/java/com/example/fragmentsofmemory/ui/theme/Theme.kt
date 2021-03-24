package com.example.fragmentsofmemory.ui.theme

import android.graphics.LightingColorFilter
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import com.example.fragmentsofmemory.UiModel


/*
@Composable
fun MyComposeExampleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColors()
    } else {
        lightColors()
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}


@Stable
object MyTheme {
    enum class Theme {
        Light, Dark
    }
}*/

/*
@Composable
fun myLightColors(
    primary: State<Color> = animateColorAsState(Color(0xFF4F7DA1), TweenSpec(1600)),
    primaryVariant: State<Color> = animateColorAsState(Color(0xFF476194), TweenSpec(1600)),
    secondary: State<Color> = animateColorAsState(Color(0xFF03DAC6),TweenSpec(1600)),
    secondaryVariant: State<Color> = animateColorAsState(Color(0xFF018786), TweenSpec(1600)),

    background: State<Color> = animateColorAsState(Color(0xFF85B3CC), TweenSpec(1600)),
    surface: State<Color> = animateColorAsState(Color.White, TweenSpec(1600)),
    error: State<Color> = animateColorAsState(Color(0xFFB00020), TweenSpec(1600)),
    onPrimary: State<Color> = animateColorAsState(Color.White, TweenSpec(1600)),
    onSecondary: State<Color> = animateColorAsState(Color.Black, TweenSpec(1600)),
    onBackground: State<Color> = animateColorAsState(Color.Black, TweenSpec(1600)),
    onSurface: State<Color> = animateColorAsState(Color.Black, TweenSpec(1600)),
    onError: State<Color> = animateColorAsState(Color.White, TweenSpec(1600)),

    ): Colors = Colors(
    primary.value,
    primaryVariant.value,
    secondary.value,
    secondaryVariant.value,
    background.value,
    surface.value,
    error.value,
    onPrimary.value,
    onSecondary.value,
    onBackground.value,
    onSurface.value,
    onError.value,
    true
)*/

fun myDarkColors(
    primary: Color = Color(0xFF202C3A),
    primaryVariant: Color = Color(0xFF3700B3),
    secondary: Color = Color(0xFF66A9E0),
    secondaryVariant: Color = secondary,
    background: Color = Color(0xFF121212),
    surface: Color = Color(0xFF1B232E),
    error: Color = Color(0xFFCF6679),
    onPrimary: Color = Color.Black,
    onSecondary: Color = Color.Black,
    onBackground: Color = Color.White,
    onSurface: Color = Color.White,
    onError: Color = Color.Black
): Colors = Colors(
    primary,
    primaryVariant,
    secondary,
    secondaryVariant,
    background,
    surface,
    error,
    onPrimary,
    onSecondary,
    onBackground,
    onSurface,
    onError,
    false
)

fun mylightColors(
    primary: Color = Color(0xFF507EA2),
    primaryVariant: Color = Color(0xFF467194),
    secondary: Color = Color(0xFF66A9E0),
    secondaryVariant: Color = Color(0xFF018786),
    background: Color = Color(90,143,185),
    surface: Color = Color.White,
    error: Color = Color(0xFFB00020),
    onPrimary: Color = Color.White,
    onSecondary: Color = Color.White,
    onBackground: Color = Color.Black,
    onSurface: Color = Color.Black,
    onError: Color = Color.White
): Colors = Colors(
    primary,
    primaryVariant,
    secondary,
    secondaryVariant,
    background,
    surface,
    error,
    onPrimary,
    onSecondary,
    onBackground,
    onSurface,
    onError,
    true
)



@Composable
fun MyTheme(viewModel:UiModel,content: @Composable() () -> Unit) {

    MaterialTheme(
        content = content,
        colors = if(viewModel.lightTheme) {
            mylightColors()
        } else myDarkColors()
    )

}
