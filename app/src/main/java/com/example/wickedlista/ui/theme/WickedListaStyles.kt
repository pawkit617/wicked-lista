package com.example.wickedlista.ui.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.animate
import androidx.compose.foundation.style.contentPadding
import androidx.compose.foundation.style.externalPadding
import androidx.compose.foundation.style.fillWidth
import androidx.compose.foundation.style.pressed
import androidx.compose.foundation.style.scale
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp


val commonShadow = Shadow(
    radius = 8.dp,
    spread = 1.dp,
    offset = DpOffset(0.dp, 2.dp),
    color = Color.Black
)

val commonIconButtonShadow = Shadow(
    radius = 4.dp,
    spread = (-4).dp,
    offset = DpOffset(0.dp, 4.dp),
    color = Color.Black
)

val commonIconButtonPressedShadow = Shadow(
    radius = 4.dp,
    spread = (-6).dp,
    offset = DpOffset(0.dp, 0.dp),
    color = Color.Black
)

@Composable
fun GetBoxAsDropShadow() = Box( //This is only a shadow to show depth b/w list content and button row
    modifier = Modifier
        .fillMaxWidth()
        .height(2.dp)
        .dropShadow(
            RectangleShape, commonShadow
        )
)

@OptIn(ExperimentalFoundationStyleApi::class)
fun getHomeCardStyle(
    myShape: CornerBasedShape,
    backgroundColor: Color,
    onPressedColor: Color
) = Style {
    fillWidth()
    shape(myShape)
    externalPadding(start = 0.dp, top= 8.dp, end = 0.dp, bottom = 0.dp)
    dropShadow(commonShadow)
    background(backgroundColor)
    pressed {
        animate(tween(durationMillis = 500)) {
            background(Brush.radialGradient(
                    colors = listOf(
                        onPressedColor,
                        Color.White.copy(alpha = 0.6f),
                        onPressedColor
                    ),
                    center = Offset.Zero,
                    radius = 500f
                )
            )
            scale(.95f)
        }
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
fun getCardListItemStyle() = Style {
    fillWidth()
    shape(RoundedCornerShape(10.dp))
    externalPadding(start = 0.dp, top= 8.dp, end = 0.dp, bottom = 8.dp)
    dropShadow(commonShadow)
}

@OptIn(ExperimentalFoundationStyleApi::class)
fun getCardForModifyItemFormStyle(
    containerColor: Color,
    myShape: Shape,
    topExternalPadding: Dp = 0.dp
) = Style {
    fillWidth()
    shape(myShape)
    externalPadding(0.dp, topExternalPadding, 0.dp, 0.dp)
    background(containerColor)
    dropShadow(commonShadow)

    animate {
        scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow), initialScale = .20f)
    }

}



@OptIn(ExperimentalFoundationStyleApi::class)
fun getDialogStyle(backgroundColor: Color) = Style {
    background(backgroundColor)
    contentPadding(12.dp)
    fillWidth()
}
@OptIn(ExperimentalFoundationStyleApi::class)
fun getButtonStyle(contentColor: Color,
                   backgroundColor: Color,
                   pressedColor: Color,
                   pressedContentColor :Color,
                   myShape: CornerBasedShape,
                   buttonWidth: Dp?): Style {
    val buttonStyle = Style {
        background(backgroundColor)
        shape(myShape)

        contentPadding(20.dp, 16.dp)
        contentColor(contentColor)
        fontWeight(FontWeight.Bold)
        dropShadow(commonShadow)
        buttonWidth?.apply { width(this) }
        pressed {
            background(pressedColor)
            contentColor(pressedContentColor)
            innerShadow(commonShadow)
//            animate(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) {
//                scale(1.2f)
//            }
        }
    }

    return buttonStyle
}

@OptIn(ExperimentalFoundationStyleApi::class)
fun getCommonTextFieldStyle(
    shapeOfTextField : Shape,
    fontSize : TextUnit,
    backgroundColor: Color,
    contentColor: Color,
    innerShadowColor: Color = Color.Black,
    height: Dp = 50.dp
) = Style {
    contentPadding(start = 8.dp, 0.dp, 0.dp, 0.dp)
    fillWidth()
    background(backgroundColor)
    contentColor(contentColor)
    height(height)
    shape(shapeOfTextField)
    innerShadow(Shadow(
        radius = 4.dp,
        spread = 0.dp,
        offset = DpOffset(0.dp, 2.dp),
        color = innerShadowColor,
    ))
    fontSize(fontSize)
}
