package com.folkmanis.laiks.ui.screens.users

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.ui.theme.LaiksTheme

@Composable
fun UserRow(
    user: LaiksUser,
    onEdit: (LaiksUser)-> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier
            .clickable { onEdit(user) },
        headlineContent = {
            Text(text = user.name)
        },
        supportingContent = {
            Text(text = user.email)
        },
        leadingContent = {
            AvatarLabel(letter = user.name.slice(0..1))
        }
    )
}

@Composable
fun AvatarLabel(
    letter: String,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = MaterialTheme.colorScheme.primaryContainer
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(40.dp)
            .background(
                color = backgroundColor,
                shape = CircleShape
            )
    ) {
        Text(
            text = letter,
            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
            color = contentColorFor(backgroundColor),
        )
    }
}

@Preview
@Composable
fun UserRowPreview() {
    LaiksTheme {
        UserRow(
            user = LaiksUser(
                name = "User User",
                email = "user@example.org"
            ),
            onEdit = {}
        )
    }
}