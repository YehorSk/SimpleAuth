package com.example.simpleauth.ui.screens.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simpleauth.todo.data.model.Todo

@Composable
fun TodoCard(todo: Todo, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,

        ) {
            Column(
                modifier = Modifier.weight(1f).padding(top = 10.dp, start = 10.dp, bottom = 10.dp),
            ) {
                Text(
                    text = todo.description
                )
            }
            Box(
                modifier = Modifier
                    .width(15.dp)
                    .fillMaxHeight()
                    .background(priorityColor(todo.priority))
            )
        }

    }
}

@Preview
@Composable
fun TodoCardPreview(){

}

fun priorityColor(priority: Int): Color{
    return when(priority){
        1 -> Color.Green
        2 -> Color.Yellow
        3 -> Color.Red
        else -> Color.Gray
    }
}

